package com.quicklib.android.mvvm

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

interface QuickSmartView<VDB : ViewDataBinding, VM : ViewModel> : QuickView<VDB> {

    var viewModel: VM?
    fun getViewModelInstance(): VM
    fun onViewReady(viewModel: VM, savedInstanceState: Bundle?)
}
