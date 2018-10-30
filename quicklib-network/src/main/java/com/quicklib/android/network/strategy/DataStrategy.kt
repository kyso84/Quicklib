package com.quicklib.android.network.strategy

import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.experimental.Job

abstract class DataStrategy<T> {

    val liveData = MutableLiveData<DataWrapper<T>>()
    private val job: Job = start()

    fun cancel() {
        job.cancel()
    }

    protected abstract fun start(): Job
}