package com.quicklib.android.core.helper;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.quicklib.android.core.R;

/**
 * This helper provides convenient methods to perform recurrent animation tasks
 *
 * @author Benoit Deschanel
 * @package com.quicklib.android.core.helper
 * @since 17-05-02
 */
public class AnimationHelper {

    /**
     * This constant is the default animation duration (personal opinion)
     */
    public static final long ANIMATION_DURATION = 500;


    /**
     * Fade in a view
     *
     * @param view the view to fade
     */
    public static void fadeIn(final View view) {
        fadeIn(view, ANIMATION_DURATION);
    }

    /**
     * Fade in a view
     *
     * @param view     the view to fade
     * @param duration the duration of the animation
     */
    public static void fadeIn(final View view, long duration) {
        fadeIn(view, duration, 0);
    }

    /**
     * Fade in a view
     *
     * @param view     the view to fade
     * @param duration the duration of the animation
     * @param delayed  the delayed start of the animation
     */
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


    /**
     * Fade out a view
     *
     * @param view     the view to fade
     */
    public static void fadeOut(final View view) {
        fadeOut(view, ANIMATION_DURATION);
    }

    /**
     * Fade out a view
     *
     * @param view     the view to fade
     * @param duration the duration of the animation
     */
    public static void fadeOut(final View view, long duration) {
        fadeOut(view, duration, 0);
    }

    /**
     * Fade out a view
     *
     * @param view     the view to fade
     * @param duration the duration of the animation
     * @param delayed  the delayed start of the animation
     */
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