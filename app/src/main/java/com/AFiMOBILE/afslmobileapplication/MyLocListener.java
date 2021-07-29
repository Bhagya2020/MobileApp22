package com.AFiMOBILE.afslmobileapplication;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location)
    {
        if (location != null)
        {
            Log.e("Latitude - 1" , "" + location.getLatitude());
            Log.e("Loantude - 1" , "" + location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int Status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
