package com.quicklib.android.recyclerview.adapter

import android.database.DataSetObserver
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.SpinnerAdapter

/**
 * This class overload Android's Application and provides useful features
 *
 * @author Benoit Deschanel
 * @since 16-11-30
 * Copyright (C) 2015 Quicklib
 */
abstract class RecyclerListAdapter<T, VH : RecyclerView.ViewHolder>(private val list: MutableList<T> = mutableListOf()) : RecyclerView.Adapter<VH>(), ListAdapter, SpinnerAdapter {

    companion object {
        const val HOLDER_TAG = 84000
    }

    protected val legacyObserverList = mutableListOf<DataSetObserver>()

    private val internalObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            legacyObserverList.forEach {
                it.onChanged()
            }
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            onChanged()
        }
    }


    init {
        registerAdapterDataObserver(internalObserver)
    }

    override fun getItemCount(): Int = list.size

    override fun isEmpty(): Boolean = list.isEmpty()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View
        var holder: VH = if (convertView?.getTag(HOLDER_TAG) is RecyclerView.ViewHolder) {
            convertView.getTag(HOLDER_TAG) as VH
        } else {
            onCreateViewHolder(parent, getItemViewType(position))
        }

        // Handle holder pattern
        view = holder.itemView
        view.setTag(HOLDER_TAG, holder)

        // Set values
        onBindViewHolder(holder, position)

        return view
    }

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): T = list[position]

    override fun isEnabled(position: Int): Boolean = true

    override fun getViewTypeCount(): Int = 1

    override fun areAllItemsEnabled(): Boolean = true

    override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        observer?.let {
            if (!legacyObserverList.contains(it)) {
                legacyObserverList.add(it)
            }
        }
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        observer?.let {
            if (legacyObserverList.contains(it)) {
                legacyObserverList.remove(it)
            }
        }
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addAll(vararg itemList: T) {
        val lastIndex = list.size
        list.addAll(itemList)
        notifyItemRangeInserted(lastIndex, itemList.size)
    }

    fun addAll(itemList: List<T>) {
        val lastIndex = list.size
        list.addAll(itemList)
        notifyItemRangeInserted(lastIndex, itemList.size)
    }

    fun add(item: T): T {
        list.add(item)
        notifyItemInserted(list.size - 1)
        return item
    }

    fun add(position: Int, item: T): T {
        list.add(position, item)
        notifyItemInserted(position)
        return item
    }

    fun remove(item: T): T {
        val position = list.indexOf(item)
        return removeAt(position)
    }

    fun removeAt(position: Int): T {
        val removed = list.removeAt(position)
        notifyItemRemoved(position)
        return removed
    }

}
