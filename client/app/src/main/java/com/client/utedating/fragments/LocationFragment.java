package com.client.utedating.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.models.User;
import com.client.utedating.utils.MySharedPreferences;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;


public class LocationFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    AppCompatButton buttonSubmitLocation;
    TextView textViewStep;
    InitialActivity initialActivity;
    double latitude = 0.0;
    double longitude = 0.0;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    };

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialActivity = (InitialActivity) getActivity();
        buttonSubmitLocation = view.findViewById(R.id.buttonSubmitLocation);
        textViewStep = view.findViewById(R.id.textViewStep);
        Shader shader = new LinearGradient(0,0,0,textViewStep.getLineHeight(),
                getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent), Shader.TileMode.REPEAT);
        textViewStep.getPaint().setShader(shader);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(initialActivity);

        buttonSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // method to get the location
                getLastLocation(view);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private synchronized void getLastLocation(View view) {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
//            if (isLocationEnabled()) {
//
                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Log.e("TAG", "Latitude: " + location.getLatitude() + "");
                            Log.e("TAG", "Longitude: " + location.getLongitude() + "");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            User user = MySharedPreferences.getUserInfo(getActivity(), "user");


                            Map<String, Object> l = new HashMap<>();
                            l.put("type", "Point");
                            l.put("coordinates", new Double[]{longitude, latitude});
                            user.setLocation(l);

                            MySharedPreferences.putUserInfo(getActivity(), "user", user);

                            initialActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainerView, AvatarFragment.class, null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("name") // name can be null
                                    .commit();

                            Log.e("TAG", user.toString());
                        }
                    }
                });
//            } else {
//                Toast.makeText(initialActivity, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    // method to check for permissions
    private boolean checkPermissions() {
        // If we want background location
        // on Android 10.0 and higher,
        // use:
        Log.e("TAG", "checkPermissions");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            // Code for Android versions higher than Android 10
            return ActivityCompat.checkSelfPermission(initialActivity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return ActivityCompat.checkSelfPermission(initialActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(initialActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationEnabled() {
        Log.e("TAG", "isLocationEnabled");
        LocationManager locationManager = (LocationManager) initialActivity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        Log.e("TAG", "requestNewLocationData");
        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(initialActivity);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

    }

    private void requestPermissions() {
        Log.e("TAG", "requestPermissions");
        requestPermissions( new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Log.e("TAG", "Latitude: " + location.getLatitude() + "");
                            Log.e("TAG", "Longitude: " + location.getLongitude() + "");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            User user = MySharedPreferences.getUserInfo(getActivity(), "user");


                            Map<String, Object> l = new HashMap<>();
                            l.put("type", "Point");
                            l.put("coordinates", new Double[]{longitude, latitude});
                            user.setLocation(l);

                            MySharedPreferences.putUserInfo(getActivity(), "user", user);

                            initialActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainerView, AvatarFragment.class, null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("name") // name can be null
                                    .commit();

                            Log.e("TAG", user.toString());
                        }
                    }
                });
            } else {
                Toast.makeText(initialActivity, "Bạn chưa cho phép truy cập địa chỉ", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
            }
        }
    }
}
