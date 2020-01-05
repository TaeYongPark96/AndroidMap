package com.typark.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private String TAG = "mainActivity";
    private int mapZoom = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        mapFragment.getMapAsync(new OnMapReadyCallback(){
//
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//
//                mMap = googleMap;
//
//                LatLng SEOUL = new LatLng(37.56, 126.97);
//
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(SEOUL);
//                markerOptions.title("서울");
//                markerOptions.snippet("한국의 수도");
//                mMap.addMarker(markerOptions);
//
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
//            }
//        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Location myLocation = getGeoLocation();
                if (myLocation != null) {
                    double lng = myLocation.getLongitude();
                    double lat = myLocation.getLatitude();
                    Log.d(TAG, "longtitude=" + lng + ", latitude=" + lat);

                    LatLng userLocation = new LatLng(lat, lng);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(userLocation);
                    markerOptions.title("유저 위치");
                    markerOptions.snippet("유저의 위치이다!");
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng SEOUL = new LatLng(37.56, 126.97);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, mapZoom));

    }


    public Location getGeoLocation() {
        Location currentLocation = null;

        // check User Permission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request User to location Permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}
                    , 100);
            return null;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (currentLocation == null) {
            Log.d(TAG, "Get Current Location Fail!!!");
        }

        return currentLocation;
    }
}
