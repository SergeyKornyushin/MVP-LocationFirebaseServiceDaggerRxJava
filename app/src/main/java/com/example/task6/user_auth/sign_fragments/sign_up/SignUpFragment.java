package com.example.task6.user_auth.sign_fragments.sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.task6.databinding.FragmentSignUpBinding;
import com.example.task6.main_screen.view.LocationActivity;
import com.example.task6.user_auth.di.DaggerSignUpComponent;
import com.example.task6.user_auth.di.SignUpComponent;

import javax.inject.Inject;

public class SignUpFragment extends Fragment
        implements SignUpFragmentInterface{

    @Inject
    SignUpPresenter.Base presenter;

    private FragmentSignUpBinding signUpBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SignUpComponent signUpComponent = DaggerSignUpComponent.create();
        signUpComponent.injectSignUpFragment(this);
        presenter.attachSignUpFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        signUpBinding = FragmentSignUpBinding.inflate(getLayoutInflater());
        return signUpBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signUpBinding.btnSignUp.setOnClickListener(view1 -> {
            presenter.checkEmailAndPassword(signUpBinding.tilEmailEt.getText().toString(),
                    signUpBinding.tilPassEt.getText().toString(),
                    signUpBinding.tilSecondPassEt.getText().toString());
        });
    }

    @Override
    public void setEmailError(String emailError) {
        signUpBinding.tilEmail.setError(emailError);
    }

    @Override
    public void setPasswordError(String passwordError) {
        signUpBinding.tilPass.setError(passwordError);
        signUpBinding.tilSecondPass.setError(passwordError);
    }

    @Override
    public void openWorkScreen() {
        Intent intent = new Intent(getActivity(), LocationActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
