package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;

public class CheckDataConnectionStatus
{

    private Context mContex;

    public CheckDataConnectionStatus (Context context)
    {
        mContex = context;
    }

    public boolean IsConnected()
    {
        boolean ConnectedCheck;

        ConnectedCheck = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)mContex.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo  = connectivityManager.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected())
        {
            ConnectedCheck=true;
        }
        else
        {
            ConnectedCheck=false;
        }

        return ConnectedCheck;
    }

}
