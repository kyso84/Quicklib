package com.quicklib.android.mvvm

import androidx.databinding.ViewDataBinding

interface QuickView<VDB : ViewDataBinding> {
    var binding: VDB?
    fun onBindingReady(binding: VDB)
}
