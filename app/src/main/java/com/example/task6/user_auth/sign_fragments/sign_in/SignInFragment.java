package com.example.task6.user_auth.sign_fragments.sign_in;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.task6.databinding.FragmentSignInBinding;
import com.example.task6.main_screen.view.LocationActivity;
import com.example.task6.user_auth.di.DaggerSignInComponent;
import com.example.task6.user_auth.di.SignInComponent;

import javax.inject.Inject;

public class SignInFragment extends Fragment
        implements SignInFragmentInterface {

    @Inject
    SignInPresenter.Base presenter;

    private FragmentSignInBinding signInBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SignInComponent signInComponent = DaggerSignInComponent.create();
        signInComponent.injectSignInFragment(this);
        presenter.attachSignInFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        signInBinding = FragmentSignInBinding.inflate(getLayoutInflater());
        return signInBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signInBinding.btnSignIn.setOnClickListener(view1 -> {
            signInBinding.tilEmailEt.setText("test@gmail.com");
            signInBinding.tilPassEt.setText("123456");
            presenter.checkEmailAndPassword(signInBinding.tilEmailEt.getText().toString(),
                    signInBinding.tilPassEt.getText().toString());
        });
    }

    @Override
    public void setEmailError(String emailError) {
        signInBinding.tilEmail.setError(emailError);
    }

    @Override
    public void setPasswordError(String passwordError) {
        signInBinding.tilPass.setError(passwordError);
    }

    @Override
    public void openWorkScreen() {
        Intent intent = new Intent(getActivity(), LocationActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}