package com.example.task6.user_auth.sign_fragments.sign_up;

import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Objects;

import javax.inject.Inject;

public interface SignUpModel {
    void setPresenterInterface(SignUpPresenter presenterInterface);
    void checkAuthUser();
    void createUserAccount(String email, String password);

    class Base implements SignUpModel {

        @Inject
        public Base(){}

        private SignUpPresenter presenterInterface;

        @Override
        public void setPresenterInterface(SignUpPresenter presenterInterface) {
            this.presenterInterface = presenterInterface;
        }

        @Override
        public void checkAuthUser() {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                presenterInterface.openWorkScreen();
            }
        }

        @Override
        public void createUserAccount(String email, String password) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            checkAuthUser();
                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidCredentialsException exception) {
                                presenterInterface.sendEmailErrorToFragment("The email address is badly formatted.");
                            } catch (FirebaseTooManyRequestsException exception) {
                                presenterInterface.sendEmailErrorToFragment("To many failed login attempts. Resetting your password or try again later.");
                            } catch (FirebaseAuthUserCollisionException exception) {
                                presenterInterface.sendEmailErrorToFragment("The email address is already registered");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}