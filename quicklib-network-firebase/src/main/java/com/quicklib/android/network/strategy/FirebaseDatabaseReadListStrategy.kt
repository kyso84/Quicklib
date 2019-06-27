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

abstract class FirebaseDatabaseReadListStrategy<T>(private val databaseRef: DatabaseReference) : DataStrategy<List<T>>() {

    override fun start(): Job = ask()

    private fun ask() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, strategy = this@FirebaseDatabaseReadListStrategy::class))
                databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists() || snapshot.hasChildren()) {
                            liveData.postValue(DataWrapper(value = deserialize(snapshot), status = DataStatus.SUCCESS, localData = true, strategy = this@FirebaseDatabaseReadListStrategy::class))
                        } else {
                            liveData.postValue(DataWrapper(error = IllegalStateException("Type mismatch: A list is expected but a object was found. If truly want a list, please use FirebaseDatabaseReadObjectStrategy instead"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDatabaseReadListStrategy::class))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        liveData.postValue(DataWrapper(error = error.toException(), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDatabaseReadListStrategy::class))
                    }
                })
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDatabaseReadListStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Local value is not available"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDatabaseReadListStrategy::class))
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @MainThread
    abstract fun deserialize(snapshot: DataSnapshot): List<T>
}
