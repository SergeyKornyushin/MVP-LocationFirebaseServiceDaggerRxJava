package com.example.task6.main_screen.di;

import com.example.task6.App;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (modules = {LocationModule.class})
public interface LocationComponent {
    void injectApp(App app);
}
