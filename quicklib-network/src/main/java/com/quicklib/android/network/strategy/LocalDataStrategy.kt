package com.quicklib.android.network.strategy

import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataCallback
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper

abstract class LocalDataStrategy<T> {

    val liveData = MutableLiveData<DataWrapper<T>>()

    init {
        askLocal()
    }

    private fun askLocal() {
        liveData.postValue(DataWrapper(status = DataStatus.LOADING))
        readData(object : DataCallback<T> {
            override fun onSuccess(localData: T?) {
                liveData.postValue(DataWrapper(value = localData, status = DataStatus.SUCCESS, isLocal = true))
            }

            override fun onError(error: Throwable?) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR))
            }
        })
    }

    abstract fun readData(callback: DataCallback<T>)
}
