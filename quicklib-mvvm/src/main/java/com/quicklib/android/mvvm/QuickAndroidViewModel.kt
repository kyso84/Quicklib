package com.quicklib.android.mvvm

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel

abstract class QuickAndroidViewModel(application: Application) : AndroidViewModel(application), Observable {
    @Transient
    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            callbacks.add(callback)
        }
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            callbacks.remove(callback)
        }
    }

    fun notifyChange() {
        synchronized(this) {
            callbacks.notifyCallbacks(this, 0, null)
        }
    }

    fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            callbacks.notifyCallbacks(this, fieldId, null)
        }
    }
}
