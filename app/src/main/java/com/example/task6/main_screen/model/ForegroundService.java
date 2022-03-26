package com.example.task6.main_screen.model;

import static com.example.task6.VariableStorage.FOREGROUND_WORK_REQUEST;
import static com.example.task6.VariableStorage.KEYS_IN_HAWK;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.task6.R;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ForegroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.N_MR1){
            Hawk.init(this).build();
            if (!Hawk.contains(KEYS_IN_HAWK)) {
                Hawk.put(KEYS_IN_HAWK, new ArrayList<String>());
            }
        }

        WorkManager.getInstance(this).enqueue(new PeriodicWorkRequest.Builder(ForegroundWorker.class,
                10, TimeUnit.MINUTES, 15, TimeUnit.MINUTES)
                .addTag(FOREGROUND_WORK_REQUEST).build());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final String CHANNEL_ID = "Foreground Service ID";
            getSystemService(NotificationManager.class)
                    .createNotificationChannel(new NotificationChannel(
                    CHANNEL_ID, CHANNEL_ID,
                    NotificationManager.IMPORTANCE_LOW
            ));
            Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentText("Track location in background")
                    .setContentTitle("Location Tracker")
                    .setSmallIcon(R.drawable.ic_foreground);
            startForeground(1001, notification.build());
        }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
