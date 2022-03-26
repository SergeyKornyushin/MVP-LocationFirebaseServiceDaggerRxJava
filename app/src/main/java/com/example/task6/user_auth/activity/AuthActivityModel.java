package com.example.task6.user_auth.activity;

import com.google.firebase.auth.FirebaseAuth;

public interface AuthActivityModel {
    boolean checkAuthUser();

    class Base implements AuthActivityModel {

        @Override
        public boolean checkAuthUser(){
            return FirebaseAuth.getInstance().getCurrentUser() != null;
        }
    }
}