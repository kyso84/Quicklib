package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.*

abstract class LocalDataAwareStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), debug: Boolean = false) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, debug = debug) {

    override fun start(): Job = askLocal()

    // https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
    private fun askLocal() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper<T>(status = DataStatus.LOADING, localData = true))
                val task: Deferred<LiveData<T>> = withContext(localScope.coroutineContext) { readData() }
                val data: LiveData<T> = task.await()
                CoroutineScope(Dispatchers.Main).launch {
                    liveData.addSource(data) { value ->
                        liveData.postValue(DataWrapper<T>(value = value, status = DataStatus.SUCCESS, localData = true))
                    }
                }
            } catch (error: Throwable) {
                if (debug) {
                    error.printStackTrace()
                }
                liveData.postValue(DataWrapper<T>(error = error, status = DataStatus.ERROR, localData = true))
            }
        } else {
            liveData.value = DataWrapper(error = IllegalStateException("Local value is not available"), status = DataStatus.ERROR, localData = true)
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun readData(): Deferred<LiveData<T>>
}
