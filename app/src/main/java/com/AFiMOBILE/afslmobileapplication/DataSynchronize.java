package com.AFiMOBILE.afslmobileapplication;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;

import androidx.appcompat.app.AlertDialog;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Currency;

import dmax.dialog.SpotsDialog;


public class DataSynchronize
{
    private Context mContex;
    public String PHP_URL_SQL , LoginUser , LoginDate;

    private RequestQueue mQueue;
    private android.app.AlertDialog progressDialog;
    private SqlliteCreateLeasing sqlliteCreateLeasing_data_sync;
    private SQLiteDatabase sqLiteDatabase_write_user;


    public DataSynchronize(Context context )
    {
        mContex = context;
        mQueue   = VollySingleton.getInstance(mContex).getRequestQueue();
        progressDialog = new SpotsDialog(mContex, R.style.Custom);
        progressDialog.setTitle("Daily Data Synchronization..");
        sqlliteCreateLeasing_data_sync = new SqlliteCreateLeasing(mContex);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginDate   =   globleClassDetails.getLoginDate();
    }

    public void  AppDataSync(final String LoginUser)
    {
        //===== Create Volly Request Ques. =======

        //==== Get User Login Detals
        JSONObject object = new JSONObject();
        //== Create Json File To Send User ID
        try {
            //input your API parameters
            object.put("NEW_USER_ID",LoginUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sqLiteDatabase_write_user = sqlliteCreateLeasing_data_sync.getWritableDatabase();
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


                                //==== Update table  recoed.
                                ContentValues contentValues_user = new ContentValues();
                                contentValues_user.put("OFFIER_ID" , OFFIER_ID);
                                contentValues_user.put("PWD" , PWD);
                                contentValues_user.put("OFFICER_NAME" , OFFICER_NAME);
                                contentValues_user.put("DEPARTMENT" , DEPARTMENT);
                                contentValues_user.put("INACTIVE" , INACTIVE);
                                contentValues_user.put("OFFICER_EPF" , OFFICER_EPF);
                                contentValues_user.put("OFFICER_EMAIL" , OFFICER_EMAIL);
                                contentValues_user.put("OFFIER_TYPE" , OFF_TYPE);
                                contentValues_user.put("USER_ROLL" , MUSER_ROLL);
                                contentValues_user.put("BRANCH_CODE" , MBRCODE);
                                contentValues_user.put("PHONE_NO" , MPHONENO);
                                sqLiteDatabase_write_user.update("USER_MANAGEMENT" , contentValues_user , "OFFIER_ID = ?", new String[]{String.valueOf(LoginUser)});
                                progressDialog.dismiss();

                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

                String ErrorDescription="Responses Failure.(" + ErrorCode + ")";
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

        progressDialog.show();
        mQueue.getCache().clear();
        mQueue.add(jsonObjectRequest);


        //===== Create Data sync table
        ContentValues contentValues_updte_sync = new ContentValues();
        contentValues_updte_sync.put("SYNC_DATE" , LoginDate);
        contentValues_updte_sync.put("SYNC_STS" , "Y");
        contentValues_updte_sync.put("SYNC_USER" , LoginUser);
        sqLiteDatabase_write_user.insert("HOME_DATA_SYNC" , null ,contentValues_updte_sync);
        //=============================




        //=========================================================

        /*
        //===== Get complete Appliction Data ======================
        //=========================================================

        SQLiteDatabase sqLiteDatabase_data_sync = sqlliteCreateLeasing_data_sync.getReadableDatabase();
        Cursor cursor_data = sqLiteDatabase_data_sync.rawQuery("SELECT APPLICATION_REF_NO FROM LE_APPLICATION WHERE AP_MK_OFFICER = '" + LoginUser +
                "' AND AP_STAGE = '001'" , null);

        if (cursor_data.getCount()!= 0)
        {
            cursor_data.moveToFirst();
            do
            {
                JSONObject jsonObject_data = new JSONObject();
                try {
                    jsonObject_data.put("APPLICATION_REF_NO" , cursor_data.getString(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //==== Get Server Data =============
                String url = "http://afimobile.abansfinance.lk/mobilephp/LiveDataSync.php";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject_data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                JSONArray myjson = new JSONArray();
                                String mGetAppno="" ,  mGetPOSts="" , mGetPODateTime="" , mGetMgrAppCode="" , mGetAppDate="" , mGetAppStage="";
                                try {
                                    myjson = response.getJSONArray("TT-APP-SYNC");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                for (int i = 0; i < myjson.length(); i++)
                                {

                                    try {
                                        JSONObject JOPRCODE = myjson.getJSONObject(i);
                                        mGetAppno = JOPRCODE.getString("APPLICATION_REF_NO");
                                        mGetPOSts = JOPRCODE.getString("AP_PO_STS");
                                        mGetPODateTime = JOPRCODE.getString("AP_PO_SEND_DATE_TIME");
                                        mGetMgrAppCode = JOPRCODE.getString("AP_APPROVE_OFFICER");
                                        mGetAppDate = JOPRCODE.getString("AP_APPROVE_DATE");
                                        mGetAppStage = JOPRCODE.getString("AP_STAGE");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    sqlliteCreateLeasing_data_sync.AppDataSync(mGetAppno,mGetPOSts,mGetPODateTime,mGetMgrAppCode,mGetAppDate,mGetAppStage , LoginUser);
                                }
                                progressDialog.dismiss();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex);
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


            }while (cursor_data.moveToNext());



        }
        else
        {
            progressDialog.dismiss();
        }
        */

    }
}
