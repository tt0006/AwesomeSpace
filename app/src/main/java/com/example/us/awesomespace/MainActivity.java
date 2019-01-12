package com.example.us.awesomespace;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private ProgressBar myBar;
    private Bitmap mBitmap;
    Context context = this;

    String REQUEST_URL = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mEmptyStateTextView = findViewById(R.id.empty_view);
        myBar = findViewById(R.id.loading_spinner);
        mImageView = findViewById(R.id.imageHdurl);

        APODViewModel model = ViewModelProviders.of(this).get(APODViewModel.class);
        model.setUrl(REQUEST_URL);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to show image in full screen:
                new PhotoFull(context, view, mBitmap);
            }
        });


        //Check if network available
        if (isNetworkAvailable()) {

            model.getAPODdata().observe(this, apod ->{display(apod);});
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

    public void display(APOD apod) {

        TextView imgTitle = findViewById(R.id.imageTitle);
        TextView imgExplanation = findViewById(R.id.imageExplanation);

        imgTitle.setText(apod.getImageTitle());
        imgExplanation.setText(apod.getExplanation());


        //String downloadImageURL = apod.getImageHDURL();
        mBitmap = apod.getImg();
        if (mBitmap != null){
            mImageView.setImageBitmap(mBitmap);
        //Picasso.get().load(downloadImageURL).into(mImageView);
        }
        else{
            //TO DO Set default image
        }

        myBar.setVisibility(View.GONE);
    }

}
