package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientDetailsAdd extends AppCompatActivity {

    private Spinner mSUBSECTOR ,  mSECTOR ,  mSPNGENREL , mMARRYSTS , mTAXAPPLY , mCOUNTRY , mNASTIONAL , mPRIVSTION , mDISTRICT , mAREA;
    private EditText   mLANDPHONE , mINSOURCE , mINCOMEAMT , mTAXTCODE ,mEMAIL , mAMAILADD1 , mAMAILADD2 , mAMAILADD3 , mAMAILADD4 , mCLREMARKS ;
    private TextView mDATEOFBIRTHDAY , mAPPNO , mCLFULLYNAM;
    public DatePickerDialog  dialog;
    public int mYear=0 , mMonth=0 , mDay=0;
    public int mBirYear;
    public SqlliteCreateLeasing sqlliteCreateLeasing_AddClient;
    public SQLiteDatabase sqLiteDatabase_AddClient;
    public String LoginBranch="", LoginDate="" , LoginUser = "" , mPrvCode="" , mInpAppno;
    public Button mSave , mClearData;
    public ArrayAdapter<String> AdapterClGenreal , arrayAdapterclsts , arrayAdapterSECTOR , arrayAdapterSECTORsub , arrayAdaptrTAT , arrayAdapterPROVSTION ,
            ArraysAdapterDistric , arrayAdapterAREA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details_add);


        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));

        sqlliteCreateLeasing_AddClient = new SqlliteCreateLeasing(this);

        //=== Globle Varible Details
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();


        mAPPNO              =   (TextView) findViewById(R.id.txtAPPNO) ;
        mDISTRICT           =   (Spinner)findViewById(R.id.spnDISTRICK);
        mAREA               =   (Spinner)findViewById(R.id.spnAREA) ;
        mCLFULLYNAM         =   (TextView)findViewById(R.id.txtFULLYNAME);
        mSave               =   (Button)findViewById(R.id.btnsave);
        mClearData          =   (Button)findViewById(R.id.btnclearData);

        mLANDPHONE          =   (EditText) findViewById(R.id.txtlandphone);
        mINSOURCE           =   (EditText) findViewById(R.id.txtincomsource);
        mINCOMEAMT          =   (EditText) findViewById(R.id.txtincomamt);
        mTAXTCODE           =   (EditText)findViewById(R.id.txtcode);
        mEMAIL              =   (EditText)findViewById(R.id.txtemail);
        mAMAILADD1          =   (EditText)findViewById(R.id.txtmailadd1);
        mAMAILADD2          =   (EditText)findViewById(R.id.txtmailadd2);
        mAMAILADD3          =   (EditText)findViewById(R.id.txtmailadd3);
        mAMAILADD4          =   (EditText)findViewById(R.id.txtmailadd4);
        mCLREMARKS          =   (EditText) findViewById(R.id.txtclremarks) ;

        mLANDPHONE.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mINSOURCE.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mEMAIL.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mAMAILADD1.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mAMAILADD2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mAMAILADD3.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mAMAILADD4.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mTAXTCODE.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        //==== Get Application No ======
        final Intent intent = getIntent();
        mInpAppno    =   intent.getStringExtra("ApplicationNo");
        mAPPNO.setText(mInpAppno);
        //=============================



        //============== Load Sppnier Data =================================

        //=== Load Sector Details
        mSECTOR = (Spinner)findViewById(R.id.spnsecter);
        List<String> labelsSECTOR = sqlliteCreateLeasing_AddClient.getAllSector();
        arrayAdapterSECTOR = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsSECTOR);
        arrayAdapterSECTOR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSECTOR.setAdapter(arrayAdapterSECTOR);


        mSUBSECTOR = (Spinner)findViewById(R.id.spnsubsecter);
        List<String> labelsSECTORSUB = sqlliteCreateLeasing_AddClient.getAllSubSector(mSECTOR.getSelectedItem().toString());
        arrayAdapterSECTORsub = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsSECTORSUB);
        arrayAdapterSECTORsub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSUBSECTOR.setAdapter(arrayAdapterSECTORsub);


        //===== Client Genrel Details
        mSPNGENREL      =       (Spinner)findViewById(R.id.spngenreal);
        List<String> list = new ArrayList<String>();
        list.add("MALE");
        list.add("FE-MALE");
        AdapterClGenreal = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        AdapterClGenreal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSPNGENREL.setAdapter(AdapterClGenreal);
        //============================

        //===== Client Marrid Details
        mMARRYSTS    = (Spinner)findViewById(R.id.spnmarilsts);
        List<String> listclsts = new ArrayList<String>();
        listclsts.add("MARRIED");
        listclsts.add("UNMARRIED");
        arrayAdapterclsts = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listclsts);
        arrayAdapterclsts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMARRYSTS.setAdapter(arrayAdapterclsts);
        //============================

        //===== Client Tax Details
        mTAXAPPLY    = (Spinner)findViewById(R.id.spntaxapplible);
        List<String> listtax = new ArrayList<String>();
        listtax.add("YES");
        listtax.add("NO");
        arrayAdaptrTAT = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listtax);
        arrayAdaptrTAT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTAXAPPLY.setAdapter(arrayAdaptrTAT);
        //============================

        //===== Client Country Details
        mCOUNTRY    = (Spinner)findViewById(R.id.spnCOUNTRY);
        List<String> listcountry = new ArrayList<String>();
        listcountry.add("SRI-LANKA");
        ArrayAdapter<String> arrayAdaptrCOUNTRY = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listcountry);
        arrayAdaptrCOUNTRY.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCOUNTRY.setAdapter(arrayAdaptrCOUNTRY);
        //============================

        //===== Client Country Details
        mNASTIONAL    = (Spinner)findViewById(R.id.spnNASTIONAL);
        List<String> listNASTON = new ArrayList<String>();
        listNASTON.add("SRI-LANKA");
        ArrayAdapter<String> arrayAdaptrnastion = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listNASTON);
        arrayAdaptrnastion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNASTIONAL.setAdapter(arrayAdaptrnastion);
        //============================

        //===== Province ========
        mPRIVSTION  = (Spinner)findViewById(R.id.spnPROVIENCE);
        List<String> labelsPROVSTION = sqlliteCreateLeasing_AddClient.getAllProvince();
        arrayAdapterPROVSTION = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsPROVSTION);
        arrayAdapterPROVSTION.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPRIVSTION.setAdapter(arrayAdapterPROVSTION);

        //=== Get SubSector
        mSECTOR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //=== Load SubSector
                if (mSECTOR.getSelectedItem().toString() != null)
                {
                    LoadSubSector();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //=== Get District Code =================
        mPRIVSTION.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //===== District ========

                String mPRNAME = mPRIVSTION.getSelectedItem().toString();
                sqLiteDatabase_AddClient = sqlliteCreateLeasing_AddClient.getReadableDatabase();
                Cursor cursorPCODE = sqLiteDatabase_AddClient.rawQuery("SELECT * FROM AP_MAST_PROVINCE WHERE PRV_NAME = '" + mPRNAME + "'" , null );
                if (cursorPCODE.getCount() != 0)
                {
                    cursorPCODE.moveToFirst();
                    mPrvCode = cursorPCODE.getString(0);
                    List<String> labelsDISTRICK = sqlliteCreateLeasing_AddClient.getAllDistrict(cursorPCODE.getString(0));
                    ArraysAdapterDistric = new ArrayAdapter<String>(ClientDetailsAdd.this, android.R.layout.simple_spinner_item, labelsDISTRICK);
                    ArraysAdapterDistric.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mDISTRICT.setAdapter(ArraysAdapterDistric);
                }
                cursorPCODE.close();
                sqLiteDatabase_AddClient.close();
                //==========================
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                }
            //====================================
        });

        //=== Get Area Code =================
        mDISTRICT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //===== District ========

                String mDICNAME = mDISTRICT.getSelectedItem().toString();
                sqLiteDatabase_AddClient = sqlliteCreateLeasing_AddClient.getReadableDatabase();
                Cursor cursoAREA = sqLiteDatabase_AddClient.rawQuery("SELECT * FROM AP_MAST_DISTRICT WHERE DIS_NAME = '" + mDICNAME + "'" , null );
                if (cursoAREA.getCount() != 0)
                {
                    cursoAREA.moveToFirst();
                    List<String> labelsAREA = sqlliteCreateLeasing_AddClient.getAllArea(mPrvCode , cursoAREA.getString(1));
                    arrayAdapterAREA = new ArrayAdapter<String>(ClientDetailsAdd.this, android.R.layout.simple_spinner_item, labelsAREA);
                    arrayAdapterAREA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mAREA.setAdapter(arrayAdapterAREA);
                }
                cursoAREA.close();
                sqLiteDatabase_AddClient.close();
                //==========================
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            //====================================
        });

        //===============================================================================
        GetSaveData();
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builderdelete = new AlertDialog.Builder(ClientDetailsAdd.this);
                builderdelete.setTitle("AFiMobile-Leasing");
                builderdelete.setMessage("Are you sure to Save data ?");
                builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SaveDataSqllite();
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



        mClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDATEOFBIRTHDAY.setText("");
                mCLFULLYNAM.setText("");

                mLANDPHONE.setText("");
                mINSOURCE.setText("");
                mINCOMEAMT.setText("");
                mTAXTCODE.setText("");
                mEMAIL.setText("");
                mAMAILADD1.setText("");
                mAMAILADD2.setText("");
                mAMAILADD3.setText("");
                mAMAILADD4.setText("");
                mCLREMARKS.setText("");
            }
        });
    }

    public void LoadSubSector()
    {
        mSUBSECTOR = (Spinner)findViewById(R.id.spnsubsecter);
        List<String> labelsSECTORSUB = sqlliteCreateLeasing_AddClient.getAllSubSector(mSECTOR.getSelectedItem().toString());
        ArrayAdapter<String> arrayAdapterSECTORsub = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsSECTORSUB);
        arrayAdapterSECTORsub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSUBSECTOR.setAdapter(arrayAdapterSECTORsub);
    }

    public void onStart()
    {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        sqLiteDatabase_AddClient.close();
        sqlliteCreateLeasing_AddClient.close();
        super.onDestroy();
    }

    public void SaveDataSqllite()
    {
        boolean Validate = true;
        String ErrorDescription="";

        if(mSPNGENREL.getSelectedItem().toString() == "")
        {
            Validate  = false;
            ErrorDescription = "Client Genrel Not Selected.";
        }
        else if (mINSOURCE.length() == 0)
        {
            Validate  = false;
            ErrorDescription = "Client Income Source Cannot be a blank.";
        }
        else if(mINCOMEAMT.length() == 0)
        {
            Validate  = false;
            ErrorDescription = "Client Income Amount Cannot be a blank.";
        }
        else if (mPRIVSTION.getSelectedItem().toString() == "")
        {
            Validate  = false;
            ErrorDescription = "Client Province Cannot be a blank.";
        }
        else if (mDISTRICT.getSelectedItem().toString() == "")
        {
            Validate  = false;
            ErrorDescription = "Client District Cannot be a blank.";
        }
        else if (mAREA.getSelectedItem().toString().equals(""))
        {
            Validate  = false;
            ErrorDescription = "Client Area Cannot be a blank.";
        }


        if (Validate == true)
        {

            SQLiteDatabase sqLiteDatabase_AddClient = sqlliteCreateLeasing_AddClient.getWritableDatabase();
            ContentValues contentValues_CREATE_DATE = new ContentValues();
            contentValues_CREATE_DATE.put("CL_GENDER" , mSPNGENREL.getSelectedItem().toString());
            contentValues_CREATE_DATE.put("CL_MARITAL_STATUS" , mMARRYSTS.getSelectedItem().toString());
            contentValues_CREATE_DATE.put("CL_INITIALS" , "");
            contentValues_CREATE_DATE.put("CL_LASTNAME" , "");
            contentValues_CREATE_DATE.put("CL_LAND_NO" , mLANDPHONE.getText().toString());
            contentValues_CREATE_DATE.put("CL_BRANCH" , LoginBranch);
            contentValues_CREATE_DATE.put("SECTOR" , mSECTOR.getSelectedItem().toString());
            contentValues_CREATE_DATE.put("SUB_SECTOR" , mSUBSECTOR.getSelectedItem().toString());
            contentValues_CREATE_DATE.put("INCOME_SOURCE" , mINSOURCE.getText().toString());
            contentValues_CREATE_DATE.put("INCOME_AMT" , mINCOMEAMT.getText().toString());
            contentValues_CREATE_DATE.put("CL_TAX" , mTAXAPPLY.getSelectedItem().toString());
            contentValues_CREATE_DATE.put("CL_TAX_CODE" , mTAXTCODE.getText().toString());
            contentValues_CREATE_DATE.put("CL_COUNTRY" , mCOUNTRY.getSelectedItem().toString() );
            contentValues_CREATE_DATE.put("CL_PROVINCE" , mPRIVSTION.getSelectedItem().toString() );
            contentValues_CREATE_DATE.put("CL_DISTRICT" , mDISTRICT.getSelectedItem().toString() );
            contentValues_CREATE_DATE.put("CL_AREA_CODE" , mAREA.getSelectedItem().toString() );
            contentValues_CREATE_DATE.put("CL_EMAIL" , mEMAIL.getText().toString() );
            contentValues_CREATE_DATE.put("CL_NATION" , mNASTIONAL.getSelectedItem().toString() );
            contentValues_CREATE_DATE.put("CL_MAIL_ADD1" , mAMAILADD1.getText().toString());
            contentValues_CREATE_DATE.put("CL_MAIL_ADD2" , mAMAILADD2.getText().toString());
            contentValues_CREATE_DATE.put("CL_MAIL_ADD3" , mAMAILADD3.getText().toString());
            contentValues_CREATE_DATE.put("CL_MAIL_ADD4" , mAMAILADD4.getText().toString());
            contentValues_CREATE_DATE.put("CL_INCOME" , "");
            contentValues_CREATE_DATE.put("CL_EMARKS" , mCLREMARKS.getText().toString());
            sqLiteDatabase_AddClient.update("LE_CLIENT_DATA" ,contentValues_CREATE_DATE ,"APPLICATION_REF_NO = ?", new String[]{String.valueOf(mInpAppno)});

            ContentValues contentValues_remarks = new ContentValues();
            contentValues_remarks.put("APPNO" , mAPPNO.getText().toString());
            contentValues_remarks.put("OFFICER_ID" , LoginUser);
            contentValues_remarks.put("RE_DATE" , LoginDate);
            contentValues_remarks.put("AP_REMARKS" , mCLREMARKS.getText().toString());
            sqLiteDatabase_AddClient.insert("APP_REMARKS" , null , contentValues_remarks);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
            builder.setMessage("Record Successfully Saved.");
            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
            builder.setMessage(ErrorDescription);
            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
    }

    public void GetSaveData()
    {
        sqLiteDatabase_AddClient = sqlliteCreateLeasing_AddClient.getReadableDatabase();
        Cursor cursor_GET_DATA = sqLiteDatabase_AddClient.rawQuery("SELECT * FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mInpAppno + "'" , null);
        if (cursor_GET_DATA.getCount() != 0)
        {
            cursor_GET_DATA.moveToFirst();
            mCLFULLYNAM.setText(cursor_GET_DATA.getString(3));
            mLANDPHONE.setText(cursor_GET_DATA.getString(15));
            mINSOURCE.setText(cursor_GET_DATA.getString(24));
            mINCOMEAMT.setText(cursor_GET_DATA.getString(25));
            mEMAIL.setText(cursor_GET_DATA.getString(32));


            mSPNGENREL.setSelection(AdapterClGenreal.getPosition(cursor_GET_DATA.getString(8)));
            mMARRYSTS.setSelection(arrayAdapterclsts.getPosition(cursor_GET_DATA.getString(9)));
            mSECTOR.setSelection(arrayAdapterSECTOR.getPosition(cursor_GET_DATA.getString(22)));
            mSUBSECTOR.setSelection(arrayAdapterSECTORsub.getPosition(cursor_GET_DATA.getString(23)));
            mTAXAPPLY.setSelection(arrayAdaptrTAT.getPosition(cursor_GET_DATA.getString(26)));
            mPRIVSTION.setSelection(arrayAdapterPROVSTION.getPosition(cursor_GET_DATA.getString(29)));

            //mDISTRICT.setSelection(ArraysAdapterDistric.getPosition(cursor_GET_DATA.getString(30)));
            //mAREA.setSelection(arrayAdapterAREA.getPosition(cursor_GET_DATA.getString(31)));

            mAMAILADD1.setText(cursor_GET_DATA.getString(34));
            mAMAILADD2.setText(cursor_GET_DATA.getString(35));
            mAMAILADD3.setText(cursor_GET_DATA.getString(36));
            mAMAILADD4.setText(cursor_GET_DATA.getString(37));
            mCLREMARKS.setText(cursor_GET_DATA.getString(38));
        }
        cursor_GET_DATA.close();
        sqLiteDatabase_AddClient.close();
    }
}
