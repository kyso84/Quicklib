package com.quicklib.android.map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class RemoteMarkerTarget implements Target {
    final Marker marker;

    public RemoteMarkerTarget(Marker marker) {
        this.marker = marker;
    }

    @Override
    public int hashCode() {
        return marker.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RemoteMarkerTarget) {
            Marker marker = ((RemoteMarkerTarget) o).marker;
            return this.marker.equals(marker);
        } else {
            return false;
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        marker.setIcon(bitmapDescriptor);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

}