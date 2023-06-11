package com.example.braguia.model;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;

import java.util.ArrayList;
import java.util.List;

public class DirectionsAsyncTask extends AsyncTask<Void, Void, DirectionsResult> {

    private GeoApiContext mGeoApiContext;
    private LatLng mOrigin;
    private LatLng mDestination;
    private GoogleMap mMap;
    private Context mContext;

    public DirectionsAsyncTask(Context context,GoogleMap map, LatLng origin, LatLng destination) {
        mContext = context;
        mMap = map;
        mGeoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyCCC_fAp_0bloz8XP2Hd-9-f6dIvTF0KpU")
                .build();
        mOrigin = origin;
        mDestination = destination;


    }

    @Override
    protected DirectionsResult doInBackground(Void... params) {
        DirectionsApiRequest request = DirectionsApi.newRequest(mGeoApiContext)
                .origin(new com.google.maps.model.LatLng(mOrigin.latitude, mOrigin.longitude))
                .destination(new com.google.maps.model.LatLng(mDestination.latitude, mDestination.longitude));

        try {
            return request.await();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(DirectionsResult result) {
        if (result != null) {
            List<LatLng> path = new ArrayList<>();
            List<com.google.maps.model.LatLng> directionsPath = result.routes[0].overviewPolyline.decodePath();
            for (com.google.maps.model.LatLng latLng : directionsPath) {
                path.add(new LatLng(latLng.lat, latLng.lng));
            }

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);
            polylineOptions.addAll(path);
            mMap.addPolyline(polylineOptions);
        } else {
            Toast.makeText(mContext, "Erro ao obter direções", Toast.LENGTH_SHORT).show();
        }
    }
}

