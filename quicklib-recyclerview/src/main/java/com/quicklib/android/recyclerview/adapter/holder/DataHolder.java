package com.quicklib.android.recyclerview.adapter.holder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import com.quicklib.android.core.common.DataEvent;

/**
 * Created by benoit on 17-04-17.
 */

public abstract class DataHolder<E extends Enum<E>, T> extends RecyclerView.ViewHolder {

    public DataHolder(Context context, @LayoutRes int resId, ViewGroup parent) {
        this(LayoutInflater.from(context), resId, parent);
    }

    public DataHolder(LayoutInflater inflater, @LayoutRes int resId, ViewGroup parent) {
        this(inflater.inflate(resId, parent, false));
    }

    public DataHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBind(DataEvent<E, T> dataEvent, T item, int position);

}