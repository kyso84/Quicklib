package com.quicklib.android.core.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.quicklib.android.core.bo.CustomViewState;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class QuickFrameLayout extends FrameLayout {
    @Nullable
    private Bundle arguments = null;

    public QuickFrameLayout(Context context) {
        this(context, null, 0);
    }

    public QuickFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        arguments = new Bundle();
        onCreate(attrs, arguments);
        onCreateView(LayoutInflater.from(context), this, true);

    }

    @Nullable
    public Bundle getArguments() {
        return arguments;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onActivityCreated(null);
        onStart();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            onResume();
        } else {
            onPause();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            onResume();
        } else {
            onPause();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        CustomViewState viewState = new CustomViewState(super.onSaveInstanceState());
        onSaveInstanceState(viewState.getInstanceState());
        return viewState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof CustomViewState) {
            CustomViewState viewState = (CustomViewState) state;
            super.onRestoreInstanceState(viewState.getSuperState());
            onActivityCreated(viewState.getInstanceState());
        } else {
            super.onRestoreInstanceState(state);
            onActivityCreated(new Bundle());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onStop();
        onDestroy();
    }


    @Nullable
    public abstract void onCreate(@Nullable AttributeSet attrs, Bundle initParams);

    @NonNull
    public abstract View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot);

    public abstract void onActivityCreated(@Nullable Bundle savedInstanceState);


    public void onSaveInstanceState(Bundle outState) {

    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onDestroy() {
    }

}
