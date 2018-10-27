package com.quicklib.android.network

class DataWrapper<T>(
    val value: T? = null,
    val status: DataStatus = DataStatus.UNKNOWN,
    val error: Throwable? = null,
    val localData: Boolean = false,
    val warning: Throwable? = null,
    val timestamp: Long = System.currentTimeMillis()
)
