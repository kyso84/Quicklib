package com.quicklib.android.network.strategy

import com.google.firebase.firestore.DocumentReference
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.launch

abstract class FirebaseFirestoreDeleteObjectStrategy<T>(private val document: DocumentReference) : DataStrategy<T>() {

    override fun start() = write()

    private fun write() = mainScope.launch {
        try {
            document.delete()
                    .addOnSuccessListener { _ ->
                        liveData.postValue(DataWrapper(status = DataStatus.DELETED, localData = true, strategy = this@FirebaseFirestoreDeleteObjectStrategy::class))
                    }
                    .addOnFailureListener {
                        liveData.postValue(DataWrapper(error = it, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreDeleteObjectStrategy::class))
                    }
        } catch (error: Throwable) {
            liveData.postValue(DataWrapper(error = error, status = DataStatus.ERROR, localData = true, strategy = this@FirebaseFirestoreDeleteObjectStrategy::class))
        }
    }
}
