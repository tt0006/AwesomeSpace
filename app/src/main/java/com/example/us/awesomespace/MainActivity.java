package com.example.us.awesomespace;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView mMediaDate;
    private TextView mMediaTitle;
    private TextView mMediaExplanation;
    private ImageView mImageView;
    private ProgressBar myBar;
    private APODViewModel mModel;
    private int mCurrentYear, mCurrentMonth, mCurrentDay;
    private Date mCurrentVisibleDate;
    private boolean mMenuButtonVisible;
    private String mMediaType;
    private String mYTurl;
    private ImageView mPlayIcon;

    static String REQUEST_URL = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial setup
        mMediaDate = findViewById(R.id.date);
        mMediaTitle = findViewById(R.id.mediaTitle);
        mMediaExplanation = findViewById(R.id.explanation);
        TextView emptyStateTextView = findViewById(R.id.empty_view);
        myBar = findViewById(R.id.loading_spinner);
        mImageView = findViewById(R.id.displayedImage);
        mMenuButtonVisible = false;
        mPlayIcon = findViewById(R.id.playIcon);

        // Get Current calendar object to set initial date values
        final Calendar c = Calendar.getInstance();
        mCurrentYear = c.get(Calendar.YEAR);
        mCurrentMonth = c.get(Calendar.MONTH);
        mCurrentDay = c.get(Calendar.DAY_OF_MONTH);

        // Get the ViewModel
        mModel = ViewModelProviders.of(this).get(APODViewModel.class);

        // Create the observer which updates the UI.
        final Observer<APOD> dataObserver = new Observer<APOD>() {
            @Override
            public void onChanged(@Nullable final APOD newData) {
                // Update the UI when data is changed(updated)
                display(newData);
                myBar.setVisibility(View.GONE);
            }
        };

        // onClick listener for image
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaType.equals("video") && mYTurl != null){
                    // play youtube video in YouTube app or web browser
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mYTurl)));
                }
                else{
                    // Code to show image in full screen:
                    displayFullScreenFragment();
                }
            }
        });


        //Check if network available
        if (isNetworkAvailable()) {
            // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
           mModel.getAPODdata().observe(this, dataObserver);
        }
        else{
            // Set empty state text to display "No network found."
            emptyStateTextView.setText(R.string.no_network);
            //hide progress bar
            myBar.setVisibility(View.GONE);
        }

    }

    private void displayFullScreenFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FullScreenFragmentDialog photoFragment = new FullScreenFragmentDialog();
        photoFragment.setDrawable(mImageView.getDrawable());
        photoFragment.show(fm, "photo_fragment");

    }

    // Menu block-----------------------------------------------------------------------------------
    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle menu dynamically
    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        MenuItem item = menu.findItem(R.id.CalendarBtn);

        if (mMenuButtonVisible) {
            item.setEnabled(true);
            item.getIcon().setAlpha(255);
        } else {
            // disabled
            item.setEnabled(false);
            item.getIcon().setAlpha(130);
        }
        return true;
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.CalendarBtn) {
            // display calendar dialog
            datePicker();
        }
        return super.onOptionsItemSelected(item);
    }
    // Menu block end-------------------------------------------------------------------------------

    //Helper method to check network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Create/update UI
    public void display(APOD apod) {

        // Set text to text fields from apod object
        mMediaTitle.setText(apod.getTitle());
        mMediaExplanation.setText(apod.getExplanation());

        // set APOD date to variable to check later and not reload the same object twice
        mCurrentVisibleDate = parseDate(apod.getMediaDate());
        // set date text to text field to display above the image
        mMediaDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(mCurrentVisibleDate));

        // set data to variables
        // this is used to check what is displayed 'video' or 'image' to change image onClick behavior
        mMediaType = apod.getMediaType();
        // this is used when media type is 'video' to play video in external app
        mYTurl = apod.getMediaURL();

        String imgurl;
        if (mMediaType.equals("video")) {
            String id = QueryUtils.extractYTId(apod.getMediaURL());
            imgurl = String.format("https://img.youtube.com/vi/%s/0.jpg", id);
        } else {
            imgurl = apod.getImageHDURL();
        }
        Glide.with(getApplicationContext())
                .load(imgurl)
                .into(mImageView);

        // add play icon for videos
        if (mMediaType.equals("video")){
            mPlayIcon.setVisibility(View.VISIBLE);
        }
        myBar.setVisibility(View.GONE);

        // enable calendar menu icon
        mMenuButtonVisible = true;
        invalidateOptionsMenu();
    }

    //Helper method to display date picker dialog
    private void datePicker(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                        Date newSelectedDate = parseDate(selectedDate);
                        Date now = new Date();
                        if (newSelectedDate.equals(mCurrentVisibleDate)){
                            Toast.makeText(getApplicationContext(), R.string.not_valid_current_date, Toast.LENGTH_SHORT).show();
                        }
                        else if(now.after(newSelectedDate)) {
                            mModel.newDateApod(String.format("%s&date=%s", REQUEST_URL, selectedDate));

                            //update calendar selected date to display last selection
                            mCurrentYear = year;
                            mCurrentMonth = monthOfYear;
                            mCurrentDay = dayOfMonth;

                            myBar.setVisibility(View.VISIBLE);
                            // hide play icon
                            mPlayIcon.setVisibility(View.INVISIBLE);

                            Glide.with(getApplicationContext()).clear(mImageView);
                            // disable calendar menu icon
                            mMenuButtonVisible = false;
                            invalidateOptionsMenu();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), R.string.not_valid_future_date, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, mCurrentYear, mCurrentMonth, mCurrentDay);
        datePickerDialog.show();
    }

    //Helper method to parse date
    private Date parseDate(String selectedDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = null;
        try{
        strDate = sdf.parse(selectedDate);
        }
        catch (ParseException e){
            Log.e("parseDate", "Fail to parse date");
        }
        return strDate;
    }

}
