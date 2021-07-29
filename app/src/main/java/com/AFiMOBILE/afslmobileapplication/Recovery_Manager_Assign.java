package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.List;

public class Recovery_Manager_Assign extends AppCompatActivity
{
    public TextView mFacilityNo , mClientName , tv , tv_sucess ;
    public EditText mResonAssing , mComment;
    public String mInpFacilityno , PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , LoginUserName;
    public Spinner mSpnManager;
    public SqlliteCreateRecovery sqlliteCreateRecovery_ManagerAssign;
    public ArrayAdapter<String> arrayUserGroup ;
    public Button btnLock , BtnSubmit;
    public View layout_sucress , layout_error;
    public CheckDataConnectionStatus checkDataConnectionStatus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_manager_assign);

        //=== Get Varible
        Intent intent = getIntent();
        mInpFacilityno     =   intent.getStringExtra("FACNO");

        //=== Create Varible
        sqlliteCreateRecovery_ManagerAssign = new SqlliteCreateRecovery(this);
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();

        checkDataConnectionStatus = new CheckDataConnectionStatus(this);
        //=================================

        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);


        mFacilityNo     =   (TextView)findViewById(R.id.TxtFacno);
        mClientName     =   (TextView)findViewById(R.id.Txtclname);
        mSpnManager     =   (Spinner)findViewById(R.id.spnMangerCode) ;
        mResonAssing    =   (EditText)findViewById(R.id.TxtResonAssign);
        mComment        =   (EditText)findViewById(R.id.TxtManagerComment);

        mFacilityNo.setText(mInpFacilityno);

        //=== Get Client Name
        SQLiteDatabase sqLiteDatabase_Managerassign = sqlliteCreateRecovery_ManagerAssign.getReadableDatabase();
        Cursor cursor_managerassign  = sqLiteDatabase_Managerassign.rawQuery("SELECT Client_Name FROM recovery_generaldetail WHERE Facility_Number = '" + mInpFacilityno + "'" , null);
        if (cursor_managerassign.getCount() != 0)
        {
            cursor_managerassign.moveToFirst();
            mClientName.setText(cursor_managerassign.getString(0));
        }

        LoadMasterData();

        //==== Lock Case =====
        btnLock = (Button)findViewById(R.id.btnLock);
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Manager_Assign.this);
                builderdelete.setTitle("AFiMobile-Leasing");
                builderdelete.setMessage("Are you sure to Lock this case ?");
                builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        LockData();
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


        //==== submit Valus
        BtnSubmit       =   (Button)findViewById(R.id.btnsubmit);
        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDataConnectionStatus.IsConnected() == true)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Manager_Assign.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to Submit this case ?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            mSubmitData();
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
                else
                {
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Manager_Assign.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Data Connection Not available. Please Turn on Connection.");
                    builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        });

    }

    private void mSubmitData()
    {
        LockData();
        Volly_Request_Recovery_Data volly_request_recovery_data = new Volly_Request_Recovery_Data(this);
        volly_request_recovery_data.PostManagerAction(mInpFacilityno);
    }

    public void LockData()
    {
        String mMgeCode = mSpnManager.getSelectedItem().toString();
        String mRrsonAss = mResonAssing.getText().toString();

        if (mMgeCode.equals(""))
        {
            tv.setText("Assign Manager Blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mRrsonAss.equals(""))
        {
            tv.setText("Assign Reason Blank.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            String mMGERCODE = "";
            SQLiteDatabase sqLiteDatabase_getdate = sqlliteCreateRecovery_ManagerAssign.getReadableDatabase();
            Cursor cursor_GetData = sqLiteDatabase_getdate.rawQuery("SELECT rec_manager FROM recovery_user_group WHERE rec_manager_name = '" + mSpnManager.getSelectedItem().toString() + "'" , null);
            if (cursor_GetData.getCount() !=0)
            {
                cursor_GetData.moveToFirst();
                mMGERCODE   =   cursor_GetData.getString(0);
            }
            cursor_GetData.close();
            sqLiteDatabase_getdate.close();


            SQLiteDatabase sqLiteDatabase_LockCAse = sqlliteCreateRecovery_ManagerAssign.getWritableDatabase();
            ContentValues contentValues_LockCase = new ContentValues();
            contentValues_LockCase.put("Facility_no" , mInpFacilityno);
            contentValues_LockCase.put("Clode" , "");
            contentValues_LockCase.put("Assign_MgrCode" , mMGERCODE);
            contentValues_LockCase.put("Assign_MgrName" , mSpnManager.getSelectedItem().toString());
            contentValues_LockCase.put("Assign_Date" , LoginDate);
            contentValues_LockCase.put("Assign_Reason" , mResonAssing.getText().toString());
            contentValues_LockCase.put("Req_User_Id" , LoginUser);
            contentValues_LockCase.put("Req_User_Name" , LoginUserName);
            contentValues_LockCase.put("Req_Comment" , mComment.getText().toString());
            contentValues_LockCase.put("Lock_Sts" , "");
            contentValues_LockCase.put("Mgr_Responces" , "");
            contentValues_LockCase.put("Mgr_Responces_Date" , "");
            contentValues_LockCase.put("Mgr_Attend" , "");
            contentValues_LockCase.put("Mgr_Attend_Date" , "");
            contentValues_LockCase.put("Mgr_Action_Code" , "");
            contentValues_LockCase.put("Req_Done_Sts" , "P");
            contentValues_LockCase.put("LIVE_SERVER_UPDATE" , "");
            sqLiteDatabase_LockCAse.insert("manager_assign_data" , null , contentValues_LockCase);
            mSpnManager.setSelection(arrayUserGroup.getPosition("  "));
            mResonAssing.setText(""); mComment.setText("");
            sqLiteDatabase_LockCAse.close();

            tv_sucess.setText("Record Successfully Locked.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_sucress);
            toast.show();
        }
    }

    public void LoadMasterData()
    {
        //==== Vehicle Condition

        List<String> labelsManagerCode = sqlliteCreateRecovery_ManagerAssign.GetRecoveryUser(LoginUser);
        labelsManagerCode.add("");
        if (labelsManagerCode != null) {
            arrayUserGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsManagerCode);
            arrayUserGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnManager.setAdapter(arrayUserGroup);
            mSpnManager.setSelection(arrayUserGroup.getPosition(""));
        }
    }

    protected void onDestroy(){

        sqlliteCreateRecovery_ManagerAssign.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
