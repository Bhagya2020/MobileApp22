package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class SubmitApplication extends AppCompatActivity {

    private RecyclerView mPendingSubmit;
    public static Button mComplete , mCanApplication , mBtnrefData;
    public String LoginUser="" , LoginDate="" , LoginBranch="" , PHP_URL_SQL="" , ApplicationJsonResponce="";
    public SqlliteCreateLeasing sqlliteCreateLeasing_AppSubmit;
    public SQLiteDatabase sqLiteDatabase_AppSubmit;
    public static Adapter_SubmitApplication mAdapter;
    public static TextView mApp;
    public RequestQueue mQueue;
    public static Toolbar toolbar;
    public Handler handler;
    public TextView ConnectionSts;
    public RequestQueue requestQueue_FileComplete;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    private android.app.AlertDialog progressDialog;
    public Boolean CheckResponce;
    public GetCompleteApplication getCompleteApplication_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_application);

        //=== Database Connection
        sqlliteCreateLeasing_AppSubmit = new SqlliteCreateLeasing(this);
        sqLiteDatabase_AppSubmit = sqlliteCreateLeasing_AppSubmit.getReadableDatabase();
        getCompleteApplication_new = new GetCompleteApplication(SubmitApplication.this);



        mPendingSubmit              =   (RecyclerView)findViewById(R.id.rcppendingsubmit);
        mComplete                   =   (Button)findViewById(R.id.btncomplete) ;
        mApp                        =   findViewById(R.id.txtSelectAppnpo2);
        mComplete                   =   (Button)findViewById(R.id.btncomplete) ;
        mCanApplication             =   (Button)findViewById(R.id.btncanpp);
        checkDataConnectionStatus   =   new CheckDataConnectionStatus(this);
        progressDialog = new SpotsDialog(SubmitApplication.this, R.style.Custom);


        //=== Globle Varible Details..
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        PHP_URL_SQL =   globleClassDetails.getPHP_Path();

        //===== Create Volly Request Ques. =======
        mQueue   = VollySingleton.getInstance(this).getRequestQueue();

        mPendingSubmit.setHasFixedSize(true);
        mPendingSubmit.setLayoutManager(new LinearLayoutManager(SubmitApplication.this));
        mAdapter = new Adapter_SubmitApplication(SubmitApplication.this , getCompleteApplication_new.GetCompleteAppliation(LoginUser));
        mPendingSubmit.setAdapter(mAdapter);

        //=== Check Data Connection status Toolbra every 5 second.
        ConnectionSts = (TextView) findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();



        // Refresh Data Button
        /*
        mBtnrefData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDataConnectionStatus.IsConnected())
                {
                    Complete_DataSync complete_dataSync = new Complete_DataSync(SubmitApplication.this);
                    complete_dataSync.GetCompleteFile(LoginUser);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SubmitApplication.this);
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
        });

         */

        //==== Cancel Application
        mCanApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean mCheckSts = checkDataConnectionStatus.IsConnected();
                if (mCheckSts)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(SubmitApplication.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to cancel this application?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ApplicationCancel();
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
                    AlertDialog dialog = builderdelete.create();
                    dialog.show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SubmitApplication.this);
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
        });

        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mApp.getText() != null)
                {
                    Intent intentsubmit = new Intent("android.intent.action.ApplcationSubmitMenu");
                    intentsubmit.putExtra("ApplicationNo" , mApp.getText());
                    startActivity(intentsubmit);
                }
                else
                {
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(SubmitApplication.this );
                    myAlert.setMessage("No Selected Application.").setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                            .setTitle("AFSL Mobile Leasing.")
                            .create();
                    myAlert.show();
                }
            }
        });
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

    protected void onStart()
    {
        super.onStart();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        mAdapter = new Adapter_SubmitApplication(SubmitApplication.this , getCompleteApplication_new.GetCompleteAppliation(LoginUser));
        mPendingSubmit.setAdapter(mAdapter);
        mComplete.setEnabled(true);
        mComplete.setBackgroundResource(R.drawable.normalbutton);
    }

    protected void onDestroy(){
        super.onDestroy();
        sqLiteDatabase_AppSubmit.close();
        sqlliteCreateLeasing_AppSubmit.close();
        Log.d("Log", "onDestroy-AppSubmit");
    }

    public void ApplicationCancel()
    {
        JSONObject jsonObject_CanRequest = new JSONObject();
        try {
            jsonObject_CanRequest.put("APPLICATION_REF_NO" , mApp.getText().toString());
            jsonObject_CanRequest.put("REQ_USER" , LoginUser.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //==== Create Json Request To Send Live Database...
        String Urlapplication = "https://afimobile.abansfinance.lk/mobilephp/MOBILE-PO-CANCEL.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urlapplication, jsonObject_CanRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            ApplicationJsonResponce = response.getString("RESULT-EMAIL");
                            Log.d("Respoces" , ApplicationJsonResponce);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

                String ErrorDescription="Responses Failure.(" + ErrorCode.toString() + ")";
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(SubmitApplication.this);
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        progressDialog.show();
        requestQueue_FileComplete   = VollySingleton.getInstance(SubmitApplication.this).getRequestQueue();
        requestQueue_FileComplete.start();
        requestQueue_FileComplete.add(jsonObjectRequest);  //=== Appliocation request

        //==== Fineash Request
        requestQueue_FileComplete.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {

                if (ApplicationJsonResponce != null)
                {
                    CheckResponce = ApplicationJsonResponce.equals("DONE");
                }

                if (CheckResponce == true)
                {

                    progressDialog.dismiss();
                    sqlliteCreateLeasing_AppSubmit.UpdateAppSts(mApp.getText().toString() , "100" , "D");
                    requestQueue_FileComplete.getCache().clear();

                    mAdapter.swapCursor(getCompleteApplication_new.GetCompleteAppliation(LoginUser));

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SubmitApplication.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("File Cancel Successfully.");
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
    }
}
