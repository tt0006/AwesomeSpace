package com.example.us.awesomespace;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.app.Application;

/** Helper class to fetch data using LiveData and ViewModel*/
public class APODViewModel extends AndroidViewModel {

    private LiveData<APOD> mData;

    public APODViewModel(Application application){
        super(application);
        DataRepository mRepository = DataRepository.getInstance(application.getApplicationContext());
        mData = mRepository.getApodObject();
    }

    /** Method to provide data for observer in MainActivity*/
    LiveData<APOD> getAPODdata(){
        return mData;
    }

}
