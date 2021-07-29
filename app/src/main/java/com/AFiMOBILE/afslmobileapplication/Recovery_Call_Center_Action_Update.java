package com.AFiMOBILE.afslmobileapplication;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Recovery_Call_Center_Action_Update extends AppCompatActivity {

    private TextView mPaidDate , mtxtShowFacility;
    private Spinner mSpnaction;
    private Button mSubmit;
    private SqlliteCreateRecovery sqlliteCreateRecovery_call_action;
    private SQLiteDatabase sqLiteDatabase_call_action;
    private String PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , mInputFacno , mInputAction;
    public int mYear , mMonth , mDay;
    public DatePickerDialog  dialog;
    public ArrayAdapter<String> mPaidThriParty;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    private EditText mPaidAmount , mCommentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_call_center_action_update);

        Intent intent = getIntent();
        mInputFacno     =   intent.getStringExtra("FACNO");
        mInputAction     =   intent.getStringExtra("ACTION");


        mPaidDate       =   (TextView)findViewById(R.id.TxtcolDate);
        mPaidAmount     =   (EditText) findViewById(R.id.Txtptpamount);
        mCommentAmount  =   (EditText) findViewById(R.id.Txtcomment);
        mSpnaction      =   (Spinner)findViewById(R.id.spnAcgioncode);
        mSubmit         =   (Button)findViewById(R.id.btnsave);
        mtxtShowFacility    =   (TextView)findViewById(R.id.TxtAssingFacno) ;

        mtxtShowFacility.setText(mInputFacno);

        sqlliteCreateRecovery_call_action   =   new SqlliteCreateRecovery(this);

        mSpnaction  = (Spinner)findViewById(R.id.spnAcgioncode);
        if (mInputAction.equals("VISIT"))
        {
            List<String> listpaid = new ArrayList<String>();
            listpaid.add("PTP");
            if (listpaid != null)
            {
                mPaidThriParty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listpaid);
                mPaidThriParty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpnaction.setAdapter(mPaidThriParty);
                mSpnaction.setSelection(mPaidThriParty.getPosition("PTP"));

            }
        }
        else
        {
            List<String> listpaid = new ArrayList<String>();
            listpaid.add("PTP");
            listpaid.add("Call Only");
            if (listpaid != null)
            {
                mPaidThriParty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listpaid);
                mPaidThriParty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpnaction.setAdapter(mPaidThriParty);
                mSpnaction.setSelection(mPaidThriParty.getPosition("PTP"));
            }
        }

        //=== Get Globle PHP Code
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        //====== Get Date Selection ======================
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(Recovery_Call_Center_Action_Update.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mPaidDate.setText(day + "-" + month + "-" + year);
            }
        }, mYear, mMonth, mDay);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mPaidDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });


        mSpnaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpnaction.getSelectedItem().toString().equals("Call Only"))
                {
                    mPaidDate.setVisibility(View.INVISIBLE);
                    mPaidAmount.setVisibility(View.INVISIBLE);
                }
                else
                {
                    mPaidDate.setVisibility(View.VISIBLE);
                    mPaidAmount.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataConnectionStatus = new CheckDataConnectionStatus(Recovery_Call_Center_Action_Update.this);

                if (checkDataConnectionStatus.IsConnected() == true)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Call_Center_Action_Update.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to update this application?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SubmitData();
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Call_Center_Action_Update.this);
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
        });

    }


    private void SubmitData()
    {
        if (mCommentAmount.length() != 0)
        {
            String mClientName="" , mClAdders="";
            SQLiteDatabase sqLiteDatabase_Get_Details = sqlliteCreateRecovery_call_action.getReadableDatabase();
            Cursor cursor_get_vehicle = sqLiteDatabase_Get_Details.rawQuery("SELECT Asset_Model ,Vehicle_Number ,Customer_Full_Name , C_Address_Line_1 , C_Address_Line_2 , C_Address_Line_3 , C_Address_Line_4 FROM CallCenter_recovery_generaldetail WHERE Facility_Number = '" + mInputFacno + "'" , null);
            if (cursor_get_vehicle.getCount() != 0)
            {
                cursor_get_vehicle.moveToFirst();
                mClientName = cursor_get_vehicle.getString(2);
                mClAdders = cursor_get_vehicle.getString(3) + "," + cursor_get_vehicle.getString(4) + "," + cursor_get_vehicle.getString(5) + "," + cursor_get_vehicle.getString(6);
            }
            sqLiteDatabase_Get_Details.close();
            cursor_get_vehicle.close();


            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateToStr = format.format(today);
            Log.e("date" , dateToStr);

            SQLiteDatabase sqLiteDatabase_DataUpdate = sqlliteCreateRecovery_call_action.getWritableDatabase();
            ContentValues contentValues_DataUpdate = new ContentValues();
            contentValues_DataUpdate.put("FACILITY_NO" , mInputFacno);
            contentValues_DataUpdate.put("ACTIONCODE" , "BACK_CALL - " + mSpnaction.getSelectedItem().toString());
            contentValues_DataUpdate.put("ACTION_DATE" , dateToStr);
            contentValues_DataUpdate.put("MADE_BY" , LoginUser);
            contentValues_DataUpdate.put("ADDRESS" , mClAdders);
            contentValues_DataUpdate.put("NAME" , mClientName);
            contentValues_DataUpdate.put("COMMENTS" , mCommentAmount.getText().toString());
            contentValues_DataUpdate.put("FAC_LOCK" , "Y");
            contentValues_DataUpdate.put("UPDATE_TIME" , LoginDate);
            contentValues_DataUpdate.put("LIVE_SERVER_UPDATE" , "");
            contentValues_DataUpdate.put("PTP_CHANNEL_MODE" , mInputAction);
            contentValues_DataUpdate.put("PROMISE_TO_PAY_DATE" , mPaidDate.getText().toString());
            contentValues_DataUpdate.put("PTP_AMOUNT" , mPaidAmount.getText().toString());
            contentValues_DataUpdate.put("PTP_BRANCH" , "BACK-OFFICE");
            sqLiteDatabase_DataUpdate.insert("nemf_form_updater" , null , contentValues_DataUpdate)  ;

            Volly_Request_Recovery_Data volly_request_recovery_data = new Volly_Request_Recovery_Data(this);
            volly_request_recovery_data.PostFacilityRecoveryActionData(mInputFacno);
        }
        else
        {
            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Call_Center_Action_Update.this);
            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
            builder.setMessage("<Comment> Cannot be a blank.");
            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        }

    }
}
