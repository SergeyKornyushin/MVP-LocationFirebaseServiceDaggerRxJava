package com.example.task6.user_auth.sign_fragments.sign_in;

import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

import javax.inject.Inject;

public interface SignInModel {
    void setPresenterInterface(SignInPresenter.Base presenterInterface);
    void signInUserAccount(String email, String password);

    class Base implements SignInModel {

        @Inject
        public Base(){}

        private SignInPresenter.Base presenterInterface;

        @Override
        public void setPresenterInterface(SignInPresenter.Base presenterInterface) {
            this.presenterInterface = presenterInterface;
        }

        @Override
        public void signInUserAccount(String email, String password) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                presenterInterface.openWorkScreen();
                            }
                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException exception) {
                                presenterInterface.sendEmailErrorToFragment("Incorrect user name");
                            } catch (FirebaseAuthInvalidCredentialsException exception) {
                                if (task.getException().toString().endsWith("The email address is badly formatted.")) {
                                    presenterInterface.sendEmailErrorToFragment("The email address is badly formatted.");
                                } else if (task.getException().toString()
                                        .endsWith("The password is invalid or the user does not have a password.")) {
                                    presenterInterface.sendPasswordErrorToFragment("Incorrect Password");
                                }
                            } catch (FirebaseTooManyRequestsException exception) {
                                presenterInterface.sendEmailErrorToFragment("To many failed login attempts. Resetting your password or try again later.");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
