package com.hearatale.sightword.utils;

import com.hearatale.sightword.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.hearatale.sightword.utils.glide.GlideApp;

import java.io.ByteArrayOutputStream;

import jp.wasabeef.blurry.Blurry;

public class ImageHelper {

    // with place holder resId
    public static void load(ImageView imageView, String url, @DrawableRes int placeHolderResId) {

        GlideApp.with(Application.Context)
                .load(url)
                .placeholder(placeHolderResId)
                .dontAnimate()
                .into(imageView);

    }

    // with place holder resId
    public static void load(ImageView imageView, Drawable drawable) {

        GlideApp.with(Application.Context)
                .load(drawable)
                .dontAnimate()
                .into(imageView);

    }


    // with place holder resId
    public static void load(ImageView imageView, Bitmap bitmap) {

        GlideApp.with(Application.Context)
                .load(bitmap)
                .dontAnimate()
                .into(imageView);

    }

    // with place holder resId
    public static void load(ImageView imageView, Drawable drawable, int width, int height) {

        GlideApp.with(Application.Context)
                .load(drawable)
                .override(width, height)
                .dontAnimate()
                .into(imageView);

    }

    // without place holder
    public static void load(ImageView imageView, String url) {
        GlideApp.with(Application.Context)
                .load(url)
                .dontAnimate()
                .into(imageView);
    }

    public static void load(ImageView imageView, String url, @DrawableRes int placeHolderResId, int width, int height) {
        GlideApp.with(Application.Context)
                .load(url)
                .override(width, height)
                .placeholder(placeHolderResId)
                .dontAnimate()
                .into(imageView);
    }

    public static void load(ImageView imageView, String url, int width, int height) {
        GlideApp.with(Application.Context)
                .load(url)
                .override(width, height)
                .dontAnimate()
                .into(imageView);
    }

    // progressBar
    public static void load(final ImageView imageView, final String url, final ProgressBar progressBar) {

        progressBar.setVisibility(View.VISIBLE);

        GlideApp.with(Application.Context)
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);

    }

    public static void load(Context context, String url, final ImageView imageView, @DrawableRes final int placeHolder) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        imageView.setImageResource(placeHolder);
                    }
                });
    }

//    public static Bitmap convert(String base64Str) throws IllegalArgumentException
//    {
//        byte[] decodedBytes = Base64.decode(
//                base64Str.substring(base64Str.indexOf(",")  + 1),
//                Base64.DEFAULT
//        );
//
//        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//    }

    public static Bitmap convert(String base64Str) {
        try {
            byte[] encodeByte = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convert(Bitmap bitmap) {
        if (null == bitmap) {
            return "";
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap getBitmapFromView(View view) {
        return getBitmapFromView(view, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap getBitmapFromView(View view, Bitmap.Config config) {

//        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            view.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
//            view.setDrawingCacheEnabled(false);
//            return bitmap;
//        }

        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), config != null ? config : Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
//        else
        //does not have background drawable, then draw white background on the canvas
//            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


    public static void blurImageView(Bitmap bitmap, ImageView imageView) {
        if (bitmap == null || imageView == null) {
            return;
        }

        Blurry.with(Application.Context)
                .radius(25)
                .sampling(1)
                .from(bitmap)
                .into(imageView);
    }

    /**
     * Return the compressed bitmap using sample size.
     *
     * @param src        The source of bitmap.
     * @param sampleSize The sample size.
     * @return the compressed bitmap
     */

    public static Bitmap compressBySampleSize(final Bitmap src, final int sampleSize) {
        return compressBySampleSize(src, sampleSize, false);
    }

    /**
     * Return the compressed bitmap using sample size.
     *
     * @param src        The source of bitmap.
     * @param sampleSize The sample size.
     * @param recycle    True to recycle the source of bitmap, false otherwise.
     * @return the compressed bitmap
     */
    public static Bitmap compressBySampleSize(final Bitmap src,
                                              final int sampleSize,
                                              final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) src.recycle();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }
}
