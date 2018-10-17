package com.quicklib.android.network.strategy

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataCallback
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper

abstract class RemoteDataOnlyStrategy<T> {

    val liveData = MutableLiveData<DataWrapper<T>>()

    init {
        askRemote()
    }

    private fun askRemote() {
        liveData.postValue(DataWrapper(status = DataStatus.FETCHING, isLocal = false, isOutDated = false))
        fetchData(object : DataCallback<T> {
            override fun onSuccess(data: T?) {
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, isLocal = false, isOutDated = false))
            }

            override fun onError(error: Throwable?) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, isLocal = false, isOutDated = false))
            }
        })
    }

    @WorkerThread
    abstract fun fetchData(callback: DataCallback<T>)
}
