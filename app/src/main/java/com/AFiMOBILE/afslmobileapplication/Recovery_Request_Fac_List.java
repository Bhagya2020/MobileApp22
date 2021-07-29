package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Recovery_Request_Fac_List extends AppCompatActivity {

    public SqlliteCreateRecovery sqlliteCreateRecovery_ManagerRequest;
    public String PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , LoginUserName;
    public TextView mTxtTotalCase , mTxtAcceptCase , mTxtRejectCase , mTxtcompleteCase , mTxtPendingAccept , mTxtPendingComplete;
    public Button mBtnPendingRequest;
    public static Adapter_Recover_FacList mAdapter;
    public RecyclerView mRecyFacist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_request_fac_list);

        //=== Create Varible
        sqlliteCreateRecovery_ManagerRequest = new SqlliteCreateRecovery(this);
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();


        //=== Pending Request
        mBtnPendingRequest      =   (Button)findViewById(R.id.btnRequestFacility);
        mBtnPendingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_epndingReq    = new Intent("android.intent.action.Recovery_Request_Pending_Case");
                startActivity(intent_epndingReq);
            }
        });

        //=== Get Case Count
        GetDataCount();


        //==== Load Browse Data
        /*
        mRecyFacist =   (RecyclerView)findViewById(R.id.ReyFacList);
        //=== Recycviwe load the data
        mRecyFacist.setHasFixedSize(true);
        mRecyFacist.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_Recover_FacList(this , LoadRequestFaciltyData() , "NORMAL");
        mRecyFacist.setAdapter(mAdapter);
        */
    }

    protected void onStart()
    {
        super.onStart();
        GetDataCount();

        mRecyFacist =   (RecyclerView)findViewById(R.id.ReyFacList);
        //=== Recycviwe load the data
        mRecyFacist.setHasFixedSize(true);
        mRecyFacist.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_Recover_FacList(this , LoadRequestFaciltyData() , "NORMAL");
        mRecyFacist.setAdapter(mAdapter);

    }


    public Cursor LoadRequestFaciltyData()
    {
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"Facility_Number" , "Customer_Full_Name" , "Total_Arrear_Amount" , "C_Address_Line_1" , "C_Address_Line_2" , "C_Address_Line_3" , "C_Address_Line_4" , "Asset_Model" ,
                "Vehicle_Number" , "Last_Payment_Amount" , "Last_Payment_Date" , "Arrears_Rental_No" });
        startManagingCursor(mOutPutcouser);

        SQLiteDatabase sqLiteDatabase_ViewRequestData = sqlliteCreateRecovery_ManagerRequest.getReadableDatabase();
        Cursor cursor_Get_Requesr = sqLiteDatabase_ViewRequestData.rawQuery("SELECT Facility_No FROM recovery_request_mgr WHERE Assign_MgrCode = '" + LoginUser + "' AND" +
                " Mgr_Respoce_Sts = 'A' AND Req_done_sts = ''" , null );
        if (cursor_Get_Requesr.getCount() != 0)
        {
            cursor_Get_Requesr.moveToFirst();
            do{
                Cursor cursor_getdata_recgen = sqLiteDatabase_ViewRequestData.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                        "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Facility_Number = '" + cursor_Get_Requesr.getString(0) + "'" , null );
                if (cursor_getdata_recgen.getCount() != 0)
                {
                    cursor_getdata_recgen.moveToFirst();
                        mOutPutcouser.addRow(new String[] {cursor_getdata_recgen.getString(0) , cursor_getdata_recgen.getString(1) , cursor_getdata_recgen.getString(2) , cursor_getdata_recgen.getString(3) ,
                                cursor_getdata_recgen.getString(4) , cursor_getdata_recgen.getString(5) , cursor_getdata_recgen.getString(6) , cursor_getdata_recgen.getString(7) , cursor_getdata_recgen.getString(8) ,
                                cursor_getdata_recgen.getString(9) , cursor_getdata_recgen.getString(10) , cursor_getdata_recgen.getString(11) });
                }
            }while (cursor_Get_Requesr.moveToNext());
        }
        return mOutPutcouser;
    }

    public void GetDataCount()
    {

        mTxtTotalCase           =   (TextView)findViewById(R.id.TxttotRequesr);
        mTxtAcceptCase          =   (TextView)findViewById(R.id.TxtAceptRequest);
        mTxtRejectCase             =   (TextView)findViewById(R.id.TxtRejectRequest);
        mTxtcompleteCase        =   (TextView)findViewById(R.id.TxtcompleteRequest);
        mTxtPendingAccept       =   (TextView)findViewById(R.id.TxtPendingAccept);
        mTxtPendingComplete     =   (TextView)findViewById(R.id.TxtPendingCompelet);

        double mtotcount=0 , mAcceptCase=0 , mRejectCase=0 , mComplete=0 , mPendingAccept=0 , mPendingcomplete=0;
        SQLiteDatabase sqLiteDatabase_getcount = sqlliteCreateRecovery_ManagerRequest.getReadableDatabase();
        Cursor cursor_getcount = sqLiteDatabase_getcount.rawQuery("SELECT * FROM recovery_request_mgr where Assign_MgrCode = '" + LoginUser + "'" , null  );
        if (cursor_getcount.getCount() != 0)
        {
            cursor_getcount.moveToFirst();

            do{
                mtotcount = mtotcount + 1;

                if (cursor_getcount.getString(7).equals("A"))
                {
                    mAcceptCase =   mAcceptCase + 1;
                }

                if (cursor_getcount.getString(7).equals("R"))
                {
                    mRejectCase =   mRejectCase + 1;
                }
                else
                {
                    //=== Get Complete Facility Details
                    Cursor cursor_GetActionSts = sqLiteDatabase_getcount.rawQuery("SELECT * FROM nemf_form_updater WHERE FACILITY_NO = '" + cursor_getcount.getString(0) + "'" , null );
                    if (cursor_GetActionSts.getCount() != 0)
                    {
                        mComplete =   mComplete + 1;
                    }
                    else
                    {
                        mPendingcomplete    = mPendingcomplete + 1;
                    }
                }

                if (cursor_getcount.getString(7).equals(""))
                {
                    mPendingAccept =   mPendingAccept + 1;
                }
            }while (cursor_getcount.moveToNext());


            if (mtotcount==0)
            {
                mTxtTotalCase.setText("0");
            }
            else
            {
                mTxtTotalCase.setText(String.valueOf((int) mtotcount));
            }

            if (mAcceptCase==0)
            {
                mTxtAcceptCase.setText("0");
            }
            else
            {
                mTxtAcceptCase.setText(String.valueOf((int) mAcceptCase));
            }

            if (mRejectCase == 0)
            {
                mTxtRejectCase.setText("0");
            }
            else
            {
                mTxtRejectCase.setText(String.valueOf((int) mRejectCase));
            }


            if (mComplete == 0)
            {
                mTxtcompleteCase.setText("0");
            }
            else
            {
                mTxtcompleteCase.setText(String.valueOf((int) mComplete));
            }


            if (mPendingAccept == 0)
            {
                mTxtPendingAccept.setText("0");
            }
            else
            {
                mTxtPendingAccept.setText(String.valueOf((int) mPendingAccept));
            }

            if (mPendingcomplete == 0)
            {
                mTxtPendingComplete.setText("0");
            }
            else
            {
                mTxtPendingComplete.setText(String.valueOf((int) mPendingcomplete));
            }
        }
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
