package com.example.us.awesomespace;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/** Helper class to fetch data using LiveData and ViewModel*/
class APODViewModel extends ViewModel {

    // save selected date in ViewModel to save data over device rotation
    public int selectedDay = 0, selectedMonth = 0, selectedYear = 0;

    /** Query URL */
    private static String currentRequestUrl = MainActivity.REQUEST_URL;
    private static MutableLiveData<APOD> data;

    /** Method to load data for new url*/
    void newDateApod(String url){
        //update current url with new value
        currentRequestUrl = url;
        //Reload data using new url
        loadData();
    }

    /** Method to provide data for observer in MainActivity*/
    LiveData<APOD> getAPODdata(){
        // if there is no data
        if (data == null){
            data = new MutableLiveData<>();
            loadData();
        }
        // return new or existing data
        return data;
    }

    private static void loadData(){new AsyncTask<Void,Void,APOD> () {
        @Override
        protected APOD doInBackground(Void... voids) {
            if (currentRequestUrl == null) {
                return null;
            }
            // Perform network request for data and process the response.
            return QueryUtils.fetchAPOD(currentRequestUrl);
        }
        @Override
        protected void onPostExecute(APOD apod) {
            data.setValue(apod);
        }
    }.execute();
    }

}
