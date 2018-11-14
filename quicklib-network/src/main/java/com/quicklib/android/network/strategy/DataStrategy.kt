package com.quicklib.android.network.strategy

import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancel
import kotlinx.coroutines.experimental.isActive

abstract class DataStrategy<T>(val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main), val localScope: CoroutineScope = CoroutineScope(Dispatchers.IO), val remoteScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {

    val liveData = MutableLiveData<DataWrapper<T>>()
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

    protected abstract fun start(): Job
}