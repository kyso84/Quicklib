package com.quicklib.android.network.strategy

import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataCallback
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper

abstract class MixedDataStrategy<T>(val allowOutdatedResult: Boolean) {

    val liveData = MutableLiveData<DataWrapper<T>>()

    init {
        askLocal()
    }

    private fun askLocal() {
        liveData.postValue(DataWrapper(status = DataStatus.LOADING))
        readData(object : DataCallback<T> {
            override fun onSuccess(localData: T?) {
                if (isOutdated(localData)) {
                    askRemote(localData)
                } else {
                    localData?.let {
                        liveData.postValue(DataWrapper(value = it, status = DataStatus.SUCCESS, isLocal = true))
                    }
                }
            }

            override fun onError(error: Throwable?) {
                askRemote(null)
            }
        })
    }

    private fun askRemote(localData: T?) {
        liveData.postValue(DataWrapper(status = DataStatus.FETCHING))
        fetchData(object : DataCallback<T> {
            override fun onSuccess(data: T?) {
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, isLocal = false))
                data?.let {
                    writeData(it)
                }
            }

            override fun onError(error: Throwable?) {
                if (allowOutdatedResult && localData != null) {
                    liveData.postValue(DataWrapper(value = localData, status = DataStatus.SUCCESS, isLocal = true, isOutDated = true))
                } else {
                    liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR))
                }
            }
        })
    }

    abstract fun isOutdated(data: T?): Boolean
    abstract fun fetchData(callback: DataCallback<T>)
    abstract fun readData(callback: DataCallback<T>)
    abstract fun writeData(data: T)
}
