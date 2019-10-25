package com.quicklib.android.mvvm.view

import androidx.lifecycle.ViewModel
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.quicklib.android.mvvm.QuickView
import com.quicklib.android.mvvm.viewmodel.QuickViewModel
import java.lang.ref.WeakReference

abstract class QuickActivity<VDB : ViewDataBinding, VM : ViewModel> : AppCompatActivity(), QuickView<VDB, VM> {

    private var _binding: WeakReference<VDB>? = null
    private var _viewModel: WeakReference<VM>? = null

    @Throws
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val view: View? = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        view?.let {
            val bindingCreated: VDB? = DataBindingUtil.findBinding(it) ?: DataBindingUtil.bind(it)
            bindingCreated?.let {binding ->
                _binding = WeakReference(binding)
                binding.lifecycleOwner = this
                onBindingReady(binding)
            } ?: run {
                throw IllegalStateException("Unable to find the binding of your root view. You should use DataBindingUtil.setContentView() instead of regular setContentView()")
            }
        }
        val viewModel = getViewModelInstance()
        _viewModel = WeakReference(viewModel)
        onViewReady(viewModel, savedInstanceState)
    }

    protected fun getBinding(): VDB = _binding?.get()?.let { it }
            ?: run { throw IllegalStateException("Unable to provide binding object. Maybe you should wait after onBindingReady()") }

    protected fun getViewModel(): VM = _viewModel?.get()?.let { it }
            ?: run { throw IllegalStateException("Unable to provide viewModel object. Maybe you should wait after onViewReady()") }

    override fun onDestroy() {
        super.onDestroy()
        _viewModel?.clear()
        _binding?.clear()
    }

    override fun onBindingReady(binding: VDB) {}
    override fun onViewReady(viewModel: VM, savedInstanceState: Bundle?) {}
}
