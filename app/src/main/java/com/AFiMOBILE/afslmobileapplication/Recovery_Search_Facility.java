package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

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

public class Recovery_Search_Facility extends AppCompatActivity
{

    public EditText mClName , mClcode , mFacilityNo , mVehicleNo , mNic , mPostalTown;
    public Button btnSearch , BtnClear , mBtnAddmyWallet;
    public Context mContex;
    public SqlliteCreateRecovery sqlliteCreateRecovery_DataSearch;
    public RequestQueue requestQueue_Dataserach;
    public String PHP_URL_SQL , MainResponceCode , LoginUser , LoginDate , LoginBranch , mInputFacilityNo ;
    private android.app.AlertDialog progressDialog;
    public RecyclerView mRecyFacist;
    public static Adapter_Recover_FacList mAdapter;
    public static String mRecoverySearchSelectFacilityNo;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public Switch mOnlineSearch;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_search_facility);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) Recovery_Search_Facility.this.getApplicationContext();
        //PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        PHP_URL_SQL = "http://afimobile.abansfinance.lk/mobilephp/";

        sqlliteCreateRecovery_DataSearch = new SqlliteCreateRecovery(this);
        Cache cache = new DiskBasedCache(this.getCacheDir(), 1024 * 1024);

        Network network = new BasicNetwork(new HurlStack());
        requestQueue_Dataserach = new RequestQueue(cache, network);
        requestQueue_Dataserach.getCache().clear();

        mOnlineSearch   =       (Switch)findViewById(R.id.Swtonline);

        mRecyFacist =   (RecyclerView)findViewById(R.id.ReyFacList);
        //=== Recycviwe load the data
        mRecyFacist.setHasFixedSize(true);
        mRecyFacist.setLayoutManager(new LinearLayoutManager(Recovery_Search_Facility.this));

        mClName         =       (EditText) findViewById(R.id.TxtSerchClname);
        mClcode         =       (EditText) findViewById(R.id.TxtSerchClcode);
        mFacilityNo     =       (EditText) findViewById(R.id.TxtSerchFacno);
        mVehicleNo      =       (EditText) findViewById(R.id.TxtSerchRegNo);
        mNic            =       (EditText) findViewById(R.id.TxtSerchnic);
        mPostalTown     =       (EditText) findViewById(R.id.TxtSerchPostTown);

        mClName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mClcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mFacilityNo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mVehicleNo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mNic.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mPostalTown.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        Intent intent = getIntent();
        mInputFacilityNo    =   intent.getStringExtra("FacilityNo");

        if (mInputFacilityNo.toString().equals(""))
        {
            mFacilityNo.setText("");
        }
        else
        {
            mFacilityNo.setText(mInputFacilityNo);
            mOnlineSearch.setChecked(true);
        }

        BtnClear  = (Button)findViewById(R.id.btnClear);
        BtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearData();
            }
        });

        btnSearch       =       (Button)findViewById(R.id.btnSerachFacdetails);

        mBtnAddmyWallet = (Button)findViewById(R.id.btnAddCase);


        mOnlineSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    mBtnAddmyWallet.setEnabled(true);
                    mBtnAddmyWallet.setBackgroundResource(R.drawable.normalbutton);
                }
                else
                {
                    mBtnAddmyWallet.setEnabled(false);
                    mBtnAddmyWallet.setBackgroundResource(R.drawable.normalbuttondisable);
                }
            }
        });

        if (mOnlineSearch.isChecked())
        {
            mBtnAddmyWallet.setEnabled(true);
            mBtnAddmyWallet.setBackgroundResource(R.drawable.normalbutton);
        }
        else
        {
            mBtnAddmyWallet.setEnabled(false);
            mBtnAddmyWallet.setBackgroundResource(R.drawable.normalbuttondisable);
        }

        checkDataConnectionStatus = new CheckDataConnectionStatus(this);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnlineSearch.isChecked())
                {
                    if (checkDataConnectionStatus.IsConnected() == true)
                    {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        GetSearchData();
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Search_Facility.this);
                        builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                        builder.setMessage("Data Connection Not available. Please Turn on Connection.");
                        builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                }
                else
                {
                    OfflineSearch();
                }
            }
        });

        //=== Add My Wallet

        mBtnAddmyWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRecoverySearchSelectFacilityNo != null)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Search_Facility.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to add this Facility?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AddFacilityMyWallet();
                        }
                    });
                    builderdelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked
                            Toast.makeText(getApplicationContext(),
                                    "No Button Clicked",Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builderdelete.create();
                    dialog.show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Selected Facility.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void AddFacilityMyWallet()
    {
        if (mRecoverySearchSelectFacilityNo != "")
        {
            SQLiteDatabase sqLiteDatabase_DataInsert = sqlliteCreateRecovery_DataSearch.getWritableDatabase();
            Cursor cursor_insertData = sqLiteDatabase_DataInsert.rawQuery("SELECT * FROM recovery_search_data WHERE Facility_Number =  '" + mRecoverySearchSelectFacilityNo + "'" , null);
            if (cursor_insertData.getCount() !=0)
            {
                cursor_insertData.moveToFirst();
                ContentValues contentValues_RecoveryGenreal = new ContentValues();
                contentValues_RecoveryGenreal.put("Client_Name", cursor_insertData.getString(0));
                contentValues_RecoveryGenreal.put("Product_Type", cursor_insertData.getString(1));
                contentValues_RecoveryGenreal.put("Asset_Model", cursor_insertData.getString(2));
                contentValues_RecoveryGenreal.put("Vehicle_Number", cursor_insertData.getString(3));
                contentValues_RecoveryGenreal.put("Due_Day", cursor_insertData.getString(4));
                contentValues_RecoveryGenreal.put("Current_Month_Rental", cursor_insertData.getString(5));
                contentValues_RecoveryGenreal.put("OD_Arrear_Amount", cursor_insertData.getString(6));
                contentValues_RecoveryGenreal.put("Total_Arrear_Amount", cursor_insertData.getString(7));
                contentValues_RecoveryGenreal.put("Last_Payment_Date", cursor_insertData.getString(8));
                contentValues_RecoveryGenreal.put("Last_Payment_Amount", cursor_insertData.getString(9));
                contentValues_RecoveryGenreal.put("Facility_Payment_Status", cursor_insertData.getString(10));
                contentValues_RecoveryGenreal.put("Letter_Demand_Date", cursor_insertData.getString(11));
                contentValues_RecoveryGenreal.put("Client_Code", cursor_insertData.getString(12));
                contentValues_RecoveryGenreal.put("NIC", cursor_insertData.getString(13));
                contentValues_RecoveryGenreal.put("Customer_Full_Name", cursor_insertData.getString(14));
                contentValues_RecoveryGenreal.put("Gender", cursor_insertData.getString(15));
                contentValues_RecoveryGenreal.put("Occupation", cursor_insertData.getString(16));
                contentValues_RecoveryGenreal.put("Work_Place_Name", cursor_insertData.getString(17));
                contentValues_RecoveryGenreal.put("Work_Place_Address", cursor_insertData.getString(18));
                contentValues_RecoveryGenreal.put("Mobile_No1", cursor_insertData.getString(19));
                contentValues_RecoveryGenreal.put("Mobile_No2", cursor_insertData.getString(20));
                contentValues_RecoveryGenreal.put("Home_No", cursor_insertData.getString(21));
                contentValues_RecoveryGenreal.put("C_Address_Line_1", cursor_insertData.getString(22));
                contentValues_RecoveryGenreal.put("C_Address_Line_2", cursor_insertData.getString(23));
                contentValues_RecoveryGenreal.put("C_Address_Line_3", cursor_insertData.getString(24));
                contentValues_RecoveryGenreal.put("C_Address_Line_4", cursor_insertData.getString(25));
                contentValues_RecoveryGenreal.put("C_Postal_Town", cursor_insertData.getString(26));
                contentValues_RecoveryGenreal.put("P_Address_Line_1", cursor_insertData.getString(27));
                contentValues_RecoveryGenreal.put("P_Address_Line_2", cursor_insertData.getString(28));
                contentValues_RecoveryGenreal.put("P_Address_Line_3", cursor_insertData.getString(29));
                contentValues_RecoveryGenreal.put("P_Address_Line_4", cursor_insertData.getString(30));
                contentValues_RecoveryGenreal.put("P_Postal_Town", cursor_insertData.getString(31));
                contentValues_RecoveryGenreal.put("Facility_Number", cursor_insertData.getString(32));
                contentValues_RecoveryGenreal.put("Facility_Amount", cursor_insertData.getString(33));
                contentValues_RecoveryGenreal.put("Facility_Branch", cursor_insertData.getString(34));
                contentValues_RecoveryGenreal.put("Tenure", cursor_insertData.getString(35));
                contentValues_RecoveryGenreal.put("Recovery_Executive", LoginUser);
                contentValues_RecoveryGenreal.put("Recovery_Executive_Name", cursor_insertData.getString(37));
                contentValues_RecoveryGenreal.put("CC_Executive", cursor_insertData.getString(38));
                contentValues_RecoveryGenreal.put("Marketing_Executive", cursor_insertData.getString(39));
                contentValues_RecoveryGenreal.put("Follow_Up_Officer", cursor_insertData.getString(40));
                contentValues_RecoveryGenreal.put("Recovery_Executive_Phone", cursor_insertData.getString(41));
                contentValues_RecoveryGenreal.put("CC_Executive_Phone", cursor_insertData.getString(42));
                contentValues_RecoveryGenreal.put("Marketing_Executive_Phone", cursor_insertData.getString(43));
                contentValues_RecoveryGenreal.put("Follow_Up_Officer_Phone", cursor_insertData.getString(44));
                contentValues_RecoveryGenreal.put("Activation_Date", cursor_insertData.getString(45));
                contentValues_RecoveryGenreal.put("Expiry_Date", cursor_insertData.getString(46));
                contentValues_RecoveryGenreal.put("Total_Facility_Amount", cursor_insertData.getString(47));
                contentValues_RecoveryGenreal.put("Disbursed_Amount", cursor_insertData.getString(48));
                contentValues_RecoveryGenreal.put("Disbursement_Status", cursor_insertData.getString(49));
                contentValues_RecoveryGenreal.put("Interest_Rate", cursor_insertData.getString(50));
                contentValues_RecoveryGenreal.put("Rental", cursor_insertData.getString(51));
                contentValues_RecoveryGenreal.put("Contract_Status", cursor_insertData.getString(52));
                contentValues_RecoveryGenreal.put("Facility_Status", cursor_insertData.getString(53));
                contentValues_RecoveryGenreal.put("Down_Payment_No", cursor_insertData.getString(54));
                contentValues_RecoveryGenreal.put("Rentals_Matured_No", cursor_insertData.getString(55));
                contentValues_RecoveryGenreal.put("Rentals_Paid_No", cursor_insertData.getString(56));
                contentValues_RecoveryGenreal.put("Arrears_Rental_No", cursor_insertData.getString(57));
                contentValues_RecoveryGenreal.put("SPDC_Available", cursor_insertData.getString(58));
                contentValues_RecoveryGenreal.put("Total_Outstanding", cursor_insertData.getString(59));
                contentValues_RecoveryGenreal.put("Capital_Outstanding", cursor_insertData.getString(60));
                contentValues_RecoveryGenreal.put("Interest_Outstanding", cursor_insertData.getString(61));
                contentValues_RecoveryGenreal.put("Arrears_Rental_Amount", cursor_insertData.getString(62));
                contentValues_RecoveryGenreal.put("OD_Interest", cursor_insertData.getString(63));
                contentValues_RecoveryGenreal.put("Interest_Arrears", cursor_insertData.getString(64));
                contentValues_RecoveryGenreal.put("Insurance_Arrears", cursor_insertData.getString(65));
                contentValues_RecoveryGenreal.put("OD_Interest_OS", cursor_insertData.getString(66));
                contentValues_RecoveryGenreal.put("Seizing_Charges", cursor_insertData.getString(67));
                contentValues_RecoveryGenreal.put("OC_Arrears", cursor_insertData.getString(68));
                contentValues_RecoveryGenreal.put("FUTURE_CAPITAL", cursor_insertData.getString(69));
                contentValues_RecoveryGenreal.put("FUTURE_INTEREST", cursor_insertData.getString(70));
                contentValues_RecoveryGenreal.put("TOTAL_SETTL_AMT", cursor_insertData.getString(71));
                sqLiteDatabase_DataInsert.insert("recovery_generaldetail" , null , contentValues_RecoveryGenreal);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                builder.setMessage("Facility Upload Successfully ");
                builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
                sqLiteDatabase_DataInsert.close();
            }
        }

    }


    public void ClearData()
    {
        mClName.setText("");  mClcode.setText("");  mFacilityNo.setText(""); mVehicleNo.setText("");
        mNic.setText(""); mPostalTown.setText("");
    }


    public void OfflineSearch()
    {

        SQLiteDatabase sqLiteDatabase_offlinesearch = sqlliteCreateRecovery_DataSearch.getReadableDatabase();
        if (mVehicleNo.getText().toString().equals(""))
        {
            Cursor cursor_offline = sqLiteDatabase_offlinesearch.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                    "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Client_Name = '" + mClName.getText() + "' and Client_Name <> '' or " +
                    "Client_Code = '" + mClcode.getText() + "' and Client_Code <> '' or Facility_Number = '" + mFacilityNo.getText() + "' and Facility_Number <> '' or NIC = '" + mNic.getText() + "' and NIC <> '' or " +
                    "P_Postal_Town = '" + mPostalTown.getText() + "' and P_Postal_Town <> ''" , null );
            if (cursor_offline.getCount() != 0 )
            {
               mAdapter = new Adapter_Recover_FacList(Recovery_Search_Facility.this , cursor_offline , "NORMAL");
               mRecyFacist.setAdapter(mAdapter);
            }
        }
        else
        {
            Cursor cursor_offline = sqLiteDatabase_offlinesearch.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                    "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Client_Name = '" + mClName.getText() + "' and Client_Name <> '' or " +
                    "Client_Code = '" + mClcode.getText() + "' and Client_Code <> '' or Facility_Number = '" + mFacilityNo.getText() + "' and Facility_Number <> '' or NIC = '" + mNic.getText() + "' and NIC <> '' or " +
                    "P_Postal_Town = '" + mPostalTown.getText() + "' and P_Postal_Town <> '' or Vehicle_Number like '%" + mVehicleNo.getText() + "%'" , null );
            if (cursor_offline.getCount() != 0 )
            {
                mAdapter = new Adapter_Recover_FacList(Recovery_Search_Facility.this , cursor_offline , "NORMAL");
                mRecyFacist.setAdapter(mAdapter);
            }
        }
    }

    public void GetSearchData()
    {
        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setTitle("Searching Data ...");


        JSONObject jsonObjecSerchField = new JSONObject();
        try {
            jsonObjecSerchField.put("CLNAME" , mClName.getText());
            jsonObjecSerchField.put("CLCODE" , mClcode.getText());
            jsonObjecSerchField.put("CLFACNO" , mFacilityNo.getText());
            jsonObjecSerchField.put("CLVEHNO" , mVehicleNo.getText());
            jsonObjecSerchField.put("CLNIC" , mNic.getText());
            jsonObjecSerchField.put("CLTOWN" , mPostalTown.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = PHP_URL_SQL + "RECOVERY-SEARCH-DATA.php";
        JsonObjectRequest jsonObjectRequest_RequestSearchData = new JsonObjectRequest(Request.Method.POST, url, jsonObjecSerchField,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //=== Get Data Responses
                        Log.d("SEARCH-FAC", "START");
                        try {

                            String mCheckData = response.getString("TT-SEARCH-DATA");
                            if (mCheckData.equals("null"))
                            {
                                MainResponceCode = "EMPTY";
                            }
                            else
                            {
                                JSONArray REC_GENERAL = response.getJSONArray("TT-SEARCH-DATA");
                                SQLiteDatabase sqLiteDatabase_RECOVERY_DATA = sqlliteCreateRecovery_DataSearch.getWritableDatabase();

                                //=== Delete The Table ===
                                sqLiteDatabase_RECOVERY_DATA.delete("recovery_search_data", "", null);

                                //=== Delete old Data
                                SQLiteDatabase db = sqlliteCreateRecovery_DataSearch.getWritableDatabase();
                                db.execSQL("DELETE FROM recovery_search_data WHERE Facility_Number <> '' ");
                                //db.close();

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
                                Log.d("RECOVERY_GEN", "DONE");

                                sqLiteDatabase_RECOVERY_DATA.close();

                                Log.d("RECOVERY-DATA", "DONE");
                                MainResponceCode = "DONE";
                                progressDialog.dismiss();
                            }
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
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Recovery_Search_Facility.this);
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
        jsonObjectRequest_RequestSearchData.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        //==== Sett the Responces
        progressDialog.setTitle("Recovery Data Process - Please Wait.");
        progressDialog.show();
        requestQueue_Dataserach.start();
        requestQueue_Dataserach.add(jsonObjectRequest_RequestSearchData);

        //==== Fineash Request
        requestQueue_Dataserach.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
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
                    else if (MainResponceCode.equals("EMPTY"))
                    {
                        CheckResponce = true;
                        mDataCheck = "EMPTY";
                    }
                }

                if (CheckResponce == true)
                {
                    progressDialog.dismiss();
                    sqlliteCreateRecovery_DataSearch.close();
                    requestQueue_Dataserach.getCache().clear();

                    if (mDataCheck.equals("EMPTY"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Search_Facility.this);
                        builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                        builder.setMessage("Record Not found.");
                        builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                    else
                    {
                        mAdapter = new Adapter_Recover_FacList(Recovery_Search_Facility.this , GetFacilityListData() , "SEARCH");
                        mRecyFacist.setAdapter(mAdapter);
                    }
                }
            }
        });
    }

    public Cursor GetFacilityListData()
    {
        SQLiteDatabase sqLiteDatabase_FacList = sqlliteCreateRecovery_DataSearch.getReadableDatabase();
        Cursor cursor_FacList=null;

        if (mVehicleNo.getText().toString().equals(""))
        {
            cursor_FacList = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                    "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_search_data WHERE Client_Name = '" + mClName.getText() + "' and Client_Name <> '' or " +
                    "Client_Code = '" + mClcode.getText() + "' and Client_Code <> '' or Facility_Number like '%" + mFacilityNo.getText() + "%' and Facility_Number <> '' or NIC = '" + mNic.getText() + "' and NIC <> '' or " +
                    "P_Postal_Town = '" + mPostalTown.getText() + "' and P_Postal_Town <> ''" , null );

        }
        else
        {
            cursor_FacList = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                    "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_search_data WHERE Client_Name = '" + mClName.getText() + "' and Client_Name <> '' or " +
                    "Client_Code = '" + mClcode.getText() + "' and Client_Code <> '' or Facility_Number like '%" + mFacilityNo.getText() + "%' and Facility_Number <> '' or NIC = '" + mNic.getText() + "' and NIC <> '' or " +
                    "P_Postal_Town = '" + mPostalTown.getText() + "' and P_Postal_Town <> '' or Vehicle_Number like '%" + mVehicleNo.getText() + "%'" , null );

        }
        return cursor_FacList;
    }

}
