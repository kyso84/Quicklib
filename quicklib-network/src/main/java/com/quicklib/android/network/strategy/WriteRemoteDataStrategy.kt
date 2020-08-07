package com.quicklib.android.network.strategy
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class WriteRemoteDataStrategy<T>(val value: T, mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), liveData: MediatorLiveData<DataWrapper<T>> = MediatorLiveData()) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, liveData = liveData) {

    override fun start() = writeRemote()

    private fun writeRemote() = mainScope.launch {
        if (isRemoteAvailable()) {
            try {
                val task = withContext(remoteScope.coroutineContext) { writeDataRemote(value) }
                val data = task.await()

                localScope.launch {
                    dataSaved(data)
                }
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true, strategy = this@WriteRemoteDataStrategy::class))
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@WriteRemoteDataStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(value = value, error = IllegalStateException("Remote value is not available"), status = DataStatus.INVALID, localData = false, strategy = this@WriteRemoteDataStrategy::class))
        }
    }

    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun writeDataRemote(data: T): Deferred<T>

    @WorkerThread
    open fun dataSaved(data: T) {}
}
