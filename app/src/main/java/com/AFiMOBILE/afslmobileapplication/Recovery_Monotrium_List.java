package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Recovery_Monotrium_List extends AppCompatActivity {

    private String LoginUser , LoginDate , LoginBranch , LoginUserName , mInputFacilityNo , PHP_URL_SQL , MainResponce , mCompleteProces;
    private RecyclerView mDataRecyView;
    private Adapter_Monotrium_list mAdapter;
    private Adapter_Monotrium_All_Details mNewAdapter;
    private Button mBtnRequest;
    private SqlliteCreateRecovery sqlliteCreateRecovery_linkFacility;
    private android.app.AlertDialog progressDialog;
    private RequestQueue requestQueue_final;
    private TextView mReqFacno;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_monotrium_list);


        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser       =   globleClassDetails.getUserid();
        LoginDate       =   globleClassDetails.getLoginDate();
        LoginBranch     =   globleClassDetails.getLoginBranch();
        LoginUserName   =   globleClassDetails.getOfficerName();
        PHP_URL_SQL     =   "http://afimobile.abansfinance.lk/mobilephp/";


        mReqFacno       =   (TextView)findViewById(R.id.Txtmaifacility);
        Intent intent = getIntent();
        mInputFacilityNo    =   intent.getStringExtra("FacilityNo");
        mReqFacno.setText(mInputFacilityNo);

        mBtnRequest     =   (Button)findViewById(R.id.btn_data_req);

        progressDialog = new SpotsDialog(Recovery_Monotrium_List.this, R.style.Custom);
        progressDialog.setTitle("Vist Data Synchronization..");

        final Cache cache = new DiskBasedCache(Recovery_Monotrium_List.this.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue_final = new RequestQueue(cache, network);

        sqlliteCreateRecovery_linkFacility = new SqlliteCreateRecovery(this);

        //=== Recycviwe load the data
        mDataRecyView   =   (RecyclerView)findViewById(R.id.ReyFacList);
        mDataRecyView.setHasFixedSize(true);
        mDataRecyView.setLayoutManager(new LinearLayoutManager(this));
        mNewAdapter = new Adapter_Monotrium_All_Details (this , LoadDataNew());
        //mDataRecyView.setAdapter(mNewAdapter);

        mBtnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLinkFacilityData();
            }
        });

    }

    private Cursor LoadDataNew()
    {
        double mTotalFcilityAmount=0.00 , mTotalReantalDue = 0.00 , mTotalRentalArrays = 0.00 , mTotalArrays=0.00 , mTotalSellemelt=0.00  ;

        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"Facility_Number" , "Facility_amount" , "Rental_Due" , "Rental_Arrays" , "Las_paid_date" , "Last_paid_amount" ,
                "Avarage_payment" , "Total_arrays" , "Selletemen_amount" });

        SQLiteDatabase sqLiteDatabase_all_data = sqlliteCreateRecovery_linkFacility.getReadableDatabase();
        Cursor cursor_all_data = sqLiteDatabase_all_data.rawQuery("select Facility_Number , Facility_Amount , Rentals_Matured_No , Arrears_Rental_No , Last_Payment_Date , " +
                "Last_Payment_Amount , Total_Arrear_Amount , TOTAL_SETTL_AMT from recovery_link_facility_details " , null);
        if (cursor_all_data.getCount() != 0)
        {
            cursor_all_data.moveToFirst();

            do {

                if (cursor_all_data.getString(1).length() != 0)
                {
                    mTotalFcilityAmount = mTotalFcilityAmount + Double.parseDouble(cursor_all_data.getString(1));
                }

                if (cursor_all_data.getString(2).length() != 0)
                {
                    mTotalReantalDue = mTotalReantalDue + Double.parseDouble(cursor_all_data.getString(2));
                }

                if (cursor_all_data.getString(3).length() != 0)
                {
                    mTotalRentalArrays = mTotalRentalArrays + Double.parseDouble(cursor_all_data.getString(3));
                }

                if (cursor_all_data.getString(6).length() != 0)
                {
                    mTotalArrays = mTotalArrays + Double.parseDouble(cursor_all_data.getString(6));
                }

                if (cursor_all_data.getString(7).length() != 0)
                {
                    mTotalSellemelt = mTotalSellemelt + Double.parseDouble(cursor_all_data.getString(7));
                }

                mOutPutcouser.addRow(new String[] { cursor_all_data.getString(0) , cursor_all_data.getString(1) , cursor_all_data.getString(2) ,
                        cursor_all_data.getString(3) , cursor_all_data.getString(4) , cursor_all_data.getString(5) , "" , cursor_all_data.getString(6) ,
                        cursor_all_data.getString(7) });

            }while (cursor_all_data.moveToNext());

            mOutPutcouser.addRow(new String[] { "TOTAL" , String.valueOf(mTotalFcilityAmount) ,
                                                        String.valueOf(mTotalReantalDue) ,
                                                        String.valueOf(mTotalRentalArrays),
                                                         "" , "" , "" , String.valueOf(mTotalArrays) , String.valueOf(mTotalSellemelt) });
        }

        return mOutPutcouser;
    }

    private void GetLinkFacilityData()
    {
        JSONObject jsonObject_visit_data = new JSONObject();
        try {
            jsonObject_visit_data.put("Facno" , mInputFacilityNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SQLiteDatabase sqLiteDatabase_montorium = sqlliteCreateRecovery_linkFacility.getWritableDatabase();
        sqLiteDatabase_montorium.execSQL("DELETE FROM recovery_link_facility_details");

        String url = PHP_URL_SQL + "RECOVERY-MONOTRIUM-FAC-DETAILS.php";
        JsonObjectRequest jsonObjectRequest_RecLinkFacility = new JsonObjectRequest(Request.Method.POST, url, jsonObject_visit_data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("responces",  String.valueOf(response.length()) );
                        MainResponce="DONE";

                        try {
                            JSONArray REC_GENERAL = response.getJSONArray("TT-GET-LINK-FACILITY");
                            for (int i = 0; i < REC_GENERAL.length(); i++)
                            {
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
                                sqLiteDatabase_montorium.insert("recovery_link_facility_details", null, contentValues_RecoveryGenreal);

                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        sqLiteDatabase_montorium.close();
                        mCompleteProces="DONE";
                        progressDialog.dismiss();
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
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Recovery_Monotrium_List.this );
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
        jsonObjectRequest_RecLinkFacility.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog.show();
        requestQueue_final.start();
        requestQueue_final.add(jsonObjectRequest_RecLinkFacility);

        //==== Fineash Request

        requestQueue_final.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce = false;
                String mDataCheck="";
                if (MainResponce != null)
                {
                    if (MainResponce.equals("DONE"))
                    {
                        CheckResponce = MainResponce.equals("DONE");
                    }
                }

                if (CheckResponce == true)
                {
                    Log.d("VISIT", "FINISH");
                    progressDialog.dismiss();
                    mNewAdapter.swapCursor(LoadDataNew());
                    mDataRecyView.setAdapter(mNewAdapter);
                }
            }
        });
    }

}
