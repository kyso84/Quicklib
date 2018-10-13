package com.quicklib.android.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.quicklib.android.core.helper.DrawableHelper;

/**
 * @author bdescha1
 * @since 16-07-25
 * Copyright (C) 2016 French Connection !!!
 */
public class MapHelper {

    private static final String KEY_MAP_BUNDLE = "mapBundle";


    public static com.google.maps.model.LatLng getLatLong(double lat, double lng) {
        return new com.google.maps.model.LatLng(lat, lng);
    }

    public static com.google.maps.model.LatLng getLatLong(com.google.android.gms.maps.model.LatLng latlng) {
        return new com.google.maps.model.LatLng(latlng.latitude, latlng.longitude);
    }

    public static com.google.android.gms.maps.model.LatLng getLatLng(double lat, double lng) {
        return new com.google.android.gms.maps.model.LatLng(lat, lng);
    }

    public static com.google.android.gms.maps.model.LatLng getLatLng(com.google.maps.model.LatLng latlong) {
        return new com.google.android.gms.maps.model.LatLng(latlong.lat, latlong.lng);
    }

    public static String getStreetViewUrl(String googleKey, Location location) {
        return getStreetViewUrl(googleKey, location.getLatitude(), location.getLongitude());
    }


    public static String getStreetViewUrl(String googleKey, LatLng latLng) {
        return getStreetViewUrl(googleKey, latLng.latitude, latLng.longitude);
    }

    public static String getStreetViewUrl(String googleKey, double lat, double lng) {
        return getStreetViewUrl( googleKey, lat, lng, 600, 300);
    }

    public static String getStreetViewUrl(String googleKey, double lat, double lng, int width, int height) {
        return "https://maps.googleapis.com/maps/api/streetview?size="+width+"x"+height+"&location=" + lat + "," + lng + "&key=" + googleKey;
    }

    public static BitmapDescriptor getBitmapDescriptor(Context context, @DrawableRes int drawableId, @DimenRes int sizeId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        return getBitmapDescriptor(context, drawable, sizeId);
    }

    public static BitmapDescriptor getBitmapDescriptor(Context context, @DrawableRes int drawableId, @DimenRes int sizeId, @ColorRes int colorId) {
        Drawable drawable = DrawableHelper.getTintedDrawable(context, drawableId, colorId);
        return getBitmapDescriptor(context, drawable, sizeId);
    }

    private static BitmapDescriptor getBitmapDescriptor(Context context, Drawable drawable, @DimenRes int sizeId) {
        int size = context.getResources().getDimensionPixelSize(sizeId);
        drawable.setBounds(0, 0, size, size);
        Bitmap b = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        drawable.draw(c);
        return BitmapDescriptorFactory.fromBitmap(b);
    }

    public static Bundle getMapBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            return savedInstanceState.getBundle(KEY_MAP_BUNDLE);
        }
        return null;
    }

    public static void saveMapBundle(MapView mapView, Bundle outState) {
        if (mapView != null) {
            Bundle safeBundle = new Bundle();
            mapView.onSaveInstanceState(safeBundle);
            outState.putBundle(KEY_MAP_BUNDLE, safeBundle);
        }
    }


    public static float getRadius(Projection projection) {
        VisibleRegion vr = projection.getVisibleRegion();
        double left = vr.latLngBounds.southwest.longitude;
        double top = vr.latLngBounds.northeast.latitude;
        double right = vr.latLngBounds.northeast.longitude;
        double bottom = vr.latLngBounds.southwest.latitude;

        Location center = new Location("center");
        center.setLatitude(vr.latLngBounds.getCenter().latitude);
        center.setLongitude(vr.latLngBounds.getCenter().longitude);

        Location middleLeft = new Location("middleLeft");
        middleLeft.setLatitude(center.getLatitude());
        middleLeft.setLongitude(left);

        return center.distanceTo(middleLeft);
    }



}