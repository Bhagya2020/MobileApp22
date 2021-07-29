package com.AFiMOBILE.afslmobileapplication;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Recovery_Meeting_Request_View extends AppCompatActivity
{

    public SqlliteCreateRecovery sqlliteCreateRecovery_dataupload;
    public String PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , mInputFacno;
    public TextView mFacno , tv , tv_sucess ;
    public Spinner mSpnMeetingReq;
    public EditText mMeetDate , mMeetTime , mComment , mVenue;
    public ArrayAdapter<String> arrayMeetingReqby ;
    public int mYear =0 , mMonth=0 ,mDay =0;
    public DatePickerDialog datePickerDialog , dialog;
    public Button mLockFac , mSubmitFacility;
    public double current_lattitude ,  current_longitude;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public View layout_sucress , layout_error;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_meeting_request_view);

        sqlliteCreateRecovery_dataupload = new SqlliteCreateRecovery(this);
        //=== Get Globle PHP Code
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        Intent intent = getIntent();
        mInputFacno     =   intent.getStringExtra("FACNO");
        mFacno          =   (TextView)findViewById(R.id.TxtAssingFacno);
        mComment        =   (EditText)findViewById(R.id.TxtComment);
        mVenue          =   (EditText)findViewById(R.id.TxtVenue);
        mFacno.setText("Facility No - " + mInputFacno);

        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);

        //=== Load Master Data
        mSpnMeetingReq  = (Spinner)findViewById(R.id.spnMeetingReq);
        List<String> labelsData = sqlliteCreateRecovery_dataupload.GetRelastionType();
        labelsData.add("");
        if (labelsData != null)
        {
            arrayMeetingReqby = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsData);
            arrayMeetingReqby.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnMeetingReq.setAdapter(arrayMeetingReqby);
            mSpnMeetingReq.setSelection(arrayMeetingReqby.getPosition(""));
        }

        //====== Get Date Selection ======================
        mMeetDate = (EditText)findViewById(R.id.TxtMeetDate);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(Recovery_Meeting_Request_View.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mMeetDate.setText(day + "-" + month + "-" + year);
            }
        }, mYear, mMonth, mDay);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mMeetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });

        //=== Get Time Picker
        mMeetTime       =   (EditText)findViewById(R.id.TxtMeetTime);
        mMeetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(Recovery_Meeting_Request_View.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mMeetTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //=== Get Location access
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);




        mLockFac = (Button)findViewById(R.id.btnLock) ;
        mLockFac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LockFacility();
            }
        });

        checkDataConnectionStatus = new CheckDataConnectionStatus(this);
        //==== submit Facility
        mSubmitFacility = (Button)findViewById(R.id.btnsubmit);
        mSubmitFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDataConnectionStatus.IsConnected() == true)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Meeting_Request_View.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to delete this application?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SubmitFacility();
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Meeting_Request_View.this);
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

    public void GetLo()
    {

        GPSTracker gps = new GPSTracker(this);
        int status = 0;
        if(gps.canGetLocation())

        {
            status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Recovery_Meeting_Request_View.this);

            if (status == ConnectionResult.SUCCESS) {
                current_lattitude = gps.getLatitude();
                current_longitude = gps.getLongitude();
                Log.d("dashlatlongon", "" + current_lattitude + "-"
                        + current_longitude);

                if (current_lattitude == 0.0 && current_longitude == 0.0) {
                    current_lattitude = 22.22;
                    current_longitude = 22.22;

                }

            } else {
                current_lattitude = 22.22;
                current_longitude = 22.22;
            }

        }
        else
        {
            gps.showSettingsAlert();
        }
    }

    public void SubmitFacility()
    {
        LockFacility();
        Volly_Request_Recovery_Data volly_request_recovery_data = new Volly_Request_Recovery_Data(this);
        volly_request_recovery_data.PostFacilityRecoveryActionData(mInputFacno);
        mSubmitFacility.setEnabled(false);
        mSubmitFacility.setBackgroundResource(R.drawable.normalbuttondisable);

        tv_sucess.setText("Record Successfully Submitted.");
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout_sucress);
        toast.show();

        mLockFac.setEnabled(false);
        mLockFac.setBackgroundResource(R.drawable.normalbuttondisable);
    }

    public void LockFacility()
    {

        if (mSpnMeetingReq.getSelectedItem().toString().equals(""))
        {
            tv.setText("<Meeting Request> is blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mMeetDate.getText().toString().equals(""))
        {
            tv.setText("<Meeting Date> is blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mVenue.getText().toString().equals(""))
        {
            tv.setText("<Meeting Venue> is blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            //=== Run Process
            GetLo(); // Get Locastion

            String mClientName="" , mClAdders="" , mPhoneNo="";
            SQLiteDatabase sqLiteDatabase_Get_Details = sqlliteCreateRecovery_dataupload.getReadableDatabase();
            Cursor cursor_get_vehicle = sqLiteDatabase_Get_Details.rawQuery("SELECT Asset_Model ,Vehicle_Number ,Customer_Full_Name , C_Address_Line_1 , C_Address_Line_2 , C_Address_Line_3 , C_Address_Line_4 , Mobile_No1" +
                    " FROM recovery_generaldetail WHERE Facility_Number = '" + mInputFacno + "'" , null);
            if (cursor_get_vehicle.getCount() != 0)
            {
                cursor_get_vehicle.moveToFirst();
                mClientName = cursor_get_vehicle.getString(2);
                mClAdders = cursor_get_vehicle.getString(3) + "," + cursor_get_vehicle.getString(4) + "," + cursor_get_vehicle.getString(5) + "," + cursor_get_vehicle.getString(6);
                mPhoneNo = cursor_get_vehicle.getString(7);
            }
            sqLiteDatabase_Get_Details.close();
            cursor_get_vehicle.close();


            Log.e("current_lattitude" , String.valueOf(current_lattitude));
            Log.e("current_longitude" , String.valueOf(current_longitude));

            if (String.valueOf(current_lattitude).equals("22.22"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Meeting_Request_View.this);
                builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                builder.setMessage("GEO Tag not update. Please try again.. ");
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
                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String dateToStr = format.format(today);
                Log.e("date" , dateToStr);

                SQLiteDatabase sqLiteDatabase_DataUpload = sqlliteCreateRecovery_dataupload.getWritableDatabase();
                ContentValues contentValues_DataUpload = new ContentValues();
                contentValues_DataUpload.put("FACILITY_NO" , mInputFacno);
                contentValues_DataUpload.put("ACTIONCODE" , "Meeting Request");
                contentValues_DataUpload.put("ACTION_DATE" , dateToStr);
                contentValues_DataUpload.put("MADE_BY" , LoginUser);
                contentValues_DataUpload.put("CNAME" , mClientName);
                contentValues_DataUpload.put("NAME" , mClientName);
                contentValues_DataUpload.put("RELATIONSHIP" , mSpnMeetingReq.getSelectedItem().toString());
                contentValues_DataUpload.put("PHONE_NUMBER" , mPhoneNo);
                contentValues_DataUpload.put("ADDRESS" , mClAdders);
                contentValues_DataUpload.put("MEETING_DATE" , mMeetDate.getText().toString());
                contentValues_DataUpload.put("VENUE" , mVenue.getText().toString());
                contentValues_DataUpload.put("COMMENTS" , mComment.getText().toString());
                contentValues_DataUpload.put("MEETING_TIME" , mMeetTime.getText().toString());
                contentValues_DataUpload.put("GEO_TAG_LAT" , String.valueOf(current_lattitude));
                contentValues_DataUpload.put("GEO_TAG_LONG" , String.valueOf(current_longitude));
                contentValues_DataUpload.put("UPDATE_TIME" , LoginDate);
                contentValues_DataUpload.put("FAC_LOCK" , "Y");
                contentValues_DataUpload.put("LIVE_SERVER_UPDATE" , "");
                sqLiteDatabase_DataUpload.insert("nemf_form_updater" , null , contentValues_DataUpload);
                Toast.makeText(getApplicationContext(), "Record Successfully Saved.", Toast.LENGTH_SHORT).show();
                mSpnMeetingReq.setSelection(arrayMeetingReqby.getPosition(""));
                mMeetDate.setText(""); mComment.setText(""); mMeetTime.setText("");

                tv_sucess.setText("Record Successfully Saved.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout_sucress);
                toast.show();

                mSubmitFacility.setEnabled(false);
                mSubmitFacility.setBackgroundResource(R.drawable.normalbuttondisable);

                mLockFac.setEnabled(false);
                mLockFac.setBackgroundResource(R.drawable.normalbuttondisable);
            }
        }
    }

    protected void onDestroy(){

        sqlliteCreateRecovery_dataupload.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
