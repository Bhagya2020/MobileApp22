package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientGurnterDetails extends AppCompatActivity {

    public static EditText   mNIC , mFULLNAME , mADD1 , mADD2 , mADD3 , mADD4 , mPHONE , mOCUPATION , mINCOMESOURCE , mSECVAL ;
    public static TextView mAPPNO , mBrithDay;
    private Button mSAVE , mCLEAR , mDELETE;
    public static Spinner mOCCUPATION , mRELSTYPE , mSECTOR , mSUBSECTOR , mCLTITLE , mGENREAL , mCOUNTRY , mPRIVSTION , mDISTRICT , mAREA;
    public static String mPrvCode, LoginUser="" , LoginDate="",LoginBranch="" , mInpAppno="";
    public SqlliteCreateLeasing sqlliteCreateLeasing_AddGur;
    public SQLiteDatabase sqLiteDatabase_AddGur;
    private int mYear=0 , mMonth=0 , mDay=0;
    private int mBirYear;
    private DatePickerDialog datePickerDialog , dialog;
    public static RadioGroup rediogroup;
    public static RadioButton rdoclgur;
    public static RadioButton rdoJoin , rdogur;
    public RecyclerView mRecycleCoApp;
    public Adapter_CoApplicant_Details myPenadapter;
    public static ArrayAdapter<String> arrayGENREAL , arrayRELASTION , arraySECTOR;
    private static Toolbar toolbar;
    private Handler handler;
    private TextView ConnectionSts;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public static ArrayAdapter<String> arrayAdapter_occupa , arrayAdaptrCOUNTRY , arrayAdapterPROVSTION , ArraysAdapterDistric , arrayAdapterAREA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_gurnter_details);


        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));

        mAPPNO              =       (TextView)findViewById(R.id.txtAPPLICATIONNO);

        //==== Create Globel Varible =====
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        sqlliteCreateLeasing_AddGur = new SqlliteCreateLeasing(this);
        checkDataConnectionStatus = new CheckDataConnectionStatus(this);

        //=== Get Input Application No
        Intent intent = getIntent();
        mInpAppno    =   intent.getStringExtra("ApplicationNo");
        mAPPNO.setText(mInpAppno);
        //=================

        mRecycleCoApp       =  (RecyclerView)findViewById(R.id.rcgurdetails);
        mRecycleCoApp.setHasFixedSize(true);
        mRecycleCoApp.setLayoutManager(new LinearLayoutManager(ClientGurnterDetails.this));
        myPenadapter = new Adapter_CoApplicant_Details(ClientGurnterDetails.this , GetCoAppDetils()) ;
        mRecycleCoApp.setAdapter(myPenadapter);

        mNIC                =       (EditText)findViewById(R.id.txtNIC);
        mFULLNAME           =       (EditText)findViewById(R.id.txtFULLYNAME);
        mADD1               =       (EditText)findViewById(R.id.txtADD1);
        mADD2               =       (EditText)findViewById(R.id.txtADD2);
        mADD3               =       (EditText)findViewById(R.id.txtADD3);
        mADD4               =       (EditText)findViewById(R.id.txtADD4);
        mPHONE              =       (EditText)findViewById(R.id.txtPHONENO);
        mOCUPATION          =       (EditText)findViewById(R.id.txtocupation);
        mINCOMESOURCE       =       (EditText)findViewById(R.id.txtINCOMESOURCE);
        mSECVAL             =       (EditText)findViewById(R.id.txtSECVAL);
        mBrithDay           =       (TextView) findViewById(R.id.txtdatebirth);

        mRELSTYPE           =       (Spinner)findViewById(R.id.txtRELATION);
        mSECTOR             =       (Spinner)findViewById(R.id.txtSECTER);
        mSUBSECTOR          =       (Spinner)findViewById(R.id.txtSUBSECTOR);
        mCLTITLE            =       (Spinner)findViewById(R.id.txtTITLE);
        mGENREAL            =       (Spinner)findViewById(R.id.txtCLFGEN) ;

        mSAVE               =       (Button)findViewById(R.id.btnsave);
        mCLEAR              =       (Button)findViewById(R.id.btnclear);
        mDELETE             =       (Button)findViewById(R.id.btndelete) ;

        rediogroup          =       (RadioGroup)findViewById(R.id.radiotype) ;
        rdoJoin             =       (RadioButton)findViewById(R.id.radioJoin) ;
        rdogur              =       (RadioButton)findViewById(R.id.radioGur);

        mNIC.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mFULLNAME.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mADD1.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mADD2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mADD3.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mADD4.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mPHONE.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mINCOMESOURCE.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mSECVAL.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        //====== Get Date Selection ======================
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(ClientGurnterDetails.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mBrithDay.setText(day + "-" + (month + 1) + "-" + year);
                mBirYear = year;
            }
        }, mYear, mMonth, mDay);


        mBrithDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        //=== Check Data Connection status Toolbra every 5 second.
        //setSupportActionBar(toolbar);
        ConnectionSts = (TextView)findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();

        //=== Occupation

        //=== Ocupation Details Enter =====
        mOCCUPATION     =   findViewById(R.id.spnOCCP);
        List<String> labels_OCCPATION = sqlliteCreateLeasing_AddGur.getOccupation();
        if (labels_OCCPATION != null)
        {
            arrayAdapter_occupa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels_OCCPATION);
            arrayAdapter_occupa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mOCCUPATION.setAdapter(arrayAdapter_occupa);
        }

        //=== Cl title ===
        List<String> listTITLE = new ArrayList<String>();
        listTITLE.add("MR.");
        listTITLE.add("MRS.");
        ArrayAdapter<String> arrayTITLE = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listTITLE);
        arrayTITLE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCLTITLE.setAdapter(arrayTITLE);


        //=== Relastion title ===
        List<String> listRELEASRION = new ArrayList<String>();
        listRELEASRION.add("FRIEND");
        listRELEASRION.add("OTHER");
        arrayRELASTION = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listRELEASRION);
        arrayRELASTION.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRELSTYPE.setAdapter(arrayRELASTION);

        //=== Client Genreal  ===
        List<String> listGENRAL = new ArrayList<String>();
        listGENRAL.add("MALE");
        listGENRAL.add("FEMALE");
        arrayGENREAL = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listGENRAL);
        arrayGENREAL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGENREAL.setAdapter(arrayGENREAL);

        //=== Client Sector  ===


        //=== Load Sector Details
        SqlliteCreateLeasing sqlliteCreateLeasing_SEC = new SqlliteCreateLeasing(this);
        List<String> listSECTOR = sqlliteCreateLeasing_SEC.getAllSector();
        arraySECTOR = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listSECTOR);
        arraySECTOR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSECTOR.setAdapter(arraySECTOR);
        //sqlliteCreateLeasing_SEC.close();

        //=== Client Sub Sector  ===

        mSECTOR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadSubSector();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //=== Load provinces / districk / postal code

        //===== Client Country Details
        mCOUNTRY    = (Spinner)findViewById(R.id.spnCOUNTRY);
        List<String> listcountry = new ArrayList<String>();
        listcountry.add("SRI-LANKA");
        arrayAdaptrCOUNTRY = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listcountry);
        arrayAdaptrCOUNTRY.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCOUNTRY.setAdapter(arrayAdaptrCOUNTRY);
        //============================



        //===== Province ========
        mPRIVSTION  = (Spinner)findViewById(R.id.spnPROVIENCE);
        List<String> labelsPROVSTION = sqlliteCreateLeasing_AddGur.getAllProvince();
        arrayAdapterPROVSTION = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsPROVSTION);
        arrayAdapterPROVSTION.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPRIVSTION.setAdapter(arrayAdapterPROVSTION);



        //=== Get District Code =================
        mDISTRICT           =   (Spinner)findViewById(R.id.spnDISTRICK);
        mPRIVSTION.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //===== District ========

                String mPRNAME = mPRIVSTION.getSelectedItem().toString();
                sqLiteDatabase_AddGur = sqlliteCreateLeasing_AddGur.getReadableDatabase();
                Cursor cursorPCODE = sqLiteDatabase_AddGur.rawQuery("SELECT * FROM AP_MAST_PROVINCE WHERE PRV_NAME = '" + mPRNAME + "'" , null );
                if (cursorPCODE.getCount() != 0)
                {
                    cursorPCODE.moveToFirst();
                    mPrvCode = cursorPCODE.getString(0);
                    List<String> labelsDISTRICK = sqlliteCreateLeasing_AddGur.getAllDistrict(cursorPCODE.getString(0));
                    ArraysAdapterDistric = new ArrayAdapter<String>(ClientGurnterDetails.this, android.R.layout.simple_spinner_item, labelsDISTRICK);
                    ArraysAdapterDistric.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mDISTRICT.setAdapter(ArraysAdapterDistric);
                }
                cursorPCODE.close();
                //==========================
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            //====================================
        });

        //=== Get Area Code =================
        mAREA               =   (Spinner)findViewById(R.id.spnAREA) ;
        mDISTRICT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //===== District ========

                String mDICNAME = mDISTRICT.getSelectedItem().toString();
                sqLiteDatabase_AddGur = sqlliteCreateLeasing_AddGur.getReadableDatabase();
                Cursor cursoAREA = sqLiteDatabase_AddGur.rawQuery("SELECT * FROM AP_MAST_DISTRICT WHERE DIS_NAME = '" + mDICNAME + "'" , null );
                if (cursoAREA.getCount() != 0)
                {
                    cursoAREA.moveToFirst();
                    List<String> labelsAREA = sqlliteCreateLeasing_AddGur.getAllArea(mPrvCode , cursoAREA.getString(1));
                    arrayAdapterAREA = new ArrayAdapter<String>(ClientGurnterDetails.this, android.R.layout.simple_spinner_item, labelsAREA);
                    arrayAdapterAREA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mAREA.setAdapter(arrayAdapterAREA);
                }
                cursoAREA.close();
                //==========================
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            //====================================
        });

        //===== Save Button ========
        mSAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveDataGur();
            }
        });

        //===== Clear Button =====
        mCLEAR = (Button)findViewById(R.id.btnclear);
        mCLEAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSECVAL.setText(""); mINCOMESOURCE.setText(""); mPHONE.setText(""); mADD4.setText(""); mADD3.setText("");
                mADD2.setText(""); mADD1.setText(""); mOCUPATION.setText(""); mFULLNAME.setText(""); mNIC.setText(""); mOCUPATION.setText("");
                mBrithDay.setText("");
                myPenadapter.swapCursor(GetCoAppDetils());

            }
        });

        //==== Delete Button ======
        mDELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builderdeletegur = new AlertDialog.Builder(ClientGurnterDetails.this);
                builderdeletegur.setTitle("AFiMobile-Leasing");
                builderdeletegur.setMessage("Are you sure to delete this application?");
                builderdeletegur.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteDatabase_AddGur = sqlliteCreateLeasing_AddGur.getReadableDatabase();
                        Cursor cursorCHECK = sqLiteDatabase_AddGur.rawQuery("SELECT * FROM LE_CO_CL_DATA WHERE CO_NIC = '" + mNIC.getText().toString() + "'" , null );
                        if (cursorCHECK.getCount() != 0)
                        {
                            sqLiteDatabase_AddGur = sqlliteCreateLeasing_AddGur.getWritableDatabase();
                            sqLiteDatabase_AddGur.delete("LE_CO_CL_DATA" , "CO_NIC =?" , new String[] {mNIC.getText().toString()});
                        }
                        myPenadapter.swapCursor(GetCoAppDetils());
                        mSECVAL.setText(""); mINCOMESOURCE.setText(""); mPHONE.setText(""); mADD4.setText(""); mADD3.setText("");
                        mADD2.setText(""); mADD1.setText(""); mOCUPATION.setText(""); mFULLNAME.setText(""); mNIC.setText(""); mOCUPATION.setText("");
                        Toast.makeText(getApplicationContext(),
                                "Record Successfully Deleted. ",Toast.LENGTH_SHORT).show();
                        cursorCHECK.close();

                    }
                });
                builderdeletegur.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "No Button Clicked",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builderdeletegur.create();
                dialog.show();
            }
        });
    }

    public void LoadSubSector()
    {
        SqlliteCreateLeasing sqlliteCreateLeasing = new SqlliteCreateLeasing(this);
        List<String> labelsSECTORSUB = sqlliteCreateLeasing.getAllSubSector(mSECTOR.getSelectedItem().toString());
        ArrayAdapter<String> arrayAdapterSECTORsub = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsSECTORSUB);
        arrayAdapterSECTORsub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSUBSECTOR.setAdapter(arrayAdapterSECTORsub);
        sqlliteCreateLeasing.close();
    }

    private void startCounting() {
        handler.post(run);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run()
        {

            boolean CheckConnection = checkDataConnectionStatus.IsConnected();

            if (CheckConnection)
            {
                ConnectionSts.setText("ONLINE");
                ConnectionSts.setTextColor(Color. parseColor("#2cb72c"));
            }
            else
            {
                ConnectionSts.setText("OFFLINE");
                ConnectionSts.setTextColor(Color. parseColor("#ffffff"));
            }
            handler.postDelayed(this, 2000);
        }
    };


    public void onStart()
    {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        sqLiteDatabase_AddGur.close();
        sqlliteCreateLeasing_AddGur.close();
        super.onDestroy();
    }

    public Cursor GetCoAppDetils()
    {
        sqLiteDatabase_AddGur = sqlliteCreateLeasing_AddGur.getReadableDatabase();
        Cursor cursorCOAPP = sqLiteDatabase_AddGur.rawQuery("SELECT CO_CLTYPE , CO_NIC , CO_FULLY_NAME FROM LE_CO_CL_DATA WHERE APPLICATION_REF_NO = '" + mInpAppno + "'" , null );
        return cursorCOAPP;
    }


    public void SaveDataGur()
    {
        int selectedId = rediogroup.getCheckedRadioButtonId();
        rdoclgur = (RadioButton) findViewById(selectedId);

        boolean Validate = true;
        String ErrorDescription="";

        if (mNIC.length() == 0)
        {
            Validate = false;
            ErrorDescription="Client Nic No Blank";
        }
        else
        {
            if (mNIC.length() < 10 )
            {
                Validate = false;
                ErrorDescription="Client Nic no Format Invalid.";
            }
            else
            {
                if (mNIC.length() == 10)
                {
                    String NicLast = mNIC.getText().toString().substring(9,10);
                    Log.d("nic " , NicLast);

                    if (NicLast.equals("X") ||  NicLast.equals("V"))
                    {

                    }
                    else
                    {
                        Validate = false;
                        ErrorDescription="Client Nic no Format Invalid.";
                    }
                }
                else if (mNIC.length() != 12)
                {
                    Validate = false;
                    ErrorDescription="Client Nic no Format Invalid.";
                }
            }
        }

        if (mFULLNAME.length() ==0)
        {
            Validate = false;
            ErrorDescription="<FULLY NAME> Is Blank";
        }
        else if (mADD1.length() == 0)
        {
            Validate = false;
            ErrorDescription="<ADDERS 1> Is Blank";
        }
        else if (mADD2.length() == 0)
        {
            Validate = false;
            ErrorDescription="<ADDERS 2> Is Blank";
        }
        else if (mADD3.length() == 0)
        {
            Validate = false;
            ErrorDescription="<ADDERS 3> Is Blank";
        }
        else if (mPHONE.length() == 0)
        {
            Validate = false;
            ErrorDescription="<PHONE NO> Is Blank";
        }
        else if (mBrithDay.length() ==0)
        {
            Validate = false;
            ErrorDescription="<BirthDay> Is Blank";
        }

        //=== To check NIC Availabe==== ()
        sqLiteDatabase_AddGur = sqlliteCreateLeasing_AddGur.getReadableDatabase();
        Cursor cursorCHECK = sqLiteDatabase_AddGur.rawQuery("SELECT * FROM LE_CO_CL_DATA WHERE CO_NIC = '" + mNIC.getText().toString() + "'" , null );
        if (cursorCHECK.getCount() != 0)
        {
            sqLiteDatabase_AddGur = sqlliteCreateLeasing_AddGur.getWritableDatabase();
            sqLiteDatabase_AddGur.delete("LE_CO_CL_DATA" , "CO_NIC =?" , new String[] {mNIC.getText().toString()});
        }
        cursorCHECK.close();

        if (Validate)
        {

            //=== Calculate Age to Client ======
            Calendar Today = Calendar.getInstance();
            int AgeClient;
            AgeClient =  Today.get(Calendar.YEAR) - mBirYear;

            sqLiteDatabase_AddGur = sqlliteCreateLeasing_AddGur.getWritableDatabase();
            ContentValues contentValuesGURDETAILS = new ContentValues();
            contentValuesGURDETAILS.put("APPLICATION_REF_NO" , mInpAppno);
            contentValuesGURDETAILS.put("CO_CLTYPE" , rdoclgur.getText().toString());
            contentValuesGURDETAILS.put("CO_NIC" , mNIC.getText().toString());
            contentValuesGURDETAILS.put("CO_FULLY_NAME" , mFULLNAME.getText().toString());
            contentValuesGURDETAILS.put("CO_INITIALS" , "");
            contentValuesGURDETAILS.put("CO_LASTNAME" , "");
            contentValuesGURDETAILS.put("CO_DATE_OF_BIRTH" , mBrithDay.getText().toString());
            contentValuesGURDETAILS.put("CO_AGE" , AgeClient);
            contentValuesGURDETAILS.put("CO_OCCUPATION" , mOCCUPATION.getSelectedItem().toString());
            contentValuesGURDETAILS.put("CO_ADDERS_1" , mADD1.getText().toString());
            contentValuesGURDETAILS.put("CO_ADDERS_2" , mADD2.getText().toString());
            contentValuesGURDETAILS.put("CO_ADDERS_3" , mADD3.getText().toString());
            contentValuesGURDETAILS.put("CO_ADDERS_4" , mADD4.getText().toString());
            contentValuesGURDETAILS.put("CO_TITLE" , mCLTITLE.getSelectedItem().toString());
            contentValuesGURDETAILS.put("CO_GENDER" , mGENREAL.getSelectedItem().toString());
            contentValuesGURDETAILS.put("CO_MOBILE_NO" , mPHONE.getText().toString());
            contentValuesGURDETAILS.put("CO_SECTOR" , mSECTOR.getSelectedItem().toString());
            contentValuesGURDETAILS.put("CO_SUBSECTOR" , mSUBSECTOR.getSelectedItem().toString());
            contentValuesGURDETAILS.put("CO_INCOME" , mINCOMESOURCE.getText().toString());
            contentValuesGURDETAILS.put("CO_MARITAL_STATUS" , mSECVAL.getText().toString());
            contentValuesGURDETAILS.put("CO_SEC_VALUE" , mSECVAL.getText().toString());
            contentValuesGURDETAILS.put("CO_CREATE_DATE" , LoginDate);
            contentValuesGURDETAILS.put("CO_CREATE_USER" , LoginUser);
            contentValuesGURDETAILS.put("CO_BRANCH" , LoginBranch);
            contentValuesGURDETAILS.put("RELATIONS_TYPE" , mRELSTYPE.getSelectedItem().toString());
            contentValuesGURDETAILS.put("CO_COUNTRY" , mCOUNTRY.getSelectedItem().toString());
            contentValuesGURDETAILS.put("CO_PROVINCE" , mPRIVSTION.getSelectedItem().toString());
            contentValuesGURDETAILS.put("CO_DISTRICT" , mDISTRICT.getSelectedItem().toString());
            contentValuesGURDETAILS.put("CO_AREA_CODE" , mAREA.getSelectedItem().toString());
            sqLiteDatabase_AddGur.insert("LE_CO_CL_DATA" , null , contentValuesGURDETAILS);

            String Msg = "You have succesfully Saved." ;
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this );
            myAlert.setMessage(Msg).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            })
                    .setTitle("AFSL Mobile Leasing.")
                    .create();
            myAlert.show();

            mSECVAL.setText(""); mINCOMESOURCE.setText(""); mPHONE.setText(""); mADD4.setText(""); mADD3.setText("");
            mADD2.setText(""); mADD1.setText(""); mFULLNAME.setText(""); mNIC.setText("");  mBrithDay.setText("");

            myPenadapter.swapCursor(GetCoAppDetils());
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this );
            String titleText = "******** Input Data Error *******";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
            ssBuilder.setSpan(
                    foregroundColorSpan,
                    0,
                    titleText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            builder.setTitle(ssBuilder);
            builder.setMessage(ErrorDescription);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
