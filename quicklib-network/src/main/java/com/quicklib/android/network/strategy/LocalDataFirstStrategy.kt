package com.quicklib.android.network.strategy

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext

abstract class LocalDataFirstStrategy<T>(val mainContext: CoroutineContext = Dispatchers.Main, val localContext: CoroutineContext = Dispatchers.IO, val remoteContext: CoroutineContext = Dispatchers.IO) : DataStrategy<T>() {

    override fun start(): Job = askLocal()

    private fun askLocal() = GlobalScope.launch(mainContext, CoroutineStart.DEFAULT) {
        try {
            liveData.value = DataWrapper(status = DataStatus.LOADING, localData = true)
            val task = withContext(localContext) { readData() }
            val data = task.await()
            if (isValid(data)) {
                liveData.value = DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true)
            } else {
                askRemote(IllegalArgumentException("local data is not valid"))
            }
        } catch (error: Throwable) {
            askRemote(error)
        }
    }

    private fun askRemote(warning: Throwable) = GlobalScope.launch(mainContext, CoroutineStart.DEFAULT) {
        try {
            liveData.value = DataWrapper(status = DataStatus.FETCHING, localData = false, warning = warning)
            val task = withContext(remoteContext) { fetchData() }
            val data = task.await()
            liveData.value = DataWrapper(value = data, status = DataStatus.SUCCESS, localData = false, warning = warning)

            withContext(localContext) { writeData(data) }
        } catch (error: Throwable) {
            liveData.value = DataWrapper(error = error, status = DataStatus.ERROR, localData = false, warning = warning)
        }
    }

    @UiThread
    abstract suspend fun isValid(data: T): Boolean

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>

    @WorkerThread
    abstract suspend fun readData(): Deferred<T>

    @WorkerThread
    abstract suspend fun writeData(data: T)
}
