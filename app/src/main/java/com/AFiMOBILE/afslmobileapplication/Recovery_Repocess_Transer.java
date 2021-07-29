package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.List;

public class Recovery_Repocess_Transer extends AppCompatActivity
{

    public String mInpFacilityno , PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , LoginUserName;
    public SqlliteCreateRecovery sqlliteCreateRecovery_RepocessTran;
    public TextView tv , tv_sucess , mFacilityno , mClientName , mAdders , mVehicleNo , mMake , mModel , mNoRnt , mTotalArrays , mFutureCap , mFutureInt , mSettlement;
    public Spinner mSizerNamae;
    public ArrayAdapter<String> arraySizerDetails ;
    public EditText mResonRepo , mComment;
    public Button mBtnRequest , mBtnclear;
    public View layout_sucress , layout_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_repocess_transer);

        //=== Get Varible
        Intent intent = getIntent();
        mInpFacilityno     =   intent.getStringExtra("FACNO");

        //== Create Goloble Varible
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();
        sqlliteCreateRecovery_RepocessTran = new SqlliteCreateRecovery(this);


        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);

        //=== Load Sizzer Details
        //== Driven By Load
        mSizerNamae   =   (Spinner)findViewById(R.id.spnsizercode);
        List<String> labelsActon = sqlliteCreateRecovery_RepocessTran.GetAllSizer(LoginBranch);
        labelsActon.add("");
        if (labelsActon != null) {
            arraySizerDetails = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsActon);
            arraySizerDetails.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSizerNamae.setAdapter(arraySizerDetails);
            mSizerNamae.setSelection(arraySizerDetails.getPosition(""));
        }


        //=== Load Data
        RequestData();

        //=== Clear Data
        mBtnclear = (Button)findViewById(R.id.btnclear);
        mBtnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResonRepo.setText("");
                mComment.setText("");
                mSizerNamae.setSelection(arraySizerDetails.getPosition(""));
            }
        });
    }


    public void RequestData()
    {
        mResonRepo  =   (EditText)findViewById(R.id.TxtRepReson);
        mComment    =   (EditText)findViewById(R.id.Txtcomment);
        mBtnRequest =   (Button)findViewById(R.id.btnSave);

        mBtnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (mSizerNamae.getSelectedItem().toString().equals(""))
                {
                    tv.setText("Assign Sizer Is blank");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else if (mResonRepo.getText().toString().equals(""))
                {
                    tv.setText("Assign Reason is Blank");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_error);
                    toast.show();
                }
                else
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Repocess_Transer.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to Request this case ?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //==== Get Sizer Details
                            String SierCode="" , SizerName="" , Sizercode="";
                            SQLiteDatabase sqLiteDatabase_getCode = sqlliteCreateRecovery_RepocessTran.getReadableDatabase();
                            Cursor cursor_getsizerCode = sqLiteDatabase_getCode.rawQuery("SELECT sizer_Code,sizer_name,sizer_clcode FROM masr_sizer WHERE sizer_name = '" + mSizerNamae.getSelectedItem().toString() + "'" , null );
                            if (cursor_getsizerCode.getCount() != 0)
                            {
                                cursor_getsizerCode.moveToFirst();
                                SierCode    =   cursor_getsizerCode.getString(0);
                                SizerName   =   cursor_getsizerCode.getString(1);
                                Sizercode   =   cursor_getsizerCode.getString(2);
                            }
                            cursor_getsizerCode.close();
                            sqLiteDatabase_getCode.close();

                            String mMGERCODE = "" , mMGERNAME="";
                            SQLiteDatabase sqLiteDatabase_getdate = sqlliteCreateRecovery_RepocessTran.getReadableDatabase();
                            Cursor cursor_GetData = sqLiteDatabase_getdate.rawQuery("SELECT rec_manager,rec_manager_name FROM recovery_user_group WHERE ass_officer = '" + LoginUser + "'" , null);
                            if (cursor_GetData.getCount() !=0)
                            {
                                cursor_GetData.moveToFirst();
                                mMGERCODE   =   cursor_GetData.getString(0);
                                mMGERNAME   =   cursor_GetData.getString(1);
                            }
                            cursor_GetData.close();
                            sqLiteDatabase_getdate.close();


                            SQLiteDatabase sqLiteDatabase_InsertRepoess = sqlliteCreateRecovery_RepocessTran.getWritableDatabase();
                            ContentValues contentValues_Repcess = new ContentValues();
                            contentValues_Repcess.put("facility_no" , mFacilityno.getText().toString());
                            contentValues_Repcess.put("clcode" , "");
                            contentValues_Repcess.put("client_name" , mClientName.getText().toString());
                            contentValues_Repcess.put("vehicle_no" , mVehicleNo.getText().toString());
                            contentValues_Repcess.put("sizer_code" , SierCode);
                            contentValues_Repcess.put("sizer_name" , SizerName);
                            contentValues_Repcess.put("sizer_clcode" , Sizercode);
                            contentValues_Repcess.put("approve_mgr_code" , mMGERCODE);
                            contentValues_Repcess.put("approve_mgr_name" , mMGERNAME);
                            contentValues_Repcess.put("Live_Server_Update" , "");
                            contentValues_Repcess.put("req_date" , LoginDate);
                            contentValues_Repcess.put("req_user" , LoginUser);
                            contentValues_Repcess.put("request_reason" , mResonRepo.getText().toString());
                            contentValues_Repcess.put("Comment" , mComment.getText().toString());
                            sqLiteDatabase_InsertRepoess.insert("recovery_repocess_order" , null , contentValues_Repcess);

                            tv_sucess.setText("Record Successfully Requested.");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout_sucress);
                            toast.show();
                            mResonRepo.setText("");
                            mComment.setText("");
                            mSizerNamae.setSelection(arraySizerDetails.getPosition(""));

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
            }
        });




    }

    public void onStart()
    {
        super.onStart();
        LoadData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public void LoadData()
    {
        mFacilityno         =   (TextView)findViewById(R.id.TxtFacno);              mClientName         =   (TextView)findViewById(R.id.TxtClname);
        mAdders             =   (TextView)findViewById(R.id.TxtAdders);             mVehicleNo          =   (TextView)findViewById(R.id.TxtVehmo);
        mMake               =   (TextView)findViewById(R.id.TxtMake);               mModel              =   (TextView)findViewById(R.id.TxtModel);
        mNoRnt              =   (TextView)findViewById(R.id.TxtNoRnt);              mTotalArrays        =   (TextView)findViewById(R.id.TxtTotarrays);
        mFutureCap          =   (TextView)findViewById(R.id.TxtFutCap);              mFutureInt          =   (TextView)findViewById(R.id.TxtFutInt);
        mSettlement         =   (TextView)findViewById(R.id.TxtFuLLYSETT);

        DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");


        SQLiteDatabase sqLiteDatabase_LoadData = sqlliteCreateRecovery_RepocessTran.getReadableDatabase();
        Cursor cursor_LoadData = sqLiteDatabase_LoadData.rawQuery("SELECT  Facility_Number,Client_Name,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,C_Postal_Town ," +
                " Vehicle_Number,Asset_Model,Arrears_Rental_No,Total_Arrear_Amount,FUTURE_CAPITAL,FUTURE_INTEREST,TOTAL_SETTL_AMT FROM recovery_generaldetail WHERE Facility_Number = '" + mInpFacilityno + "'" , null);
        if (cursor_LoadData.getCount() != 0)
        {
            cursor_LoadData.moveToFirst();

            mFacilityno.setText(cursor_LoadData.getString(0));
            mClientName.setText(cursor_LoadData.getString(1));
            mAdders.setText(cursor_LoadData.getString(2) + " " + cursor_LoadData.getString(3) + " "  + cursor_LoadData.getString(4) + " " + cursor_LoadData.getString(5) );
            mVehicleNo.setText(cursor_LoadData.getString(7));
            mMake.setText(cursor_LoadData.getString(8));
            mModel.setText(cursor_LoadData.getString(8));
            mNoRnt.setText(cursor_LoadData.getString(9));


            mTotalArrays.setText(nf.format(Double.parseDouble(cursor_LoadData.getString(10).replace("," ,""))));
            mFutureCap.setText(nf.format(Double.parseDouble(cursor_LoadData.getString(11).replace("," , ""))));
            mFutureInt.setText(nf.format(Double.parseDouble(cursor_LoadData.getString(12).replace("," , ""))));
            mSettlement.setText(nf.format(Double.parseDouble(cursor_LoadData.getString(13).replace("," , ""))));

            //mTotalArrays.setText(cursor_LoadData.getString(10));
            //mFutureCap.setText(cursor_LoadData.getString(11));
            //mFutureInt.setText(cursor_LoadData.getString(12));
            //mSettlement.setText(cursor_LoadData.getString(13));

        }
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
