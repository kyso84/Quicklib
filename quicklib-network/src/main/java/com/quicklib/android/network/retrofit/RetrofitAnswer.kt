package com.quicklib.android.network.retrofit

import com.quicklib.android.network.DataCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class RetrofitAnswer<T> : Callback<T>, DataCallback<T> {
    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        response?.body()?.let {
            onSuccess(it)
        } ?: run {
            onSuccess(null)
        }
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        onError(t)
    }
}
