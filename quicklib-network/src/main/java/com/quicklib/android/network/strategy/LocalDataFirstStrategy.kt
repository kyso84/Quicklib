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

abstract class LocalDataFirstStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO), liveData: MediatorLiveData<DataWrapper<T>> = MediatorLiveData(), debug: Boolean = false) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, remoteScope = remoteScope, liveData = liveData, debug = debug) {

    override fun start(): Job = askLocal()

    private fun askLocal() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, strategy = this@LocalDataFirstStrategy::class))
                val task = withContext(localScope.coroutineContext) { readData() }
                val data = task.await()
                if (isValid(data)) {
                    liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true, strategy = this@LocalDataFirstStrategy::class))
                } else {
                    askRemote(value = data, warning = IllegalArgumentException("local value is not valid"))
                }
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

    private fun askRemote(warning: Throwable, value: T? = null) = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.FETCHING, localData = false, warning = warning, strategy = this@LocalDataFirstStrategy::class))
                val task = withContext(remoteScope.coroutineContext) { fetchData() }
                val data = task.await()
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = false, warning = warning, strategy = this@LocalDataFirstStrategy::class))

                withContext(localScope.coroutineContext) { writeData(data) }
            } catch (error: Throwable) {
                if (debug) {
                    error.printStackTrace()
                }
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = false, warning = warning, strategy = this@LocalDataFirstStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(value = value, error = IllegalStateException("Remote value is not available"), status = DataStatus.INVALID, localData = false, warning = warning, strategy = this@LocalDataFirstStrategy::class))
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
