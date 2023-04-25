package com.example.braguia.ui;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.braguia.model.Trail.Point;
import com.example.braguia.R;
import com.example.braguia.model.DirectionsAsyncTask;
import com.example.braguia.model.LocationService;
import com.example.braguia.model.Trail;
import com.example.braguia.model.find_Point;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private LatLng location;
    private GoogleMap mMap;
    private Circle currentLocationMarker;
    private Button trail_buton;
    private Button center;
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
        if (locationPermissionGranted) {
        center = findViewById(R.id.center);
        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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

            DirectionsAsyncTask task = new DirectionsAsyncTask(MainActivity.this, mMap, location_start, location_end);
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
                find_Point fp = new find_Point();
                Context context = MainActivity.this;
                Intent intent = new Intent(MainActivity.this, Marker_info.class);
                intent.putExtra("point_info", fp.encontraPonto(marker.getTitle(), list));
                context.startActivity(intent);
                return true;
            }
        });
    }



    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
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

    private BroadcastReceiver locationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           if (intent.getAction().equals(LocationService.ACTION_LOCATION_UPDATE)) {
                double latitude = intent.getDoubleExtra("latitude", 0);
                double longitude = intent.getDoubleExtra("longitude", 0);
                location = new LatLng(latitude, longitude);
                if (currentLocationMarker == null) {
                    CircleOptions circleOptions = new CircleOptions()
                            .fillColor(Color.BLUE)
                            .strokeWidth(0)
                            .center(location)
                            .radius(15);
                    currentLocationMarker = mMap.addCircle(circleOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                } else {
                    currentLocationMarker.setCenter(location);
                }

                List<Trail.Edge> list = trail.getEdges();
                for (int i = 0; i < list.size(); i++) {
                    Point start = list.get(i).getpoint_start();
                    latitude = start.getLat();
                    longitude = start.getLng();
                    Location pointOfInterest = new Location("");
                    pointOfInterest.setLatitude(latitude);
                    pointOfInterest.setLongitude(longitude);

                    Location currentLocation = new Location("");
                    currentLocation.setLatitude(location.latitude);
                    currentLocation.setLongitude(location.longitude);

                    float distance = currentLocation.distanceTo(pointOfInterest);
                    if (distance < 1000) {
                        addNotification(start);
                    }
                }

                Point end = list.get(list.size()-1).getpoint_start();
                latitude = end.getLat();
                longitude = end.getLng();
                Location pointOfInterest = new Location("");
                pointOfInterest.setLatitude(latitude);
                pointOfInterest.setLongitude(longitude);

                Location currentLocation = new Location("");
                currentLocation.setLatitude(location.latitude);
                currentLocation.setLongitude(location.longitude);

                float distance = currentLocation.distanceTo(pointOfInterest);
                if (distance < 1000) {
                    addNotification(end);
                }
            }
        }
    };

    private void addNotification(Point point) {
        String channelId = "default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Nearby point of interest")
                .setContentText("Are you close to "+point.getName())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, Marker_info.class);
        notificationIntent.putExtra("point_info",point);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("message", "This is a notification message");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(LocationService.ACTION_LOCATION_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(locationUpdateReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationUpdateReceiver != null) {
            try {
                unregisterReceiver(locationUpdateReceiver);
            } catch (IllegalArgumentException e) {
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(this, LocationService.class);
        serviceIntent.setAction(LocationService.ACTION_STOP_LOCATION_SERVICE);
        startService(serviceIntent);
    }
}

