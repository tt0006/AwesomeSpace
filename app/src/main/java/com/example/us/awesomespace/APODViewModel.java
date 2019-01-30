package com.example.us.awesomespace;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;


public class APODViewModel extends ViewModel {

    /** Query URL */
    private static String currentRequestUrl = MainActivity.REQUEST_URL;
    private static MutableLiveData<APOD> data;

    public void newDateApod(String url){
        currentRequestUrl = url;
        //Reload data using new url
        loadData();
    }

    LiveData<APOD> getAPODdata(){
        if (data == null){
            data = new MutableLiveData<>();
            loadData();
        }
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
