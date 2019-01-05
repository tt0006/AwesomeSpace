package com.example.us.awesomespace;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Helper loader class
 */

public class APODLoader extends AsyncTaskLoader<APOD> {

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link APODLoader}.
     * @param context of the activity
     * @param url to load data from
     */
    public APODLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public APOD loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the HTTP request for earthquake data and process the response.
        APOD apod = QueryUtils.fetchAPOD(mUrl);
        return apod;
    }
}