package com.quicklib.android.network

import com.quicklib.android.network.strategy.DataStrategy
import kotlin.reflect.KClass

data class DataWrapper<T>(
    val value: T? = null,
    val status: DataStatus = DataStatus.UNKNOWN,
    val error: Throwable? = null,
    val localData: Boolean = false,
    val warning: Throwable? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val strategy: KClass<out DataStrategy<T>>? = null
)