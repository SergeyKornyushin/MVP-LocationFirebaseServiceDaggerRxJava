package com.example.task6.user_auth.di;

import com.example.task6.user_auth.sign_fragments.sign_in.SignInFragment;

import dagger.Component;

@AuthActivityScope
@Component (modules = {SignInModule.class})
public interface SignInComponent {
    void injectSignInFragment(SignInFragment signInFragment);
}
