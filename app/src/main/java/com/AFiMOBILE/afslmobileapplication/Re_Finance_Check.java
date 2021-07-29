package com.AFiMOBILE.afslmobileapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import dmax.dialog.SpotsDialog;

public class Re_Finance_Check extends AppCompatActivity {

    private TextView mVehno, mclNic, mFacno,  mOthLeasingamt, mOthBalances;
    private EditText mInvamt, mInvDate, mValDate;
    private Button mOwncheck, mOwnupdate, mOtherUpdate;
    private RadioGroup mSelectType;
    private RadioButton mOwncom, mOthCom;
    private RelativeLayout mLayOwn, mLayOth;
    private String mVehicleNo = "", mFacilityamount = "" , ApplicationJsonResponce="" , LoginUser , LoginDate , LoginBranch;
    private RequestQueue requestQueue_FilecomSts;
    private SqlliteCreateLeasing sqlliteCreateLeasing_refinance;
    private android.app.AlertDialog progressDialog;
    public RecyclerView mRecyFacist;
    private Adapter_Re_Finance_fac_list mAdapter;
    public static String mSelectFacilityno;
    public static TextView mSettlement , mBalances ,mNewFinaance ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_finance_check);


        GlobleClassDetails globleClassDetails = (GlobleClassDetails) Re_Finance_Check.this.getApplicationContext();
        //PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        sqlliteCreateLeasing_refinance = new SqlliteCreateLeasing(this);

        progressDialog = new SpotsDialog(Re_Finance_Check.this, R.style.Custom);
        progressDialog.setTitle("Searching ...");
        progressDialog.setMessage("Searching...");

        mRecyFacist =   (RecyclerView)findViewById(R.id.ReyFacList);
        //=== Recycviwe load the data
        mRecyFacist.setHasFixedSize(true);
        mRecyFacist.setLayoutManager(new LinearLayoutManager(Re_Finance_Check.this));

        mVehno = (TextView) findViewById(R.id.txTVEHNO);
        mSettlement = (TextView) findViewById(R.id.txtsettlement);
        mNewFinaance = (TextView) findViewById(R.id.txtleasingamt);
        mBalances = (TextView) findViewById(R.id.txtbalamt);

        mInvamt = (EditText) findViewById(R.id.txtINVAMT);
        mInvDate = (EditText) findViewById(R.id.txtinvdate);
        mValDate = (EditText) findViewById(R.id.txtVALDATE);
        mOthLeasingamt = (TextView) findViewById(R.id.txtoTH_leasingamt);
        mOthBalances = (TextView) findViewById(R.id.txtOTH_balamt);

        mOwncheck = (Button) findViewById(R.id.btnownSERCH);
        mOwnupdate = (Button) findViewById(R.id.btnownupdate);
        mOtherUpdate = (Button) findViewById(R.id.btn_OTHupdate);

        mSelectType = (RadioGroup) findViewById(R.id.radiotype);
        //mOwncom         =   (RadioButton)findViewById(R.id.radioOwncom);
        //mOthCom         =   (RadioButton)findViewById(R.id.radioOthcom);

        mLayOwn = (RelativeLayout) findViewById(R.id.own_layout);
        mLayOth = (RelativeLayout) findViewById(R.id.relativeOth);

        Intent intent = getIntent();
        mVehicleNo = intent.getStringExtra("VEH_NO");
        mFacilityamount = intent.getStringExtra("FAC_AMT");

        Log.e("VEH_NO", mVehicleNo);
        Log.e("FAC_AMT", mFacilityamount);

        mVehno.setText(mVehicleNo);
        mNewFinaance.setText(mFacilityamount);


        mOwncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSettlementData();
            }
        });

        mSelectType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton mLayout = (RadioButton) findViewById(checkedId);
                int index = mSelectType.indexOfChild(mLayout);

                switch (index) {
                    case 0: // first button
                        mLayOwn.setVisibility(View.VISIBLE);
                        mLayOth.setVisibility(View.GONE);
                        break;
                    case 1: // secondbutton

                        mLayOwn.setVisibility(View.GONE);
                        mLayOth.setVisibility(View.VISIBLE);
                        break;
                }


            }
        });
    }

    protected void onStart() {
        super.onStart();
        mLayOwn.setVisibility(View.VISIBLE);
        mLayOth.setVisibility(View.GONE);
    }


    private void GetSettlementData()
    {
        SQLiteDatabase sqLiteDatabase_refinance = sqlliteCreateLeasing_refinance.getWritableDatabase();

        //=== Delete The Table ===
        sqLiteDatabase_refinance.delete("REFINNCE_SEARC_DATA", "", null);


        JSONObject jsonObject_Get_Refinane = new JSONObject();
        try {
            jsonObject_Get_Refinane.put("REG_NO" , mVehno.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue_FilecomSts   = VollySingleton.getInstance(Re_Finance_Check.this).getRequestQueue();
        String Urlapplication = "http://afimobile.abansfinance.lk/mobilephp/MOBILE-REFINANCE-GET-BAL.php";
        JsonObjectRequest jsonObjectRequestGetRefinance = new JsonObjectRequest(Request.Method.POST, Urlapplication, jsonObject_Get_Refinane,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Refinance" , "Done");
                        JSONArray myjson = new JSONArray();
                        SQLiteDatabase sqLiteDatabase_refinance = sqlliteCreateLeasing_refinance.getWritableDatabase();

                        try {
                            myjson = response.getJSONArray("TT-REFINACE-SETT");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < myjson.length(); i++)
                        {
                            try {
                                JSONObject JOPRCODE = myjson.getJSONObject(i);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("mCLIENT_NAME" , JOPRCODE.getString("Client_Name"));
                                contentValues.put("mPRODUCT" , JOPRCODE.getString("Product_Type"));
                                contentValues.put("mFACILITY_NO" , JOPRCODE.getString("Facility_Number"));
                                contentValues.put("mVEHICLE_NO" , JOPRCODE.getString("Vehicle_Number"));
                                contentValues.put("mFACILITY_AMT" , JOPRCODE.getString("Facility_Amount"));
                                contentValues.put("mSETTLEMENT_AMT" , JOPRCODE.getString("TOTAL_SETTL_AMT"));
                                sqLiteDatabase_refinance.insert("REFINNCE_SEARC_DATA" , null , contentValues);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Re_Finance_Check.this );
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
        jsonObjectRequestGetRefinance.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //=== Add Volly Request
        progressDialog.show();
        requestQueue_FilecomSts.start();
        requestQueue_FilecomSts.add(jsonObjectRequestGetRefinance);

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
                    if(!((Activity) Re_Finance_Check.this).isFinishing())
                    {

                        progressDialog.dismiss();
                        requestQueue_FilecomSts.getCache().clear();

                        mAdapter = new Adapter_Re_Finance_fac_list(Re_Finance_Check.this , GetSerachData() );
                        mRecyFacist.setAdapter(mAdapter);
                    }
                }
            }
        });
    }

    private Cursor GetSerachData()
    {
        SQLiteDatabase sqLiteDatabase_serchdata = sqlliteCreateLeasing_refinance.getReadableDatabase();
        Cursor cursor_serch = sqLiteDatabase_serchdata.rawQuery("SELECT mFACILITY_NO,mSETTLEMENT_AMT,mCLIENT_NAME,mPRODUCT,mVEHICLE_NO,mFACILITY_AMT FROM REFINNCE_SEARC_DATA" +
                " WHERE mVEHICLE_NO LIKE '%" + mVehno.getText().toString() + "%'" , null);

        return cursor_serch;
    }

}
