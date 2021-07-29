package com.AFiMOBILE.afslmobileapplication;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

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
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Recover_Visit_Informastion extends AppCompatActivity {


    private EditText mDatefrom , mDateto ;
    private Button mBtnReqData;
    public int mYear , mMonth , mDay;
    private Spinner mActionCode;
    public RequestQueue requestQueue_final;

    private DatePickerDialog datePickerDialog , dialog , dialogtodate;
    private SqlliteCreateRecovery sqlliteCreateRecovery;
    public ArrayAdapter<String> arrayActionCode;
    private android.app.AlertDialog progressDialog;
    private RecyclerView mRecycleviwer;
    private Adapter_Visit_Data mAdapter;
    private SQLiteDatabase sqLiteDatabase_visit;

    private String PHP_URL_SQL,LoginUser,LoginDate,LoginBranch,LoginUserName , mCompleteProces , MainResponce;
    private int mcount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_visit_informastion);


        //==== Create globle varible
        sqlliteCreateRecovery = new SqlliteCreateRecovery(this);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();

        PHP_URL_SQL = "http://afimobile.abansfinance.lk/mobilephp/";


        progressDialog = new SpotsDialog(Recover_Visit_Informastion.this, R.style.Custom);
        progressDialog.setTitle("Vist Data Synchronization..");

        final Cache cache = new DiskBasedCache(Recover_Visit_Informastion.this.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue_final = new RequestQueue(cache, network);

        mDatefrom   =   (EditText)findViewById(R.id.txtdatefrom);
        mDateto     =   (EditText)findViewById(R.id.txtdateto);
        mBtnReqData = (Button)findViewById(R.id.btnrequestdata);

        //==== Create Date picker ======
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(Recover_Visit_Informastion.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mDatefrom.setText(year + "-" + (month + 1) + "-" + day);
            }
        }, mYear, mMonth, mDay);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDatefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });

        dialogtodate = new DatePickerDialog(Recover_Visit_Informastion.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mDateto.setText(year + "-" + (month + 1) + "-" + day);
            }
        }, mYear, mMonth, mDay);

        dialogtodate.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogtodate.show();

            }
        });

        mActionCode  = (Spinner)findViewById(R.id.spnActionUpdate) ;
        List<String> labelsActon = sqlliteCreateRecovery.GetAllActionCode();
        labelsActon.add("");
        if (labelsActon != null) {
            arrayActionCode = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsActon);
            arrayActionCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mActionCode.setAdapter(arrayActionCode);
            mActionCode.setSelection(arrayActionCode.getPosition(""));
        }

        //=== Recycviwe load the data
        mRecycleviwer  = (RecyclerView)findViewById(R.id.ReyDetailslist) ;
        mRecycleviwer.setHasFixedSize(true);
        mRecycleviwer.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_Visit_Data(this , LoadVistData());
        mRecycleviwer.setAdapter(mAdapter);


        mBtnReqData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetRecoveryVisitData();
            }
        });
    }

    private Cursor LoadVistData ()
    {
        SQLiteDatabase sqLiteDatabase_load_data = sqlliteCreateRecovery.getReadableDatabase();
        Cursor cursor_Load_data = sqLiteDatabase_load_data.rawQuery("SELECT Id_sequence,Action_date,Facility_no,Action_code,Geo_Tag_lon,Get_tag_lat FROM Officer_recoey_visit WHERE Officer_id = '" + LoginUser +
                "'" , null);
        return cursor_Load_data;
    }


    private void GetRecoveryVisitData()
    {
        Log.e("VISIT" , "DONE-1");
        JSONObject jsonObject_visit_data = new JSONObject();
        try {
            jsonObject_visit_data.put("OFFICER_CODE" , LoginUser);
            jsonObject_visit_data.put("DATE_FROM" , mDatefrom.getText().toString());
            jsonObject_visit_data.put("DATE_TO" , mDateto.getText().toString());
            jsonObject_visit_data.put("ACTION_CODE" , mActionCode.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCompleteProces="";

        SQLiteDatabase db = sqlliteCreateRecovery.getWritableDatabase();
        db.execSQL("DELETE FROM Officer_recoey_visit  ");

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
                                contentValues_data.put("Table_id" , jsonObject_REC_GENERAL.getString("ID") );
                                contentValues_data.put("Facility_no" , jsonObject_REC_GENERAL.getString("FACILITY_NO") );
                                contentValues_data.put("Action_code" , jsonObject_REC_GENERAL.getString("ACTIONCODE") );
                                contentValues_data.put("Action_date" , jsonObject_REC_GENERAL.getString("ACTION_DATE") );
                                contentValues_data.put("Vehicle_no" , jsonObject_REC_GENERAL.getString("VEHICLE_NO") );
                                contentValues_data.put("Get_tag_lat" , jsonObject_REC_GENERAL.getString("GEO_TAG_LAT") );
                                contentValues_data.put("Geo_Tag_lon" , jsonObject_REC_GENERAL.getString("GEO_TAG_LONG") );
                                sqLiteDatabase_visit.insert("Officer_recoey_visit" , null , contentValues_data);


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
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Recover_Visit_Informastion.this );
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
                    mAdapter.swapCursor(LoadVistData());
                    mRecycleviwer.setAdapter(mAdapter);

                }
            }
        });









    }
}
