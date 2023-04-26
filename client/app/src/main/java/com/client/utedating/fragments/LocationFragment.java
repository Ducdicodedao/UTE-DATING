package com.client.utedating.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.client.utedating.R;
import com.client.utedating.activities.InitialActivity;
import com.client.utedating.models.Locat;
import com.client.utedating.models.User;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LocationFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    Button buttonSubmitLocation;
    InitialActivity initialActivity;
    private FusedLocationProviderClient mFusedLocationClient;
    double latitude = 0.0;
    double longitude = 0.0;
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(initialActivity);

        getLastLocation(view);
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
            if (isLocationEnabled()) {

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
                            Log.e("TAG","Longitude: " + location.getLongitude() + "");
                            latitude = location.getLatitude();
                            longitude =  location.getLongitude();

                            SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
                            User user = sharedPreferencesClient.getUserInfo("user");


                            Map<String, Object> l = new HashMap<>();
                            l.put("type", "Point");
                            l.put("coordinates", new Double[]{longitude, latitude});
                            user.setLocation(l);

                            sharedPreferencesClient.putUserInfo("user", user);

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
                Toast.makeText(initialActivity, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }
    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(initialActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(initialActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) initialActivity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

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
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude =  mLastLocation.getLongitude();
        }
    };
    private void requestPermissions() {
        ActivityCompat.requestPermissions(initialActivity, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
    }
}