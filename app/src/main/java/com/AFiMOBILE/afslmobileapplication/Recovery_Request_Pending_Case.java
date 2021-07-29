package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Recovery_Request_Pending_Case extends AppCompatActivity {

    public Adapter_Pending_Request adapterPendingReq;
    public RecyclerView mRecyPendingReq;
    public SqlliteCreateRecovery sqlliteCreateRecovery_PendingRequest;
    public String PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , LoginUserName;
    public Button mApprove , mReject;
    public static String mRequestManagerFacno;
    public View layout_sucress , layout_error;
    public TextView tv , tv_sucess;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_request_pending_case);


        sqlliteCreateRecovery_PendingRequest    =   new SqlliteCreateRecovery(this);
        //=== Create Varible
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();

        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);


        //=== Load Pending Request
        LoadPendingRequest();

        //=== Manager Approve And Reject Request
        ActionCode();

    }

    public void ActionCode()
    {
        //=== Approve Case
        mApprove    =   (Button)findViewById(R.id.btnApprove);
        mApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Request_Pending_Case.this);
                builderdelete.setTitle("AFiMobile-Leasing");
                builderdelete.setMessage("Are you sure to Request Approved ?");
                builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    SQLiteDatabase sqLiteDatabase_CaseApprove = sqlliteCreateRecovery_PendingRequest.getWritableDatabase();
                    Cursor cursor_approveCase = sqLiteDatabase_CaseApprove.rawQuery("SELECT * FROM recovery_search_data WHERE Facility_Number = '" + mRequestManagerFacno + "'" , null );
                    if (cursor_approveCase.getCount() != 0)
                    {
                        cursor_approveCase.moveToFirst();

                        ContentValues contentValues_RecoveryGenreal = new ContentValues();
                        contentValues_RecoveryGenreal.put("Client_Name", cursor_approveCase.getString(0));
                        contentValues_RecoveryGenreal.put("Product_Type", cursor_approveCase.getString(1));
                        contentValues_RecoveryGenreal.put("Asset_Model", cursor_approveCase.getString(2));
                        contentValues_RecoveryGenreal.put("Vehicle_Number", cursor_approveCase.getString(3));
                        contentValues_RecoveryGenreal.put("Due_Day", cursor_approveCase.getString(4));
                        contentValues_RecoveryGenreal.put("Current_Month_Rental", cursor_approveCase.getString(5));
                        contentValues_RecoveryGenreal.put("OD_Arrear_Amount", cursor_approveCase.getString(6));
                        contentValues_RecoveryGenreal.put("Total_Arrear_Amount", cursor_approveCase.getString(7));
                        contentValues_RecoveryGenreal.put("Last_Payment_Date", cursor_approveCase.getString(8));
                        contentValues_RecoveryGenreal.put("Last_Payment_Amount", cursor_approveCase.getString(9));
                        contentValues_RecoveryGenreal.put("Facility_Payment_Status", cursor_approveCase.getString(10));
                        contentValues_RecoveryGenreal.put("Letter_Demand_Date", cursor_approveCase.getString(11));
                        contentValues_RecoveryGenreal.put("Client_Code", cursor_approveCase.getString(12));
                        contentValues_RecoveryGenreal.put("NIC", cursor_approveCase.getString(13));
                        contentValues_RecoveryGenreal.put("Customer_Full_Name", cursor_approveCase.getString(14));
                        contentValues_RecoveryGenreal.put("Gender", cursor_approveCase.getString(15));
                        contentValues_RecoveryGenreal.put("Occupation", cursor_approveCase.getString(16));
                        contentValues_RecoveryGenreal.put("Work_Place_Name", cursor_approveCase.getString(17));
                        contentValues_RecoveryGenreal.put("Work_Place_Address", cursor_approveCase.getString(18));
                        contentValues_RecoveryGenreal.put("Mobile_No1", cursor_approveCase.getString(19));
                        contentValues_RecoveryGenreal.put("Mobile_No2", cursor_approveCase.getString(20));
                        contentValues_RecoveryGenreal.put("Home_No", cursor_approveCase.getString(21));
                        contentValues_RecoveryGenreal.put("C_Address_Line_1", cursor_approveCase.getString(22));
                        contentValues_RecoveryGenreal.put("C_Address_Line_2", cursor_approveCase.getString(23));
                        contentValues_RecoveryGenreal.put("C_Address_Line_3", cursor_approveCase.getString(24));
                        contentValues_RecoveryGenreal.put("C_Address_Line_4", cursor_approveCase.getString(25));
                        contentValues_RecoveryGenreal.put("C_Postal_Town", cursor_approveCase.getString(26));
                        contentValues_RecoveryGenreal.put("P_Address_Line_1", cursor_approveCase.getString(27));
                        contentValues_RecoveryGenreal.put("P_Address_Line_2", cursor_approveCase.getString(28));
                        contentValues_RecoveryGenreal.put("P_Address_Line_3", cursor_approveCase.getString(29));
                        contentValues_RecoveryGenreal.put("P_Address_Line_4", cursor_approveCase.getString(30));
                        contentValues_RecoveryGenreal.put("P_Postal_Town", cursor_approveCase.getString(31));
                        contentValues_RecoveryGenreal.put("Facility_Number", cursor_approveCase.getString(32));
                        contentValues_RecoveryGenreal.put("Facility_Amount", cursor_approveCase.getString(33));
                        contentValues_RecoveryGenreal.put("Facility_Branch", cursor_approveCase.getString(34));
                        contentValues_RecoveryGenreal.put("Tenure", cursor_approveCase.getString(35));
                        contentValues_RecoveryGenreal.put("Recovery_Executive", LoginUser);
                        contentValues_RecoveryGenreal.put("Recovery_Executive_Name", cursor_approveCase.getString(37));
                        contentValues_RecoveryGenreal.put("CC_Executive", cursor_approveCase.getString(38));
                        contentValues_RecoveryGenreal.put("Marketing_Executive", cursor_approveCase.getString(39));
                        contentValues_RecoveryGenreal.put("Follow_Up_Officer", cursor_approveCase.getString(40));
                        contentValues_RecoveryGenreal.put("Recovery_Executive_Phone", cursor_approveCase.getString(41));
                        contentValues_RecoveryGenreal.put("CC_Executive_Phone", cursor_approveCase.getString(42));
                        contentValues_RecoveryGenreal.put("Marketing_Executive_Phone", cursor_approveCase.getString(43));
                        contentValues_RecoveryGenreal.put("Follow_Up_Officer_Phone", cursor_approveCase.getString(44));
                        contentValues_RecoveryGenreal.put("Activation_Date", cursor_approveCase.getString(45));
                        contentValues_RecoveryGenreal.put("Expiry_Date", cursor_approveCase.getString(46));
                        contentValues_RecoveryGenreal.put("Total_Facility_Amount", cursor_approveCase.getString(47));
                        contentValues_RecoveryGenreal.put("Disbursed_Amount", cursor_approveCase.getString(48));
                        contentValues_RecoveryGenreal.put("Disbursement_Status", cursor_approveCase.getString(49));
                        contentValues_RecoveryGenreal.put("Interest_Rate", cursor_approveCase.getString(50));
                        contentValues_RecoveryGenreal.put("Rental", cursor_approveCase.getString(51));
                        contentValues_RecoveryGenreal.put("Contract_Status", cursor_approveCase.getString(52));
                        contentValues_RecoveryGenreal.put("Facility_Status", cursor_approveCase.getString(53));
                        contentValues_RecoveryGenreal.put("Down_Payment_No", cursor_approveCase.getString(54));
                        contentValues_RecoveryGenreal.put("Rentals_Matured_No", cursor_approveCase.getString(55));
                        contentValues_RecoveryGenreal.put("Rentals_Paid_No", cursor_approveCase.getString(56));
                        contentValues_RecoveryGenreal.put("Arrears_Rental_No", cursor_approveCase.getString(57));
                        contentValues_RecoveryGenreal.put("SPDC_Available", cursor_approveCase.getString(58));
                        contentValues_RecoveryGenreal.put("Total_Outstanding", cursor_approveCase.getString(59));
                        contentValues_RecoveryGenreal.put("Capital_Outstanding", cursor_approveCase.getString(60));
                        contentValues_RecoveryGenreal.put("Interest_Outstanding", cursor_approveCase.getString(61));
                        contentValues_RecoveryGenreal.put("Arrears_Rental_Amount", cursor_approveCase.getString(62));
                        contentValues_RecoveryGenreal.put("OD_Interest", cursor_approveCase.getString(63));
                        contentValues_RecoveryGenreal.put("Interest_Arrears", cursor_approveCase.getString(64));
                        contentValues_RecoveryGenreal.put("Insurance_Arrears", cursor_approveCase.getString(65));
                        contentValues_RecoveryGenreal.put("OD_Interest_OS", cursor_approveCase.getString(66));
                        contentValues_RecoveryGenreal.put("Seizing_Charges", cursor_approveCase.getString(67));
                        contentValues_RecoveryGenreal.put("OC_Arrears", cursor_approveCase.getString(68));
                        contentValues_RecoveryGenreal.put("FUTURE_CAPITAL", cursor_approveCase.getString(69));
                        contentValues_RecoveryGenreal.put("FUTURE_INTEREST", cursor_approveCase.getString(70));
                        contentValues_RecoveryGenreal.put("TOTAL_SETTL_AMT", cursor_approveCase.getString(71));
                        sqLiteDatabase_CaseApprove.insert("recovery_generaldetail" , null , contentValues_RecoveryGenreal);


                        //==== Update Status
                        ContentValues contentValues_data = new ContentValues();
                        contentValues_data.put("Mgr_Respoce_Sts" , "A");
                        sqLiteDatabase_CaseApprove.update("recovery_request_mgr" ,contentValues_data , "Facility_No = ?" , new String[]{String.valueOf(mRequestManagerFacno)});
                        LoadPendingRequest();

                        tv_sucess.setText("Record Successfully Assigned.");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_sucress);
                        toast.show();


                    }
                    }
                });
                builderdelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "No Button Clicked",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builderdelete.create();
                dialog.show();
            }
        });

        mReject =   (Button)findViewById(R.id.btnReject);
        mReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Request_Pending_Case.this);
                builderdelete.setTitle("AFiMobile-Leasing");
                builderdelete.setMessage("Are you sure to Request Reject ?");
                builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //==== Update Status
                        SQLiteDatabase sqLiteDatabase_RejectFile = sqlliteCreateRecovery_PendingRequest.getWritableDatabase();
                        ContentValues contentValues_data = new ContentValues();
                        contentValues_data.put("Mgr_Respoce_Sts" , "R");
                        sqLiteDatabase_RejectFile.update("recovery_request_mgr" ,contentValues_data , "Facility_No = ?" , new String[]{String.valueOf(mRequestManagerFacno)});
                        LoadPendingRequest();

                        tv.setText("Record Successfully Rejected.");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout_error);
                        toast.show();

                    }
                });
                builderdelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "No Button Clicked",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builderdelete.create();
                dialog.show();
            }
        });
    }


    public void LoadPendingRequest()
    {
        mRecyPendingReq =   (RecyclerView)findViewById(R.id.ReyFacList);

        SQLiteDatabase sqLiteDatabaseGetPendingReq  =   sqlliteCreateRecovery_PendingRequest.getReadableDatabase();
        Cursor cursorPendingReq  = sqLiteDatabaseGetPendingReq.rawQuery("SELECT Facility_No,Request_user_name,Request_Date,Requet_Reason,Request_comment FROM" +
                " recovery_request_mgr WHERE Assign_MgrCode = '" + LoginUser + "' and Mgr_Respoce_Sts = ''" , null);
        if (cursorPendingReq.getCount()!= 0)
        {
            cursorPendingReq.moveToFirst();

            mRecyPendingReq.setHasFixedSize(true);
            mRecyPendingReq.setLayoutManager(new LinearLayoutManager(Recovery_Request_Pending_Case.this));
            adapterPendingReq = new Adapter_Pending_Request(Recovery_Request_Pending_Case.this , cursorPendingReq);
            mRecyPendingReq.setAdapter(adapterPendingReq);
        }
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
