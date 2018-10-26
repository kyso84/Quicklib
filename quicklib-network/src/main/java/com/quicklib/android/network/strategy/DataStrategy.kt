package com.quicklib.android.network.strategy

import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlin.coroutines.experimental.CoroutineContext

abstract class DataStrategy<T>() {

    protected val bgContext: CoroutineContext = Dispatchers.IO
    protected val fgContext: CoroutineContext = Dispatchers.Main

    val liveData = MutableLiveData<DataWrapper<T>>()
    val job: Job = start()

    fun cancel() {
        job.cancel()
    }

    abstract fun start(): Job
}