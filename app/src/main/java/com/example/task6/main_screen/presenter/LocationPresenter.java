package com.example.task6.main_screen.presenter;

import androidx.work.PeriodicWorkRequest;

import com.example.task6.main_screen.model.LocationModel;
import com.example.task6.main_screen.view.LocationActivityInterface;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;
import javax.inject.Singleton;

public interface LocationPresenter {
    void viewAttach(LocationActivityInterface workView);
    String checkUserEmail();
    PeriodicWorkRequest getWorkRequestFromModel();
    void setCurrentCoordinates(String latitude, String longitude);
    void firebaseSignOut();

    @Singleton
    class Base implements LocationPresenter {

        @Inject
        public Base(LocationModel.Base locationModel) {
            this.locationModel = locationModel;
        }

        private final LocationModel.Base locationModel;
        private LocationActivityInterface workView;

        @Override
        public void viewAttach(LocationActivityInterface workView) {
            this.workView = workView;
        }

        @Override
        public void setCurrentCoordinates(String latitude, String longitude) {
            if (latitude.length() > 9) latitude = latitude.substring(0, 9);
            if (longitude.length() > 9) longitude = longitude.substring(0, 9);
            workView.setCurrentCoordinates(latitude, longitude);
        }

        @Override
        public String checkUserEmail() {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (FirebaseAuth.getInstance().getCurrentUser().getEmail().length() > 18) {
                    return FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0, 18) + "...";
                } else {
                    return FirebaseAuth.getInstance().getCurrentUser().getEmail();
                }
            } else {
                return "";
            }
        }

        @Override
        public PeriodicWorkRequest getWorkRequestFromModel() {
            return locationModel.getWorkRequest();
        }

        @Override
        public void firebaseSignOut() {
            locationModel.firebaseSignOut();
        }
    }
}
