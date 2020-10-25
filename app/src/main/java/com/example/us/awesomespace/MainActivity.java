package com.example.us.awesomespace;

import android.app.DatePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView mMediaDate;
    private TextView mMediaTitle;
    private TextView mMediaExplanation;
    private ImageView mImageView;
    private ProgressBar myBar;
    private Date mCurrentVisibleDate;
    private boolean mMenuButtonVisible;
    private String mMediaType;
    private String mVideoURL;
    private ImageView mPlayIcon;
    private Context mContext = this;
    private DataRepository mRepository;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial setup
        mMediaDate = findViewById(R.id.date);
        mMediaTitle = findViewById(R.id.mediaTitle);
        mMediaExplanation = findViewById(R.id.explanation);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        myBar = findViewById(R.id.loading_spinner);
        mImageView = findViewById(R.id.displayedImage);
        mMenuButtonVisible = false;
        mPlayIcon = findViewById(R.id.playIcon);

        // onClick listener for image
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaType.equals("video") && mVideoURL != null){
                    // play youtube video in YouTube app or web browser
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mVideoURL)));
                }
                else{
                    // Code to show image in full screen:
                    displayFullScreenFragment();
                }
            }
        });


        //Check if network available
        if (isNetworkAvailable()) {
            mRepository = DataRepository.getInstance(getApplicationContext());
            setupViewModel();
        }
        else{
            // Set empty state text to display "No network found."
            mEmptyStateTextView.setText(R.string.no_network);
            //hide progress bar
            myBar.setVisibility(View.GONE);
        }

    }

    private void setupViewModel() {
        APODViewModel viewModel = new ViewModelProvider(this).get(APODViewModel.class);
        viewModel.getAPODdata().observe(this, new Observer<APOD>() {
            @Override
            public void onChanged(@Nullable APOD apodObject) {
                // Update the UI when data is changed(updated)
                if (apodObject != null){
                    display(apodObject);}
                else{
                    Toast.makeText(mContext, R.string.null_APOD, Toast.LENGTH_SHORT).show();
                    // enable calendar menu icon
                    mMenuButtonVisible = true;
                    invalidateOptionsMenu();
                }
            }
        });
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

        mEmptyStateTextView.setText("");

        // Set text to text fields from apod object
        mMediaTitle.setText(apod.getTitle());
        mMediaExplanation.setText(apod.getExplanation());

        // set APOD date to variable to check later and not reload the same object twice
        mCurrentVisibleDate = parseDate(apod.getMediaDate());
        // set date text to text field to display above the image in systems date format
        mMediaDate.setText(SimpleDateFormat.getDateInstance().format(mCurrentVisibleDate));


        // set data to variables
        // this is used to check what is displayed 'video' or 'image' to change image onClick behavior
        mMediaType = apod.getMediaType();
        // this is used when media type is 'video' to play video in external app
        mVideoURL = apod.getMediaURL();

        String imgurl;
        if (mMediaType.equals("video")) {
            String id = QueryUtils.extractYTId(apod.getMediaURL());
            imgurl = String.format("https://img.youtube.com/vi/%s/0.jpg", id);
        } else {
            imgurl = apod.getImageHDURL();
        }

        // cover vimeo video case
        if (mMediaType.equals("video") && mVideoURL.contains("vimeo.com/")){
            mImageView.setImageResource(R.drawable.vimeo_logo);
        } else{
            Glide.with(mContext)
                    .load(imgurl)
                    // show loading spinner code
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            myBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            myBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(mImageView);
        }

        // add play icon for videos
        if (mMediaType.equals("video")){
            mPlayIcon.setVisibility(View.VISIBLE);
        }
        //myBar.setVisibility(View.GONE);

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
                            Toast.makeText(mContext, R.string.not_valid_current_date, Toast.LENGTH_SHORT).show();
                        }
                        else if(now.after(newSelectedDate)) {
                            //clear text fields
                            mPlayIcon.setVisibility(View.INVISIBLE);
                            mMediaTitle.setText("");
                            mMediaExplanation.setText("");
                            mMediaDate.setText("");
                            Glide.with(mContext).clear(mImageView);

                            //update calendar selected date to display last selection
                            mRepository.updateCalendar(dayOfMonth, monthOfYear, year);

                            //load new data
                            if (isNetworkAvailable()) {
                                mRepository.loadApodForNewDate(mContext, selectedDate);
                                myBar.setVisibility(View.VISIBLE);
                            }
                            else{
                                // Set empty state text to display "No network found."
                                mEmptyStateTextView.setText(R.string.no_network);
                                //hide progress bar
                                myBar.setVisibility(View.GONE);
                            }
                            // disable calendar menu icon
                            mMenuButtonVisible = false;
                            invalidateOptionsMenu();
                        }
                        else{
                            Toast.makeText(mContext, R.string.not_valid_future_date, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, mRepository.getSelectedYear(), mRepository.getSelectedMonth(), mRepository.getSelectedDay());
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
