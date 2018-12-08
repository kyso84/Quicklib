package com.quicklib.android.network.strategy
import androidx.annotation.WorkerThread
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.*

abstract class WriteLocalDataStrategy<T>(val value: T, mainScope: CoroutineScope = CoroutineScope(Dispatchers.Default), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) : DataStrategy<T>(mainScope = mainScope, localScope = localScope) {

    override fun start() = writeLocal()

    private fun writeLocal() = mainScope.launch {
        try {
            val task = withContext(localScope.coroutineContext) { writeData(value) }
            val data = task.await()
            liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true))
        } catch (error: Throwable) {
            liveData.postValue(DataWrapper<T>(error = error, status = DataStatus.ERROR, localData = true))
        }
    }

    @WorkerThread
    abstract suspend fun writeData(data: T): Deferred<T>
}
