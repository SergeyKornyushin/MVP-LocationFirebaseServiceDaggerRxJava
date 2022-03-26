package com.example.task6.main_screen.model;

import static com.example.task6.VariableStorage.BACKGROUND_WORK_REQUEST;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.work.WorkManager;

public class BRForegroundStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WorkManager.getInstance(context).cancelAllWorkByTag(BACKGROUND_WORK_REQUEST);
        Intent serviceIntent = new Intent(context, ForegroundService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}
