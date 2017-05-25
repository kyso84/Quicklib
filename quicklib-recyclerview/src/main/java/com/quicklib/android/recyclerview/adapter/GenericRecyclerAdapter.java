package com.quicklib.android.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.quicklib.android.core.common.DataSetEvent;
import com.quicklib.android.core.common.DataSetSource;
import com.quicklib.android.recyclerview.adapter.holder.GenericDataHolder;

public abstract class GenericRecyclerAdapter<E extends Enum<E>,T> extends RecyclerListAdapter<T, GenericDataHolder<E, T>> {

    protected final LayoutInflater inflater;
    protected final DataSetSource<T> dataSource;
    protected final DataSetEvent<E, T> dataEvent;


    public GenericRecyclerAdapter(Context context, DataSetSource<T> dataSource, DataSetEvent<E, T> dataEvent) {
        this(LayoutInflater.from(context), dataSource, dataEvent);
    }

    public GenericRecyclerAdapter(LayoutInflater inflater, DataSetSource<T> dataSource, DataSetEvent<E, T> dataEvent) {
        this.inflater = inflater;
        this.dataSource = dataSource;
        this.dataEvent = dataEvent;
    }

    @Override
    public int getItemViewType(int position) {
        return dataSource.getItemType(position);
    }

    @Override
    public abstract GenericDataHolder<E, T> onCreateViewHolder(ViewGroup parent, int viewType) ;

    @Override
    public void onBindViewHolder(GenericDataHolder<E, T> holder, int position) {
        T item = dataSource.getItem(position);
        holder.onBind(dataEvent, item, position);
    }

    @Override
    public int getItemCount() {
        return dataSource.getCount();
    }

    @Override
    public T getItem(int position){
        return dataSource.getItem(position);
    }
}
