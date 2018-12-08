package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.*

abstract class LocalDataOnlyStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) : DataStrategy<T>(mainScope = mainScope, localScope = localScope) {

    override fun start(): Job = askLocal()

    // https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
    private fun askLocal() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper<T>(status = DataStatus.LOADING, localData = true))
                val task = withContext(localScope.coroutineContext) { readData() }
                val data = task.await()
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true))
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper<T>(error = error, status = DataStatus.ERROR, localData = true))
            }
        } else {
            liveData.value = DataWrapper(error = IllegalStateException("Local value is not available"), status = DataStatus.ERROR, localData = true)
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun readData(): Deferred<T>
}
