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

abstract class FirebaseDatabaseReadObjectAwareStrategy<T>(private val databaseRef: DatabaseReference) : DataStrategy<T>() {

    override fun start(): Job = ask()

    private fun ask() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, strategy = this@FirebaseDatabaseReadObjectAwareStrategy::class))
                databaseRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists() || !snapshot.hasChildren()) {
                            liveData.postValue(DataWrapper(value = deserialize(snapshot), status = DataStatus.SUCCESS, localData = true, strategy = this@FirebaseDatabaseReadObjectAwareStrategy::class))
                        } else {
                            liveData.postValue(DataWrapper(error = IllegalStateException("Type mismatch: An object is expected but a list was found. If truly want a list, please use FirebaseDatabaseReadListStrategy instead"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDatabaseReadObjectAwareStrategy::class))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        liveData.postValue(DataWrapper(error = error.toException(), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDatabaseReadObjectAwareStrategy::class))
                    }
                })
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDatabaseReadObjectAwareStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Local value is not available"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDatabaseReadObjectAwareStrategy::class))
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true

    @MainThread
    abstract fun deserialize(snapshot: DataSnapshot): T?
}
