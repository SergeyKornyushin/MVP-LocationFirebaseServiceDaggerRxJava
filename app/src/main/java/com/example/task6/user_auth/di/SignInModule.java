package com.example.task6.user_auth.di;

import com.example.task6.user_auth.sign_fragments.sign_in.SignInModel;
import com.example.task6.user_auth.sign_fragments.sign_in.SignInPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SignInModule {

    @Provides
    @AuthActivityScope
    SignInPresenter.Base provideSignInPresenter(SignInModel.Base signInModel){
        return new SignInPresenter.Base(signInModel);
    }

    @Provides
    @AuthActivityScope
    SignInModel.Base provideSignInModel(){
        return new SignInModel.Base();
    }
}
