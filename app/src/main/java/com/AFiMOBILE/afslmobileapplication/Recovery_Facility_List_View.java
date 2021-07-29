package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Recovery_Facility_List_View extends AppCompatActivity
{
    public TextView mAssignCae , mTotCase;
    public RecyclerView mRecyFacist;
    public SqlliteCreateRecovery sqlliteCreateRecovery_FacList;
    public static Adapter_Recover_FacList mAdapter;
    public String mInpType , mInpMin , mInpMax , LoginUser , LoginDate , LoginBranch , LoginUserName ;
    public int RecordCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_facility_list_view);

        mAssignCae  =   (TextView)findViewById(R.id.TxtAssingCaseName);
        mTotCase    =   (TextView)findViewById(R.id.TxttotalCase);
        mRecyFacist =   (RecyclerView)findViewById(R.id.ReyFacList);

        //=== Get Input Paramater
        Intent intent = getIntent();
        mInpType    =   intent.getStringExtra("TYPE");
        mInpMin     =   intent.getStringExtra("MIN");
        mInpMax     =   intent.getStringExtra("MAX");

        //=== Create Connection And Golble Varible
        sqlliteCreateRecovery_FacList = new SqlliteCreateRecovery(this);
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName = globleClassDetails.getOfficerName();


        //=== Run Process
        if (mInpType.equals("RANGE"))
        {
            GetFacilityList();  // Get Facility List
        }
        else
        {
            LoadclickOverview();
        }
    }


    public void LoadclickOverview()
    {
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"Facility_Number" , "Customer_Full_Name" , "Total_Arrear_Amount" , "C_Address_Line_1" , "C_Address_Line_2" , "C_Address_Line_3" , "C_Address_Line_4" , "Asset_Model" ,
        "Vehicle_Number" , "Last_Payment_Amount" , "Last_Payment_Date" , "Arrears_Rental_No"});

        SQLiteDatabase sqLiteDatabase_FacList = sqlliteCreateRecovery_FacList.getReadableDatabase();
        Cursor cursor_getOverData=null;
        if (mInpType.equals("OVER-ALL"))
        {
            cursor_getOverData = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                    "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "'" , null);
        }
        else if (mInpType.equals("OVER-LOCK"))
        {
            Cursor cursor_locak_case = sqLiteDatabase_FacList.rawQuery("SELECT FACILITY_NO FROM nemf_form_updater WHERE MADE_BY = '" + LoginUser + "' and LIVE_SERVER_UPDATE = ''" , null);
            if (cursor_locak_case.getCount() !=0)
            {
                cursor_locak_case.moveToFirst();
                do {
                    Cursor cursor_getsubData  = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                            "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Facility_Number = '" + cursor_locak_case.getString(0) + "'" , null);

                    if (cursor_getsubData.getCount() != 0)
                    {
                        cursor_getsubData.moveToFirst();
                        mOutPutcouser.addRow(new String[] {cursor_getsubData.getString(0) , cursor_getsubData.getString(1) , cursor_getsubData.getString(2) , cursor_getsubData.getString(3) , cursor_getsubData.getString(4) , cursor_getsubData.getString(5) ,
                                cursor_getsubData.getString(6) , cursor_getsubData.getString(7) , cursor_getsubData.getString(8) , cursor_getsubData.getString(9) , cursor_getsubData.getString(10) , cursor_getsubData.getString(11) });
                    }
                }while (cursor_locak_case.moveToNext());

                cursor_getOverData = mOutPutcouser;
            }
        }
        else if (mInpType.equals("OVER-VIST"))
        {
            Cursor cursor_locak_case = sqLiteDatabase_FacList.rawQuery("SELECT FACILITY_NO FROM nemf_form_updater WHERE MADE_BY = '" + LoginUser + "'" , null);
            if (cursor_locak_case.getCount() !=0)
            {
                cursor_locak_case.moveToFirst();
                do {
                    Cursor cursor_getsubData = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                            "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Facility_Number = '" + cursor_locak_case.getString(0) + "'" , null);

                    if (cursor_getsubData.getCount() != 0)
                    {
                        cursor_getsubData.moveToFirst();
                        mOutPutcouser.addRow(new String[] {cursor_getsubData.getString(0) , cursor_getsubData.getString(1) , cursor_getsubData.getString(2) , cursor_getsubData.getString(3) , cursor_getsubData.getString(4) , cursor_getsubData.getString(5) ,
                                cursor_getsubData.getString(6) , cursor_getsubData.getString(7) , cursor_getsubData.getString(8) , cursor_getsubData.getString(9) , cursor_getsubData.getString(10) , cursor_getsubData.getString(11) });
                    }
                }while (cursor_locak_case.moveToNext());

                cursor_getOverData = mOutPutcouser;
            }
        }
        else if (mInpType.equals("OVER-CLOSE"))
        {
            cursor_getOverData = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                    "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "' AND Facility_Status = 'C'" , null);
        }
        else if (mInpType.equals("OVER-ACTION"))
        {
            Cursor cursor_manager_responces = sqLiteDatabase_FacList.rawQuery("SELECT Facility_No FROM recovery_request_mgr WHERE Request_userid = '" + LoginUser + "'" , null);
            if (cursor_manager_responces.getCount() != 0)
            {
                cursor_manager_responces.moveToFirst();
                do{
                    Cursor cursor_getsubData = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                            "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Facility_Number = '" + cursor_manager_responces.getString(0) + "'" , null);

                    if (cursor_getsubData.getCount() != 0)
                    {
                        cursor_getsubData.moveToFirst();
                        mOutPutcouser.addRow(new String[] {cursor_getsubData.getString(0) , cursor_getsubData.getString(1) , cursor_getsubData.getString(2) , cursor_getsubData.getString(3) , cursor_getsubData.getString(4) , cursor_getsubData.getString(5) ,
                                cursor_getsubData.getString(6) , cursor_getsubData.getString(7) , cursor_getsubData.getString(8) , cursor_getsubData.getString(9) , cursor_getsubData.getString(10) , cursor_getsubData.getString(11) });
                    }

                }while (cursor_manager_responces.moveToNext());
                cursor_getOverData = mOutPutcouser;
            }


        }
        else if (mInpType.equals("OVER-MANAGER"))
        {
            Cursor cursor_manager_assing = sqLiteDatabase_FacList.rawQuery("SELECT Facility_No FROM recovery_request_mgr WHERE Request_userid = '" + LoginUser + "'" , null);
            if (cursor_manager_assing.getCount() != 0)
            {
                cursor_manager_assing.moveToFirst();
                do{
                    Cursor cursor_getsubData = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                            "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Facility_Number = '" + cursor_manager_assing.getString(0) + "'" , null);

                    if (cursor_getsubData.getCount() != 0)
                    {
                        cursor_getsubData.moveToFirst();
                        mOutPutcouser.addRow(new String[] {cursor_getsubData.getString(0) , cursor_getsubData.getString(1) , cursor_getsubData.getString(2) , cursor_getsubData.getString(3) , cursor_getsubData.getString(4) , cursor_getsubData.getString(5) ,
                                cursor_getsubData.getString(6) , cursor_getsubData.getString(7) , cursor_getsubData.getString(8) , cursor_getsubData.getString(9) , cursor_getsubData.getString(10) , cursor_getsubData.getString(11) });
                    }

                }while (cursor_manager_assing.moveToNext());
                cursor_getOverData = mOutPutcouser;
            }
        }
        else if (mInpType.equals("OVER-REPOCESS"))
        {
            cursor_getOverData = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                    "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "' AND Contract_Status = 'R'" , null);
        }
        else if (mInpType.equals("OVER-FULLYPAID"))
        {
            cursor_getOverData = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                    "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "' AND  Total_Arrear_Amount = '0.00' " , null);
        }
        else if (mInpType.equals("OVER-PTP"))
        {
             Cursor cursor_ptp_details = sqLiteDatabase_FacList.rawQuery("SELECT FACILITY_NO FROM nemf_form_updater WHERE MADE_BY = '" + LoginUser + "' AND ACTIONCODE = 'PTP' " , null);
             if (cursor_ptp_details.getCount() != 0)
             {
                cursor_ptp_details.moveToFirst();
                 do{
                     Cursor cursor_getsubData  = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                             "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Facility_Number = '" + cursor_ptp_details.getString(0) + "'" , null);

                     if (cursor_getsubData.getCount() !=0)
                     {
                         cursor_getsubData.moveToFirst();
                         mOutPutcouser.addRow(new String[] {cursor_getsubData.getString(0) , cursor_getsubData.getString(1) , cursor_getsubData.getString(2) , cursor_getsubData.getString(3) , cursor_getsubData.getString(4) , cursor_getsubData.getString(5) ,
                                 cursor_getsubData.getString(6) , cursor_getsubData.getString(7) , cursor_getsubData.getString(8) , cursor_getsubData.getString(9) , cursor_getsubData.getString(10) , cursor_getsubData.getString(11) });
                     }
                 }while (cursor_ptp_details.moveToNext());
                 cursor_getOverData = mOutPutcouser;
             }
        }
        else if (mInpType.equals("OVER-SPECIAL"))
        {
            Cursor cursor_sps_details = sqLiteDatabase_FacList.rawQuery("SELECT facility_no FROM Recovery_speical_backet WHERE recovery_executive = '" + LoginUser + "'" , null);
            if (cursor_sps_details.getCount() != 0)
            {
                cursor_sps_details.moveToFirst();
                do{
                    Cursor cursor_getsubData  = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                            "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Facility_Number = '" + cursor_sps_details.getString(0) + "'" , null);

                    if (cursor_getsubData.getCount() !=0)
                    {
                        cursor_getsubData.moveToFirst();
                        mOutPutcouser.addRow(new String[] {cursor_getsubData.getString(0) , cursor_getsubData.getString(1) , cursor_getsubData.getString(2) , cursor_getsubData.getString(3) , cursor_getsubData.getString(4) , cursor_getsubData.getString(5) ,
                                cursor_getsubData.getString(6) , cursor_getsubData.getString(7) , cursor_getsubData.getString(8) , cursor_getsubData.getString(9) , cursor_getsubData.getString(10) , cursor_getsubData.getString(11) });
                    }
                    cursor_getsubData.close();
                }while (cursor_sps_details.moveToNext());
                cursor_getOverData = mOutPutcouser;
            }
            cursor_sps_details.close();
        }


        mTotCase.setText("Total No of Facility - " + String.valueOf(RecordCount));
        mAssignCae.setText(" Facility List - " + mInpType);
        //=== Recycviwe load the data
        mRecyFacist.setHasFixedSize(true);
        mRecyFacist.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_Recover_FacList(this , cursor_getOverData , "NORMAL");
        mRecyFacist.setAdapter(mAdapter);
    }

    public void GetFacilityList()
    {
        mAssignCae.setText(mInpMin + " - " + mInpMax + " Facility List");
        //=== Recycviwe load the data
        mRecyFacist.setHasFixedSize(true);
        mRecyFacist.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_Recover_FacList(this , GetFacilityListData() , "NORMAL");
        mRecyFacist.setAdapter(mAdapter);
    }

    public Cursor GetFacilityListData()
    {
        SQLiteDatabase sqLiteDatabase_FacList = sqlliteCreateRecovery_FacList.getReadableDatabase();
        Cursor cursor_FacList;

        if (mInpMin.equals("0")  && mInpMax.equals("2"))
        {
            cursor_FacList = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                    "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "' and " +
                    "CAST(Arrears_Rental_No as DOUBLE) >= " + mInpMin + " and  " +
                    "CAST(Arrears_Rental_No as DOUBLE) <= " + mInpMax , null);
        }
        else
        {
            cursor_FacList = sqLiteDatabase_FacList.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                    "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "' and " +
                    "CAST(Arrears_Rental_No as DOUBLE) > " + mInpMin + " and  " +
                    "CAST(Arrears_Rental_No as DOUBLE) <= " + mInpMax , null);
        }

        mTotCase.setText("Total No of Facility - " + String.valueOf(cursor_FacList.getCount()));
        return cursor_FacList;
    }

    protected void onStart()
    {
        super.onStart();

        if (mInpType.equals("RANGE"))
        {
            GetFacilityList();  // Get Facility List
        }
        else
        {
            LoadclickOverview();
        }


        //mAdapter.swapCursor(GetFacilityListData());
    }

    protected void onDestroy(){

        sqlliteCreateRecovery_FacList.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

}
