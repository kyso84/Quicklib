package com.quicklib.android.network.strategy

import com.google.firebase.firestore.CollectionReference
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.launch

abstract class FirebaseFirestoreWriteStrategy<T>(private val collection: CollectionReference, private val data: T, private val generatedKey: ((newId: String?) -> T)? = null) : DataStrategy<T>() {

    override fun start() = write()

    private fun write() = mainScope.launch {
        try {
            collection.add(data as Any)
                    .addOnSuccessListener { res ->
                        val inserted = generatedKey?.let {
                            it.invoke(res.id)
                        } ?: run {
                            data
                        }
                        liveData.postValue(DataWrapper(value = inserted, status = DataStatus.SUCCESS, localData = true, strategy = this@FirebaseFirestoreWriteStrategy::class))
                    }
                    .addOnFailureListener {
                        liveData.postValue(DataWrapper(error = it, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreWriteStrategy::class))
                    }
        } catch (error: Throwable) {
            liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreWriteStrategy::class))
        }
    }
}
