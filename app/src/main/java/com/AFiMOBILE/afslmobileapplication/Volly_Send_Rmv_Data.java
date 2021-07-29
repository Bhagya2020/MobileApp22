package com.AFiMOBILE.afslmobileapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

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
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;

import dmax.dialog.SpotsDialog;

public class Volly_Send_Rmv_Data {

    public Context mContex;
    private SqlliteCreateLeasing sqlliteCreateLeasing_Recovery_data;
    private android.app.AlertDialog progressDialog;
    private String msubmit_Regno ,   FileUploadSts , ApplicationJsonResponce , mInpAppno , PHP_URL_SQL , LoginUser="" , LoginDate = "" , LoginBranch= "";
    private RequestQueue requestQueue_FileComplete;
    private Boolean CheckResponce;



    public Volly_Send_Rmv_Data (Context context)
    {
        mContex =  context;
        sqlliteCreateLeasing_Recovery_data = new SqlliteCreateLeasing(mContex);
        progressDialog = new SpotsDialog(mContex, R.style.Custom);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
    }

    public void SendRmvData(JSONObject jsonObject_inpdata , String Inp_regno)
    {
        msubmit_Regno = Inp_regno;
        requestQueue_FileComplete   = VollySingleton.getInstance(mContex).getRequestQueue();

        //==== Create Json Request To Send Live Database...
        String Url = "http://afimobile.abansfinance.lk/mobilephp/RMV_CONFORM_UPDATE_DATA.php";
        JsonObjectRequest jsonObjectRequest_CompleteData = new JsonObjectRequest(Request.Method.POST, Url, jsonObject_inpdata,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            ApplicationJsonResponce = response.getString("RESULT-RMV");
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

                progressDialog.dismiss();
                String ErrorDescription="Responses Failure.(" + ErrorCode.toString() + ")";
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex );
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
        jsonObjectRequest_CompleteData.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog.show();
        requestQueue_FileComplete.getCache().clear();
        requestQueue_FileComplete.start();
        requestQueue_FileComplete.add(jsonObjectRequest_CompleteData);  //=== Appliocation request

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
                    //==== To Send the upload Image file to server
                    //==== Get Application Image folder path
                    String mFileSavePath=""; Boolean CheckFileCommplete=false;
                    SQLiteDatabase sqLiteDatabase_file = sqlliteCreateLeasing_Recovery_data.getReadableDatabase();
                    Cursor cursor_file_path = sqLiteDatabase_file.rawQuery("SELECT FOLDER_PATH FROM AP_IMAGE_FOLDER_DETAILS WHERE APP_REFNO = '" + msubmit_Regno + "'" , null);
                    if (cursor_file_path.getCount() != 0)
                    {
                        cursor_file_path.moveToFirst();
                        mFileSavePath = cursor_file_path.getString(0);
                    }
                    cursor_file_path.close();

                    //==== Send the Image Folder to Live server
                    FileUploadServer fileUploadServer = new FileUploadServer(mContex);
                    FileUploadSts = fileUploadServer.Upload_doc(mInpAppno , mFileSavePath );

                    if (FileUploadSts != null)
                    {
                        CheckFileCommplete = FileUploadSts.equals("DONE") ;
                    }
                    //==================================================

                    if (CheckFileCommplete)
                    {
                        progressDialog.dismiss();
                        requestQueue_FileComplete.getCache().clear();

                        //===== Update table Record
                        SQLiteDatabase sqLiteDatabase_update_rmv = sqlliteCreateLeasing_Recovery_data.getWritableDatabase();
                        ContentValues contentValues_update_table = new ContentValues();
                        contentValues_update_table.put("CO_SYS_READ" , "");
                        sqLiteDatabase_update_rmv.update("RMV_CONFORM_DATA" , contentValues_update_table , "REG_NO = ?" , new String[]{String.valueOf(msubmit_Regno)});

                        if(!((Activity) mContex).isFinishing())
                        {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContex);
                            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                            builder.setMessage("File Complete Successfully.");
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
            }
        });


    }

}
