package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class RecoverySearchData
{
    public Context mContex;
    public SqlliteCreateRecovery sqlliteCreateRecovery_DataSearch;
    public RequestQueue requestQueue_final;
    public String PHP_URL_SQL , MainResponceCode;
    private android.app.AlertDialog progressDialog;



    public RecoverySearchData (Context context)
    {
        mContex = context;
        sqlliteCreateRecovery_DataSearch = new SqlliteCreateRecovery(mContex);

        //==== Create Globle Varible
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        progressDialog = new SpotsDialog(mContex, R.style.Custom);
        progressDialog.setTitle("Searching Data ...");

        final Cache cache = new DiskBasedCache(mContex.getCacheDir(), 1024 * 1024);

        Network network = new BasicNetwork(new HurlStack());
        requestQueue_final = new RequestQueue(cache, network);
        requestQueue_final.getCache().clear();

    }




}
