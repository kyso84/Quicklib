package com.quicklib.android.core.common;


import android.view.View;


/**
 * This interface is used to pass events from an adapter to a "event handler"
 *
 * @param <E> this enum describes all view events (e.g. click, long_press, swipe_left)
 * @param <T> this is the data class
 * @author Benoit Deschanel
 * @package com.quicklib.android.core.common
 * @since 17 -05-02
 */
public interface DataEvent< E extends Enum<E> , T> {
    /**
     * Is called on each view event
     *
     * @param eventType this is the "event handler" (e.g. click, long_press, swipe_left)
     * @param data      this is the instance impacted by the event
     * @param views     this views are used to pass views for transition animations
     */
    void onEvent(E eventType, T data, View... views);
}