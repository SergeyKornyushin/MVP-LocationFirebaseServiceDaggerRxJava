package com.example.task6.user_auth.di;

import com.example.task6.user_auth.activity.AuthActivityModel;
import com.example.task6.user_auth.activity.AuthActivityPresenter;
import com.example.task6.user_auth.sign_fragments.sign_in.SignInModel;
import com.example.task6.user_auth.sign_fragments.sign_in.SignInPresenter;
import com.example.task6.user_auth.sign_fragments.sign_in.SignInFragment;
import com.example.task6.user_auth.sign_fragments.sign_up.SignUpModel;
import com.example.task6.user_auth.sign_fragments.sign_up.SignUpPresenter;
import com.example.task6.user_auth.sign_fragments.sign_up.SignUpFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class AuthModule {

    @Provides
    @AuthActivityScope
    SignUpFragment provideSignUpFragment(){
        return new SignUpFragment();
    }

    @Provides
    @AuthActivityScope
    SignInFragment provideSignInFragment(){
        return new SignInFragment();
    }

    @Provides
    @AuthActivityScope
    AuthActivityPresenter.Base provideAuthActivityPresenter(AuthActivityModel.Base authActivityModel){
        return new AuthActivityPresenter.Base(authActivityModel);
    }

    @Provides
    @AuthActivityScope
    AuthActivityModel.Base provideAuthActivityModel(){
        return new AuthActivityModel.Base();
    }
}
