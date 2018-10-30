package com.quicklib.android.network.strategy

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

abstract class RemoteDataOnlyStrategy<T>(val mainContext: CoroutineContext = Dispatchers.Main, val remoteContext: CoroutineContext = Dispatchers.IO) : DataStrategy<T>() {

    override fun start(): Job = askRemote()

    private fun askRemote() = GlobalScope.launch(mainContext, CoroutineStart.DEFAULT) {
        try {
            liveData.value = DataWrapper(status = DataStatus.FETCHING, localData = false)
            val task = withContext(remoteContext) { fetchData() }
            val data = task.await()
            liveData.value = DataWrapper(value = data, status = DataStatus.SUCCESS, localData = false)
        } catch (error: Throwable) {
            liveData.value = DataWrapper(error = error, status = DataStatus.ERROR, localData = false)
        }
    }

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>
}
