package com.AFiMOBILE.afslmobileapplication;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recovery_Refuse_Pay_View extends AppCompatActivity {

    public SqlliteCreateRecovery sqlliteCreateRecovery_collectionentry;
    public String PHP_URL_SQL ,  LoginUser , LoginDate , LoginBranch , mInputFacno;
    public TextView mFacno , tv , tv_sucess;
    public EditText mComment;
    public Spinner mReasonRef;
    public ArrayAdapter<String> arrayRefouseReson;
    public Button mBtnLock , mSubmitFacility;
    public double current_lattitude ,  current_longitude;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public View layout_sucress , layout_error;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_refuse_pay_view);

        sqlliteCreateRecovery_collectionentry = new SqlliteCreateRecovery(this);
        //=== Get Globle PHP Code
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        Intent intent = getIntent();
        mInputFacno     =   intent.getStringExtra("FACNO");
        mFacno          =   (TextView)findViewById(R.id.TxtAssingFacno);
        mFacno.setText("Facility No - " + mInputFacno);
        mComment    =   (EditText)findViewById(R.id.TxtComment) ;

        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);

        //=== Load Master
        mReasonRef  =   (Spinner)findViewById(R.id.spnReasonRefuse);
        List<String> labelsVehilce = new ArrayList<String>();
        labelsVehilce.add("Found Problem");
        labelsVehilce.add("Intentional");
        labelsVehilce.add("Dispute");
        labelsVehilce.add("Vehicle Accident");
        labelsVehilce.add("Medical Issue");
        labelsVehilce.add(" ");
        if (labelsVehilce != null)
        {
            arrayRefouseReson = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsVehilce);
            arrayRefouseReson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mReasonRef.setAdapter(arrayRefouseReson);
            mReasonRef.setSelection(arrayRefouseReson.getPosition(" "));
        }



        //=== Get Location access
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        mBtnLock     =   (Button)findViewById(R.id.btnLock);
        mBtnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Refuse_Pay_View.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to Submit this Facility?");
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Refuse_Pay_View.this);
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

        mBtnLock.setEnabled(false);
        mBtnLock.setBackgroundResource(R.drawable.normalbuttondisable);
    }

    public void GetLo()
    {

        GPSTracker gps = new GPSTracker(this);
        int status = 0;
        if(gps.canGetLocation())

        {
            status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Recovery_Refuse_Pay_View.this);

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

    public void LockFacility()
    {
        //=== Run Process
        GetLo(); // Get Location

        if (mReasonRef.getSelectedItem().toString().equals(""))
        {
            tv.setText("<Reason> is Blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            if (String.valueOf(current_lattitude).equals("22.22"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Refuse_Pay_View.this);
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
                String mClientName="" , mClAdders="";
                SQLiteDatabase sqLiteDatabase_Get_Details = sqlliteCreateRecovery_collectionentry.getReadableDatabase();
                Cursor cursor_get_vehicle = sqLiteDatabase_Get_Details.rawQuery("SELECT Asset_Model ,Vehicle_Number ,Customer_Full_Name , C_Address_Line_1 , C_Address_Line_2 , C_Address_Line_3 , C_Address_Line_4 FROM recovery_generaldetail WHERE Facility_Number = '" + mInputFacno + "'" , null);
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

                SQLiteDatabase sqLiteDatabase_DataUpdate = sqlliteCreateRecovery_collectionentry.getWritableDatabase();
                ContentValues contentValues_DataUpdate = new ContentValues();
                contentValues_DataUpdate.put("FACILITY_NO" , mInputFacno);
                contentValues_DataUpdate.put("ACTIONCODE" , "Refuse to Pay");
                contentValues_DataUpdate.put("ACTION_DATE" , dateToStr);
                contentValues_DataUpdate.put("MADE_BY" , LoginUser);
                contentValues_DataUpdate.put("ADDRESS" , mClAdders);
                contentValues_DataUpdate.put("NAME" , mClientName);
                contentValues_DataUpdate.put("RTP_REASON" , mReasonRef.getSelectedItem().toString());
                contentValues_DataUpdate.put("COMMENTS" , mComment.getText().toString());
                contentValues_DataUpdate.put("GEO_TAG_LAT" , String.valueOf(current_lattitude));
                contentValues_DataUpdate.put("GEO_TAG_LONG" , String.valueOf(current_longitude));
                contentValues_DataUpdate.put("FAC_LOCK" , "Y");
                contentValues_DataUpdate.put("UPDATE_TIME" , LoginDate);
                contentValues_DataUpdate.put("LIVE_SERVER_UPDATE" , "");
                sqLiteDatabase_DataUpdate.insert("nemf_form_updater" , null , contentValues_DataUpdate)  ;
                Toast.makeText(getApplicationContext(), "Record Successfully Saved.", Toast.LENGTH_SHORT).show();
                mReasonRef.setSelection(arrayRefouseReson.getPosition("  "));
                mComment.setText("");
                mSubmitFacility.setEnabled(false);
                mSubmitFacility.setBackgroundResource(R.drawable.normalbuttondisable);

                tv_sucess.setText("Record Successfully Saved.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout_sucress);
                toast.show();

                mBtnLock.setEnabled(false);
                mBtnLock.setBackgroundResource(R.drawable.normalbuttondisable);
            }
        }
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

}
