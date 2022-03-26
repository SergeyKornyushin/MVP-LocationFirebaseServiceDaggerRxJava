package com.example.task6.main_screen.model;

import static com.example.task6.VariableStorage.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.task6.App;
import com.example.task6.main_screen.presenter.LocationPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ForegroundWorker extends Worker
        implements LocationListener {
    public ForegroundWorker(@NonNull Context context,
                            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        presenter = ((App) context.getApplicationContext()).getWorkPresenter();
    }

    private final Context context;
    private final LocationManager locationManager;
    private final LocationPresenter.Base presenter;

    @NonNull
    @Override
    public Result doWork() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,
                    this, Looper.getMainLooper());
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
                    this, Looper.getMainLooper());
        }
        return Result.success();
    }

    public static boolean isConnectedToInternet(@NonNull Context _context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void passHawkItemsToFirestore() {
        Single<Object> single = Single.create(emitter -> {
            ArrayList<String> tempList = Hawk.get(KEYS_IN_HAWK);
            if (tempList.size() > 0) {
                for (String item : tempList) {
                    if (Hawk.contains(item)) {
                        HashMap<String, String> tempMap = Hawk.get(item);
                        FirebaseFirestore.getInstance()
                                .collection(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                .document(tempMap.get(TIMESTAMP))
                                .set(tempMap).addOnCompleteListener(task -> {
                            Hawk.delete(item);
                            tempList.remove(item);
                            Hawk.put(KEYS_IN_HAWK, tempList);
                        });
                    } else {
                        tempList.remove(item);
                        Hawk.put(KEYS_IN_HAWK, tempList);
                    }
                }
                Hawk.put(KEYS_IN_HAWK, tempList);
            }
            emitter.onSuccess(Hawk.get(KEYS_IN_HAWK));
        });
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) { }

                    @Override
                    public void onSuccess(@NonNull Object o) { }

                    @Override
                    public void onError(@NonNull Throwable e) { }
                });
    }

    public void saveLocationInHawk(HashMap<String, String> coordinates) {
        Completable completable = Completable.fromAction(() -> {
            ArrayList<String> tempList = Hawk.get(KEYS_IN_HAWK);
            if (!tempList.contains(coordinates.get(TIMESTAMP))
                    || !Hawk.contains(coordinates.get(TIMESTAMP))) {
                tempList.add(coordinates.get(TIMESTAMP));
                Hawk.put(KEYS_IN_HAWK, tempList);
                Hawk.put(coordinates.get(TIMESTAMP), coordinates);
            }
        });
        completable.subscribeOn(Schedulers.io()).subscribe();
    }

    public void passCurrentLocationToFirestore(HashMap<String, String> coordinates) {
        FirebaseFirestore.getInstance()
                .collection(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .document(coordinates.get(TIMESTAMP))
                .set(coordinates);
    }

    @SuppressLint("SimpleDateFormat")
    public void passToFirestoreOrSaveCurrentLocation(Location location) {
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        HashMap<String, String> coordinates = new HashMap<>();
        coordinates.put(TIMESTAMP, date + " | " + time);
        coordinates.put("Latitude", String.valueOf(location.getLatitude()));
        coordinates.put("Longitude", String.valueOf(location.getLongitude()));
        coordinates.put("Date", date);
        coordinates.put("Time", time);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (isConnectedToInternet(context)) {
                passHawkItemsToFirestore();
                passCurrentLocationToFirestore(coordinates);
            } else {
                saveLocationInHawk(coordinates);
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        passToFirestoreOrSaveCurrentLocation(location);
        presenter.setCurrentCoordinates(String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()));

    }

    //this methods need for API 26 and less, without them app is crashing
    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}