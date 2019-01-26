package com.example.us.awesomespace;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.github.chrisbanes.photoview.PhotoView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

class PhotoFull extends PopupWindow {

    View view;
    Context mContext;
    PhotoView photoView;
    ViewGroup parent;

    PhotoFull(Context ctx, View v, Bitmap bitmap) {
        super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate( R.layout.image_full, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        /*
        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }*/
        //this.mContext = ctx;

        // add animation to popup window
        this.setAnimationStyle(R.style.PopupAnimation);

        //setOutsideTouchable(true);
        setFocusable(true);

        this.view = getContentView();
        photoView = view.findViewById(R.id.image);
        photoView.setMaximumScale(6);
        //parent = (ViewGroup) photoView.getParent();

        photoView.setImageBitmap(bitmap);
        showAtLocation(v, Gravity.CENTER, 0, 0);
    }
/*
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    */
}