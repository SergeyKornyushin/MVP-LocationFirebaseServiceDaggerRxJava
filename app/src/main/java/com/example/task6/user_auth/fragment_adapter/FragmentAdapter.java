package com.example.task6.user_auth.fragment_adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.task6.user_auth.sign_fragments.sign_in.SignInFragment;
import com.example.task6.user_auth.sign_fragments.sign_up.SignUpFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setSignInFragment(SignInFragment signInFragment) {
        this.signInFragment = signInFragment;
    }

    public void setSignUpFragment(SignUpFragment signUpFragment) {
        this.signUpFragment = signUpFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return signInFragment;
        } else {
            return signUpFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
