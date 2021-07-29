package com.AFiMOBILE.afslmobileapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.LocaleData;
import android.text.Html;
import android.util.Log;

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

public class ManagerDataSync {

    public Context mContex;
    public String PHP_URL_SQL;
    private RequestQueue mQueue;
    private android.app.AlertDialog progressDialog;
    public String UserCode , RespoceData , RespoceDoneimage;
    public SqlliteCreateLeasing sqlliteCreateLeasing;
    public Boolean ChkRespoData=false , ChkRespImage=false;


    public ManagerDataSync (Context context)
    {
        mContex = context;
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        PHP_URL_SQL             =   globleClassDetails.getPHP_Path();
        mQueue                  =   VollySingleton.getInstance(mContex).getRequestQueue();
        progressDialog          =   new SpotsDialog(mContex, R.style.Custom);
        sqlliteCreateLeasing    =   new SqlliteCreateLeasing(mContex);

        RespoceData = "";
        RespoceDoneimage = "";


        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {

                if (RespoceData != null)
                {
                    ChkRespoData = RespoceData.equals("DONE");
                }

                if (RespoceDoneimage != null)
                {
                    ChkRespImage = RespoceDoneimage.equals("DONE");
                }

                if (ChkRespoData==true && ChkRespImage==true)
                {
                    progressDialog.dismiss();
                    mQueue.getCache().clear();
                    sqlliteCreateLeasing.close();

                    AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex);
                    bmyAlert.setMessage("Record Successfully Upload.").setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                            .setTitle("AFSL Mobile Leasing.test")
                            .create();
                    bmyAlert.show();
                }
            }
        });
    }

    public void GetManageApproveApplication(String mGrCode)
    {
        Log.d("GetManageApprove" , "DONE");
        UserCode = mGrCode;
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("MGR_CODE" , UserCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog.show();
        //==== Get Pending Approve PO Details ==========================
        //String PostUrlPendingPO = "http://afimobile.abansfinance.lk/mobilephp/MOBILE-PO-GET-APPLICATION-DETAILS.php";
        String PostUrlPendingPO = PHP_URL_SQL + "MOBILE-PO-GET-APPLICATION-DETAILS.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PostUrlPendingPO, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("MGR-APPLICATION" , "DONE");
                        try {
                            String  mBranchCode="", mPosts="" , TempAPPLICATION_REF_NO ="" , TempAP_FACILITY_AMT = "" , TempAP_RENTAL_AMT = "" , TempAP_PERIOD="" , TempAP_RATE="" ,
                                    TempAP_DOWN_PAY="" , TempAP_MK_OFFICER="" , TempCL_FULLY_NAME="" , TempCL_ADDERS_1="" , TempCL_ADDERS_2="" ,
                                    TempCL_ADDERS_3 = "" , TempCL_ADDERS_4="" , TempCL_MOBILE_NO="" , TempCL_OCCUPATION="" , TempAS_EQ_TYPE="" ,
                                    TempAS_EQ_MAKE = "" , TempAS_EQ_MODEL = "" , TempAS_EQ_CHAS_NO="" , TempAS_EQ_ENG_NO="" , TempAS_EQ_YEAR="" , TempMkVal="" ,
                                    Temp_EXP_RATE="" , Temp_product="" , Temp_inv_val="" , Temp_Eq_categery="" ,
                                    Temp_supplier="" , Temp_delera="" , Temp_Intduser="" , Temp_inscompaney="" ,
                                    TempCl_nic="" , TEmp_regno="";

                            RespoceData = "DONE";
                            JSONArray jsonArraygetdata = response.getJSONArray("TT-GET-PO-DETAILS");
                            for (int i=0 ; i< jsonArraygetdata.length();i++)
                            {
                                JSONObject mydataJson = jsonArraygetdata.getJSONObject(i);
                                TempAPPLICATION_REF_NO      =   mydataJson.getString("APPLICATION_REF_NO") ;
                                TempAP_FACILITY_AMT         =   mydataJson.getString("AP_FACILITY_AMT") ;
                                TempAP_RENTAL_AMT           =   mydataJson.getString("AP_RENTAL_AMT") ;
                                TempAP_PERIOD               =   mydataJson.getString("AP_PERIOD") ;
                                TempAP_RATE                 =   mydataJson.getString("AP_RATE") ;
                                TempAP_DOWN_PAY             =   mydataJson.getString("AP_DOWN_PAY") ;
                                TempAP_MK_OFFICER           =   mydataJson.getString("AP_MK_OFFICER") ;
                                TempCL_FULLY_NAME           =   mydataJson.getString("CL_FULLY_NAME") ;
                                TempCL_ADDERS_1             =   mydataJson.getString("CL_ADDERS_1") ;
                                TempCL_ADDERS_2             =   mydataJson.getString("CL_ADDERS_2") ;
                                TempCL_ADDERS_3             =   mydataJson.getString("CL_ADDERS_3") ;
                                TempCL_ADDERS_4             =   mydataJson.getString("CL_ADDERS_4") ;
                                TempCL_MOBILE_NO            =   mydataJson.getString("CL_MOBILE_NO") ;
                                TempCL_OCCUPATION           =   mydataJson.getString("CL_OCCUPATION") ;
                                TempAS_EQ_TYPE              =   mydataJson.getString("AS_EQ_TYPE") ;
                                TempAS_EQ_MAKE              =   mydataJson.getString("AS_EQ_MAKE") ;
                                TempAS_EQ_MODEL             =   mydataJson.getString("AS_EQ_MAKE") ;
                                TempAS_EQ_CHAS_NO           =   mydataJson.getString("AS_EQ_CHAS_NO") ;
                                TempAS_EQ_ENG_NO            =   mydataJson.getString("AS_EQ_ENG_NO") ;
                                TempAS_EQ_YEAR              =   mydataJson.getString("AS_EQ_YEAR") ;
                                TempMkVal                   =   mydataJson.getString("AP_MKVAL") ;

                                Temp_EXP_RATE               =   mydataJson.getString("AP_ETV") ;
                                Temp_product                =   mydataJson.getString("AP_PRODUCT") ;
                                Temp_inv_val                =   mydataJson.getString("AP_INVOICE_AMT") ;
                                Temp_Eq_categery            =   mydataJson.getString("AS_EQ_CATAGE") ;

                                Temp_supplier               =   mydataJson.getString("AS_INV_SUPPLIER") ;
                                Temp_delera                 =   mydataJson.getString("AS_INV_DELER") ;
                                Temp_Intduser               =   mydataJson.getString("AP_INTDU") ;
                                Temp_inscompaney            =   mydataJson.getString("AP_INSURANCE_COMPANY") ;

                                TempCl_nic                  =   mydataJson.getString("CL_NIC") ;
                                TEmp_regno                  =   mydataJson.getString("AS_EQ_REGNO") ;

                                mBranchCode =   "HO";
                                mPosts      =   "001";

                                //=== Manager Pending Application
                                sqlliteCreateLeasing.CreatePendingPO(TempAPPLICATION_REF_NO,TempCl_nic,TempCL_FULLY_NAME,TempAP_FACILITY_AMT,TempAP_MK_OFFICER,mBranchCode,mPosts,UserCode);

                                //=== Pending Application Details
                                sqlliteCreateLeasing.PoPendingApplicationDetails(TempAPPLICATION_REF_NO , TempAP_FACILITY_AMT , TempAP_RENTAL_AMT , TempAP_PERIOD , TempAP_RATE
                                        ,TempAP_DOWN_PAY , TempAP_MK_OFFICER , TempCL_FULLY_NAME , TempCL_ADDERS_1 , TempCL_ADDERS_2 , TempCL_ADDERS_3 , TempCL_ADDERS_4 ,  TempCL_MOBILE_NO ,
                                        TempCL_OCCUPATION , TempAS_EQ_TYPE , TempAS_EQ_MAKE , TempAS_EQ_MODEL , TempAS_EQ_CHAS_NO , TempAS_EQ_ENG_NO , TempAS_EQ_YEAR , TempMkVal , Temp_EXP_RATE, Temp_product, Temp_inv_val ,  Temp_Eq_categery ,
                                        Temp_supplier , Temp_delera , Temp_Intduser , Temp_inscompaney , TempCl_nic , TEmp_regno  , "NO" , "NO");

                            }
                            Log.d("ManagerRespoce" , "DONE");

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

                progressDialog.dismiss();
                String ErrorDescription="Responses Failure.(" + ErrorCode.toString() + ")";
                android.app.AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex );
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

        //===========================================================

        //=== Get Pending PO Image Data
        String PostUrlPendingPO_Image = PHP_URL_SQL + "MOBILE-MGR-IMAGE-REQ.php";
        JsonObjectRequest jsonObjectRequest_image = new JsonObjectRequest(Request.Method.POST, PostUrlPendingPO_Image, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("MGR-IMAGE" , "DONE");
                        try {
                            String mAppno="" , mDcoref="" , mdoctype="" , mdocimage="";
                            RespoceDoneimage = "DONE";
                            JSONArray jsonArraygetdata_image = response.getJSONArray("TT-NEW-MGR-IMAGE");

                            for (int i=0 ; i< jsonArraygetdata_image.length();i++)
                            {
                                JSONObject mydataJson = jsonArraygetdata_image.getJSONObject(i);
                                mAppno          =   mydataJson.getString("APPLICATION_REF_NO");
                                mDcoref         =   mydataJson.getString("DOC_REF");
                                mdoctype        =   mydataJson.getString("DOC_TYPE");
                                mdocimage       =   mydataJson.getString("DOC_IMAGE");

                                sqlliteCreateLeasing.CreateManagerImage(mAppno , mDcoref , mdoctype , mdocimage);
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

                progressDialog.dismiss();
                String ErrorDescription="Responses Failure.(" + ErrorCode.toString() + ")";
                android.app.AlertDialog.Builder bmyAlert = new android.app.AlertDialog.Builder(mContex );
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
        jsonObjectRequest_image.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.start();
        mQueue.add(jsonObjectRequest);
        mQueue.add(jsonObjectRequest_image);

    }
}
