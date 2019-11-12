package com.quicklib.android.mvvm.view

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.quicklib.android.core.common.Const
import com.quicklib.android.mvvm.QuickView
import java.lang.ref.WeakReference

abstract class QuickViewHolder<T, VDB : ViewDataBinding>(binding: VDB, lifecycleOwner: LifecycleOwner? = null) : RecyclerView.ViewHolder(binding.root), QuickView<VDB> {
    constructor(binding: VDB, activity: AppCompatActivity? = null) : this(binding, activity as LifecycleOwner)
    constructor(binding: VDB, fragment: Fragment? = null) : this(binding, fragment as LifecycleOwner)

    @Transient
    private var _binding: WeakReference<VDB>? = null
    override var binding: VDB?
        get() = _binding?.get().let { it } ?: run {
            Log.w(Const.LOG_TAG, "Unable to provide binding object. Maybe you should wait after onBindingReady()")
            null
        }
        set(value) {
            _binding = value?.let { WeakReference(it) }
        }

    init {
        lifecycleOwner?.let {
            binding.lifecycleOwner = it
        }
        this.binding = binding
        onBindingReady(binding)
    }

    override fun onBindingReady(binding: VDB) {}
}
