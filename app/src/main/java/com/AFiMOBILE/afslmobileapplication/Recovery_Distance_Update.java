package com.AFiMOBILE.afslmobileapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Calendar;

public class Recovery_Distance_Update extends AppCompatActivity {

    public SqlliteCreateRecovery sqlliteCreateRecovery_dis_upate;
    public String mInpAppno , PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , LoginUserName;
    public EditText mAll1Km , mFoodAllow , mTotalDis ;
    public TextView mDateTravelled , mInTime , mOutTime;
    public int mYear =0 , mMonth=0 ,mDay =0;
    public DatePickerDialog datePickerDialog , dialog;
    public View layout_sucress , layout_error;
    public TextView tv , tv_sucess , Facilityno;
    public Button mSave , mClear ;
    private RecyclerView mRecycleviwer;
    private Adapter_distance_details mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_distance_update);

        //=== Create Varible
        sqlliteCreateRecovery_dis_upate = new SqlliteCreateRecovery(this);
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();

        //=== Get Input Application No
        Intent intent = getIntent();
        mInpAppno    =   intent.getStringExtra("FACNO");
        Facilityno  =   (TextView)findViewById(R.id.TxtAssingFacno);
        Facilityno.setText(mInpAppno);


        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);

        //====== Get Date Selection ======================
        mDateTravelled = (TextView) findViewById(R.id.TxtDateTraveld);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(Recovery_Distance_Update.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mDateTravelled.setText(day + "-" + month + "-" + year);
            }
        }, mYear, mMonth, mDay);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDateTravelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });

        //=== Get Time Picker
        mInTime         =   (TextView)findViewById(R.id.TxtinTime);
        mInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(Recovery_Distance_Update.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mInTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        mOutTime        =   (TextView)findViewById(R.id.TxtOutTime);
        mOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(Recovery_Distance_Update.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mOutTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //=== Recycviwe load the data
        mRecycleviwer  = (RecyclerView)findViewById(R.id.ReyDetailslist) ;
        mRecycleviwer.setHasFixedSize(true);
        mRecycleviwer.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_distance_details(this , LoadVistData());
        mRecycleviwer.setAdapter(mAdapter);

        //=== Save Data
        mSave   =   (Button)findViewById(R.id.btnSave);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });

        mClear  =   (Button)findViewById(R.id.btnclear);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAll1Km.setText(""); mFoodAllow.setText(""); mTotalDis.setText(""); mInTime.setText("");
                mOutTime.setText(""); mDateTravelled.setText("");
            }
        });
    }


    private Cursor LoadVistData ()
    {
        SQLiteDatabase sqLiteDatabase_loaddata = sqlliteCreateRecovery_dis_upate.getReadableDatabase();
        Cursor cursor_GetData = sqLiteDatabase_loaddata.rawQuery("SELECT visit_date , facility_no , no_distance_km FROM recovery_distance_data WHERE user_id = '" + LoginUser + "' AND Live_Server_update = ''" , null );
        sqLiteDatabase_loaddata.close();
        return cursor_GetData;
    }

    public void SaveData()
    {
        mAll1Km         =   (EditText)findViewById(R.id.TxtAllow1km);
        mFoodAllow      =   (EditText)findViewById(R.id.TxtFoodAllowance);
        mTotalDis       =   (EditText)findViewById(R.id.TxtTotDistance);

        if (mAll1Km.getText().equals(""))
        {
            tv.setText("<Allowance for 1km> is blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mTotalDis.getText().equals(""))
        {
            tv.setText("<Total Distance Travelled> is blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mInTime.getText().equals(""))
        {
            tv.setText("<In Time> is blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mOutTime.getText().equals(""))
        {
            tv.setText("<Out Time> is blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            SQLiteDatabase sqLiteDatabase_distance_update = sqlliteCreateRecovery_dis_upate.getWritableDatabase();
            ContentValues contentValues_Dis_update = new ContentValues();
            contentValues_Dis_update.put("facility_no" , mInpAppno);
            contentValues_Dis_update.put("user_id" , LoginUser);
            contentValues_Dis_update.put("ent_date" , LoginDate);
            contentValues_Dis_update.put("allowance_km_lkr" , mAll1Km.getText().toString());
            contentValues_Dis_update.put("food_allow" , mFoodAllow.getText().toString());
            contentValues_Dis_update.put("no_distance_km" , mTotalDis.getText().toString());
            contentValues_Dis_update.put("in_time" , mInTime.getText().toString());
            contentValues_Dis_update.put("out_time" , mOutTime.getText().toString());
            contentValues_Dis_update.put("visit_date" , mDateTravelled.getText().toString());
            contentValues_Dis_update.put("Lock_Record" , "Y");
            contentValues_Dis_update.put("Live_Server_update" , "");
            sqLiteDatabase_distance_update.insert("recovery_distance_data" , null , contentValues_Dis_update);

            tv_sucess.setText("Record Successfully Saved.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_sucress);
            toast.show();

            mAll1Km.setText(""); mFoodAllow.setText(""); mTotalDis.setText(""); mInTime.setText("");
            mOutTime.setText(""); mDateTravelled.setText("");

            mSave.setEnabled(false);
            mSave.setBackgroundResource(R.drawable.normalbuttondisable);

            mClear.setEnabled(false);
            mClear.setBackgroundResource(R.drawable.normalbuttondisable);

            mAdapter.swapCursor(LoadVistData());
            mRecycleviwer.setAdapter(mAdapter);
        }
    }

    protected void onDestroy(){

        sqlliteCreateRecovery_dis_upate.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
