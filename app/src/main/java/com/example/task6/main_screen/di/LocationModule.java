package com.example.task6.main_screen.di;

import com.example.task6.main_screen.model.LocationModel;
import com.example.task6.main_screen.presenter.LocationPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationModule {

    @Provides
    @Singleton
    LocationPresenter.Base provideWorkPresenter(LocationModel.Base locationModel){
        return new LocationPresenter.Base(locationModel);
    }

    @Provides
    @Singleton
    LocationModel.Base provideLocationModel(){
        return new LocationModel.Base();
    }
}
