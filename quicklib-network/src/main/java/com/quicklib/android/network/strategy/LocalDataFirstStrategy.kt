package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.*

abstract class LocalDataFirstStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, remoteScope = remoteScope) {

    override fun start(): Job = askLocal()

    private fun askLocal() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true))
                val task = withContext(localScope.coroutineContext) { readData() }
                val data = task.await()
                if (isValid(data)) {
                    liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true))
                } else {
                    askRemote(value = data, warning = IllegalArgumentException("local value is not valid"))
                }
            } catch (error: Throwable) {
                askRemote(warning = error)
            }
        } else {
            askRemote(warning = IllegalStateException("Local value is not available"))
        }
    }

    private fun askRemote(warning: Throwable, value: T? = null) = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.FETCHING, localData = false, warning = warning))
                val task = withContext(remoteScope.coroutineContext) { fetchData() }
                val data = task.await()
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = false, warning = warning))

                withContext(localScope.coroutineContext) { writeData(data) }
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = false, warning = warning))
            }
        } else {
            liveData.postValue(DataWrapper(value = value, error = IllegalStateException("Remote value is not available"), status = DataStatus.INVALID, localData = false, warning = warning))
        }
    }

    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @MainThread
    abstract suspend fun isValid(data: T): Boolean

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>

    @WorkerThread
    abstract suspend fun readData(): Deferred<T>

    @WorkerThread
    abstract suspend fun writeData(data: T)
}
