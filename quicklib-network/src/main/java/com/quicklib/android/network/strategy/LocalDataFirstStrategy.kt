package com.quicklib.android.network.strategy

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataCallback
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper

abstract class LocalDataFirstStrategy<T>() {

    val liveData = MutableLiveData<DataWrapper<T>>()

    init {
        askLocal()
    }

    private fun askLocal() {
        liveData.postValue(DataWrapper(status = DataStatus.LOADING, isLocal = true, isOutDated = false))
        readData(object : DataCallback<T> {
            override fun onSuccess(localData: T?) {
                if (isOutdated(localData)) {
                    askRemote(localData = localData)
                } else {
                    liveData.postValue(DataWrapper(value = localData, status = DataStatus.SUCCESS, isLocal = true, isOutDated = false))
                }
            }

            override fun onError(error: Throwable?) {
                askRemote(localData = null, warning = error)
            }
        })
    }

    private fun askRemote(localData: T?, warning: Throwable? = null) {
        liveData.postValue(DataWrapper(status = DataStatus.FETCHING, isLocal = false, isOutDated = true, warning = warning))
        fetchData(object : DataCallback<T> {
            override fun onSuccess(data: T?) {
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, isLocal = false, isOutDated = true, warning = warning))
                data?.let {
                    writeData(it)
                }
            }

            override fun onError(error: Throwable?) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, isLocal = false, isOutDated = true, warning = warning))
            }
        })
    }

    abstract fun isOutdated(data: T?): Boolean

    @WorkerThread
    abstract fun fetchData(callback: DataCallback<T>)

    @WorkerThread
    abstract fun readData(callback: DataCallback<T>)

    @WorkerThread
    abstract fun writeData(data: T)
}
