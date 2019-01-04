package com.example.us.awesomespace;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    String REQUEST_URL = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kick off an {@link AsyncTask} to perform the network request
        APODAsyncTask task = new APODAsyncTask();
        task.execute(REQUEST_URL);
    }

    /**
     * {@link APODAsyncTask} to perform the network request on a background thread, and then
     * update the UI
     */
    private class APODAsyncTask extends AsyncTask<String, Void, APOD> {

        @Override
        protected APOD doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            return QueryUtils.fetchAPOD(urls[0]);
        }

        /**
         * Update the screen with the given APOD (which was the result of the
         * {@link APODAsyncTask}).
         */
        @Override
        protected void onPostExecute(APOD data) {
            // If there is a valid object {@link APOD}, then update UI with it.
            if (data != null) {
                display(data);
            }
        }
    }

    public void display(APOD apod) {

        TextView imgTitle = findViewById(R.id.imageTitle);
        TextView imgExplanation = findViewById(R.id.imageExplanation);

        imgTitle.setText(apod.getImageTitle());
        imgExplanation.setText(apod.getExplanation());
    }

}
