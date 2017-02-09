package com.quicklib.android.core.common;


import android.view.View;

/**
 * @author bdescha1
 * @since 16-06-17
 * Copyright (C) 2016 French Connection !!!
 */
public interface DataEvent< E extends Enum<E> , T> {
    void onEvent(E eventType, T data, View... views);
}