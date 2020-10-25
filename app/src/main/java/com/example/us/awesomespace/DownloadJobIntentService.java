package com.example.us.awesomespace;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

/**
 * An {@link JobIntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class DownloadJobIntentService extends JobIntentService {

    private static final int JOB_ID = 1;
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, DownloadJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // get String URL from intent extra
        String downloadUrl = intent.getStringExtra("downloadUrl");
        if (downloadUrl == null){
            return;
        }
        // run method to download data using service
        QueryUtils.fetchAPOD(getApplicationContext(), downloadUrl);
    }
}