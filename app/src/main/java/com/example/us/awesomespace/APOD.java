package com.example.us.awesomespace;

import android.graphics.Bitmap;

public class APOD {

    private String mTitle;
    private String mExplanation;
    private String mImageHDURL;
    private String mMediaURL;
    private String mDate;
    private String mMediaType;
    private Bitmap mImg;

    /*
     * Create a new APOD object.
     *
     * @param mTitle is the image title
     * @param mExplanation is the image explanation
     * @param mImageHDURL is the image hd URL
     * @param mMediaURL is the media URL
     * @param mDate is the image date
     * @param mMediaType is the media type
     * */
    public APOD(String title, String explanation, String hdurl, String url, String date, String mediaType)
    {
        mTitle = title;
        mExplanation = explanation;
        mImageHDURL = hdurl;
        mMediaURL = url;
        mDate = date;
        mMediaType = mediaType;
    }

    /**
     * Get title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Get explanation
     */
    public String getExplanation() {
        return mExplanation;
    }

    /**
     * Get image hd url
     */
    public String getImageHDURL() {
        return mImageHDURL;
    }

    /**
     * Get media url
     */
    public String getMediaURL() {
        return mMediaURL;
    }

    /**
     * Get media type
     */
    public String getMediaType() {
        return mMediaType;
    }

    /**
     * Get media date value
     */
    public String getMediaDate() {
        return mDate;
    }

    /**
     * Get image
     */
    public Bitmap getImg(){
        return mImg;
    }

    /**
     * Set image
     */
    public void setImg(Bitmap img){
        mImg = img;
    }
}
