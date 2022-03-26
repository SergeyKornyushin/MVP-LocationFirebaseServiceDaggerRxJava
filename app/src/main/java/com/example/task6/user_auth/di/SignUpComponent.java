package com.example.task6.user_auth.di;

import com.example.task6.user_auth.sign_fragments.sign_up.SignUpFragment;

import dagger.Component;

@AuthActivityScope
@Component (modules = {SignUpModule.class})
public interface SignUpComponent {
    void injectSignUpFragment(SignUpFragment signUpFragment);
}
