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

import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class Po_DataSend_LiveServer
{
    public Context mContex;
    private android.app.AlertDialog progressDialog;
    private String FileUploadSts="" , InputApplicationno , PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , mClNic , mBranch , mMkOfficer , ApplicationJsonResponce , ApplicationJsonResponce_Image;
    public SqlliteCreateLeasing sqlliteCreateLeasing_PoDataRequest;
    public SQLiteDatabase sqLiteDatabase_PoData;
    public RequestQueue requestQueue_FileComplete , requestQueue_Data;
    private String mData_Appno="" , mDataCl_Nic="" , mData_FullyName="" , mData_Product="" , mData_SubProduct="" , mData_Mobileno="";

    public Po_DataSend_LiveServer(Context context)
    {
        mContex = context;
        sqlliteCreateLeasing_PoDataRequest = new SqlliteCreateLeasing(mContex);
        sqLiteDatabase_PoData = sqlliteCreateLeasing_PoDataRequest.getReadableDatabase();

        progressDialog = new SpotsDialog(mContex, R.style.Custom);
        progressDialog.setTitle("Sending ...");
        progressDialog.setMessage("Please wait...");

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

    }

    public void SendDataToPo(String mInpAppno)
    {
        //=== Create Joson Data

        InputApplicationno = mInpAppno;
        mData_Appno = mInpAppno;

        JSONArray jsonObject_ApplicationData   =   new JSONArray();
        JSONArray jsonObject_ClientData        =   new JSONArray();
        JSONArray jsonObject_AssetData         =   new JSONArray();
        JSONArray jsonObject_ChargesData       =   new JSONArray();
        JSONArray jsonObject_PoData            =   new JSONArray();

        //==== Get Application Data
        Cursor mApplicationCurser = sqLiteDatabase_PoData.rawQuery("SELECT APPLICATION_REF_NO,AP_PRODUCT,AP_INVOICE_AMT,AP_DOWN_PAY,AP_FACILITY_AMT,AP_ETV,AP_RATE," +
                "AP_PERIOD,AP_RENTAL_AMT,AP_MK_OFFICER,AP_BRANCH,AP_PO_STS,AP_PO_SEND_DELEAR,AP_PO_DELEAR_EMAIL,AP_ENTDATE,AP_STAGE,AP_MAIN_SUPPLIR,AP_MAIN_SUPPLIR_EMAIL,AP_PO_RQ_USER FROM LE_APPLICATION WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);

        if (mApplicationCurser.getCount() != 0)
        {
            mApplicationCurser.moveToFirst();
            JSONObject ApplicationDataJson = new JSONObject();
            try {

                mData_SubProduct    =  mApplicationCurser.getString(1);

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

                //=== Final Json Data
                jsonObject_ApplicationData.put(ApplicationDataJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mApplicationCurser.close();

        //==== Get Client Data
        Cursor mClientCurser = sqLiteDatabase_PoData.rawQuery("SELECT APPLICATION_REF_NO,CL_NIC,CL_TITLE,CL_FULLY_NAME,CL_ADDERS_1" +
                ",CL_ADDERS_2,CL_ADDERS_3,CL_ADDERS_4,CL_OCCUPATION,CL_MOBILE_NO,CL_CREATE_DATE,CL_CREATE_USER,INCOME_SOURCE,CL_DATE_OF_BIRTH,CL_AGE FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);

        if (mClientCurser.getCount() != 0)
        {
            mClientCurser.moveToFirst();
            JSONObject ApplicationClData = new JSONObject();
            try {
                mDataCl_Nic     =   mClientCurser.getString(1);
                mData_FullyName =   mClientCurser.getString(3);
                mData_Mobileno  =   mClientCurser.getString(9);

                ApplicationClData.put("CL_APPLICATION_REF_NO", mClientCurser.getString(0));
                ApplicationClData.put("CL_NIC", mClientCurser.getString(1));
                ApplicationClData.put("CL_TITLE", mClientCurser.getString(2));
                ApplicationClData.put("CL_FULLY_NAME", mClientCurser.getString(3));
                ApplicationClData.put("CL_ADDERS_1", mClientCurser.getString(4));
                ApplicationClData.put("CL_ADDERS_2", mClientCurser.getString(5));
                ApplicationClData.put("CL_ADDERS_3", mClientCurser.getString(6));
                ApplicationClData.put("CL_ADDERS_4", mClientCurser.getString(7));
                ApplicationClData.put("CL_OCCUPATION", mClientCurser.getString(8));
                ApplicationClData.put("CL_MOBILE_NO", mClientCurser.getString(9));
                ApplicationClData.put("CL_CREATE_DATE", mClientCurser.getString(10));
                ApplicationClData.put("CL_CREATE_USER", mClientCurser.getString(11));
                ApplicationClData.put("INCOME_SOURCE", mClientCurser.getString(12));
                ApplicationClData.put("CL_DATE_OF_BIRTH", mClientCurser.getString(13));
                ApplicationClData.put("CL_AGE", mClientCurser.getString(14));

                mClNic = mClientCurser.getString(1);

                //=== Final Json Data
                jsonObject_ClientData.put(ApplicationClData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mClientCurser.close();

        //==== Get Asset Data
        Cursor cursorAsset = sqLiteDatabase_PoData.rawQuery("SELECT APPLICATION_REF_NO,AS_EQ_TYPE,AS_EQ_CATAGE,AS_EQ_MAKE,AS_EQ_MODEL,AS_EQ_REGNO,AS_EQ_ENG_NO,AS_EQ_CHAS_NO,AS_EQ_YEAR," +
                "AS_INV_DELER,AS_INV_SUPPLIER,AP_INV_DATE,AP_INSURANCE_COMPANY,AP_ENT_DATE,AP_ENT_USER,MK_VAL ,AP_INTDU,M_IND_CODE,M_SUP_CODE,M_DELEAR_CODE FROM LE_ASSET_DETAILS WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);

        if (cursorAsset.getCount() != 0)
        {
            cursorAsset.moveToFirst();
            JSONObject ApplicationAssetData = new JSONObject();
            try {

                mData_Product = cursorAsset.getString(1);

                ApplicationAssetData.put("AS_APPLICATION_REF_NO" , cursorAsset.getString(0));
                ApplicationAssetData.put("AS_EQ_TYPE" , cursorAsset.getString(1));
                ApplicationAssetData.put("AS_EQ_CATAGE" , cursorAsset.getString(2));
                ApplicationAssetData.put("AS_EQ_MAKE" , cursorAsset.getString(3));
                ApplicationAssetData.put("AS_EQ_MODEL" , cursorAsset.getString(4));
                ApplicationAssetData.put("AS_EQ_REGNO" , cursorAsset.getString(5));
                ApplicationAssetData.put("AS_EQ_ENG_NO" , cursorAsset.getString(6));
                ApplicationAssetData.put("AS_EQ_CHAS_NO" , cursorAsset.getString(7));
                ApplicationAssetData.put("AS_EQ_YEAR" , cursorAsset.getString(8));
                ApplicationAssetData.put("AS_INV_DELER" , cursorAsset.getString(9));
                ApplicationAssetData.put("AS_INV_SUPPLIER" , cursorAsset.getString(10));
                //JsonAsset.put("AP_INV_DATE" , cursorAsset.getString(11));
                ApplicationAssetData.put("AP_INV_DATE" , "2019-01-01");
                ApplicationAssetData.put("AP_INSURANCE_COMPANY" , cursorAsset.getString(12));
                ApplicationAssetData.put("AP_ENT_DATE" , cursorAsset.getString(13));
                ApplicationAssetData.put("AP_ENT_USER" , cursorAsset.getString(14));
                ApplicationAssetData.put("AP_MKVAL" , cursorAsset.getString(15));
                ApplicationAssetData.put("AP_INTDU" , cursorAsset.getString(16));
                ApplicationAssetData.put("M_IND_CODE" , cursorAsset.getString(17));
                ApplicationAssetData.put("M_SUP_CODE" , cursorAsset.getString(18));
                ApplicationAssetData.put("M_DELEAR_CODE" , cursorAsset.getString(19));

                //== Final Json
                jsonObject_AssetData.put(ApplicationAssetData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursorAsset.close();

        //==== Get Asset Data
        Cursor cursorcharge = sqLiteDatabase_PoData.rawQuery("SELECT APPLICATION_REF_NO,OT_CHARGE_NAME,OT_CHARGE_AMT,OT_CHARGE_TYPE FROM LE_CHARGS WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);
        if (cursorcharge.getCount() != 0)
        {
            cursorcharge.moveToFirst();
            do{
                JSONObject jsonObjectCharge = new JSONObject();
                try {
                    jsonObjectCharge.put("APPLICATION_REF_NO" , cursorcharge.getString(0));
                    jsonObjectCharge.put("OT_CHARGE_NAME" , cursorcharge.getString(1));
                    jsonObjectCharge.put("OT_CHARGE_AMT" , cursorcharge.getString(2));
                    jsonObjectCharge.put("OT_CHARGE_TYPE" , cursorcharge.getString(3));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //== Final Json
                jsonObject_ChargesData.put(jsonObjectCharge);
            }while (cursorcharge.moveToNext());
        }
        cursorcharge.close();

        //==== Get PO Request Data
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

        //=== Final Data
        jsonObject_PoData.put(jsonObjectPorequest);

        //========= Create Final Json Data - to Send
        JSONObject jsonObject_FileComplete  = new JSONObject();

        try {
            jsonObject_FileComplete.put("APPLICATION" , jsonObject_ApplicationData);
            jsonObject_FileComplete.put("CLIENT" , jsonObject_ClientData);
            jsonObject_FileComplete.put("ASSET" , jsonObject_AssetData);
            jsonObject_FileComplete.put("CHARGES" , jsonObject_ChargesData);
            jsonObject_FileComplete.put("PODATA" , jsonObject_PoData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //=== Create Request to send the Json data to live server
        String Urlapplication = "http://afimobile.abansfinance.lk/mobilephp/Po-Request-Application-DataJsonRead.php";
        JsonObjectRequest jsonObjectRequest_CompleteData = new JsonObjectRequest(Request.Method.POST, Urlapplication, jsonObject_FileComplete,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            ApplicationJsonResponce = response.getString("RESULT-APPLICATION");
                            Log.d("PO-REQ-RESPONSE" , ApplicationJsonResponce);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
        jsonObjectRequest_CompleteData.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //===== Create  NemProud db To view the image file to omniDoc system
        //==== Create Application Json Object
        JSONObject jsonObject_data = new JSONObject();
        try {
            jsonObject_data.put("APPLICATION_REF_NO" ,  mData_Appno);
            jsonObject_data.put("NIC" , mDataCl_Nic);
            jsonObject_data.put("FULLYNAME" , mData_FullyName);
            jsonObject_data.put("PRODUCT" , mData_Product);
            jsonObject_data.put("SUB_PRODUCT" , mData_SubProduct);
            jsonObject_data.put("MOBILE_NO" , mData_Mobileno);
            jsonObject_data.put("SUBMIT" , LoginUser );

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String Urlapplication_image = "http://203.115.12.125:82/core/mobnew/Image-Data-Record-Create.php";
        //String Urlapplication_image = "http://afimobile.abansfinance.lk/mobilephp/TEST.php";
        JsonObjectRequest jsonObjectRequest_ImageData = new JsonObjectRequest(Request.Method.POST, Urlapplication_image, jsonObject_data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ApplicationJsonResponce_Image  = response.getString("RESULT");
                            Log.d("IMAGE-DATA-RECORD" , ApplicationJsonResponce_Image);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                        .setTitle("Error... test")
                        .create();
                bmyAlert.show();
            }
        });

        jsonObjectRequest_ImageData.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        //progressDialog.show();
        requestQueue_FileComplete   = VollySingleton.getInstance(mContex).getRequestQueue();
        requestQueue_FileComplete.start();
        requestQueue_FileComplete.add(jsonObjectRequest_CompleteData);  //=== Appliocation request
        requestQueue_FileComplete.add(jsonObjectRequest_ImageData);     //=== Image Data Record Create

        //==== Request Finesh Check =========
        requestQueue_FileComplete.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {

                Boolean CheckApplication=false; Boolean CheckFileCommplete=false; Boolean mCheck_Image=false;

                if (ApplicationJsonResponce != null)
                {
                    CheckApplication    = ApplicationJsonResponce.equals("DONE");
                }

                if (ApplicationJsonResponce_Image != null)
                {
                    mCheck_Image    =   ApplicationJsonResponce_Image.equals("DONE");
                }

                if (CheckApplication==true && mCheck_Image==true)
                {

                    //==== To Send the upload Image file to server
                    //==== Get Application Image folder path
                    String mFileSavePath="";
                    Cursor cursor_file_path = sqLiteDatabase_PoData.rawQuery("SELECT FOLDER_PATH FROM AP_IMAGE_FOLDER_DETAILS WHERE APP_REFNO = '" + InputApplicationno + "'" , null);
                    if (cursor_file_path.getCount() != 0)
                    {
                        cursor_file_path.moveToFirst();
                        mFileSavePath = cursor_file_path.getString(0);
                    }
                    cursor_file_path.close();

                    //==== Send the Image Folder to Live server
                    FileUploadServer fileUploadServer = new FileUploadServer(mContex);
                    FileUploadSts = fileUploadServer.Upload_doc(InputApplicationno ,
                                                                mFileSavePath );

                    if (FileUploadSts != null)
                    {
                        CheckFileCommplete = FileUploadSts.equals("DONE") ;
                    }

                    if (CheckFileCommplete)
                    {
                        //progressDialog.dismiss();
                        //=== Update Application Status
                        Date currentTime = Calendar.getInstance().getTime();

                        sqlliteCreateLeasing_PoDataRequest.UpdateAppSts(InputApplicationno, "001" , "S");

                        //===========================================
                        requestQueue_FileComplete.stop();
                        requestQueue_FileComplete.getCache().clear();

                        if(!((Activity) mContex).isFinishing())
                        {
                            //show dialog
                            android.app.AlertDialog.Builder builder_SUCESS = new android.app.AlertDialog.Builder(mContex);
                            builder_SUCESS.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                            builder_SUCESS.setMessage("Po Request Successfully.");
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
            }
        });


    }
}
