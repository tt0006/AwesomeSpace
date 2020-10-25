package com.example.us.awesomespace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/** Helper class to deal with data*/
public class DataRepository {

    private static DataRepository mInstance;
    private static MutableLiveData<APOD> mApodObject;
    private static final String mRequestUrl = "DEMO";

    private int mSelectedDay, mSelectedMonth, mSelectedYear;

    // private constructor to ensure that instance can be created only via method
    private DataRepository(Context context){

        // date values initialisation
        Calendar c = Calendar.getInstance();
        mSelectedYear = c.get(Calendar.YEAR);
        mSelectedMonth = c.get(Calendar.MONTH);
        mSelectedDay = c.get(Calendar.DAY_OF_MONTH);

        // load initial data
        downloadApod(context, mRequestUrl);
    }

    /** Static method to get class instance
     * Create new if it is null or return existed one*/
    public static DataRepository getInstance(final Context context) {
        if (mInstance == null) {
            synchronized (DataRepository.class) {
                if (mInstance == null) {
                    mInstance = new DataRepository(context);
                }
            }
        }
        return mInstance;
    }

    /** Method to get LiveData APOD object to ViewModel class*/
    LiveData<APOD> getApodObject(){
        if (mApodObject == null){
            mApodObject = new MutableLiveData<>();
        }
        return mApodObject;
    }

    /** Method to start background download with provided url*/
    private void downloadApod(Context context, String url){
        Intent intentToLoad = new Intent(context, DownloadJobIntentService.class);
        intentToLoad.putExtra("downloadUrl", url);
        DownloadJobIntentService.enqueueWork(context, intentToLoad);
    }

    /** Method to load data for new url*/
    void loadApodForNewDate(Context context, String selectedDate){
        //create new url with new date value
        String url = String.format("%s&date=%s", mRequestUrl, selectedDate);
        //Reload data using new url
        downloadApod(context, url);
    }

    /** Method to insert object created using downloaded data from service to LiveData*/
    void insertData(APOD apod){
        mApodObject.postValue(apod);
    }

    /** Method to update calendar values to selected date in date picker dialog*/
    void updateCalendar(int day, int month, int year){
        mSelectedDay = day;
        mSelectedMonth = month;
        mSelectedYear = year;
    }

    /** Methods to return saved date values*/
    int getSelectedDay(){
        return mSelectedDay;
    }

    int getSelectedMonth(){
        return mSelectedMonth;
    }

    int getSelectedYear(){
        return mSelectedYear;
    }

}
