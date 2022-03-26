package com.example.task6.user_auth.sign_fragments.sign_in;

import javax.inject.Inject;

public interface SignInPresenter {
    void checkEmailAndPassword(String email, String password);
    void attachSignInFragment(SignInFragmentInterface signInFragmentInterface);
    void sendEmailErrorToFragment(String emailError);
    void sendPasswordErrorToFragment(String passwordError);

    void openWorkScreen();

    class Base implements SignInPresenter {
        private SignInFragmentInterface signInFragment;
        private final SignInModel.Base signInModel;

        @Inject
        public Base(SignInModel.Base signInModel){
            this.signInModel = signInModel;
            this.signInModel.setPresenterInterface(this);
        }

        @Override
        public void attachSignInFragment(SignInFragmentInterface signInFragmentInterface) {
            this.signInFragment = signInFragmentInterface;
        }

        @Override
        public void checkEmailAndPassword(String email, String password) {
            signInFragment.setEmailError(null);
            signInFragment.setPasswordError(null);
            if (email == null || email.equals("")) {
                signInFragment.setEmailError("Enter E-mail address");
            } else if (password == null || password.length() < 6) {
                signInFragment.setPasswordError("Password too short");
            } else {
                signInModel.signInUserAccount(email, password);
            }
        }

        @Override
        public void sendEmailErrorToFragment(String emailError) {
            signInFragment.setEmailError(emailError);
        }

        @Override
        public void sendPasswordErrorToFragment(String passwordError) {
            signInFragment.setPasswordError(passwordError);
        }

        @Override
        public void openWorkScreen() {
            signInFragment.openWorkScreen();
        }
    }
}
