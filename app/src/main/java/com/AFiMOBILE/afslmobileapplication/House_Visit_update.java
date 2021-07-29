package com.AFiMOBILE.afslmobileapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class House_Visit_update extends AppCompatActivity {
    private TextView mTxtAppnp, mTxtClname, mTxtNic, mTxtAdders, mTxtAdders2, mTxtLocas , mTxtSaveData;
    private Button btnGetLoc , mSave , BtnFinsh;
    public SqlliteCreateLeasing sqlliteCreateLeasing_house_visit;
    public String mLitude="" , mLongTude="" , mDraftApplication="";
    public String PHP_URL_SQL="" , LoginUser="" , LoginDate = "" , LoginBranch= "";

    private LocationManager locationManager;
    private LocationListener locationListener;
    private AlertDialog progressDialog;
    private RequestQueue mQueue;
    private CheckDataConnectionStatus checkDataConnectionStatus;
    public static Toolbar toolbar;
    private Handler handler;
    private TextView ConnectionSts;
    private double current_lattitude ,  current_longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house__visit_update);

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));


        mTxtAppnp       =   (TextView) findViewById(R.id.tappno);
        mTxtClname      =   (TextView) findViewById(R.id.txtclname);
        mTxtNic         =   (TextView) findViewById(R.id.txtclnic);
        mTxtAdders      =   (TextView) findViewById(R.id.txadders);
        mTxtAdders2     =   (TextView) findViewById(R.id.txadders2);
        btnGetLoc       =   (Button) findViewById(R.id.btnlocastion);
        mTxtSaveData    =   (TextView)findViewById(R.id.txtSaveLoca);
        mSave           =   (Button)findViewById(R.id.btnSave);
        BtnFinsh        =   (Button)    findViewById(R.id.btnfinash);


        sqlliteCreateLeasing_house_visit = new SqlliteCreateLeasing(this);
        checkDataConnectionStatus = new CheckDataConnectionStatus(this);


        //=== Get Location access
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //===== Create Volly Request Ques. =======
        mQueue   = VollySingleton.getInstance(this).getRequestQueue();

        //=== Check Data Connection status Toolbra every 5 second.
        //setSupportActionBar(toolbar);
        ConnectionSts = (TextView) findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();


        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        progressDialog = new SpotsDialog(House_Visit_update.this, R.style.Custom);

        //====== Get Application Details
        Intent intent = getIntent();
        mTxtAppnp.setText(intent.getStringExtra("ApplicationNo"));
        SQLiteDatabase sqLiteDatabase_house = sqlliteCreateLeasing_house_visit.getReadableDatabase();
        Cursor cursor_house = sqLiteDatabase_house.rawQuery("SELECT CL_NIC , CL_FULLY_NAME , CL_ADDERS_1 , CL_ADDERS_2 , CL_ADDERS_3 , CL_ADDERS_4 " +
                "FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mTxtAppnp.getText() + "'", null);
        if (cursor_house.getCount() != 0) {
            cursor_house.moveToFirst();
            mTxtNic.setText(cursor_house.getString(0));
            mTxtClname.setText(cursor_house.getString(1));
            mTxtAdders.setText(cursor_house.getString(2) + "," + cursor_house.getString(3));
            mTxtAdders2.setText(cursor_house.getString(4) + "," + cursor_house.getString(5));
        }
        cursor_house.close();
        sqLiteDatabase_house.close();

        //== Load Save Data ====
        mDraftApplication = "";
        SQLiteDatabase sqLiteDatabase_dataLoad = sqlliteCreateLeasing_house_visit.getReadableDatabase();
        Cursor cursor_load = sqLiteDatabase_dataLoad.rawQuery("SELECT * FROM LE_GEO_TAG_UPDATE WHERE APP_REF_NO = '" + mTxtAppnp.getText() + "'" , null );
        if (cursor_load.getCount() != 0)
        {
            cursor_load.moveToFirst();
            mTxtSaveData.setText("**  LATITUDE = " + cursor_load.getString(3) + "\n" + "**  LONGITUDE = " + cursor_load.getString(4)) ;
            mLitude = cursor_load.getString(3);
            mLongTude = cursor_load.getString(4);

            mSave.setEnabled(false);
            mSave.setBackgroundResource(R.drawable.normalbuttondisable);

            btnGetLoc.setEnabled(false);
            btnGetLoc.setBackgroundResource(R.drawable.normalbuttondisable);

            BtnFinsh.setEnabled(true);
            BtnFinsh.setBackgroundResource(R.drawable.normalbutton);
            
        }
        else
        {
            Cursor cursor_get_draft = sqLiteDatabase_dataLoad.rawQuery("SELECT CURENT_LATTITUDE , CURRENT_LONGGITUDE FROM DRAFT_APPLICATION WHERE APP_REF_NO = '" + mTxtAppnp.getText() + "' and GEO_FLG_UPDATE = ''" , null);
            if (cursor_get_draft.getCount() !=0)
            {
                cursor_get_draft.moveToFirst();
                mTxtSaveData.setText("**  LATITUDE = " + cursor_get_draft.getString(0) + "\n" + "**  LONGITUDE = " + cursor_get_draft.getString(1)) ;
                mLitude = cursor_get_draft.getString(0);
                mLongTude = cursor_get_draft.getString(0);


                mDraftApplication="D";
                btnGetLoc.setEnabled(false);
                btnGetLoc.setBackgroundResource(R.drawable.normalbuttondisable);

                mSave.setEnabled(true);
                mSave.setBackgroundResource(R.drawable.normalbutton);

                BtnFinsh.setEnabled(false);
                BtnFinsh.setBackgroundResource(R.drawable.normalbuttondisable);
            }
            else
            {
                mSave.setEnabled(true);
                mSave.setBackgroundResource(R.drawable.normalbutton);

                BtnFinsh.setEnabled(false);
                BtnFinsh.setBackgroundResource(R.drawable.normalbuttondisable);

                btnGetLoc.setEnabled(true);
                btnGetLoc.setBackgroundResource(R.drawable.normalbutton);
            }
            cursor_get_draft.close();
        }


        cursor_load.close();
        sqLiteDatabase_dataLoad.close();


                /*
        LocationManager myManager;
        MyLocListener loc;
        loc = new MyLocListener();
        myManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loc);
        */

        //==== Send the SqlDate To Server Database
        BtnFinsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                boolean mChk = checkDataConnectionStatus.IsConnected();
                if (mChk == true)
                {
                    androidx.appcompat.app.AlertDialog.Builder builderdelete = new androidx.appcompat.app.AlertDialog.Builder(House_Visit_update.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you want finsh the to Record ?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SendServer();

                        }
                    });
                    builderdelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked
                            Toast.makeText(getApplicationContext(),
                                    "No Button Clicked",Toast.LENGTH_SHORT).show();
                        }
                    });
                    androidx.appcompat.app.AlertDialog dialog = builderdelete.create();
                    dialog.show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(House_Visit_update.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Data Connection Not available. Please Turn on Connection.");
                    builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        });


        //  = Save Locastion
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (mLitude==null && mLongTude==null)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(House_Visit_update.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Please get the Correct locastion.");
                    builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                }
                else
                {
                    androidx.appcompat.app.AlertDialog.Builder builderdelete = new androidx.appcompat.app.AlertDialog.Builder(House_Visit_update.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you save to Record ?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SaveSqlLite();

                            mSave.setEnabled(false);
                            mSave.setBackgroundResource(R.drawable.normalbuttondisable);

                            BtnFinsh.setEnabled(true);
                            BtnFinsh.setBackgroundResource(R.drawable.normalbutton);

                            btnGetLoc.setEnabled(false);
                            btnGetLoc.setBackgroundResource(R.drawable.normalbuttondisable);

                        }
                    });
                    builderdelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked
                            Toast.makeText(getApplicationContext(),
                                    "No Button Clicked",Toast.LENGTH_SHORT).show();
                        }
                    });
                    androidx.appcompat.app.AlertDialog dialog = builderdelete.create();
                    dialog.show();
                }
            }
        });


        //====== Get Locastion ===================================
        btnGetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*=== Get Geo Locastion ===*/
                GetLo();

                mTxtSaveData.setText("**  LATITUDE = " + current_lattitude  + "\n" + "**  LONGITUDE = " + current_longitude) ;
                mLitude     =   Double.toString(current_lattitude) ;
                mLongTude   =   Double.toString(current_longitude);
            }
        });
        //========================================================
    }

    private void startCounting() {
        handler.post(run);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run()
        {

            boolean CheckConnection = checkDataConnectionStatus.IsConnected();

            if (CheckConnection ==true)
            {
                ConnectionSts.setText("ONLINE");
                ConnectionSts.setTextColor(Color. parseColor("#2cb72c"));
            }
            else
            {
                ConnectionSts.setText("OFFLINE");
                ConnectionSts.setTextColor(Color. parseColor("#ffffff"));
            }
            handler.postDelayed(this, 2000);
        }
    };

    public void GetLo()
    {
        GPSTracker gps = new GPSTracker(this);
        int status = 0;
        if(gps.canGetLocation())

        {
            status = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(getApplicationContext());

            if (status == ConnectionResult.SUCCESS) {
                current_lattitude = gps.getLatitude();
                current_longitude = gps.getLongitude();
                Log.d("dashlatlongon", "" + current_lattitude + "-"
                        + current_longitude);

                if (current_lattitude == 0.0 && current_longitude == 0.0) {
                    current_lattitude = 22.22;
                    current_longitude = 22.22;

                }

            } else {
                current_lattitude = 22.22;
                current_longitude = 22.22;
            }

        }
        else
        {
            gps.showSettingsAlert();
        }
    }



    public void SendServer()
    {
        SQLiteDatabase sqLiteDatabase_geo = sqlliteCreateLeasing_house_visit.getReadableDatabase();
        Cursor cursor_data = sqLiteDatabase_geo.rawQuery("SELECT * FROM LE_GEO_TAG_UPDATE WHERE APP_REF_NO = '" + mTxtAppnp.getText() + "'" , null);
        if (cursor_data.getCount() != 0)
        {
            cursor_data.moveToFirst();
            JSONObject jsonObject_data = new JSONObject();

            try {
                jsonObject_data.put("APP_REF_NO" , cursor_data.getString(0));
                jsonObject_data.put("CL_ADDERS" , cursor_data.getString(1));
                jsonObject_data.put("MK_CODE" , cursor_data.getString(2));
                jsonObject_data.put("GEO_LATITUDE" , cursor_data.getString(3));
                jsonObject_data.put("GEO_LONGITUDE" , cursor_data.getString(4));
                jsonObject_data.put("GEO_UPDATE_DATE" , cursor_data.getString(5));
                jsonObject_data.put("GEO_UPDATE_TIME" , cursor_data.getString(6));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String Url = PHP_URL_SQL + "MOBILE-COMPLETE-GEO-TAG.php";
            progressDialog.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url, jsonObject_data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String Resp = "";
                            try {
                                Resp = response.getString("RESULT");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (Resp.equals("DONE")) {

                                //=== Update send server sts
                                SQLiteDatabase sqLiteDatabase_updatserver = sqlliteCreateLeasing_house_visit.getWritableDatabase();
                                ContentValues contentValues_sendsrver = new ContentValues();
                                contentValues_sendsrver.put("SERVER_SEND","Y");
                                sqLiteDatabase_updatserver.update("LE_GEO_TAG_UPDATE" , contentValues_sendsrver , "APP_REF_NO = ?", new String[]{String.valueOf(mTxtAppnp.getText())});
                                //=======================


                                progressDialog.dismiss();
                                androidx.appcompat.app.AlertDialog.Builder abmyAlert = new androidx.appcompat.app.AlertDialog.Builder(House_Visit_update.this);
                                abmyAlert.setMessage("File Complete Successfully.").setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                })
                                        .setTitle("AFSL Mobile Leasing.")
                                        .create();
                                abmyAlert.show();

                            } else {
                                androidx.appcompat.app.AlertDialog.Builder abmyAlert = new androidx.appcompat.app.AlertDialog.Builder(House_Visit_update.this);
                                abmyAlert.setMessage("Please try again..").setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                })
                                        .setTitle("AFSL Mobile Leasing.")
                                        .create();
                                abmyAlert.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    androidx.appcompat.app.AlertDialog.Builder bmyAlert = new androidx.appcompat.app.AlertDialog.Builder(House_Visit_update.this);
                    bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                            .setTitle("AFSL Mobile Leasing.")
                            .create();
                    bmyAlert.show();
                }
            });
            mQueue.add(jsonObjectRequest);
        }
    }


    public void SaveSqlLite()
    {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String Adders= mTxtAdders.getText() + "," + mTxtAdders2.getText();
        sqlliteCreateLeasing_house_visit.GeoTagSave(mTxtAppnp.getText().toString() , Adders , LoginUser ,
                mLitude , mLongTude , LoginDate , currentTime );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        btnGetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //noinspection MissingPermission
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 0, locationListener);
            }
        });
    }


    @Override
    protected void onDestroy() {
        sqlliteCreateLeasing_house_visit.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();


        super.onDestroy();
    }
}
