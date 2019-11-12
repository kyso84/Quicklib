package com.quicklib.android.mvvm.view

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.quicklib.android.core.common.Const
import com.quicklib.android.mvvm.QuickSmartView

abstract class QuickActivity<VDB : ViewDataBinding, VM : ViewModel> : AppCompatActivity(), QuickSmartView<VDB, VM> {

    override var binding: VDB?
        get() = binding.let { it } ?: run {
            Log.w(Const.LOG_TAG, "Unable to provide binding object. Maybe you should wait after onBindingReady()")
            binding
        }
        set(value) {
            binding = value
        }

    override var viewModel: VM?
        get() = viewModel.let { it } ?: run {
            Log.w(Const.LOG_TAG, "Unable to provide viewModel object. Maybe you should wait after onViewReady()")
            viewModel
        }
        set(value) {
            viewModel = value
        }

    @Throws
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0)?.let {
            val bindingCreated: VDB? = DataBindingUtil.findBinding(it) ?: DataBindingUtil.bind(it)
            bindingCreated?.let { binding ->
                binding.lifecycleOwner = this
                this.binding = binding
                onBindingReady(binding)
            } ?: run {
                throw IllegalStateException("Unable to find the binding of your root view. You should use DataBindingUtil.setContentView() instead of regular setContentView()")
            }
        }
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
    override fun onViewReady(viewModel: VM, savedInstanceState: Bundle?) {}
}
