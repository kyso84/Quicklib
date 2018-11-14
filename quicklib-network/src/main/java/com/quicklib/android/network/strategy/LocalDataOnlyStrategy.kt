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

abstract class LocalDataOnlyStrategy<T>(val mainContext: CoroutineContext = Dispatchers.Main, val localContext: CoroutineContext = Dispatchers.IO) : DataStrategy<T>() {

    override fun start(): Job = askLocal()

    // https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
    private fun askLocal() = GlobalScope.launch(mainContext, CoroutineStart.DEFAULT) {
        if (isLocalAvailable()) {
            try {
                liveData.value = DataWrapper(status = DataStatus.LOADING, localData = true)
                val task = withContext(localContext) { readData() }
                val data = task.await()
                liveData.value = DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true)
            } catch (error: Throwable) {
                liveData.value = DataWrapper(error = error, status = DataStatus.ERROR, localData = true)
            }
        } else {
            liveData.value = DataWrapper(error = IllegalStateException("Local data is not available"), status = DataStatus.ERROR, localData = true)
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @WorkerThread
    abstract suspend fun readData(): Deferred<T>
}
