package com.quicklib.android.app.view.dumb;
/*
 * Copyright 2017 (C) Yellow Pages
 *
 * Created by: the French Connection (bdescha1)
 * Created on: 19 juin 2017
 *
 * Project: yp-messaging
 * Package: com.ypg.android.common.view.dumb
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.quicklib.android.app.R;
import com.quicklib.android.core.view.CustomFrameLayout;


public class LoadingView extends CustomFrameLayout {


    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE_ID = "imageId";
    private static final String KEY_CONTENT = "message";
    private static final String KEY_FOOTER = "footer";
    private static final String KEY_BACKGROUND_COLOR_ID = "backgroundColorId";

    TextView text;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Nullable
    @Override
    public void onCreate(@Nullable AttributeSet attributeSet, Bundle bundle) {

    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean b) {
        View view = layoutInflater.inflate(R.layout.view_loading, this, b);
        text = findViewById(R.id.view_loading_text);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {

    }


}
