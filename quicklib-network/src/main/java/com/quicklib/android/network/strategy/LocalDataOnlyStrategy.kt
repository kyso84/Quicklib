package com.quicklib.android.network.strategy

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataCallback
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper

abstract class LocalDataOnlyStrategy<T> {

    val liveData = MutableLiveData<DataWrapper<T>>()

    init {
        askLocal()
    }

    private fun askLocal() {
        liveData.postValue(DataWrapper(status = DataStatus.LOADING, isLocal = true, isOutDated = false))
        readData(object : DataCallback<T> {
            override fun onSuccess(localData: T?) {
                liveData.postValue(DataWrapper(value = localData, status = DataStatus.SUCCESS, isLocal = true, isOutDated = false))
            }

            override fun onError(error: Throwable?) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, isLocal = true, isOutDated = false))
            }
        })
    }

    @WorkerThread
    abstract fun readData(callback: DataCallback<T>)
}
