package com.quicklib.android.core.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quicklib.android.core.bo.CustomViewState;
@Deprecated
public abstract class CustomLinearLayout extends LinearLayoutCompat {
    @Nullable
    private Bundle arguments = null;

    public CustomLinearLayout(Context context) {
        this(context, null, 0);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
