package com.quicklib.android.mvvm.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.quicklib.android.mvvm.QuickView

abstract class QuickViewHolder<T, VDB : ViewDataBinding, VM : ViewModel>(protected val lifecycleOwner: LifecycleOwner, protected val binding: VDB) : RecyclerView.ViewHolder(binding.root), QuickView<VDB, VM> {
    constructor(appCompatActivity: AppCompatActivity, binding: VDB) : this(appCompatActivity as LifecycleOwner, binding)
    constructor(fragment: Fragment, binding: VDB) : this(fragment as LifecycleOwner, binding)

    protected val viewModel = getViewModelInstance()

    init {
        onBindingReady(binding)
        onViewReady(null)
    }

    override fun onBindingReady(binding: VDB) {}
    override fun onViewReady(savedInstanceState: Bundle?) {}

    protected abstract fun bind(data: T, position: Int)
}
