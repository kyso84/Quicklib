package com.quicklib.android.network

data class DataWrapper<T>(
        val value: T? = null,
        val status: DataStatus = DataStatus.UNKNOWN,
        val error: Throwable? = null,
        val localData: Boolean = false,
        val warning: Throwable? = null,
        val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        fun <T> LOADING(warning: Throwable? = null) = DataWrapper<T>(status = DataStatus.LOADING, localData = true, warning = warning)
        fun <T> FETCHING(warning: Throwable? = null) = DataWrapper<T>(status = DataStatus.FETCHING, localData = false, warning = warning)
        fun <T> SUCCESS(value: T, localData: Boolean, warning: Throwable? = null) = DataWrapper<T>(value = value, status = DataStatus.SUCCESS, localData = localData, warning = warning)
        fun <T> ERROR(error: Throwable, localData: Boolean, warning: Throwable? = null) = DataWrapper<T>(error = error, status = DataStatus.ERROR, localData = localData, warning = warning)
        fun <T> INVALID(error: Throwable, localData: Boolean, warning: Throwable? = null) = DataWrapper<T>(error = error, status = DataStatus.INVALID, localData = localData, warning = warning)
    }
}
