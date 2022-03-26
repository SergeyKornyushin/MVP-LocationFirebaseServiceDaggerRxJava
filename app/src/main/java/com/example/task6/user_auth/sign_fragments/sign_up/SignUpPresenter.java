package com.example.task6.user_auth.sign_fragments.sign_up;

import javax.inject.Inject;

public interface SignUpPresenter {
    void checkEmailAndPassword(String email, String password, String confirmPassword);
    void attachSignUpFragment(SignUpFragmentInterface signInFragmentInterface);
    void sendEmailErrorToFragment(String emailError);
    void openWorkScreen();

    class Base implements SignUpPresenter {

        private SignUpFragmentInterface signUpFragment;
        private final SignUpModel signUpModel;

        @Inject
        public Base(SignUpModel.Base signUpModel){
            this.signUpModel = signUpModel;
            this.signUpModel.setPresenterInterface(this);
        }

        @Override
        public void attachSignUpFragment(SignUpFragmentInterface fragmentInterface) {
            this.signUpFragment = fragmentInterface;
        }

        @Override
        public void checkEmailAndPassword(String email, String password, String confirmPassword) {
            signUpFragment.setEmailError(null);
            signUpFragment.setPasswordError(null);
            if (email == null || email.equals("")) {
                signUpFragment.setEmailError("Enter your E-mail address");
            } else if (password == null || password.length() < 6) {
                signUpFragment.setPasswordError("Password too short");
            } else if(!password.equals(confirmPassword)){
                signUpFragment.setPasswordError("Passwords do not match");
            } else {
                signUpModel.createUserAccount(email, password);
            }
        }

        @Override
        public void sendEmailErrorToFragment(String emailError) {
            signUpFragment.setEmailError(emailError);
        }

        @Override
        public void openWorkScreen() {
            signUpFragment.openWorkScreen();
        }
    }
}
