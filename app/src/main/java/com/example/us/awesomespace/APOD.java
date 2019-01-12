package com.example.us.awesomespace;

import android.graphics.Bitmap;

public class APOD {

    private String mTitle;
    private String mExplanation;
    private String mImageHDURL;
    private String mImageURL;
    private Bitmap mImg;

    /*
     * Create a new APOD object.
     *
     * @param mTitle is the image title
     * @param mExplanation is the image explanation
     * @param mImageHDURL is the image hd URL
     * */
    public APOD(String title, String explanation, String hdurl, String url)
    {
        mTitle = title;
        mExplanation = explanation;
        mImageHDURL = hdurl;
        mImageURL= url;
    }

    /**
     * Get the image title
     */
    public String getImageTitle() {
        return mTitle;
    }

    /**
     * Get the explanation
     */
    public String getExplanation() {
        return mExplanation;
    }

    /**
     * Get the image hd url
     */
    public String getImageHDURL() {
        return mImageHDURL;
    }

    /**
     * Get the image  url
     */
    public String getImageURL() {
        return mImageURL;
    }

    public Bitmap getImg(){
        return mImg;
    }

    public void setImg(Bitmap img){
        mImg = img;
    }
}
