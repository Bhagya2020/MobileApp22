package com.AFiMOBILE.afslmobileapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
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

import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class Complete_DataSync
{
    private Context mContex;
    private SqlliteCreateLeasing sqlliteCreateLeasing_com_file;
    private JSONObject jsonObject_Request_complete;
    private android.app.AlertDialog progressDialog;
    private RequestQueue requestQueue_FilecomSts;
    private String ApplicationJsonResponce , LoginUser , LoginDate , LoginBranch;
    private GetCompleteApplication getCompleteApplication;

    public Complete_DataSync(Context context)
    {
        mContex = context;
        sqlliteCreateLeasing_com_file = new SqlliteCreateLeasing(mContex);
        getCompleteApplication = new GetCompleteApplication(mContex);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();


        progressDialog = new SpotsDialog(mContex, R.style.Custom);
        progressDialog.setTitle("Sending ...");
        progressDialog.setMessage("Please wait...");
    }


    public void GetCompleteFile(String Loginname)
    {
        //=== Create Volly Request
        //requestQueue_FilecomSts   = VollySingleton.getInstance(mContex).getRequestQueue();
        //requestQueue_FilecomSts.getCache().clear();

        final Cache cache = new DiskBasedCache(mContex.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue_FilecomSts = new RequestQueue(cache, network);


        JSONArray jsonArray_complete_file         = new JSONArray();
        JSONObject jsonObjectfile               = new JSONObject();

        SQLiteDatabase sqLiteDatabase_comfile = sqlliteCreateLeasing_com_file.getReadableDatabase();
        Cursor cursor_com = sqLiteDatabase_comfile.rawQuery("SELECT APPLICATION_REF_NO FROM LE_APPLICATION WHERE AP_MK_OFFICER = '" + Loginname + "' AND " +
                "AP_STAGE = '001'" , null);
        if (cursor_com.getCount() != 0)
        {
            cursor_com.moveToFirst();
            do{
                jsonObjectfile = new JSONObject();

                try {
                    jsonObjectfile.put("APPLICATION_REF_NO" , cursor_com.getString(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray_complete_file.put(jsonObjectfile);
            }while (cursor_com.moveToNext());

            //=== Create Final Json Arrays
            jsonObject_Request_complete  = new JSONObject();
            try {
                jsonObject_Request_complete.put("CHECK_APPROVE" , jsonArray_complete_file);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor_com.close();
        sqLiteDatabase_comfile.close();

        //=== Create Volley Request
        String Urlapplication = "http://afimobile.abansfinance.lk/mobilephp/MOBILE-MGR-APPROVE-DATA-SYNC.php";
        JsonObjectRequest jsonObjectRequestManagerApproveApp = new JsonObjectRequest(Request.Method.POST, Urlapplication, jsonObject_Request_complete,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Complete-res" , "Done");
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

                            Log.d("App_no" , mGetAppno);
                            sqlliteCreateLeasing_com_file.AppDataSync(mGetAppno,mGetPOSts,mGetPODateTime,mGetMgrAppCode,mGetAppDate,mGetAppStage , LoginUser);
                        }
                        ApplicationJsonResponce="DONE";

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
        jsonObjectRequestManagerApproveApp.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        //=== Add Volly Request
        progressDialog.show();
        requestQueue_FilecomSts.start();
        requestQueue_FilecomSts.add(jsonObjectRequestManagerApproveApp);

        //=== Check Volly Request finish
        requestQueue_FilecomSts.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {

                Boolean CheckApplication=false;

                if (ApplicationJsonResponce != null)
                {
                    CheckApplication    = ApplicationJsonResponce.equals("DONE");
                }

                if (CheckApplication==true)
                {
                    progressDialog.dismiss();
                    if(!((Activity) mContex).isFinishing())
                    {
                        SubmitApplication submitApplication = new SubmitApplication();
                        submitApplication.mAdapter.swapCursor(getCompleteApplication.GetCompleteAppliation(LoginUser));

                        //show dialog
                        android.app.AlertDialog.Builder builder_SUCESS = new android.app.AlertDialog.Builder(mContex);
                        builder_SUCESS.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                        builder_SUCESS.setMessage("Data Request Successfully.");
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


}
