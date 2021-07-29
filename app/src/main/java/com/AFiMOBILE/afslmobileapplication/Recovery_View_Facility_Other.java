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

import androidx.annotation.Nullable;
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

public class Recovery_View_Facility_Other extends AppCompatActivity {

    public String mInputType , mInputRecFacno , PHP_URL_SQL , JsonResponcesCode;
    public TextView mViewInputType , mInputFacilityNo;
    public SqlliteCreateRecovery sqlliteCreateRecovery_ViewDetails;
    public RequestQueue requestQueue_FileGetFacilityDetails;
    private android.app.AlertDialog progressDialog;
    public RecyclerView RecyDataView;
    public Adapter_Facility_Payment mAdapter_Payment;
    public Adapter_Facility_Notepaid mAdapter_Note;
    public Adapter_Recover_CoDetails mAdapter_CoApp;
    public Button mDataFitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_view_facility_other);

        sqlliteCreateRecovery_ViewDetails = new SqlliteCreateRecovery(this);
        //GlobleClassDetails globleClassDetails = (GlobleClassDetails) Recovery_View_Facility_Other.this.getApplicationContext();
        //PHP_URL_SQL = globleClassDetails.getPHP_Path();

        PHP_URL_SQL = "http://afimobile.abansfinance.lk/mobilephp/";


        progressDialog = new SpotsDialog(this, R.style.Custom);
        RecyDataView = (RecyclerView)findViewById(R.id.ReyDatView);
        mDataFitch = (Button)findViewById(R.id.btnFitchData);

        Intent intent = getIntent();
        mInputType          =   intent.getStringExtra("TYPE");
        mInputRecFacno      =   intent.getStringExtra("FACNO");

        mViewInputType      =   (TextView)findViewById(R.id.TxtDetailsView);
        mInputFacilityNo    =   (TextView)findViewById(R.id.TxtAssingCaseName);

        mInputFacilityNo.setText("Facility No - " + mInputRecFacno);
        mViewInputType.setText(mInputType + " Information");

        mDataFitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (mInputType.equals("PAYMENT"))
                {
                    GetFacilityPayment();
                }
                else if (mInputType.equals("NOTE"))
                {
                    GetNotePaid();
                }
                else if (mInputType.equals("COAPP"))
                {
                    GetCoAppDetais();
                }
            }
        });

        //==== To load the screen load the save data
        SQLiteDatabase sqLiteDatabase = sqlliteCreateRecovery_ViewDetails.getReadableDatabase();
        if (mInputType.equals("NOTE"))
        {
            Cursor cursor_Note = sqLiteDatabase.rawQuery("SELECT NOTE_DATE , EXECUTIVE_ID , NOTE_DESC FROM recovery_notepad_history WHERE FACILITY_NO = '" + mInputRecFacno +"' ORDER BY NOTE_DATE DESC " , null);
            if(cursor_Note.getCount() != 0)
            {
                cursor_Note.moveToFirst();
                RecyDataView.setHasFixedSize(true);
                RecyDataView.setLayoutManager(new LinearLayoutManager(Recovery_View_Facility_Other.this));
                mAdapter_Note = new Adapter_Facility_Notepaid(Recovery_View_Facility_Other.this , cursor_Note);
                RecyDataView.setAdapter(mAdapter_Note);
            }
        }
        else if (mInputType.equals("PAYMENT"))
        {
            Cursor cursor_Payment = sqLiteDatabase.rawQuery("SELECT Payment_Date_Time , Receipt_Number , Amount , Payment_Mode FROM recovery_paymentdetail WHERE Facility_Number = '" + mInputRecFacno + "' ORDER BY Payment_Date_Time" , null );
            if(cursor_Payment.getCount() != 0)
            {
                cursor_Payment.moveToFirst();
                RecyDataView.setHasFixedSize(true);
                RecyDataView.setLayoutManager(new LinearLayoutManager(Recovery_View_Facility_Other.this));
                mAdapter_Payment = new Adapter_Facility_Payment(Recovery_View_Facility_Other.this , cursor_Payment);
                RecyDataView.setAdapter(mAdapter_Payment);
            }
        }
        else if (mInputType.equals("COAPP"))
        {
            Cursor cursor_Coapp = sqLiteDatabase.rawQuery("SELECT Facility_Number , Relationship_Applicant , Customer_Full_Name , C_Address_Line_1 , " +
                    "C_Address_Line_2 , C_Address_Line_3 , C_Address_Line_4 , NIC , Client_Code , Gender , Mobile_No1 , Occupation  FROM recovery_coapp_guarantor " +
                    "WHERE Facility_Number = '" + mInputRecFacno + "'" , null);
            if(cursor_Coapp.getCount() != 0)
            {
                cursor_Coapp.moveToFirst();
                RecyDataView.setHasFixedSize(true);
                RecyDataView.setLayoutManager(new LinearLayoutManager(Recovery_View_Facility_Other.this));
                mAdapter_CoApp = new Adapter_Recover_CoDetails(Recovery_View_Facility_Other.this , cursor_Coapp);
                RecyDataView.setAdapter(mAdapter_CoApp);
            }
        }

    }

    //=== Get Co Applicartion Details
    public void GetCoAppDetais()
    {
        JSONObject jsonObjectFacno = new JSONObject();
        try
        {
            jsonObjectFacno.put("FACNO" , mInputRecFacno);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue_FileGetFacilityDetails  = VollySingleton.getInstance(this).getRequestQueue();
        //String url = PHP_URL_SQL + "RECOVERY-GET-FAC-COAPP.php";
        String url = "http://203.115.12.125:82/core/mobnew/RECOVERY-GET-COAPP.php";

        JsonObjectRequest jsonObjectRequest_MasterData = new JsonObjectRequest(Request.Method.POST, url, jsonObjectFacno,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //=== Get Data Responses
                        Log.d("Log", "RECOVERY-COAPPLICANT");

                        try {
                            //=== Delete The Table ===
                            SQLiteDatabase sqLiteDatabase_RECOVERY_DATA = sqlliteCreateRecovery_ViewDetails.getWritableDatabase();
                            sqLiteDatabase_RECOVERY_DATA.delete("recovery_coapp_guarantor","Facility_Number =?",new String[] {mInputRecFacno});

                            JSONArray REC_CO_APP  =   response.getJSONArray("TT-REC-FAC-COAPP");
                            //==== Update Recovery Genreal Details
                            String Facility_Number , Relationship_Applicant , Customer_Full_Name , C_Address_Line_1 , C_Address_Line_2 , C_Address_Line_3 ,
                                    C_Address_Line_4 , NIC , Client_Code , Gender , Mobile_No1 , Mobile_No2 , Occupation;

                            for (int i = 0; i < REC_CO_APP.length(); i++)
                            {
                                JSONObject JOPRCODE = REC_CO_APP.getJSONObject(i);

                                Facility_Number             =       JOPRCODE.getString("Facility_Number");
                                Relationship_Applicant      =       JOPRCODE.getString("Relationship_Applicant");
                                Customer_Full_Name          =       JOPRCODE.getString("Customer_Full_Name");
                                C_Address_Line_1            =       JOPRCODE.getString("C_Address_Line_1");
                                C_Address_Line_2            =       JOPRCODE.getString("C_Address_Line_2");
                                C_Address_Line_3            =       JOPRCODE.getString("C_Address_Line_3");
                                C_Address_Line_4            =       JOPRCODE.getString("C_Address_Line_4");
                                NIC                         =       JOPRCODE.getString("NIC");
                                Client_Code                 =       JOPRCODE.getString("Client_Code");
                                Gender                      =       JOPRCODE.getString("Gender");
                                Mobile_No1                  =       JOPRCODE.getString("Mobile_No1");
                                Mobile_No2                  =       JOPRCODE.getString("Mobile_No2");
                                Occupation                  =       JOPRCODE.getString("Occupation");


                                sqlliteCreateRecovery_ViewDetails.InsertCoAppDetails(Facility_Number , Relationship_Applicant , Customer_Full_Name , C_Address_Line_1 , C_Address_Line_2 ,
                                        C_Address_Line_3 , C_Address_Line_4 , NIC , Client_Code , Gender , Mobile_No1 , Mobile_No2 , Occupation );
                            }
                            JsonResponcesCode="DONE";
                            sqLiteDatabase_RECOVERY_DATA.close();
                            Log.d("Recovery-Note", JsonResponcesCode);
                            sqLiteDatabase_RECOVERY_DATA.close();

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
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Recovery_View_Facility_Other.this );
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
                15000,
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
                    SQLiteDatabase sqLiteDatabase_Coapp = sqlliteCreateRecovery_ViewDetails.getReadableDatabase();
                    Cursor cursor_Coapp = sqLiteDatabase_Coapp.rawQuery("SELECT Facility_Number , Relationship_Applicant , Customer_Full_Name , C_Address_Line_1 , " +
                            "C_Address_Line_2 , C_Address_Line_3 , C_Address_Line_4 , NIC , Client_Code , Gender , Mobile_No1 , Occupation  FROM recovery_coapp_guarantor " +
                            "WHERE Facility_Number = '" + mInputRecFacno + "'" , null);
                    if(cursor_Coapp.getCount() != 0)
                    {
                        cursor_Coapp.moveToFirst();
                        RecyDataView.setHasFixedSize(true);
                        RecyDataView.setLayoutManager(new LinearLayoutManager(Recovery_View_Facility_Other.this));
                        mAdapter_CoApp = new Adapter_Recover_CoDetails(Recovery_View_Facility_Other.this , cursor_Coapp);
                        RecyDataView.setAdapter(mAdapter_CoApp);
                    }

                }
            }
        });


    }

    public void GetNotePaid()
    {
        JSONObject jsonObjectFacno = new JSONObject();
        try
        {
            jsonObjectFacno.put("FACNO" , mInputRecFacno);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue_FileGetFacilityDetails  = VollySingleton.getInstance(this).getRequestQueue();
        //String url = PHP_URL_SQL + "RECOVERY-GET-FAC-NOTE.php";
        String url = "http://203.115.12.125:82/core/mobnew/RECOVERY-GET-NOTEPAID.php";


        JsonObjectRequest jsonObjectRequest_MasterData = new JsonObjectRequest(Request.Method.POST, url, jsonObjectFacno,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //=== Get Data Responses
                        Log.d("Log", "RECOVERY-NOTEPAID");

                        try {
                            //=== Delete The Table ===
                            SQLiteDatabase sqLiteDatabase_RECOVERY_DATA = sqlliteCreateRecovery_ViewDetails.getWritableDatabase();
                            sqLiteDatabase_RECOVERY_DATA.delete("recovery_notepad_history","FACILITY_NO =?",new String[] {mInputRecFacno});

                            JSONArray REC_PAYMENT  =   response.getJSONArray("TT-REC-FAC-NOTE");
                            //==== Update Recovery Genreal Details
                            String mFacno , mUserId , mNoteDate , mNoteDes , mNoteSer;

                            for (int i = 0; i < REC_PAYMENT.length(); i++)
                            {
                                JSONObject JOPRCODE = REC_PAYMENT.getJSONObject(i);

                                mFacno          =       JOPRCODE.getString("FACILITY_NO");
                                mUserId         =       JOPRCODE.getString("EXECUTIVE_ID");
                                mNoteDate       =       JOPRCODE.getString("NOTE_DATE");
                                mNoteDes        =       JOPRCODE.getString("NOTE_DESC");
                                mNoteSer        =       JOPRCODE.getString("NOTE_SER");

                                sqlliteCreateRecovery_ViewDetails.InsertnoteDetails(mFacno , mUserId , mNoteDate , mNoteDes , mNoteSer);
                            }
                            JsonResponcesCode="DONE";
                            sqLiteDatabase_RECOVERY_DATA.close();
                            Log.d("Recovery-Note", JsonResponcesCode);
                            sqLiteDatabase_RECOVERY_DATA.close();

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
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Recovery_View_Facility_Other.this );
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
                15000,
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
                    SQLiteDatabase sqLiteDatabase_Note = sqlliteCreateRecovery_ViewDetails.getReadableDatabase();
                   // Cursor cursor_Note = sqLiteDatabase_Note.rawQuery("SELECT NOTE_DATE , EXECUTIVE_ID , NOTE_DESC FROM recovery_notepad_history " +
                    //        "WHERE FACILITY_NO = '" + mInputRecFacno + "' ORDER BY datetime(substr(NOTE_DATE, 7, 4) || '-' || substr(NOTE_DATE, 4, 2) || '-' || substr(NOTE_DATE, 1, 2)) DESC" , null);

                    Cursor cursor_Note = sqLiteDatabase_Note.rawQuery("SELECT NOTE_DATE , EXECUTIVE_ID , NOTE_DESC FROM recovery_notepad_history WHERE FACILITY_NO = '" + mInputRecFacno +"' ORDER BY NOTE_DATE DESC " , null);
                    if(cursor_Note.getCount() != 0)
                    {
                        cursor_Note.moveToFirst();
                        RecyDataView.setHasFixedSize(true);
                        RecyDataView.setLayoutManager(new LinearLayoutManager(Recovery_View_Facility_Other.this));
                        mAdapter_Note = new Adapter_Facility_Notepaid(Recovery_View_Facility_Other.this , cursor_Note);
                        RecyDataView.setAdapter(mAdapter_Note);
                    }

                }
            }
        });
    }


    public void GetFacilityPayment()
    {
        JSONObject jsonObjectFacno = new JSONObject();
        try
        {
            jsonObjectFacno.put("FACNO" , mInputRecFacno);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //=== Delete old data
        SQLiteDatabase sqLiteDatabase_delete = sqlliteCreateRecovery_ViewDetails.getWritableDatabase();
        sqLiteDatabase_delete.delete("recovery_paymentdetail","Facility_Number=?",new String[]{mInputRecFacno});

        requestQueue_FileGetFacilityDetails  = VollySingleton.getInstance(this).getRequestQueue();
        //String url = PHP_URL_SQL + "RECOVERY-GET-FAC-RECEIPT.php";
        String url = "http://203.115.12.125:82/core/mobnew/RECOERY-GET-PAYMENT.php";

        JsonObjectRequest jsonObjectRequest_MasterData = new JsonObjectRequest(Request.Method.POST, url, jsonObjectFacno,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //=== Get Data Responses
                        Log.d("Log", "RECOVERY-RECEIPT");

                        try {
                            //=== Delete The Table ===
                            SQLiteDatabase sqLiteDatabase_RECOVERY_DATA = sqlliteCreateRecovery_ViewDetails.getWritableDatabase();

                            JSONArray REC_PAYMENT  =   response.getJSONArray("TT-REC-FAC-RECEIPT");
                            //==== Update Recovery Genreal Details
                            String mRecptno , Rdate , RPaymode , Ramt , RPayChanal , RRemarks , RRelized , RRFacno;
                            for (int i = 0; i < REC_PAYMENT.length(); i++)
                            {
                                JSONObject JOPRCODE = REC_PAYMENT.getJSONObject(i);
                                mRecptno        =       JOPRCODE.getString("Receipt_Number");
                                Rdate           =       JOPRCODE.getString("Payment_Date_Time");
                                RPaymode        =       JOPRCODE.getString("Payment_Mode");
                                Ramt            =       JOPRCODE.getString("Amount");
                                RPayChanal      =       JOPRCODE.getString("Payment_Channel");
                                RRemarks        =       JOPRCODE.getString("Remarks");
                                RRelized        =       JOPRCODE.getString("Realized");
                                RRFacno         =       JOPRCODE.getString("Facility_Number");

                                sqlliteCreateRecovery_ViewDetails.InsertPaymentDetaiis(mRecptno , Rdate , RPaymode , Ramt , RPayChanal ,
                                        RRemarks , RRelized , RRFacno);
                            }
                            JsonResponcesCode="DONE";
                            sqLiteDatabase_RECOVERY_DATA.close();
                            Log.d("Recovery-Receipt", JsonResponcesCode);
                            sqLiteDatabase_RECOVERY_DATA.close();

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
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Recovery_View_Facility_Other.this );
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
                150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //==== Sett the Responces
        progressDialog.show();
        requestQueue_FileGetFacilityDetails.getCache().clear();
        requestQueue_FileGetFacilityDetails.start();
        requestQueue_FileGetFacilityDetails.add(jsonObjectRequest_MasterData);

        //==== Fineash Request
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

                    /*
                    if(!((Activity) Recovery_View_Facility_Other.this).isFinishing())
                    {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Recovery_View_Facility_Other.this);
                        builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                        builder.setMessage("Recovery Data Upload Successfully.");
                        builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                    */


                    //=== Recycviwe load the data
                    SQLiteDatabase sqLiteDatabase_Payment = sqlliteCreateRecovery_ViewDetails.getReadableDatabase();
                    //Cursor cursor_Payment = sqLiteDatabase_Payment.rawQuery("SELECT Payment_Date_Time , Receipt_Number , Amount , Payment_Mode FROM recovery_paymentdetail " +
                    //        "WHERE Facility_Number = '" + mInputRecFacno + "' ORDER BY datetime(substr(Payment_Date_Time, 7, 4) || '-' || substr(Payment_Date_Time, 4, 2) || '-' || substr(Payment_Date_Time, 1, 2)) DESC" , null);

                    Cursor cursor_Payment = sqLiteDatabase_Payment.rawQuery("SELECT Payment_Date_Time , Receipt_Number , Amount , Payment_Mode FROM recovery_paymentdetail WHERE Facility_Number = '" + mInputRecFacno + "' ORDER BY Payment_Date_Time" , null );
                    if(cursor_Payment.getCount() != 0)
                    {
                        cursor_Payment.moveToFirst();
                        RecyDataView.setHasFixedSize(true);
                        RecyDataView.setLayoutManager(new LinearLayoutManager(Recovery_View_Facility_Other.this));
                        mAdapter_Payment = new Adapter_Facility_Payment(Recovery_View_Facility_Other.this , cursor_Payment);
                        RecyDataView.setAdapter(mAdapter_Payment);
                    }

                }
            }
        });
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
