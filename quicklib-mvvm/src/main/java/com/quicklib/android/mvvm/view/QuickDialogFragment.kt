package com.quicklib.android.mvvm.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.quicklib.android.mvvm.QuickView
import com.quicklib.android.mvvm.viewmodel.QuickViewModel

abstract class QuickDialogFragment<VDB : ViewDataBinding, VM : ViewModel> : DialogFragment(), QuickView<VDB, VM> {

    lateinit var binding: VDB
    lateinit var viewModel: VM

    @Throws
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bindingCreated: VDB? = DataBindingUtil.findBinding(view) ?: DataBindingUtil.bind(view)
        bindingCreated?.let {
            binding = it
            binding.setLifecycleOwner(this)
            onBindingReady(binding)
        } ?: run {
            throw IllegalStateException("Unable to find the binding of your view. You should use DataBindingUtil.inflate()?.root instead of regular inflate()")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModelInstance()
        onViewReady(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (viewModel is QuickViewModel) {
            (viewModel as QuickViewModel).onFinish()
        }
    }

    override fun onBindingReady(binding: VDB) {}
    override fun onViewReady(savedInstanceState: Bundle?) {}
}
