package com.example.task6.user_auth.di;

import com.example.task6.user_auth.activity.AuthActivity;

import dagger.Component;

@Component (modules = {AuthModule.class})
@AuthActivityScope
public interface UserAuthComponent {
    void injectAuthActivity(AuthActivity activity);
}
