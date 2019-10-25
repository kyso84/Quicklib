package com.quicklib.android.mvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.quicklib.android.mvvm.QuickView
import java.lang.ref.WeakReference

abstract class QuickViewHolder<T, VDB : ViewDataBinding, VM : ViewModel>(lifecycleOwner: LifecycleOwner, binding: VDB, viewModelProvider: (() -> VM)? = null) : RecyclerView.ViewHolder(binding.root), QuickView<VDB, VM> {
    constructor(appCompatActivity: AppCompatActivity, binding: VDB, viewModelProvider: (() -> VM)? = null) : this(appCompatActivity as LifecycleOwner, binding, viewModelProvider)
    constructor(fragment: Fragment, binding: VDB, viewModel: VM, viewModelProvider: (() -> VM)? = null) : this(fragment as LifecycleOwner, binding, viewModelProvider)


    private var _binding: WeakReference<VDB>? = null
    private var _viewModel: WeakReference<VM>? = null


    init {
        _binding = WeakReference(binding)
        binding.lifecycleOwner = lifecycleOwner
        onBindingReady(binding)

        viewModelProvider?.let {
            val viewModel = it.invoke()
            _viewModel = WeakReference(viewModel)
            onViewReady(viewModel, null)
        }
    }

    override fun onBindingReady(binding: VDB) {}
    override fun onViewReady(viewModel: VM, savedInstanceState: Bundle?) {}

    abstract fun bind(data: T, position: Int)

    protected fun getBinding(): VDB = _binding?.get()?.let { it }
            ?: run { throw IllegalStateException("Unable to provide binding object.") }

    protected fun getViewModel(): VM = _viewModel?.get()?.let { it }
            ?: run { throw IllegalStateException("Unable to provide viewModel object. Maybe you should provide a viewModelProvider at the class initialization") }

}
