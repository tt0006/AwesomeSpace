package com.example.us.awesomespace;

import android.graphics.drawable.Drawable;

/**
 * Helper class to store APOD data
 */
public class APOD {

    private String mTitle;
    private String mExplanation;
    private String mImageHDURL;
    private String mMediaURL;
    private String mDate;
    private String mMediaType;
    private Drawable mImg;

    /**
     * Create a new APOD object.
     *
     * @param title is the media title
     * @param explanation is the media explanation
     * @param hdurl is the image hd URL
     * @param url is the media URL
     * @param date is the media date
     * @param mediaType is the media type
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
    public Drawable getImg(){
        return mImg;
    }

    /**
     * Set image
     */
    public void setImg(Drawable img){
        mImg = img;
    }
}
