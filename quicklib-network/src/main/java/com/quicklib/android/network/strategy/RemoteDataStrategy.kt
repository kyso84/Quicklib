package com.quicklib.android.network.strategy

import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataCallback
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper

abstract class RemoteDataStrategy<T> {

    val liveData = MutableLiveData<DataWrapper<T>>()

    init {
        askRemote()
    }

    private fun askRemote() {
        liveData.postValue(DataWrapper(status = DataStatus.FETCHING))
        fetchData(object : DataCallback<T> {
            override fun onSuccess(data: T?) {
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, isLocal = false))
            }

            override fun onError(error: Throwable?) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR))
            }
        })
    }

    abstract fun fetchData(callback: DataCallback<T>)
}
