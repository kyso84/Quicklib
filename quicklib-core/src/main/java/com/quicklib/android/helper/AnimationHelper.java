package com.quicklib.android.helper;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ypg.find.R;

import timber.log.Timber;

/**
 * @author bdescha1
 * @since 16-07-21
 * Copyright (C) 2016 French Connection !!!
 */
public class AnimationHelper {

    public static final long ANIMATION_DURATION = 500;


    public static void fadeIn(final View view) {
        fadeIn(view, ANIMATION_DURATION);
    }

    public static void fadeIn(final View view, long duration) {
        fadeIn(view, duration, 0);
    }

    public static void fadeIn(final View view, final long duration, long delayed) {
        // Setup the animation
        final Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fadein);
        animation.setDuration(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        // Start the animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                view.startAnimation(animation);
            }
        }, delayed);
    }


    public static void fadeOut(final View view) {
        fadeOut(view, ANIMATION_DURATION);
    }

    public static void fadeOut(final View view, long duration) {
        fadeOut(view, duration, 0);
    }

    public static void fadeOut(final View view, final long duration, long delayed) {
        // Setup the animation
        final Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fadeout);
        animation.setDuration(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        // Start the animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                view.startAnimation(animation);
            }
        }, delayed);
    }


}