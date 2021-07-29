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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ConcurrentModificationException;

import dmax.dialog.SpotsDialog;

public class Volly_FileComplete
{
    public Context mContex;
    public String  FileUploadSts , ApplicationJsonResponce , mInpAppno , PHP_URL_SQL , LoginUser="" , LoginDate = "" , LoginBranch= "";
    public SqlliteCreateLeasing sqlliteCreateLeasing_FileComplete;
    public SQLiteDatabase sqLiteDatabase_FileComplete;
    private android.app.AlertDialog progressDialog;
    public RequestQueue requestQueue_FileComplete;
    public Boolean CheckResponce;

    public Volly_FileComplete (Context context)
    {
        Log.d("Volly_FileComplete-" , "RUN");
        mContex = context;
        sqlliteCreateLeasing_FileComplete = new SqlliteCreateLeasing(mContex);
        progressDialog = new SpotsDialog(mContex, R.style.Custom);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
    }

    public void SendFileComplete(String Appno)
    {
        /*
        //=======================================================
        //        Create Volly Request
        //=======================================================
        Cache cache = new DiskBasedCache(mContex.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue_FileComplete = new RequestQueue(cache, network);
        //=======================================================
        */

        requestQueue_FileComplete   = VollySingleton.getInstance(mContex).getRequestQueue();

        mInpAppno = Appno;
        sqLiteDatabase_FileComplete = sqlliteCreateLeasing_FileComplete.getReadableDatabase();

        JSONObject jsonObject_FileComplete  = new JSONObject();
        JSONArray jsonArray_Arry_ClientData = new JSONArray();
        JSONArray jsonArray_Arry_GurData    = new JSONArray();
        JSONArray jsonArray_Arry_ImgData    = new JSONArray();
        JSONArray jsonArray_Arry_ComplateData    = new JSONArray();

        Cursor cursor_CLIENT = sqLiteDatabase_FileComplete.rawQuery("SELECT * FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);
        if (cursor_CLIENT.getCount() != 0)
        {
            cursor_CLIENT.moveToFirst();
            JSONObject jsonObject_Obj_client = new JSONObject();
            try {
                jsonObject_Obj_client.put("APPLICATION_REF_NO" , cursor_CLIENT.getString(0));
                jsonObject_Obj_client.put("CL_NIC" , cursor_CLIENT.getString(1));
                jsonObject_Obj_client.put("CL_TITLE" , cursor_CLIENT.getString(2));
                jsonObject_Obj_client.put("CL_FULLY_NAME" , cursor_CLIENT.getString(3));
                jsonObject_Obj_client.put("CL_ADDERS_1" , cursor_CLIENT.getString(4));
                jsonObject_Obj_client.put("CL_ADDERS_2" , cursor_CLIENT.getString(5));
                jsonObject_Obj_client.put("CL_ADDERS_3" , cursor_CLIENT.getString(6));
                jsonObject_Obj_client.put("CL_ADDERS_4" , cursor_CLIENT.getString(7));
                jsonObject_Obj_client.put("CL_GENDER" , cursor_CLIENT.getString(8));
                jsonObject_Obj_client.put("CL_MARITAL_STATUS" , cursor_CLIENT.getString(9));
                jsonObject_Obj_client.put("CL_INITIALS" , cursor_CLIENT.getString(10));
                jsonObject_Obj_client.put("CL_LASTNAME" , cursor_CLIENT.getString(11));
                jsonObject_Obj_client.put("CL_DATE_OF_BIRTH" , cursor_CLIENT.getString(12));
                jsonObject_Obj_client.put("CL_AGE" , cursor_CLIENT.getString(13));
                jsonObject_Obj_client.put("CL_MOBILE_NO" , cursor_CLIENT.getString(14));
                jsonObject_Obj_client.put("CL_LAND_NO" , cursor_CLIENT.getString(15));
                jsonObject_Obj_client.put("CL_OCCUPATION" , cursor_CLIENT.getString(17));
                jsonObject_Obj_client.put("CL_INCOME" , cursor_CLIENT.getString(18));
                jsonObject_Obj_client.put("CL_CREATE_DATE" , cursor_CLIENT.getString(19));
                jsonObject_Obj_client.put("CL_CREATE_USER" , cursor_CLIENT.getString(20));
                jsonObject_Obj_client.put("CL_BRANCH" , cursor_CLIENT.getString(21));
                jsonObject_Obj_client.put("SECTOR" , cursor_CLIENT.getString(22));
                jsonObject_Obj_client.put("SUB_SECTOR" , cursor_CLIENT.getString(23));
                jsonObject_Obj_client.put("INCOME_SOURCE" , cursor_CLIENT.getString(24));
                jsonObject_Obj_client.put("INCOME_AMT" , cursor_CLIENT.getString(25));
                jsonObject_Obj_client.put("CL_TAX" , cursor_CLIENT.getString(26));
                jsonObject_Obj_client.put("CL_TAX_CODE" , cursor_CLIENT.getString(27));
                jsonObject_Obj_client.put("CL_COUNTRY" , cursor_CLIENT.getString(28));
                jsonObject_Obj_client.put("CL_PROVINCE" , cursor_CLIENT.getString(29));
                jsonObject_Obj_client.put("CL_DISTRICT" , cursor_CLIENT.getString(30));
                jsonObject_Obj_client.put("CL_AREA_CODE" , cursor_CLIENT.getString(31));
                jsonObject_Obj_client.put("CL_EMAIL" , cursor_CLIENT.getString(32));
                jsonObject_Obj_client.put("CL_NATION" , cursor_CLIENT.getString(33));
                jsonObject_Obj_client.put("CL_MAIL_ADD1" , cursor_CLIENT.getString(34));
                jsonObject_Obj_client.put("CL_MAIL_ADD2" , cursor_CLIENT.getString(35));
                jsonObject_Obj_client.put("CL_MAIL_ADD3" , cursor_CLIENT.getString(36));
                jsonObject_Obj_client.put("CL_MAIL_ADD4" , cursor_CLIENT.getString(37));
                jsonObject_Obj_client.put("CL_EMARKS" , cursor_CLIENT.getString(38));

                jsonArray_Arry_ClientData.put(jsonObject_Obj_client);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor_CLIENT.close();

        //===============================================================================================
        //       Gur Details Json Create
        //===============================================================================================
        Cursor cursor_Gur_Details = sqLiteDatabase_FileComplete.rawQuery("SELECT * FROM LE_CO_CL_DATA WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);
        if (cursor_Gur_Details.getCount() != 0) {
            cursor_Gur_Details.moveToFirst();
            do
            {
                JSONObject jsonObject_Gur_Details = new JSONObject();
                try {
                    jsonObject_Gur_Details.put("APPLICATION_REF_NO", cursor_Gur_Details.getString(0));
                    jsonObject_Gur_Details.put("CO_CLTYPE", cursor_Gur_Details.getString(1));
                    jsonObject_Gur_Details.put("CO_NIC", cursor_Gur_Details.getString(2));
                    jsonObject_Gur_Details.put("CO_TITLE", cursor_Gur_Details.getString(3));
                    jsonObject_Gur_Details.put("CO_FULLY_NAME", cursor_Gur_Details.getString(4));
                    jsonObject_Gur_Details.put("CO_ADDERS_1", cursor_Gur_Details.getString(5));
                    jsonObject_Gur_Details.put("CO_ADDERS_2", cursor_Gur_Details.getString(6));
                    jsonObject_Gur_Details.put("CO_ADDERS_3", cursor_Gur_Details.getString(7));
                    jsonObject_Gur_Details.put("CO_ADDERS_4", cursor_Gur_Details.getString(8));
                    jsonObject_Gur_Details.put("CO_GENDER", cursor_Gur_Details.getString(9));
                    jsonObject_Gur_Details.put("CO_MARITAL_STATUS", cursor_Gur_Details.getString(10));
                    jsonObject_Gur_Details.put("CO_INITIALS", cursor_Gur_Details.getString(11));
                    jsonObject_Gur_Details.put("CO_LASTNAME", cursor_Gur_Details.getString(12));
                    jsonObject_Gur_Details.put("CO_DATE_OF_BIRTH", cursor_Gur_Details.getString(13));
                    jsonObject_Gur_Details.put("CO_AGE", cursor_Gur_Details.getString(14));
                    jsonObject_Gur_Details.put("CO_MOBILE_NO", cursor_Gur_Details.getString(15));
                    jsonObject_Gur_Details.put("CO_SECTOR", cursor_Gur_Details.getString(17));
                    jsonObject_Gur_Details.put("CO_SUBSECTOR", cursor_Gur_Details.getString(18));
                    jsonObject_Gur_Details.put("CO_OCCUPATION", cursor_Gur_Details.getString(19));
                    jsonObject_Gur_Details.put("CO_INCOME", cursor_Gur_Details.getString(20));
                    jsonObject_Gur_Details.put("CO_SEC_VALUE", cursor_Gur_Details.getString(21));
                    jsonObject_Gur_Details.put("CO_CREATE_DATE", cursor_Gur_Details.getString(22));
                    jsonObject_Gur_Details.put("CO_CREATE_USER", cursor_Gur_Details.getString(23));
                    jsonObject_Gur_Details.put("CO_BRANCH", cursor_Gur_Details.getString(24));
                    jsonObject_Gur_Details.put("RELATIONS_TYPE", cursor_Gur_Details.getString(25));
                    jsonObject_Gur_Details.put("CO_COUNTRY", cursor_Gur_Details.getString(26));
                    jsonObject_Gur_Details.put("CO_PROVINCE", cursor_Gur_Details.getString(27));
                    jsonObject_Gur_Details.put("CO_DISTRICT", cursor_Gur_Details.getString(28));
                    jsonObject_Gur_Details.put("CO_AREA_CODE", cursor_Gur_Details.getString(29));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray_Arry_GurData.put(jsonObject_Gur_Details);

            }while (cursor_Gur_Details.moveToNext());
        }
        cursor_Gur_Details.close();


        //===============================================================================================
        //       Image Details Json Create
        //===============================================================================================
        Cursor cursor_Doc_Image = sqLiteDatabase_FileComplete.rawQuery("SELECT * FROM AP_DOC_IMAGE WHERE APP_REF_NO = '" + mInpAppno + "'", null);
        if (cursor_Doc_Image.getCount() != 0) {
            cursor_Doc_Image.moveToFirst();

            do {
                JSONObject jsonObject_Doc_Image = new JSONObject();
                try {
                    jsonObject_Doc_Image.put("APPLICATION_REF_NO", cursor_Doc_Image.getString(0));
                    jsonObject_Doc_Image.put("DOC_REF", cursor_Doc_Image.getString(1));
                    jsonObject_Doc_Image.put("DOC_TYPE", cursor_Doc_Image.getString(3));
                    jsonObject_Doc_Image.put("DOC_STS", "A");
                    jsonObject_Doc_Image.put("DOC_DATE", cursor_Doc_Image.getString(2));
                    jsonObject_Doc_Image.put("DOC_USER", cursor_Doc_Image.getString(4));
                    jsonObject_Doc_Image.put("DOC_IMAGE", cursor_Doc_Image.getString(5));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray_Arry_ImgData.put(jsonObject_Doc_Image);

            } while (cursor_Doc_Image.moveToNext());
        }
        cursor_Doc_Image.close();


        //===============================================================================================
        //       Final Data Complete.
        //===============================================================================================
        String mCL_NIC = "";
        Cursor cursor_complete = sqLiteDatabase_FileComplete.rawQuery("SELECT CL_NIC FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mInpAppno + "'", null);
        if (cursor_complete.getCount() != 0) {
            cursor_complete.moveToFirst();
            mCL_NIC = cursor_complete.getString(0);
        }

        JSONObject jsonObject_complete_app = new JSONObject();
        try {
            jsonObject_complete_app.put("APP_REF_NO", mInpAppno);
            jsonObject_complete_app.put("CL_NIC", mCL_NIC);
            jsonObject_complete_app.put("BRANCH_CODE", LoginBranch);
            jsonObject_complete_app.put("MKT_CODE", LoginUser);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray_Arry_ComplateData.put(jsonObject_complete_app);
        cursor_complete.close();

        //=========== Create Final Json Object ====
        try {
            jsonObject_FileComplete.put("CLIENT" , jsonArray_Arry_ClientData);
            jsonObject_FileComplete.put("GURNTER" , jsonArray_Arry_GurData);
            //jsonObject_FileComplete.put("IMAGE" , jsonArray_Arry_ImgData);
            jsonObject_FileComplete.put("FINAL" , jsonArray_Arry_ComplateData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //=====================================

        //==== Create Json Request To Send Live Database...
        String Urlapplication = PHP_URL_SQL + "File-Complete-ApplicationData.php";
        JsonObjectRequest jsonObjectRequest_CompleteData = new JsonObjectRequest(Request.Method.POST, Urlapplication, jsonObject_FileComplete,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            ApplicationJsonResponce = response.getString("RESULT-COMPLETE");
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
                150000,
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
                    Cursor cursor_file_path = sqLiteDatabase_FileComplete.rawQuery("SELECT FOLDER_PATH FROM AP_IMAGE_FOLDER_DETAILS WHERE APP_REFNO = '" + mInpAppno + "'" , null);
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
                        sqlliteCreateLeasing_FileComplete.UpdateAppSts(mInpAppno , "003" , "D");
                        requestQueue_FileComplete.getCache().clear();

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
