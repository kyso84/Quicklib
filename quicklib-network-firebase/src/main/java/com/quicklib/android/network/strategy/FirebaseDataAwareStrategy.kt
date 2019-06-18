package com.quicklib.android.network.strategy

import androidx.annotation.MainThread
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class FirebaseDataAwareStrategy<T>(private val databaseRef: DatabaseReference) : DataStrategy<T>() {

    override fun start(): Job = ask()

    private fun ask() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, strategy = this@FirebaseDataAwareStrategy::class))
                databaseRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists() || !snapshot.hasChildren()) {
                            liveData.postValue(DataWrapper(value = snapshot.value as T, status = DataStatus.SUCCESS, localData = true, strategy = this@FirebaseDataAwareStrategy::class))
                        } else {
                            liveData.postValue(DataWrapper(error = IllegalStateException("Type mismatch: An object is expected but a list was found. If truly want a list, please use FirebaseDataListStrategy instead"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDataAwareStrategy::class))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        liveData.postValue(DataWrapper(error = error.toException(), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDataAwareStrategy::class))
                    }
                })
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDataAwareStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Local value is not available"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDataAwareStrategy::class))
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true
}
