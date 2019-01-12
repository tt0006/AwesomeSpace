package com.example.us.awesomespace;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.os.AsyncTask;


public class APODViewModel extends ViewModel {

    /** Query URL */
    private String mUrl;

    private MutableLiveData<APOD> data;

    public void setUrl(String url){
        mUrl=url;
    }

    LiveData<APOD> getAPODdata(){
        if (data == null){
            data = new MutableLiveData<>();
            loadData();
        }
        return data;
    }

    private void loadData(){new AsyncTask<Void,Void,APOD> () {
        @Override
        protected APOD doInBackground(Void... voids) {
            if (mUrl == null) {
                return null;
            }
            // Perform network request for data and process the response.
            APOD apod = QueryUtils.fetchAPOD(mUrl);
            if (apod.getImageHDURL() != null){
                Bitmap img = QueryUtils.fetchImage(apod.getImageHDURL());
                apod.setImg(img);
            }
            return apod;
        }
        @Override
        protected void onPostExecute(APOD apod) {
            data.setValue(apod);
        }
    }.execute();
    }

}
