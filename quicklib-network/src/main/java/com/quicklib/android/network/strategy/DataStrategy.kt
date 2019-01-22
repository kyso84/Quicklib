package com.quicklib.android.network.strategy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive

abstract class DataStrategy<T>(val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main), val localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), val remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO), protected val liveData: MediatorLiveData<DataWrapper<T>> = MediatorLiveData(), var debug: Boolean = false) {

    private val job = start()

    fun cancel() {
        if (mainScope.isActive) {
            mainScope.coroutineContext.cancel()
        }
        if (localScope.isActive) {
            localScope.coroutineContext.cancel()
        }
        if (remoteScope.isActive) {
            remoteScope.coroutineContext.cancel()
        }
        if (job.isActive) {
            job.cancel()
        }
    }

    fun asLiveData(): LiveData<DataWrapper<T>> = liveData
    protected abstract fun start(): Job
}