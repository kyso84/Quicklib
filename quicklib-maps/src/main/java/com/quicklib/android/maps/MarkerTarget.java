package com.quicklib.android.maps;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MarkerTarget implements Target {
    private final Marker marker;

    public MarkerTarget(Marker marker) {
        this.marker = marker;
    }

    @Override
    public int hashCode() {
        return marker.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MarkerTarget) {
            Marker marker = ((MarkerTarget) o).marker;
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
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

}