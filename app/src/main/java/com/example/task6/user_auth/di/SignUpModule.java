package com.example.task6.user_auth.di;

import com.example.task6.user_auth.sign_fragments.sign_up.SignUpModel;
import com.example.task6.user_auth.sign_fragments.sign_up.SignUpPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SignUpModule {

    @Provides
    @AuthActivityScope
    SignUpPresenter.Base provideSignUnPresenter(SignUpModel.Base signUpModel){
        return new SignUpPresenter.Base(signUpModel);
    }

    @Provides
    @AuthActivityScope
    SignUpModel.Base provideSignUpModel(){
        return new SignUpModel.Base();
    }
}
