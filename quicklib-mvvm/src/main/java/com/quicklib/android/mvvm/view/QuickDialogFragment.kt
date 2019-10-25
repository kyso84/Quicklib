package com.quicklib.android.mvvm.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import com.quicklib.android.mvvm.QuickView
import com.quicklib.android.mvvm.viewmodel.QuickViewModel
import java.lang.ref.WeakReference

abstract class QuickDialogFragment<VDB : ViewDataBinding, VM : ViewModel> : DialogFragment(), QuickView<VDB, VM> {

    private var _binding: WeakReference<VDB>? = null
    private var _viewModel: WeakReference<VM>? = null

    @Throws
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bindingCreated: VDB? = DataBindingUtil.findBinding(view) ?: DataBindingUtil.bind(view)
        bindingCreated?.let { binding ->
            _binding = WeakReference(binding)
            binding.lifecycleOwner = this
            onBindingReady(binding)
        } ?: run {
            throw IllegalStateException("Unable to find the binding of your view. You should use DataBindingUtil.inflate()?.root instead of regular inflate()")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
    override fun onViewReady(viewMovel: VM, savedInstanceState: Bundle?) {}
}
