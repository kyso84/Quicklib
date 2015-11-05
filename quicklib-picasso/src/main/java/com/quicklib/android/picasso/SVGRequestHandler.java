package com.quicklib.android.picasso;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.quicklib.android.utils.ScreenUtils;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.util.Locale;

public class SVGRequestHandler extends RequestHandler {

    private static final String SCHEME_HTTP = "http";
    private static final String SCHEME_HTTPS = "https";

    private final Downloader downloader;
    private final float maxSize;

    public SVGRequestHandler(Downloader downloader, Context context) {
        ScreenUtils.ScreenSize screenSize = ScreenUtils.getScreenSize(context);
        this.downloader = downloader;
        this.maxSize = Math.min(screenSize.getWidth(), screenSize.getHeight());
    }

    public SVGRequestHandler(Downloader downloader, float maxSize) {
        this.downloader = downloader;
        this.maxSize = maxSize;
    }

    @Override
    public boolean canHandleRequest(Request data) {
        String scheme = data.uri.getScheme();
        boolean isRemote = (SCHEME_HTTP.equals(scheme) || SCHEME_HTTPS.equals(scheme));
        return isRemote && data.uri.toString().toLowerCase(Locale.getDefault()).endsWith(".svg");
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {

        Downloader.Response response = downloader.load(request.uri, networkPolicy);
        if (response == null) {
            return null;
        }

        try {
            SVG svg = SVG.getFromInputStream(response.getInputStream());
            float ratio = svg.getDocumentViewBox().width() / svg.getDocumentViewBox().height();
            float width = maxSize * ratio;
            float height = maxSize;
            svg.setDocumentWidth(width);
            svg.setDocumentHeight(height);
            PictureDrawable pictureDrawable = new PictureDrawable(svg.renderToPicture());
            Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(), pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawPicture(pictureDrawable.getPicture());
            return new Result(bitmap, Picasso.LoadedFrom.NETWORK);
        } catch (SVGParseException e) {
        }
        return null;
    }


}
