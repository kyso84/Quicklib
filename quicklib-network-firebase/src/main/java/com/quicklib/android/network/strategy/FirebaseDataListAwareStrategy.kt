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

abstract class FirebaseDataListAwareStrategy<T>(private val databaseRef: DatabaseReference) : DataStrategy<List<T>>() {

    override fun start(): Job = ask()

    private fun ask() = mainScope.launch {
        if (isLocalAvailable()) {
            try {
                liveData.postValue(DataWrapper(status = DataStatus.LOADING, localData = true, strategy = this@FirebaseDataListAwareStrategy::class))
                databaseRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists() || snapshot.hasChildren()) {
                            val list = mutableListOf<T>()
                            snapshot.children.forEach {
                                list.add(it.value as T)
                            }
                            liveData.postValue(DataWrapper(value = list, status = DataStatus.SUCCESS, localData = true, strategy = this@FirebaseDataListAwareStrategy::class))
                        } else {
                            liveData.postValue(DataWrapper(error = IllegalStateException("Type mismatch: A list is expected but a object was found. If truly want a list, please use FirebaseDataStrategy instead"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDataListAwareStrategy::class))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        liveData.postValue(DataWrapper(error = error.toException(), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDataListAwareStrategy::class))
                    }
                })
            } catch (error: Throwable) {
                liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDataListAwareStrategy::class))
            }
        } else {
            liveData.postValue(DataWrapper(error = IllegalStateException("Local value is not available"), status = DataStatus.ERROR, localData = true, strategy = this@FirebaseDataListAwareStrategy::class))
        }
    }

    @MainThread
    open fun isLocalAvailable(): Boolean = true
}
