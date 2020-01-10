package com.typark.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    private Location currLocation = new Location("");;
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
                getGeoLocation();
                if (currLocation != null) {
                    double lng = currLocation.getLongitude();
                    double lat = currLocation.getLatitude();
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


    public void getGeoLocation() {

        // check User Permission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request User to location Permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}
                    , 100);
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d(TAG, "isGPSEnabled="+ isGPSEnabled);
        Log.d(TAG, "isNetworkEnabled="+ isNetworkEnabled);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        if (currLocation == null ||
                currLocation.getLatitude()==0.0 || currLocation.getLongitude()==0.0) {
            Log.d(TAG, "Get Current Location Fail!!!");
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currLocation.setLatitude(location.getLatitude());
            currLocation.setLongitude(location.getLongitude());
        }

//        locationManager.removeUpdates(locationListener);

    }


    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        currLocation.setLatitude(location.getLatitude());
        currLocation.setLongitude(location.getLongitude());
        Log.d(TAG,"latitude: "+ lat +", longitude: "+ lng);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG,"onStatusChanged");
    }

    public void onProviderEnabled(String provider) {
        Log.d(TAG,"onProviderEnabled");
    }

    public void onProviderDisabled(String provider) {
        Log.d(TAG,"onProviderDisabled");
    }
}
