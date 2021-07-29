package com.AFiMOBILE.afslmobileapplication;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ApplcationSubmitMenu extends AppCompatActivity {

    //============ Define Varible ============================
    private EditText mProduct    , mMkVal , mInvAmt  , mClCont , Mfacamt , mExp , mRate
            , mPeriod , mIndAmt , mSchamt , mRmvAmt , mInsAmt , mTapAmt , mOthAmt , mLeasingAmt , mCapAmt , mUpforntamt ,
                mRentalAmt , mAsstype , mMake , mModel , mRegNo , mYear , mEngNo , mChassNo , mSuuname , mDelear , Minscom;
    private EditText   mIndCap , mSchCap , mRmvCap , mInsCap , mTrpCap , mOthCap ;
    private TextView mInpapplicationNo , mFullyName , mAdders , mPhoneno;
    public String mInpAppno="" ,PHP_URL_SQL="";
    public Double mCapTot , mUpfronttot;
    private Button BtnClcreate , BtnCoAppCreate , mDOCUPLOAD , mFilecomplete;
    public boolean ClientCheck , GurCheck , mDocValidate , mDocValidateClient;
    public String ErroDescription="" , mDcoType;
    public String LoginUser="" , LoginDate = "" , LoginBranch= "";
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public JsonDataTranferToLive jsonDataTranferToLive;
    private RequestQueue mQueue;
    public SqlliteCreateLeasing sqlliteCreateLeasing_AppMenu;
    public SQLiteDatabase sqLiteDatabase_AppMenu;
    public static Toolbar toolbar;
    public TextView ConnectionSts;
    public Handler handler;
    public ProgressDialog progressDialog;
    //=========================================================
    SqlliteCreateLeasing sqlliteCreateLeasing = new SqlliteCreateLeasing(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applcation_submit_menu);

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));

        //=== Database Connection
        sqlliteCreateLeasing_AppMenu = new SqlliteCreateLeasing(this);
        sqLiteDatabase_AppMenu = sqlliteCreateLeasing_AppMenu.getReadableDatabase();

        mFullyName      =   (TextView)findViewById(R.id.txtFULLYNAME) ;
        mAdders         =   (TextView)findViewById(R.id.txtADDERS) ;
        mPhoneno        =   (TextView)findViewById(R.id.txtphoneno) ;
        mProduct        =   (EditText)findViewById(R.id.txtPRODUCT) ;
        mMkVal          =   (EditText)findViewById(R.id.txtMKVAL) ;
        mInvAmt         =   (EditText)findViewById(R.id.txtINVAMT) ;
        mClCont         =   (EditText)findViewById(R.id.txtCLIENTCON) ;
        Mfacamt         =   (EditText)findViewById(R.id.txtFACAMT) ;
        mExp            =   (EditText)findViewById(R.id.txtEXP) ;
        mRate           =   (EditText)findViewById(R.id.txtRATE) ;
        mPeriod         =   (EditText)findViewById(R.id.txtPERIOD) ;
        mIndAmt         =   (EditText)findViewById(R.id.txtINDAMT) ;
        mSchamt         =   (EditText)findViewById(R.id.txtSCHAMT) ;
        mRmvAmt         =   (EditText)findViewById(R.id.txtRMVAMT) ;
        mInsAmt         =   (EditText)findViewById(R.id.txtINSAMT) ;
        mTapAmt         =   (EditText)findViewById(R.id.txtTRAAMT) ;
        mOthAmt         =   (EditText)findViewById(R.id.txtOTHAMT) ;
        mLeasingAmt     =   (EditText)findViewById(R.id.txtFINACEAMT) ;
        mCapAmt         =   (EditText)findViewById(R.id.txtCAPAMT) ;
        mUpforntamt     =   (EditText)findViewById(R.id.txtUPFORNT) ;
        mRentalAmt      =   (EditText)findViewById(R.id.txtRENTAL) ;
        mAsstype        =   (EditText)findViewById(R.id.txtASTYPE) ;
        mMake           =   (EditText)findViewById(R.id.txtMAKE) ;
        mModel          =   (EditText)findViewById(R.id.txtMODEL) ;
        mRegNo          =   (EditText)findViewById(R.id.txtREGNO) ;
        mYear           =   (EditText)findViewById(R.id.txtYEAR) ;
        mEngNo          =   (EditText)findViewById(R.id.txtERGNO) ;
        mChassNo        =   (EditText)findViewById(R.id.txtCHANO) ;
        mSuuname        =   (EditText)findViewById(R.id.txtSUPPLIER) ;
        mDelear         =   (EditText)findViewById(R.id.txtDELAER) ;
        Minscom         =   (EditText)findViewById(R.id.txtINSCOM) ;

        mIndCap         =   (EditText)findViewById(R.id.txtINDAMTCAP) ;
        mSchCap         =   (EditText)findViewById(R.id.txtSCHCAP) ;
        mRmvCap         =   (EditText)findViewById(R.id.txtRMVCAP) ;
        mInsCap         =   (EditText)findViewById(R.id.txtINSCAP) ;
        mTrpCap         =   (EditText)findViewById(R.id.txtTRACAP) ;
        mOthCap         =   (EditText)findViewById(R.id.txtOTHCAP) ;
        mProduct        =   (EditText)findViewById(R.id.txtPRODUCT);

        mInpapplicationNo   = (TextView) findViewById(R.id.txtAPPNO);
        BtnClcreate         = (Button) findViewById(R.id.btnclcreate);
        BtnCoAppCreate      =  (Button)findViewById(R.id.btnguradd);
        mFilecomplete       =  (Button)findViewById(R.id.btncomplete);

        mDOCUPLOAD          =   (Button)findViewById(R.id.btnDocupload);

        mProduct.setEnabled(false);
        mFullyName.setEnabled(false);

        mFullyName.setEnabled(false);           mAdders.setEnabled(false);
        mPhoneno.setEnabled(false);             mProduct.setEnabled(false);
        mMkVal.setEnabled(false);               mInvAmt.setEnabled(false);
        mClCont.setEnabled(false);              Mfacamt.setEnabled(false);
        mExp.setEnabled(false);                 mRate.setEnabled(false);
        mPeriod.setEnabled(false);              mIndAmt.setEnabled(false);
        mSchamt.setEnabled(false);              mRmvAmt.setEnabled(false);
        mInsAmt.setEnabled(false);              mTapAmt.setEnabled(false);
        mOthAmt.setEnabled(false);              mLeasingAmt.setEnabled(false);
        mCapAmt.setEnabled(false);              mUpforntamt.setEnabled(false);
        mRentalAmt.setEnabled(false);           mAsstype.setEnabled(false);
        mMake.setEnabled(false);                mModel.setEnabled(false);
        mRegNo.setEnabled(false);               mYear.setEnabled(false);
        mEngNo.setEnabled(false);               mChassNo.setEnabled(false);
        mSuuname.setEnabled(false);             mDelear.setEnabled(false);
        Minscom.setEnabled(false);              mIndCap.setEnabled(false);
        mSchCap.setEnabled(false);              mRmvCap.setEnabled(false);
        mInsCap.setEnabled(false);              mTrpCap.setEnabled(false);
        mOthCap.setEnabled(false);              mProduct.setEnabled(false);


        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        checkDataConnectionStatus = new CheckDataConnectionStatus(this);
        //===== Create Volly Request Ques. =======
        mQueue   = VollySingleton.getInstance(this).getRequestQueue();

        //=== Get Input Application No
        final Intent intent = getIntent();
        mInpAppno           =   intent.getStringExtra("ApplicationNo");
        mInpapplicationNo.setText(mInpAppno);
        jsonDataTranferToLive = new JsonDataTranferToLive(this);

        progressDialog = new ProgressDialog(ApplcationSubmitMenu.this);
        progressDialog.setTitle("AFi Mobile");
        progressDialog.setMessage("Application Submit , please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);


        //=== Check Data Connection status Toolbra every 5 second.
        ConnectionSts = (TextView)findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();

        //====== Submit Complete Applicatiion ====
        mFilecomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                ValidateComplete();

                Log.e("client" , String.valueOf(ClientCheck));
                Log.e("gur" , String.valueOf(GurCheck));
                Log.e("gurdoc" , String.valueOf(mDocValidate));
                Log.e("clidoc" , String.valueOf(mDocValidateClient));



                if (ClientCheck==true && GurCheck==true && mDocValidate==true && mDocValidateClient==true)
                {
                    boolean CksSts = checkDataConnectionStatus.IsConnected();
                    if (CksSts==true)
                    {
                        AlertDialog.Builder builderdelete = new AlertDialog.Builder(ApplcationSubmitMenu.this);
                        builderdelete.setTitle("AFiMobile-Leasing");
                        builderdelete.setMessage("Are you sure to Complete Application ?");
                        builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Volly_FileComplete volly_fileComplete = new Volly_FileComplete(ApplcationSubmitMenu.this);
                                volly_fileComplete.SendFileComplete(mInpAppno);

                                BtnClcreate.setEnabled(false);
                                BtnCoAppCreate.setEnabled(false);
                                mFilecomplete.setEnabled(false);
                                mDOCUPLOAD.setEnabled(false);

                                BtnClcreate.setBackgroundResource(R.drawable.normalbuttondisable);
                                BtnCoAppCreate.setBackgroundResource(R.drawable.normalbuttondisable);
                                mFilecomplete.setBackgroundResource(R.drawable.normalbuttondisable);
                                mDOCUPLOAD.setBackgroundResource(R.drawable.normalbuttondisable);

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplcationSubmitMenu.this);
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
                else
                {
                   AlertDialog.Builder builder = new AlertDialog.Builder(ApplcationSubmitMenu.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage(ErroDescription);
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


        //===== Client Create Screen =====
        BtnClcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.ClientDetailsAdd");
                intent.putExtra("ApplicationNo" ,mInpapplicationNo.getText());
                startActivity(intent);
            }
        });


        //===== Core Client Create Screen =====
        BtnCoAppCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1Coapplicato = new Intent("android.intent.action.ClientGurnterDetails");
                intent1Coapplicato.putExtra("ApplicationNo" ,mInpapplicationNo.getText());
                startActivity(intent1Coapplicato);
            }
        });

        //====== Document Upload ===
        mDOCUPLOAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentdocupload = new Intent("android.intent.action.DocumentUpload");
                intentdocupload.putExtra("ApplicationNo" ,mInpapplicationNo.getText());
                intentdocupload.putExtra("AppStage" , "002");
                startActivity(intentdocupload);
            }
        });
        GetMainSaveData();
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

    public void CheckDataComplete()
    {
        //=============== Client Date Validate Check ==============
        Cursor cursor_client_check = sqLiteDatabase_AppMenu.rawQuery("SELECT CL_AGE FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO ='" + mInpapplicationNo.getText() + "'" , null );
        if (cursor_client_check.getCount() !=0)
        {
            cursor_client_check.moveToFirst();

            if (cursor_client_check.getString(0) != "")
            {
               // BtnClcreate.setBackgroundResource(R.drawable.normalcompletebutton);
            }
            else
            {
               // BtnClcreate.setBackgroundResource(R.drawable.normalbutton);
            }
        }
        cursor_client_check.close();

        //================ Check Gur Validation ======================
        Cursor cursor_gur_check = sqLiteDatabase_AppMenu.rawQuery("SELECT * FROM LE_CO_CL_DATA WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'", null);
        if (cursor_gur_check.getCount() != 0)
        {
            //BtnCoAppCreate.setBackgroundResource(R.drawable.normalcompletebutton);
        }
        else
        {
            //BtnCoAppCreate.setBackgroundResource(R.drawable.normalbutton);
        }
        cursor_gur_check.close();

        //=== check Image Data
        String ProductCode="" , EqCagery="";
        mDocValidate=true;
        mDcoType="";
        Cursor cursor_doc_procde = sqLiteDatabase_AppMenu.rawQuery("SELECT LE_APPLICATION.AP_PRODUCT , LE_ASSET_DETAILS.AS_EQ_CATAGE FROM LE_APPLICATION " +
                "INNER JOIN LE_ASSET_DETAILS ON LE_ASSET_DETAILS.APPLICATION_REF_NO = LE_APPLICATION.APPLICATION_REF_NO WHERE LE_APPLICATION.APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'" , null);
        if (cursor_doc_procde.getCount() != 0)
        {
            cursor_doc_procde.moveToFirst();
            ProductCode = cursor_doc_procde.getString(0);
            EqCagery    = cursor_doc_procde.getString(1);

            Cursor cursor_doc_type = sqLiteDatabase_AppMenu.rawQuery("SELECT DOC_NAME FROM AP_MAST_DOC_TYPE WHERE PRODUCT_CODE = '" + ProductCode + "' AND " +
                    "EQ_TYPE = '" + EqCagery + "' and MAN_STAGE = '002' and MAY_TYPE = 'Y'" , null);
            if (cursor_doc_type.getCount() != 0)
            {
                cursor_doc_type.moveToFirst();
                do{

                    Cursor cursor_gur_data_check = sqLiteDatabase_AppMenu.rawQuery("SELECT CO_NIC FROM LE_CO_CL_DATA WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'" , null );
                    if (cursor_gur_data_check.getCount() != 0)
                    {
                        cursor_gur_data_check.moveToFirst();
                        do
                        {
                            Cursor cursor_image_check = sqLiteDatabase_AppMenu.rawQuery("SELECT DOC_STS FROM AP_DOC_IMAGE WHERE DOC_STS = '" + cursor_doc_type.getString(0) + "' and trim (CL_NIC) = '" + cursor_gur_data_check.getString(0) + "'"  , null );
                            if (cursor_image_check.getCount() == 0)
                            {
                                mDocValidate=false;
                                mDcoType = mDcoType + cursor_doc_type.getString(0) + ",";
                            }
                            cursor_image_check.close();

                        }while (cursor_gur_data_check.moveToNext());
                    }
                }while (cursor_doc_type.moveToNext());
            }
            cursor_doc_type.close();
            ErroDescription="Submit Application Document Not Complete. - " +  mDcoType;
        }

        if (mDocValidate==true)
        {
           // mDOCUPLOAD.setBackgroundResource(R.drawable.normalcompletebutton);
        }
        else
        {
           // mDOCUPLOAD.setBackgroundResource(R.drawable.normalbutton);
        }
        cursor_doc_procde.close();
    }

    protected void onStart()
    {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        BtnClcreate.setEnabled(true);
        BtnCoAppCreate.setEnabled(true);
        mFilecomplete.setEnabled(true);
        mDOCUPLOAD.setEnabled(true);

        //==== check Button complete
        CheckDataComplete();
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        sqLiteDatabase_AppMenu.close();
        sqlliteCreateLeasing.close();
        sqlliteCreateLeasing_AppMenu.close();
        Log.d("Log", "onDestroy-Application");

        super.onDestroy();
    }

    //===== to check Validatin to file complete.
    public void ValidateComplete()
    {
        ClientCheck  = true;
        GurCheck     = true;
        mDocValidate = true;
        mDocValidateClient = true;

        //=============== Client Date Validate Check ==============
        Cursor cursor_client_check = sqLiteDatabase_AppMenu.rawQuery("SELECT CL_AGE FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO ='" + mInpapplicationNo.getText() + "'" , null );
        if (cursor_client_check.getCount() !=0)
        {
            cursor_client_check.moveToFirst();
            if (cursor_client_check.getString(0) == null)
            {
                ClientCheck = false;
                ErroDescription="Client Data Not Complete..";
            }
        }
        else
        {
            ClientCheck = false;
        }
        cursor_client_check.close();
        //============================================================



        if (ClientCheck == true)
        {
            //================ Check Gur Validation ======================
            Cursor cursor_gur_check = sqLiteDatabase_AppMenu.rawQuery("SELECT * FROM LE_CO_CL_DATA WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'", null);
            if (cursor_gur_check.getCount() == 0) {
                GurCheck = false;
                ErroDescription = "Co-Applicant Details Not Updated.";
            }
            cursor_gur_check.close();
        }

        //=== Client Validate Check ======
        if (ClientCheck == true && GurCheck==true)
        {
            String ProductCode="" , EqCagery="";
            mDocValidateClient=true;
            mDcoType="";
            Cursor cursor_doc_procde = sqLiteDatabase_AppMenu.rawQuery("SELECT LE_APPLICATION.AP_PRODUCT , LE_ASSET_DETAILS.AS_EQ_CATAGE FROM LE_APPLICATION " +
                    "INNER JOIN LE_ASSET_DETAILS ON LE_ASSET_DETAILS.APPLICATION_REF_NO = LE_APPLICATION.APPLICATION_REF_NO WHERE LE_APPLICATION.APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'" , null);
            if (cursor_doc_procde.getCount() != 0)
            {
                cursor_doc_procde.moveToFirst();
                ProductCode = cursor_doc_procde.getString(0);
                EqCagery    = cursor_doc_procde.getString(1);

                Cursor cursor_doc_type = sqLiteDatabase_AppMenu.rawQuery("SELECT DOC_NAME FROM AP_MAST_DOC_TYPE WHERE PRODUCT_CODE = '" + ProductCode + "' AND " +
                        "EQ_TYPE = '" + EqCagery + "' and MAN_STAGE = '002' and MAY_TYPE = 'Y' AND HOLDER_TYPE = 'CLI'" , null);
                if (cursor_doc_type.getCount() != 0)
                {
                    cursor_doc_type.moveToFirst();
                    do{

                        Cursor cursor_gur_data_check = sqLiteDatabase_AppMenu.rawQuery("SELECT CL_NIC FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'" , null );
                        if (cursor_gur_data_check.getCount() != 0)
                        {
                            cursor_gur_data_check.moveToFirst();
                            do
                            {
                                Cursor cursor_image_check = sqLiteDatabase_AppMenu.rawQuery("SELECT DOC_STS FROM AP_DOC_IMAGE WHERE DOC_STS = '" + cursor_doc_type.getString(0) + "' and trim (CL_NIC) = '" + cursor_gur_data_check.getString(0) + "'"  , null );
                                if (cursor_image_check.getCount() == 0)
                                {
                                    Log.e("doc name" , cursor_doc_type.getString(0));
                                    mDocValidateClient=false;
                                    mDcoType = mDcoType + cursor_doc_type.getString(0) + ",";
                                }
                                cursor_image_check.close();

                            }while (cursor_gur_data_check.moveToNext());
                        }
                    }while (cursor_doc_type.moveToNext());
                }
                cursor_doc_type.close();
                ErroDescription="Client Submit Application Document Not Complete - " + mDcoType;
            }
            cursor_doc_procde.close();
        }

        if (ClientCheck == true && GurCheck==true && mDocValidateClient==true)
        {
            String ProductCode="" , EqCagery="";
            mDocValidate=true;
            mDcoType="";
            Cursor cursor_doc_procde = sqLiteDatabase_AppMenu.rawQuery("SELECT LE_APPLICATION.AP_PRODUCT , LE_ASSET_DETAILS.AS_EQ_CATAGE FROM LE_APPLICATION " +
                    "INNER JOIN LE_ASSET_DETAILS ON LE_ASSET_DETAILS.APPLICATION_REF_NO = LE_APPLICATION.APPLICATION_REF_NO WHERE LE_APPLICATION.APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'" , null);
            if (cursor_doc_procde.getCount() != 0)
            {
                cursor_doc_procde.moveToFirst();
                ProductCode = cursor_doc_procde.getString(0);
                EqCagery    = cursor_doc_procde.getString(1);

                Cursor cursor_doc_type = sqLiteDatabase_AppMenu.rawQuery("SELECT DOC_NAME FROM AP_MAST_DOC_TYPE WHERE PRODUCT_CODE = '" + ProductCode + "' AND " +
                        "EQ_TYPE = '" + EqCagery + "' and MAN_STAGE = '002' and MAY_TYPE = 'Y' AND HOLDER_TYPE = 'GUR'" , null);
                if (cursor_doc_type.getCount() != 0)
                {
                    cursor_doc_type.moveToFirst();
                    do{

                        Cursor cursor_gur_data_check = sqLiteDatabase_AppMenu.rawQuery("SELECT CO_NIC FROM LE_CO_CL_DATA WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'" , null );
                        if (cursor_gur_data_check.getCount() != 0)
                        {
                            cursor_gur_data_check.moveToFirst();
                            do
                            {
                                Cursor cursor_image_check = sqLiteDatabase_AppMenu.rawQuery("SELECT DOC_STS FROM AP_DOC_IMAGE WHERE DOC_STS = '" + cursor_doc_type.getString(0) + "' and trim (CL_NIC) = '" + cursor_gur_data_check.getString(0) + "'"  , null );
                                if (cursor_image_check.getCount() == 0)
                                {
                                    Log.e("doc name" , cursor_doc_type.getString(0));
                                    mDocValidate=false;
                                    mDcoType = mDcoType + cursor_doc_type.getString(0) + ",";
                                }
                                cursor_image_check.close();

                            }while (cursor_gur_data_check.moveToNext());
                        }
                    }while (cursor_doc_type.moveToNext());
                }
                cursor_doc_type.close();
                ErroDescription="Co-Applicant Submit Application Document Not Complete - " + mDcoType;
            }
            cursor_doc_procde.close();
        }
    }

    //==== To Complete the data to send sqllite data in live db
    public void CompleteDataToLive() {
        //============== Send Client Data to live =======

        progressDialog.show();
        Cursor cursor_CLIENT = sqLiteDatabase_AppMenu.rawQuery("SELECT * FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'", null);
        if (cursor_CLIENT.getCount() != 0) {
            cursor_CLIENT.moveToFirst();
            JSONObject jsonObject_client = new JSONObject();
            try {
                jsonObject_client.put("APPLICATION_REF_NO", cursor_CLIENT.getString(0));
                jsonObject_client.put("CL_NIC", cursor_CLIENT.getString(1));
                jsonObject_client.put("CL_TITLE", cursor_CLIENT.getString(2));
                jsonObject_client.put("CL_FULLY_NAME", cursor_CLIENT.getString(3));
                jsonObject_client.put("CL_ADDERS_1", cursor_CLIENT.getString(4));
                jsonObject_client.put("CL_ADDERS_2", cursor_CLIENT.getString(5));
                jsonObject_client.put("CL_ADDERS_3", cursor_CLIENT.getString(6));
                jsonObject_client.put("CL_ADDERS_4", cursor_CLIENT.getString(7));
                jsonObject_client.put("CL_GENDER", cursor_CLIENT.getString(8));
                jsonObject_client.put("CL_MARITAL_STATUS", cursor_CLIENT.getString(9));
                jsonObject_client.put("CL_INITIALS", cursor_CLIENT.getString(10));
                jsonObject_client.put("CL_LASTNAME", cursor_CLIENT.getString(11));
                jsonObject_client.put("CL_DATE_OF_BIRTH", cursor_CLIENT.getString(12));
                jsonObject_client.put("CL_AGE", cursor_CLIENT.getString(13));
                jsonObject_client.put("CL_MOBILE_NO", cursor_CLIENT.getString(14));
                jsonObject_client.put("CL_LAND_NO", cursor_CLIENT.getString(15));
                jsonObject_client.put("CL_SECTOR", cursor_CLIENT.getString(16));
                jsonObject_client.put("CL_OCCUPATION", cursor_CLIENT.getString(17));
                jsonObject_client.put("CL_INCOME", cursor_CLIENT.getString(18));
                jsonObject_client.put("CL_CREATE_DATE", cursor_CLIENT.getString(19));
                jsonObject_client.put("CL_CREATE_USER", cursor_CLIENT.getString(20));
                jsonObject_client.put("CL_BRANCH", cursor_CLIENT.getString(20));
                jsonObject_client.put("SECTOR", cursor_CLIENT.getString(21));
                jsonObject_client.put("SUB_SECTOR", cursor_CLIENT.getString(22));
                jsonObject_client.put("INCOME_SOURCE", cursor_CLIENT.getString(23));
                jsonObject_client.put("INCOME_AMT", cursor_CLIENT.getString(24));
                jsonObject_client.put("CL_TAX", cursor_CLIENT.getString(25));
                jsonObject_client.put("CL_TAX_CODE", cursor_CLIENT.getString(26));
                jsonObject_client.put("CL_COUNTRY", cursor_CLIENT.getString(27));
                jsonObject_client.put("CL_PROVINCE", cursor_CLIENT.getString(28));
                jsonObject_client.put("CL_DISTRICT", cursor_CLIENT.getString(29));
                jsonObject_client.put("CL_AREA_CODE", cursor_CLIENT.getString(30));
                jsonObject_client.put("CL_EMAIL", cursor_CLIENT.getString(31));
                jsonObject_client.put("CL_NATION", cursor_CLIENT.getString(32));
                jsonObject_client.put("CL_MAIL_ADD1", cursor_CLIENT.getString(33));
                jsonObject_client.put("CL_MAIL_ADD2", cursor_CLIENT.getString(34));
                jsonObject_client.put("CL_MAIL_ADD3", cursor_CLIENT.getString(35));
                jsonObject_client.put("CL_MAIL_ADD4", cursor_CLIENT.getString(36));
                jsonObject_client.put("CL_EMARKS", cursor_CLIENT.getString(37));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url_client = PHP_URL_SQL + "MOBILE-COMPLETE-CLIENT_DATA.php";
            jsonDataTranferToLive.SendDataToLive(url_client, jsonObject_client);
        }
        cursor_CLIENT.close();

        //===================================================================================
        // ==== To Tranfer Gur Details To live==

        Cursor cursor_Gur_Details = sqLiteDatabase_AppMenu.rawQuery("SELECT * FROM LE_CO_CL_DATA WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'", null);
        if (cursor_Gur_Details.getCount() != 0) {
            cursor_Gur_Details.moveToFirst();
            JSONObject jsonObject_Gur_Details = new JSONObject();
            try {
                jsonObject_Gur_Details.put("APPLICATION_REF_NO", cursor_Gur_Details.getString(0));
                jsonObject_Gur_Details.put("CO_CLTYPE", cursor_Gur_Details.getString(1));
                jsonObject_Gur_Details.put("CO_NIC", cursor_Gur_Details.getString(2));
                jsonObject_Gur_Details.put("CO_TITLE", cursor_Gur_Details.getString(3));
                jsonObject_Gur_Details.put("CO_FULLY_NAME", cursor_Gur_Details.getString(4));
                jsonObject_Gur_Details.put("CO_ADDERS_1", cursor_Gur_Details.getString(5));
                jsonObject_Gur_Details.put("CO_ADDERS_2", cursor_Gur_Details.getString(6));
                jsonObject_Gur_Details.put("CO_ADDERS_3", cursor_Gur_Details.getString(7));
                jsonObject_Gur_Details.put("CO_ADDERS_4", cursor_Gur_Details.getString(8));
                jsonObject_Gur_Details.put("CO_GENDER", cursor_Gur_Details.getString(9));
                jsonObject_Gur_Details.put("CO_MARITAL_STATUS", cursor_Gur_Details.getString(10));
                jsonObject_Gur_Details.put("CO_INITIALS", cursor_Gur_Details.getString(11));
                jsonObject_Gur_Details.put("CO_LASTNAME", cursor_Gur_Details.getString(12));
                jsonObject_Gur_Details.put("CO_DATE_OF_BIRTH", cursor_Gur_Details.getString(13));
                jsonObject_Gur_Details.put("CO_AGE", cursor_Gur_Details.getString(14));
                jsonObject_Gur_Details.put("CO_MOBILE_NO", cursor_Gur_Details.getString(15));
                jsonObject_Gur_Details.put("CO_LAND_NO", cursor_Gur_Details.getString(16));
                jsonObject_Gur_Details.put("CO_SECTOR", cursor_Gur_Details.getString(17));
                jsonObject_Gur_Details.put("CO_SUBSECTOR", cursor_Gur_Details.getString(18));
                jsonObject_Gur_Details.put("CO_OCCUPATION", cursor_Gur_Details.getString(19));
                jsonObject_Gur_Details.put("CO_INCOME", cursor_Gur_Details.getString(20));
                jsonObject_Gur_Details.put("CO_SEC_VALUE", cursor_Gur_Details.getString(21));
                jsonObject_Gur_Details.put("CO_CREATE_DATE", cursor_Gur_Details.getString(21));
                jsonObject_Gur_Details.put("CO_CREATE_USER", cursor_Gur_Details.getString(22));
                jsonObject_Gur_Details.put("CO_BRANCH", cursor_Gur_Details.getString(23));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url_gur = PHP_URL_SQL + "MOBILE-COMPLETE-COAPP_DATA.php";
            jsonDataTranferToLive.SendDataToLive(url_gur, jsonObject_Gur_Details);
        }
        cursor_Gur_Details.close();

        //============================================================
        //===== Document Image Tranfer ====================
        Cursor cursor_Doc_Image = sqLiteDatabase_AppMenu.rawQuery("SELECT * FROM AP_DOC_IMAGE WHERE APP_REF_NO = '" + mInpapplicationNo.getText() + "'", null);
        if (cursor_Doc_Image.getCount() != 0) {
            cursor_Doc_Image.moveToFirst();

            do {
                JSONObject jsonObject_Doc_Image = new JSONObject();

                try {
                    jsonObject_Doc_Image.put("APPLICATION_REF_NO", cursor_Doc_Image.getString(0));
                    jsonObject_Doc_Image.put("DOC_REF", cursor_Doc_Image.getString(1));
                    jsonObject_Doc_Image.put("DOC_TYPE", cursor_Doc_Image.getString(3));
                    jsonObject_Doc_Image.put("DOC_STS", "A");
                    jsonObject_Doc_Image.put("DOC_DATE", cursor_Doc_Image.getString(2));
                    jsonObject_Doc_Image.put("DOC_USER", cursor_Doc_Image.getString(4));
                    jsonObject_Doc_Image.put("DOC_IMAGE", cursor_Doc_Image.getString(5));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            String url_Image = PHP_URL_SQL + "MOBILE-COMPLETE-DOC-IMAGE.php";
            jsonDataTranferToLive.SendDataToLive(url_Image, jsonObject_Doc_Image);

            } while (cursor_Doc_Image.moveToNext());
        }
        cursor_Doc_Image.close();

        //====== COMPLETE UPDATE STS - TABLE =======================
        String mCL_NIC = "";
        Cursor cursor_complete = sqLiteDatabase_AppMenu.rawQuery("SELECT CL_NIC FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'", null);
        if (cursor_complete.getCount() != 0) {
            cursor_complete.moveToFirst();
            mCL_NIC = cursor_complete.getString(0);
        }

        JSONObject jsonObject_complete_app = new JSONObject();
        try {
            jsonObject_complete_app.put("APP_REF_NO", mInpapplicationNo.getText());
            jsonObject_complete_app.put("CL_NIC", mCL_NIC);
            jsonObject_complete_app.put("BRANCH_CODE", LoginBranch);
            jsonObject_complete_app.put("MKT_CODE", LoginUser);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        cursor_complete.close();

        String url = PHP_URL_SQL + "MOBILE-COMPLETE-APPLICATION.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject_complete_app,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String Resp = "";
                        try {
                            Resp = response.getString("RESULT");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (Resp.equals("DONE")) {

                            //======= Change Sql Lite Sts
                            sqlliteCreateLeasing_AppMenu.UpdateAppSts(mInpapplicationNo.getText().toString() , "003" , "D");

                            progressDialog.dismiss();
                            AlertDialog.Builder abmyAlert = new AlertDialog.Builder(ApplcationSubmitMenu.this);
                            abmyAlert.setMessage("File Complete Successfully.").setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            })
                                    .setTitle("AFSL Mobile Leasing.")
                                    .create();
                            abmyAlert.show();
                        } else {
                            AlertDialog.Builder abmyAlert = new AlertDialog.Builder(ApplcationSubmitMenu.this);
                            abmyAlert.setMessage("Please try again..").setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            })
                                    .setTitle("AFSL Mobile Leasing.")
                                    .create();
                            abmyAlert.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(ApplcationSubmitMenu.this);
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
        mQueue.add(jsonObjectRequest);

        //==================================================
    }

    //=== To Get The Before PO Save Data to Display
    public void GetMainSaveData ()
    {
        //=== Application Details =====
        Cursor cursorApplication = sqLiteDatabase_AppMenu.rawQuery("SELECT AP_PRODUCT , AP_INVOICE_AMT , AP_DOWN_PAY , AP_FACILITY_AMT," +
                "AP_ETV , AP_RATE , AP_PERIOD , AP_RENTAL_AMT , AP_PO_SEND_DELEAR , AP_MAIN_SUPPLIR FROM LE_APPLICATION WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'", null);

        DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");



        if (cursorApplication.getCount() != 0) {
            cursorApplication.moveToFirst();
            mProduct.setText(cursorApplication.getString(0));
            mInvAmt.setText( nf.format(Double.parseDouble(cursorApplication.getString(1))));


            if (cursorApplication.getString(2).equals(""))
            {
                mClCont.setText("0");
            }
            else
            {
                mClCont.setText(nf.format(Double.parseDouble(cursorApplication.getString(2))) );
            }

            Mfacamt.setText( nf.format(Double.parseDouble(cursorApplication.getString(3))));
            mExp.setText(cursorApplication.getString(4));
            mRate.setText(cursorApplication.getString(5));
            mPeriod.setText(cursorApplication.getString(6));
            mRentalAmt.setText( nf.format(Double.parseDouble(cursorApplication.getString(7))));
            mDelear.setText(cursorApplication.getString(8));
            mSuuname.setText(cursorApplication.getString(9));
            mLeasingAmt.setText( nf.format(Double.parseDouble(cursorApplication.getString(3))));
            cursorApplication.close();
        }

        //=== Clent Details ==============
        Cursor cursorCLIENT = sqLiteDatabase_AppMenu.rawQuery("SELECT CL_FULLY_NAME , CL_ADDERS_1 , CL_ADDERS_2 , CL_ADDERS_3 ,CL_ADDERS_4" +
                ",CL_MOBILE_NO FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'", null);
        if (cursorCLIENT.getCount() != 0) {
            cursorCLIENT.moveToFirst();
            mFullyName.setText(cursorCLIENT.getString(0));
            mAdders.setText(cursorCLIENT.getString(1) + "," + cursorCLIENT.getString(2) + "," + cursorCLIENT.getString(3) + "," + cursorCLIENT.getString(4));
            mPhoneno.setText(cursorCLIENT.getString(5));
            cursorCLIENT.close();
        }

        //=== Asset Details
        Cursor cursorASSET = sqLiteDatabase_AppMenu.rawQuery("SELECT AS_EQ_TYPE , AS_EQ_MAKE , AS_EQ_MODEL , AS_EQ_REGNO , AS_EQ_ENG_NO , AS_EQ_CHAS_NO," +
                "AS_EQ_YEAR , AP_INSURANCE_COMPANY , MK_VAL FROM LE_ASSET_DETAILS WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'", null);
        if (cursorASSET.getCount() != 0) {
            cursorASSET.moveToFirst();
            mAsstype.setText(cursorASSET.getString(0));
            mMake.setText(cursorASSET.getString(1));
            mModel.setText(cursorASSET.getString(2));
            mRegNo.setText(cursorASSET.getString(3));
            mEngNo.setText(cursorASSET.getString(4));
            mChassNo.setText(cursorASSET.getString(5));
            mYear.setText(cursorASSET.getString(6));
            Minscom.setText(cursorASSET.getString(7));
            mMkVal.setText( nf.format(Double.parseDouble(cursorASSET.getString(8))));
            cursorASSET.close();
        }

        //=== Charges Details (Intduser) ============================
        mCapTot = 0.00;
        mUpfronttot = 0.00;
        Cursor cursorCHARGES = sqLiteDatabase_AppMenu.rawQuery("SELECT OT_CHARGE_AMT,OT_CHARGE_TYPE FROM LE_CHARGS WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "' and OT_CHARGE_NAME = 'IND CHARGE'", null);
        if (cursorCHARGES.getCount() != 0) {
            cursorCHARGES.moveToFirst();

            if (cursorCHARGES.getString(0).length() !=0)
            {
                mIndAmt.setText( nf.format(Double.parseDouble(cursorCHARGES.getString(0))));
            }

            if (cursorCHARGES.getString(1).equals("1")) {
                if (mIndAmt.length() != 0) {
                    mCapTot = mCapTot + Double.parseDouble(mIndAmt.getText().toString().replace("," , ""));
                }
                mIndCap.setText("CAP");
            } else {
                if (mIndAmt.length() != 0) {
                    mUpfronttot = mUpfronttot + Double.parseDouble(mIndAmt.getText().toString().replace("," , ""));
                }
                mIndCap.setText("UOF");
            }

            cursorCHARGES.close();
        }


        //=== Charges Details (Sch) =====
        cursorCHARGES = sqLiteDatabase_AppMenu.rawQuery("SELECT OT_CHARGE_AMT,OT_CHARGE_TYPE FROM LE_CHARGS WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "' and OT_CHARGE_NAME = 'SERVICE CHARGE'", null);
        if (cursorCHARGES.getCount() != 0) {
            cursorCHARGES.moveToFirst();

            if (cursorCHARGES.getString(0).length() != 0)
            {
                mSchamt.setText( nf.format(Double.parseDouble(cursorCHARGES.getString(0))));
            }

            if (cursorCHARGES.getString(1).equals("1")) {
                if (mSchamt.length() != 0) {
                    mCapTot = mCapTot + Double.parseDouble(mSchamt.getText().toString().replace("," , ""));
                }
                mSchCap.setText("CAP");
            } else {
                if (mSchamt.length() != 0) {
                    mUpfronttot = mUpfronttot + Double.parseDouble(mSchamt.getText().toString().replace("," , ""));
                }
                mSchCap.setText("UOF");
            }
            cursorCHARGES.close();
        }

        //=== Charges Details (RMV) =====
        cursorCHARGES = sqLiteDatabase_AppMenu.rawQuery("SELECT OT_CHARGE_AMT,OT_CHARGE_TYPE FROM LE_CHARGS WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "' and OT_CHARGE_NAME = 'RMV CHARGE'", null);
        if (cursorCHARGES.getCount() != 0) {
            cursorCHARGES.moveToFirst();

            if (cursorCHARGES.getString(0).length() != 0)
            {
                mRmvAmt.setText( nf.format(Double.parseDouble(cursorCHARGES.getString(0))));
            }

            if (cursorCHARGES.getString(1).equals("1")) {
                if (mRmvAmt.length() != 0) {
                    mCapTot = mCapTot + Double.parseDouble(mRmvAmt.getText().toString().replace("," , ""));
                }
                mRmvCap.setText("CAP");
            } else {
                if (mRmvAmt.length() != 0) {
                    mUpfronttot = mUpfronttot + Double.parseDouble(mRmvAmt.getText().toString().replace("," , ""));
                }
                mRmvCap.setText("UOF");
            }
            cursorCHARGES.close();
        }

        //=== Charges Details (INS) =====
        cursorCHARGES = sqLiteDatabase_AppMenu.rawQuery("SELECT OT_CHARGE_AMT,OT_CHARGE_TYPE FROM LE_CHARGS WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "' and OT_CHARGE_NAME = 'INSURANCE CHARGE'", null);
        if (cursorCHARGES.getCount() != 0) {
            cursorCHARGES.moveToFirst();

            if (cursorCHARGES.getString(0).length() != 0)
            {
                mInsAmt.setText( nf.format(Double.parseDouble(cursorCHARGES.getString(0))) );
            }

            if (cursorCHARGES.getString(1).equals("1")) {
                if (mInsAmt.length() != 0) {
                    mCapTot = mCapTot + Double.parseDouble(mInsAmt.getText().toString().replace("," , ""));
                }
                mInsCap.setText("CAP");
            } else {
                if (mInsAmt.length() != 0) {
                    mUpfronttot = mUpfronttot + Double.parseDouble(mInsAmt.getText().toString().replace("," , ""));
                }
                mInsCap.setText("UOF");
            }
            cursorCHARGES.close();
        }

        //=== Charges Details (TRP) =====
        cursorCHARGES = sqLiteDatabase_AppMenu.rawQuery("SELECT OT_CHARGE_AMT,OT_CHARGE_TYPE FROM LE_CHARGS WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "' and OT_CHARGE_NAME = 'TRANSPORT CHARGES'", null);
        if (cursorCHARGES.getCount() != 0) {
            cursorCHARGES.moveToFirst();

            if (cursorCHARGES.getString(0).length() != 0)
            {
                mTapAmt.setText( nf.format(Double.parseDouble(cursorCHARGES.getString(0))));
            }

            if (cursorCHARGES.getString(1).equals("1")) {
                if (mTapAmt.length() != 0) {
                    mCapTot = mCapTot + Double.parseDouble(mTapAmt.getText().toString().replace("," , ""));
                }
                mTrpCap.setText("CAP");
            } else {
                if (mTapAmt.length() != 0) {
                    mUpfronttot = mUpfronttot + Double.parseDouble(mTapAmt.getText().toString().replace("," , ""));
                }

                mTrpCap.setText("UOF");
            }
            cursorCHARGES.close();
        }

        //=== Charges Details (TRP) =====
        cursorCHARGES = sqLiteDatabase_AppMenu.rawQuery("SELECT OT_CHARGE_AMT,OT_CHARGE_TYPE "
                +
                "FROM LE_CHARGS WHERE APPLICATION_REF_NO = '" + mInpapplicationNo.getText() + "'" +
                " and OT_CHARGE_NAME = 'OTHER CHARGE'", null);
        if (cursorCHARGES.getCount() != 0) {
            cursorCHARGES.moveToFirst();

            if (cursorCHARGES.getString(0).length() != 0)
            {
                mOthAmt.setText( nf.format(Double.parseDouble(cursorCHARGES.getString(0))));
            }

            if (cursorCHARGES.getString(1).equals("1")) {
                if (mOthAmt.length() != 0) {
                    mCapTot = mCapTot + Double.parseDouble(mOthAmt.getText().toString().replace("," , ""));
                }
                mOthCap.setText("CAP");
            } else {
                if (mOthAmt.length() != 0) {
                    mUpfronttot = mUpfronttot + Double.parseDouble(mOthAmt.getText().toString().replace("," , ""));
                }
                mOthCap.setText("UOF");
            }
            cursorCHARGES.close();
        }

        if (mCapTot != 0)
        {
            mCapAmt.setText( nf.format(Double.parseDouble(mCapTot.toString())) );
        }

        if (mUpfronttot != 0)
        {
            mUpforntamt.setText( nf.format(Double.parseDouble(mUpfronttot.toString())));
        }

    }
}
