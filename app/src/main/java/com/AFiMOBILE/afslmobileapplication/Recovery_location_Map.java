package com.AFiMOBILE.afslmobileapplication;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Recovery_location_Map extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback  {

    private GoogleMap mMap;
    LocationManager locationManager;
    public String mInputAdders;
    public double Palce1_mLatitude , Palce1_mLongTude , Palce2_mLatitude , Palce2_mLongTude;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_location__map);

        //=== Get Location access
        ActivityCompat.requestPermissions(Recovery_location_Map.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //=== Get Input Application No
        Intent intent = getIntent();
        mInputAdders    =   intent.getStringExtra("Adders1");

        //===== Get Client Adders Place
        Geocoder geocoder = new Geocoder(Recovery_location_Map.this , Locale.getDefault());
        String Result = null;
        try {
            List adderslist =   geocoder.getFromLocationName(mInputAdders , 1);

            if (adderslist != null && adderslist.size() > 0)
            {
                Address address = (Address) adderslist.get(0);
                Palce2_mLatitude   =   address.getLatitude();
                Palce2_mLongTude   =   address.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //===============================================

        //===== Get My Palce =====
        GPSTracker gps = new GPSTracker(this);
        int status = 0;
        if(gps.canGetLocation())
        {
            status = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(getApplicationContext());

            if (status == ConnectionResult.SUCCESS)
            {
                Palce1_mLatitude = gps.getLatitude();
                Palce1_mLongTude = gps.getLongitude();
            }
        }

        //============================================
        place1 = new MarkerOptions().position(new LatLng(Palce1_mLatitude, Palce1_mLongTude)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(Palce2_mLatitude, Palce2_mLongTude)).title("Location 2");
        String url = getUrl(place1.getPosition() , place2.getPosition() , "driving");
        new FetchURL(Recovery_location_Map.this).execute(url , "driving");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(place1);
        mMap.addMarker(place2);

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(7.581835732479316, 79.78754208695742);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service



        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
