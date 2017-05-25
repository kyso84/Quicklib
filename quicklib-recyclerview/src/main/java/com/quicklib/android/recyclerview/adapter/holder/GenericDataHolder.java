package com.quicklib.android.recyclerview.adapter.holder;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quicklib.android.core.common.DataSetEvent;


/**
 *
 * @param <E> This enum is a set of actions available on this view
 * @param <T> is the business object type
 */
public abstract class GenericDataHolder<E extends Enum<E>, T> extends RecyclerView.ViewHolder {

    /**
     * Instantiates a new Data holder.
     *
     * @param context the context
     * @param resId   the id of layout thet represents this holder
     * @param parent  the parent used to inflate the view
     */
    public GenericDataHolder(Context context, @LayoutRes int resId, ViewGroup parent) {
        this(LayoutInflater.from(context), resId, parent);
    }

    /**
     * Instantiates a new Data holder.
     *
     * @param inflater the inflater
     * @param resId   the id of layout thet represents this holder
     * @param parent  the parent used to inflate the view
     */
    public GenericDataHolder(LayoutInflater inflater, @LayoutRes int resId, ViewGroup parent) {
        this(inflater.inflate(resId, parent, false));
    }

    /**
     * Instantiates a new Data holder.
     *
     * @param itemView the item view
     */
    public GenericDataHolder(View itemView) {
        super(itemView);
    }

    /**
     * This method must set values to view and map UI events to DataEvent object
     *
     * @param dataEvent the DataEvent (event handler)
     * @param item      the bo targeted
     * @param position  the position in the adapter
     */
    public abstract void onBind(final DataSetEvent<E, T> dataEvent, final T item, final int position);

}
