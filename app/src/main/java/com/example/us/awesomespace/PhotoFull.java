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

public class PhotoFull extends PopupWindow {

    View view;
    Context mContext;
    PhotoView photoView;
    ViewGroup parent;

    public PhotoFull(Context ctx, View v, Bitmap bitmap) {
        super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate( R.layout.image_full, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }
        this.mContext = ctx;
        this.view = getContentView();
        this.setAnimationStyle(R.style.PopupAnimation);
        setOutsideTouchable(true);
        setFocusable(true);

        photoView = view.findViewById(R.id.image);
        photoView.setMaximumScale(6);
        parent = (ViewGroup) photoView.getParent();

        photoView.setImageBitmap(bitmap);
        showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}