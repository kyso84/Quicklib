package com.quicklib.android.mvvm

import androidx.lifecycle.ViewModel
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

abstract class QuickActivity<VM : ViewModel, VDB : ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: VDB
    lateinit var viewModel: VM

    @Throws
    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        val view: View? = findViewById(android.R.id.content)
        view?.let {
            val bindingCreated: VDB? = DataBindingUtil.getBinding(it) ?: DataBindingUtil.bind(it)
            bindingCreated?.let {
                binding = it
                binding.setLifecycleOwner(this)
            } ?: run {
                throw IllegalStateException("Unable to find the binding of your root view. Did you set a contentView with a \"databindable\" layout ?")
            }
        }

        viewModel = getViewModelInstance()
        onViewReady(savedInstanceState)
    }

    abstract fun getViewModelInstance(): VM
    abstract fun onViewReady(savedInstanceState: Bundle?)
}
