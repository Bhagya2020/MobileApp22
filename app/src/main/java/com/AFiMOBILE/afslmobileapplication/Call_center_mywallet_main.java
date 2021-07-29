package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Call_center_mywallet_main extends AppCompatActivity {

    private String LoginUser , LoginDate , LoginBranch , LoginUserName ;
    private Spinner mSortDtails;
    private RecyclerView mDataRecyView;
    private SqlliteCreateRecovery sqlliteCreateRecovery_mywallet;
    private Adapter_call_my_wallet_facility adapter_call_my_wallet_facility;
    private ArrayAdapter<String> arrayTITLE;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_center_mywallet_main);


        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();


        mSortDtails = (Spinner)findViewById(R.id.txtsortorder);
        //=== Load ShotOrder
        List<String> listTITLE = new ArrayList<String>();
        listTITLE.add("ALL");
        listTITLE.add("PTP");
        listTITLE.add("UN-CONTACT");
        listTITLE.add("BROKEN PTP");
        arrayTITLE = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listTITLE);
        arrayTITLE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortDtails.setAdapter(arrayTITLE);

        sqlliteCreateRecovery_mywallet = new SqlliteCreateRecovery(this);

        mDataRecyView   =   (RecyclerView)findViewById(R.id.ReyFacList);
        mDataRecyView.setHasFixedSize(true);
        mDataRecyView.setLayoutManager(new LinearLayoutManager(this));
        adapter_call_my_wallet_facility = new Adapter_call_my_wallet_facility (this , LoadData());
        mDataRecyView.setAdapter(adapter_call_my_wallet_facility);


        //=== Load sort details
        mSortDtails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mSortDtails.getSelectedItem().toString() != "")
                {
                    if (mSortDtails.getSelectedItem().toString().equals("ALL"))
                    {
                        adapter_call_my_wallet_facility.swapCursor(LoadData());
                        mDataRecyView.setAdapter(adapter_call_my_wallet_facility);
                    }
                    else if (mSortDtails.getSelectedItem().toString().equals("PTP"))
                    {
                        adapter_call_my_wallet_facility.swapCursor(LoadData_ptp());
                        mDataRecyView.setAdapter(adapter_call_my_wallet_facility);
                    }
                    else if (mSortDtails.getSelectedItem().toString().equals("UN-CONTACT"))
                    {
                        adapter_call_my_wallet_facility.swapCursor(LoadData_uncontact());
                        mDataRecyView.setAdapter(adapter_call_my_wallet_facility);
                    }
                    else if (mSortDtails.getSelectedItem().toString().equals("BROKEN PTP"))
                    {
                        adapter_call_my_wallet_facility.swapCursor(LoadData_Broken());
                        mDataRecyView.setAdapter(adapter_call_my_wallet_facility);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Cursor LoadData_Broken()
    {
        int mCount=0;
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] { "Seriel_no" ,"Facility_Number" , "ACTION_DATE" , "ACTION_CODE"});
        SQLiteDatabase sqLiteDatabase_mycase = sqlliteCreateRecovery_mywallet.getReadableDatabase();

        Cursor cursor_ptp = sqLiteDatabase_mycase.rawQuery("select FACNO , CALL_PTP_DATE , CALL_ACTION_CODE from recovery_call_center_action where ALOCATE_OFF_CODE = '" + LoginUser + "'  and ACTION_STS = '' and (CALL_ACTION_CODE = '001' or CALL_ACTION_CODE = '002') " , null);
        if (cursor_ptp.getCount() != 0)
        {
            cursor_ptp.moveToFirst();
            do {
                Date dateptp=new Date();
                Date datecurrent=new Date();

                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String datenow=cursor_ptp.getString(1);


                try {
                    dateptp = (Date) formatter.parse(datenow);
                    datecurrent = (Date) formatter.parse(LoginDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long diff_date = datecurrent.getTime() - dateptp.getTime();
                int diff = (int) (diff_date / (24 * 60 * 60 * 1000));

                if (diff>0)
                {
                    mCount = mCount + 1;
                    mOutPutcouser.addRow(new String[] { String.valueOf(mCount) , cursor_ptp.getString(0) , cursor_ptp.getString(1) , cursor_ptp.getString(2)  });
                }
            }while (cursor_ptp.moveToNext());
        }

        return mOutPutcouser;

    }

    private Cursor LoadData_ptp()
    {
        int mCount=0;
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] { "Seriel_no" ,"Facility_Number" , "ACTION_DATE" , "ACTION_CODE"});

        SQLiteDatabase sqLiteDatabase_mycase = sqlliteCreateRecovery_mywallet.getReadableDatabase();

        Cursor cursor_uncon = sqLiteDatabase_mycase.rawQuery("SELECT FACNO , CALL_ACTION_DATE , CALL_ACTION_CODE FROM recovery_call_center_action WHERE ALOCATE_OFF_CODE = '" + LoginUser + "' and " +
                "(CALL_ACTION_CODE = '001' or CALL_ACTION_CODE = '002')",null );
        if (cursor_uncon.getCount() != 0)
        {
            cursor_uncon.moveToFirst();
            do {
                mCount = mCount + 1;
                mOutPutcouser.addRow(new String[] { String.valueOf(mCount) , cursor_uncon.getString(0) , cursor_uncon.getString(1) , cursor_uncon.getString(2)  });
            }while (cursor_uncon.moveToNext());
        }

        return mOutPutcouser;
    }

    private Cursor LoadData_uncontact()
    {
        int mCount=0;
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] { "Seriel_no" ,"Facility_Number" , "ACTION_DATE" , "ACTION_CODE"});

        SQLiteDatabase sqLiteDatabase_mycase = sqlliteCreateRecovery_mywallet.getReadableDatabase();

        Cursor cursor_uncon = sqLiteDatabase_mycase.rawQuery("SELECT FACNO , CALL_ACTION_DATE , CALL_ACTION_CODE FROM recovery_call_center_action WHERE ALOCATE_OFF_CODE = '" + LoginUser + "' and " +
                "(CALL_ACTION_CODE <> '001' and CALL_ACTION_CODE <> '002')",null );
        if (cursor_uncon.getCount() != 0)
        {
            cursor_uncon.moveToFirst();
            do {
                mCount = mCount + 1;
                mOutPutcouser.addRow(new String[] { String.valueOf(mCount) , cursor_uncon.getString(0) , cursor_uncon.getString(1) , cursor_uncon.getString(2)  });


            }while (cursor_uncon.moveToNext());
        }
        return mOutPutcouser;
    }

    private Cursor LoadData()
    {
        int mCount=0;
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] { "Seriel_no" ,"Facility_Number" , "ACTION_DATE" , "ACTION_CODE"});
        SQLiteDatabase sqLiteDatabase_mycase = sqlliteCreateRecovery_mywallet.getReadableDatabase();

        Cursor cursor_uncon = sqLiteDatabase_mycase.rawQuery("SELECT FACNO , CALL_ACTION_DATE , CALL_ACTION_CODE FROM recovery_call_center_action WHERE ALOCATE_OFF_CODE = '" + LoginUser + "'",null );
        if (cursor_uncon.getCount() != 0)
        {
            cursor_uncon.moveToFirst();
            do {
                mCount = mCount + 1;

                mOutPutcouser.addRow(new String[] { String.valueOf(mCount) , cursor_uncon.getString(0) , cursor_uncon.getString(1) , cursor_uncon.getString(2)  });

            }while (cursor_uncon.moveToNext());
        }

        return mOutPutcouser;
    }

}
