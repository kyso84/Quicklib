package com.quicklib.android.mvvm.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.quicklib.android.core.common.Const
import com.quicklib.android.mvvm.QuickSmartView
import java.lang.ref.WeakReference

abstract class QuickSmartViewHolder<T, VDB : ViewDataBinding, VM : ViewModel>(binding: VDB, lifecycleOwner: LifecycleOwner? = null) : QuickViewHolder<T, VDB>(binding, lifecycleOwner), QuickSmartView<VDB, VM> {
    constructor(binding: VDB, activity: AppCompatActivity? = null) : this(binding, activity as LifecycleOwner)
    constructor(binding: VDB, fragment: Fragment? = null) : this(binding, fragment as LifecycleOwner)

    @Transient
    private var _viewModel: WeakReference<VM>? = null
    override var viewModel: VM?
        get() = _viewModel?.get()?.let { it } ?: run {
            Log.w(Const.LOG_TAG, "Unable to provide viewModel object. Maybe you should wait after onViewReady()")
            null
        }
        set(value) {
            _viewModel = value?.let { WeakReference(value) }
        }

    init {
        getViewModelInstance()?.let {
            viewModel = it
            onViewReady(it, null)
        }
    }

    override fun onBindingReady(binding: VDB) {}
    override fun onViewReady(viewModel: VM, savedInstanceState: Bundle?) {}
}
