package com.quicklib.android.mvvm

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

interface QuickView<VDB : ViewDataBinding, VM : ViewModel> {
    fun getViewModelInstance(): VM
    fun onBindingReady(binding: VDB)
    fun onViewReady(savedInstanceState: Bundle?)
}