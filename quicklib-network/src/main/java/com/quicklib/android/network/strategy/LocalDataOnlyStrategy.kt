package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class LocalDataOnlyStrategy<T>(mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main), localScope: CoroutineScope = CoroutineScope(Dispatchers.IO)) : DataStrategy<T>(mainScope = mainScope, localScope = localScope) {

    override fun start(): Job = askLocal()

    // https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
    private fun askLocal() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.value = DataWrapper(status = DataStatus.LOADING, localData = true)
                val task = withContext(localScope.coroutineContext) { readData() }
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
