package com.AFiMOBILE.afslmobileapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class Rmv_Home extends AppCompatActivity {

    private String PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , LoginUserName ,Responces_Rmv_data;
    private Button btn_RMV_UPDATE , btn_RMV_CHECK ;
    private ImageButton btn_RMV_SYNC;
    private JsonObjectRequest jsonObjectRequest_RMV_data;
    private SqlliteCreateLeasing sqlliteCreateLeasing_rmv;
    private SQLiteDatabase sqLiteDatabase_rmv_doc;
    private android.app.AlertDialog progressDialog;
    private RequestQueue mQueue;
    private CheckDataConnectionStatus checkDataConnectionStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rmv_home);

        //=== Create Varible
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();


        btn_RMV_UPDATE  =   (Button)findViewById(R.id.btnupdateverify);
        btn_RMV_CHECK   =   (Button)findViewById(R.id.btncheck);
        btn_RMV_SYNC    =   (ImageButton) findViewById(R.id.imgrmvupdate);
        Responces_Rmv_data="";

        sqlliteCreateLeasing_rmv = new SqlliteCreateLeasing(Rmv_Home.this);
        checkDataConnectionStatus = new CheckDataConnectionStatus(Rmv_Home.this);

        final Cache cache = new DiskBasedCache(Rmv_Home.this.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        mQueue = new RequestQueue(cache, network);

        progressDialog = new SpotsDialog(Rmv_Home.this, R.style.Custom);
        progressDialog.setTitle("AFSL Processing Data");

        btn_RMV_UPDATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_update = new Intent("android.intent.action.Rmv_Verification_Update");
                startActivity(intent_update);
            }
        });


        btn_RMV_SYNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean mCheckSts = checkDataConnectionStatus.IsConnected();
                if (mCheckSts)
                {
                    GetRMVData();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Rmv_Home.this);
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


    }

    public void GetRMVData()
    {
        sqLiteDatabase_rmv_doc = sqlliteCreateLeasing_rmv.getWritableDatabase();

        String url = "http://afimobile.abansfinance.lk/mobilephp/GET_RMV_DATA.php";
        jsonObjectRequest_RMV_data = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            JSONArray jsonArray_RMV_DATA= response.getJSONArray("TT-RMV_DATA");
                            for (int i=0 ; i< jsonArray_RMV_DATA.length();i++)
                            {
                                JSONObject mydataJson = jsonArray_RMV_DATA.getJSONObject(i);

                                ContentValues contentValues_data = new ContentValues();
                                contentValues_data.put("BRANCH_CODE" , mydataJson.getString("BRANCH_CODE"));
                                contentValues_data.put("MK_CODE" , mydataJson.getString("MK_CODE"));
                                contentValues_data.put("MK_NAME" , mydataJson.getString("MK_NAME"));
                                contentValues_data.put("CHASSI_NO" , mydataJson.getString("CHASSI_NO"));
                                contentValues_data.put("ENGINE_NO" , mydataJson.getString("ENGINE_NO"));
                                contentValues_data.put("REG_NO" , mydataJson.getString("REG_NO"));
                                contentValues_data.put("PO_DATE" , mydataJson.getString("PO_DATE"));
                                contentValues_data.put("PO_TIME" , mydataJson.getString("PO_TIME"));
                                contentValues_data.put("REQ_DATE" , mydataJson.getString("REQ_DATE"));
                                contentValues_data.put("RMV_CONFORM_STS" , "");
                                contentValues_data.put("RMV_CONFORM_DATE" , "");
                                contentValues_data.put("RMV_CONFORM_USER" , "");
                                contentValues_data.put("RMV_REMARKS" , "");
                                contentValues_data.put("CO_SYS_READ" , "");

                                //=== Update sqllite data ===
                                sqLiteDatabase_rmv_doc.insert("RMV_CONFORM_DATA" , null , contentValues_data);
                            }
                            Responces_Rmv_data="DONE";
                            Log.d("GetRequestRmvData", Responces_Rmv_data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String ErrorCode="";
                progressDialog.dismiss();
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
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Rmv_Home.this );
                bmyAlert.setMessage(ErrorDescription).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("Error... - 5")
                        .create();
                bmyAlert.show();
            }
        });
        jsonObjectRequest_RMV_data.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //==== Add Request ========
        progressDialog.show();
        mQueue.start();
        mQueue.add(jsonObjectRequest_RMV_data);

        //=== Check request done
        //==== Check Finsh Responces
        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce = false;
                if (Responces_Rmv_data != null)
                {
                    if (Responces_Rmv_data.equals("DONE"))
                    {
                        CheckResponce = Responces_Rmv_data.equals("DONE");
                    }
                }

                if (CheckResponce == true)
                {
                    Log.d("GetRMVData", "COMPLETE");

                    mQueue.stop();
                    mQueue.getCache().clear();

                    if(!((Activity) Rmv_Home.this).isFinishing())
                    {
                        //show dialog
                        progressDialog.dismiss();
                        android.app.AlertDialog.Builder builder_SUCESS = new android.app.AlertDialog.Builder(Rmv_Home.this);
                        builder_SUCESS.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                        builder_SUCESS.setMessage("Data Process Successfully.");
                        builder_SUCESS.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });
                        builder_SUCESS.create();
                        builder_SUCESS.show();
                    }


                }
            }
        });


    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
