package com.AFiMOBILE.afslmobileapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.text.DecimalFormat;

public class Recovery_Buket_Movement extends AppCompatActivity
{

    SqlliteCreateRecovery sqlliteCreateRecovery_buket;
    private String LoginUser , LoginDate , LoginBranch , LoginUserName;
    private TextView mTxt_beg_0_2 , mTxt_beg_2_3 , mTxt_beg_3_5 , mTxt_beg_5_6 , mTxt_beg_6;
    private TextView mTxt_cur_0_2 , mTxt_cur_2_3 , mTxt_cur_3_5 , mTxt_cur_5_6 , mTxt_cur_6;
    private TextView mTxt_sum_0_2 , mTxt_sum_2_3 , mTxt_sum_3_5 , mTxt_sum_5_6 , mTxt_sum_6;
    private RadioButton mRadioFacCount , mRadioFacAmount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_buket_movment);

        sqlliteCreateRecovery_buket = new SqlliteCreateRecovery(this);
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();

        mTxt_beg_0_2    =   findViewById(R.id.begin_0_1);
        mTxt_beg_2_3    =   findViewById(R.id.begin_2_3);
        mTxt_beg_3_5    =   findViewById(R.id.begin_3_5);
        mTxt_beg_5_6    =   findViewById(R.id.begin_5_6);
        mTxt_beg_6      =   findViewById(R.id.begin_over_6);


        mTxt_cur_0_2    =   findViewById(R.id.current_0_1);
        mTxt_cur_2_3    =   findViewById(R.id.current_2_3);
        mTxt_cur_3_5    =   findViewById(R.id.current_3_5);
        mTxt_cur_5_6    =   findViewById(R.id.current_5_6);
        mTxt_cur_6      =   findViewById(R.id.current_over_6);

        mTxt_sum_0_2    =   findViewById(R.id.summery_0_1);
        mTxt_sum_2_3    =   findViewById(R.id.summery_2_3);
        mTxt_sum_3_5    =   findViewById(R.id.summery_3_5);
        mTxt_sum_5_6    =   findViewById(R.id.summery_5_6);
        mTxt_sum_6      =   findViewById(R.id.summery_over_6);

        mRadioFacCount  =   findViewById(R.id.radioContactWise);
        mRadioFacAmount =   findViewById(R.id.radioamounttWise);


        mRadioFacCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFacilityCountSummery();
            }
        });

        mRadioFacAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFacilityAmountSummery();
            }
        });
    }

    public void GetFacilityAmountSummery()
    {
        double current_0_2 = 0.00 , current_2_3 = 0.00 , current_3_5 = 0.00 , current_5_6 = 0.00 , current_6 = 0.00 ;
        double begin_0_2 = 0.00 , begin_2_3 = 0.00 , begin_3_5 = 0.00 , begin_5_6 = 0.00 , begin_6 = 0.00 ;
        double Summery_0_2 = 0.00 , Summery_2_3 = 0.00 , Summery_3_5 = 0.00 , Summery_5_6 = 0.00 , Summery_6 = 0.00 ;
        double mNoArrays=0.00 , mCurrentRental=0.00;

        //=== Get Month Begin Facility Count Summery
        SQLiteDatabase sqLiteDatabase_getcount = sqlliteCreateRecovery_buket.getReadableDatabase();
        Cursor  cursor_get_count = sqLiteDatabase_getcount.rawQuery("SELECT No_Rnt_arrays , Total_arrays from Recovery_Month_Begin WHERE recovery_executive = '" + LoginUser + "'" , null );
        if (cursor_get_count.getCount() != 0)
        {
            Log.d("recovery" , "done");
            cursor_get_count.moveToFirst();
            do{
                mNoArrays = Double.parseDouble(cursor_get_count.getString(0));


                if (mNoArrays >= 0 && mNoArrays <= 2)
                {
                    begin_0_2 = begin_0_2 + Double.parseDouble(cursor_get_count.getString(1).replace("," , ""));
                }
                else if (mNoArrays > 2 && mNoArrays <= 3)
                {
                    begin_2_3   =   begin_2_3 + Double.parseDouble(cursor_get_count.getString(1).replace("," , ""));
                }
                else if (mNoArrays > 3 && mNoArrays <= 5)
                {
                    begin_3_5   =   begin_3_5 + Double.parseDouble(cursor_get_count.getString(1).replace("," , ""));
                }
                else if (mNoArrays > 5 && mNoArrays <= 6)
                {
                    begin_5_6   =   begin_5_6   +   Double.parseDouble(cursor_get_count.getString(1).replace("," , ""));
                }
                else if (mNoArrays > 6)
                {
                    begin_6     =   begin_6     +   Double.parseDouble(cursor_get_count.getString(1).replace("," , ""));
                }
            }while (cursor_get_count.moveToNext());

        }
        cursor_get_count.close();
        //===============================================================

        //=== Get Current Facillty Count summery
        Cursor cursor_get_current = sqLiteDatabase_getcount.rawQuery("SELECT Arrears_Rental_No , Total_Arrear_Amount FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "'" , null);
        if (cursor_get_current.getCount() != 0)
        {
            cursor_get_current.moveToFirst();
            do{
                mCurrentRental = Double.parseDouble(cursor_get_current.getString(0));

                if (mCurrentRental >= 0 && mCurrentRental <= 2)
                {
                    current_0_2 =   current_0_2 +   Double.parseDouble(cursor_get_current.getString(1).replace("," , ""));
                }
                else if (mCurrentRental > 2 && mCurrentRental <= 3)
                {
                    current_2_3 =   current_2_3 + Double.parseDouble(cursor_get_current.getString(1).replace("," , ""));
                }
                else if (mCurrentRental > 3 && mCurrentRental <= 5)
                {
                    current_3_5 =   current_3_5 + Double.parseDouble(cursor_get_current.getString(1).replace("," , ""));
                }
                else if (mCurrentRental > 5 && mCurrentRental <= 6)
                {
                    current_5_6 =   current_5_6 + Double.parseDouble(cursor_get_current.getString(1).replace("," , ""));
                }
                else if (current_5_6 > 6)
                {
                    current_6   =   current_6 + Double.parseDouble(cursor_get_current.getString(1).replace("," , ""));
                }
            }while (cursor_get_current.moveToNext());
        }
        cursor_get_current.close();
        sqLiteDatabase_getcount.close();

        Log.d("begin" , String.valueOf(begin_0_2));

        Summery_0_2     =   begin_0_2 - current_0_2;
        Summery_2_3     =   begin_2_3 - current_2_3;
        Summery_3_5     =   begin_3_5 - current_3_5;
        Summery_5_6     =   begin_5_6 - current_5_6;
        Summery_6       =   begin_6   - current_6;

        DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");

        mTxt_beg_0_2.setText(nf.format(begin_0_2 / 1000));
        mTxt_beg_2_3.setText(nf.format(begin_2_3 / 1000));
        mTxt_beg_3_5.setText(nf.format(begin_3_5 / 1000));
        mTxt_beg_5_6.setText(nf.format(begin_5_6 / 1000));
        mTxt_beg_6.setText(nf.format(begin_6 / 1000));

        mTxt_cur_0_2.setText(nf.format(current_0_2 / 1000));
        mTxt_cur_2_3.setText(nf.format(current_2_3 / 1000));
        mTxt_cur_3_5.setText(nf.format(current_3_5 / 1000));
        mTxt_cur_5_6.setText(nf.format(current_5_6 / 1000));
        mTxt_cur_6.setText(nf.format(current_6 / 1000));

        mTxt_sum_0_2.setText(nf.format(Summery_0_2 / 1000));
        mTxt_sum_2_3.setText(nf.format(Summery_2_3 / 1000));
        mTxt_sum_3_5.setText(nf.format(Summery_3_5 / 1000));
        mTxt_sum_5_6.setText(nf.format(Summery_5_6 / 1000));
        mTxt_sum_6.setText(nf.format(Summery_6 / 1000));
    }

    public void GetFacilityCountSummery()
    {
        double current_0_2 = 0.00 , current_2_3 = 0.00 , current_3_5 = 0.00 , current_5_6 = 0.00 , current_6 = 0.00 ;
        double begin_0_2 = 0.00 , begin_2_3 = 0.00 , begin_3_5 = 0.00 , begin_5_6 = 0.00 , begin_6 = 0.00 ;
        double Summery_0_2 = 0.00 , Summery_2_3 = 0.00 , Summery_3_5 = 0.00 , Summery_5_6 = 0.00 , Summery_6 = 0.00 ;
        double mNoArrays=0.00 , mCurrentRental=0.00;

        //=== Get Month Begin Facility Count Summery
        SQLiteDatabase sqLiteDatabase_getcount = sqlliteCreateRecovery_buket.getReadableDatabase();
        Cursor  cursor_get_count = sqLiteDatabase_getcount.rawQuery("SELECT No_Rnt_arrays FROM Recovery_Month_Begin WHERE recovery_executive = '" + LoginUser + "'" , null );
        if (cursor_get_count.getCount() != 0)
        {
            Log.d("recovery" , "done");
            cursor_get_count.moveToFirst();
            do{
                mNoArrays = Double.parseDouble(cursor_get_count.getString(0));


                if (mNoArrays >= 0 && mNoArrays <= 2)
                {
                    begin_0_2 = begin_0_2 + 1;
                }
                else if (mNoArrays > 2 && mNoArrays <= 3)
                {
                    begin_2_3   =   begin_2_3 + 1;
                }
                else if (mNoArrays > 3 && mNoArrays <= 5)
                {
                    begin_3_5   =   begin_3_5 + 1;
                }
                else if (mNoArrays > 5 && mNoArrays <= 6)
                {
                    begin_5_6   =   begin_5_6   +   1;
                }
                else if (mNoArrays > 6)
                {
                    begin_6     =   begin_6     +   1;
                }
            }while (cursor_get_count.moveToNext());

        }
        cursor_get_count.close();
        //===============================================================

        //=== Get Current Facillty Count summery
        Cursor cursor_get_current = sqLiteDatabase_getcount.rawQuery("SELECT Arrears_Rental_No FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "'" , null);
        if (cursor_get_current.getCount() != 0)
        {
            cursor_get_current.moveToFirst();
            do{
                mCurrentRental = Double.parseDouble(cursor_get_current.getString(0));

                if (mCurrentRental >= 0 && mCurrentRental <= 2)
                {
                    current_0_2 =   current_0_2 +   1;
                }
                else if (mCurrentRental > 2 && mCurrentRental <= 3)
                {
                    current_2_3 =   current_2_3 + 1;
                }
                else if (mCurrentRental > 3 && mCurrentRental <= 5)
                {
                    current_3_5 =   current_3_5 + 1;
                }
                else if (mCurrentRental > 5 && mCurrentRental <= 6)
                {
                    current_5_6 =   current_5_6 + 1;
                }
                else if (current_5_6 > 6)
                {
                    current_6   =   current_6 + 1;
                }
            }while (cursor_get_current.moveToNext());
        }
        cursor_get_current.close();
        sqLiteDatabase_getcount.close();

        Log.d("begin" , String.valueOf(begin_0_2));

        Summery_0_2     =   begin_0_2 - current_0_2;
        Summery_2_3     =   begin_2_3 - current_2_3;
        Summery_3_5     =   begin_3_5 - current_3_5;
        Summery_5_6     =   begin_5_6 - current_5_6;
        Summery_6       =   begin_6   - current_6;

        mTxt_beg_0_2.setText(String.valueOf(begin_0_2));
        mTxt_beg_2_3.setText(String.valueOf(begin_2_3));
        mTxt_beg_3_5.setText(String.valueOf(begin_3_5));
        mTxt_beg_5_6.setText(String.valueOf(begin_5_6));
        mTxt_beg_6.setText(String.valueOf(begin_6));

        mTxt_cur_0_2.setText(String.valueOf(current_0_2));
        mTxt_cur_2_3.setText(String.valueOf(current_2_3));
        mTxt_cur_3_5.setText(String.valueOf(current_3_5));
        mTxt_cur_5_6.setText(String.valueOf(current_5_6));
        mTxt_cur_6.setText(String.valueOf(current_6));

        mTxt_sum_0_2.setText(String.valueOf(Summery_0_2));
        mTxt_sum_2_3.setText(String.valueOf(Summery_2_3));
        mTxt_sum_3_5.setText(String.valueOf(Summery_3_5));
        mTxt_sum_5_6.setText(String.valueOf(Summery_5_6));
        mTxt_sum_6.setText(String.valueOf(Summery_6));

    }
}
