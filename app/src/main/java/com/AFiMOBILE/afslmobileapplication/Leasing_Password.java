package com.AFiMOBILE.afslmobileapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class Leasing_Password extends AppCompatActivity {

    private EditText mUsername , mPassword ;
    private TextView mErrormsg;
    private Button  buttonLogin;
     String Userid , Password;
    SQLiteDatabase sqLiteDatabase;
    SqlliteCreateLeasing sqlliteCreateLeasing;
    GlobleClassDetails globleClassDetails;
    CheckDataConnectionStatus checkDataConnectionStatus;
     android.app.AlertDialog progressDialog;
    RequestQueue requestQueue_app;
    LoadToast MsgWait;
    Switch SwRemme;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "userid";
    String mSaveUserId , mLoginSts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leasing__password);

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));


        mUsername       =   findViewById(R.id.txtuserid);
        mPassword       =   findViewById(R.id.txtpassword);
        mErrormsg       =   findViewById(R.id.txterrorshow);
        buttonLogin     =   findViewById(R.id.btnlogin);
        SwRemme         =   findViewById(R.id.remuserid);

        //=== Please Wait Massagwe
        MsgWait = new LoadToast(Leasing_Password.this);
        MsgWait.setText(" Please Wait..." );
        MsgWait.setTextColor(Color.rgb(173,56,109)).setBackgroundColor(Color.WHITE).setProgressColor(Color.BLUE);
        MsgWait.setTextDirection(true);
        MsgWait.setTranslationY(820);

        //========================
        mUsername.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        //=== Set Rember User name
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        mSaveUserId = sharedPreferences.getString(TEXT , "");
        mUsername.setText(mSaveUserId);

        //== Create Sql lite db and globle varible
        sqlliteCreateLeasing        = new SqlliteCreateLeasing(this);
        checkDataConnectionStatus   = new CheckDataConnectionStatus(Leasing_Password.this);
        progressDialog = new SpotsDialog(Leasing_Password.this, R.style.Custom);
        requestQueue_app   = VollySingleton.getInstance(Leasing_Password.this).getRequestQueue();
        requestQueue_app.start();

        buttonLogin.requestFocus();
        //=== Remove And Add the Text hint
        mUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus)
            {
                mUsername.setHint("");
            }
            else
            {
                mUsername.setHint("User Name");
            }
            }
        });


        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus)
            {
                mPassword.setHint("");
            }
            else
            {
                mPassword.setHint("Password");
            }
            }
        });

        //=================================

       buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Userid = "";
                Password = "";
                Userid      =   mUsername.getText().toString();
                Password    =   mPassword.getText().toString();
                mErrormsg.setText("");

                if (Userid.length() != 0)
                {
                    if (Password.length() != 0)
                    {

                        CheckLoginDetails();
                    }
                    else
                    {
                        mErrormsg.setText("Password is Blank");
                    }
                }
                else
                {
                    mErrormsg.setText("User Name Blank.");
                }
            }
        });
    }


    //=== Check Login Details
    public void CheckLoginDetails ()
    {
        sqLiteDatabase = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM USER_MANAGEMENT WHERE TRIM (OFFIER_ID) = '" + Userid.trim() + "'" , null);
        if (cursor.getCount() != 0)
        {
            cursor.moveToFirst();
            String mPassword;
            String Userroll;

            mPassword = cursor.getString(1).trim();
            Userroll = cursor.getString(8);

            //==== Check login User sts
            mLoginSts = cursor.getString(4);

            if (mLoginSts.equals("Y"))
            {
                android.app.AlertDialog.Builder builder_master = new android.app.AlertDialog.Builder(this);
                builder_master.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                builder_master.setMessage("Login is Inactive.");
                builder_master.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });
                builder_master.create();
                builder_master.show();
            }
            else
            {
                if (mPassword.equals(Password))
                {

                    //=== To Assign Globle Varible Data ======
                    // Client Details Insert ===============

                    //=== Run system config
                    GetSystemConfig getSystemConfig = new GetSystemConfig(Leasing_Password.this);
                    getSystemConfig.UpdateSystemConfig();
                    //======================

                    GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
                    String NewDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    globleClassDetails.setUserid(cursor.getString(0));
                    globleClassDetails.setOfficerName(cursor.getString(2));
                    globleClassDetails.setLoginBranch(cursor.getString(3));
                    globleClassDetails.setLoginDate(NewDate);

                    //=== Set Save Sharable Data ("User name")
                    if (SwRemme.isChecked())
                    {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TEXT , mUsername.getText().toString());
                        editor.apply();
                    }
                    //=======================================

                    //==== Check Normal Leasng Screen or RMV Screen

                    if (Userroll.equals("RMV"))
                    {
                        Intent intent = new Intent("android.intent.action.Rmv_Home");
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent("android.intent.action.LeasingHome");
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    MsgWait.error();
                    mErrormsg.setText("Password is Worng");

                }
            }
        }
        else
        {
            postData();
        }
        cursor.close();
        sqLiteDatabase.close();
    }

    protected void onStart()
    {
        super.onStart();
    }

    protected void onDestroy(){
        sqLiteDatabase.close();
        sqlliteCreateLeasing.close();

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

    public void postData()
    {
        boolean mCheckSts = checkDataConnectionStatus.IsConnected();
        if (mCheckSts)
        {
            JSONObject object = new JSONObject();
            //== Create Json File To Send User ID
            try {
                //input your API parameters
                object.put("NEW_USER_ID",Userid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MsgWait.show();
            String url = "http://afimobile.abansfinance.lk/mobilephp/MOBILE-PO-GET-NEWUSER.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            String OFFIER_ID, PWD, OFFICER_NAME, DEPARTMENT, INACTIVE, OFFICER_EPF, OFFICER_EMAIL,
                                    OFF_TYPE, MUSER_ROLL, MBRCODE, MPHONENO;
                            try{

                                JSONArray myjson = response.getJSONArray("TT-NEW-USER");
                                for (int i = 0; i <= myjson.length(); i++)
                                {

                                    JSONObject userid = myjson.getJSONObject(i) ;

                                    OFFIER_ID       =   userid.getString("OFFIER_ID");
                                    PWD             =   userid.getString("PWD");
                                    OFFICER_NAME    =   userid.getString("OFFICER_NAME");
                                    DEPARTMENT      =   userid.getString("DEPARTMENT");
                                    INACTIVE        =   userid.getString("INACTIVE");
                                    OFFICER_EPF     =   userid.getString("OFFICER_EPF");
                                    OFFICER_EMAIL   =   userid.getString("OFFICER_EMAIL");

                                    OFF_TYPE        =   userid.getString("OFFIER_TYPE");
                                    MUSER_ROLL      =   userid.getString("USER_ROLL");
                                    MBRCODE         =   userid.getString("BRANCH_CODE");
                                    MPHONENO        =   userid.getString("PHONE_NO");


                                    sqlliteCreateLeasing.CreateNewUser(OFFIER_ID , PWD , OFFICER_NAME , DEPARTMENT, INACTIVE , OFFICER_EPF , OFFICER_EMAIL , OFF_TYPE ,
                                            MUSER_ROLL , MBRCODE , MPHONENO);
                                    MsgWait.hide();

                                    //=== Call Login Deatils Class
                                    CheckLoginDetails();
                                }
                            }catch(Exception e){}

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String ErrorCode="";
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        ErrorCode = "No Internet Connection;";
                    } else if (error instanceof AuthFailureError) {
                        ErrorCode = "An expired login session";
                    } else if (error instanceof ServerError) {
                        ErrorCode = "Server is down or is unable to process the request;";
                    } else if (error instanceof NetworkError) {
                        ErrorCode = "Very slow internet connection;";
                    } else if (error instanceof ParseError) {
                        ErrorCode = "Client not able to parse(read) the response;";
                    }

                    MsgWait.hide();
                    String ErrorDescription="Responses Failure.(" + ErrorCode + ")";
                    AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Leasing_Password.this );
                    bmyAlert.setMessage(ErrorDescription).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                            .setTitle("Error...")
                            .create();
                    bmyAlert.show();
                }
            });

            requestQueue_app.getCache().clear();
            requestQueue_app.add(jsonObjectRequest);
        }
        else
        {
            MsgWait.hide();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
            builder.setMessage("Data Connection Not available. Please Turn on Connection. ** Note - This is your First Login.It is Need to Mobile Data On.");
            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();

        }
    }
}
