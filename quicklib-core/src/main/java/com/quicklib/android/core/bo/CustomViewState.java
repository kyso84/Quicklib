package com.quicklib.android.core.bo;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class CustomViewState implements Parcelable{
    private final String SUPER_STATE = "state_";
    private final Bundle bundle;

    /**
     * Instantiates a new View state.
     *
     * @param superState the super state
     */
    public CustomViewState(Parcelable superState) {
        bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE + hashCode(), superState);
    }

    /**
     * Instantiates a new View state.
     *
     * @param in the in
     */
    protected CustomViewState(Parcel in) {
        bundle = in.readBundle(Bundle.class.getClassLoader());
    }

    /**
     * Get the real state of the parent class
     *
     * @return the parcelable
     */
    public Parcelable getSuperState() {
        return bundle.getParcelable(SUPER_STATE + hashCode());
    }

    /**
     * Get the bundle to provide in order to save instance
     *
     * @return the instance state
     */
    public Bundle getInstanceState() {
        Bundle extra = (Bundle) bundle.clone();
        extra.remove(SUPER_STATE + hashCode());
        return extra;
    }

    public static final Parcelable.Creator<CustomViewState> CREATOR = new Parcelable.Creator<CustomViewState>() {
        @Override
        public CustomViewState createFromParcel(Parcel in) {
            return new CustomViewState(in);
        }

        @Override
        public CustomViewState[] newArray(int size) {
            return new CustomViewState[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(bundle);
    }
}
