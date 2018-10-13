package com.quicklib.android.network

interface DataCallback<T> {
    fun onSuccess(result: T?)
    fun onError(error: Throwable?)
}
