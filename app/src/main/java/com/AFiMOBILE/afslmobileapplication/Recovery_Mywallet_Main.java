package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Recovery_Mywallet_Main extends AppCompatActivity {

    public Button BtnRecoveryAction , mBtnLoadaAction;
    private int locationRequestCode = 1000;
    private FusedLocationProviderClient client;
    public String LoginUser , LoginDate , LoginBranch  , LoginUserName ;
    public TextView tv , tv_sucess , TxtRenta_1_2 , TxtRenta_2_3 , TxtRenta_3_5 , TxtRenta_5_6 , TxtRenta_6_9 , TxtRenta_9_12 , TxtRenta_12_24 , TxtRenta_24_Above , TxtRenta_PtP , TxtRenta_ManagerAssign ,
            TxtRenta_ActionTranfer , TxtRenta_SpicealCase  ;
    public SqlliteCreateRecovery sqlliteCreateRecovery_MyCaseMain;
    public Button mBtnSpicealCode , mRental1_2 , mRental2_3 ,mRental3_5 , mRental5_6 , mRental6_9 , mRental9_12 , mRenta12_24 , mRentaAbove24 , mRentalPTP;
    public TextView mMainTotalAllocate , mMainLocak , mMainVist , mMainFullyPaid , mMainFullySettle  , mMainActionTranfer ,
            mMainManagerAction , mMainRepocess , mMainLegal;
    public View layout_sucress , layout_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery__mywallet__main);

        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName = globleClassDetails.getOfficerName();

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));

        //=== Databse Connection
        sqlliteCreateRecovery_MyCaseMain = new SqlliteCreateRecovery(this);

        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);

        BtnRecoveryAction = (Button)findViewById(R.id.btnRecAction);
        //mBtnLoadaAction = (Button)findViewById(R.id.btnActionget);
        client = LocationServices.getFusedLocationProviderClient(this);
        mMainTotalAllocate      =   (TextView)findViewById(R.id.EditAlocateCase);
        mMainLocak              =   (TextView)findViewById(R.id.EditLocakCase);
        mMainVist               =   (TextView)findViewById(R.id.EditVistCase);
        mMainFullyPaid          =   (TextView)findViewById(R.id.EditFullypaid);
        mMainActionTranfer      =   (TextView)findViewById(R.id.EditActionCase);
        mMainManagerAction      =   (TextView)findViewById(R.id.EditManagerAccign);
        mMainRepocess           =   (TextView)findViewById(R.id.EditRepocesscase);
        mMainLegal              =   (TextView)findViewById(R.id.EditLegal);

        //=== Load My Case Class
        BtnRecoveryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_RecAction = new Intent("android.intent.action.Recovery_Actin_Tranfer");
                startActivity(intent_RecAction);
            }
        });

        //=== Run Process
        GertCountMyCase();
        LoadFacilityList();
        LoadMainCaseCount();
        GetOverviewDatalist();
    }


    public void GetOverviewDatalist()
    {
        mMainTotalAllocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (mMainTotalAllocate.getText().equals("0"))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "OVER-ALL");
                    intentRentalList.putExtra("MIN" , "0");
                    intentRentalList.putExtra("MAX" , "0");
                    startActivity(intentRentalList);
                }
            }
        });


        mMainLocak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (mMainLocak.getText().equals("0"))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "OVER-LOCK");
                    intentRentalList.putExtra("MIN" , "0");
                    intentRentalList.putExtra("MAX" , "0");
                    startActivity(intentRentalList);
                }

            }
        });

        mMainVist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (mMainVist.getText().equals("0"))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "OVER-VIST");
                    intentRentalList.putExtra("MIN" , "0");
                    intentRentalList.putExtra("MAX" , "0");
                    startActivity(intentRentalList);
                }
            }
        });


        mMainActionTranfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMainActionTranfer.getText().equals("0"))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "OVER-ACTION");
                    intentRentalList.putExtra("MIN" , "0");
                    intentRentalList.putExtra("MAX" , "0");
                    startActivity(intentRentalList);
                }
            }
        });

        mMainManagerAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMainManagerAction.getText().equals("0"))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "OVER-MANAGER");
                    intentRentalList.putExtra("MIN" , "0");
                    intentRentalList.putExtra("MAX" , "0");
                    startActivity(intentRentalList);
                }
            }
        });


        mMainRepocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMainRepocess.getText().equals("0"))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "OVER-REPOCESS");
                    intentRentalList.putExtra("MIN" , "0");
                    intentRentalList.putExtra("MAX" , "0");
                    startActivity(intentRentalList);
                }
            }
        });

        //==== fully settle case
        mMainFullyPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainFullyPaid.getText().equals("0"))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Overview_Paid_Details");
                    startActivity(intentRentalList);
                }
            }
        });
    }

    public void LoadMainCaseCount()
    {


        int mTotalCase=0 , mTotalArraysZero=0 , mTotalClose=0 , mTotalRepocess=0 , mTotalManagerAssign=0 ;
        SQLiteDatabase sqLiteDatabase_MainCount = sqlliteCreateRecovery_MyCaseMain.getReadableDatabase();
        Cursor cursor_MainCount = sqLiteDatabase_MainCount.rawQuery("SELECT Total_Arrear_Amount,Facility_Status,Contract_Status FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "'" , null);
        if (cursor_MainCount.getCount() != 0)
        {
            cursor_MainCount.moveToFirst();
            do
            {
                mTotalCase = mTotalCase + 1;


                if (cursor_MainCount.getString(0) != "")
                {
                    if (Double.parseDouble(cursor_MainCount.getString(0).replace("," , "")) == 0)
                    {
                        mTotalArraysZero = mTotalArraysZero + 1;
                    }
                }

                if (cursor_MainCount.getString(1) == "C")
                {
                    mTotalClose = mTotalClose + 1;
                }

                if (cursor_MainCount.getString(2) == "R")
                {
                    mTotalRepocess = mTotalRepocess + 1;
                }
            }while (cursor_MainCount.moveToNext());
        }

        //=== Get month begin date
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = 1;
        c.set(year, month, day);
        Date lastDayOfMonth = c.getTime();
        String MonthBGdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(lastDayOfMonth);
        Log.e("bg" , MonthBGdate);

        int LockCount=0 , VisitCount=0 , ActionTranfer=0;
        Cursor cursor_GetActionCount = sqLiteDatabase_MainCount.rawQuery("SELECT FAC_LOCK , GEO_TAG_LAT , LIVE_SERVER_UPDATE FROM nemf_form_updater WHERE MADE_BY = '" + LoginUser + "' and ACTION_DATE >= '" + MonthBGdate + "' and ACTION_DATE <= '" + LoginDate + "'"  , null );
        if (cursor_GetActionCount.getCount() != 0)
        {
            cursor_GetActionCount.moveToFirst();
            do
            {
                if (cursor_GetActionCount.getString(0) != null)
                {
                    if (cursor_GetActionCount.getString(0).equals("Y"))
                    {
                        LockCount = LockCount + 1;
                    }
                }

                if (cursor_GetActionCount.getString(1) != " ")
                {
                    VisitCount = VisitCount + 1;
                }


                ActionTranfer=0;
                /*
                if (cursor_GetActionCount.getString(2) != null)
                {
                    if (cursor_GetActionCount.getString(2).equals("Y"))
                    {
                        ActionTranfer = ActionTranfer + 1;
                    }
                }
                */

            }while (cursor_GetActionCount.moveToNext());
        }


        Cursor cursor_manager_Assign = sqLiteDatabase_MainCount.rawQuery("SELECT * FROM recovery_request_mgr WHERE Request_userid = '" + LoginUser + "'" , null );
        mTotalManagerAssign = cursor_manager_Assign.getCount();


        mMainActionTranfer.setText(String.valueOf(ActionTranfer));
        mMainVist.setText(String.valueOf(VisitCount));
        mMainLocak.setText(String.valueOf(LockCount));
        mMainTotalAllocate.setText(String.valueOf(mTotalCase));
        mMainFullyPaid.setText(String.valueOf(mTotalArraysZero));

        mMainRepocess.setText(String.valueOf(mTotalRepocess));
        mMainManagerAction.setText(String.valueOf(mTotalManagerAssign));

    }

    protected void onStart()
    {
        super.onStart();
        GertCountMyCase();
        LoadFacilityList();
        LoadMainCaseCount();

    }

    public void LoadFacilityList()
    {
        mRental1_2 = (Button)findViewById(R.id.btnRNT1_2);
        mRental2_3 = (Button)findViewById(R.id.btnRNT2_3);
        mRental3_5 = (Button)findViewById(R.id.btnRNT3_5);
        mRental5_6 = (Button)findViewById(R.id.btnRNT5_6);
        mRental6_9 = (Button)findViewById(R.id.btnRNT6_9);
        mRental9_12 = (Button)findViewById(R.id.btnRNT9_12);
        mRenta12_24 = (Button)findViewById(R.id.btnRNT12_24);
        mRentaAbove24 = (Button)findViewById(R.id.btnRNT24above);
        mRentalPTP  =   (Button)findViewById(R.id.btnRNTPTP);
        mBtnSpicealCode = (Button)findViewById(R.id.btnTotAllocate);

        mBtnSpicealCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (TxtRenta_SpicealCase.getText().equals(""))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "OVER-SPECIAL");
                    intentRentalList.putExtra("MIN" , "0");
                    intentRentalList.putExtra("MAX" , "0");
                    startActivity(intentRentalList);
                }
            }
        });


        mRentalPTP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (TxtRenta_PtP.getText().equals(""))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "OVER-PTP");
                    intentRentalList.putExtra("MIN" , "0");
                    intentRentalList.putExtra("MAX" , "0");
                    startActivity(intentRentalList);
                }
            }
        });

        mRental1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TxtRenta_1_2.getText().equals(""))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "RANGE");
                    intentRentalList.putExtra("MIN" , "0");
                    intentRentalList.putExtra("MAX" , "2");
                    startActivity(intentRentalList);
                }
            }
        });

        mRental2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (TxtRenta_2_3.getText().equals(""))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "RANGE");
                    intentRentalList.putExtra("MIN" , "2");
                    intentRentalList.putExtra("MAX" , "3");
                    startActivity(intentRentalList);
                }
            }
        });

        mRental3_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (TxtRenta_3_5.getText().equals(""))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "RANGE");
                    intentRentalList.putExtra("MIN" , "3");
                    intentRentalList.putExtra("MAX" , "5");
                    startActivity(intentRentalList);
                }
            }
        });

        mRental5_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (TxtRenta_5_6.getText().equals(""))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "RANGE");
                    intentRentalList.putExtra("MIN" , "5");
                    intentRentalList.putExtra("MAX" , "6");
                    startActivity(intentRentalList);
                }
            }
        });


        mRental6_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.e("6-9txt" , TxtRenta_6_9.getText().toString());

                if (TxtRenta_6_9.getText().equals(""))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "RANGE");
                    intentRentalList.putExtra("MIN" , "6");
                    intentRentalList.putExtra("MAX" , "9");
                    startActivity(intentRentalList);
                }
            }
        });

        mRental9_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (TxtRenta_9_12.getText().equals(""))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "RANGE");
                    intentRentalList.putExtra("MIN" , "9");
                    intentRentalList.putExtra("MAX" , "12");
                    startActivity(intentRentalList);
                }
            }
        });

        mRenta12_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (TxtRenta_12_24.getText().equals(""))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "RANGE");
                    intentRentalList.putExtra("MIN" , "12");
                    intentRentalList.putExtra("MAX" , "24");
                    startActivity(intentRentalList);
                }

            }
        });

        mRentaAbove24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TxtRenta_24_Above.getText().equals(""))
                {
                    tv.setText("Record not found.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                    intentRentalList.putExtra("TYPE" , "RANGE");
                    intentRentalList.putExtra("MIN" , "12");
                    intentRentalList.putExtra("MAX" , "100");
                    startActivity(intentRentalList);
                }
            }
        });
    }

    public void GertCountMyCase()
    {
        TxtRenta_1_2                =   (TextView)findViewById(R.id.TxtRenta1_2);
        TxtRenta_2_3                =   (TextView)findViewById(R.id.TxtRenta2_3);
        TxtRenta_3_5                =   (TextView)findViewById(R.id.TxtRenta3_5);
        TxtRenta_5_6                =   (TextView)findViewById(R.id.TxtRenta5_6);
        TxtRenta_6_9                =   (TextView)findViewById(R.id.TxtRenta6_9);
        TxtRenta_9_12               =   (TextView)findViewById(R.id.TxtRenta9_12);
        TxtRenta_12_24              =   (TextView)findViewById(R.id.TxtRenta12_24);
        TxtRenta_24_Above           =   (TextView)findViewById(R.id.TxtRenta24Above);
        TxtRenta_PtP                =   (TextView)findViewById(R.id.TxtRentaPTP);
        TxtRenta_ManagerAssign      =   (TextView)findViewById(R.id.TxtRentaManager);
        TxtRenta_ActionTranfer      =   (TextView)findViewById(R.id.TxtActionTranfer);
        TxtRenta_SpicealCase      =   (TextView)findViewById(R.id.TxtTotAlocate);

        //=== Get Data
        double mRenta_1_2=0 , mRenta_2_3=0 , mRenta_3_5=0 , mRenta_5_6=0 , mRenta_6_9=0 , mRenta_9_12=0 , mRenta_12_24=0 , mRenta_24_Above=0 , mRenta_Ptm=0 , mRenta_ManagerAssign=0 ,
                mRenta_ActionTranfer=0 , mRenta_TotalAllocate=0 , mRenta_Spical_code=0;

        SQLiteDatabase sqLiteDatabase_MyCaseMain = sqlliteCreateRecovery_MyCaseMain.getReadableDatabase();
        Cursor cursor_MyCaseMain = sqLiteDatabase_MyCaseMain.rawQuery("SELECT Arrears_Rental_No FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "'" , null);
        if (cursor_MyCaseMain.getCount() != 0)
        {
            cursor_MyCaseMain.moveToFirst();
            do
            {
                double GetVal = Double.parseDouble(cursor_MyCaseMain.getString(0));
                mRenta_TotalAllocate = mRenta_TotalAllocate + 1;

                if (GetVal >= 0 && GetVal <= 2)
                {
                    mRenta_1_2 = mRenta_1_2 + 1;
                }
                else if (GetVal > 2 && GetVal <= 3)
                {
                    mRenta_2_3 = mRenta_2_3 + 1;
                }
                else if (GetVal > 3 && GetVal <= 5)
                {
                    mRenta_3_5 = mRenta_3_5 + 1;
                }
                else if ( GetVal > 5 && GetVal <= 6)
                {
                    mRenta_5_6 = mRenta_5_6 + 1;
                }
                else if (GetVal > 6 && GetVal <= 9)
                {
                    mRenta_6_9 = mRenta_6_9 + 1;
                }
                else if (GetVal > 9 && GetVal <= 12)
                {
                    mRenta_9_12 = mRenta_9_12 + 1;
                }
                else if (GetVal > 12 && GetVal <= 24)
                {
                    mRenta_12_24 = mRenta_12_24 + 1;
                }
                else if ( GetVal > 24)
                {
                    mRenta_24_Above = mRenta_24_Above + 1;
                }
            }while (cursor_MyCaseMain.moveToNext());
        }


        //=== Get Rental PTP count
        Cursor cursor_rental_ptp = sqLiteDatabase_MyCaseMain.rawQuery("SELECT * FROM nemf_form_updater WHERE MADE_BY = '" + LoginUser + "' and ACTIONCODE = 'PTP'" , null );
        if (cursor_rental_ptp.getCount() != 0)
        {
            mRenta_Ptm = cursor_rental_ptp.getCount();
        }

        //=== Get Speical Allocvate Case Count
        mRenta_Spical_code = 0;
        Cursor cursor_sp_case = sqLiteDatabase_MyCaseMain.rawQuery("SELECT * FROM Recovery_speical_backet WHERE recovery_executive = '" + LoginUser + "'" , null);
        if (cursor_sp_case.getCount() != 0)
        {
            cursor_sp_case.moveToFirst();
            do{
                mRenta_Spical_code = mRenta_Spical_code + 1;
            }while (cursor_sp_case.moveToNext());
        }

        if (mRenta_Spical_code != 0)
        {
            TxtRenta_SpicealCase.setText(String.valueOf((int) mRenta_Spical_code));
        }
        else
        {
            TxtRenta_SpicealCase.setVisibility(View.INVISIBLE);
        }

        //=======================================


        //=== Set Data View

        if (mRenta_Ptm !=0)
        {
            TxtRenta_PtP.setText(String.valueOf((int) mRenta_Ptm));
        }
        else
        {
            TxtRenta_PtP.setVisibility(View.INVISIBLE);
        }

        if (mRenta_1_2 != 0)
            TxtRenta_1_2.setText(String.valueOf((int) mRenta_1_2));
        else
            TxtRenta_1_2.setVisibility(View.INVISIBLE);

        if (mRenta_2_3 != 0)
            TxtRenta_2_3.setText(String.valueOf((int) mRenta_2_3));
        else
            TxtRenta_2_3.setVisibility(View.INVISIBLE);

        if (mRenta_3_5 != 0)
            TxtRenta_3_5.setText(String.valueOf((int) mRenta_3_5));
        else
            TxtRenta_3_5.setVisibility(View.INVISIBLE);

        if (mRenta_5_6 != 0)
            TxtRenta_5_6.setText(String.valueOf((int) mRenta_5_6));
        else
            TxtRenta_5_6.setVisibility(View.INVISIBLE);

        if (mRenta_6_9 != 0)
            TxtRenta_6_9.setText(String.valueOf((int) mRenta_6_9));
        else
            TxtRenta_6_9.setVisibility(View.INVISIBLE);

        if (mRenta_9_12 != 0)
            TxtRenta_9_12.setText(String.valueOf((int) mRenta_9_12));
        else
            TxtRenta_9_12.setVisibility(View.INVISIBLE);

        if (mRenta_12_24 != 0)
            TxtRenta_12_24.setText(String.valueOf((int) mRenta_12_24));
        else
            TxtRenta_12_24.setVisibility(View.INVISIBLE);

        if (mRenta_24_Above != 0)
            TxtRenta_24_Above.setText(String.valueOf((int) mRenta_24_Above));
        else
            TxtRenta_24_Above.setVisibility(View.INVISIBLE);

        /*
        if (mRenta_TotalAllocate != 0)
            TxtRenta_TotalAllocate.setText(String.valueOf((int) mRenta_TotalAllocate));
        else
            TxtRenta_TotalAllocate.setVisibility(View.INVISIBLE);
            */

        sqLiteDatabase_MyCaseMain.close();

    }

    public void GetLo()
    {
        double current_lattitude ,  current_longitude;
        GPSTracker gps = new GPSTracker(this);
        int status = 0;
        if(gps.canGetLocation())

        {
            status = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(getApplicationContext());

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

    public void LoadMyCase()
    {
        Intent intent_MyCase = new Intent(this , Recovery_MyCase_Main.class);
        startActivity(intent_MyCase);
    }


}
