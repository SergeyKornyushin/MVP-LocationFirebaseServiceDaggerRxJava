package com.example.task6.main_screen.view;

import static com.example.task6.VariableStorage.*;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.WorkManager;

import com.example.task6.App;
import com.example.task6.databinding.ActivityMainBinding;
import com.example.task6.main_screen.model.*;
import com.example.task6.main_screen.presenter.*;
import com.example.task6.user_auth.activity.AuthActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LocationActivity extends AppCompatActivity
        implements LocationActivityInterface, LocationPermissionInterface {
    private ActivityMainBinding mainBinding;
    private LocationPresenter.Base workPresenter;

    private void init() {
        workPresenter = ((App) getApplication()).getWorkPresenter();
        workPresenter.viewAttach(this);

        setSupportActionBar(mainBinding.mainToolbar);
        getSupportActionBar().setTitle(workPresenter.checkUserEmail());

        checkLocationPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
        WorkManager.getInstance(this).cancelAllWorkByTag(FOREGROUND_WORK_REQUEST);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        init();

        mainBinding.btnLogoutToolbar.setOnClickListener(view -> {
            WorkManager.getInstance(this).cancelAllWork();
            workPresenter.firebaseSignOut();
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        });

        mainBinding.btnLocationToolbar.setOnClickListener(view -> {
            checkLocationPermission();
        });
    }


    @Override
    public void setCurrentCoordinates(String latitude, String longitude) {
        mainBinding.tvCurrentLatitude.setText(latitude);
        mainBinding.tvCurrentLongitude.setText(longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RQ_PERMISSIONS_FOR_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED
                    && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                            || !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        askUserForOpeningAppSetting();
                    }
                }
            } else {
                WorkManager.getInstance(this).enqueue(workPresenter.getWorkRequestFromModel());
            }
        }
    }

    @Override
    public void askUserForOpeningAppSetting() {
        Intent appSettingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        if (getPackageManager().resolveActivity(appSettingIntent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Application need all location permissions")
                    .setMessage("If you don't grand the permission to the Application, it won't work")
                    .setPositiveButton("Give", (dialogInterface, i) ->
                            startActivity(appSettingIntent))
                    .setNegativeButton("cancel", (dialogInterface, i) ->
                            dialogInterface.dismiss());
            builder.create().show();
        }
    }

    @Override
    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_DENIED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                askUserForOpeningAppSetting();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        RQ_PERMISSIONS_FOR_LOCATION);
            }
        } else {
            WorkManager.getInstance(this).enqueue(workPresenter.getWorkRequestFromModel());
        }
    }

    @Override
    protected void onStop() {
        if (!isChangingConfigurations() && FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("START_SERVICE");
            broadcastIntent.setClass(this, BRForegroundStarter.class);
            this.sendBroadcast(broadcastIntent);
        }
        super.onStop();
    }
}