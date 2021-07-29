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

import java.util.Currency;

import dmax.dialog.SpotsDialog;

import static org.jsoup.nodes.Entities.EscapeMode.base;

public class Volly_Request_Recovery_Data
{
    public Context mContex;
    public SqlliteCreateRecovery sqlliteCreateRecovery_GetMaster;
    public SQLiteDatabase sqLiteDatabase_GetMaster;
    public RequestQueue requestQueue_FileComplete , requestQueue_PostAllAction , requestQueue_GetRecoveryData , requestQueue_FacilityAction;
    private android.app.AlertDialog progressDialog;
    public String PHP_URL_SQL , DataAvialbleCheck , LoginUser="";
    public RequestQueue requestQueue_final;
    public String MainResponceDistance ,  MainResponceRepocess , MainResponcerrRequestAction="" ,  mReqMgreCode="", InputFacno="" ,  MainResponceManagerReq="" , ManagerCode="" , MainResponceCode="" , mLoginCode="" , MainResponceCodeRecovery="" , UpdateFacno="" , FacilityResponceCodeRecovery="" , ManagerAssignResponces="";

    public Volly_Request_Recovery_Data(Context context)
    {
        mContex = context;
        sqlliteCreateRecovery_GetMaster = new SqlliteCreateRecovery(mContex);
        sqLiteDatabase_GetMaster = sqlliteCreateRecovery_GetMaster.getWritableDatabase();
        PHP_URL_SQL = "http://afimobile.abansfinance.lk/mobilephp/";
        progressDialog = new SpotsDialog(mContex, R.style.Custom);


        final Cache cache = new DiskBasedCache(mContex.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue_final = new RequestQueue(cache, network);

    }

    //========================================================
    //        Post Travelling Distance Data to Live Server
    //========================================================
    public void PostTravellingDistacne(String Mgrcode)
    {
        mReqMgreCode =  Mgrcode;
        MainResponceDistance="";
        JSONArray jsonArray_Mgerdata = new JSONArray();

        SQLiteDatabase sqLiteDatabase_DistanceUpdate = sqlliteCreateRecovery_GetMaster.getReadableDatabase();
        Cursor cursor_distance = sqLiteDatabase_DistanceUpdate.rawQuery("SELECT * FROM recovery_distance_data WHERE user_id = '" + mReqMgreCode + "' AND Live_Server_update = ''" , null  );
        if (cursor_distance.getCount() != 0)
        {
            cursor_distance.moveToFirst();
            do{

                JSONObject jsonObject_Post_Distance = new JSONObject();
                try {
                    jsonObject_Post_Distance.put("facility_no" , cursor_distance.getString(0));
                    jsonObject_Post_Distance.put("user_id" , cursor_distance.getString(1));
                    jsonObject_Post_Distance.put("ent_date" , cursor_distance.getString(2));
                    jsonObject_Post_Distance.put("allowance_km_lkr" , cursor_distance.getString(3));
                    jsonObject_Post_Distance.put("food_allow" , cursor_distance.getString(4));
                    jsonObject_Post_Distance.put("no_distance_km" , cursor_distance.getString(5));
                    jsonObject_Post_Distance.put("in_time" , cursor_distance.getString(6));
                    jsonObject_Post_Distance.put("out_time" , cursor_distance.getString(7));
                    jsonObject_Post_Distance.put("visit_date" , cursor_distance.getString(8));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray_Mgerdata.put(jsonObject_Post_Distance);

            }while (cursor_distance.moveToNext());
        }

        //==== Create Final Json Array
        JSONObject jsonObject_PostFinal_Repocess = new JSONObject();
        try {
            jsonObject_PostFinal_Repocess.put("REC-DISTANCE-DATA" , jsonArray_Mgerdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //==== Create Json Request
        String url = PHP_URL_SQL + "RECOVERY-POST-TRAVELLING-DATA.php";
        JsonObjectRequest jsonObjectRequest_RecRequestTrevelling = new JsonObjectRequest(Request.Method.POST, url, jsonObject_PostFinal_Repocess,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            MainResponceDistance = response.getString("RESULT");
                            Log.d("PostTravellingDistacne", MainResponceDistance);
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
                        .setTitle("Error... - 1 ")
                        .create();
                bmyAlert.show();
            }
        });
        jsonObjectRequest_RecRequestTrevelling.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog.show();
        requestQueue_final.start();
        requestQueue_final.add(jsonObjectRequest_RecRequestTrevelling);

        //==== Fineash Request
        requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce = false;
                String mDataCheck="";
                if (MainResponceDistance != null)
                {
                    if (MainResponceDistance.equals("DONE"))
                    {
                        CheckResponce = MainResponceDistance.equals("DONE");
                    }
                }

                if (CheckResponce == true)
                {
                    Log.d("PostTravellingDistacne", "FINISH");
                    progressDialog.dismiss();
                    sqlliteCreateRecovery_GetMaster.close();
                    requestQueue_final.getCache().clear();

                    SQLiteDatabase sqLiteDatabase_updateglg = sqlliteCreateRecovery_GetMaster.getWritableDatabase();
                    ContentValues contentValues_data = new ContentValues();
                    contentValues_data.put("Live_Server_update" , "Y");
                    sqLiteDatabase_updateglg.update("recovery_distance_data" ,contentValues_data , "user_id = ?" , new String[]{mReqMgreCode});

                }
            }
        });
    }

    //========================================================
    //        Post Repocess Data to Live Server
    //========================================================
    public void PostRepocessData(String Mgrcode)
    {
        mReqMgreCode =  Mgrcode;
        MainResponceRepocess="";
        DataAvialbleCheck="";

        JSONArray jsonArray_Mgerdata = new JSONArray();
        SQLiteDatabase sqLiteDatabase_RepcessData = sqlliteCreateRecovery_GetMaster.getReadableDatabase();
        Cursor cursor_RepocessData = sqLiteDatabase_RepcessData.rawQuery("SELECT * FROM recovery_repocess_order where req_user = '" + Mgrcode + "' AND Live_Server_Update = ''" , null );
        if (cursor_RepocessData.getCount() != 0)
        {
            DataAvialbleCheck="YES";
            cursor_RepocessData.moveToFirst();
            do{
                JSONObject jsonObject_Post_Repocess = new JSONObject();
                try {
                    jsonObject_Post_Repocess.put("facility_no" , cursor_RepocessData.getString(0));
                    jsonObject_Post_Repocess.put("clcode" , cursor_RepocessData.getString(1));
                    jsonObject_Post_Repocess.put("client_name" , cursor_RepocessData.getString(2));
                    jsonObject_Post_Repocess.put("vehicle_no" , cursor_RepocessData.getString(3));
                    jsonObject_Post_Repocess.put("sizer_code" , cursor_RepocessData.getString(4));
                    jsonObject_Post_Repocess.put("sizer_name" , cursor_RepocessData.getString(5));
                    jsonObject_Post_Repocess.put("sizer_clcode" , cursor_RepocessData.getString(6));
                    jsonObject_Post_Repocess.put("approve_mgr_code" , cursor_RepocessData.getString(7));
                    jsonObject_Post_Repocess.put("approve_mgr_name" , cursor_RepocessData.getString(8));
                    jsonObject_Post_Repocess.put("req_date" , cursor_RepocessData.getString(10));
                    jsonObject_Post_Repocess.put("req_user" , cursor_RepocessData.getString(11));
                    jsonObject_Post_Repocess.put("request_reason" , cursor_RepocessData.getString(12));
                    jsonObject_Post_Repocess.put("Comment" , cursor_RepocessData.getString(13));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArray_Mgerdata.put(jsonObject_Post_Repocess);

            }while (cursor_RepocessData.moveToNext());
        }
        else
        {
            DataAvialbleCheck="NO";
        }



        //==== Create Final Json Array
        JSONObject jsonObject_PostFinal_Repocess = new JSONObject();
        try {
            jsonObject_PostFinal_Repocess.put("REC-REPOCESS-DATA" , jsonArray_Mgerdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (DataAvialbleCheck.equals("YES"))
        {
            //==== Create Json Request
            String url = PHP_URL_SQL + "RECOVERY-POST-REPOCEESS-DATA.php";
            JsonObjectRequest jsonObjectRequest_RecRequestResponces = new JsonObjectRequest(Request.Method.POST, url, jsonObject_PostFinal_Repocess,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try {
                                MainResponceRepocess = response.getString("RESULT");
                                Log.d("PostRepocessData", MainResponceRepocess);
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
                            .setTitle("Error... - 2")
                            .create();
                    bmyAlert.show();
                }
            });
            jsonObjectRequest_RecRequestResponces.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            progressDialog.show();
            requestQueue_final.start();
            requestQueue_final.add(jsonObjectRequest_RecRequestResponces);

            //==== Fineash Request
            requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(Request<Object> request)
                {
                    boolean CheckResponce = false;
                    String mDataCheck="";
                    if (MainResponceRepocess != null)
                    {
                        if (MainResponceRepocess.equals("DONE"))
                        {
                            CheckResponce = MainResponceRepocess.equals("DONE");
                        }
                    }

                    if (CheckResponce == true)
                    {
                        Log.d("PostRepocessData", "FINISH");
                        progressDialog.dismiss();
                        sqlliteCreateRecovery_GetMaster.close();
                        requestQueue_final.getCache().clear();

                        SQLiteDatabase sqLiteDatabase_updateglg = sqlliteCreateRecovery_GetMaster.getWritableDatabase();
                        ContentValues contentValues_data = new ContentValues();
                        contentValues_data.put("Live_Server_Update" , "Y");
                        sqLiteDatabase_updateglg.update("recovery_repocess_order" ,contentValues_data , "req_user = ?" , new String[]{mReqMgreCode});
                    }
                }
            });
        }
    }



    //========================================================
    //        Post Manager Repsonces Code to Live Server
    //========================================================
    public void PostManagerResponces(String MgrCode)
    {
        mReqMgreCode = MgrCode;
        MainResponcerrRequestAction = "";
        DataAvialbleCheck="";

        JSONArray jsonArray_Mgerdata = new JSONArray();
        SQLiteDatabase sqLiteDatabase_PostManagerResponce = sqlliteCreateRecovery_GetMaster.getReadableDatabase();
        Cursor cursor_PostMmger = sqLiteDatabase_PostManagerResponce.rawQuery("SELECT Facility_No,Mgr_Respoce_Sts FROM recovery_request_mgr WHERE Assign_MgrCode = '" + mReqMgreCode + "' and Mgr_Respoce_Sts != ''" , null );

        if (cursor_PostMmger.getCount() != 0)
        {
            DataAvialbleCheck="YES";
            cursor_PostMmger.moveToFirst();
            do{
                JSONObject jsonObject_Post_Manager = new JSONObject();
                try {
                    jsonObject_Post_Manager.put("Facility_No" , cursor_PostMmger.getString(0));
                    jsonObject_Post_Manager.put("Mgr_Respoce_Sts" , cursor_PostMmger.getString(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArray_Mgerdata.put(jsonObject_Post_Manager);
            }while (cursor_PostMmger.moveToNext());
        }
        else
        {
            DataAvialbleCheck="NO";
        }

        //==== Create Final Json Array
        JSONObject jsonObject_PostFinal_Manager = new JSONObject();
        try {
            jsonObject_PostFinal_Manager.put("REC-MANAGER-MAIN-RESPONCES" , jsonArray_Mgerdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //==== Create Json Request
        //requestQueue_FacilityAction   = VollySingleton.getInstance(mContex).getRequestQueue();

        if (DataAvialbleCheck.equals("YES"))
        {
            String url = PHP_URL_SQL + "RECOVERY-POST-MGR-REQ-RESPONCES.php";
            JsonObjectRequest jsonObjectRequest_RecRequestResponces = new JsonObjectRequest(Request.Method.POST, url, jsonObject_PostFinal_Manager,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try {
                                MainResponcerrRequestAction = response.getString("RESULT");
                                Log.d("PostManagerResponces", "MainResponcerrRequestAction");
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
                            .setTitle("Error... - 3")
                            .create();
                    bmyAlert.show();
                }
            });
            jsonObjectRequest_RecRequestResponces.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            progressDialog.show();
            requestQueue_final.start();
            requestQueue_final.add(jsonObjectRequest_RecRequestResponces);

            //==== Fineash Request
            requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(Request<Object> request)
                {
                    boolean CheckResponce = false;
                    String mDataCheck="";
                    if (MainResponcerrRequestAction != null)
                    {
                        if (MainResponcerrRequestAction.equals("DONE"))
                        {
                            CheckResponce = MainResponcerrRequestAction.equals("DONE");
                        }
                    }

                    if (CheckResponce == true)
                    {
                        Log.d("PostManagerResponces", "FINISH");
                        progressDialog.dismiss();
                        sqlliteCreateRecovery_GetMaster.close();
                        requestQueue_final.getCache().clear();

                        SQLiteDatabase sqLiteDatabase_updateglg = sqlliteCreateRecovery_GetMaster.getWritableDatabase();
                        ContentValues contentValues_data = new ContentValues();
                        contentValues_data.put("Live_Server_update" , "Y");
                        sqLiteDatabase_updateglg.update("recovery_request_mgr" ,contentValues_data , "Assign_MgrCode = ?" , new String[]{mReqMgreCode});
                    }
                }
            });
        }
    }


    //========================================================
    //        Get The Facility Details
    //========================================================

    public void GetFacilityData(String Facno)
    {
        InputFacno = Facno;

        JSONObject jsonObjecSerchField = new JSONObject();
        try {
            jsonObjecSerchField.put("CLNAME" , "");
            jsonObjecSerchField.put("CLCODE" , "");
            jsonObjecSerchField.put("CLFACNO" , InputFacno);
            jsonObjecSerchField.put("CLVEHNO" , "");
            jsonObjecSerchField.put("CLNIC" , "");
            jsonObjecSerchField.put("CLTOWN" , "");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //=== Delete old Data
        SQLiteDatabase db = sqlliteCreateRecovery_GetMaster.getWritableDatabase();
        db.execSQL("DELETE FROM recovery_search_data WHERE Facility_Number = '" + InputFacno + "'");

        String url = PHP_URL_SQL + "RECOVERY-SEARCH-DATA.php";
        JsonObjectRequest jsonObjectRequest_RequestSearchData = new JsonObjectRequest(Request.Method.POST, url, jsonObjecSerchField,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //=== Get Data Responses

                        try {
                            Log.d("GetFacilityData", "RESPONCE-DONE");
                            JSONArray REC_GENERAL = response.getJSONArray("TT-SEARCH-DATA");
                            SQLiteDatabase sqLiteDatabase_RECOVERY_DATA = sqlliteCreateRecovery_GetMaster.getWritableDatabase();

                            //==== Update Recovery Genreal Details
                            for (int i = 0; i < REC_GENERAL.length(); i++) {
                                JSONObject jsonObject_REC_GENERAL = REC_GENERAL.getJSONObject(i);
                                ContentValues contentValues_RecoveryGenreal = new ContentValues();
                                contentValues_RecoveryGenreal.put("Client_Name", jsonObject_REC_GENERAL.getString("Client_Name"));
                                contentValues_RecoveryGenreal.put("Product_Type", jsonObject_REC_GENERAL.getString("Product_Type"));
                                contentValues_RecoveryGenreal.put("Asset_Model", jsonObject_REC_GENERAL.getString("Asset_Model"));
                                contentValues_RecoveryGenreal.put("Vehicle_Number", jsonObject_REC_GENERAL.getString("Vehicle_Number"));
                                contentValues_RecoveryGenreal.put("Due_Day", jsonObject_REC_GENERAL.getString("Due_Day"));
                                contentValues_RecoveryGenreal.put("Current_Month_Rental", jsonObject_REC_GENERAL.getString("Current_Month_Rental"));
                                contentValues_RecoveryGenreal.put("OD_Arrear_Amount", jsonObject_REC_GENERAL.getString("OD_Arrear_Amount"));
                                contentValues_RecoveryGenreal.put("Total_Arrear_Amount", jsonObject_REC_GENERAL.getString("Total_Arrear_Amount"));
                                contentValues_RecoveryGenreal.put("Last_Payment_Date", jsonObject_REC_GENERAL.getString("Last_Payment_Date"));
                                contentValues_RecoveryGenreal.put("Last_Payment_Amount", jsonObject_REC_GENERAL.getString("Last_Payment_Amount"));
                                contentValues_RecoveryGenreal.put("Facility_Payment_Status", jsonObject_REC_GENERAL.getString("Facility_Payment_Status"));
                                contentValues_RecoveryGenreal.put("Letter_Demand_Date", jsonObject_REC_GENERAL.getString("Letter_Demand_Date"));
                                contentValues_RecoveryGenreal.put("Client_Code", jsonObject_REC_GENERAL.getString("Client_Code"));
                                contentValues_RecoveryGenreal.put("NIC", jsonObject_REC_GENERAL.getString("NIC"));
                                contentValues_RecoveryGenreal.put("Customer_Full_Name", jsonObject_REC_GENERAL.getString("Customer_Full_Name"));
                                contentValues_RecoveryGenreal.put("Gender", jsonObject_REC_GENERAL.getString("Gender"));
                                contentValues_RecoveryGenreal.put("Occupation", jsonObject_REC_GENERAL.getString("Occupation"));
                                contentValues_RecoveryGenreal.put("Work_Place_Name", jsonObject_REC_GENERAL.getString("Work_Place_Name"));
                                contentValues_RecoveryGenreal.put("Work_Place_Address", jsonObject_REC_GENERAL.getString("Work_Place_Address"));
                                contentValues_RecoveryGenreal.put("Mobile_No1", jsonObject_REC_GENERAL.getString("Mobile_No1"));
                                contentValues_RecoveryGenreal.put("Mobile_No2", jsonObject_REC_GENERAL.getString("Mobile_No2"));
                                contentValues_RecoveryGenreal.put("Home_No", jsonObject_REC_GENERAL.getString("Home_No"));
                                contentValues_RecoveryGenreal.put("C_Address_Line_1", jsonObject_REC_GENERAL.getString("C_Address_Line_1"));
                                contentValues_RecoveryGenreal.put("C_Address_Line_2", jsonObject_REC_GENERAL.getString("C_Address_Line_2"));
                                contentValues_RecoveryGenreal.put("C_Address_Line_3", jsonObject_REC_GENERAL.getString("C_Address_Line_3"));
                                contentValues_RecoveryGenreal.put("C_Address_Line_4", jsonObject_REC_GENERAL.getString("C_Address_Line_4"));
                                contentValues_RecoveryGenreal.put("C_Postal_Town", jsonObject_REC_GENERAL.getString("C_Postal_Town"));
                                contentValues_RecoveryGenreal.put("P_Address_Line_1", jsonObject_REC_GENERAL.getString("P_Address_Line_1"));
                                contentValues_RecoveryGenreal.put("P_Address_Line_2", jsonObject_REC_GENERAL.getString("P_Address_Line_2"));
                                contentValues_RecoveryGenreal.put("P_Address_Line_3", jsonObject_REC_GENERAL.getString("P_Address_Line_3"));
                                contentValues_RecoveryGenreal.put("P_Address_Line_4", jsonObject_REC_GENERAL.getString("P_Address_Line_4"));
                                contentValues_RecoveryGenreal.put("P_Postal_Town", jsonObject_REC_GENERAL.getString("P_Postal_Town"));
                                contentValues_RecoveryGenreal.put("Facility_Number", jsonObject_REC_GENERAL.getString("Facility_Number"));
                                contentValues_RecoveryGenreal.put("Facility_Amount", jsonObject_REC_GENERAL.getString("Facility_Amount"));
                                contentValues_RecoveryGenreal.put("Facility_Branch", jsonObject_REC_GENERAL.getString("Facility_Branch"));
                                contentValues_RecoveryGenreal.put("Tenure", jsonObject_REC_GENERAL.getString("Tenure"));
                                contentValues_RecoveryGenreal.put("Recovery_Executive", jsonObject_REC_GENERAL.getString("Recovery_Executive"));
                                contentValues_RecoveryGenreal.put("Recovery_Executive_Name", jsonObject_REC_GENERAL.getString("Recovery_Executive_Name"));
                                contentValues_RecoveryGenreal.put("CC_Executive", jsonObject_REC_GENERAL.getString("CC_Executive"));
                                contentValues_RecoveryGenreal.put("Marketing_Executive", jsonObject_REC_GENERAL.getString("Marketing_Executive"));
                                contentValues_RecoveryGenreal.put("Follow_Up_Officer", jsonObject_REC_GENERAL.getString("Follow_Up_Officer"));
                                contentValues_RecoveryGenreal.put("Recovery_Executive_Phone", jsonObject_REC_GENERAL.getString("Recovery_Executive_Phone"));
                                contentValues_RecoveryGenreal.put("CC_Executive_Phone", jsonObject_REC_GENERAL.getString("CC_Executive_Phone"));
                                contentValues_RecoveryGenreal.put("Marketing_Executive_Phone", jsonObject_REC_GENERAL.getString("Marketing_Executive_Phone"));
                                contentValues_RecoveryGenreal.put("Follow_Up_Officer_Phone", jsonObject_REC_GENERAL.getString("Follow_Up_Officer_Phone"));
                                contentValues_RecoveryGenreal.put("Activation_Date", jsonObject_REC_GENERAL.getString("Activation_Date"));
                                contentValues_RecoveryGenreal.put("Expiry_Date", jsonObject_REC_GENERAL.getString("Expiry_Date"));
                                contentValues_RecoveryGenreal.put("Total_Facility_Amount", jsonObject_REC_GENERAL.getString("Total_Facility_Amount"));
                                contentValues_RecoveryGenreal.put("Disbursed_Amount", jsonObject_REC_GENERAL.getString("Disbursed_Amount"));
                                contentValues_RecoveryGenreal.put("Disbursement_Status", jsonObject_REC_GENERAL.getString("Disbursement_Status"));
                                contentValues_RecoveryGenreal.put("Interest_Rate", jsonObject_REC_GENERAL.getString("Interest_Rate"));
                                contentValues_RecoveryGenreal.put("Rental", jsonObject_REC_GENERAL.getString("Rental"));
                                contentValues_RecoveryGenreal.put("Contract_Status", jsonObject_REC_GENERAL.getString("Contract_Status"));
                                contentValues_RecoveryGenreal.put("Facility_Status", jsonObject_REC_GENERAL.getString("Facility_Status"));
                                contentValues_RecoveryGenreal.put("Down_Payment_No", jsonObject_REC_GENERAL.getString("Down_Payment_No"));
                                contentValues_RecoveryGenreal.put("Rentals_Matured_No", jsonObject_REC_GENERAL.getString("Rentals_Matured_No"));
                                contentValues_RecoveryGenreal.put("Rentals_Paid_No", jsonObject_REC_GENERAL.getString("Rentals_Paid_No"));
                                contentValues_RecoveryGenreal.put("Arrears_Rental_No", jsonObject_REC_GENERAL.getString("Arrears_Rental_No"));
                                contentValues_RecoveryGenreal.put("SPDC_Available", jsonObject_REC_GENERAL.getString("SPDC_Available"));
                                contentValues_RecoveryGenreal.put("Total_Outstanding", jsonObject_REC_GENERAL.getString("Total_Outstanding"));
                                contentValues_RecoveryGenreal.put("Capital_Outstanding", jsonObject_REC_GENERAL.getString("Capital_Outstanding"));
                                contentValues_RecoveryGenreal.put("Interest_Outstanding", jsonObject_REC_GENERAL.getString("Interest_Outstanding"));
                                contentValues_RecoveryGenreal.put("Arrears_Rental_Amount", jsonObject_REC_GENERAL.getString("Arrears_Rental_Amount"));
                                contentValues_RecoveryGenreal.put("OD_Interest", jsonObject_REC_GENERAL.getString("OD_Interest"));
                                contentValues_RecoveryGenreal.put("Interest_Arrears", jsonObject_REC_GENERAL.getString("Interest_Arrears"));
                                contentValues_RecoveryGenreal.put("Insurance_Arrears", jsonObject_REC_GENERAL.getString("Insurance_Arrears"));
                                contentValues_RecoveryGenreal.put("OD_Interest_OS", jsonObject_REC_GENERAL.getString("OD_Interest_OS"));
                                contentValues_RecoveryGenreal.put("Seizing_Charges", jsonObject_REC_GENERAL.getString("Seizing_Charges"));
                                contentValues_RecoveryGenreal.put("OC_Arrears", jsonObject_REC_GENERAL.getString("OC_Arrears"));
                                contentValues_RecoveryGenreal.put("FUTURE_CAPITAL", jsonObject_REC_GENERAL.getString("FUTURE_CAPITAL"));
                                contentValues_RecoveryGenreal.put("FUTURE_INTEREST", jsonObject_REC_GENERAL.getString("FUTURE_INTEREST"));
                                contentValues_RecoveryGenreal.put("TOTAL_SETTL_AMT", jsonObject_REC_GENERAL.getString("TOTAL_SETTL_AMT"));
                                sqLiteDatabase_RECOVERY_DATA.insert("recovery_search_data", null, contentValues_RecoveryGenreal);
                            }
                            sqLiteDatabase_RECOVERY_DATA.close();
                            MainResponceCode = "DONE";


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                MainResponceCode = "ERROR";
                String ErrorCode = "";
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

                String ErrorDescription = "Responses Failure.(" + ErrorCode.toString() + ")";
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex);
                bmyAlert.setMessage(ErrorDescription).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("Error... - 4")
                        .create();
                bmyAlert.show();
            }
        });
        jsonObjectRequest_RequestSearchData.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        //==== Sett the Responces
        progressDialog.show();
        requestQueue_final.start();
        requestQueue_final.add(jsonObjectRequest_RequestSearchData);

        //==== Fineash Request
        requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce = false;
                String mDataCheck="";
                if (MainResponceCode != null)
                {
                    if (MainResponceCode.equals("DONE"))
                    {
                        CheckResponce = MainResponceCode.equals("DONE");
                    }
                }

                if (CheckResponce == true)
                {
                    Log.d("GetFacilityData", "FINES");
                    progressDialog.dismiss();
                    sqlliteCreateRecovery_GetMaster.close();
                    requestQueue_final.getCache().clear();
                }
            }
        });
    }


    //========================================================
    //        Get The Manager Request Facility Details
    //========================================================

    public void GetRequestManagerAction(String MgrCode)
    {
        ManagerCode = MgrCode;
        MainResponceManagerReq="";

        JSONObject jsonObject_Post_Manager = new JSONObject();
        try {
            jsonObject_Post_Manager.put("REQ_MGR_CODE" , ManagerCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //==== Create Json Request
        String url = PHP_URL_SQL + "RECOVERY-GET-REQUEST-MGR.php";
        JsonObjectRequest jsonObjectRequest_RecManagerAssign = new JsonObjectRequest(Request.Method.POST, url, jsonObject_Post_Manager,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        MainResponceManagerReq="DONE";
                        try {

                            JSONArray jsonArray_reqmanager= response.getJSONArray("TT-GET-REQ-MGR-FAC");
                            String mFacno="" , mAssignMgr="" , mAssDate="" , mAssReason="" , mReq_id="" , Req_userName="" , Req_comment="";
                            for (int i=0 ; i< jsonArray_reqmanager.length();i++)
                            {
                                JSONObject mydataJson = jsonArray_reqmanager.getJSONObject(i);

                                mFacno          =   mydataJson.getString("Facility_no") ;
                                mAssignMgr      =   mydataJson.getString("Assign_MgrCode") ;
                                mAssDate        =   mydataJson.getString("Assign_Date") ;
                                mAssReason      =   mydataJson.getString("Assign_Reason") ;
                                mReq_id         =   mydataJson.getString("Req_User_Id") ;
                                Req_userName    =   mydataJson.getString("Req_User_Name") ;
                                Req_comment     =   mydataJson.getString("Req_Comment") ;


                                //=== Get Facility Details ===
                                GetFacilityData(mFacno);

                                sqlliteCreateRecovery_GetMaster.InsertRequestMgrData(mFacno,mAssignMgr,mAssDate,mAssReason,mReq_id,Req_userName,Req_comment);
                            }

                            Log.d("GetRequestManagerAction", "RESPONCES-DONE");
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
                        .setTitle("Error... - 5")
                        .create();
                bmyAlert.show();
            }
        });
        jsonObjectRequest_RecManagerAssign.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog.show();
        requestQueue_final.start();
        requestQueue_final.add(jsonObjectRequest_RecManagerAssign);

        //==== Fineash Request
        requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {

                AlertDialog.Builder builders = new AlertDialog.Builder(mContex);
                builders.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                builders.setMessage(MainResponceManagerReq);
                builders.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });
                builders.create();
                builders.show();




                boolean CheckResponce=false;
                if (MainResponceManagerReq != null)
                {
                    CheckResponce = MainResponceManagerReq.equals("DONE");
                }

                if (CheckResponce == true)
                {
                    Log.d("GetRequestManagerAction", "FINESH");
                    progressDialog.dismiss();
                    //=== Update Recovery Tranfer flag
                    sqlliteCreateRecovery_GetMaster.close();
                    requestQueue_final.getCache().clear();
                    //===========================
                }
                else
                {
                    progressDialog.dismiss();
                    requestQueue_final.stop();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContex);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Response fail. Please try again. - 8");
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

    //===============================================================
    //                Tranfer the Manager Assign Case
    //===============================================================

    public void PostManagerAction(String mInputFacno)
    {
        UpdateFacno = mInputFacno;
        ManagerAssignResponces="";
        JSONArray jsonArray_Mgerdata = new JSONArray();

        SQLiteDatabase sqLiteDatabase_PostManage = sqlliteCreateRecovery_GetMaster.getReadableDatabase();
        Cursor cursor_ManagerData = sqLiteDatabase_PostManage.rawQuery("SELECT * FROM manager_assign_data WHERE Facility_no = '" + UpdateFacno + "'" , null );
        if (cursor_ManagerData.getCount() != 0)
        {
            cursor_ManagerData.moveToFirst();
            JSONObject jsonObject_Post_Manager = new JSONObject();
            try {
                jsonObject_Post_Manager.put("Facility_no" , cursor_ManagerData.getString(0));
                jsonObject_Post_Manager.put("Clode" , cursor_ManagerData.getString(1));
                jsonObject_Post_Manager.put("Assign_MgrCode" , cursor_ManagerData.getString(2));
                jsonObject_Post_Manager.put("Assign_MgrName" , cursor_ManagerData.getString(3));
                jsonObject_Post_Manager.put("Assign_Date" , cursor_ManagerData.getString(4));
                jsonObject_Post_Manager.put("Assign_Reason" , cursor_ManagerData.getString(5));
                jsonObject_Post_Manager.put("Req_User_Id" , cursor_ManagerData.getString(6));
                jsonObject_Post_Manager.put("Req_User_Name" , cursor_ManagerData.getString(7));
                jsonObject_Post_Manager.put("Req_Comment" , cursor_ManagerData.getString(8));
                jsonObject_Post_Manager.put("Mgr_Responces" , cursor_ManagerData.getString(10));
                jsonObject_Post_Manager.put("Mgr_Responces_Date" , cursor_ManagerData.getString(11));
                jsonObject_Post_Manager.put("Mgr_Attend" , cursor_ManagerData.getString(12));
                jsonObject_Post_Manager.put("Mgr_Attend_Date" , cursor_ManagerData.getString(13));
                jsonObject_Post_Manager.put("Mgr_Action_Code" , cursor_ManagerData.getString(14));
                jsonObject_Post_Manager.put("Req_Done_Sts" , cursor_ManagerData.getString(15));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray_Mgerdata.put(jsonObject_Post_Manager);
        }

        //==== Create Final Json Array
        JSONObject jsonObject_PostFinal_Manager = new JSONObject();
        try {
            jsonObject_PostFinal_Manager.put("REC-MANAGER-ASSIGN" , jsonArray_Mgerdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //==== Create Json Request
        String url = PHP_URL_SQL + "RECOVERY-POST-MGR-ASSIGN.php";
        JsonObjectRequest jsonObjectRequest_RecManagerAssign = new JsonObjectRequest(Request.Method.POST, url, jsonObject_PostFinal_Manager,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            ManagerAssignResponces = response.getString("RESULT");
                            Log.d("PostManagerAction", ManagerAssignResponces);
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
                        .setTitle("Error... - 9")
                        .create();
                bmyAlert.show();
            }
        });
        jsonObjectRequest_RecManagerAssign.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog.show();
        requestQueue_final.start();
        requestQueue_final.add(jsonObjectRequest_RecManagerAssign);


        //==== Fineash Request
        requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce=false;
                if (ManagerAssignResponces != null)
                {
                    CheckResponce = ManagerAssignResponces.equals("DONE");
                }

                if (CheckResponce == true)
                {
                    Log.d("PostManagerAction", "FINISH");
                    progressDialog.dismiss();

                    //=== Update Recovery Tranfer flag
                    SQLiteDatabase sqLiteDatabase_updateglg = sqlliteCreateRecovery_GetMaster.getWritableDatabase();
                    ContentValues contentValues_data = new ContentValues();
                    contentValues_data.put("LIVE_SERVER_UPDATE" , "Y");
                    sqLiteDatabase_updateglg.update("manager_assign_data" ,contentValues_data , "FACILITY_NO = ?" , new String[]{UpdateFacno});
                    sqlliteCreateRecovery_GetMaster.close();
                    requestQueue_final.getCache().clear();
                    //===========================

                }
                else
                {
                    progressDialog.dismiss();
                    requestQueue_final.stop();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContex);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Response fail. Please try again. - 10");
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

    //===============================================================
    //                Post Assign Action Code
    //===============================================================
    public void PostFacilityRecoveryActionData(String mInputFacno)
    {

        UpdateFacno = mInputFacno;

        FacilityResponceCodeRecovery="";
        JSONArray jsonArray_PostRecoveryData    = new JSONArray();
        SQLiteDatabase sqLiteDatabase_PostRcData = sqlliteCreateRecovery_GetMaster.getReadableDatabase();
        Cursor cursor_PostRcData = sqLiteDatabase_PostRcData.rawQuery("SELECT * FROM nemf_form_updater where FACILITY_NO =  '" + mInputFacno + "' AND LIVE_SERVER_UPDATE = ''"  , null);

        if (cursor_PostRcData.getCount() != 0)
        {
            cursor_PostRcData.moveToFirst();
            do{
                JSONObject jsonObject_Post_RecData = new JSONObject();
                try {

                    if (cursor_PostRcData.getString(0) != null)
                        jsonObject_Post_RecData.put("ID" , cursor_PostRcData.getString(0));
                    else
                        jsonObject_Post_RecData.put("ID" , "");

                    if (cursor_PostRcData.getString(1) != null)
                        jsonObject_Post_RecData.put("FACILITY_NO" , cursor_PostRcData.getString(1));
                    else
                        jsonObject_Post_RecData.put("FACILITY_NO" , "");

                    if (cursor_PostRcData.getString(2) != null)
                        jsonObject_Post_RecData.put("ACTIONCODE" , cursor_PostRcData.getString(2));
                    else
                        jsonObject_Post_RecData.put("ACTIONCODE" , "");

                    if (cursor_PostRcData.getString(3) != null)
                        jsonObject_Post_RecData.put("ACTION_DATE" , cursor_PostRcData.getString(3));
                    else
                        jsonObject_Post_RecData.put("ACTION_DATE" , cursor_PostRcData.getString(3));


                    if (cursor_PostRcData.getString(4) != null)
                        jsonObject_Post_RecData.put("MADE_BY" , cursor_PostRcData.getString(4));
                    else
                    {
                        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
                        LoginUser   =   globleClassDetails.getUserid();
                        jsonObject_Post_RecData.put("MADE_BY" , LoginUser);
                    }

                    if (cursor_PostRcData.getString(5) != null)
                        jsonObject_Post_RecData.put("CURRENT_BUCKET" , cursor_PostRcData.getString(5));
                    else
                        jsonObject_Post_RecData.put("CURRENT_BUCKET" , "");

                    if (cursor_PostRcData.getString(6) != null)
                        jsonObject_Post_RecData.put("LINKED_ID" , cursor_PostRcData.getString(6));
                    else
                        jsonObject_Post_RecData.put("LINKED_ID" , "");

                    if (cursor_PostRcData.getString(7) != null)
                        jsonObject_Post_RecData.put("CNAME" , cursor_PostRcData.getString(7));
                    else
                        jsonObject_Post_RecData.put("CNAME" , "");

                    if (cursor_PostRcData.getString(8) != null)
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_TYPE" , cursor_PostRcData.getString(8));
                    else
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_TYPE" , "");

                    if (cursor_PostRcData.getString(9) != null)
                        jsonObject_Post_RecData.put("MODE_OF_PAYMENT" , cursor_PostRcData.getString(9));
                    else
                        jsonObject_Post_RecData.put("MODE_OF_PAYMENT" , "");

                    if (cursor_PostRcData.getString(10) != null)
                        jsonObject_Post_RecData.put("COLLECTION_AMOUNT" , cursor_PostRcData.getString(10));
                    else
                        jsonObject_Post_RecData.put("COLLECTION_AMOUNT" , "");

                    if (cursor_PostRcData.getString(11) != null)
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_DATE" , cursor_PostRcData.getString(11));
                    else
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_DATE" , "");

                    if (cursor_PostRcData.getString(12) != null)
                        jsonObject_Post_RecData.put("MANUAL_RECEIPT_NO" , cursor_PostRcData.getString(12));
                    else
                        jsonObject_Post_RecData.put("MANUAL_RECEIPT_NO" , "");

                    if (cursor_PostRcData.getString(13) != null)
                        jsonObject_Post_RecData.put("PROVISIONAL_RECEIPT_NO" , cursor_PostRcData.getString(13));
                    else
                        jsonObject_Post_RecData.put("PROVISIONAL_RECEIPT_NO" , "");

                    if (cursor_PostRcData.getString(14) != null)
                        jsonObject_Post_RecData.put("CHEQUE_NO" , cursor_PostRcData.getString(14));
                    else
                        jsonObject_Post_RecData.put("CHEQUE_NO" , "");

                    if (cursor_PostRcData.getString(15) != null)
                        jsonObject_Post_RecData.put("CHEQUE_BANK" , cursor_PostRcData.getString(15));
                    else
                        jsonObject_Post_RecData.put("CHEQUE_BANK" , "");

                    if (cursor_PostRcData.getString(16) != null)
                        jsonObject_Post_RecData.put("BRANCH" , cursor_PostRcData.getString(16));
                    else
                        jsonObject_Post_RecData.put("BRANCH" , "");

                    if (cursor_PostRcData.getString(17) != null)
                        jsonObject_Post_RecData.put("CHEQUE_DATE" , cursor_PostRcData.getString(17));
                    else
                        jsonObject_Post_RecData.put("CHEQUE_DATE" , "");

                    if (cursor_PostRcData.getString(18) != null)
                        jsonObject_Post_RecData.put("REFERENCE_NO" , cursor_PostRcData.getString(18));
                    else
                        jsonObject_Post_RecData.put("REFERENCE_NO" , "");

                    if (cursor_PostRcData.getString(19) != null)
                        jsonObject_Post_RecData.put("PAYMENT_DATE" , cursor_PostRcData.getString(19));
                    else
                        jsonObject_Post_RecData.put("PAYMENT_DATE" , "");

                    if (cursor_PostRcData.getString(20) != null)
                        jsonObject_Post_RecData.put("OPERATOR" , cursor_PostRcData.getString(20));
                    else
                        jsonObject_Post_RecData.put("OPERATOR" , "");

                    if (cursor_PostRcData.getString(21) != null)
                        jsonObject_Post_RecData.put("PAYMENT_RECEIVED_FROM_THIRD_PARTY" , cursor_PostRcData.getString(21));
                    else
                        jsonObject_Post_RecData.put("PAYMENT_RECEIVED_FROM_THIRD_PARTY" , "");

                    if (cursor_PostRcData.getString(22) != null)
                        jsonObject_Post_RecData.put("NAME" , cursor_PostRcData.getString(22));
                    else
                        jsonObject_Post_RecData.put("NAME" , "");

                    if (cursor_PostRcData.getString(23) != null)
                        jsonObject_Post_RecData.put("RELATIONSHIP" , cursor_PostRcData.getString(23));
                    else
                        jsonObject_Post_RecData.put("RELATIONSHIP" , "");

                    if (cursor_PostRcData.getString(24) != null)
                        jsonObject_Post_RecData.put("PHONE_NUMBER" , cursor_PostRcData.getString(24));
                    else
                        jsonObject_Post_RecData.put("PHONE_NUMBER" , "");

                    if (cursor_PostRcData.getString(25) != null)
                        jsonObject_Post_RecData.put("ADDRESS" , cursor_PostRcData.getString(25));
                    else
                        jsonObject_Post_RecData.put("ADDRESS" , "");

                    if (cursor_PostRcData.getString(26) != null)
                        jsonObject_Post_RecData.put("PROMISE_TO_PAY_DATE" , cursor_PostRcData.getString(26));
                    else
                        jsonObject_Post_RecData.put("PROMISE_TO_PAY_DATE" , "");

                    if (cursor_PostRcData.getString(27) != null)
                        jsonObject_Post_RecData.put("PTP_AMOUNT" , cursor_PostRcData.getString(27));
                    else
                        jsonObject_Post_RecData.put("PTP_AMOUNT" , cursor_PostRcData.getString(27));

                    if (cursor_PostRcData.getString(28) != null)
                        jsonObject_Post_RecData.put("PTP_CHANNEL_MODE" , cursor_PostRcData.getString(28));
                    else
                        jsonObject_Post_RecData.put("PTP_CHANNEL_MODE" , cursor_PostRcData.getString(28));

                    if (cursor_PostRcData.getString(29) != null)
                        jsonObject_Post_RecData.put("PTP_BRANCH" , cursor_PostRcData.getString(29));
                    else
                        jsonObject_Post_RecData.put("PTP_BRANCH" , "");

                    if (cursor_PostRcData.getString(30) != null)
                        jsonObject_Post_RecData.put("PROMISE_MADE_BY" , cursor_PostRcData.getString(30));
                    else
                        jsonObject_Post_RecData.put("PROMISE_MADE_BY" , "");

                    if (cursor_PostRcData.getString(31) != null)
                        jsonObject_Post_RecData.put("PICK_UP_DATE_AND_TIME" , cursor_PostRcData.getString(31));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_DATE_AND_TIME" , "");

                    if (cursor_PostRcData.getString(32) != null)
                        jsonObject_Post_RecData.put("ADDRESS_FOR_PICK_UP" , cursor_PostRcData.getString(32));
                    else
                        jsonObject_Post_RecData.put("ADDRESS_FOR_PICK_UP" , "");

                    if (cursor_PostRcData.getString(33) != null)
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_BRANCH" , cursor_PostRcData.getString(33));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_BRANCH" , "");

                    if (cursor_PostRcData.getString(34) != null)
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_COLLECTION_EXECUTIVE" , cursor_PostRcData.getString(34));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_COLLECTION_EXECUTIVE" , "");

                    if (cursor_PostRcData.getString(35) != null)
                        jsonObject_Post_RecData.put("PICK_UP_AMOUNT" , cursor_PostRcData.getString(35));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_AMOUNT" , "");

                    if (cursor_PostRcData.getString(36) != null)
                        jsonObject_Post_RecData.put("ENTRY_NO" , cursor_PostRcData.getString(36));
                    else
                        jsonObject_Post_RecData.put("ENTRY_NO" , "");

                    if (cursor_PostRcData.getString(37) != null)
                        jsonObject_Post_RecData.put("COMPLAINT_DATE" , cursor_PostRcData.getString(37));
                    else
                        jsonObject_Post_RecData.put("COMPLAINT_DATE" , "");

                    if (cursor_PostRcData.getString(38) != null)
                        jsonObject_Post_RecData.put("POLICE_STATION" , cursor_PostRcData.getString(38));
                    else
                        jsonObject_Post_RecData.put("POLICE_STATION" , "");

                    if (cursor_PostRcData.getString(39) != null)
                        jsonObject_Post_RecData.put("INQUIRY_DATE" , cursor_PostRcData.getString(39));
                    else
                        jsonObject_Post_RecData.put("INQUIRY_DATE" , "");

                    if (cursor_PostRcData.getString(40) != null)
                        jsonObject_Post_RecData.put("VEHICLE_MAKE" , cursor_PostRcData.getString(40));
                    else
                        jsonObject_Post_RecData.put("VEHICLE_MAKE" , "");

                    if (cursor_PostRcData.getString(41) != null)
                        jsonObject_Post_RecData.put("MODEL" , cursor_PostRcData.getString(41));
                    else
                        jsonObject_Post_RecData.put("MODEL" , "");

                    if (cursor_PostRcData.getString(42) != null)
                        jsonObject_Post_RecData.put("VARIANT" , cursor_PostRcData.getString(42));
                    else
                        jsonObject_Post_RecData.put("VARIANT" , "");

                    if (cursor_PostRcData.getString(43) != null)
                        jsonObject_Post_RecData.put("VEHICLE_NO" , cursor_PostRcData.getString(43));
                    else
                        jsonObject_Post_RecData.put("VEHICLE_NO" , "");

                    if (cursor_PostRcData.getString(44) != null)
                        jsonObject_Post_RecData.put("ASSET_DATE" , cursor_PostRcData.getString(44));
                    else
                        jsonObject_Post_RecData.put("ASSET_DATE" , "");

                    if (cursor_PostRcData.getString(45) != null)
                        jsonObject_Post_RecData.put("DRIVEN_BY" , cursor_PostRcData.getString(45));
                    else
                        jsonObject_Post_RecData.put("DRIVEN_BY" , "");

                    if (cursor_PostRcData.getString(46) != null)
                        jsonObject_Post_RecData.put("VEHICLE_CONDITION" , cursor_PostRcData.getString(46));
                    else
                        jsonObject_Post_RecData.put("VEHICLE_CONDITION" , "");

                    if (cursor_PostRcData.getString(47) != null)
                        jsonObject_Post_RecData.put("RENTAL_PAYING_BY" , cursor_PostRcData.getString(47));
                    else
                        jsonObject_Post_RecData.put("RENTAL_PAYING_BY" , "");

                    if (cursor_PostRcData.getString(48) != null)
                        jsonObject_Post_RecData.put("OWNERSHIP" , cursor_PostRcData.getString(48));
                    else
                        jsonObject_Post_RecData.put("OWNERSHIP" , "");

                    if (cursor_PostRcData.getString(49) != null)
                        jsonObject_Post_RecData.put("RTP_REASON" , cursor_PostRcData.getString(49));
                    else
                        jsonObject_Post_RecData.put("RTP_REASON" , "");

                    if (cursor_PostRcData.getString(50) != null)
                        jsonObject_Post_RecData.put("PAID_AMOUNT" , cursor_PostRcData.getString(50));
                    else
                        jsonObject_Post_RecData.put("PAID_AMOUNT" , "");

                    if (cursor_PostRcData.getString(51) != null)
                        jsonObject_Post_RecData.put("PAID_CHANNEL_MODE" , cursor_PostRcData.getString(51));
                    else
                        jsonObject_Post_RecData.put("PAID_CHANNEL_MODE" , "");

                    if (cursor_PostRcData.getString(52) != null)
                        jsonObject_Post_RecData.put("PAID_RECEIPT_NO" , cursor_PostRcData.getString(52));
                    else
                        jsonObject_Post_RecData.put("PAID_RECEIPT_NO" , "");

                    if (cursor_PostRcData.getString(53) != null)
                        jsonObject_Post_RecData.put("PAID_COLLECTION_EXECUTIVE_NAME" , cursor_PostRcData.getString(53));
                    else
                        jsonObject_Post_RecData.put("PAID_COLLECTION_EXECUTIVE_NAME" , "");

                    if (cursor_PostRcData.getString(54) != null)
                        jsonObject_Post_RecData.put("FOLLOW_UP_DATE" , cursor_PostRcData.getString(54));
                    else
                        jsonObject_Post_RecData.put("FOLLOW_UP_DATE" , "");

                    if (cursor_PostRcData.getString(55) != null)
                        jsonObject_Post_RecData.put("MEETING_DATE" , cursor_PostRcData.getString(55));
                    else
                        jsonObject_Post_RecData.put("MEETING_DATE" , "");

                    if (cursor_PostRcData.getString(56) != null)
                        jsonObject_Post_RecData.put("COMMENTS" , cursor_PostRcData.getString(56));
                    else
                        jsonObject_Post_RecData.put("COMMENTS" , cursor_PostRcData.getString(57));

                    if (cursor_PostRcData.getString(57) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_ADDRESS" , cursor_PostRcData.getString(57));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_ADDRESS" , "");

                    if (cursor_PostRcData.getString(58) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_LAT" , cursor_PostRcData.getString(58));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_LAT" , "");

                    if (cursor_PostRcData.getString(59) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_LONG" , cursor_PostRcData.getString(59));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_LONG" , "");

                    if (cursor_PostRcData.getString(60) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_PINCODE" , cursor_PostRcData.getString(60));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_PINCODE" , "");

                    if (cursor_PostRcData.getString(61) != null)
                        jsonObject_Post_RecData.put("CORE_STATUS" , cursor_PostRcData.getString(61));
                    else
                        jsonObject_Post_RecData.put("CORE_STATUS" , "");

                    if (cursor_PostRcData.getString(62) != null)
                        jsonObject_Post_RecData.put("PAYMENT_TYPE" , cursor_PostRcData.getString(62));
                    else
                        jsonObject_Post_RecData.put("PAYMENT_TYPE" , "");

                    if (cursor_PostRcData.getString(63) != null)
                        jsonObject_Post_RecData.put("SERVICE_PROVIDER" , cursor_PostRcData.getString(63));
                    else
                        jsonObject_Post_RecData.put("SERVICE_PROVIDER" , "");

                    if (cursor_PostRcData.getString(64) != null)
                        jsonObject_Post_RecData.put("CONTACTNO_TYPE" , cursor_PostRcData.getString(64));
                    else
                        jsonObject_Post_RecData.put("CONTACTNO_TYPE" , "");

                    if (cursor_PostRcData.getString(65) != null)
                        jsonObject_Post_RecData.put("ADDLINE1" , cursor_PostRcData.getString(65));
                    else
                        jsonObject_Post_RecData.put("ADDLINE1" , "");

                    if (cursor_PostRcData.getString(66) != null)
                        jsonObject_Post_RecData.put("ADDLINE2" , cursor_PostRcData.getString(66));
                    else
                        jsonObject_Post_RecData.put("ADDLINE2" , "");

                    if (cursor_PostRcData.getString(67) != null)
                        jsonObject_Post_RecData.put("ADDLINE3" , cursor_PostRcData.getString(67));
                    else
                        jsonObject_Post_RecData.put("ADDLINE3" , "");

                    if (cursor_PostRcData.getString(68) != null)
                        jsonObject_Post_RecData.put("ADDLINE4" , cursor_PostRcData.getString(68));
                    else
                        jsonObject_Post_RecData.put("ADDLINE4" , "");

                    if (cursor_PostRcData.getString(69) != null)
                        jsonObject_Post_RecData.put("POSTAL_TOWN" , cursor_PostRcData.getString(69));
                    else
                        jsonObject_Post_RecData.put("POSTAL_TOWN" , "");

                    if (cursor_PostRcData.getString(70) != null)
                        jsonObject_Post_RecData.put("POSTAL_CODE" , cursor_PostRcData.getString(70));
                    else
                        jsonObject_Post_RecData.put("POSTAL_CODE" , "");

                    if (cursor_PostRcData.getString(71) != null)
                        jsonObject_Post_RecData.put("MEETING_TIME" , cursor_PostRcData.getString(71));
                    else
                        jsonObject_Post_RecData.put("MEETING_TIME" , "");

                    if (cursor_PostRcData.getString(72) != null)
                        jsonObject_Post_RecData.put("VENUE" , cursor_PostRcData.getString(72));
                    else
                        jsonObject_Post_RecData.put("VENUE" , "");

                    if (cursor_PostRcData.getString(73) != null)
                        jsonObject_Post_RecData.put("HANDOVER_BY" , cursor_PostRcData.getString(73));
                    else
                        jsonObject_Post_RecData.put("HANDOVER_BY" , "");

                    if (cursor_PostRcData.getString(74) != null)
                        jsonObject_Post_RecData.put("HANDOVER_NAME" , cursor_PostRcData.getString(74));
                    else
                        jsonObject_Post_RecData.put("HANDOVER_NAME" , "");

                    if (cursor_PostRcData.getString(75) != null)
                        jsonObject_Post_RecData.put("HANDOVER_NIC" , cursor_PostRcData.getString(75));
                    else
                        jsonObject_Post_RecData.put("HANDOVER_NIC" , "");

                    if (cursor_PostRcData.getString(76) != null)
                        jsonObject_Post_RecData.put("INFORMED_TO" , cursor_PostRcData.getString(76));
                    else
                        jsonObject_Post_RecData.put("INFORMED_TO" , "");

                    if (cursor_PostRcData.getString(77) != null)
                        jsonObject_Post_RecData.put("FOLLOW_UP_TIME" , cursor_PostRcData.getString(77));
                    else
                        jsonObject_Post_RecData.put("FOLLOW_UP_TIME" , "");

                    if (cursor_PostRcData.getString(78) != null)
                        jsonObject_Post_RecData.put("PICKUP_TIME" , cursor_PostRcData.getString(78));
                    else
                        jsonObject_Post_RecData.put("PICKUP_TIME" , "");

                    if (cursor_PostRcData.getString(79) != null)
                        jsonObject_Post_RecData.put("UPDATE_TIME" , cursor_PostRcData.getString(79));
                    else
                        jsonObject_Post_RecData.put("UPDATE_TIME" , "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray_PostRecoveryData.put(jsonObject_Post_RecData);
            }while (cursor_PostRcData.moveToNext());
        }


        //==== Create Final Json Array
        JSONObject jsonObject_PostFinal = new JSONObject();
        try {
            jsonObject_PostFinal.put("REC-ACTION" , jsonArray_PostRecoveryData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = PHP_URL_SQL + "RECOVERY-POST-ACTION.php";
        JsonObjectRequest jsonObjectRequest_RecActionFacilty = new JsonObjectRequest(Request.Method.POST, url, jsonObject_PostFinal,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            FacilityResponceCodeRecovery = response.getString("RESULT");
                            Log.d("PostFacilityRecovery", FacilityResponceCodeRecovery);
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
                MainResponceCodeRecovery="";
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
        jsonObjectRequest_RecActionFacilty.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog.setTitle("Action fetching Process - Please Wait.");
        progressDialog.show();
        requestQueue_final.start();
        requestQueue_final.add(jsonObjectRequest_RecActionFacilty);

        //==== Fineash Request
        requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce=false;
                if (FacilityResponceCodeRecovery != null)
                {
                    CheckResponce = FacilityResponceCodeRecovery.equals("DONE");
                }

                if (CheckResponce == true)
                {
                    Log.d("PostFacilityRecovery", "FINISH");
                    progressDialog.dismiss();
                    //=== Update Recovery Tranfer flag
                    SQLiteDatabase sqLiteDatabase_updateglg = sqlliteCreateRecovery_GetMaster.getWritableDatabase();
                    ContentValues contentValues_data = new ContentValues();
                    contentValues_data.put("LIVE_SERVER_UPDATE" , "Y");
                    sqLiteDatabase_updateglg.update("nemf_form_updater" ,contentValues_data , "FACILITY_NO = ?" , new String[]{String.valueOf(UpdateFacno)});
                    sqlliteCreateRecovery_GetMaster.close();
                    requestQueue_final.getCache().clear();

                    if(!((Activity) mContex).isFinishing())
                    {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContex);
                        builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                        builder.setMessage("File submit Successfully.");
                        builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                    //===========================
                }
                else
                {
                    progressDialog.dismiss();
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(mContex);
                    builder.setTitle(Html.fromHtml("<font color='#FF0000'>Connection Failure ......</font>"));
                    builder.setMessage("Data connection Failure... Please Re-Login the Mobile App.");
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

    //===============================================================
    //                Post All Action Code
    //===============================================================
    public void PostRecoveryActionData()
    {
        MainResponceCodeRecovery="";
        JSONArray jsonArray_PostRecoveryData    = new JSONArray();
        SQLiteDatabase sqLiteDatabase_PostRcData = sqlliteCreateRecovery_GetMaster.getReadableDatabase();
        Cursor cursor_PostRcData = sqLiteDatabase_PostRcData.rawQuery("SELECT * FROM nemf_form_updater where LIVE_SERVER_UPDATE = '' " , null);

        if (cursor_PostRcData.getCount() != 0)
        {
            cursor_PostRcData.moveToFirst();
            do{
                JSONObject jsonObject_Post_RecData = new JSONObject();
                try {

                    if (cursor_PostRcData.getString(0) != null)
                        jsonObject_Post_RecData.put("ID" , cursor_PostRcData.getString(0));
                    else
                        jsonObject_Post_RecData.put("ID" , "");

                    if (cursor_PostRcData.getString(1) != null)
                        jsonObject_Post_RecData.put("FACILITY_NO" , cursor_PostRcData.getString(1));
                    else
                        jsonObject_Post_RecData.put("FACILITY_NO" , "");

                    if (cursor_PostRcData.getString(2) != null)
                        jsonObject_Post_RecData.put("ACTIONCODE" , cursor_PostRcData.getString(2));
                    else
                        jsonObject_Post_RecData.put("ACTIONCODE" , "");

                    if (cursor_PostRcData.getString(3) != null)
                        jsonObject_Post_RecData.put("ACTION_DATE" , cursor_PostRcData.getString(3));
                    else
                        jsonObject_Post_RecData.put("ACTION_DATE" , cursor_PostRcData.getString(3));


                    if (cursor_PostRcData.getString(4) != null)
                        jsonObject_Post_RecData.put("MADE_BY" , cursor_PostRcData.getString(4));
                    else
                        jsonObject_Post_RecData.put("MADE_BY" , "");

                    if (cursor_PostRcData.getString(5) != null)
                        jsonObject_Post_RecData.put("CURRENT_BUCKET" , cursor_PostRcData.getString(5));
                    else
                        jsonObject_Post_RecData.put("CURRENT_BUCKET" , "");

                    if (cursor_PostRcData.getString(6) != null)
                        jsonObject_Post_RecData.put("LINKED_ID" , cursor_PostRcData.getString(6));
                    else
                        jsonObject_Post_RecData.put("LINKED_ID" , "");

                    if (cursor_PostRcData.getString(7) != null)
                        jsonObject_Post_RecData.put("CNAME" , cursor_PostRcData.getString(7));
                    else
                        jsonObject_Post_RecData.put("CNAME" , "");

                    if (cursor_PostRcData.getString(8) != null)
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_TYPE" , cursor_PostRcData.getString(8));
                    else
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_TYPE" , "");

                    if (cursor_PostRcData.getString(9) != null)
                        jsonObject_Post_RecData.put("MODE_OF_PAYMENT" , cursor_PostRcData.getString(9));
                    else
                        jsonObject_Post_RecData.put("MODE_OF_PAYMENT" , "");

                    if (cursor_PostRcData.getString(10) != null)
                        jsonObject_Post_RecData.put("COLLECTION_AMOUNT" , cursor_PostRcData.getString(10));
                    else
                        jsonObject_Post_RecData.put("COLLECTION_AMOUNT" , "");

                    if (cursor_PostRcData.getString(11) != null)
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_DATE" , cursor_PostRcData.getString(11));
                    else
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_DATE" , "");

                    if (cursor_PostRcData.getString(12) != null)
                        jsonObject_Post_RecData.put("MANUAL_RECEIPT_NO" , cursor_PostRcData.getString(12));
                    else
                        jsonObject_Post_RecData.put("MANUAL_RECEIPT_NO" , "");

                    if (cursor_PostRcData.getString(13) != null)
                        jsonObject_Post_RecData.put("PROVISIONAL_RECEIPT_NO" , cursor_PostRcData.getString(13));
                    else
                        jsonObject_Post_RecData.put("PROVISIONAL_RECEIPT_NO" , "");

                    if (cursor_PostRcData.getString(14) != null)
                        jsonObject_Post_RecData.put("CHEQUE_NO" , cursor_PostRcData.getString(14));
                    else
                        jsonObject_Post_RecData.put("CHEQUE_NO" , "");

                    if (cursor_PostRcData.getString(15) != null)
                        jsonObject_Post_RecData.put("CHEQUE_BANK" , cursor_PostRcData.getString(15));
                    else
                        jsonObject_Post_RecData.put("CHEQUE_BANK" , "");

                    if (cursor_PostRcData.getString(16) != null)
                        jsonObject_Post_RecData.put("BRANCH" , cursor_PostRcData.getString(16));
                    else
                        jsonObject_Post_RecData.put("BRANCH" , "");

                    if (cursor_PostRcData.getString(17) != null)
                        jsonObject_Post_RecData.put("CHEQUE_DATE" , cursor_PostRcData.getString(17));
                    else
                        jsonObject_Post_RecData.put("CHEQUE_DATE" , "");

                    if (cursor_PostRcData.getString(18) != null)
                        jsonObject_Post_RecData.put("REFERENCE_NO" , cursor_PostRcData.getString(18));
                    else
                        jsonObject_Post_RecData.put("REFERENCE_NO" , "");

                    if (cursor_PostRcData.getString(19) != null)
                        jsonObject_Post_RecData.put("PAYMENT_DATE" , cursor_PostRcData.getString(19));
                    else
                        jsonObject_Post_RecData.put("PAYMENT_DATE" , "");

                    if (cursor_PostRcData.getString(20) != null)
                        jsonObject_Post_RecData.put("OPERATOR" , cursor_PostRcData.getString(20));
                    else
                        jsonObject_Post_RecData.put("OPERATOR" , "");

                    if (cursor_PostRcData.getString(21) != null)
                        jsonObject_Post_RecData.put("PAYMENT_RECEIVED_FROM_THIRD_PARTY" , cursor_PostRcData.getString(21));
                    else
                        jsonObject_Post_RecData.put("PAYMENT_RECEIVED_FROM_THIRD_PARTY" , "");

                    if (cursor_PostRcData.getString(22) != null)
                        jsonObject_Post_RecData.put("NAME" , cursor_PostRcData.getString(22));
                    else
                        jsonObject_Post_RecData.put("NAME" , "");

                    if (cursor_PostRcData.getString(23) != null)
                        jsonObject_Post_RecData.put("RELATIONSHIP" , cursor_PostRcData.getString(23));
                    else
                        jsonObject_Post_RecData.put("RELATIONSHIP" , "");

                    if (cursor_PostRcData.getString(24) != null)
                        jsonObject_Post_RecData.put("PHONE_NUMBER" , cursor_PostRcData.getString(24));
                    else
                        jsonObject_Post_RecData.put("PHONE_NUMBER" , "");

                    if (cursor_PostRcData.getString(25) != null)
                        jsonObject_Post_RecData.put("ADDRESS" , cursor_PostRcData.getString(25));
                    else
                        jsonObject_Post_RecData.put("ADDRESS" , "");

                    if (cursor_PostRcData.getString(26) != null)
                        jsonObject_Post_RecData.put("PROMISE_TO_PAY_DATE" , cursor_PostRcData.getString(26));
                    else
                        jsonObject_Post_RecData.put("PROMISE_TO_PAY_DATE" , "");

                    if (cursor_PostRcData.getString(27) != null)
                        jsonObject_Post_RecData.put("PTP_AMOUNT" , cursor_PostRcData.getString(27));
                    else
                        jsonObject_Post_RecData.put("PTP_AMOUNT" , cursor_PostRcData.getString(27));

                    if (cursor_PostRcData.getString(28) != null)
                        jsonObject_Post_RecData.put("PTP_CHANNEL_MODE" , cursor_PostRcData.getString(28));
                    else
                        jsonObject_Post_RecData.put("PTP_CHANNEL_MODE" , cursor_PostRcData.getString(28));

                    if (cursor_PostRcData.getString(29) != null)
                        jsonObject_Post_RecData.put("PTP_BRANCH" , cursor_PostRcData.getString(29));
                    else
                        jsonObject_Post_RecData.put("PTP_BRANCH" , "");

                    if (cursor_PostRcData.getString(30) != null)
                        jsonObject_Post_RecData.put("PROMISE_MADE_BY" , cursor_PostRcData.getString(30));
                    else
                        jsonObject_Post_RecData.put("PROMISE_MADE_BY" , "");

                    if (cursor_PostRcData.getString(31) != null)
                        jsonObject_Post_RecData.put("PICK_UP_DATE_AND_TIME" , cursor_PostRcData.getString(31));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_DATE_AND_TIME" , "");

                    if (cursor_PostRcData.getString(32) != null)
                        jsonObject_Post_RecData.put("ADDRESS_FOR_PICK_UP" , cursor_PostRcData.getString(32));
                    else
                        jsonObject_Post_RecData.put("ADDRESS_FOR_PICK_UP" , "");

                    if (cursor_PostRcData.getString(33) != null)
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_BRANCH" , cursor_PostRcData.getString(33));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_BRANCH" , "");

                    if (cursor_PostRcData.getString(34) != null)
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_COLLECTION_EXECUTIVE" , cursor_PostRcData.getString(34));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_COLLECTION_EXECUTIVE" , "");

                    if (cursor_PostRcData.getString(35) != null)
                        jsonObject_Post_RecData.put("PICK_UP_AMOUNT" , cursor_PostRcData.getString(35));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_AMOUNT" , "");

                    if (cursor_PostRcData.getString(36) != null)
                        jsonObject_Post_RecData.put("ENTRY_NO" , cursor_PostRcData.getString(36));
                    else
                        jsonObject_Post_RecData.put("ENTRY_NO" , "");

                    if (cursor_PostRcData.getString(37) != null)
                        jsonObject_Post_RecData.put("COMPLAINT_DATE" , cursor_PostRcData.getString(37));
                    else
                        jsonObject_Post_RecData.put("COMPLAINT_DATE" , "");

                    if (cursor_PostRcData.getString(38) != null)
                        jsonObject_Post_RecData.put("POLICE_STATION" , cursor_PostRcData.getString(38));
                    else
                        jsonObject_Post_RecData.put("POLICE_STATION" , "");

                    if (cursor_PostRcData.getString(39) != null)
                        jsonObject_Post_RecData.put("INQUIRY_DATE" , cursor_PostRcData.getString(39));
                    else
                        jsonObject_Post_RecData.put("INQUIRY_DATE" , "");

                    if (cursor_PostRcData.getString(40) != null)
                        jsonObject_Post_RecData.put("VEHICLE_MAKE" , cursor_PostRcData.getString(40));
                    else
                        jsonObject_Post_RecData.put("VEHICLE_MAKE" , "");

                    if (cursor_PostRcData.getString(41) != null)
                        jsonObject_Post_RecData.put("MODEL" , cursor_PostRcData.getString(41));
                    else
                        jsonObject_Post_RecData.put("MODEL" , "");

                    if (cursor_PostRcData.getString(42) != null)
                        jsonObject_Post_RecData.put("VARIANT" , cursor_PostRcData.getString(42));
                    else
                        jsonObject_Post_RecData.put("VARIANT" , "");

                    if (cursor_PostRcData.getString(43) != null)
                        jsonObject_Post_RecData.put("VEHICLE_NO" , cursor_PostRcData.getString(43));
                    else
                        jsonObject_Post_RecData.put("VEHICLE_NO" , "");

                    if (cursor_PostRcData.getString(44) != null)
                        jsonObject_Post_RecData.put("ASSET_DATE" , cursor_PostRcData.getString(44));
                    else
                        jsonObject_Post_RecData.put("ASSET_DATE" , "");

                    if (cursor_PostRcData.getString(45) != null)
                        jsonObject_Post_RecData.put("DRIVEN_BY" , cursor_PostRcData.getString(45));
                    else
                        jsonObject_Post_RecData.put("DRIVEN_BY" , "");

                    if (cursor_PostRcData.getString(46) != null)
                        jsonObject_Post_RecData.put("VEHICLE_CONDITION" , cursor_PostRcData.getString(46));
                    else
                        jsonObject_Post_RecData.put("VEHICLE_CONDITION" , "");

                    if (cursor_PostRcData.getString(47) != null)
                        jsonObject_Post_RecData.put("RENTAL_PAYING_BY" , cursor_PostRcData.getString(47));
                    else
                        jsonObject_Post_RecData.put("RENTAL_PAYING_BY" , "");

                    if (cursor_PostRcData.getString(48) != null)
                        jsonObject_Post_RecData.put("OWNERSHIP" , cursor_PostRcData.getString(48));
                    else
                        jsonObject_Post_RecData.put("OWNERSHIP" , "");

                    if (cursor_PostRcData.getString(49) != null)
                        jsonObject_Post_RecData.put("RTP_REASON" , cursor_PostRcData.getString(49));
                    else
                        jsonObject_Post_RecData.put("RTP_REASON" , "");

                    if (cursor_PostRcData.getString(50) != null)
                        jsonObject_Post_RecData.put("PAID_AMOUNT" , cursor_PostRcData.getString(50));
                    else
                        jsonObject_Post_RecData.put("PAID_AMOUNT" , "");

                    if (cursor_PostRcData.getString(51) != null)
                        jsonObject_Post_RecData.put("PAID_CHANNEL_MODE" , cursor_PostRcData.getString(51));
                    else
                        jsonObject_Post_RecData.put("PAID_CHANNEL_MODE" , "");

                    if (cursor_PostRcData.getString(52) != null)
                        jsonObject_Post_RecData.put("PAID_RECEIPT_NO" , cursor_PostRcData.getString(52));
                    else
                        jsonObject_Post_RecData.put("PAID_RECEIPT_NO" , "");

                    if (cursor_PostRcData.getString(53) != null)
                        jsonObject_Post_RecData.put("PAID_COLLECTION_EXECUTIVE_NAME" , cursor_PostRcData.getString(53));
                    else
                        jsonObject_Post_RecData.put("PAID_COLLECTION_EXECUTIVE_NAME" , "");

                    if (cursor_PostRcData.getString(54) != null)
                        jsonObject_Post_RecData.put("FOLLOW_UP_DATE" , cursor_PostRcData.getString(54));
                    else
                        jsonObject_Post_RecData.put("FOLLOW_UP_DATE" , "");

                    if (cursor_PostRcData.getString(55) != null)
                        jsonObject_Post_RecData.put("MEETING_DATE" , cursor_PostRcData.getString(55));
                    else
                        jsonObject_Post_RecData.put("MEETING_DATE" , "");

                    if (cursor_PostRcData.getString(56) != null)
                        jsonObject_Post_RecData.put("COMMENTS" , cursor_PostRcData.getString(56));
                    else
                        jsonObject_Post_RecData.put("COMMENTS" , cursor_PostRcData.getString(57));

                    if (cursor_PostRcData.getString(57) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_ADDRESS" , cursor_PostRcData.getString(57));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_ADDRESS" , "");

                    if (cursor_PostRcData.getString(58) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_LAT" , cursor_PostRcData.getString(58));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_LAT" , "");

                    if (cursor_PostRcData.getString(59) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_LONG" , cursor_PostRcData.getString(59));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_LONG" , "");

                    if (cursor_PostRcData.getString(60) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_PINCODE" , cursor_PostRcData.getString(60));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_PINCODE" , "");

                    if (cursor_PostRcData.getString(61) != null)
                        jsonObject_Post_RecData.put("CORE_STATUS" , cursor_PostRcData.getString(61));
                    else
                        jsonObject_Post_RecData.put("CORE_STATUS" , "");

                    if (cursor_PostRcData.getString(62) != null)
                        jsonObject_Post_RecData.put("PAYMENT_TYPE" , cursor_PostRcData.getString(62));
                    else
                        jsonObject_Post_RecData.put("PAYMENT_TYPE" , "");

                    if (cursor_PostRcData.getString(63) != null)
                        jsonObject_Post_RecData.put("SERVICE_PROVIDER" , cursor_PostRcData.getString(63));
                    else
                        jsonObject_Post_RecData.put("SERVICE_PROVIDER" , "");

                    if (cursor_PostRcData.getString(64) != null)
                        jsonObject_Post_RecData.put("CONTACTNO_TYPE" , cursor_PostRcData.getString(64));
                    else
                        jsonObject_Post_RecData.put("CONTACTNO_TYPE" , "");

                    if (cursor_PostRcData.getString(65) != null)
                        jsonObject_Post_RecData.put("ADDLINE1" , cursor_PostRcData.getString(65));
                    else
                        jsonObject_Post_RecData.put("ADDLINE1" , "");

                    if (cursor_PostRcData.getString(66) != null)
                        jsonObject_Post_RecData.put("ADDLINE2" , cursor_PostRcData.getString(66));
                    else
                        jsonObject_Post_RecData.put("ADDLINE2" , "");

                    if (cursor_PostRcData.getString(67) != null)
                        jsonObject_Post_RecData.put("ADDLINE3" , cursor_PostRcData.getString(67));
                    else
                        jsonObject_Post_RecData.put("ADDLINE3" , "");

                    if (cursor_PostRcData.getString(68) != null)
                        jsonObject_Post_RecData.put("ADDLINE4" , cursor_PostRcData.getString(68));
                    else
                        jsonObject_Post_RecData.put("ADDLINE4" , "");

                    if (cursor_PostRcData.getString(69) != null)
                        jsonObject_Post_RecData.put("POSTAL_TOWN" , cursor_PostRcData.getString(69));
                    else
                        jsonObject_Post_RecData.put("POSTAL_TOWN" , "");

                    if (cursor_PostRcData.getString(70) != null)
                        jsonObject_Post_RecData.put("POSTAL_CODE" , cursor_PostRcData.getString(70));
                    else
                        jsonObject_Post_RecData.put("POSTAL_CODE" , "");

                    if (cursor_PostRcData.getString(71) != null)
                        jsonObject_Post_RecData.put("MEETING_TIME" , cursor_PostRcData.getString(71));
                    else
                        jsonObject_Post_RecData.put("MEETING_TIME" , "");

                    if (cursor_PostRcData.getString(72) != null)
                        jsonObject_Post_RecData.put("VENUE" , cursor_PostRcData.getString(72));
                    else
                        jsonObject_Post_RecData.put("VENUE" , "");

                    if (cursor_PostRcData.getString(73) != null)
                        jsonObject_Post_RecData.put("HANDOVER_BY" , cursor_PostRcData.getString(73));
                    else
                        jsonObject_Post_RecData.put("HANDOVER_BY" , "");

                    if (cursor_PostRcData.getString(74) != null)
                        jsonObject_Post_RecData.put("HANDOVER_NAME" , cursor_PostRcData.getString(74));
                    else
                        jsonObject_Post_RecData.put("HANDOVER_NAME" , "");

                    if (cursor_PostRcData.getString(75) != null)
                        jsonObject_Post_RecData.put("HANDOVER_NIC" , cursor_PostRcData.getString(75));
                    else
                        jsonObject_Post_RecData.put("HANDOVER_NIC" , "");

                    if (cursor_PostRcData.getString(76) != null)
                        jsonObject_Post_RecData.put("INFORMED_TO" , cursor_PostRcData.getString(76));
                    else
                        jsonObject_Post_RecData.put("INFORMED_TO" , "");

                    if (cursor_PostRcData.getString(77) != null)
                        jsonObject_Post_RecData.put("FOLLOW_UP_TIME" , cursor_PostRcData.getString(77));
                    else
                        jsonObject_Post_RecData.put("FOLLOW_UP_TIME" , "");

                    if (cursor_PostRcData.getString(78) != null)
                        jsonObject_Post_RecData.put("PICKUP_TIME" , cursor_PostRcData.getString(78));
                    else
                        jsonObject_Post_RecData.put("PICKUP_TIME" , "");

                    if (cursor_PostRcData.getString(79) != null)
                        jsonObject_Post_RecData.put("UPDATE_TIME" , cursor_PostRcData.getString(79));
                    else
                        jsonObject_Post_RecData.put("UPDATE_TIME" , "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray_PostRecoveryData.put(jsonObject_Post_RecData);
            }while (cursor_PostRcData.moveToNext());
        }


        //==== Create Final Json Array
        JSONObject jsonObject_PostFinal = new JSONObject();
        try {
            jsonObject_PostFinal.put("REC-ACTION" , jsonArray_PostRecoveryData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //requestQueue_PostAllAction   = VollySingleton.getInstance(mContex).getRequestQueue();
        String url = PHP_URL_SQL + "RECOVERY-POST-ACTION.php";
        JsonObjectRequest jsonObjectRequest_RecActionAllData = new JsonObjectRequest(Request.Method.POST, url, jsonObject_PostFinal,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            MainResponceCodeRecovery = response.getString("RESULT");
                            Log.d("PostRecoveryActionData", MainResponceCodeRecovery);
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
                MainResponceCodeRecovery="";
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
        jsonObjectRequest_RecActionAllData.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog.show();
        //requestQueue_PostAllAction.getCache().clear();
        requestQueue_final.start();
        requestQueue_final.add(jsonObjectRequest_RecActionAllData);

        //==== Fineash Request
        requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce=false;
                if (MainResponceCodeRecovery != null)
                {
                    CheckResponce = MainResponceCodeRecovery.equals("DONE");
                }

                if (CheckResponce == true)
                {
                    Log.d("PostRecoveryActionData", "FINESH");
                    progressDialog.dismiss();
                    //=== Update Recovery Tranfer flag
                    SQLiteDatabase sqLiteDatabase_updateglg = sqlliteCreateRecovery_GetMaster.getWritableDatabase();
                    ContentValues contentValues_data = new ContentValues();
                    contentValues_data.put("LIVE_SERVER_UPDATE" , "Y");
                    sqLiteDatabase_updateglg.update("nemf_form_updater" ,contentValues_data , "LIVE_SERVER_UPDATE = ''" , null);
                    sqlliteCreateRecovery_GetMaster.close();
                    //===========================

                    /*
                    requestQueue_final.getCache().clear();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContex);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Data synchronization process done.");
                    builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                    */
                }
            }
        });
    }


    //===============================================================
    //                Get Recovery Master Data
    //===============================================================
    public void GetRecoveryMasterData()
    {

        //==== Delete the old data
        sqlliteCreateRecovery_GetMaster.DeleteData("masr_sizer");
        sqlliteCreateRecovery_GetMaster.DeleteData("recovery_user_group");
        sqlliteCreateRecovery_GetMaster.DeleteData("masr_actioncode");
        sqlliteCreateRecovery_GetMaster.DeleteData("masr_actioncodeptp");
        sqlliteCreateRecovery_GetMaster.DeleteData("masr_channelmode");
        sqlliteCreateRecovery_GetMaster.DeleteData("masr_drivenby");
        sqlliteCreateRecovery_GetMaster.DeleteData("masr_ownership");
        sqlliteCreateRecovery_GetMaster.DeleteData("masr_paymentmode");
        sqlliteCreateRecovery_GetMaster.DeleteData("masr_relationship");
        sqlliteCreateRecovery_GetMaster.DeleteData("masr_vehiclecondition");
        sqlliteCreateRecovery_GetMaster.DeleteData("masr_cash_wallet");

        String url = "http://afimobile.abansfinance.lk/mobilephp/RECOVERY-MASTER-DATA.php";
        JsonObjectRequest jsonObjectRequest_MasterData = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("Log", "RecoveryMast-Respoce");
                        try {

                            JSONArray Master_ACTIONCODE         = response.getJSONArray("TT-NEW-ACTIONCODE");
                            JSONArray Master_PTPCODE            = response.getJSONArray("TT-NEW-PTPCODE");
                            JSONArray Master_CHANEL             = response.getJSONArray("TT-NEW-CHANEL");
                            JSONArray Master_DRIVEMODE          = response.getJSONArray("TT-NEW-DRIVEMODE");
                            JSONArray Master_OWNERSHIP          = response.getJSONArray("TT-NEW-OWNERSHIP");
                            JSONArray Master_PAYMENTMODE        = response.getJSONArray("TT-NEW-PAYMENTMODE");
                            JSONArray Master_RELASTIONSHIP      = response.getJSONArray("TT-NEW-RELASTIONSHIP");
                            JSONArray Master_VEHICLECONDTION    = response.getJSONArray("TT-NEW-VEHICLECONDTION");
                            JSONArray Master_RECOVERY_GROUP     = response.getJSONArray("TT-NEW-RECUSERGROUP");
                            JSONArray Master_RECOVERY_SIZER     = response.getJSONArray("TT-NEW-SIZER");
                            JSONArray Master_RECOVERY_WALLET     = response.getJSONArray("TT-NEW-OFF-WALLET");


                            //==== Offier Wallet Details
                            String mOfficecode="" , mOffice_name="" , mOfficer_Branch="" , mOfficer_manager="" , mOfficer_wallet="" , mOfficer_nic="" , mSts="";
                            for (int i = 0; i < Master_RECOVERY_WALLET.length(); i++)
                            {
                                JSONObject jsonObject_wallet = Master_RECOVERY_WALLET.getJSONObject(i);
                                mOfficecode         =   jsonObject_wallet.getString("offier_code");
                                mOffice_name        =   jsonObject_wallet.getString("officer_name");
                                mOfficer_Branch     =   jsonObject_wallet.getString("officer_branch");
                                mOfficer_manager    =   jsonObject_wallet.getString("offier_manager");
                                mOfficer_wallet     =   jsonObject_wallet.getString("officer_wallet_limit");
                                mOfficer_nic        =   jsonObject_wallet.getString("offier_nic");
                                mSts                =   jsonObject_wallet.getString("wallet_sts");

                                sqlliteCreateRecovery_GetMaster.InsertWallet(mOfficecode,mOffice_name,mOfficer_Branch,mOfficer_manager,mOfficer_wallet,mOfficer_nic,mSts);
                            }


                            //=== Recovery Sizerg Details Updata
                            String Scode="" , SName="" , SNic="" , SAdders="" , Sarea="" , Sclode="" , Ssts="";
                            for (int i = 0; i < Master_RECOVERY_SIZER.length(); i++)
                            {
                                JSONObject jsonObject_Sizer = Master_RECOVERY_SIZER.getJSONObject(i);
                                Scode       =    jsonObject_Sizer.getString("sizer_code");
                                SName       =    jsonObject_Sizer.getString("sizer_name");
                                SNic        =    jsonObject_Sizer.getString("sizer_nic");
                                SAdders     =    jsonObject_Sizer.getString("sizer_adders");
                                Sarea       =    jsonObject_Sizer.getString("sizer_area");
                                Sclode      =    jsonObject_Sizer.getString("sizer_clcode");
                                Ssts      =    jsonObject_Sizer.getString("sizer_sts");

                                sqlliteCreateRecovery_GetMaster.InsertSizerData(Scode,SName,SNic,SAdders,Sarea,Sclode,Ssts);
                            }

                            //===   Recovery User Group
                            for (int i = 0; i < Master_RECOVERY_GROUP.length(); i++)
                            {
                                String mMgrCode="" , mMgrName="" , mMgerPhone="" , mOffcode="" , mOffName="" , mOffPhone , mReginalMgr="" , mReginalName="" , mReginalPhoneno="";
                                JSONObject jsonObject_UserGroup = Master_RECOVERY_GROUP.getJSONObject(i);
                                mMgrCode        =   jsonObject_UserGroup.getString("rec_manager");
                                mMgrName        =   jsonObject_UserGroup.getString("rec_manager_name");
                                mMgerPhone      =   jsonObject_UserGroup.getString("rec_manager_phoneno");
                                mOffcode        =   jsonObject_UserGroup.getString("ass_officer");
                                mOffName        =   jsonObject_UserGroup.getString("ass_offier_name");
                                mOffPhone       =   jsonObject_UserGroup.getString("ass_officer_phoneno");
                                mReginalMgr     =   jsonObject_UserGroup.getString("reginal_manger");
                                mReginalName    =   jsonObject_UserGroup.getString("reginal_manager_name");
                                mReginalPhoneno =   jsonObject_UserGroup.getString("reginal_manager_phoneno");

                                sqlliteCreateRecovery_GetMaster.InsertRecoveryUser(mMgrCode,mMgrName,mMgerPhone,mOffcode,mOffName,mOffPhone,mReginalMgr,mReginalName,mReginalPhoneno);
                            }

                            //=== Action Code Update
                            for (int i = 0; i < Master_ACTIONCODE.length(); i++)
                            {
                                String ActionCode="" , ActionDescription="" ;
                                JSONObject jsonObject_ActionCode = Master_ACTIONCODE.getJSONObject(i);
                                ActionCode          = jsonObject_ActionCode.getString("action_code");
                                ActionDescription   = jsonObject_ActionCode.getString("action_descr");


                                sqlliteCreateRecovery_GetMaster.InsertActionCode(ActionCode , ActionDescription);
                            }
                            Log.d("Log", "ActionCode-DONE");

                            //=== PTP Code Update
                            for (int i = 0; i < Master_PTPCODE.length(); i++)
                            {
                                String PtpCode="" , PtpDes="";
                                JSONObject jsonObject_PTPCODE = Master_PTPCODE.getJSONObject(i);
                                PtpCode = jsonObject_PTPCODE.getString("actionptp_code");
                                PtpDes = jsonObject_PTPCODE.getString("actionptp_descr");
                                sqlliteCreateRecovery_GetMaster.InsertPTPCode(PtpCode , PtpDes);
                            }
                            Log.d("Log", "PTP-DONE");

                            //=== Chanell Code Update
                            for (int i = 0; i < Master_CHANEL.length(); i++)
                            {
                                String ChanCode="" , ChanalDes="";
                                JSONObject jsonObject_CHANEL = Master_CHANEL.getJSONObject(i);
                                ChanCode = jsonObject_CHANEL.getString("channelmode_code");
                                ChanalDes = jsonObject_CHANEL.getString("channelmode_descr");
                                sqlliteCreateRecovery_GetMaster.InsertChanaaData(ChanCode , ChanalDes);
                            }
                            Log.d("Log", "CHANALL-DONE");

                            //=== Drive Code Update
                            for (int i = 0; i < Master_DRIVEMODE.length(); i++)
                            {
                                String DriveCode="" , DriveDEs="";
                                JSONObject jsonObject_DRIVEMODE = Master_DRIVEMODE.getJSONObject(i);
                                DriveCode = jsonObject_DRIVEMODE.getString("drivenby_code");
                                DriveDEs = jsonObject_DRIVEMODE.getString("drivenby_descr");
                                sqlliteCreateRecovery_GetMaster.InsertDriveMode(DriveCode , DriveDEs);
                            }
                            Log.d("Log", "DRIVE-DONE");

                            //=== Ownership Code Update
                            for (int i = 0; i < Master_OWNERSHIP.length(); i++)
                            {
                                String OwnCode="" , OwnDes="";
                                JSONObject jsonObject_OWNERSHIP = Master_OWNERSHIP.getJSONObject(i);
                                OwnCode = jsonObject_OWNERSHIP.getString("ownership_code");
                                OwnDes = jsonObject_OWNERSHIP.getString("ownership_descr");
                                sqlliteCreateRecovery_GetMaster.InsertOwnship(OwnCode , OwnDes);
                            }
                            Log.d("Log", "OWNSHIP-DONE");

                            //=== Payment Mode Update
                            for (int i = 0; i < Master_PAYMENTMODE.length(); i++)
                            {
                                String PayCode="" , PayDes="";
                                JSONObject jsonObject_PAYMENTMODE = Master_PAYMENTMODE.getJSONObject(i);
                                PayCode = jsonObject_PAYMENTMODE.getString("paymentmode_code");
                                PayDes = jsonObject_PAYMENTMODE.getString("paymentmode_descr");
                                sqlliteCreateRecovery_GetMaster.InsertPaymentMode(PayCode , PayDes);
                            }
                            Log.d("Log", "PAYMODE-DONE");

                            //=== Relastion Update
                            for (int i = 0; i < Master_RELASTIONSHIP.length(); i++)
                            {
                                String RelCode="" , RelDes="";
                                JSONObject jsonObject_RELASTIONSHIP = Master_RELASTIONSHIP.getJSONObject(i);
                                RelCode = jsonObject_RELASTIONSHIP.getString("relationship_code");
                                RelDes = jsonObject_RELASTIONSHIP.getString("relationship_descr");
                                sqlliteCreateRecovery_GetMaster.InsertRelastionCode(RelCode , RelDes);
                            }
                            Log.d("Log", "RELASTION-DONE");

                            //=== Vehiclde Condtion Update
                            for (int i = 0; i < Master_VEHICLECONDTION.length(); i++)
                            {
                                String VehCode="" , VehDes="";
                                JSONObject jsonObject_VEHICLECONDTION = Master_VEHICLECONDTION.getJSONObject(i);
                                VehCode = jsonObject_VEHICLECONDTION.getString("vehiclecon_code");
                                VehDes = jsonObject_VEHICLECONDTION.getString("vehiclecon_descr");
                                sqlliteCreateRecovery_GetMaster.InsertVehicleCond(VehCode , VehDes);
                            }
                            Log.d("Log", "VEHICLECON-DONE");

                            MainResponceCode="DONE";

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                MainResponceCode="ERROR";
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
        jsonObjectRequest_MasterData.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        //==== Sett the Responces
        progressDialog.show();
        //requestQueue_FileComplete.getCache().clear();
        requestQueue_final.start();
        requestQueue_final.add(jsonObjectRequest_MasterData);

        //==== Fineash Request
        requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce=false;
                if (MainResponceCode != null)
                {
                    CheckResponce = MainResponceCode.equals("DONE");
                }

                if (CheckResponce == true)
                {

                    //=======  Create Sequence Table
                    SQLiteDatabase sqLiteDatabase_cretaseq = sqlliteCreateRecovery_GetMaster.getWritableDatabase();
                    ContentValues contentValues_seq_create = new ContentValues();
                    contentValues_seq_create.put("seq_code" , "RCPT");
                    contentValues_seq_create.put("seq_count" , "0");
                    sqLiteDatabase_cretaseq.insert("masr_sequence" , null , contentValues_seq_create);

                    contentValues_seq_create.put("seq_code" , "DEPO");
                    contentValues_seq_create.put("seq_count" , "0");
                    sqLiteDatabase_cretaseq.insert("masr_sequence" , null , contentValues_seq_create);


                    progressDialog.dismiss();
                    sqlliteCreateRecovery_GetMaster.close();
                    requestQueue_final.getCache().clear();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContex);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Master Data Upload Successfully.");
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

    //===============================================================
    //                Get Recovery Genreal Data with User Id
    //===============================================================
    public void GetRecoveryGenrealData (String InpUserId) {
        mLoginCode = InpUserId;

        JSONObject jsonObjectUserId = new JSONObject();
        try {
            jsonObjectUserId.put("MGR_CODE", mLoginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //requestQueue_GetRecoveryData = VollySingleton.getInstance(mContex).getRequestQueue();
        String url = PHP_URL_SQL + "RECOVERY-GET-REC-DETAILS.php";
        JsonObjectRequest jsonObjectRequest_RecoveryGendetails = new JsonObjectRequest(Request.Method.POST, url, jsonObjectUserId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //=== Get Data Responses


                        try {
                            JSONArray REC_GENERAL = response.getJSONArray("TT-RECOVER-DETAILS");
                            SQLiteDatabase sqLiteDatabase_RECOVERY_DATA = sqlliteCreateRecovery_GetMaster.getWritableDatabase();

                            //=== Delete The Table ===
                            sqLiteDatabase_RECOVERY_DATA.delete("recovery_generaldetail", "Recovery_Executive =?", new String[]{mLoginCode});


                            //==== Update Recovery Genreal Details
                            for (int i = 0; i < REC_GENERAL.length(); i++) {
                                JSONObject jsonObject_REC_GENERAL = REC_GENERAL.getJSONObject(i);
                                ContentValues contentValues_RecoveryGenreal = new ContentValues();
                                contentValues_RecoveryGenreal.put("Client_Name", jsonObject_REC_GENERAL.getString("Client_Name"));
                                contentValues_RecoveryGenreal.put("Product_Type", jsonObject_REC_GENERAL.getString("Product_Type"));
                                contentValues_RecoveryGenreal.put("Asset_Model", jsonObject_REC_GENERAL.getString("Asset_Model"));
                                contentValues_RecoveryGenreal.put("Vehicle_Number", jsonObject_REC_GENERAL.getString("Vehicle_Number"));
                                contentValues_RecoveryGenreal.put("Due_Day", jsonObject_REC_GENERAL.getString("Due_Day"));
                                contentValues_RecoveryGenreal.put("Current_Month_Rental", jsonObject_REC_GENERAL.getString("Current_Month_Rental"));
                                contentValues_RecoveryGenreal.put("OD_Arrear_Amount", jsonObject_REC_GENERAL.getString("OD_Arrear_Amount"));
                                contentValues_RecoveryGenreal.put("Total_Arrear_Amount", jsonObject_REC_GENERAL.getString("Total_Arrear_Amount"));
                                contentValues_RecoveryGenreal.put("Last_Payment_Date", jsonObject_REC_GENERAL.getString("Last_Payment_Date"));
                                contentValues_RecoveryGenreal.put("Last_Payment_Amount", jsonObject_REC_GENERAL.getString("Last_Payment_Amount"));
                                contentValues_RecoveryGenreal.put("Facility_Payment_Status", jsonObject_REC_GENERAL.getString("Facility_Payment_Status"));
                                contentValues_RecoveryGenreal.put("Letter_Demand_Date", jsonObject_REC_GENERAL.getString("Letter_Demand_Date"));
                                contentValues_RecoveryGenreal.put("Client_Code", jsonObject_REC_GENERAL.getString("Client_Code"));
                                contentValues_RecoveryGenreal.put("NIC", jsonObject_REC_GENERAL.getString("NIC"));
                                contentValues_RecoveryGenreal.put("Customer_Full_Name", jsonObject_REC_GENERAL.getString("Customer_Full_Name"));
                                contentValues_RecoveryGenreal.put("Gender", jsonObject_REC_GENERAL.getString("Gender"));
                                contentValues_RecoveryGenreal.put("Occupation", jsonObject_REC_GENERAL.getString("Occupation"));
                                contentValues_RecoveryGenreal.put("Work_Place_Name", jsonObject_REC_GENERAL.getString("Work_Place_Name"));
                                contentValues_RecoveryGenreal.put("Work_Place_Address", jsonObject_REC_GENERAL.getString("Work_Place_Address"));
                                contentValues_RecoveryGenreal.put("Mobile_No1", jsonObject_REC_GENERAL.getString("Mobile_No1"));
                                contentValues_RecoveryGenreal.put("Mobile_No2", jsonObject_REC_GENERAL.getString("Mobile_No2"));
                                contentValues_RecoveryGenreal.put("Home_No", jsonObject_REC_GENERAL.getString("Home_No"));
                                contentValues_RecoveryGenreal.put("C_Address_Line_1", jsonObject_REC_GENERAL.getString("C_Address_Line_1"));
                                contentValues_RecoveryGenreal.put("C_Address_Line_2", jsonObject_REC_GENERAL.getString("C_Address_Line_2"));
                                contentValues_RecoveryGenreal.put("C_Address_Line_3", jsonObject_REC_GENERAL.getString("C_Address_Line_3"));
                                contentValues_RecoveryGenreal.put("C_Address_Line_4", jsonObject_REC_GENERAL.getString("C_Address_Line_4"));
                                contentValues_RecoveryGenreal.put("C_Postal_Town", jsonObject_REC_GENERAL.getString("C_Postal_Town"));
                                contentValues_RecoveryGenreal.put("P_Address_Line_1", jsonObject_REC_GENERAL.getString("P_Address_Line_1"));
                                contentValues_RecoveryGenreal.put("P_Address_Line_2", jsonObject_REC_GENERAL.getString("P_Address_Line_2"));
                                contentValues_RecoveryGenreal.put("P_Address_Line_3", jsonObject_REC_GENERAL.getString("P_Address_Line_3"));
                                contentValues_RecoveryGenreal.put("P_Address_Line_4", jsonObject_REC_GENERAL.getString("P_Address_Line_4"));
                                contentValues_RecoveryGenreal.put("P_Postal_Town", jsonObject_REC_GENERAL.getString("P_Postal_Town"));
                                contentValues_RecoveryGenreal.put("Facility_Number", jsonObject_REC_GENERAL.getString("Facility_Number"));
                                contentValues_RecoveryGenreal.put("Facility_Amount", jsonObject_REC_GENERAL.getString("Facility_Amount"));
                                contentValues_RecoveryGenreal.put("Facility_Branch", jsonObject_REC_GENERAL.getString("Facility_Branch"));
                                contentValues_RecoveryGenreal.put("Tenure", jsonObject_REC_GENERAL.getString("Tenure"));
                                contentValues_RecoveryGenreal.put("Recovery_Executive", jsonObject_REC_GENERAL.getString("Recovery_Executive"));
                                contentValues_RecoveryGenreal.put("Recovery_Executive_Name", jsonObject_REC_GENERAL.getString("Recovery_Executive_Name"));
                                contentValues_RecoveryGenreal.put("CC_Executive", jsonObject_REC_GENERAL.getString("CC_Executive"));
                                contentValues_RecoveryGenreal.put("Marketing_Executive", jsonObject_REC_GENERAL.getString("Marketing_Executive"));
                                contentValues_RecoveryGenreal.put("Follow_Up_Officer", jsonObject_REC_GENERAL.getString("Follow_Up_Officer"));
                                contentValues_RecoveryGenreal.put("Recovery_Executive_Phone", jsonObject_REC_GENERAL.getString("Recovery_Executive_Phone"));
                                contentValues_RecoveryGenreal.put("CC_Executive_Phone", jsonObject_REC_GENERAL.getString("CC_Executive_Phone"));
                                contentValues_RecoveryGenreal.put("Marketing_Executive_Phone", jsonObject_REC_GENERAL.getString("Marketing_Executive_Phone"));
                                contentValues_RecoveryGenreal.put("Follow_Up_Officer_Phone", jsonObject_REC_GENERAL.getString("Follow_Up_Officer_Phone"));
                                contentValues_RecoveryGenreal.put("Activation_Date", jsonObject_REC_GENERAL.getString("Activation_Date"));
                                contentValues_RecoveryGenreal.put("Expiry_Date", jsonObject_REC_GENERAL.getString("Expiry_Date"));
                                contentValues_RecoveryGenreal.put("Total_Facility_Amount", jsonObject_REC_GENERAL.getString("Total_Facility_Amount"));
                                contentValues_RecoveryGenreal.put("Disbursed_Amount", jsonObject_REC_GENERAL.getString("Disbursed_Amount"));
                                contentValues_RecoveryGenreal.put("Disbursement_Status", jsonObject_REC_GENERAL.getString("Disbursement_Status"));
                                contentValues_RecoveryGenreal.put("Interest_Rate", jsonObject_REC_GENERAL.getString("Interest_Rate"));
                                contentValues_RecoveryGenreal.put("Rental", jsonObject_REC_GENERAL.getString("Rental"));
                                contentValues_RecoveryGenreal.put("Contract_Status", jsonObject_REC_GENERAL.getString("Contract_Status"));
                                contentValues_RecoveryGenreal.put("Facility_Status", jsonObject_REC_GENERAL.getString("Facility_Status"));
                                contentValues_RecoveryGenreal.put("Down_Payment_No", jsonObject_REC_GENERAL.getString("Down_Payment_No"));
                                contentValues_RecoveryGenreal.put("Rentals_Matured_No", jsonObject_REC_GENERAL.getString("Rentals_Matured_No"));
                                contentValues_RecoveryGenreal.put("Rentals_Paid_No", jsonObject_REC_GENERAL.getString("Rentals_Paid_No"));
                                contentValues_RecoveryGenreal.put("Arrears_Rental_No", jsonObject_REC_GENERAL.getString("Arrears_Rental_No"));
                                contentValues_RecoveryGenreal.put("SPDC_Available", jsonObject_REC_GENERAL.getString("SPDC_Available"));
                                contentValues_RecoveryGenreal.put("Total_Outstanding", jsonObject_REC_GENERAL.getString("Total_Outstanding"));
                                contentValues_RecoveryGenreal.put("Capital_Outstanding", jsonObject_REC_GENERAL.getString("Capital_Outstanding"));
                                contentValues_RecoveryGenreal.put("Interest_Outstanding", jsonObject_REC_GENERAL.getString("Interest_Outstanding"));
                                contentValues_RecoveryGenreal.put("Arrears_Rental_Amount", jsonObject_REC_GENERAL.getString("Arrears_Rental_Amount"));
                                contentValues_RecoveryGenreal.put("OD_Interest", jsonObject_REC_GENERAL.getString("OD_Interest"));
                                contentValues_RecoveryGenreal.put("Interest_Arrears", jsonObject_REC_GENERAL.getString("Interest_Arrears"));
                                contentValues_RecoveryGenreal.put("Insurance_Arrears", jsonObject_REC_GENERAL.getString("Insurance_Arrears"));
                                contentValues_RecoveryGenreal.put("OD_Interest_OS", jsonObject_REC_GENERAL.getString("OD_Interest_OS"));
                                contentValues_RecoveryGenreal.put("Seizing_Charges", jsonObject_REC_GENERAL.getString("Seizing_Charges"));
                                contentValues_RecoveryGenreal.put("OC_Arrears", jsonObject_REC_GENERAL.getString("OC_Arrears"));
                                contentValues_RecoveryGenreal.put("FUTURE_CAPITAL", jsonObject_REC_GENERAL.getString("FUTURE_CAPITAL"));
                                contentValues_RecoveryGenreal.put("FUTURE_INTEREST", jsonObject_REC_GENERAL.getString("FUTURE_INTEREST"));
                                contentValues_RecoveryGenreal.put("TOTAL_SETTL_AMT", jsonObject_REC_GENERAL.getString("TOTAL_SETTL_AMT"));
                                sqLiteDatabase_RECOVERY_DATA.insert("recovery_generaldetail", null, contentValues_RecoveryGenreal);
                            }
                            Log.d("GetRecoveryGenrealData", "RESPONCE-DONE");

                            sqLiteDatabase_RECOVERY_DATA.close();


                            MainResponceCode = "DONE";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                MainResponceCode = "ERROR";
                String ErrorCode = "";
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

                String ErrorDescription = "Responses Failure.(" + ErrorCode.toString() + ")";
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex);
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
        jsonObjectRequest_RecoveryGendetails.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        //==== Sett the Responces
        progressDialog.setTitle("Recovery Data Process - Please Wait.");
        progressDialog.show();
        //requestQueue_GetRecoveryData.getCache().clear();
        requestQueue_final.start();
        requestQueue_final.add(jsonObjectRequest_RecoveryGendetails);

        //==== Fineash Request
        requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                boolean CheckResponce = false;
                if (MainResponceCode != null) {
                    CheckResponce = MainResponceCode.equals("DONE");
                }

                if (CheckResponce == true)
                {
                    Log.d("GetRecoveryGenrealData", "FINESH");
                    progressDialog.dismiss();
                    sqlliteCreateRecovery_GetMaster.close();
                    requestQueue_final.getCache().clear();

                    /*
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContex);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Recovery Data Upload Successfully.");
                    builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                    */
                }
            }
        });
    }
}
