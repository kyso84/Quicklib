package com.quicklib.android.network.strategy

import com.google.firebase.database.DatabaseReference
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.launch

abstract class FirebaseDatabaseWriteStrategy<T>(private val databaseRef: DatabaseReference, private val data: T, private val dataId: (() -> String)? = null, private val newDataId: ((newId: String?) -> T)? = null) : DataStrategy<T>() {

    override fun start() = write()

    private fun write() = mainScope.launch {
        try {
            var path: DatabaseReference = databaseRef
            var value: T = data

            val existingId = dataId?.invoke()
            if (!existingId.isNullOrEmpty()) {
                path = databaseRef.child(existingId)
                value = data
            } else {
                // Generate an id if needed
                path = databaseRef.push()

                // Update the current data with generated id
                newDataId?.let {
                    value = it.invoke(path.key)
                } ?: run {
                    value = data
                }
            }

            path.setValue(value) { error, _ ->
                error?.let {
                    liveData.postValue(DataWrapper(value = null, status = DataStatus.ERROR, error = it.toException(), localData = false, warning = null, timestamp = System.currentTimeMillis(), strategy = this@FirebaseDatabaseWriteStrategy::class))
                } ?: run {
                    liveData.postValue(DataWrapper(value = value, status = DataStatus.SUCCESS, error = null, localData = false, warning = null, timestamp = System.currentTimeMillis(), strategy = this@FirebaseDatabaseWriteStrategy::class))
                }
            }
            liveData.postValue(DataWrapper(value = data, status = DataStatus.SUCCESS, localData = true, strategy = this@FirebaseDatabaseWriteStrategy::class))
        } catch (error: Throwable) {
            liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDatabaseWriteStrategy::class))
        }
    }
}
