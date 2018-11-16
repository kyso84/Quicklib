package com.quicklib.android.mvvm

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class QuickViewHolder<T, VM : ViewModel, VDB : ViewDataBinding>(protected val binding: VDB) : RecyclerView.ViewHolder(binding.root) {

    constructor(inflater: LayoutInflater, container: ViewGroup?, @LayoutRes layoutId: Int) : this(DataBindingUtil.inflate(inflater, layoutId, container, false))
    constructor(context: Context, container: ViewGroup?, @LayoutRes layoutId: Int) : this(DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, container, false))

    protected val viewModel = getViewModelInstance()

    init {
        onBindingReady(binding)
    }

    fun onBindingReady(binding: VDB) {}
    protected abstract fun getViewModelInstance(): VM
    protected abstract fun bind(data: T, position: Int)
}
