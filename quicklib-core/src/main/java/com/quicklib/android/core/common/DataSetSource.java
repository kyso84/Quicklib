package com.quicklib.android.core.common;

/**
 * This interface is used provide data to an adapter.
 * This is the minimum required to make android's adapters working
 *
 * @param <T> this is the data class
 * @author Benoit Deschanel
 * @package com.quicklib.android.core.common
 * @since 17-05-02
 */
public interface DataSetSource<T> {
    /**
     * How many items are in the data set represented by this Adapter.
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#getItemCount()">RecyclerView.Adapter#getItemCount()</a>
     * @see <a href="https://developer.android.com/reference/android/widget/Adapter.html#getCount()">Adapter#getCount()</a>
     *
     * @return the number of data object
     */
    int getCount();

    /**
     * Get the data item associated with the specified position in the data set.
     * @see <a href="https://developer.android.com/reference/android/widget/Adapter.html#getItem()">Adapter#getItem()</a>
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    T getItem(int position);

    /**
     * Return the view type of the item at position for the purposes of view recycling
     * @see <a href="https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#getItemViewType(int)">RecyclerView.Adapter#getItemViewType(int)</a>
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at position. Type codes need not be contiguous.
     */
    int getItemType(int position);
}