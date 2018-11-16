package com.quicklib.android.mvvm

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class QuickFragment<VM : ViewModel, VDB : ViewDataBinding> : Fragment() {

    lateinit var binding: VDB
    lateinit var viewModel: VM

    @Throws
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bindingCreated: VDB? = DataBindingUtil.findBinding(view) ?: DataBindingUtil.bind(view)
        bindingCreated?.let {
            binding = it
            it.setLifecycleOwner(this)
            onBindingReady(it)
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

    fun onBindingReady(binding: VDB) {}
    fun onViewReady(savedInstanceState: Bundle?) {}

    abstract fun getViewModelInstance(): VM
}
