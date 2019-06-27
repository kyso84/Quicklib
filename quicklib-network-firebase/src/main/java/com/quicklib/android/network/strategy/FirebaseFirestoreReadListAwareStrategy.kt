package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class FirebaseFirestoreReadListAwareStrategy<T : Any>(private val collection: CollectionReference) : DataStrategy<List<T>>() {

    override fun start(): Job = ask()

    private fun ask() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, strategy = this@FirebaseFirestoreReadListAwareStrategy::class))
                collection
                        .addSnapshotListener { value, e ->
                            e?.let {
                                liveData.postValue(DataWrapper(error = it, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadListAwareStrategy::class))
                            } ?: run {
                                value?.let { snapshot ->
                                    liveData.postValue(DataWrapper(value = deserialize(snapshot), status = DataStatus.SUCCESS, localData = true, strategy = this@FirebaseFirestoreReadListAwareStrategy::class))
                                } ?: run {
                                    liveData.postValue(DataWrapper(error = IllegalStateException("Resource is not available"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadListAwareStrategy::class))
                                }
                            }
                        }
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadListAwareStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Resource is not available"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadListAwareStrategy::class))
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @MainThread
    abstract fun deserialize(snapshot: QuerySnapshot): List<T>
}
