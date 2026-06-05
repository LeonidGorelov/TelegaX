package org.telegram;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import org.telegram.messenger.NotificationsService;

public class KeepAliveJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Context context = this;

        Intent intent = new Intent(context, NotificationsService.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent);
        } else {
            context.startService(intent);
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
