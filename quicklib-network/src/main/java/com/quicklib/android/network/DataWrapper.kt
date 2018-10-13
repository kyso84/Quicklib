package com.quicklib.android.network

class DataWrapper<T>(
    val value: T? = null,
    val status: DataStatus = DataStatus.UNKNOWN,
    val error: Throwable? = null,
    val isLocal: Boolean = false,
    val isOutDated: Boolean = false
)
