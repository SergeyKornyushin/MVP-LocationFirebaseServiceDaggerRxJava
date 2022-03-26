package com.example.task6.user_auth.activity;

public interface AuthActivityPresenter {
    void setViewAndModelInterfaces(AuthActivityInterface activityInterface);

    void checkAuthUser();

    void openWorkScreen();

    class Base implements AuthActivityPresenter {

        public Base(AuthActivityModel.Base authActivityModel){
            this.authActivityModel = authActivityModel;
        }

        private final AuthActivityModel authActivityModel;
        private AuthActivityInterface activityInterface;

        public void setViewAndModelInterfaces(AuthActivityInterface activityInterface) {
            this.activityInterface = activityInterface;
        }

        public void checkAuthUser() {
            if (authActivityModel.checkAuthUser()){
                activityInterface.openWorkScreen();
            }
        }

        public void openWorkScreen() {
            activityInterface.openWorkScreen();
        }
    }
}