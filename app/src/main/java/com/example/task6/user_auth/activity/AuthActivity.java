package com.example.task6.user_auth.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task6.databinding.ActivityAuthBinding;
import com.example.task6.user_auth.di.DaggerUserAuthComponent;
import com.example.task6.user_auth.fragment_adapter.FragmentAdapter;
import com.example.task6.main_screen.view.LocationActivity;
import com.example.task6.user_auth.di.UserAuthComponent;
import com.example.task6.user_auth.sign_fragments.sign_in.SignInFragment;
import com.example.task6.user_auth.sign_fragments.sign_up.SignUpFragment;
import com.google.android.material.tabs.TabLayoutMediator;

import javax.inject.Inject;

public class AuthActivity extends AppCompatActivity
        implements AuthActivityInterface {
    private ActivityAuthBinding binding;

    @Inject
    SignInFragment signInFragment;

    @Inject
    SignUpFragment signUpFragment;

    @Inject
    AuthActivityPresenter.Base activityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserAuthComponent authComponent = DaggerUserAuthComponent.create();
        authComponent.injectAuthActivity(this);

        activityPresenter.setViewAndModelInterfaces(this);
//        activityModel.setPresenterInterface(activityPresenter);
        activityPresenter.checkAuthUser();

        initVPager();
    }

    private void initVPager() {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        binding.vPager.setAdapter(fragmentAdapter);

        fragmentAdapter.setSignInFragment(signInFragment);
        fragmentAdapter.setSignUpFragment(signUpFragment);

        String[] tabs = {"Sign In", "Registration"};
        new TabLayoutMediator(binding.tabLayout, binding.vPager,
                (tab, position) -> tab.setText(tabs[position])).attach();
    }

    @Override
    public void openWorkScreen() {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
        finish();
    }
}