package com.example.us.awesomespace;

public class APOD {

    private String mTitle;
    private String mExplanation;
    private String mImageHDURL;

    /*
     * Create a new APOD object.
     *
     * @param mTitle is the image title
     * @param mExplanation is the image explanation
     * @param mImageHDURL is the image hd URL
     * */
    public APOD(String title, String explanation, String hdurl)
    {
        mTitle = title;
        mExplanation = explanation;
        mImageHDURL = hdurl;
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
}
