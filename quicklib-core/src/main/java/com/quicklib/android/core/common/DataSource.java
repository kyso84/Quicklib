package com.quicklib.android.core.common;

/**
 * @author bdescha1
 * @since 16-06-17
 * Copyright (C) 2016 French Connection !!!
 */
public interface DataSource<T> {
    int getCount();
    T getItem(int position);
    int getItemType(int position);
}