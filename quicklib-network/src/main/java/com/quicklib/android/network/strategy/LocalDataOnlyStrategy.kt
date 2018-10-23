package com.quicklib.android.network.strategy

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext

abstract class LocalDataOnlyStrategy<T> {

    val liveData = MutableLiveData<DataWrapper<T>>()

    val bgContext: CoroutineContext = Dispatchers.IO
    val fgContext: CoroutineContext = Dispatchers.Main
    val job: Job = askLocal()

    fun cancel() {
        job.cancel()
    }

//    private fun askLocal() {
//        liveData.postValue(DataWrapper(status = DataStatus.LOADING, isLocal = true, isOutDated = false))
//        readData(object : DataCallback<T> {
//            override fun onSuccess(localData: T?) {
//                liveData.postValue(DataWrapper(value = localData, status = DataStatus.SUCCESS, isLocal = true, isOutDated = false))
//            }
//
//            override fun onError(error: Throwable?) {
//                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, isLocal = true, isOutDated = false))
//            }
//        })
//    }

    // https://proandroiddev.com/android-coroutine-recipes-33467a4302e9
    private fun askLocal() = GlobalScope.launch(fgContext, CoroutineStart.UNDISPATCHED) {
        try {
            liveData.value = DataWrapper(status = DataStatus.LOADING, isLocal = true, isOutDated = false)
            val task = withContext(bgContext) { readData() }
            val data = task.await()
            liveData.value = DataWrapper(value = data, status = DataStatus.SUCCESS, isLocal = true, isOutDated = false)
        } catch (error: Throwable) {
            liveData.value = DataWrapper(error = error, status = DataStatus.ERROR, isLocal = true, isOutDated = false)
        }
    }

    @WorkerThread
    abstract suspend fun readData(): Deferred<T>
}
