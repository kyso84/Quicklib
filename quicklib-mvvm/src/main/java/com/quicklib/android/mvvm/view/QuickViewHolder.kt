package com.quicklib.android.mvvm.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.quicklib.android.mvvm.QuickView

abstract class QuickViewHolder<T, VDB : ViewDataBinding, VM : ViewModel>(protected val binding: VDB) : RecyclerView.ViewHolder(binding.root), QuickView<VDB, VM> {

    constructor(inflater: LayoutInflater, container: ViewGroup?, @LayoutRes layoutId: Int) : this(DataBindingUtil.inflate(inflater, layoutId, container, false))
    constructor(context: Context, container: ViewGroup?, @LayoutRes layoutId: Int) : this(DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, container, false))

    protected val viewModel = getViewModelInstance()


    init {
        onBindingReady(binding)
        onViewReady(null)
    }


    override fun onBindingReady(binding: VDB) {}
    override fun onViewReady(savedInstanceState: Bundle?) {}

    protected abstract fun bind(data: T, position: Int)
}
