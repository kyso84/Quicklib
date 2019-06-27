package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class FirebaseFirestoreReadObjectAwareStrategy<T>(private val document: DocumentReference) : DataStrategy<T>() {

    override fun start(): Job = ask()

    private fun ask() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, strategy = this@FirebaseFirestoreReadObjectAwareStrategy::class))
                document.addSnapshotListener { value, e ->
                    e?.let {
                        liveData.postValue(DataWrapper(error = it, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadObjectAwareStrategy::class))
                    } ?: run {
                        value?.let { snapshot ->
                            liveData.postValue(DataWrapper(value = deserialize(snapshot), status = DataStatus.SUCCESS, localData = true, strategy = this@FirebaseFirestoreReadObjectAwareStrategy::class))
                        } ?: run {
                            liveData.postValue(DataWrapper(error = IllegalStateException("Resource is not available"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadObjectAwareStrategy::class))
                        }
                    }
                }
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadObjectAwareStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Resource is not available"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadObjectAwareStrategy::class))
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @MainThread
    abstract fun deserialize(snapshot: DocumentSnapshot): T
}
