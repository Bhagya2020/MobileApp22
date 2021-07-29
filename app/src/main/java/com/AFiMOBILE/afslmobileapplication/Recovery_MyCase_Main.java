package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Recovery_MyCase_Main extends AppCompatActivity
{

    public TextView TxtRenta_1_2 , TxtRenta_2_3 , TxtRenta_3_5 , TxtRenta_5_6 , TxtRenta_6_9 , TxtRenta_9_12 , TxtRenta_12_24 , TxtRenta_24_Above , TxtRenta_Ptm , TxtRenta_ManagerAssign ,
            TxtRenta_ActionTranfer , TxtRenta_TotalAllocate ;
    public String LoginUser , LoginDate , LoginBranch , LoginUserName;
    public SqlliteCreateRecovery sqlliteCreateRecovery_MyCaseMain;
    public Button mRental1_2 , mRental2_3 ,mRental3_5 , mRental5_6 , mRental6_9 , mRental9_12 , mRenta12_24 , mRentaAbove24;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_mycase_main);

        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName = globleClassDetails.getOfficerName();

        //=== Databse Connection
        sqlliteCreateRecovery_MyCaseMain = new SqlliteCreateRecovery(this);


        //=== Run  Process
        GertCountMyCase();   // Get Facility Count
        LoadFacilityList();  // LoadFacility List


    }

    public void LoadFacilityList()
    {
        mRental1_2 = (Button)findViewById(R.id.btnRNT1_2);
        mRental2_3 = (Button)findViewById(R.id.btnRNT2_3);
        mRental3_5 = (Button)findViewById(R.id.btnRNT3_5);
        mRental5_6 = (Button)findViewById(R.id.btnRNT5_6);
        mRental6_9 = (Button)findViewById(R.id.btnRNT6_9);
        mRental9_12 = (Button)findViewById(R.id.btnRNT6_9);
        mRenta12_24 = (Button)findViewById(R.id.btnRNT12_24);
        mRentaAbove24 = (Button)findViewById(R.id.btnRNT24above);

        mRental1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                intentRentalList.putExtra("TYPE" , "RANGE");
                intentRentalList.putExtra("MIN" , "0");
                intentRentalList.putExtra("MAX" , "2");
                startActivity(intentRentalList);
            }
        });

        mRental2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                intentRentalList.putExtra("TYPE" , "RANGE");
                intentRentalList.putExtra("MIN" , "2");
                intentRentalList.putExtra("MAX" , "3");
                startActivity(intentRentalList);
            }
        });

        mRental3_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                intentRentalList.putExtra("TYPE" , "RANGE");
                intentRentalList.putExtra("MIN" , "3");
                intentRentalList.putExtra("MAX" , "5");
                startActivity(intentRentalList);
            }
        });

        mRental5_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                intentRentalList.putExtra("TYPE" , "RANGE");
                intentRentalList.putExtra("MIN" , "5");
                intentRentalList.putExtra("MAX" , "6");
                startActivity(intentRentalList);
            }
        });


        mRental6_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                intentRentalList.putExtra("TYPE" , "RANGE");
                intentRentalList.putExtra("MIN" , "6");
                intentRentalList.putExtra("MAX" , "9");
                startActivity(intentRentalList);
            }
        });

        mRental9_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                intentRentalList.putExtra("TYPE" , "RANGE");
                intentRentalList.putExtra("MIN" , "9");
                intentRentalList.putExtra("MAX" , "12");
                startActivity(intentRentalList);
            }
        });

        mRenta12_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                intentRentalList.putExtra("TYPE" , "RANGE");
                intentRentalList.putExtra("MIN" , "12");
                intentRentalList.putExtra("MAX" , "24");
                startActivity(intentRentalList);
            }
        });

        mRentaAbove24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRentalList = new Intent("android.intent.action.Recovery_Facility_List_View");
                intentRentalList.putExtra("TYPE" , "RANGE");
                intentRentalList.putExtra("MIN" , 24);
                intentRentalList.putExtra("MAX" , 500);
                startActivity(intentRentalList);
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
        TxtRenta_Ptm                =   (TextView)findViewById(R.id.TxtRentaPTP);
        TxtRenta_ManagerAssign      =   (TextView)findViewById(R.id.TxtRentaManager);
        TxtRenta_ActionTranfer      =   (TextView)findViewById(R.id.TxtActionTranfer);
        TxtRenta_TotalAllocate      =   (TextView)findViewById(R.id.TxtTotAlocate);

        //=== Get Data
        double mRenta_1_2=0 , mRenta_2_3=0 , mRenta_3_5=0 , mRenta_5_6=0 , mRenta_6_9=0 , mRenta_9_12=0 , mRenta_12_24=0 , mRenta_24_Above=0 , mRenta_Ptm=0 , mRenta_ManagerAssign=0 ,
                mRenta_ActionTranfer=0 , mRenta_TotalAllocate=0 ;

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
        sqLiteDatabase_MyCaseMain.close();

        //=== Set Data View

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

        if (mRenta_TotalAllocate != 0)
            TxtRenta_TotalAllocate.setText(String.valueOf((int) mRenta_TotalAllocate));
        else
            TxtRenta_TotalAllocate.setVisibility(View.INVISIBLE);

    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }


}
