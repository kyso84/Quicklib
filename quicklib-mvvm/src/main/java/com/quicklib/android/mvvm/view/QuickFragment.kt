package com.quicklib.android.mvvm.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.quicklib.android.core.common.Const
import com.quicklib.android.mvvm.QuickSmartView

abstract class QuickFragment<VDB : ViewDataBinding, VM : ViewModel> : Fragment(), QuickSmartView<VDB, VM> {

    override var binding: VDB? = null
        get() = binding.let { it } ?: run {
            Log.w(Const.LOG_TAG, "Unable to provide binding object. Maybe you should wait after onBindingReady()")
            null
        }
        set(value) {
            field = value
        }

    override var viewModel: VM? = null
        get() = viewModel.let { it } ?: run {
            Log.w(Const.LOG_TAG, "Unable to provide viewModel object. Maybe you should wait after onViewReady()")
            null
        }
        set(value) {
            field = value
        }

    @Throws
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bindingCreated: VDB? = DataBindingUtil.findBinding(view) ?: DataBindingUtil.bind(view)
        bindingCreated?.let { binding ->
            binding.lifecycleOwner = this
            this.binding = binding
            onBindingReady(binding)
        } ?: run {
            throw IllegalStateException("Unable to find the binding of your view. You should use DataBindingUtil.inflate()?.root instead of regular inflate()")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getViewModelInstance().let {
            viewModel = it
            onViewReady(it, savedInstanceState)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        viewModel = null
    }

    override fun onBindingReady(binding: VDB) {}
    override fun onViewReady(viewMovel: VM, savedInstanceState: Bundle?) {}
}
