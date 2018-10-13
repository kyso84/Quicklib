package com.quicklib.android.mvvm

import androidx.lifecycle.ViewModel
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

abstract class QuickActivity<VM : ViewModel, VDB : ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: VDB
    lateinit var viewModel: VM

    @Throws
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val view: View? = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        view?.let {
            val bindingCreated: VDB? = DataBindingUtil.findBinding(it) ?: DataBindingUtil.bind(it)
            bindingCreated?.let {
                binding = it
                binding.setLifecycleOwner(this)
            } ?: run {
                throw IllegalStateException("Unable to find the binding of your root view. You should use DataBindingUtil.setContentView() instead of regular setContentView()")
            }
        }

        viewModel = getViewModelInstance()
        onViewReady(savedInstanceState)
    }

    abstract fun getViewModelInstance(): VM
    abstract fun onViewReady(savedInstanceState: Bundle?)
}
