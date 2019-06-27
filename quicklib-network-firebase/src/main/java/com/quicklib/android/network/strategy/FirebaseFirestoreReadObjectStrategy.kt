package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.quicklib.android.network.DataStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.quicklib.android.network.DataWrapper
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.lifecycle.MediatorLiveData

abstract class FirebaseFirestoreReadObjectStrategy<T>(private val document: DocumentReference, liveData: MediatorLiveData<DataWrapper<T>> = MediatorLiveData()) : DataStrategy<T>() {

    override fun start(): Job = ask()

    private fun ask() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, strategy = this@FirebaseFirestoreReadObjectStrategy::class))
                document.get()
                        .addOnSuccessListener { snapshot: DocumentSnapshot ->
                            liveData.postValue(DataWrapper(value = deserialize(snapshot), status = DataStatus.SUCCESS, localData = true, strategy = null))
                        }
                        .addOnFailureListener {
                            liveData.postValue(DataWrapper(error = it, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadObjectStrategy::class))
                        }
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadObjectStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Resource is not available"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreReadObjectStrategy::class))
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @MainThread
    abstract fun deserialize(snapshot: DocumentSnapshot): T
}
