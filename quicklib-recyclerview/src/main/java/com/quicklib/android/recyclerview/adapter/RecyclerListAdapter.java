package com.quicklib.android.recyclerview.adapter;

import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

/**
 * This class overload Android's Application and provides useful features
 *
 * @author Benoit Deschanel
 * @since 16-11-30
 * Copyright (C) 2015 Quicklib
 */
public abstract class RecyclerListAdapter<E, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements ListAdapter, SpinnerAdapter {

    private static final int HOLDER_TAG = 777;

    @Override
    public final int getCount() {
        return getItemCount();
    }

    @Override
    public abstract E getItem(int position);

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if (convertView != null) {
            holder = (VH) convertView.getTag(HOLDER_TAG);
        } else {
            holder = onCreateViewHolder(parent, getItemViewType(position));
            convertView.setTag(HOLDER_TAG, holder);
        }
        onBindViewHolder(holder, position);
        return holder.itemView;
    }

    @Override
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerAdapterDataObserver(new LegacyDataSetObserver(observer));
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterAdapterDataObserver(new LegacyDataSetObserver(observer));
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    private static class LegacyDataSetObserver extends RecyclerView.AdapterDataObserver {

        private final DataSetObserver legacyObserver;

        public LegacyDataSetObserver(DataSetObserver legacyObserver) {
            this.legacyObserver = legacyObserver;
        }

        @Override
        public void onChanged() {
            super.onChanged();
            legacyObserver.onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            legacyObserver.onInvalidated();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            legacyObserver.onInvalidated();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            legacyObserver.onInvalidated();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            legacyObserver.onInvalidated();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            legacyObserver.onInvalidated();
        }
    }

}
