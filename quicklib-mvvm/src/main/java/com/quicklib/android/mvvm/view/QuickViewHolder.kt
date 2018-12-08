package com.quicklib.android.mvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.quicklib.android.mvvm.QuickView

abstract class QuickViewHolder<T, VDB : ViewDataBinding, VM : ViewModel>(protected val lifecycleOwner: LifecycleOwner, protected val binding: VDB) : RecyclerView.ViewHolder(binding.root), QuickView<VDB, VM> {
    constructor(appCompatActivity: AppCompatActivity, binding: VDB) : this(appCompatActivity as LifecycleOwner, binding)
    constructor(fragment: Fragment, binding: VDB) : this(fragment as LifecycleOwner, binding)

    protected val viewModel: VM

    init {
        onBindingReady(binding)
        viewModel = getViewModelInstance()
        onViewReady(null)
    }

    override fun onBindingReady(binding: VDB) {}
    override fun onViewReady(savedInstanceState: Bundle?) {}

    abstract fun bind(data: T, position: Int)
}
