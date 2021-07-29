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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Recovery_Hand_Over_Details extends AppCompatActivity {

    public SqlliteCreateRecovery sqlliteCreateRecovery_collectionentry;
    public String PHP_URL_SQL , LoginUser , LoginDate ,  LoginBranch , mInputFacno;
    public TextView mFacno , tv , tv_sucess;
    public EditText mHandOverBy , mHandOverName , mHandOverNIC , mComment;
    public Button mBtnLock , mSubmitFacility;
    public double current_lattitude ,  current_longitude;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public View layout_sucress , layout_error;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_hand_over_view);

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

        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);

        mHandOverBy     =   (EditText)findViewById(R.id.TxtHandOverBy);
        mHandOverName   =   (EditText)findViewById(R.id.TxtcolOffName);
        mHandOverNIC    =   (EditText)findViewById(R.id.TxtcolOffNic);
        mComment        =   (EditText)findViewById(R.id.TxtComment);

        //=== Get Location access
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        mBtnLock        =   (Button)findViewById(R.id.btnLock);
        mBtnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockFacility();
            }
        });

        //==== submit Facility
        checkDataConnectionStatus = new CheckDataConnectionStatus(this);
        mSubmitFacility = (Button)findViewById(R.id.btnsubmit);
        mSubmitFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDataConnectionStatus.IsConnected() == true)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Hand_Over_Details.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to update this application?");
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Hand_Over_Details.this);
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
            status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Recovery_Hand_Over_Details.this);

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
        if (mHandOverBy.getText().toString().equals(""))
        {
            tv.setText("<Hand Over By> is blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mHandOverName.getText().toString().equals(""))
        {
            tv.setText("<Hand Name> is blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mHandOverNIC.getText().toString().equals(""))
        {
            tv.setText("<Hand Over NIC> is blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            //=== Run  Process
            GetLo(); // Get Locastion


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


            if (String.valueOf(current_lattitude).equals("22.22"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Hand_Over_Details.this);
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


                SQLiteDatabase sqLiteDatabase_DataUpdate = sqlliteCreateRecovery_collectionentry.getWritableDatabase();
                ContentValues contentValues_DataUpdate = new ContentValues();
                contentValues_DataUpdate.put("FACILITY_NO" , mInputFacno);
                contentValues_DataUpdate.put("ACTIONCODE" , "Hand Over");
                contentValues_DataUpdate.put("ACTION_DATE" , dateToStr);
                contentValues_DataUpdate.put("MADE_BY" , LoginUser);
                contentValues_DataUpdate.put("ADDRESS" , mClAdders);
                contentValues_DataUpdate.put("NAME" , mClientName);
                contentValues_DataUpdate.put("HANDOVER_BY" , mHandOverBy.getText().toString());
                contentValues_DataUpdate.put("HANDOVER_NAME" , mHandOverName.getText().toString());
                contentValues_DataUpdate.put("HANDOVER_NIC" , mHandOverNIC.getText().toString());
                contentValues_DataUpdate.put("COMMENTS" , mComment.getText().toString());
                contentValues_DataUpdate.put("GEO_TAG_LAT" , String.valueOf(current_lattitude));
                contentValues_DataUpdate.put("GEO_TAG_LONG" , String.valueOf(current_longitude));
                contentValues_DataUpdate.put("FAC_LOCK" , "Y");
                contentValues_DataUpdate.put("UPDATE_TIME" , LoginDate);
                contentValues_DataUpdate.put("LIVE_SERVER_UPDATE" , "");
                sqLiteDatabase_DataUpdate.insert("nemf_form_updater" , null , contentValues_DataUpdate)  ;
                Toast.makeText(getApplicationContext(), "Record Successfully Saved.", Toast.LENGTH_SHORT).show();
                mHandOverBy.setText(""); mHandOverName.setText(""); mHandOverNIC.setText(""); mComment.setText("");


                tv_sucess.setText("Record Successfully Saved.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout_sucress);
                toast.show();

                mSubmitFacility.setEnabled(false);
                mSubmitFacility.setBackgroundResource(R.drawable.normalbuttondisable);

                mBtnLock.setEnabled(false);
                mBtnLock.setBackgroundResource(R.drawable.normalbuttondisable);
            }


        }
    }

    protected void onDestroy(){

        sqlliteCreateRecovery_collectionentry.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

}
