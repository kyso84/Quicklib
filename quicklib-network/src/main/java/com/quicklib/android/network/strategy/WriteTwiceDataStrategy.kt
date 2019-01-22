package com.quicklib.android.network.strategy
import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.*

abstract class WriteTwiceDataStrategy<T>(val value: T, mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), remoteScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined), liveData: MediatorLiveData<DataWrapper<T>> = MediatorLiveData(), debug: Boolean = false) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, liveData = liveData, debug = debug) {

    override fun start() = writeTwice()

    private fun writeTwice() = mainScope.launch {
        try {
            val task1 = withContext(localScope.coroutineContext) { writeDataLocal(value) }
            val data1 = task1.await()
            val task2 = withContext(remoteScope.coroutineContext) { writeDataRemote(value) }
            val data2 = task2.await()
            liveData.postValue(DataWrapper(value = data1, status = DataStatus.SUCCESS, localData = true, strategy = this@WriteTwiceDataStrategy::class))
        } catch (error: Throwable) {
            if (debug) {
                error.printStackTrace()
            }
            liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@WriteTwiceDataStrategy::class))
        }
    }


    @WorkerThread
    abstract suspend fun writeDataLocal(data: T): Deferred<T>

    @WorkerThread
    abstract suspend fun writeDataRemote(data: T): Deferred<T>
}
