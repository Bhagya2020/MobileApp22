package com.AFiMOBILE.afslmobileapplication;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Recovery_Distance_Details extends AppCompatActivity {

    public SqlliteCreateRecovery sqlliteCreateRecovery_distance;
    public String LoginUser,LoginDate,LoginBranch,LoginUserName;
    public RecyclerView mRecyPendingReq;
    public Adapter_Vist_Plan adapter_vist_plan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_distance_details);

        //==== Create Globle Varible
        sqlliteCreateRecovery_distance = new SqlliteCreateRecovery(this);
        //=== Create Connection And Golble Varible
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName = globleClassDetails.getOfficerName();


        mRecyPendingReq =   (RecyclerView)findViewById(R.id.ReyFacList);
        mRecyPendingReq.setHasFixedSize(true);
        mRecyPendingReq.setLayoutManager(new LinearLayoutManager(Recovery_Distance_Details.this));
        adapter_vist_plan = new Adapter_Vist_Plan(Recovery_Distance_Details.this , LoadDistanceData() , "Distance");
        mRecyPendingReq.setAdapter(adapter_vist_plan);
    }

    protected void onStart()
    {
        super.onStart();
        adapter_vist_plan.swapCursor(LoadDistanceData());
        mRecyPendingReq.setAdapter(adapter_vist_plan);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public Cursor LoadDistanceData()
    {
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"FacilityNo" , "ClientName" , "Adders"});
        startManagingCursor(mOutPutcouser);

        SQLiteDatabase sqLiteDatabase_distance = sqlliteCreateRecovery_distance.getReadableDatabase();
        Cursor cursor_distance = sqLiteDatabase_distance.rawQuery("SELECT FACILITY_NO,NAME,ADDRESS FROM nemf_form_updater WHERE MADE_BY = '" + LoginUser + "'" , null);
        if (cursor_distance.getCount() != 0)
        {
            cursor_distance.moveToFirst();
            do{
                Cursor cursor_check_data = sqLiteDatabase_distance.rawQuery("SELECT * FROM recovery_distance_data WHERE facility_no = '" + cursor_distance.getString(0) + "'" , null );
                if (cursor_check_data.getCount() == 0)
                {
                    Cursor cursor_getReggen = sqLiteDatabase_distance.rawQuery("SELECT Facility_Number,Client_Name,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4 FROM recovery_generaldetail WHERE Facility_Number = '" + cursor_distance.getString(0) + "'" ,null);
                    if (cursor_getReggen.getCount() != 0)
                    {
                        cursor_getReggen.moveToFirst();
                        mOutPutcouser.addRow(new String[] {cursor_getReggen.getString(0) , cursor_getReggen.getString(1) , (cursor_getReggen.getString(2) + "," + cursor_getReggen.getString(3) + "," + cursor_getReggen.getString(4) + "," + cursor_getReggen.getString(5) )});
                    }
                }

            }while (cursor_distance.moveToNext());
        }
        return mOutPutcouser;
    }

    protected void onDestroy(){
        sqlliteCreateRecovery_distance.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
