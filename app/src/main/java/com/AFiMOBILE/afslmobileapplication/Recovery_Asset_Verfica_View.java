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
import java.util.Date;
import java.util.List;

public class Recovery_Asset_Verfica_View extends AppCompatActivity
{

    public String mInputFacno , PHP_URL_SQL ,LoginUser  , LoginDate , LoginBranch ;
    public String mClientName , mClAdders;
    public TextView mInpFacno , mVehilceModel , mVehicleno , mDateEval , mComment , tv , tv_sucess;
    public SqlliteCreateRecovery sqlliteCreateRecovery_AssetVeri_Update;
    public Spinner mDrivenBy , mVehicleCondtion , mVehilceOwnship;
    public ArrayAdapter<String> arrayDrivenBy , arrayVehilceCond , arrayVehilceOwnship;
    public Button mLockFacility , mSubmitFacility;
    double current_lattitude ,  current_longitude;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public View layout_sucress , layout_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_asset_verfica_view);

        Intent intent = getIntent();
        mInputFacno    =   intent.getStringExtra("FACNO");

        mInpFacno           =   (TextView)findViewById(R.id.TxtAssingFacno);
        mVehilceModel       =   (TextView)findViewById(R.id.TxtVehilceModel);
        mVehicleno          =   (TextView)findViewById(R.id.TxtVehilceNo);
        mDateEval           =   (TextView)findViewById(R.id.TxtDateEvalstion);
        mComment            =   (TextView)findViewById(R.id.Txtcomment);

        //=== Create Globle Varible
        sqlliteCreateRecovery_AssetVeri_Update = new SqlliteCreateRecovery(this);

        //=== Load Spnnier Data
        //== Driven By Load
        mDrivenBy   =   (Spinner)findViewById(R.id.spnDriveBy);
        List<String> labelsActon = sqlliteCreateRecovery_AssetVeri_Update.GetAllDrivenBy();
        labelsActon.add("");
        if (labelsActon != null) {
            arrayDrivenBy = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsActon);
            arrayDrivenBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDrivenBy.setAdapter(arrayDrivenBy);
            mDrivenBy.setSelection(arrayDrivenBy.getPosition(""));
        }

        //==== Vehicle Condition
        mVehicleCondtion = (Spinner)findViewById(R.id.spnVehicleCondtion) ;
        List<String> labelsVehilce = sqlliteCreateRecovery_AssetVeri_Update.GetVehicleCondtion();
        labelsVehilce.add("");
        if (labelsVehilce != null) {
            arrayVehilceCond = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsVehilce);
            arrayVehilceCond.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mVehicleCondtion.setAdapter(arrayVehilceCond);
            mVehicleCondtion.setSelection(arrayVehilceCond.getPosition(""));
        }


        //=== Vehiclce Ownership
        mVehilceOwnship = (Spinner)findViewById(R.id.spnOwnship);
        List<String> labelsVehilceOwn = sqlliteCreateRecovery_AssetVeri_Update.GetVehicleOwnShip();
        labelsVehilceOwn.add("");
        if (labelsVehilceOwn != null) {
            arrayVehilceOwnship = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsVehilceOwn);
            arrayVehilceOwnship.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mVehilceOwnship.setAdapter(arrayVehilceOwnship);
            mVehilceOwnship.setSelection(arrayVehilceOwnship.getPosition(""));
        }
        //=======================================

        mInpFacno.setText("Facility No - " + mInputFacno);

        //=== Get Globle PHP Code
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        //=== Get Location access
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);


        //=== Get Vehilce Details
        SQLiteDatabase sqLiteDatabase_Get_Vehilce = sqlliteCreateRecovery_AssetVeri_Update.getReadableDatabase();
        Cursor cursor_get_vehicle = sqLiteDatabase_Get_Vehilce.rawQuery("SELECT Asset_Model ,Vehicle_Number ,Customer_Full_Name , C_Address_Line_1 , C_Address_Line_2 , C_Address_Line_3 , C_Address_Line_4 FROM recovery_generaldetail WHERE Facility_Number = '" + mInputFacno + "'" , null);
        if (cursor_get_vehicle.getCount() != 0)
        {
            cursor_get_vehicle.moveToFirst();
            mVehilceModel.setText(cursor_get_vehicle.getString(0));
            mVehicleno.setText(cursor_get_vehicle.getString(1));
            mClientName = cursor_get_vehicle.getString(2);
            mClAdders = cursor_get_vehicle.getString(3) + "," + cursor_get_vehicle.getString(4) + "," + cursor_get_vehicle.getString(5) + "," + cursor_get_vehicle.getString(6);
        }

        //=== Lock Facility
        mLockFacility = (Button)findViewById(R.id.btnLock);
        mLockFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LockFacilityAction();
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

                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Asset_Verfica_View.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to submit record ?");
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Asset_Verfica_View.this);
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
        LockFacilityAction();
        Volly_Request_Recovery_Data volly_request_recovery_data = new Volly_Request_Recovery_Data(this);
        volly_request_recovery_data.PostFacilityRecoveryActionData(mInputFacno);
        mSubmitFacility.setEnabled(false);
        mSubmitFacility.setBackgroundResource(R.drawable.normalbuttondisable);

        tv_sucess.setText("Record Successfully Submitted.");
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout_sucress);
        toast.show();

        mLockFacility.setEnabled(false);
        mLockFacility.setBackgroundResource(R.drawable.normalbuttondisable);
    }

    public void LockFacilityAction()
    {
        if (mDrivenBy.getSelectedItem().toString().equals(""))
        {
            tv.setText("<Driven By> is Blank." );
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mDateEval.getText().equals(""))
        {
            tv.setText("<Date Evaluation> is Blank." );
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mVehicleCondtion.getSelectedItem().toString().equals(""))
        {
            tv.setText("<Vehicle Condition> is Blank." );
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mVehilceOwnship.getSelectedItem().toString().equals(""))
        {
            tv.setText("<Vehicle Ownership> is Blank." );
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            //=== To Lock the Record;
            GetLo(); //== Get GEo Locastion

            Log.e("current_lattitude" , String.valueOf(current_lattitude));
            Log.e("current_longitude" , String.valueOf(current_longitude));

            if (String.valueOf(current_lattitude).equals("22.22"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Asset_Verfica_View.this);
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

                SQLiteDatabase sqLiteDatabase_LockAction = sqlliteCreateRecovery_AssetVeri_Update.getWritableDatabase();
                ContentValues contentValues_LockAction = new ContentValues();
                contentValues_LockAction.put("FACILITY_NO" , mInputFacno);
                contentValues_LockAction.put("ACTIONCODE" , "Asset Verification");
                contentValues_LockAction.put("ACTION_DATE" , dateToStr);
                contentValues_LockAction.put("MADE_BY" , LoginUser);
                contentValues_LockAction.put("CURRENT_BUCKET" , "3 - 6");
                contentValues_LockAction.put("CNAME" , mClientName);
                contentValues_LockAction.put("ADDRESS" , mClAdders);
                contentValues_LockAction.put("VEHICLE_MAKE" , "HONDA");
                contentValues_LockAction.put("MODEL" , mVehilceModel.getText().toString());
                contentValues_LockAction.put("VEHICLE_NO" , mVehicleno.getText().toString());
                contentValues_LockAction.put("ASSET_DATE" , mDateEval.getText().toString());
                contentValues_LockAction.put("DRIVEN_BY" , mDrivenBy.getSelectedItem().toString());
                contentValues_LockAction.put("VEHICLE_CONDITION" , mVehicleCondtion.getSelectedItem().toString());
                contentValues_LockAction.put("OWNERSHIP" , mVehilceOwnship.getSelectedItem().toString());
                contentValues_LockAction.put("UPDATE_TIME" , LoginDate);
                contentValues_LockAction.put("GEO_TAG_LAT" , String.valueOf(current_lattitude));
                contentValues_LockAction.put("GEO_TAG_LONG" , String.valueOf(current_longitude));
                contentValues_LockAction.put("FAC_LOCK" , "Y");
                contentValues_LockAction.put("UPDATE_TIME" , LoginDate);
                contentValues_LockAction.put("LIVE_SERVER_UPDATE" , "");
                sqLiteDatabase_LockAction.insert("nemf_form_updater" ,null , contentValues_LockAction);
                Toast.makeText(getApplicationContext(), "Record Successfully Saved.", Toast.LENGTH_SHORT).show();
                mDateEval.setText("");
                mComment.setText("");
                mVehilceOwnship.setSelection(arrayVehilceOwnship.getPosition(""));
                mDrivenBy.setSelection(arrayDrivenBy.getPosition(""));
                mVehicleCondtion.setSelection(arrayVehilceCond.getPosition(""));

                tv_sucess.setText("Record Successfully Saved.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout_sucress);
                toast.show();

                mLockFacility.setEnabled(false);
                mLockFacility.setBackgroundResource(R.drawable.normalbuttondisable);

                mSubmitFacility.setEnabled(false);
                mSubmitFacility.setBackgroundResource(R.drawable.normalbuttondisable);
            }

        }
    }

    public void GetLo()
    {
        GPSTracker gps = new GPSTracker(this);
        int status = 0;
        if(gps.canGetLocation())
        {
           status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Recovery_Asset_Verfica_View.this);

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

    protected void onDestroy(){

        sqlliteCreateRecovery_AssetVeri_Update.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
