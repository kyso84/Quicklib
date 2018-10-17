package com.quicklib.android.network.strategy

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.quicklib.android.network.DataCallback
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import java.util.*

abstract class RemoteDataFirstStrategy<T>() {

    val liveData = MutableLiveData<DataWrapper<T>>()

    init {
        askRemote()
    }

    private fun askLocal(localData: T?, warning: Throwable? = null) {
        liveData.postValue(DataWrapper(status = DataStatus.LOADING, isLocal = true, isOutDated = false, warning = warning))
        readData(object : DataCallback<T> {
            override fun onSuccess(localData: T?) {
                liveData.postValue(DataWrapper(value = localData, status = DataStatus.SUCCESS, isLocal = true, isOutDated = false, warning = warning))
            }

            override fun onError(error: Throwable?) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, isLocal = true, isOutDated = false, warning = warning))
            }
        })
    }

    private fun askRemote() {
        liveData.postValue(DataWrapper(status = DataStatus.FETCHING))
        fetchData(object : DataCallback<T> {
            override fun onSuccess(data: T?) {
                liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, isLocal = false, isOutDated = false))
                data?.let {
                    writeData(it)
                }
            }

            override fun onError(error: Throwable?) {
                askLocal(localData = null, warning = error)
            }
        })
    }

    @WorkerThread
    abstract fun fetchData(callback: DataCallback<T>)

    @WorkerThread
    abstract fun readData(callback: DataCallback<T>)

    @WorkerThread
    abstract fun writeData(data: T)
}
