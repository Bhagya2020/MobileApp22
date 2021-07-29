package com.AFiMOBILE.afslmobileapplication;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class Update_Travel_distance extends AppCompatActivity {

    private TextView mDateOfVisit;
    private Button mBtnRequestData , mUpdateData;
    private String mDate;
    private String MainResponce , mCompleteProces , LoginUser, LoginDate , LoginBranch ,  LoginUserName , PHP_URL_SQL;
    private SqlliteCreateRecovery sqlliteCreateRecovery;
    private SQLiteDatabase sqLiteDatabase_visit;
    private int mcount;
    private android.app.AlertDialog progressDialog;
    private RequestQueue requestQueue_final;
    private RecyclerView mRecycleviwer;
    private Adapter_distance_visit mAdapter;
    private EditText mIntime , mOutTime , mfoodallowance , mTotalDistance;
    public View layout_sucress , layout_error;
    public TextView tv , tv_sucess , Facilityno;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_travel_distance);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();
        PHP_URL_SQL = "http://afimobile.abansfinance.lk/mobilephp/";

        mIntime     =   (EditText)findViewById(R.id.TxtinTime) ;
        mOutTime    =   (EditText)findViewById(R.id.TxtOutTime) ;
        mfoodallowance  =   (EditText)findViewById(R.id.TxtFoodAllowance) ;
        mTotalDistance  =   (EditText)findViewById(R.id.TxtTotDistance) ;
        mUpdateData     =   (Button)findViewById(R.id.btnupdate);

        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);


        mIntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(Update_Travel_distance.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mIntime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        mOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(Update_Travel_distance.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mOutTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        sqlliteCreateRecovery = new SqlliteCreateRecovery(this);
        progressDialog = new SpotsDialog(Update_Travel_distance.this, R.style.Custom);
        progressDialog.setTitle("Vist Data Synchronization..");

        final Cache cache = new DiskBasedCache(Update_Travel_distance.this.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue_final = new RequestQueue(cache, network);


        //=== Get Input Date
        Intent intent = getIntent();
        mDate    =   intent.getStringExtra("INPUT-DATA");

        mDateOfVisit    =   (TextView)findViewById(R.id.TxtDateTraveld);
        mBtnRequestData =   (Button)findViewById(R.id.btnrequestvisitdata);

        mDateOfVisit.setText(mDate);

        //=== Recycviwe load the data
        mRecycleviwer  = (RecyclerView)findViewById(R.id.ReyDetailslist) ;
        mRecycleviwer.setHasFixedSize(true);
        mRecycleviwer.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_distance_visit (this , LoadData());
        //mRecycleviwer.setAdapter(mAdapter);

        mBtnRequestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetRecoveryVisitData();
            }
        });

        //==== Save Data ====
        mUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });
    }

    private void SaveData()
    {
        if (mIntime.getText().toString().trim().length() == 0)
        {
            tv.setText("<Time In is blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mOutTime.getText().toString().trim().length() == 0)
        {
            tv.setText("<Time Out is blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mTotalDistance.getText().toString().trim().length() ==0)
        {
            tv.setText("<Total Distance  is blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            String Faclist="";
            Cursor cursor_get_facility_no = LoadData();
            cursor_get_facility_no.moveToFirst();
            do {
                Faclist = Faclist + cursor_get_facility_no.getString(1) + ",";
            }while (cursor_get_facility_no.moveToNext());


            SQLiteDatabase sqLiteDatabase_distance_update = sqlliteCreateRecovery.getWritableDatabase();
            ContentValues contentValues_Dis_update = new ContentValues();
            contentValues_Dis_update.put("facility_no" , Faclist);
            contentValues_Dis_update.put("user_id" , LoginUser);
            contentValues_Dis_update.put("ent_date" , LoginDate);
            contentValues_Dis_update.put("allowance_km_lkr" , "");
            contentValues_Dis_update.put("food_allow" , mfoodallowance.getText().toString());
            contentValues_Dis_update.put("no_distance_km" , mTotalDistance.getText().toString());
            contentValues_Dis_update.put("in_time" , mIntime.getText().toString());
            contentValues_Dis_update.put("out_time" , mOutTime.getText().toString());
            contentValues_Dis_update.put("visit_date" , mDate);
            contentValues_Dis_update.put("Lock_Record" , "Y");
            contentValues_Dis_update.put("Live_Server_update" , "");
            sqLiteDatabase_distance_update.insert("recovery_distance_data" , null , contentValues_Dis_update);

            tv_sucess.setText("Record Successfully Saved.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_sucress);
            toast.show();

            mfoodallowance.setText("");
            mTotalDistance.setText("");
            mIntime.setText("");
            mOutTime.setText("");
        }

    }

    private Cursor LoadData()
    {
        SQLiteDatabase sqLiteDatabase_loaddata = sqlliteCreateRecovery.getReadableDatabase();
        Cursor cursor_load_data = sqLiteDatabase_loaddata.rawQuery("SELECT Id_sequence , Facility_no , Action_code FROM Distance_Details WHERE Officer_id = '" + LoginUser + "' AND DATE (Action_date) = '" + mDate + "'", null);
        return cursor_load_data;
    }

    private void GetRecoveryVisitData()
    {
        Log.e("VISIT" , "DONE-1");
        JSONObject jsonObject_visit_data = new JSONObject();
        try {
            jsonObject_visit_data.put("OFFICER_CODE" , LoginUser);
            jsonObject_visit_data.put("DATE_FROM" , mDate);
            jsonObject_visit_data.put("DATE_TO" , mDate);
            jsonObject_visit_data.put("ACTION_CODE" , "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCompleteProces="";

        SQLiteDatabase db = sqlliteCreateRecovery.getWritableDatabase();
        db.execSQL("DELETE FROM Distance_Details  ");

        String url = PHP_URL_SQL + "RECOVERY-GET-VISIT-DATA.php";
        JsonObjectRequest jsonObjectRequest_RecRequestTrevelling = new JsonObjectRequest(Request.Method.POST, url, jsonObject_visit_data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("responces",  String.valueOf(response.length()) );
                        MainResponce="DONE";

                        sqLiteDatabase_visit = sqlliteCreateRecovery.getWritableDatabase();

                        try {
                            JSONArray REC_GENERAL = response.getJSONArray("TT-GET-VISIT-DATA");
                            for (int i = 0; i < REC_GENERAL.length(); i++)
                            {
                                mcount = mcount + 1;
                                Log.e("count" , String.valueOf(mcount));
                                JSONObject jsonObject_REC_GENERAL = REC_GENERAL.getJSONObject(i);
                                ContentValues contentValues_data = new ContentValues();
                                contentValues_data.put("Officer_id" , jsonObject_REC_GENERAL.getString("MADE_BY") );
                                contentValues_data.put("Id_sequence" , String.valueOf(mcount) );
                                contentValues_data.put("Facility_no" , jsonObject_REC_GENERAL.getString("FACILITY_NO") );
                                contentValues_data.put("Action_code" , jsonObject_REC_GENERAL.getString("ACTIONCODE") );
                                contentValues_data.put("Action_date" , jsonObject_REC_GENERAL.getString("ACTION_DATE") );
                                sqLiteDatabase_visit.insert("Distance_Details" , null , contentValues_data);
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

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
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Update_Travel_distance.this );
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
                50000,
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
                    mAdapter.swapCursor(LoadData());
                    mRecycleviwer.setAdapter(mAdapter);
                }
            }
        });

    }



}
