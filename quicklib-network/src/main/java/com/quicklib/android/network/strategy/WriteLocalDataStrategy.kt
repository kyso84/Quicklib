package com.quicklib.android.network.strategy
import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class WriteLocalDataStrategy<T>(val value: T, mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), liveData: MediatorLiveData<DataWrapper<T>> = MediatorLiveData(), debug: Boolean = false) : DataStrategy<T>(mainScope = mainScope, localScope = localScope, liveData = liveData, debug = debug) {

    override fun start() = writeLocal()

    private fun writeLocal() = mainScope.launch {
        try {
            val task = withContext(localScope.coroutineContext) { writeData(value) }
            val data = task.await()
            liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true, strategy = this@WriteLocalDataStrategy::class))
        } catch (error: Throwable) {
            if (debug) {
                error.printStackTrace()
            }
            liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@WriteLocalDataStrategy::class))
        }
    }

    @WorkerThread
    abstract suspend fun writeData(data: T): Deferred<T>
}
