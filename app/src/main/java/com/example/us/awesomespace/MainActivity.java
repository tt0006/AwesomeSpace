package com.example.us.awesomespace;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<APOD> {

    private ImageView mImageView;
    private TextView mEmptyStateTextView;
    private ProgressBar myBar;

    String REQUEST_URL = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBar = findViewById(R.id.loading_spinner);
        mEmptyStateTextView = findViewById(R.id.empty_view);


        //Check if network available
        if (isNetworkAvailable()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(1, null, this);
        }
        else{
            // Set empty state text to display "No network found."
            mEmptyStateTextView.setText(R.string.no_network);
            //hide progress bar
            myBar.setVisibility(View.GONE);

        }

    }

    //Helper method implementation to check network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public Loader<APOD> onCreateLoader(int id, Bundle args) {
        //Create a new loader for the given URL
        APODLoader myLoader = new APODLoader(MainActivity.this, REQUEST_URL);
        return myLoader;
    }

    @Override
    public void onLoadFinished(Loader<APOD> loader, APOD data) {
        myBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_data);
        // If there is a valid object {@link APOD}, then update UI with it.
        if (data != null) {
            display(data);
            mEmptyStateTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<APOD> loader) {

    }

    public void display(APOD apod) {

        TextView imgTitle = findViewById(R.id.imageTitle);
        TextView imgExplanation = findViewById(R.id.imageExplanation);

        imgTitle.setText(apod.getImageTitle());
        imgExplanation.setText(apod.getExplanation());
    }

}
