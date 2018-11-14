package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
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

abstract class RemoteDataFirstStrategy<T>(val mainContext: CoroutineContext = Dispatchers.Main, val localContext: CoroutineContext = Dispatchers.IO, val remoteContext: CoroutineContext = Dispatchers.IO) : DataStrategy<T>() {

    override fun start(): Job = askRemote()

    private fun askRemote() = GlobalScope.launch(mainContext, CoroutineStart.DEFAULT) {
        if (isRemoteAvailable()) {
            try {
                liveData.value = DataWrapper(status = DataStatus.FETCHING, localData = false)
                val task = withContext(remoteContext) { fetchData() }
                val data = task.await()
                liveData.value = DataWrapper(value = data, status = DataStatus.SUCCESS, localData = false)

                withContext(localContext) { writeData(data) }
            } catch (error: Throwable) {
                askLocal(error)
            }
        } else {
            askLocal(IllegalStateException("Remote data is not available"))
        }
    }

    private fun askLocal(warning: Throwable) = GlobalScope.launch(mainContext, CoroutineStart.DEFAULT) {
        if (isLocalAvailable()) {
            try {
                liveData.value = DataWrapper(status = DataStatus.LOADING, localData = true, warning = warning)
                val task = withContext(localContext) { readData() }
                val data = task.await()
                liveData.value = DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true, warning = warning)
            } catch (error: Throwable) {
                liveData.value = DataWrapper(error = error, status = DataStatus.ERROR, localData = true, warning = warning)
            }
        } else {
            liveData.value = DataWrapper(error = IllegalStateException("Local data is not available"), status = DataStatus.ERROR, localData = true, warning = warning)
        }
    }

    @MainThread
    open fun isRemoteAvailable(): Boolean = true

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>

    @WorkerThread
    abstract suspend fun readData(): Deferred<T>

    @WorkerThread
    abstract suspend fun writeData(data: T)
}
