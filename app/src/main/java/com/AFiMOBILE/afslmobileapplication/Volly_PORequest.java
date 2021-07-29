package com.AFiMOBILE.afslmobileapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;


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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import dmax.dialog.SpotsDialog;

public class Volly_PORequest
{
    public String mClNic , mBranch , mMkOfficer , mApplication , PHP_URL_SQL , ChargesJsonREsponce , ApplicationJsonResponce , LohinOfficerName,
            ImageResponces;
    public Context mContex;
    private AlertDialog progressDialog;
    public SqlliteCreateLeasing sqlliteCreateLeasing_porequest;
    public SQLiteDatabase sqLiteDatabase_poRequestApplication;
    public int RespocesCount=0;


    public Volly_PORequest(Context context)
    {
        mContex = context;
        progressDialog = new SpotsDialog(mContex, R.style.Custom);
        sqlliteCreateLeasing_porequest = new SqlliteCreateLeasing(mContex);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        PHP_URL_SQL = "http://afimobile.abansfinance.lk/mobilephp/";
        LohinOfficerName = globleClassDetails.getOfficerName();
    }

    public void SendDataPORequest( final String mInpAppno)
    {
        //=======================================================
        //        Create Volly Request
        //=======================================================
        //final RequestQueue requestQueue=null;
        final Cache cache = new DiskBasedCache(mContex.getCacheDir(), 1024 * 1024);

        Network network = new BasicNetwork(new HurlStack());
        final RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.getCache().clear();
        RespocesCount=0;
        mClNic = "";  mBranch = "";  mMkOfficer = ""; mApplication="";
        sqLiteDatabase_poRequestApplication = sqlliteCreateLeasing_porequest.getReadableDatabase();
        JSONObject ApplicationDataJson = new JSONObject();
        Log.d("Respoces" , "SendDataPORequest");

        //===============================================================================================================================
        //=== Applicaton Data
        Cursor mApplicationCurser = sqLiteDatabase_poRequestApplication.rawQuery("SELECT APPLICATION_REF_NO,AP_PRODUCT,AP_INVOICE_AMT,AP_DOWN_PAY,AP_FACILITY_AMT,AP_ETV,AP_RATE," +
                "AP_PERIOD,AP_RENTAL_AMT,AP_MK_OFFICER,AP_BRANCH,AP_PO_STS,AP_PO_SEND_DELEAR,AP_PO_DELEAR_EMAIL,AP_ENTDATE,AP_STAGE,AP_MAIN_SUPPLIR,AP_MAIN_SUPPLIR_EMAIL,AP_PO_RQ_USER FROM LE_APPLICATION WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);

        //=== Send the Application Data To Live database.  =====================================================================
        if (mApplicationCurser.getCount() != 0)
        {
            mApplicationCurser.moveToFirst();
            try {
                ApplicationDataJson.put("APPLICATION_REF_NO", mApplicationCurser.getString(0));
                ApplicationDataJson.put("AP_PRODUCT", mApplicationCurser.getString(1));
                ApplicationDataJson.put("AP_INVOICE_AMT", mApplicationCurser.getString(2));
                ApplicationDataJson.put("AP_DOWN_PAY", mApplicationCurser.getString(3));
                ApplicationDataJson.put("AP_FACILITY_AMT", mApplicationCurser.getString(4));
                ApplicationDataJson.put("AP_ETV", mApplicationCurser.getString(5));
                ApplicationDataJson.put("AP_RATE", mApplicationCurser.getString(6));
                ApplicationDataJson.put("AP_PERIOD", mApplicationCurser.getString(7));
                ApplicationDataJson.put("AP_RENTAL_AMT", mApplicationCurser.getString(8));
                ApplicationDataJson.put("AP_MK_OFFICER", mApplicationCurser.getString(9));
                ApplicationDataJson.put("AP_BRANCH", mApplicationCurser.getString(10));
                ApplicationDataJson.put("AP_PO_STS", mApplicationCurser.getString(11));
                ApplicationDataJson.put("AP_PO_SEND_DELEAR", mApplicationCurser.getString(12));
                ApplicationDataJson.put("AP_PO_DELEAR_EMAIL", mApplicationCurser.getString(13));
                ApplicationDataJson.put("AP_ENTDATE", mApplicationCurser.getString(14));
                ApplicationDataJson.put("AP_STAGE", mApplicationCurser.getString(15));
                ApplicationDataJson.put("AP_MAIN_SUPPLIR", mApplicationCurser.getString(16));
                ApplicationDataJson.put("AP_MAIN_SUPPLIR_EMAIL", mApplicationCurser.getString(17));
                ApplicationDataJson.put("AP_PO_RQ_USER", mApplicationCurser.getString(18));

                mBranch = mApplicationCurser.getString(10);
                mMkOfficer = mApplicationCurser.getString(9);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mApplicationCurser.close();


        //=== Client Data
        Cursor mClientCurser = sqLiteDatabase_poRequestApplication.rawQuery("SELECT APPLICATION_REF_NO,CL_NIC,CL_TITLE,CL_FULLY_NAME,CL_ADDERS_1" +
                ",CL_ADDERS_2,CL_ADDERS_3,CL_ADDERS_4,CL_OCCUPATION,CL_MOBILE_NO,CL_CREATE_DATE,CL_CREATE_USER FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);
        mClientCurser.moveToFirst();
        if (mClientCurser.getCount() != 0) {
            try {
                ApplicationDataJson.put("CL_APPLICATION_REF_NO", mClientCurser.getString(0));
                ApplicationDataJson.put("CL_NIC", mClientCurser.getString(1));
                ApplicationDataJson.put("CL_TITLE", mClientCurser.getString(2));
                ApplicationDataJson.put("CL_FULLY_NAME", mClientCurser.getString(3));
                ApplicationDataJson.put("CL_ADDERS_1", mClientCurser.getString(4));
                ApplicationDataJson.put("CL_ADDERS_2", mClientCurser.getString(5));
                ApplicationDataJson.put("CL_ADDERS_3", mClientCurser.getString(6));
                ApplicationDataJson.put("CL_ADDERS_4", mClientCurser.getString(7));
                ApplicationDataJson.put("CL_OCCUPATION", mClientCurser.getString(8));
                ApplicationDataJson.put("CL_MOBILE_NO", mClientCurser.getString(9));
                ApplicationDataJson.put("CL_CREATE_DATE", mClientCurser.getString(10));
                ApplicationDataJson.put("CL_CREATE_USER", mClientCurser.getString(11));

                mClNic = mClientCurser.getString(1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mClientCurser.close();

        //=== Asset Data
        Cursor cursorAsset = sqLiteDatabase_poRequestApplication.rawQuery("SELECT APPLICATION_REF_NO,AS_EQ_TYPE,AS_EQ_CATAGE,AS_EQ_MAKE,AS_EQ_MODEL,AS_EQ_REGNO,AS_EQ_ENG_NO,AS_EQ_CHAS_NO,AS_EQ_YEAR," +
                "AS_INV_DELER,AS_INV_SUPPLIER,AP_INV_DATE,AP_INSURANCE_COMPANY,AP_ENT_DATE,AP_ENT_USER,MK_VAL ,AP_INTDU FROM LE_ASSET_DETAILS WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);
        cursorAsset.moveToFirst();
        if (cursorAsset.getCount() != 0)
        {

            try {
                ApplicationDataJson.put("AS_APPLICATION_REF_NO" , cursorAsset.getString(0));
                ApplicationDataJson.put("AS_EQ_TYPE" , cursorAsset.getString(1));
                ApplicationDataJson.put("AS_EQ_CATAGE" , cursorAsset.getString(2));
                ApplicationDataJson.put("AS_EQ_MAKE" , cursorAsset.getString(3));
                ApplicationDataJson.put("AS_EQ_MODEL" , cursorAsset.getString(4));
                ApplicationDataJson.put("AS_EQ_REGNO" , cursorAsset.getString(5));
                ApplicationDataJson.put("AS_EQ_ENG_NO" , cursorAsset.getString(6));
                ApplicationDataJson.put("AS_EQ_CHAS_NO" , cursorAsset.getString(7));
                ApplicationDataJson.put("AS_EQ_YEAR" , cursorAsset.getString(8));
                ApplicationDataJson.put("AS_INV_DELER" , cursorAsset.getString(9));
                ApplicationDataJson.put("AS_INV_SUPPLIER" , cursorAsset.getString(10));
                //JsonAsset.put("AP_INV_DATE" , cursorAsset.getString(11));
                ApplicationDataJson.put("AP_INV_DATE" , "2019-01-01");
                ApplicationDataJson.put("AP_INSURANCE_COMPANY" , cursorAsset.getString(12));
                ApplicationDataJson.put("AP_ENT_DATE" , cursorAsset.getString(13));
                ApplicationDataJson.put("AP_ENT_USER" , cursorAsset.getString(14));
                ApplicationDataJson.put("AP_MKVAL" , cursorAsset.getString(15));
                ApplicationDataJson.put("AP_INTDU" , cursorAsset.getString(16));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursorAsset.close();

        //==== Create Json Request To Send Live Database...
        String Urlapplication = PHP_URL_SQL + "Po-Request-Application-DataJsonRead.php";
        final JsonObjectRequest jsonObjectRequest_applicationdata = new JsonObjectRequest(Request.Method.POST, Urlapplication, ApplicationDataJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ApplicationJsonResponce = response.getString("RESULT-APPLICATION");
                            Log.d("Respoces" , ApplicationJsonResponce);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RespocesCount = RespocesCount + 1;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                androidx.appcompat.app.AlertDialog.Builder bmyAlert = new androidx.appcompat.app.AlertDialog.Builder(mContex);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.DataJsonRead")
                        .create();
                bmyAlert.show();
            }
        });

        //=======================================================================================================================
        //================ Send the Charges Data To Live database.  ========================================================
        Cursor cursorcharge = sqLiteDatabase_poRequestApplication.rawQuery("SELECT APPLICATION_REF_NO,OT_CHARGE_NAME,OT_CHARGE_AMT,OT_CHARGE_TYPE FROM LE_CHARGS WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);
        JSONObject jsonObjectCharge         = new JSONObject();
        JSONArray jsonArray_charges         = new JSONArray();
        JSONObject jsonObjectCharge_input   = new JSONObject();
        if (cursorcharge.getCount() != 0)
        {
            cursorcharge.moveToFirst();
            do{
                jsonObjectCharge = new JSONObject();
                try {
                    jsonObjectCharge.put("APPLICATION_REF_NO" , cursorcharge.getString(0));
                    jsonObjectCharge.put("OT_CHARGE_NAME" , cursorcharge.getString(1));
                    jsonObjectCharge.put("OT_CHARGE_AMT" , cursorcharge.getString(2));
                    jsonObjectCharge.put("OT_CHARGE_TYPE" , cursorcharge.getString(3));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray_charges.put(jsonObjectCharge);


            }while (cursorcharge.moveToNext());
        }
        cursorcharge.close();

        String UrlCharges = PHP_URL_SQL + "MOBILE-PO-CHARGESCREATE.php";
        JsonArrayRequest jsonObjectRequest_chargesdata = new JsonArrayRequest(Request.Method.POST, UrlCharges, jsonArray_charges,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject = response.getJSONObject(0);
                            ChargesJsonREsponce = jsonObject.getString("RESULT-APPLICATION");
                            Log.d("Respoces" , ChargesJsonREsponce);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RespocesCount = RespocesCount + 1;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                androidx.appcompat.app.AlertDialog.Builder bmyAlert = new androidx.appcompat.app.AlertDialog.Builder(mContex);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing-CHARGESCREATE")
                        .create();
                bmyAlert.show();
            }
        });
        jsonObjectRequest_chargesdata.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //=======================================================================================================================
        //================ Send the Image Details Data To Live database.  ========================================================
        JSONArray jsonArray_Image         = new JSONArray();
        Cursor cursor_doc_image = sqLiteDatabase_poRequestApplication.rawQuery("SELECT * FROM AP_DOC_IMAGE WHERE APP_REF_NO = '" + mInpAppno + "'" , null);
        if (cursor_doc_image.getCount() != 0)
        {
            cursor_doc_image.moveToFirst();
            do{
                JSONObject jsonObjectImage = new JSONObject();
                try {
                    jsonObjectImage.put("APPLICATION_REF_NO" , cursor_doc_image.getString(0));
                    jsonObjectImage.put("DOC_REF" , cursor_doc_image.getString(1));
                    jsonObjectImage.put("DOC_TYPE" , cursor_doc_image.getString(3));
                    jsonObjectImage.put("DOC_STS" , "A");
                    jsonObjectImage.put("DOC_DATE" , cursor_doc_image.getString(2));
                    jsonObjectImage.put("DOC_USER" , cursor_doc_image.getString(4));
                    jsonObjectImage.put("DOC_IMAGE" , cursor_doc_image.getString(5));
                    jsonObjectImage.put("DOC_REMARKS" , cursor_doc_image.getString(6));
                    jsonObjectImage.put("DOC_REF_NIC" , cursor_doc_image.getString(7));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArray_Image.put(jsonObjectImage);

            }while (cursor_doc_image.moveToNext());
        }
        cursor_doc_image.close();

        String UrlImage = PHP_URL_SQL + "MOBILE-COMPLETE-DOC-IMAGE.php";
        JsonArrayRequest jsonObjectRequest_Imagedata = new JsonArrayRequest(Request.Method.POST, UrlImage, jsonArray_Image,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject = response.getJSONObject(0);
                            ImageResponces = jsonObject.getString("RESULT-APPLICATION");
                            Log.d("Respoces" , ImageResponces);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RespocesCount = RespocesCount + 1;
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
        jsonObjectRequest_Imagedata.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        //=====================================================================
        //=== Send PO Request To live data base
        JSONObject jsonObjectPorequest = new JSONObject();
        try {
            jsonObjectPorequest.put("APPLICATION_REF_NO" , mInpAppno);
            jsonObjectPorequest.put("CL_NIC" , mClNic);
            jsonObjectPorequest.put("REQ_BRANCH" , mBranch);
            jsonObjectPorequest.put("REQ_OFFICER" , mMkOfficer);
            jsonObjectPorequest.put("MGR_CODE" , "");
            jsonObjectPorequest.put("REQ_PO_STS" , "001");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonDataTranferToLive jsonDataTranferToLive1Porequest = new JsonDataTranferToLive(mContex);
        String UrlPorequest = PHP_URL_SQL + "MOBILE-PO-REQUEST.php";
        jsonDataTranferToLive1Porequest.SendDataToLive(UrlPorequest , jsonObjectPorequest);

     //========================== Finesh The Json REquest Create ==============================================================

        progressDialog.show();
        requestQueue.start();
        requestQueue.add(jsonObjectRequest_applicationdata);  //=== Appliocation request
        requestQueue.add(jsonObjectRequest_chargesdata);  //=== charges request
        requestQueue.add(jsonObjectRequest_Imagedata);  //=== Image request

        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {

                Boolean CheckApplication=false , CheckCharges=false , CheckImage=false;
                if (ApplicationJsonResponce != null)
                {
                    CheckApplication    = ApplicationJsonResponce.equals("DONE");
                }

                if (ChargesJsonREsponce != null)
                {
                    CheckCharges        = ChargesJsonREsponce.equals("DONE");
                }

                if (ImageResponces != null)
                {
                    CheckImage          = ImageResponces.equals("DONE");
                }

                if (CheckApplication==true && CheckCharges==true && CheckImage==true)
                {
                    progressDialog.dismiss();
                    //=== Update Application Status
                    sqlliteCreateLeasing_porequest.UpdateAppSts(mInpAppno , "001" , "S");

                    //===========================================
                    requestQueue.getCache().clear();
                    sqLiteDatabase_poRequestApplication.close();
                    sqlliteCreateLeasing_porequest.close();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContex);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Po Request Successfully.");
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
