package com.quicklib.android.network.strategy

import androidx.annotation.WorkerThread
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

abstract class RemoteDataOnlyStrategy<T> : DataStrategy<T>() {

    override fun start(): Job = askRemote()

    private fun askRemote() = GlobalScope.launch(fgContext, CoroutineStart.DEFAULT) {
        try {
            liveData.value = DataWrapper(status = DataStatus.FETCHING, localData = false)
            val task = withContext(bgContext) { fetchData() }
            val data = task.await()
            liveData.value = DataWrapper(value = data, status = DataStatus.SUCCESS, localData = false)
        } catch (error: Throwable) {
            liveData.value = DataWrapper(error = error, status = DataStatus.ERROR, localData = false)
        }
    }

    @WorkerThread
    abstract suspend fun fetchData(): Deferred<T>
}
