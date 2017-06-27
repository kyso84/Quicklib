package com.quicklib.android.core.common;

/**
 * This interface is used provide data to an adapter with a selectable.
 * This is the minimum required to make android's adapters working
 *
 * @param <T> this is the data class
 * @author Benoit Deschanel
 * @package com.quicklib.android.core.common
 * @since 17-06-27
 */
public interface SelectableDataSetSource<T> extends DataSetSource<T>{
    boolean isSelected(int position);
}