   package com.AFiMOBILE.afslmobileapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


   public class CreateApplication extends AppCompatActivity {

    private Spinner mNatureIncome ,  mOcupation, mMake, mModel, mSupplier, mDelear, mInsurace, Mproduct , mAssetT , mEquNature , mIntduser , mClSpnTitle;
    private EditText mNic, mFullyName, mAdd1, mAdd2, mAdd3, mAdd4, mPhoneno, mClRemarks;
    private EditText mMarketVal, mInvoiceAmt, mClconAmt, mExpRate , mLeasingAmt, mRate, mPerid, mIndAmt, mServiceAmt, mRmvAmt, mInsAmt, mTrpAmt, mOthAmt;
    private EditText mStamDuty , mUpforntAmt, mCaplizedAmnt, mRental , mFacamt;
    private EditText mYear, mRegNo, mEngNo, mchassNo, mMEremarks , mIndsuppName , mInsuuAdders;
    private Switch mStdCap , mIndCap, mSchCap, mRMVCap, mInsCap, mTransport, mOthCha;
    private TextView mInputAppno;
    private Button mNicSeracn, mSaveData, mClearData, mCalRental , mRefinancechck;
    public ProgressDialog progressDialog;
    public static Toolbar toolbar;
    public TextView ConnectionSts, mDateofbirth;
    public Handler handler;
    public int mSelectYear=0 , mMonth=0 , mDay=0;
    public DatePickerDialog  dialog;
    public RequestQueue mQueue;
    public String mDraft_Nic ,  Reg_Age_catgery="" , LoginUser="" , LoginDate="" , LoginBranch="" , mModelPararate , mEqType_ltv ,   mMakeCode , mMoelCode , mSTDapply , mSTDAmt ,  mInpAppno , mInpType , PHP_URL_SQL , mEtvRate , mIntRate , mMaxFacAmt , mMinFacAmt , mMax_Int_rate;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public ArrayAdapter<String> arrayAdapter_occtype , arrayAdapter_product_con , arrayAdapter_occupa , arrayAdapterMODEL , arrayAdaptereqnature,  arrayAdapterMAKE
            , arrayAdapterins , arrayAdaptersupplier , arrayAdapterdelear , arrayAdapterintduser  , arrayAdaptereqtype , arrayTITLE , arrayAdapterMODEL_modify;
    public SqlliteCreateLeasing sqlliteCreateLeasing_CreateApplicartion;
    public SQLiteDatabase sqLiteDatabase_Create_application;
    public int mBirYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_application);

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));

        mInputAppno = findViewById(R.id.txtinpappno);

        //=== Get Input Application No
        Intent intent = getIntent();
        mInpAppno    =   intent.getStringExtra("ApplicationNo");
        mInpType     =   intent.getStringExtra("Type");

        checkDataConnectionStatus   = new CheckDataConnectionStatus(CreateApplication.this);
        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        //=== Check Data Connection status Toolbra every 5 second.
        //setSupportActionBar(toolbar);
        ConnectionSts = (TextView)findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();

        //===== DataBase Connection
        sqlliteCreateLeasing_CreateApplicartion = new SqlliteCreateLeasing(this);
        sqLiteDatabase_Create_application = sqlliteCreateLeasing_CreateApplicartion.getReadableDatabase();

        //=== Spinner
        mMake       = (Spinner) findViewById(R.id.spnmake);
        mModel      = (Spinner) findViewById(R.id.spnmodel);
        mSupplier   = (Spinner) findViewById(R.id.spnsupplier);
        mDelear     = (Spinner) findViewById(R.id.spndelear);
        Mproduct    = (Spinner) findViewById(R.id.spnproduct);
        mInsurace   = (Spinner) findViewById(R.id.spnins);
        mAssetT     = (Spinner) findViewById(R.id.spneqtype) ;
        mEquNature  = (Spinner) findViewById(R.id.spnEQNATURE);
        mIntduser   = (Spinner)findViewById(R.id.spnind);
        mOcupation  = (Spinner)findViewById(R.id.spnOCCP);
        mClSpnTitle = (Spinner)findViewById(R.id.txtTITLE);
        mNatureIncome   =   (Spinner)findViewById(R.id.txtincomsource);


        //==== Create Please Wait Massage ====
        progressDialog = new ProgressDialog(CreateApplication.this);
        progressDialog.setTitle("AFi Mobile");
        progressDialog.setMessage("Loading application View, please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);

        //=== Text Filed Details =====
        mNic        = findViewById(R.id.edtnic);
        mFullyName  = findViewById(R.id.txtclname);
        mAdd1       = findViewById(R.id.txtadd1);
        mAdd2       = findViewById(R.id.txtadd2);
        mAdd3       = findViewById(R.id.txtadd3);
        mAdd4       = findViewById(R.id.txtadd4);
        mPhoneno    = findViewById(R.id.txtphoneno);
        mClRemarks  = findViewById(R.id.txtclremarks);

        mMarketVal  = findViewById(R.id.txtmkval);
        mInvoiceAmt = findViewById(R.id.txtinvoiceamt);
        mClconAmt   = findViewById(R.id.txtclientcontbution);
        mLeasingAmt = findViewById(R.id.txtleasingamt);
        mRate       = findViewById(R.id.txtrate);
        mPerid      = findViewById(R.id.txtpeiod);
        mIndAmt     = findViewById(R.id.txtindcom);
        mServiceAmt = findViewById(R.id.txtservicecharge);
        mRmvAmt     = findViewById(R.id.txtrmvcharge);
        mInsAmt     = findViewById(R.id.txtinsurancecharge);
        mTrpAmt     = findViewById(R.id.txttransportcharge);
        mOthAmt     = findViewById(R.id.txtothercharge);
        mExpRate    = findViewById(R.id.txtexpourse);
        mStamDuty   = findViewById(R.id.txtstampduty);

        mFacamt         =   findViewById(R.id.txtfacilityaamount);
        mUpforntAmt     = findViewById(R.id.txtupfornt);
        mCaplizedAmnt   = findViewById(R.id.txtcapilized);
        mRental         = findViewById(R.id.txtrental);

        mYear       = findViewById(R.id.txtyear);
        mRegNo      = findViewById(R.id.txtregno);
        mEngNo      = findViewById(R.id.txtengno);
        mchassNo    = findViewById(R.id.txtchassino);
        mMEremarks  = findViewById(R.id.txtmeremarks);

        mIndsuppName    =   findViewById(R.id.txtindsuppname);
        mInsuuAdders    =   findViewById(R.id.txtindsuppadders);

        mIndsuppName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mInsuuAdders.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mIndsuppName.setEnabled(false);
        mInsuuAdders.setEnabled(false);

        mIndCap     = findViewById(R.id.SwtindCap);
        mSchCap     = findViewById(R.id.SwtschCap);
        mRMVCap     = findViewById(R.id.SwtrmvCap);
        mInsCap     = findViewById(R.id.SwtincCap);
        mTransport  = findViewById(R.id.SwttrfCap);
        mOthCha     = findViewById(R.id.SwtothCap);
        mStdCap     = findViewById(R.id.SwtStdCap);

        mIndCap.setChecked(true);
        mSchCap.setChecked(true);
        mRMVCap.setChecked(true);
        mInsCap.setChecked(true);
        mTransport.setChecked(true);
        mOthCha.setChecked(true);

        //====Load Re-finance Chck
        /*
        mRefinancechck = (Button)findViewById(R.id.btncalRefinancecheck) ;
        mRefinancechck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRegNo.getText().toString() != "")
                {
                    Intent intent_refinance = new Intent("android.intent.Re_Finance_Check");
                    intent_refinance.putExtra("VEH_NO" , mRegNo.getText().toString());
                    intent_refinance.putExtra("FAC_AMT" , mFacamt.getText().toString());
                    startActivity(intent_refinance);
                }
            }
        });

         */


        mNicSeracn  = findViewById(R.id.btnserach);
        mSaveData   = findViewById(R.id.btnmodify);
        mClearData  = findViewById(R.id.btncledata);
        mCalRental  = findViewById(R.id.btncalRental);

        mCalRental  = findViewById(R.id.btncalRental);
        mSaveData   = findViewById(R.id.btnmodify);


        //=== Edit Text All Caps Assign  ===
        mNic.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mFullyName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mAdd1.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mAdd2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mAdd3.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mAdd4.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mPhoneno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mClRemarks.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mMarketVal.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mInvoiceAmt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mClconAmt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mRegNo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mEngNo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mchassNo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mMEremarks.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        //=== Date of Birth - Date picker
        //====== Get Date Selection ======================
        mDateofbirth = (TextView) findViewById(R.id.txtdatebirth);

        final Calendar c = Calendar.getInstance();
        mSelectYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(CreateApplication.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mDateofbirth.setText(day + "-" + (month + 1) + "-" + year);
                mBirYear = year;
            }
        }, mSelectYear, mMonth, mDay);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mDateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });

        if (mInpType.equals("M"))
        {
            mInputAppno.setText(mInpAppno);
            mInputAppno.setVisibility(View.VISIBLE);

            mSaveData.setEnabled(true);
            mSaveData.setBackgroundResource(R.drawable.normalbutton);

        }
        else if (mInpType.equals("D"))  //=== Draft Application Create
        {
            mDraft_Nic = mInpAppno;

            Cursor cursor_drfaft = sqLiteDatabase_Create_application.rawQuery("SELECT * FROM DRAFT_APPLICATION WHERE NIC = '" + mDraft_Nic + "'" ,  null);
            if (cursor_drfaft.getCount() != 0)
            {
                cursor_drfaft.moveToFirst();

                do{
                    mNic.setText(cursor_drfaft.getString(0));
                    mFullyName.setText(cursor_drfaft.getString(1));
                    mAdd1.setText(cursor_drfaft.getString(2));
                    mAdd2.setText(cursor_drfaft.getString(3));
                    mAdd3.setText(cursor_drfaft.getString(4));
                    mAdd4.setText(cursor_drfaft.getString(4));
                }while (cursor_drfaft.moveToNext());
            }
        }
        else
        {
            mInputAppno.setText("");
            mInputAppno.setVisibility(View.INVISIBLE);
        }

        LoadMasterData();

        //======= Clear Data =====
        mClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFullyName.setText(""); mNic.setText(""); mAdd1.setText(""); mAdd2.setText("");
                mAdd3.setText(""); mAdd4.setText(""); mPhoneno.setText("");
                mInvoiceAmt.setText(""); mExpRate.setText(""); mRate.setText(""); mPerid.setText("");
                mIndAmt.setText(""); mServiceAmt.setText(""); mRmvAmt.setText("");
                mInsAmt.setText(""); mOthAmt.setText(""); mTrpAmt.setText("");
                mYear.setText(""); mRegNo.setText(""); mEngNo.setText(""); mchassNo.setText("");
                mClconAmt.setText(""); mFacamt.setText(""); mUpforntAmt.setText("");
                mRental.setText(""); mCaplizedAmnt.setText(""); mMarketVal.setText(""); mMEremarks.setText("");

                mIndCap.setChecked(false);
                mSchCap.setChecked(false);
                mRMVCap.setChecked(false);
                mOthCha.setChecked(false);
                mInsCap.setChecked(false);
                mTransport.setChecked(false);

                mSaveData.setEnabled(false);

                mSaveData.setBackgroundResource(R.drawable.normalbuttondisable);

            }
        });
        //========================


        //====== Serach Exsistiong Client ===
        mNicSeracn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFullyName.setText("");  mAdd1.setText("");
                mAdd2.setText("");  mAdd3.setText("");  mAdd4.setText("");
                  mPhoneno.setText("");

                if (mNic.getText().toString() != "")
                {
                    //== Data Off Local Serach

                    Cursor cursorNIC_SERCH = sqLiteDatabase_Create_application.rawQuery("SELECT * FROM LE_CLIENT_DATA WHERE CL_NIC = '" + mNic.getText().toString() + "'" , null );
                    if (cursorNIC_SERCH.getCount() != 0)
                    {
                        cursorNIC_SERCH.moveToFirst();
                        mClSpnTitle.setSelection(arrayTITLE.getPosition(cursorNIC_SERCH.getString(17)));
                        mFullyName.setText(cursorNIC_SERCH.getString(3));
                        mAdd1.setText(cursorNIC_SERCH.getString(4));
                        mAdd2.setText(cursorNIC_SERCH.getString(5));
                        mAdd3.setText(cursorNIC_SERCH.getString(6));
                        mAdd4.setText(cursorNIC_SERCH.getString(7));
                        mPhoneno.setText(cursorNIC_SERCH.getString(14));
                    }
                    else
                    {
                        boolean mCheckSts = checkDataConnectionStatus.IsConnected();
                        if (mCheckSts == true)
                        {
                            SerchOnlineClient();
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateApplication.this);
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
                    cursorNIC_SERCH.close();
                }
            }
        });




        mSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
            }
        });

        mCalRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean Validate = true;
                String ErrorDescription="";


                if (mRate.length() != 0)
                {
                    if (Double.parseDouble(mRate.getText().toString()) < Double.parseDouble(mIntRate))
                    {
                        Validate = false;
                        ErrorDescription="<Interest Rate> Cannot be a Less than Product Rate - " + mIntRate + "%";
                    }
                }

                if (mRate.length() != 0)
                {
                    if (Double.parseDouble(mRate.getText().toString()) > Double.parseDouble(mMax_Int_rate))
                    {
                        Validate = false;
                        ErrorDescription="<Interest Rate> Cannot be a Grater than Product Rate - " + mMax_Int_rate + "%";
                    }
                }

                if (mLeasingAmt.length() !=0)
                {
                    if  (Double.parseDouble(mLeasingAmt.getText().toString().replace(",","")) >  Double.parseDouble(mMaxFacAmt))
                    {
                        Validate = false;
                        ErrorDescription="<Facility Amount> Cannot be a grate than product Amount - " + mMaxFacAmt ;
                    }
                    else
                    if  ( Double.parseDouble(mLeasingAmt.getText().toString().replace(",","")) <  Double.parseDouble(mMinFacAmt))
                    {
                        Validate = false;
                        ErrorDescription="<Facility Amount> Cannot be a less than product Amount - " + mMinFacAmt ;
                    }
                }

                if (mPerid.getText().length() == 0)
                {
                    Validate = false;
                    ErrorDescription="<Tenure> Cannot be Zero." ;
                }
                else
                if (mRate.getText().length() ==0)
                {
                    Validate = false;
                    ErrorDescription="<Rate> Cannot be Zero." ;
                }
                else
                if (mExpRate.length() > 0)
                {
                    if ( Double.parseDouble(mExpRate.getText().toString())> Double.parseDouble(mEtvRate) )
                    {
                        Validate = false;
                        ErrorDescription="<ETV Rate> Cannot Grater than Product Rate -" + mEtvRate + " %";
                    }
                }
                else
                if (mMarketVal.getText().length() == 0)
                {
                    Validate = false;
                    ErrorDescription="<Market Value> Cannot be Zero." ;
                }
                else
                if (mInvoiceAmt.getText().length() == 0)
                {
                    Validate = false;
                    ErrorDescription="<Invoice Value> Cannot be Zero." ;
                }


                if (Validate == true)
                {
                    RentalCalculate();

                    mSaveData.setEnabled(true);
                    mSaveData.setBackgroundResource(R.drawable.normalbutton);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateApplication.this);
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
        });


        mInvoiceAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                CalFacilityAmount();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mClconAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                CalFacilityAmount();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //=== Check Black list model code
        mModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (mModel.getSelectedItem().toString() != "")
                {
                    SQLiteDatabase sqLiteDatabase_model_check = sqlliteCreateLeasing_CreateApplicartion.getReadableDatabase();
                    Cursor cursor_model = sqLiteDatabase_model_check.rawQuery("SELECT MODEL_STS FROM AP_MAST_MODEL WHERE MODEL_DESC = '" + mModel.getSelectedItem().toString() + "'" , null );
                    if (cursor_model.getCount() != 0)
                    {
                        cursor_model.moveToFirst();
                        if (cursor_model.getString(0).equals("BLOCK"))
                        {
                            mModel.setSelection(arrayAdapterMODEL.getPosition(" "));
                            AlertDialog.Builder builder_DELEAR = new AlertDialog.Builder(CreateApplication.this);
                            builder_DELEAR.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                            builder_DELEAR.setMessage("Cannot Select this model. Model is Blacklisted");
                            builder_DELEAR.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();
                                }
                            });
                            builder_DELEAR.create();
                            builder_DELEAR.show();
                        }
                    }
                    cursor_model.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //===== Ind supplier select to add the supplier detais
        mSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSupplier.getSelectedItem().toString().equals("INDIVIDUAL SUPPLIER-DIRECT"))
                {
                    mIndsuppName.setEnabled(true);
                    mInsuuAdders.setEnabled(true);

                    mIndsuppName.setText("");
                    mInsuuAdders.setText("");
                }
                else
                {

                    mIndsuppName.setText("");
                    mInsuuAdders.setText("");

                    mIndsuppName.setEnabled(false);
                    mInsuuAdders.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //=== Check Delear Balck Listed Details
        mDelear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                //Log.d("DELERE" , mDelear.getSelectedItem().toString().substring(0,mDelear.getSelectedItem().toString().indexOf("-")));

                String Delarename = mDelear.getSelectedItem().toString();

                SQLiteDatabase sqLiteDatabase_delear = sqlliteCreateLeasing_CreateApplicartion.getReadableDatabase();
                Cursor cursor_delear = sqLiteDatabase_delear.rawQuery("SELECT DELEAR_STS FROM AP_MAST_OWNER WHERE SHOWROOM_N = '" + Delarename + "'" , null);
                if (cursor_delear.getCount() != 0)
                {
                    cursor_delear.moveToFirst();
                    if (cursor_delear.getString(0).equals("BLOCK"))
                    {
                        mDelear.setSelection(arrayAdapterdelear.getPosition(" "));
                        AlertDialog.Builder builder_DELEAR = new AlertDialog.Builder(CreateApplication.this);
                        builder_DELEAR.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                        builder_DELEAR.setMessage("Cannot Request the po this dealer. Dealer is Blacklisted");
                        builder_DELEAR.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });
                        builder_DELEAR.create();
                        builder_DELEAR.show();
                    }
                    cursor_delear.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //=== Get Model Code
        mMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadModel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //==== Get Product Cofig Details ===========================
        Mproduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                //====  Product Config Details
                Cursor cursor_PR = sqLiteDatabase_Create_application.rawQuery("SELECT * FROM MAST_PRODUCT_CONFIG WHERE APP_PR_CODE = '" + Mproduct.getSelectedItem().toString() + "'" , null );
                if (cursor_PR.getCount() != 0)
                {
                    cursor_PR.moveToFirst();
                    mEtvRate        =       cursor_PR.getString(2);
                    mIntRate        =       cursor_PR.getString(3);
                    mMax_Int_rate   =       cursor_PR.getString(4);
                    mMinFacAmt      =       cursor_PR.getString(5);
                    mMaxFacAmt      =       cursor_PR.getString(6);
                }
                cursor_PR.close();

                //==== Get Product Wise Paramater

                //  Stamp Duty
                mSTDapply = "YES";
                Cursor cursor_PARA = sqLiteDatabase_Create_application.rawQuery("SELECT * FROM MAST_PR_PARAMATER WHERE PRRA_CODE = 'STD' AND PR_CODE = '" + Mproduct.getSelectedItem().toString() + "'" , null);
                if (cursor_PARA.getCount() != 0)
                {
                    cursor_PARA.moveToFirst();
                    if(cursor_PARA.getString(4) != "0")
                    {
                        mSTDAmt = cursor_PARA.getString(4);
                    }
                    else
                    {
                        mSTDapply = "NO";
                    }
                }
                else
                {
                    mSTDapply = "NO";
                }
                cursor_PARA.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                mEtvRate=""; mIntRate =""; mMaxFacAmt=""; mMinFacAmt=""; mMax_Int_rate="";
            }
        });
        //==============================
    }

    private void startCounting() {
        handler.post(run);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run()
        {

            boolean CheckConnection = checkDataConnectionStatus.IsConnected();

            if (CheckConnection ==true)
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

    protected void onStart()
    {
        super.onStart();
        if (mInpType.equals("M"))
        {
            LoadModifyData();
        }

        mSaveData.setEnabled(false);


        mSaveData.setBackgroundResource(R.drawable.normalbuttondisable);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void onDestroy(){

        sqLiteDatabase_Create_application.close();
        sqlliteCreateLeasing_CreateApplicartion.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        super.onDestroy();

    }

    public void SerchOnlineClient()
    {
        /*=== Get the New Data App codig File*/
        mQueue   = VollySingleton.getInstance(this).getRequestQueue();
        JSONObject object = new JSONObject();
        progressDialog.show();
        try {
            object.put("CL_NIC" , mNic.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = PHP_URL_SQL + "Mobile-ClientSearch.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{

                            JSONArray myjson = response.getJSONArray("TT-SERCH-CLDATA");

                            if (myjson.length() != 0)
                            {
                                for (int i = 0; i <= myjson.length(); i++)
                                {
                                    JSONObject Clserch = myjson.getJSONObject(i) ;
                                    mClSpnTitle.setSelection(arrayTITLE.getPosition(Clserch.getString("CL_TITLE")));
                                    mFullyName.setText(Clserch.getString("CL_FULLY_NAME"));
                                    mAdd1.setText(Clserch.getString("CL_ADDERS_1"));
                                    mAdd2.setText(Clserch.getString("CL_ADDERS_2"));
                                    mAdd3.setText(Clserch.getString("CL_ADDERS_3"));
                                    mAdd4.setText(Clserch.getString("CL_ADDERS_4"));
                                    //mOcupation.setText(Clserch.getString("CL_OCCUPATION"));
                                    mPhoneno.setText(Clserch.getString("CL_MOBILE_NO"));
                                    progressDialog.dismiss();
                                }
                            }
                            else
                            {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(CreateApplication.this);
                                builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                                builder.setMessage("Client Search Data Not Found");
                                builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create();
                                builder.show();
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(CreateApplication.this );
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jsonObjectRequest);
    }


    public void LoadModel() {
        String MakeCode = mMake.getSelectedItem().toString();
        SqlliteCreateLeasing db = new SqlliteCreateLeasing(this);
        List<String> labels = db.getAllModelSqllite(MakeCode);
        labels.add(" ");
        if (labels != null) {
            arrayAdapterMODEL = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
            arrayAdapterMODEL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mModel.setAdapter(arrayAdapterMODEL);
        }
    }

    //=== Load Master Data
    public void LoadMasterData() {

        //==== Client Tile
        //=== Cl title ===
        List<String> listTITLE = new ArrayList<String>();
        listTITLE.add("MR.");
        listTITLE.add("MRS.");
        arrayTITLE = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listTITLE);
        arrayTITLE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mClSpnTitle.setAdapter(arrayTITLE);

        //==== Prpoduct Details ======
        SqlliteCreateLeasing db = new SqlliteCreateLeasing(this);
        List<String> labels_PRO_CON = db.getProductConfig();
        if (labels_PRO_CON != null)
        {
            arrayAdapter_product_con = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels_PRO_CON);
            arrayAdapter_product_con.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Mproduct.setAdapter(arrayAdapter_product_con);
        }

        //=== Ocupation Details Enter =====
        List<String> labels_OCCPATION = db.getOccupation();
        if (labels_OCCPATION != null)
        {
            arrayAdapter_occupa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels_OCCPATION);
            arrayAdapter_occupa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mOcupation.setAdapter(arrayAdapter_occupa);
        }

        //=== Make Delete ....
        List<String> labels = db.getAllMakeSqllite();
        arrayAdapterMAKE = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        arrayAdapterMAKE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMake.setAdapter(arrayAdapterMAKE);

        //=== Insurance Details
        List<String> labelsins = db.getAllInsuranceSqllite();
        if (labelsins != null) {
            arrayAdapterins = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsins);
            arrayAdapterins.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mInsurace.setAdapter(arrayAdapterins);
        }

        //=== Supplier Details
        List<String> labelssupplier = db.getAllSupplierSqllite();
        if (labelssupplier != null) {
            arrayAdaptersupplier = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelssupplier);
            arrayAdaptersupplier.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSupplier.setAdapter(arrayAdaptersupplier);
        }

        //=== Delera Details
        List<String> labelsdelear = db.getAllDelearSqllite();
        labelsdelear.add(" ");
        if (labelsdelear != null) {
            arrayAdapterdelear = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsdelear);
            arrayAdapterdelear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDelear.setAdapter(arrayAdapterdelear);
        }
        mDelear.setSelection(arrayAdapterdelear.getPosition(" "));

        //=== Intduser Details
        List<String> labelInduser = db.getAllIntduscerSqllite();
        if (labelInduser != null) {
            arrayAdapterintduser = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelInduser);
            arrayAdapterintduser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mIntduser.setAdapter(arrayAdapterintduser);
        }
        //===========================================


        // Equment Type Details
        List<String> listEqument = new ArrayList<String>();
        listEqument.add("Two Wheeler Lease");
        listEqument.add("Three Wheeler Lease");
        listEqument.add("Personal Loan");
        arrayAdaptereqtype = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listEqument);
        arrayAdaptereqtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAssetT.setAdapter(arrayAdaptereqtype);

        // Equment Nature Details
        List<String> listEqumentnature = new ArrayList<String>();
        listEqumentnature.add("UN-REGISTERED VEHICLE");
        listEqumentnature.add("REGISTERED VEHICLE");
        arrayAdaptereqnature = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listEqumentnature);
        arrayAdaptereqnature.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEquNature.setAdapter(arrayAdaptereqnature);

        //=== Occupation Type
        List<String> labelOcctype = db.getAllOccupationType();
        if (labelOcctype != null)
        {
            arrayAdapter_occtype = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelOcctype);
            arrayAdapter_occtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mNatureIncome.setAdapter(arrayAdapter_occtype);
        }
    }

    public void RentalCalculate() {
        Double mRentalamount = 0.00;
        Double mTotalCharge = 0.00;
        Double mTotalUpforntCharge = 0.00;
        Double mInterestRate = 0.00;
        Double mLoanAmount = 0.00;
        Double mLoanPerid = 0.00;
        Double mFacilityAmount = 0.00;
        Long RentalAmountRounded;


        //===== Charges Varible ========
        Double IntduserCharge = 0.00;
        Double ServiceCharge = 0.00;
        Double RMVCharge = 0.00;
        Double Inscharge = 0.00;
        Double TranportCharge = 0.00;
        Double Othercharge = 0.00;
        Double Stampcharge = 0.00;

        Double UpforntIntduserCharge = 0.00;
        Double UpforntServiceCharge = 0.00;
        Double UpforntRMVCharge = 0.00;
        Double UpforntInscharge = 0.00;
        Double UpforntTranportCharge = 0.00;
        Double UpforntOthercharge = 0.00;
        Double UpforntStampcharge = 0.00;


        //=== Get Varible Details =======

        if (mLeasingAmt.length() != 0) {
            mLoanAmount = Double.parseDouble(mLeasingAmt.getText().toString().replace("," , ""));
        }

        if (mRate.length() != 0) {
            mInterestRate = Double.parseDouble(mRate.getText().toString());
        }

        if (mPerid.length() != 0) {
            mLoanPerid = Double.parseDouble(mPerid.getText().toString());
        }

        //=== Intduser Commsion
        if (mIndAmt.length() != 0) {
            if (mIndCap.isChecked()) {
                IntduserCharge = Double.parseDouble(mIndAmt.getText().toString());
            } else {
                UpforntIntduserCharge = Double.parseDouble(mIndAmt.getText().toString());
            }
        }

        //=== Service Charges
        if (mServiceAmt.length() != 0) {
            if (mSchCap.isChecked()) {
                ServiceCharge = Double.parseDouble(mServiceAmt.getText().toString());
            } else {
                UpforntServiceCharge = Double.parseDouble(mServiceAmt.getText().toString());
            }
        }

        //=== RMV Charges
        if (mRmvAmt.length() != 0) {
            if (mRMVCap.isChecked()) {
                RMVCharge = Double.parseDouble(mRmvAmt.getText().toString());
            } else {
                UpforntRMVCharge = Double.parseDouble(mRmvAmt.getText().toString());
            }
        }


        //=== Insurance Amt
        if (mInsAmt.length() != 0) {
            if (mInsCap.isChecked()) {
                Inscharge = Double.parseDouble(mInsAmt.getText().toString());
            } else {
                UpforntInscharge = Double.parseDouble(mInsAmt.getText().toString());
            }
        }

        //=== Tranport Charges
        if (mTrpAmt.length() != 0) {
            if (mTransport.isChecked()) {
                TranportCharge = Double.parseDouble(mTrpAmt.getText().toString());
            } else {
                UpforntTranportCharge = Double.parseDouble(mTrpAmt.getText().toString());
            }
        }

        //=== Other Charges
        if (mOthAmt.length() != 0) {
            if (mOthCha.isChecked()) {
                Othercharge = Double.parseDouble(mOthAmt.getText().toString());
            } else {
                UpforntOthercharge = Double.parseDouble(mOthAmt.getText().toString());
            }
        }

        //=== Stamp Duty Charges
        if (mStamDuty.length() != 0) {
            if (mStdCap.isChecked()) {
                Stampcharge = Double.parseDouble(mStamDuty.getText().toString());
            } else {
                UpforntStampcharge = Double.parseDouble(mStamDuty.getText().toString());
            }
        }

        //===============================
        mTotalCharge = Stampcharge + Othercharge + TranportCharge + Inscharge + RMVCharge + ServiceCharge + IntduserCharge;
        mTotalUpforntCharge = UpforntStampcharge + UpforntOthercharge + UpforntTranportCharge + UpforntInscharge + UpforntRMVCharge + UpforntServiceCharge + UpforntIntduserCharge;

        //========== Calculate Rental Amount ============
        Double r = mInterestRate / 1200;
        Double r1 = Math.pow(r + 1, mLoanPerid);
        mRentalamount = (Double) ((r + (r / (r1 - 1))) * (mLoanAmount + mTotalCharge));

        RentalAmountRounded = Math.round(mRentalamount);

        DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");

        mUpforntAmt.setText(nf.format(mTotalUpforntCharge));
        mCaplizedAmnt.setText(nf.format(mTotalCharge));
        mRental.setText(nf.format(RentalAmountRounded));
        mFacamt.setText(nf.format(mLoanAmount + mTotalCharge));

    }

    public void CalFacilityAmount()
    {
        try
        {
            Double FacilityAmount=0.00;
            Double Invoiceamt=0.00;
            Double ClientConAmt=0.00;
            Double mExposeRate=0.00;
            Double temMarketVal=0.00;
            Double StamDuty=0.00;

            if (mInvoiceAmt.length() != 0)
            {
                Invoiceamt    =  Double.parseDouble(mInvoiceAmt.getText().toString());
            }

            if (mClconAmt.length() != 0)
            {
                ClientConAmt  =  Double.parseDouble(mClconAmt.getText().toString());
            }

            if (mMarketVal.length() != 0)
            {
                temMarketVal =   Double.parseDouble(mMarketVal.getText().toString());
            }

            DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");
            FacilityAmount = (Invoiceamt - ClientConAmt);
            mLeasingAmt.setText(nf.format(FacilityAmount));
            mExposeRate = FacilityAmount / temMarketVal * 100;
            mExpRate.setText(mExposeRate.toString());

            if (mSTDapply == "YES")
            {
                StamDuty = (FacilityAmount * Double.parseDouble(mSTDAmt));
                mStamDuty.setText(StamDuty.toString());
            }
            else
            {
                mStamDuty.setText("");
            }

        }catch (NumberFormatException e)
        {

        }
    }

    public void SaveData()
    {
        boolean Validate = true;
        String ErrorDescription="";
        //===== To Validate the Data =====

        //==== Nmic Validate

        if (mNic.length() == 0)
        {
            Validate = false;
            ErrorDescription="Client Nic No Blank";
        }
        else
        {
            if (mNic.length() < 10 )
            {
                Validate = false;
                ErrorDescription="Client Nic no Format Invalid.";
            }
            else
            {
                if (mNic.length() == 10)
                {
                    String NicLast = mNic.getText().toString().substring(9,10);
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
                else if (mNic.length() != 12)
                {
                    Validate = false;
                    ErrorDescription="Client Nic no Format Invalid.";
                }
            }
        }

        if (mPhoneno.length() < 10)
        {
            Validate = false;
            ErrorDescription="Client PhoneNo Format Invalid..";
        }

        if (mExpRate.length() > 0)
        {
            if ( Double.parseDouble(mExpRate.getText().toString())> Double.parseDouble(mEtvRate) )
            {
                Validate = false;
                ErrorDescription="<ETV Rate> Cannot Grater than Product Rate -" + mEtvRate + " %";
            }
        }

        if (mFullyName.length() == 0)
        {
            Validate = false;
            ErrorDescription="Full Name is Blank.";
        }
        else
        if (mAdd1.length() == 0)
        {
            Validate = false;
            ErrorDescription="Adders 1 Name is Blank.";
        }
        else
        if (mAdd2.length() == 0)
        {
            Validate = false;
            ErrorDescription="Adders 2 Name is Blank.";
        }
        else
        if (mPhoneno.length() ==0)
        {
            Validate = false;
            ErrorDescription="Phone is Blank.";
        }
        else
        if (mInvoiceAmt.length() == 0)
        {
            Validate = false;
            ErrorDescription="Invoice Amount Zero.";
        }
        else
        if (mRate.length() == 0)
        {
            Validate = false;
            ErrorDescription="Rate Amount Zero.";
        }
        else
        if (mPerid.length() == 0)
        {
            Validate = false;
            ErrorDescription="Period Zero.";
        }
        else
        if (mEngNo.length() ==0)
        {
            Validate = false;
            ErrorDescription="Engine No is Blank.";
        }
        else
        if (mchassNo.length() == 0)
        {
            Validate = false;
            ErrorDescription="Chassis No is Blank.";
        }
        else if (mNatureIncome.getSelectedItem().equals(""))
        {
            Validate = false;
            ErrorDescription="Income Type No is Blank.";
        }
        else if (mYear.length() == 0)
        {
            Validate = false;
            ErrorDescription="Year is Blank.";
        }
        else if (mYear.length() < 4)
        {
            Validate = false;
            ErrorDescription="Year format invalid.";
        }

        //=== Check Model Wise LTV
        //=====================================================
        mMakeCode=""; mMoelCode=""; mEqType_ltv="" ; mModelPararate = "0"; Reg_Age_catgery="";
        SQLiteDatabase sqLiteDatabase_getLtv = sqlliteCreateLeasing_CreateApplicartion.getReadableDatabase();
        Cursor cursor_getmake = sqLiteDatabase_getLtv.rawQuery("SELECT * FROM AP_MAST_MAKE WHERE MAKE_DESCR = '" + mMake.getSelectedItem().toString() + "'" , null);
        if (cursor_getmake.getCount() != 0)
        {
            cursor_getmake.moveToFirst();
            mMakeCode  = cursor_getmake.getString(0);

            Cursor cursor_model = sqLiteDatabase_getLtv.rawQuery("SELECT * FROM AP_MAST_MODEL WHERE MAKE_CODE = '" + mMakeCode + "' AND MODEL_DESC = '" + mModel.getSelectedItem().toString() + "'" , null );
            if (cursor_model.getCount() != 0)
            {
                cursor_model.moveToFirst();
                mMoelCode = cursor_model.getString(1);
            }
            cursor_model.close();
        }
        cursor_getmake.close();

        Log.d("MAKE" , mMakeCode);
        Log.d("MODEL" , mMoelCode);

        int NoOfManifacture=0;
        if (mEquNature.getSelectedItem().toString().equals("REGISTERED VEHICLE"))
        {
            //==== Get No of Year
            int mCurrentYear=0 , mYomYear=0;

            mCurrentYear = Integer.parseInt(LoginDate.substring(0,4));

            if (mYear.length() != 0)
            {
                mYomYear     = Integer.parseInt(mYear.getText().toString());
            }

            NoOfManifacture =  mCurrentYear - mYomYear;

            Log.d("Current" , String.valueOf(mCurrentYear));
            Log.d("YOM" , String.valueOf(mYomYear));

            Log.d("NoYear" , String.valueOf(NoOfManifacture));

            if (NoOfManifacture==0)
            {
                Reg_Age_catgery = "0";
            }
            else if (NoOfManifacture >= 1 && NoOfManifacture <= 3)
            {
                Reg_Age_catgery = "3";
            }
            else if (NoOfManifacture > 3)
            {
                Reg_Age_catgery="7";
            }



            //==== Comment by Bhagya - 11:46 PM 31/10/2020 - 7 year validate remove
            /*
            else if (NoOfManifacture > 3 && NoOfManifacture <= 7)
            {
                Reg_Age_catgery="7";
            }
            else
            {
                Reg_Age_catgery="Over";
            }
            */


            Log.d("AgeCatg" ,Reg_Age_catgery);
            Cursor cursor_get_rate = sqLiteDatabase_getLtv.rawQuery("SELECT * FROM AP_MODEL_LTV WHERE MAKE_CODE = '" + mMakeCode + "' AND MODEL_CODE = '" + mMoelCode + "' AND YEAR_AGE = '" + Reg_Age_catgery + "'" , null );
            if (cursor_get_rate.getCount() != 0)
            {
                cursor_get_rate.moveToFirst();
                mModelPararate = cursor_get_rate.getString(4);
            }
            cursor_get_rate.close();
        }
        else
        {
            Cursor cursor_getvalidat = sqLiteDatabase_getLtv.rawQuery("SELECT * FROM AP_MODEL_LTV WHERE MAKE_CODE = '" + mMakeCode + "' AND MODEL_CODE = '" + mMoelCode + "' AND YEAR_AGE = '0'" , null );
            if (cursor_getvalidat.getCount() != 0)
            {
                cursor_getvalidat.moveToFirst();
                mModelPararate = cursor_getvalidat.getString(4);
            }
            cursor_getvalidat.close();
        }

        Log.d("Para-rate" , mModelPararate);

        //=== Over 7 Years
        if (Reg_Age_catgery.equals("Over"))
        {
            Validate = false;
            ErrorDescription="Cannot Process this file. YOM cannot be grater than 7 Years.";
        }
        else
        {
            if (mModelPararate != "0")
            {
                Log.e("exp" , mExpRate.getText().toString() );
                Log.e("para" , mModelPararate );

                if (Double.parseDouble(mExpRate.getText().toString()) > Double.parseDouble(mModelPararate))
                {
                    Validate = false;
                    ErrorDescription="Cannot Process this file. LTV Rate Cannot be a grater than - " + mModelPararate + " To " + mMake.getSelectedItem().toString() + "-" + mModel.getSelectedItem().toString() ;
                }
            }

        }
        //=======================================

        if (Validate)
        {
            AlertDialog.Builder builderdelete = new AlertDialog.Builder(CreateApplication.this);
            builderdelete.setTitle("AFiMobile-Leasing");
            builderdelete.setMessage("Are you sure to Save this application?");
            builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    String NewApplicationNo="" ;

                    //=== Globle Varible Details..

                    SqlliteCreateLeasing mySqlCreateCalss = new SqlliteCreateLeasing(CreateApplication.this);
                    SQLiteDatabase sqLiteDatabase = mySqlCreateCalss.getWritableDatabase();

                    // Client Details Insert ===============
                    SimpleDateFormat curdate = new SimpleDateFormat("yyyy-mm-dd");
                    String NewDate = curdate.format(new Date());
                    Calendar now = Calendar.getInstance();


                    //=== Calculate Age to Client ======
                    Calendar Today = Calendar.getInstance();
                    int AgeClient=0;
                    AgeClient =  Today.get(Calendar.YEAR) - mBirYear;


                    //==== Application No Create
                    if (mInpType.equals("M"))
                    {
                        NewApplicationNo = mInpAppno;
                        sqlliteCreateLeasing_CreateApplicartion.DeleteAppData(NewApplicationNo);
                    }
                    else
                    {
                        Calendar calendar = Calendar.getInstance(Locale.getDefault());
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int second = calendar.get(Calendar.SECOND);
                        //NewApplicationNo = LoginBranch + now.get(Calendar.YEAR) + now.get(Calendar.MONTH) + "A" + mNic.getText().toString() + String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second)   ;

                        String Serial_no = now.get(Calendar.DATE) + String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second);
                        NewApplicationNo = LoginBranch + mNic.getText().toString() + "A" + String.format("%010d", Integer.parseInt(Serial_no))   ;
                        NewApplicationNo = NewApplicationNo.replace(" ", "");
                    }

                    mInputAppno.setText(NewApplicationNo);

                    //==== Get Delear And Main Supplier Details =====
                    String mDelaerEmail="" , mSupplierEmail="";

                    //Delear
                    String mDelearname      =   mDelear.getSelectedItem().toString();
                    String mSupplierName    =   mSupplier.getSelectedItem().toString();

                    Cursor cursordelear = sqLiteDatabase.rawQuery("Select EMAIL_ID from AP_MAST_OWNER where SHOWROOM_N = '" + mDelearname + "'" , null);
                    if (cursordelear.getCount() != 0)
                    {
                        cursordelear.moveToFirst();
                        mDelaerEmail = cursordelear.getString(0);
                    }
                    cursordelear.close();

                    //Supplier
                    Cursor cursorsupplier = sqLiteDatabase.rawQuery("Select EMAIL from AP_MAST_SUPPLIER where SUPPLIER_N = '" + mSupplierName + "'" , null);
                    if (cursorsupplier.getCount()!= 0)
                    {
                        cursorsupplier.moveToFirst();
                        mSupplierEmail = cursorsupplier.getString(0);
                    }
                    cursorsupplier.close();

                    //=============================================
                    // ===========  Clent Data Save ========
                    ContentValues LEDataInput = new ContentValues();
                    LEDataInput.put("APPLICATION_REF_NO" , NewApplicationNo);
                    LEDataInput.put("CL_NIC" , mNic.getText().toString());
                    LEDataInput.put("CL_TITLE" , mClSpnTitle.getSelectedItem().toString());
                    LEDataInput.put("CL_FULLY_NAME" , mFullyName.getText().toString());
                    LEDataInput.put("CL_ADDERS_1" , mAdd1.getText().toString());
                    LEDataInput.put("CL_ADDERS_2" , mAdd2.getText().toString());
                    LEDataInput.put("CL_ADDERS_3" , mAdd3.getText().toString());
                    LEDataInput.put("CL_ADDERS_4", mAdd4.getText().toString());
                    LEDataInput.put("CL_OCCUPATION" , mOcupation.getSelectedItem().toString());
                    LEDataInput.put("INCOME_SOURCE" , mNatureIncome.getSelectedItem().toString());
                    LEDataInput.put("CL_MOBILE_NO" , mPhoneno.getText().toString());
                    LEDataInput.put("CL_CREATE_DATE" , LoginDate);
                    LEDataInput.put("CL_CREATE_USER" , LoginUser);
                    LEDataInput.put("CL_DATE_OF_BIRTH" , mDateofbirth.getText().toString());
                    LEDataInput.put("CL_AGE" , Integer.toString(AgeClient));
                    LEDataInput.put("CL_EMARKS" , mClRemarks.getText().toString());
                    sqLiteDatabase.insert("LE_CLIENT_DATA" , null   ,LEDataInput);

                    //============  Application Data Save ====
                    ContentValues APDataInput = new ContentValues();
                    APDataInput.put("APPLICATION_REF_NO" , NewApplicationNo);
                    APDataInput.put("AP_PRODUCT",Mproduct.getSelectedItem().toString());
                    APDataInput.put("AP_INVOICE_AMT" , mInvoiceAmt.getText().toString());


                    if (mClconAmt.getText().equals(""))
                    {
                        APDataInput.put("AP_DOWN_PAY" , "0");
                    }
                    else
                    {
                        APDataInput.put("AP_DOWN_PAY" , mClconAmt.getText().toString());
                    }

                    APDataInput.put("AP_FACILITY_AMT" , mFacamt.getText().toString().replace("," , ""));
                    APDataInput.put("AP_ETV" , mExpRate.getText().toString());
                    APDataInput.put("AP_RATE" , mRate.getText().toString());
                    APDataInput.put("AP_PERIOD" , mPerid.getText().toString());
                    APDataInput.put("AP_RENTAL_AMT" , mRental.getText().toString().replace("," , ""));
                    APDataInput.put("AP_MK_OFFICER" , LoginUser);
                    APDataInput.put("AP_BRANCH" , LoginBranch);
                    APDataInput.put("AP_PO_STS" , "P");   //  POS SAVE STS
                    APDataInput.put("AP_PO_SEND_DELEAR" , mDelear.getSelectedItem().toString());
                    APDataInput.put("AP_PO_DELEAR_EMAIL" , mDelaerEmail);
                    APDataInput.put("AP_ENTDATE" , LoginDate);
                    APDataInput.put("AP_LAST_MOD_DATE" , LoginDate);
                    APDataInput.put("AP_STAGE" , "000");    // APPLICATION STS - 000 - Application Save
                    APDataInput.put("AP_PO_RQ_USER" , LoginUser);
                    APDataInput.put("AP_PO_RQ_DATE" , LoginDate);

                    if (mSupplier.getSelectedItem().toString().equals("INDIVIDUAL SUPPLIER-DIRECT"))
                    {
                        APDataInput.put("AP_MAIN_SUPPLIR" , mIndsuppName.getText().toString());
                        APDataInput.put("AP_MAIN_SUPPLIR_EMAIL" , mInsuuAdders.getText().toString());
                    }
                    else
                    {
                        APDataInput.put("AP_MAIN_SUPPLIR" , mSupplier.getSelectedItem().toString());
                        APDataInput.put("AP_MAIN_SUPPLIR_EMAIL" , mSupplierEmail);
                    }

                    sqLiteDatabase.insert("LE_APPLICATION" , null ,APDataInput );

                    //============  Asset Data Save ====

                    ContentValues EQDataInput = new ContentValues();
                    EQDataInput.put("APPLICATION_REF_NO" , NewApplicationNo);
                    EQDataInput.put("AS_EQ_TYPE" , mAssetT.getSelectedItem().toString());
                    EQDataInput.put("AS_EQ_CATAGE" , mEquNature.getSelectedItem().toString());
                    EQDataInput.put("AS_EQ_MAKE" , mMake.getSelectedItem().toString());
                    EQDataInput.put("AS_EQ_MODEL" , mModel.getSelectedItem().toString());
                    EQDataInput.put("AS_EQ_REGNO" , mRegNo.getText().toString());
                    EQDataInput.put("AS_EQ_ENG_NO" ,mEngNo.getText().toString());
                    EQDataInput.put("AS_EQ_CHAS_NO" ,mchassNo.getText().toString());
                    EQDataInput.put("AS_EQ_YEAR" , mYear.getText().toString());
                    EQDataInput.put("AS_INV_DELER" , mDelear.getSelectedItem().toString());
                    EQDataInput.put("AS_INV_SUPPLIER" ,mSupplier.getSelectedItem().toString() );
                    EQDataInput.put("AP_INTDU" ,mIntduser.getSelectedItem().toString() );
                    EQDataInput.put("AP_INV_DATE" , LoginDate);
                    EQDataInput.put("AP_INSURANCE_COMPANY" , mInsurace.getSelectedItem().toString());
                    EQDataInput.put("AP_ENT_DATE" , LoginDate);
                    EQDataInput.put("AP_MOD_DATE" , LoginDate);
                    EQDataInput.put("AP_ENT_USER" , LoginUser);
                    EQDataInput.put("AP_BRANCH" , LoginBranch);
                    EQDataInput.put("MK_VAL" , mMarketVal.getText().toString());
                    EQDataInput.put("M_IND_CODE",   mIntduser.getSelectedItem().toString().substring(mIntduser.getSelectedItem().toString().indexOf("-") + 1));
                    EQDataInput.put("M_SUP_CODE",   mSupplier.getSelectedItem().toString().substring(mSupplier.getSelectedItem().toString().indexOf("-") + 1));
                    EQDataInput.put("M_DELEAR_CODE",   mDelear.getSelectedItem().toString().substring(mDelear.getSelectedItem().toString().indexOf("-") + 1));

                    sqLiteDatabase.insert("LE_ASSET_DETAILS" , null , EQDataInput);

                    //==== Application Remarks
                    ContentValues contentValuesREMARKS = new ContentValues();
                    contentValuesREMARKS.put("APPNO" , NewApplicationNo);
                    contentValuesREMARKS.put("OFFICER_ID" , LoginUser);
                    contentValuesREMARKS.put("RE_DATE" , LoginDate);
                    contentValuesREMARKS.put("AP_REMARKS" , mMEremarks.getText().toString());
                    sqLiteDatabase.insert("APP_REMARKS" , null , contentValuesREMARKS);

                    //======== Charges Data input =======
                    ContentValues ChargeVal = new ContentValues();
                    ChargeVal.put("APPLICATION_REF_NO" , NewApplicationNo);
                    ChargeVal.put("OT_CHARGE_NAME" , "IND CHARGE");
                    ChargeVal.put("OT_CHARGE_AMT" , mIndAmt.getText().toString());
                    ChargeVal.put("OT_CHARGE_TYPE" , mIndCap.isChecked());
                    sqLiteDatabase.insert("LE_CHARGS" , null , ChargeVal);

                    ChargeVal.put("APPLICATION_REF_NO" , NewApplicationNo);
                    ChargeVal.put("OT_CHARGE_NAME" , "SERVICE CHARGE");
                    ChargeVal.put("OT_CHARGE_AMT" ,mServiceAmt.getText().toString() );
                    ChargeVal.put("OT_CHARGE_TYPE" , mSchCap.isChecked());
                    sqLiteDatabase.insert("LE_CHARGS" , null , ChargeVal);

                    ChargeVal.put("APPLICATION_REF_NO" , NewApplicationNo);
                    ChargeVal.put("OT_CHARGE_NAME" , "RMV CHARGE");
                    ChargeVal.put("OT_CHARGE_AMT" ,mRmvAmt.getText().toString() );
                    ChargeVal.put("OT_CHARGE_TYPE" , mRMVCap.isChecked());
                    sqLiteDatabase.insert("LE_CHARGS" , null , ChargeVal);

                    ChargeVal.put("APPLICATION_REF_NO" , NewApplicationNo);
                    ChargeVal.put("OT_CHARGE_NAME" , "INSURANCE CHARGE");
                    ChargeVal.put("OT_CHARGE_AMT" ,mInsAmt.getText().toString() );
                    ChargeVal.put("OT_CHARGE_TYPE" , mInsCap.isChecked());
                    sqLiteDatabase.insert("LE_CHARGS" , null , ChargeVal);

                    ChargeVal.put("APPLICATION_REF_NO" , NewApplicationNo);
                    ChargeVal.put("OT_CHARGE_NAME" , "OTHER CHARGE");
                    ChargeVal.put("OT_CHARGE_AMT" ,mOthAmt.getText().toString() );
                    ChargeVal.put("OT_CHARGE_TYPE" , mOthCha.isChecked());
                    sqLiteDatabase.insert("LE_CHARGS" , null , ChargeVal);

                    ChargeVal.put("APPLICATION_REF_NO" , NewApplicationNo);
                    ChargeVal.put("OT_CHARGE_NAME" , "TRANSPORT CHARGES");
                    ChargeVal.put("OT_CHARGE_AMT" ,mTrpAmt.getText().toString() );
                    ChargeVal.put("OT_CHARGE_TYPE" , mTransport.isChecked());
                    sqLiteDatabase.insert("LE_CHARGS" , null , ChargeVal);

                    ChargeVal.put("APPLICATION_REF_NO" , NewApplicationNo);
                    ChargeVal.put("OT_CHARGE_NAME" , "STAMP CHARGES");
                    ChargeVal.put("OT_CHARGE_AMT" ,mStamDuty.getText().toString() );
                    ChargeVal.put("OT_CHARGE_TYPE" , mStdCap.isChecked());
                    sqLiteDatabase.insert("LE_CHARGS" , null , ChargeVal);

                    //=== Update Draft Details
                    if (mInpType.equals("D"))
                    {
                        ContentValues contentValues_draft_update = new ContentValues();
                        contentValues_draft_update.put("APP_REF_NO" , NewApplicationNo);
                        contentValues_draft_update.put("UPD_FLG" , "D");
                        sqLiteDatabase.update("DRAFT_APPLICATION" ,contentValues_draft_update ,"NIC = ?", new String[]{String.valueOf(mDraft_Nic)});
                    }

                    String Msg = "You have succesfully Saved. To " + mNic.getText().toString() + " - " + mFullyName.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateApplication.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage(Msg);
                    builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();

                    mFullyName.setText(""); mNic.setText(""); mAdd1.setText(""); mAdd2.setText("");
                    mAdd3.setText(""); mAdd4.setText(""); mPhoneno.setText("");
                    mInvoiceAmt.setText(""); mExpRate.setText(""); mRate.setText(""); mPerid.setText("");
                    mIndAmt.setText(""); mServiceAmt.setText(""); mRmvAmt.setText("");
                    mInsAmt.setText(""); mOthAmt.setText(""); mTrpAmt.setText("");
                    mYear.setText(""); mRegNo.setText(""); mEngNo.setText(""); mchassNo.setText("");
                    mClconAmt.setText(""); mFacamt.setText(""); mUpforntAmt.setText("");
                    mRental.setText(""); mCaplizedAmnt.setText(""); mMarketVal.setText(""); mMEremarks.setText("");

                    mIndCap.setChecked(false);
                    mSchCap.setChecked(false);
                    mRMVCap.setChecked(false);
                    mOthCha.setChecked(false);
                    mInsCap.setChecked(false);
                    mTransport.setChecked(false);
                    mSaveData.setEnabled(false);


                    mSaveData.setBackgroundResource(R.drawable.normalbuttondisable);


                    sqLiteDatabase.close();
                    mySqlCreateCalss.close();
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

    public void LoadModifyData()
    {
        Cursor mClientData = sqLiteDatabase_Create_application.rawQuery("Select * from LE_CLIENT_DATA where APPLICATION_REF_NO = '" + mInpAppno + "'" , null);
        if (mClientData.getCount() != 0)
        {
            mClientData.moveToFirst();
            mNic.setText(mClientData.getString(1));
            mClSpnTitle.setSelection(arrayTITLE.getPosition(mClientData.getString(2)));
            mFullyName.setText(mClientData.getString(3));
            mAdd1.setText(mClientData.getString(4));
            mAdd2.setText(mClientData.getString(5));
            mAdd3.setText(mClientData.getString(6));
            mAdd4.setText(mClientData.getString(7));
            mPhoneno.setText(mClientData.getString(14));
            mClRemarks.setText(mClientData.getString(38));
            mOcupation.setSelection(arrayAdapter_occupa.getPosition(mClientData.getString(17)));
            mNatureIncome.setSelection(arrayAdapter_occtype.getPosition(mClientData.getString(24)));
            mDateofbirth.setText(mClientData.getString(12));
        }
        mClientData.close();

        Cursor ReadDataApplicaion = sqLiteDatabase_Create_application.rawQuery("Select * from LE_APPLICATION where APPLICATION_REF_NO = '" +
                mInpAppno + "' and AP_STAGE = '000'" , null);
        if (ReadDataApplicaion.getCount() !=0)
        {
            ReadDataApplicaion.moveToFirst();
            mInvoiceAmt.setText(ReadDataApplicaion.getString(2));
            mClconAmt.setText(ReadDataApplicaion.getString(3));
            mFacamt.setText(ReadDataApplicaion.getString(4));
            mExpRate.setText(ReadDataApplicaion.getString(5));
            mRate.setText(ReadDataApplicaion.getString(6));
            mPerid.setText(ReadDataApplicaion.getString(7));
            mRental.setText(ReadDataApplicaion.getString(8));
            Mproduct.setSelection(arrayAdapter_product_con.getPosition(ReadDataApplicaion.getString(1)));
        }
        ReadDataApplicaion.close();

        //=== Asset Data ====
        Cursor ReadDataAsset = sqLiteDatabase_Create_application.rawQuery("Select * from LE_ASSET_DETAILS where APPLICATION_REF_NO = '" + mInpAppno + "'" , null);
        ReadDataAsset.moveToFirst();
        if (ReadDataAsset.getCount() != 0)
        {
            mYear.setText(ReadDataAsset.getString(8));
            mMarketVal.setText(ReadDataAsset.getString(22));
            mRegNo.setText(ReadDataAsset.getString(5));
            mEngNo.setText(ReadDataAsset.getString(6));
            mchassNo.setText(ReadDataAsset.getString(7));
            mMake.setSelection(arrayAdapterMAKE.getPosition(ReadDataAsset.getString(3)));

            String MakeCode = mMake.getSelectedItem().toString();
            SqlliteCreateLeasing db = new SqlliteCreateLeasing(this);
            List<String> labels = db.getAllModelSqllite(MakeCode);

            if (labels != null) {
                arrayAdapterMODEL_modify = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
                arrayAdapterMODEL_modify.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mModel.setAdapter(arrayAdapterMODEL_modify);
            }

            Log.e("model" , ReadDataAsset.getString(4));

            mModel.setSelection(arrayAdapterMODEL_modify.getPosition(ReadDataAsset.getString(4)));

            //mModel.setSelection(arrayAdapterMODEL.getPosition(ReadDataAsset.getString(4)));
            mSupplier.setSelection(arrayAdaptersupplier.getPosition(ReadDataAsset.getString(11)));
            mDelear.setSelection(arrayAdapterdelear.getPosition(ReadDataAsset.getString(10)));
            mInsurace.setSelection(arrayAdapterins .getPosition(ReadDataAsset.getString(16)));
            mAssetT.setSelection(arrayAdaptereqtype.getPosition(ReadDataAsset.getString(1)));
            mEquNature.setSelection(arrayAdaptereqnature.getPosition(ReadDataAsset.getString(2)));
            mIntduser.setSelection(arrayAdapterintduser.getPosition(ReadDataAsset.getString(13)));
        }
        ReadDataAsset.close();

        //=== Load Remarks Details
        Cursor cursorRMARKS = sqLiteDatabase_Create_application.rawQuery("SELECT * FROM APP_REMARKS WHERE APPNO = '" +  mInpAppno  + "'" , null);
        if (cursorRMARKS.getCount() != 0)
        {
            cursorRMARKS.moveToFirst();
            mMEremarks.setText(cursorRMARKS.getString(3));
        }
        cursorRMARKS.close();

        Cursor ReadDataCharge = sqLiteDatabase_Create_application.rawQuery("Select * from LE_CHARGS where APPLICATION_REF_NO = '" + mInpAppno + "' and OT_CHARGE_NAME = 'IND CHARGE'" , null);
        ReadDataCharge.moveToFirst();
        if (ReadDataCharge.getCount() != 0)
        {
            mIndAmt.setText(ReadDataCharge.getString(2));

            if (ReadDataCharge.getString(3).equals("1"))
            {
                mIndCap.setChecked(true);
            }
            else
                mIndCap.setChecked(false);
        }
        ReadDataCharge.close();

        Cursor ReadDataChargeSch = sqLiteDatabase_Create_application.rawQuery("Select * from LE_CHARGS where APPLICATION_REF_NO = '" + mInpAppno + "' and OT_CHARGE_NAME = 'SERVICE CHARGE'" , null);
        ReadDataChargeSch.moveToFirst();
        if (ReadDataChargeSch.getCount() != 0)
        {
            mServiceAmt.setText(ReadDataChargeSch.getString(2));

            if (ReadDataChargeSch.getString(3).equals("1"))
            {
                mSchCap.setChecked(true);
            }
            else
                mSchCap.setChecked(false);
        }
        ReadDataChargeSch.close();

        Cursor ReadDataChargeRmv = sqLiteDatabase_Create_application.rawQuery("Select * from LE_CHARGS where APPLICATION_REF_NO = '" + mInpAppno + "' and OT_CHARGE_NAME = 'RMV CHARGE'" , null);
        ReadDataChargeRmv.moveToFirst();
        if (ReadDataChargeRmv.getCount() != 0)
        {
            mRmvAmt.setText(ReadDataChargeRmv.getString(2));

            if (ReadDataChargeRmv.getString(3).equals("1"))
            {
                mRMVCap.setChecked(true);
            }
            else
                mRMVCap.setChecked(false);
        }
        ReadDataChargeRmv.close();

        Cursor ReadDataChargeIns = sqLiteDatabase_Create_application.rawQuery("Select * from LE_CHARGS where APPLICATION_REF_NO = '" + mInpAppno + "' and OT_CHARGE_NAME = 'INSURANCE CHARGE'" , null);
        ReadDataChargeIns.moveToFirst();
        if (ReadDataChargeIns.getCount() != 0)
        {
            mInsAmt.setText(ReadDataChargeIns.getString(2));

            if (ReadDataChargeIns.getString(3).equals("1"))
            {
                mInsCap.setChecked(true);
            }
            else
                mInsCap.setChecked(false);
        }
        ReadDataChargeIns.close();

        Cursor ReadDataChargeTra = sqLiteDatabase_Create_application.rawQuery("Select * from LE_CHARGS where APPLICATION_REF_NO = '" + mInpAppno + "' and OT_CHARGE_NAME = 'TRANSPORT CHARGES'" , null);
        ReadDataChargeTra.moveToFirst();
        if (ReadDataChargeTra.getCount() != 0)
        {
            mTrpAmt.setText(ReadDataChargeTra.getString(2));

            if (ReadDataChargeTra.getString(3).equals("1"))
            {
                mTransport.setChecked(true);
            }
            else
                mTransport.setChecked(false);
        }
        ReadDataChargeTra.close();

        Cursor ReadDataChargeOth = sqLiteDatabase_Create_application.rawQuery("Select * from LE_CHARGS where APPLICATION_REF_NO = '" + mInpAppno + "' and OT_CHARGE_NAME = 'OTHER CHARGE'" , null);
        ReadDataChargeOth.moveToFirst();
        if (ReadDataChargeOth.getCount() != 0)
        {
            mOthAmt.setText(ReadDataChargeOth.getString(2));

            if (ReadDataChargeOth.getString(3).equals("1"))
            {
                mOthCha.setChecked(true);
            }
            else
                mOthCha.setChecked(false);
        }
        ReadDataChargeOth.close();
        RentalCalculate();
    }
}
