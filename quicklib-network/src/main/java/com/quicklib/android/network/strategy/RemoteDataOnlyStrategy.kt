package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.*

abstract class RemoteDataOnlyStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) : DataStrategy<T>(mainScope = mainScope, remoteScope = remoteScope) {

    override fun start(): Job = askRemote()

    private fun askRemote() = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                liveData.value = DataWrapper(status = DataStatus.FETCHING, localData = false)
                val task = withContext(remoteScope.coroutineContext) { fetchData() }
                val data = task.await()
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = false))
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = false))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Remote data is not available"), status = DataStatus.ERROR, localData = false))
        }
    }

    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>
}
