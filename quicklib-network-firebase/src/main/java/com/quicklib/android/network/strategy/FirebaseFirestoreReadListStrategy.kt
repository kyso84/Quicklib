package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import com.google.firebase.firestore.CollectionReference
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.firestore.QuerySnapshot
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

abstract class FirebaseFirestoreReadListStrategy<T : Any>(private val collection: CollectionReference) : DataStrategy<List<T>>() {

    override fun start(): Job = ask()

    private fun ask() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, strategy = this@FirebaseFirestoreReadListStrategy::class))
                collection.get()
                        .addOnSuccessListener { snapshot ->
                            liveData.postValue(DataWrapper(value = deserialize(snapshot), status = DataStatus.SUCCESS, localData = true, strategy = this@FirebaseFirestoreReadListStrategy::class))
                        }
                        .addOnFailureListener {
                            liveData.postValue(DataWrapper(error = it, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadListStrategy::class))
                        }
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadListStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Local value is not available"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadListStrategy::class))
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @MainThread
    abstract fun deserialize(snapshot: QuerySnapshot): List<T>
}
