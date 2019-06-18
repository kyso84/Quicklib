package com.quicklib.android.network.strategy

import com.google.firebase.database.DatabaseReference
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.launch

abstract class WriteFirebaseDataStrategy<T>(private val databaseRef: DatabaseReference, private val data: T, private val generatedKey: ((newId: String?) -> T)? = null) : DataStrategy<T>() {

    override fun start() = write()

    private fun write() = mainScope.launch {
        try {
            val path = databaseRef.push()
            val key = path.key
            val value = generatedKey?.let {
                it.invoke(key)
            } ?: run {
                data
            }

            path.setValue(value) { error, ref ->
                error?.let {
                    liveData.postValue(DataWrapper(value = null, status = DataStatus.ERROR, error = it.toException(), localData = false, warning = null, timestamp = System.currentTimeMillis(), strategy = this@WriteFirebaseDataStrategy::class))
                } ?: run {
                    liveData.postValue(DataWrapper(value = value, status = DataStatus.SUCCESS, error = null, localData = false, warning = null, timestamp = System.currentTimeMillis(), strategy = this@WriteFirebaseDataStrategy::class))
                }
            }
            liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true, strategy = this@WriteFirebaseDataStrategy::class))
        } catch (error: Throwable) {
            liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@WriteFirebaseDataStrategy::class))
        }
    }
}
