package com.example.task6.main_screen.model;

import static com.example.task6.VariableStorage.BACKGROUND_WORK_REQUEST;
import static com.example.task6.VariableStorage.KEYS_IN_HAWK;

import android.util.Log;

import androidx.work.PeriodicWorkRequest;

import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;


public interface LocationModel {
    PeriodicWorkRequest getWorkRequest();
    void firebaseSignOut();

    @Singleton
    class Base implements LocationModel {

        @Inject
        public Base(){
            if (!Hawk.contains(KEYS_IN_HAWK)) {
                Hawk.put(KEYS_IN_HAWK, new ArrayList<String>());
            }
        }

        @Override
        public PeriodicWorkRequest getWorkRequest() {
            return new PeriodicWorkRequest.Builder(ForegroundWorker.class,
                    10, TimeUnit.MINUTES, 15, TimeUnit.MINUTES)
                    .addTag(BACKGROUND_WORK_REQUEST).build();
        }

        @Override
        public void firebaseSignOut() {
            FirebaseAuth.getInstance().signOut();
        }
    }
}
