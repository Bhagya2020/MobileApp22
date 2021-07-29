package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
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

import dmax.dialog.SpotsDialog;

public class Volly_Recovery_Get_DataSync
{
    public Context mContex;
    public SqlliteCreateRecovery sqlliteCreateRecovery_HomeSuns;
    public String PHP_URL_SQL,LoginUser,LoginDate,LoginBranch,mLoginName , mInputUserName;
    public RequestQueue mQueue;
    private android.app.AlertDialog progressDialog;
    public JsonObjectRequest jsonObjectRequest_GetUserData , jsonObjectRequest_GetCallData;
    public String mRespnces_GetAllData;


    public Volly_Recovery_Get_DataSync (Context context)
    {
        mContex = context;
        sqlliteCreateRecovery_HomeSuns = new SqlliteCreateRecovery(mContex);

        final Cache cache = new DiskBasedCache(mContex.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        mQueue = new RequestQueue(cache, network);


        progressDialog = new SpotsDialog(mContex, R.style.Custom);
        progressDialog.setTitle("Bhagya");

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        PHP_URL_SQL = "http://afimobile.abansfinance.lk/mobilephp/";
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        mLoginName  =   globleClassDetails.getOfficerName();
    }

    public void GetOffierData (String mUserId)
    {
        mInputUserName = mUserId;

        JSONObject jsonObject_Get_UserID = new JSONObject();
        try {
            jsonObject_Get_UserID.put("USER_CODE" , mInputUserName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //==== Create Request
        String url = PHP_URL_SQL + "RECOVERY-GET-HOME-SYNC-DATA.php";
        jsonObjectRequest_GetUserData = new JsonObjectRequest(Request.Method.POST, url, jsonObject_Get_UserID,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("GetOffierData", "RESPONDS-DONE");
                        try {

                            JSONArray jsonArray_Recovery_Genreal         =  response.getJSONArray("TT-RECOVER-DETAILS");
                            JSONArray jsonArray_Request_Manager          =  response.getJSONArray("TT-RECOVER-MANAGER-REQ");
                            JSONArray jsonArray_Request_facdetails       =  response.getJSONArray("TT-RECOVER-MANAGER-FACNO");
                            JSONArray jsonArray_Spical_back              =  response.getJSONArray("TT-RECOVER-SPICAL-CASE");
                            JSONArray jsonArray_Month_begin              =  response.getJSONArray("TT-RECOVER-MONTH-BEGIN");

                            //=== Update Recovery Genreal Data To Sqllite Databse
                            //====================================================

                            SQLiteDatabase sqLiteDatabase_RECOVERY_DATA = sqlliteCreateRecovery_HomeSuns.getWritableDatabase();
                            //=== Delete The Table ===
                            sqLiteDatabase_RECOVERY_DATA.delete("recovery_generaldetail", "Recovery_Executive =?", new String[]{mInputUserName});

                            for (int i = 0; i < jsonArray_Recovery_Genreal.length(); i++)
                            {
                                JSONObject jsonObject_REC_GENERAL = jsonArray_Recovery_Genreal.getJSONObject(i);
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
                            Log.d("GetOffierData-RecGen", "DONE");

                            //=== Update Manager Request Data ====================
                            //====================================================
                            String mFacno="" , mAssignMgr="" , mAssDate="" , mAssReason="" , mReq_id="" , Req_userName="" , Req_comment="";
                            for (int i=0 ; i< jsonArray_Request_Manager.length();i++)
                            {
                                JSONObject mydataJson = jsonArray_Request_Manager.getJSONObject(i);

                                mFacno          =   mydataJson.getString("Facility_no") ;
                                mAssignMgr      =   mydataJson.getString("Assign_MgrCode") ;
                                mAssDate        =   mydataJson.getString("Assign_Date") ;
                                mAssReason      =   mydataJson.getString("Assign_Reason") ;
                                mReq_id         =   mydataJson.getString("Req_User_Id") ;
                                Req_userName    =   mydataJson.getString("Req_User_Name") ;
                                Req_comment     =   mydataJson.getString("Req_Comment") ;

                                //=== Get Facility Details ===
                                sqlliteCreateRecovery_HomeSuns.InsertRequestMgrData(mFacno,mAssignMgr,mAssDate,mAssReason,mReq_id,Req_userName,Req_comment);
                            }
                            Log.d("GetOffierData-MgrReg", "DONE");

                            //=== Update Manager Request Facility Details ====================
                            //====================================================
                            //==== Update Recovery Genreal Details
                            for (int i = 0; i < jsonArray_Request_facdetails.length(); i++) {
                                JSONObject jsonObject_REC_GENERAL = jsonArray_Request_facdetails.getJSONObject(i);
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


                            //=== Transfer Spical Recovery Case
                            //==================================================
                            sqLiteDatabase_RECOVERY_DATA.delete("Recovery_speical_backet", "recovery_executive =?", new String[]{mInputUserName});
                            for (int i = 0; i < jsonArray_Spical_back.length(); i++)
                            {
                                JSONObject jsonObject_SPICAL_CASE = jsonArray_Spical_back.getJSONObject(i);
                                ContentValues contentValues_spical = new ContentValues();
                                contentValues_spical.put("facility_no" , jsonObject_SPICAL_CASE.getString("facility_no"));
                                contentValues_spical.put("client_name" , jsonObject_SPICAL_CASE.getString("client_name"));
                                contentValues_spical.put("recovery_executive" , jsonObject_SPICAL_CASE.getString("recovery_executive"));
                                contentValues_spical.put("branch_code" , jsonObject_SPICAL_CASE.getString("branch_code"));
                                contentValues_spical.put("recovery_bucket" , jsonObject_SPICAL_CASE.getString("recovery_bucket"));
                                contentValues_spical.put("recovery_bucket" , jsonObject_SPICAL_CASE.getString("recovery_bucket"));
                                sqLiteDatabase_RECOVERY_DATA.insert("Recovery_speical_backet" , null , contentValues_spical);
                            }

                            //==== Update the Month Begin Data Details
                            if (jsonArray_Month_begin.length() != 0)
                            {
                                sqLiteDatabase_RECOVERY_DATA.delete("Recovery_Month_Begin", "recovery_executive =?", new String[]{mInputUserName});

                                for (int i = 0; i < jsonArray_Month_begin.length(); i++)
                                {
                                    JSONObject jsonObject_Month_beg = jsonArray_Month_begin.getJSONObject(i);

                                    ContentValues contentValues_monthBegin = new ContentValues();
                                    contentValues_monthBegin.put("facility_no" , jsonObject_Month_beg.getString("Facility_Number"));
                                    contentValues_monthBegin.put("No_Rnt_arrays" , jsonObject_Month_beg.getString("Arrears_Rental_No"));
                                    contentValues_monthBegin.put("recovery_executive" , jsonObject_Month_beg.getString("Recovery_Executive"));
                                    contentValues_monthBegin.put("product_code" , jsonObject_Month_beg.getString("Product_Type"));
                                    contentValues_monthBegin.put("duedate" , jsonObject_Month_beg.getString("Due_Day"));
                                    contentValues_monthBegin.put("Total_arrays" , jsonObject_Month_beg.getString("Total_Arrear_Amount"));
                                    contentValues_monthBegin.put("OD_arrays" , jsonObject_Month_beg.getString("OD_Arrear_Amount"));
                                    contentValues_monthBegin.put("Rental_arrays" , jsonObject_Month_beg.getString("Arrears_Rental_Amount"));
                                    contentValues_monthBegin.put("Other_arrays" , jsonObject_Month_beg.getString("OD_Arrear_Amount"));
                                    contentValues_monthBegin.put("Process_Month" , jsonObject_Month_beg.getString("Process_month"));
                                    sqLiteDatabase_RECOVERY_DATA.insert("Recovery_Month_Begin" , null, contentValues_monthBegin);
                                }
                            }


                            sqLiteDatabase_RECOVERY_DATA.close();
                            Log.d("GetOffierData-MgrFacno", "DONE");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //==== Update Responces Code
                        mRespnces_GetAllData="DONE";
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
                        .setTitle("Error... 2")
                        .create();
                bmyAlert.show();
            }
        });
        jsonObjectRequest_GetUserData.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog.show();
        mQueue.start();
        mQueue.add(jsonObjectRequest_GetUserData);

        //==== Check Finsh Responces
        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce = false;
                if (mRespnces_GetAllData != null)
                {
                    if (mRespnces_GetAllData.equals("DONE"))
                    {
                        CheckResponce = mRespnces_GetAllData.equals("DONE");
                    }
                }

                if (CheckResponce == true)
                {
                    Log.d("GetOffierData", "COMPLETE");
                    progressDialog.dismiss();
                    //mQueue.getCache().clear();

                    Volly_Recovery_Post_DataSync volly_recovery_post_dataSync = new Volly_Recovery_Post_DataSync(mContex);
                    volly_recovery_post_dataSync.PostOfficerData(LoginUser);
                }
            }
        });
    }


    //=== Get Call Center PTP and Broken PTP details
    public void GetCallCenterData(String BrnCode)
    {
        JSONObject jsonObject_Get_CallCnter = new JSONObject();
        try {
            jsonObject_Get_CallCnter.put("BRANCH_CODE" , BrnCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("brcode" , BrnCode);

        String url = PHP_URL_SQL + "RECOVERY-GET-CALLCENTER-DATA.php";
        jsonObjectRequest_GetCallData = new JsonObjectRequest(Request.Method.POST, url, jsonObject_Get_CallCnter,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("GetCALLCENTER", "RESPONDS-DONE");
                        try {

                            JSONArray jsonArray_Recovery_Genreal         =  response.getJSONArray("TT-RECOVER-DETAILS-CALL");
                            JSONArray jsonArray_Recovery_Alocate         =  response.getJSONArray("TT-RECOVER-CALL-ALOCATE");

                            //=== Update Recovery Genreal Data To Sqllite Databse
                            //====================================================

                            SQLiteDatabase sqLiteDatabase_RECOVERY_DATA = sqlliteCreateRecovery_HomeSuns.getWritableDatabase();

                            //====== UPDATE ALOCATE CASE ALL
                            sqLiteDatabase_RECOVERY_DATA.execSQL("DELETE FROM recovery_call_center_action WHERE FACNO <> ''");

                            for (int i = 0; i < jsonArray_Recovery_Alocate.length(); i++)
                            {
                                JSONObject jsonObject_REC_ALO = jsonArray_Recovery_Alocate.getJSONObject(i);
                                ContentValues contentValues_alocate = new ContentValues();
                                contentValues_alocate.put("FACNO" , jsonObject_REC_ALO.getString("FACNO"));
                                contentValues_alocate.put("ALOCATE_OFF_CODE" , jsonObject_REC_ALO.getString("ALOCATE_OFF_CODE"));
                                contentValues_alocate.put("ALOCATE_OFF_NAME" , jsonObject_REC_ALO.getString("ALOCATE_OFF_NAME"));
                                contentValues_alocate.put("ALOCATE_BRANCH" , jsonObject_REC_ALO.getString("ALOCATE_BRANCH"));
                                contentValues_alocate.put("CALL_ACTION_CODE" , jsonObject_REC_ALO.getString("CALL_ACTION_CODE"));
                                contentValues_alocate.put("CALL_ACTION_DATE" , jsonObject_REC_ALO.getString("CALL_ACTION_DATE"));
                                contentValues_alocate.put("CALL_ACTION_USER" , jsonObject_REC_ALO.getString("CALL_ACTION_USER"));
                                contentValues_alocate.put("CALL_PTP_DATE" , jsonObject_REC_ALO.getString("CALL_PTP_DATE"));
                                contentValues_alocate.put("CALL_PTP_AMOUNT" , jsonObject_REC_ALO.getString("CALL_PTP_AMOUNT"));
                                contentValues_alocate.put("CALL_REMARKS" , jsonObject_REC_ALO.getString("CALL_REMARKS"));
                                contentValues_alocate.put("PAY_DATE" , jsonObject_REC_ALO.getString("PAY_DATE"));
                                contentValues_alocate.put("PAY_AMOUNT" , jsonObject_REC_ALO.getString("PAY_AMOUNT"));
                                contentValues_alocate.put("ACTION_STS" , jsonObject_REC_ALO.getString("ACTION_STS"));
                                sqLiteDatabase_RECOVERY_DATA.insert("recovery_call_center_action", null, contentValues_alocate);
                            }

                            //=== Delete The Table ===
                            sqLiteDatabase_RECOVERY_DATA.delete("CallCenter_recovery_generaldetail", "Facility_Branch =?", new String[]{BrnCode});

                            for (int i = 0; i < jsonArray_Recovery_Genreal.length(); i++)
                            {
                                JSONObject jsonObject_REC_GENERAL = jsonArray_Recovery_Genreal.getJSONObject(i);
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

                                sqLiteDatabase_RECOVERY_DATA.insert("CallCenter_recovery_generaldetail", null, contentValues_RecoveryGenreal);
                            }
                            Log.d("GetOffierData-RecGen", "DONE");
                            sqLiteDatabase_RECOVERY_DATA.close();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //==== Update Responces Code
                        mRespnces_GetAllData="DONE";
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
                        .setTitle("Error... 2")
                        .create();
                bmyAlert.show();
            }
        });
        jsonObjectRequest_GetCallData.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog.show();
        mQueue.start();
        mQueue.add(jsonObjectRequest_GetCallData);

        //==== Check Finsh Responces
        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce = false;
                if (mRespnces_GetAllData != null)
                {
                    if (mRespnces_GetAllData.equals("DONE"))
                    {
                        CheckResponce = mRespnces_GetAllData.equals("DONE");
                    }
                }

                if (CheckResponce == true)
                {
                    Log.d("GetCallData", "COMPLETE");
                    progressDialog.dismiss();
                    mQueue.getCache().clear();
                }
            }
        });

    }
}
