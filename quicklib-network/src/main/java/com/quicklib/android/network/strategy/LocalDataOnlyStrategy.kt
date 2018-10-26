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

abstract class LocalDataOnlyStrategy<T> : DataStrategy<T>() {

    override fun start(): Job = askLocal()

    // https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
    private fun askLocal() = GlobalScope.launch(fgContext, CoroutineStart.LAZY) {
        try {
            liveData.value = DataWrapper(status = DataStatus.LOADING, localData = true)
            val task = withContext(bgContext) { readData() }
            val data = task.await()
            liveData.value = DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true)
        } catch (error: Throwable) {
            liveData.value = DataWrapper(error = error, status = DataStatus.ERROR, localData = true)
        }
    }

    @WorkerThread
    abstract suspend fun readData(): Deferred<T>
}
