package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class LocalDataAwareFirstStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO), liveData: MediatorLiveData<DataWrapper<T>> = MediatorLiveData(), debug: Boolean = false) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, remoteScope = remoteScope, liveData = liveData, debug = debug) {

    override fun start(): Job = askLocal()

    private fun askLocal() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, strategy = this@LocalDataAwareFirstStrategy::class))
                val task: Deferred<LiveData<T>> = withContext(localScope.coroutineContext) { readData() }
                val data: LiveData<T> = task.await()

                CoroutineScope(Dispatchers.Main).launch {
                    liveData.addSource(data) { value ->
                        liveData.postValue(DataWrapper(value = value, status = DataStatus.SUCCESS, localData = true, strategy = this@LocalDataAwareFirstStrategy::class))
                    }
                }
                askRemote()
            } catch (error: Throwable) {
                if (debug) {
                    error.printStackTrace()
                }
                askRemote(warning = error)
            }
        } else {
            askRemote(warning = IllegalStateException("Local value is not available"))
        }
    }

    private fun askRemote(warning: Throwable? = null) = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.FETCHING, localData = false, warning = warning, strategy = this@LocalDataAwareFirstStrategy::class))
                val task = withContext(remoteScope.coroutineContext) { fetchData() }
                val data = task.await()
                withContext(localScope.coroutineContext) { writeData(data) }
            } catch (error: Throwable) {
                if (debug) {
                    error.printStackTrace()
                }
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = false, warning = warning, strategy = this@LocalDataAwareFirstStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Remote value is not available"), status = DataStatus.INVALID, localData = false, warning = warning, strategy = this@LocalDataAwareFirstStrategy::class))
        }
    }

    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>

    @WorkerThread
    abstract suspend fun readData(): Deferred<LiveData<T>>

    @WorkerThread
    abstract suspend fun writeData(data: T)
}
