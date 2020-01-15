package com.example.us.awesomespace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class DataRepository {

    private static DataRepository sInstance;
    private static MutableLiveData<APOD> mApodObject;
    private static String currentRequestUrl = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

    private int selectedDay, selectedMonth, selectedYear;

    private DataRepository(Context context){
        // Get Current calendar object to set initial date values
        Calendar c = Calendar.getInstance();
        selectedYear = c.get(Calendar.YEAR);
        selectedMonth = c.get(Calendar.MONTH);
        selectedDay = c.get(Calendar.DAY_OF_MONTH);

        downloadApod(context, currentRequestUrl);
    }

    public static DataRepository getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(context);
                }
            }
        }
        return sInstance;
    }

    LiveData<APOD> getApodObject(){
        if (mApodObject == null){
            mApodObject = new MutableLiveData<>();
        }
        return mApodObject;
    }

    private void downloadApod(Context context, String url){
        Intent intentToLoad = new Intent(context, DownloadJobIntentService.class);
        intentToLoad.putExtra("downloadUrl", url);
        DownloadJobIntentService.enqueueWork(context, intentToLoad);

    }

    /** Method to load data for new url*/
    void loadApodForNewDate(Context context, String selectedDate){
        //create new url with new date value
        String url = String.format("%s&date=%s", currentRequestUrl, selectedDate);
        //Reload data using new url
        downloadApod(context, url);
    }

    void insertData(APOD apod){
        mApodObject.postValue(apod);
    }

    void updateCalendar(int day, int month, int year){
        selectedDay = day;
        selectedMonth = month;
        selectedYear = year;
    }

    int getSelectedDay(){
        return selectedDay;
    }

    int getSelectedMonth(){
        return selectedMonth;
    }

    int getSelectedYear(){
        return selectedYear;
    }

}
