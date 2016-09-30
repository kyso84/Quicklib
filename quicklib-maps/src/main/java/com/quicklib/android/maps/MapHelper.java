package com.quicklib.android.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.quicklib.android.core.helper.DrawableHelper;

/**
 * @author bdescha1
 * @since 16-07-25
 * Copyright (C) 2016 French Connection !!!
 */
public class MapHelper {

    private static final String KEY_MAP_BUNDLE = "mapBundle";


    public static com.google.android.gms.maps.model.LatLng getLatLng(double lat, double lng) {
        return new com.google.android.gms.maps.model.LatLng(lat, lng);
    }

    public static com.google.android.gms.maps.model.LatLng getLatLng(Location location) {
        return new com.google.android.gms.maps.model.LatLng(location.getLatitude(), location.getLongitude());
    }

    public static String getStreetViewUrl(String googleKey, Location location) {
        return getStreetViewUrl(googleKey, location.getLatitude(), location.getLongitude());
    }


    public static String getStreetViewUrl(String googleKey, LatLng latLng) {
        return getStreetViewUrl(googleKey, latLng.latitude, latLng.longitude);
    }

    public static String getStreetViewUrl(String googleKey, double lat, double lng) {
        return "https://maps.googleapis.com/maps/api/streetview?size=600x300&location=" + lat + "," + lng + "&key=" + googleKey;
    }

    public static BitmapDescriptor getBitmapDescriptor(Context context, @DrawableRes int id, @DimenRes int sizeId) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        return getBitmapDescriptor(context, drawable, sizeId);
    }

    public static BitmapDescriptor getBitmapDescriptor(Context context, @DrawableRes int id, @DimenRes int sizeId, @ColorRes int colorId) {
        Drawable drawable = DrawableHelper.getTintedDrawable(context, id, colorId);
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

//    public static void test(GoogleMap map, ){
//
//        final long duration = 400;
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = map.getProjection();
//
//        Point startPoint = proj.toScreenLocation(marker.getPosition());
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//
//        final Interpolator interpolator = new LinearInterpolator();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed / duration);
//                double lng = t * target.longitude + (1 - t) * startLatLng.longitude;
//                double lat = t * target.latitude + (1 - t) * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
//                if (t < 1.0) {
//                    // Post again 10ms later.
//                    handler.postDelayed(this, 10);
//                } else {
//                    // animation ended
//                }
//            }
//        });
//    }

}