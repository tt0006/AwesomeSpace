package com.example.us.awesomespace;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


public class APODViewModel extends ViewModel {

    /** Query URL */
    private static String mUrl;
    private static MutableLiveData<APOD> data;

    public void setUrl(String url){
        mUrl=url;
        //Reload data only if it is not initial setUrl call
        if (data != null) { loadData();}
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
            if (mUrl == null) {
                return null;
            }
            // Perform network request for data and process the response.
            APOD apod = QueryUtils.fetchAPOD(mUrl);
            //Log.i("APODViewModelAct1", String.format("%s %s mediaUrl:%s" ,apod.getMediaType(), apod.getImageHDURL(), apod.getMediaURL()));

            //load image
            if (apod.getImageHDURL() != null && apod.getImageHDURL().length()>0){
                Bitmap img = QueryUtils.fetchImage(apod.getImageHDURL());
                apod.setImg(img);
            } else if(apod.getMediaType().equals("video") && apod.getMediaURL() != null){
                //Log.i("APODViewModelAct1", String.format("%s" , "inside"));
                Bitmap img = QueryUtils.fetchThumbnail(apod.getMediaURL());
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
