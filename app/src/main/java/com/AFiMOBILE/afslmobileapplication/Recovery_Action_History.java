package com.AFiMOBILE.afslmobileapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

public class Recovery_Action_History extends AppCompatActivity
{

    public String JsonResponcesCode, mInputFacno, PHP_URL_SQL , LoginUser , LoginDate , LoginBranch;
    public SqlliteCreateRecovery sqlliteCreateRecovery_Action_history;
    public TextView mFacno;
    public RequestQueue requestQueue_FileGetFacilityDetails;
    private android.app.AlertDialog progressDialog;
    public RecyclerView RecyDataView;
    public Adapter_Action_History mAdapter;
    public Button mFitchData;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_action_history);

        //=== Get Globle PHP Code
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        PHP_URL_SQL = "http://afimobile.abansfinance.lk/mobilephp/";


        sqlliteCreateRecovery_Action_history    = new SqlliteCreateRecovery(this);
        progressDialog = new SpotsDialog(this, R.style.Custom);

        RecyDataView    =   (RecyclerView)findViewById(R.id.ReyDatView);
        mFitchData      =   (Button)findViewById(R.id.btnFitchData)  ;


        //==== Get Input Facility No
        mFacno  =   (TextView)findViewById(R.id.TxtAssingCaseName);
        Intent intent = getIntent();
        mInputFacno     =   intent.getStringExtra("FACNO");
        mFacno          =   (TextView)findViewById(R.id.TxtAssingCaseName);
        mFacno.setText("Facility No - " + mInputFacno);


        //=== Get Data To Live Server
        mFitchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FitchData();
            }
        });
    }

    protected void onDestroy(){

        sqlliteCreateRecovery_Action_history.close();
        //requestQueue_FileGetFacilityDetails.getCache().clear();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

    public void FitchData()
    {
        JSONObject jsonObjectFacno = new JSONObject();
        try
        {
            jsonObjectFacno.put("FACNO" , mInputFacno);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue_FileGetFacilityDetails  = VollySingleton.getInstance(this).getRequestQueue();
        String url = PHP_URL_SQL + "RECOVERY-GET-FAC-ACTION-HISTORY.php";

        //=== Delete The Table ===
        SQLiteDatabase sqLiteDatabase_RECOVERY_DATA = sqlliteCreateRecovery_Action_history.getWritableDatabase();
        sqLiteDatabase_RECOVERY_DATA.delete("recovery_coapp_guarantor","Facility_Number =?",new String[] {mInputFacno});
        sqLiteDatabase_RECOVERY_DATA.close();

        JsonObjectRequest jsonObjectRequest_MasterData = new JsonObjectRequest(Request.Method.POST, url, jsonObjectFacno,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //=== Get Data Responses
                        Log.d("Log", "RECOVERY-ACTION-HISTORY");

                        try {

                            JSONArray REC_CO_APP  =   response.getJSONArray("TT-REC-ACTION-HIS");
                            //==== Update Recovery Genreal Details

                            String mFacilityNo="" , mActionDate="" , mActionUser="" , mActionCode="" , mActionComment;
                            for (int i = 0; i < REC_CO_APP.length(); i++)
                            {
                                JSONObject JOPRCODE = REC_CO_APP.getJSONObject(i);

                                mFacilityNo      =       JOPRCODE.getString("FACILITY_NO");
                                mActionDate      =       JOPRCODE.getString("ACTION_DATE");
                                mActionUser      =       JOPRCODE.getString("MADE_BY");
                                mActionCode      =       JOPRCODE.getString("ACTIONCODE");
                                mActionComment   =       JOPRCODE.getString("COMMENTS");


                                sqlliteCreateRecovery_Action_history.InsertActionHistory(mFacilityNo,mActionDate,mActionUser,mActionCode,mActionComment);
                            }
                            JsonResponcesCode="DONE";
                            Log.d("Recovery-Action-His", JsonResponcesCode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                JsonResponcesCode="ERROR";
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
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Recovery_Action_History.this);
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
        requestQueue_FileGetFacilityDetails.getCache().clear();
        requestQueue_FileGetFacilityDetails.start();
        requestQueue_FileGetFacilityDetails.add(jsonObjectRequest_MasterData);

        requestQueue_FileGetFacilityDetails.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce=false;
                if (JsonResponcesCode != null)
                {
                    CheckResponce = JsonResponcesCode.equals("DONE");
                }

                if (CheckResponce == true)
                {
                    progressDialog.dismiss();
                    requestQueue_FileGetFacilityDetails.getCache().clear();

                    //=== Recycviwe load the data
                    SQLiteDatabase sqLiteDatabase_getHistory = sqlliteCreateRecovery_Action_history.getReadableDatabase();
                    Cursor cursor_get_his = sqLiteDatabase_getHistory.rawQuery("SELECT ActionDate,ActionUser,ActionCode,ActionComment FROM Recovery_action_history WHERE Facility_no = '" + mInputFacno + "'" , null );
                    if (cursor_get_his.getCount() != 0)
                    {
                        cursor_get_his.moveToFirst();
                        RecyDataView.setHasFixedSize(true);
                        RecyDataView.setLayoutManager(new LinearLayoutManager(Recovery_Action_History.this));
                        mAdapter = new Adapter_Action_History(Recovery_Action_History.this , cursor_get_his);
                        RecyDataView.setAdapter(mAdapter);

                    }
                }
            }
        });
    }
}
