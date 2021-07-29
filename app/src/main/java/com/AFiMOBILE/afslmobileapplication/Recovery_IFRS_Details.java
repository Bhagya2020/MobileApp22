package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;


public class Recovery_IFRS_Details extends AppCompatActivity {

    private SqlliteCreateRecovery sqlliteCreateRecovery_ifrs_details;
    private TextView m_3_currentmon_due  , m_3_RNT_collect , m_3_OTH_collect , m_3_TOT_collect , m_3_After_NRA ,
            m_6_currentmon_due , m_6_RNT_collect , m_6_OTH_collect , m_6_TOT_collect , m_6_After_NRA;
    private String mInputFacilityNo , LoginUser , LoginDate , LoginBranch , LoginUserName;
    private Double mNoArraysRNT , mAvarage_3_below , mAvarage_6_below ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_ifrs_details);

        Intent intent = getIntent();
        mInputFacilityNo    =   intent.getStringExtra("FacilityNo");

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();

        sqlliteCreateRecovery_ifrs_details = new SqlliteCreateRecovery(this);

        m_3_currentmon_due  =   (TextView)findViewById(R.id.txtbelw_3_currentdue) ;
        m_3_RNT_collect     =   (TextView)findViewById(R.id.txtbelw_3_rnt_collected) ;
        m_3_OTH_collect     =   (TextView)findViewById(R.id.txtbelw_3_oth_collected) ;
        m_3_TOT_collect     =   (TextView)findViewById(R.id.txtbelw_3_total_collected) ;
        m_3_After_NRA       =   (TextView)findViewById(R.id.txtbelw_3_after_nra) ;

        m_6_currentmon_due  =   (TextView)findViewById(R.id.txtbelw_6_currentdue) ;
        m_6_RNT_collect     =   (TextView)findViewById(R.id.txtbelw_6_rnt_collected) ;
        m_6_OTH_collect     =   (TextView)findViewById(R.id.txtbelw_6_oth_collected) ;
        m_6_TOT_collect     =   (TextView)findViewById(R.id.txtbelw_6_total_collected) ;
        m_6_After_NRA       =   (TextView)findViewById(R.id.txtbelw_6_after_nra) ;

        GetIFRSData();

    }


    public void GetIFRSData()
    {
        double mCuurentRental=0.00 , mNeedRNTCollect=0.00 , mNeedOTHcollect=0.00 , mNeedtoCurrentRental , mTotalCollect , mAfterNRA;
        double ODArrays=0.00 , InsArrays=0.00 , OthArrays=0.00;
        int RenDueDate=0 , CurrentDay=0;
        float ConVertNRA;

        SQLiteDatabase sqLiteDatabase_ifrs = sqlliteCreateRecovery_ifrs_details.getReadableDatabase();
        Cursor cursor_ifrs = sqLiteDatabase_ifrs.rawQuery("SELECT Due_Day,Current_Month_Rental,OD_Arrear_Amount,Arrears_Rental_No,Arrears_Rental_Amount,Insurance_Arrears," +
                "OC_Arrears FROM recovery_generaldetail WHERE Facility_Number = '" + mInputFacilityNo + "'" , null );
        if (cursor_ifrs.getCount() != 0)
        {
            cursor_ifrs.moveToFirst();
            mNoArraysRNT = 0.00;

            mAvarage_3_below = 2.5;
            mAvarage_6_below = 5.5;

            DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");

            do {

                //DUEDATE
                if (cursor_ifrs.getString(0) != "")
                {
                    RenDueDate  = Integer.parseInt(cursor_ifrs.getString(0));
                }

                //   if check no of rental arrays  ----
                if (cursor_ifrs.getString(3) != "")
                {
                    mNoArraysRNT = Double.parseDouble(cursor_ifrs.getString(3));
                    ConVertNRA = (float)Math.round(mNoArraysRNT * 100) / 100;
                }
                else {ConVertNRA = 0;}

                if (cursor_ifrs.getString(1) != "")
                {
                    mCuurentRental =  Double.parseDouble(cursor_ifrs.getString(1));
                }
                else {mCuurentRental=0.00;}

                //OTH ARRAYS
                if (cursor_ifrs.getString(2) != "")
                {
                    ODArrays =  Double.parseDouble(cursor_ifrs.getString(2));
                }

                //INS ARRAYS
                if (cursor_ifrs.getString(5) != "")
                {
                    InsArrays =  Double.parseDouble(cursor_ifrs.getString(5));
                }

                //OTH ARRAYS
                if (cursor_ifrs.getString(6) != "")
                {
                    OthArrays =  Double.parseDouble(cursor_ifrs.getString(6));
                }

                Log.e("nra" , String.valueOf(ConVertNRA));

                //===== Get Below 3
                if (ConVertNRA >= 3 && ConVertNRA < 6)
                {
                    mNeedRNTCollect = mCuurentRental * (ConVertNRA - mAvarage_3_below);
                    mNeedOTHcollect = (ODArrays + InsArrays + OthArrays);

                    mAfterNRA = ConVertNRA - (ConVertNRA - mAvarage_3_below);

                    CurrentDay =  Integer.parseInt(LoginDate.substring(9,10)) ;

                    if (RenDueDate > CurrentDay)
                    {
                        mNeedtoCurrentRental = mCuurentRental;
                    }
                    else{mNeedtoCurrentRental=0.00;}

                    mTotalCollect = mNeedRNTCollect + mNeedOTHcollect + mNeedtoCurrentRental;

                    m_3_currentmon_due.setText(nf.format(mNeedtoCurrentRental));
                    m_3_RNT_collect.setText(nf.format(mNeedRNTCollect));
                    m_3_OTH_collect.setText(nf.format(mNeedOTHcollect));
                    m_3_TOT_collect.setText(nf.format(mTotalCollect));
                    m_3_After_NRA.setText(nf.format(mAfterNRA));
                }
                else if (ConVertNRA >= 6)
                {
                    mNeedRNTCollect = mCuurentRental * (ConVertNRA - mAvarage_6_below);
                    mNeedOTHcollect = (ODArrays + InsArrays + OthArrays);

                    CurrentDay =  Integer.parseInt(LoginDate.substring(9,10)) ;

                    if (RenDueDate > CurrentDay)
                    {
                        mNeedtoCurrentRental = mCuurentRental;
                    }
                    else{mNeedtoCurrentRental=0.00;}

                    mAfterNRA = ConVertNRA - (ConVertNRA - mAvarage_6_below);

                    mTotalCollect = mNeedRNTCollect + mNeedOTHcollect + mNeedtoCurrentRental;

                    m_6_currentmon_due.setText(nf.format(mNeedtoCurrentRental));
                    m_6_RNT_collect.setText(nf.format(mNeedRNTCollect));
                    m_6_OTH_collect.setText(nf.format(mNeedOTHcollect));
                    m_6_TOT_collect.setText(nf.format(mTotalCollect));
                    m_6_After_NRA.setText(nf.format(mAfterNRA));
                }

            }while (cursor_ifrs.moveToNext());
        }
    }

}
