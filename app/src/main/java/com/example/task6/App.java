package com.example.task6;

import android.app.Application;
import android.util.Log;

import com.example.task6.main_screen.di.DaggerLocationComponent;
import com.example.task6.main_screen.di.LocationComponent;
import com.example.task6.main_screen.presenter.LocationPresenter;
import com.orhanobut.hawk.Hawk;

import javax.inject.Inject;

public class App extends Application {
    private LocationComponent locationComponent;

    @Inject
    LocationPresenter.Base presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
    }

    public LocationPresenter.Base getWorkPresenter(){
        if (locationComponent == null){
            locationComponent = DaggerLocationComponent.create();
            locationComponent.injectApp(this);
        }
        return presenter;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}