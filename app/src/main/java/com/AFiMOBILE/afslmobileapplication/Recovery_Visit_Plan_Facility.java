package com.AFiMOBILE.afslmobileapplication;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Recovery_Visit_Plan_Facility extends AppCompatActivity {


    public SqlliteCreateRecovery sqlliteCreateRecovery_Visit_Plan;
    public String PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , LoginUserName ;
    public TextView mPendingCase , mCompleteCase;
    public Adapter_Vist_Plan adapter_vist_plan;
    public RecyclerView mRecyPendingReq;
    int CountPendingCase , CountcompleteCase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_visit_plan_facility);


        //=== Create golbel Varible
        sqlliteCreateRecovery_Visit_Plan = new SqlliteCreateRecovery(this);
        //=== Create Varible
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();
        mPendingCase    = (TextView)findViewById(R.id.TotcountPendingCase);
        mCompleteCase   = (TextView)findViewById(R.id.TotcountComplete);


        //=== Load Visit Data
        mRecyPendingReq =   (RecyclerView)findViewById(R.id.ReyFacList);
        mRecyPendingReq.setHasFixedSize(true);
        mRecyPendingReq.setLayoutManager(new LinearLayoutManager(Recovery_Visit_Plan_Facility.this));
        adapter_vist_plan = new Adapter_Vist_Plan(Recovery_Visit_Plan_Facility.this , LoadVistData() , "VISIT");
        mRecyPendingReq.setAdapter(adapter_vist_plan);

    }

    protected void onStart()
    {
        super.onStart();
        adapter_vist_plan.swapCursor(LoadVistData());
        mRecyPendingReq.setAdapter(adapter_vist_plan);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public Cursor LoadVistData()
    {
        CountPendingCase=0 ;  CountcompleteCase=0;
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"FacilityNo" , "ClientName" , "Adders"});
        startManagingCursor(mOutPutcouser);

        SQLiteDatabase sqLiteDatabase_Vist_data = sqlliteCreateRecovery_Visit_Plan.getReadableDatabase();
        Cursor cursor_get_visit = sqLiteDatabase_Vist_data.rawQuery("SELECT facility_no FROM recovery_visit_plan WHERE user_id = '" + LoginUser + "' AND plan_sts = '' AND plan_Date = '" + LoginDate + "'"  , null );
        if (cursor_get_visit.getCount() != 0)
        {
            cursor_get_visit.moveToFirst();
            do {
                    Cursor cursorCheckfacility = sqLiteDatabase_Vist_data.rawQuery("SELECT FACILITY_NO FROM nemf_form_updater WHERE FACILITY_NO = '" + cursor_get_visit.getString(0) + "' AND ACTION_DATE = '" + LoginDate + "'" , null );
                    if (cursorCheckfacility.getCount() ==0)
                    {
                        Cursor cursor_getReggen = sqLiteDatabase_Vist_data.rawQuery("SELECT Facility_Number,Client_Name,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4 FROM recovery_generaldetail WHERE Facility_Number = '" + cursor_get_visit.getString(0) + "'" ,null);
                        if (cursor_getReggen.getCount() != 0)
                        {

                            cursor_getReggen.moveToFirst();
                            mOutPutcouser.addRow(new String[] {cursor_getReggen.getString(0) , cursor_getReggen.getString(1) , (cursor_getReggen.getString(2) + "," + cursor_getReggen.getString(3) + "," + cursor_getReggen.getString(4) + "," + cursor_getReggen.getString(5) )});
                        }
                        CountPendingCase = CountPendingCase + 1;
                    }
                    else
                    {
                        CountcompleteCase = CountcompleteCase + 1;
                    }

            }while (cursor_get_visit.moveToNext());
        }

        mPendingCase.setText(String.valueOf(CountPendingCase));
        mCompleteCase.setText(String.valueOf(CountPendingCase));
        return mOutPutcouser;
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
