package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class RemoteDataFirstStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO), liveData: MediatorLiveData<DataWrapper<T>> = MediatorLiveData(), debug: Boolean = false) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, remoteScope = remoteScope, liveData = liveData, debug = debug) {

    override fun start(): Job = askRemote()

    private fun askRemote() = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.FETCHING, localData = false, strategy = this@RemoteDataFirstStrategy::class))
                val task = withContext(remoteScope.coroutineContext) { fetchData() }
                val data = task.await()
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = false, strategy = this@RemoteDataFirstStrategy::class))

                withContext(localScope.coroutineContext) { writeData(data) }
            } catch (error: Throwable) {
                if (debug) {
                    error.printStackTrace()
                }
                askLocal(error)
            }
        } else {
            askLocal(IllegalStateException("Remote data is not available"))
        }
    }

    private fun askLocal(warning: Throwable) = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, warning = warning, strategy = this@RemoteDataFirstStrategy::class))
                val task = withContext(localScope.coroutineContext) { readData() }
                val data = task.await()
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true, warning = warning, strategy = this@RemoteDataFirstStrategy::class))
            } catch (error: Throwable) {
                if (debug) {
                    error.printStackTrace()
                }
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, warning = warning, strategy = this@RemoteDataFirstStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Local data is not available"), status = DataStatus.INVALID, localData = true, warning = warning, strategy = this@RemoteDataFirstStrategy::class))
        }
    }

    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>

    @WorkerThread
    abstract suspend fun readData(): Deferred<T>

    @WorkerThread
    abstract suspend fun writeData(data: T)
}
