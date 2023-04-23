package com.example.braguia.ui;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.braguia.R;
import com.example.braguia.model.DirectionsAsyncTask;
import com.example.braguia.model.LocationService;
import com.example.braguia.model.Trail;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private LocationService locationService;
    private Button trail_buton;
    private Trail trail;
    private Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        trail = (Trail) getIntent().getSerializableExtra("trail_info");
        getLocationPermission();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);

        Intent serviceIntent = new Intent(this, LocationService.class);
        serviceIntent.setAction(LocationService.ACTION_START_LOCATION_SERVICE);
        startService(serviceIntent);

        trail_buton = findViewById(R.id.trail);
        trail_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Trails_activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (locationPermissionGranted) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            List<Trail.Edge> list = trail.getEdges();

            for (int i = 0; i < list.size(); i++) {
                Trail.Point start = list.get(i).getpoint_start();
                Trail.Point end = list.get(i).getpoint_end();
                double latitude_start = start.getLat();
                double longitude_start = start.getLng();
                double latitude_end = end.getLat();
                double longitude_end = end.getLng();
                LatLng location_start = new LatLng(latitude_start, longitude_start);
                LatLng location_end = new LatLng(latitude_end, longitude_end);
                mMap.addMarker(new MarkerOptions().position(location_start).title(start.getName()));

                DirectionsAsyncTask task = new DirectionsAsyncTask(MainActivity.this,mMap,location_start,location_end);
                task.execute();
            }

            Trail.Point end = list.get(list.size() - 1).getpoint_end();
            double latitude = end.getLat();
            double longitude = end.getLng();
            LatLng location_end = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(location_end).title(end.getName()));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(MainActivity.this, pagina_inicial.class);
                    startActivity(intent);
                    return true;
                }
            });

        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
    }

    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocationService.ACTION_LOCATION_UPDATE)) {
                double latitude = intent.getDoubleExtra("latitude", 0);
                double longitude = intent.getDoubleExtra("longitude", 0);
                LatLng location = new LatLng(latitude, longitude);
                if (currentLocationMarker == null) {
                    currentLocationMarker = mMap.addMarker(new MarkerOptions().position(location));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                } else {
                    currentLocationMarker.setPosition(location);
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(locationReceiver, new IntentFilter(LocationService.ACTION_LOCATION_UPDATE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(locationReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(this, LocationService.class);
        serviceIntent.setAction(LocationService.ACTION_STOP_LOCATION_SERVICE);
        startService(serviceIntent);
    }
}

