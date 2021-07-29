package com.AFiMOBILE.afslmobileapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Application extends AppCompatActivity {

    private Button btnNewApplication ;
    public static Button btnModifyData , btnmDelete , btnPoReq , btnDoc;
    private RecyclerView mRecycleviwer;
    public static Adapter_PendingApplication mAdapter;
    public static TextView mApp;
    public String mDcoType;
    public static androidx.appcompat.widget.Toolbar toolbar;
    public TextView ConnectionSts;
    public Handler handler;
    public String mApplicationNo ,mClientName , mFacAmt , mInpApStage , mClNic;
    SqlliteCreateLeasing sqlliteCreateLeasing = new SqlliteCreateLeasing(this);
    public String PHP_URL_SQL="" , LoginUser="" , LoginDate = "" , LoginBranch= "";
    public ProgressDialog progressDialog;
    public SQLiteDatabase sqLiteDatabase_Application;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    private RequestQueue mQueue;
    public static boolean SendSms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));

        btnNewApplication   =   (Button)findViewById(R.id.btncreatenewapp);
        mRecycleviwer       =   (RecyclerView)findViewById(R.id.rcppendingapp) ;
        mApp                =   findViewById(R.id.txtSelectAppnpo);
        btnModifyData       =   findViewById(R.id.btnmodify);
        btnmDelete          =   findViewById(R.id.btndelete);
        btnPoReq            =   findViewById(R.id.btnporequest);
        btnDoc              =   findViewById(R.id.btndocupload);

        progressDialog = new ProgressDialog(Application.this);
        progressDialog.setTitle("AFi Mobile");
        progressDialog.setMessage("Loading application View, please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);

        checkDataConnectionStatus   = new CheckDataConnectionStatus(Application.this);
        sqLiteDatabase_Application = sqlliteCreateLeasing.getReadableDatabase();

        //===== Create Volly Request Ques. =======
        mQueue   = VollySingleton.getInstance(this).getRequestQueue();

        //=== Recycviwe load the data
        mRecycleviwer.setHasFixedSize(true);
        mRecycleviwer.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_PendingApplication(this , GetPendingDataApp());
        mRecycleviwer.setAdapter(mAdapter);

        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        //=== Check Data Connection status Toolbra every 5 second.
        ConnectionSts = (TextView)findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();

        //==== Document Upload ======
        btnDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent_doc_upload = new Intent("android.intent.action.DocumentUpload");

                //Intent intent_doc_upload = new Intent("android.intent.document_uplaod_new");
                intent_doc_upload.putExtra("ApplicationNo" , mApp.getText());
                intent_doc_upload.putExtra("AppStage" , "001");
                startActivity(intent_doc_upload);

            }
        });

        //=== PO Request
        btnPoReq.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                boolean mDocValidate;
                mDocValidate=false;
                mDocValidate =  CheckDocValidate();

                Log.d("DOC VALIDAT-",String.valueOf(mDocValidate));
                if (mDocValidate == true)
                {
                    boolean mCheckSts = checkDataConnectionStatus.IsConnected();
                    if (mCheckSts == true)
                    {
                        Log.d("test" , "Bhagya");
                        String mExpireAppno = CheckPOReqValidate();

                        Log.d("test" , mExpireAppno);
                        if (mExpireAppno.equals(""))
                        {
                            AlertDialog.Builder builderdelete = new AlertDialog.Builder(Application.this);
                            builderdelete.setTitle("AFiMobile-Leasing");
                            builderdelete.setMessage("Are you sure to Request PO ?");
                            builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {

                                    Application.btnModifyData.setEnabled(false);
                                    Application.btnmDelete.setEnabled(false);
                                    Application.btnPoReq.setEnabled(false);
                                    Application.btnDoc.setEnabled(false);

                                    Application.btnModifyData.setBackgroundResource(R.drawable.normalbuttondisable);
                                    Application.btnmDelete.setBackgroundResource(R.drawable.normalbuttondisable);
                                    Application.btnPoReq.setBackgroundResource(R.drawable.normalbuttondisable);
                                    Application.btnDoc.setBackgroundResource(R.drawable.normalbuttondisable);

                                    new PoRequest_BackGround_Process(Application.this).execute(mApp.getText().toString());

                                    //Po_DataSend_LiveServer po_dataSend_liveServer = new Po_DataSend_LiveServer(Application.this);
                                   // po_dataSend_liveServer.SendDataToPo(mApp.getText().toString());

                                    mAdapter.swapCursor(GetPendingDataApp());
                                    mApp.setText("");

                                    Log.d("PO REQUEST","DONE");
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(Application.this);
                            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                            builder.setMessage("Please complete this application - " + mExpireAppno);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Application.this);
                        builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                        builder.setMessage("Data Connection Not available. Please Turn on Connection. ** Note - This is your First Login.It is Need to Mobile Data On.");
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Application.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("PO Submit Document Not Complete -" + mDcoType);
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

        //=== Delete Button Action===
        btnmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mApp.getText() != null)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Application.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to delete this application?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SqlliteCreateLeasing sqlliteCreateLeasing = new SqlliteCreateLeasing(Application.this);
                            sqlliteCreateLeasing.DeleteAppData(mApp.getText().toString());
                            mAdapter.swapCursor(GetPendingDataApp());
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
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Application.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to delete this application?");

                    builderdelete.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builderdelete.create();
                    dialog.show();
                }


            }
        });

        //=== Modify Data - Button
        btnModifyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent modifyintent = new Intent("android.intent.action.CreateApplication");
                modifyintent.putExtra("ApplicationNo" , mApp.getText());
                modifyintent.putExtra("Type" , "M");
                startActivity(modifyintent);
            }
        });


        //=== Add New Application - Button
        btnNewApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.CreateApplication");
                intent.putExtra("ApplicationNo" , "");
                intent.putExtra("Type" , "A");
                startActivity(intent);
            }
        });
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

        mAdapter = new Adapter_PendingApplication(this , GetPendingDataApp());
        mRecycleviwer.setAdapter(mAdapter);

        Application.btnModifyData.setEnabled(false);
        Application.btnmDelete.setEnabled(false);
        Application.btnPoReq.setEnabled(false);
        Application.btnDoc.setEnabled(false);

        Application.btnModifyData.setBackgroundResource(R.drawable.normalbuttondisable);
        Application.btnmDelete.setBackgroundResource(R.drawable.normalbuttondisable);
        Application.btnPoReq.setBackgroundResource(R.drawable.normalbuttondisable);
        Application.btnDoc.setBackgroundResource(R.drawable.normalbuttondisable);
    }

    protected void onDestroy(){

        sqLiteDatabase_Application.close();
        sqlliteCreateLeasing.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

    public String CheckPOReqValidate()
    {
        //==== Check PO Validate Period
        String PRODUCT_CODE=""; String mPARA_VAL=""; String mApplicationNo="";


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Cursor cursor_prcode = sqLiteDatabase_Application.rawQuery("SELECT AP_PRODUCT FROM LE_APPLICATION WHERE APPLICATION_REF_NO = '" + mApp.getText().toString() + "'" , null);
        if (cursor_prcode.getCount() != 0)
        {
            cursor_prcode.moveToFirst();
            PRODUCT_CODE = cursor_prcode.getString(0);
        }


        Cursor cursor_po_date =  sqLiteDatabase_Application.rawQuery("SELECT PARA_VAL FROM MAST_PR_PARAMATER WHERE PR_CODE = '" + PRODUCT_CODE + "' AND PRRA_CODE = 'PDE'" , null);
        if (cursor_po_date.getCount() != 0)
        {
            cursor_po_date.moveToFirst();
            mPARA_VAL   =   cursor_po_date.getString(0);
        }

        Cursor cursor_check = sqLiteDatabase_Application.rawQuery("SELECT AP_PO_RQ_DATE,APPLICATION_REF_NO FROM LE_APPLICATION WHERE AP_PO_RQ_USER = '" + LoginUser + "' and AP_STAGE = '002'" , null);
        if (cursor_check.getCount() != 0)
        {
            cursor_check.moveToFirst();

            do {

                try {
                    Date Po_date = dateFormat.parse(cursor_check.getString(0));
                    Date Curent_Date = dateFormat.parse(LoginDate);

                    Log.d("PO-DATE" ,dateFormat.format(Po_date) );
                    Log.d("CURRENT-DATE" ,dateFormat.format(Curent_Date));

                    long diff_date = Curent_Date.getTime() - Po_date.getTime();
                    int diff = (int) (diff_date / (24 * 60 * 60 * 1000));

                    Log.d("DATE-DEF" , String.valueOf(diff));

                    if (diff >= Integer.parseInt(mPARA_VAL))
                    {
                        mApplicationNo  =   cursor_check.getString(1);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }while (cursor_check.moveToNext());

        }
        return mApplicationNo;
    }


    public boolean CheckDocValidate()
    {
        String ProductCode="" , EqCagery="";
        boolean mDocOutValidatValidate=true;
        mDcoType="";
        Cursor cursor_doc_procde = sqLiteDatabase_Application.rawQuery("SELECT LE_APPLICATION.AP_PRODUCT , LE_ASSET_DETAILS.AS_EQ_CATAGE FROM LE_APPLICATION " +
                "INNER JOIN LE_ASSET_DETAILS ON LE_ASSET_DETAILS.APPLICATION_REF_NO = LE_APPLICATION.APPLICATION_REF_NO WHERE LE_APPLICATION.APPLICATION_REF_NO = '" + mApp.getText() + "'" , null);
        if (cursor_doc_procde.getCount() != 0)
        {
            cursor_doc_procde.moveToFirst();
            ProductCode = cursor_doc_procde.getString(0);
            EqCagery    = cursor_doc_procde.getString(1);
            Cursor cursor_doc_type = sqLiteDatabase_Application.rawQuery("SELECT DOC_NAME FROM AP_MAST_DOC_TYPE WHERE PRODUCT_CODE = '" + ProductCode + "' AND " +
                    "EQ_TYPE = '" + EqCagery + "' and MAN_STAGE = '001' and MAY_TYPE = 'Y'" , null);
            if (cursor_doc_type.getCount() != 0)
            {
                cursor_doc_type.moveToFirst();
                do{
                    Cursor cursor_image_check = sqLiteDatabase_Application.rawQuery("SELECT DOC_STS FROM AP_DOC_IMAGE WHERE DOC_STS = '" + cursor_doc_type.getString(0) + "' AND APP_REF_NO = '" + mApp.getText() + "'"  , null );
                    if (cursor_image_check.getCount() == 0)
                    {
                        Log.d("DOC-VALIDAT" , "DONE");
                        mDocOutValidatValidate=false;
                        mDcoType = mDcoType + cursor_doc_type.getString(0) + ",";
                    }
                }while (cursor_doc_type.moveToNext());
            }
            cursor_doc_type.close();
        }
        cursor_doc_procde.close();

        return mDocOutValidatValidate;
    }

    public Cursor GetPendingDataApp ()
    {
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"Appno" , "ClientName" , "Amount" , "CL_NIC" , "AP_STAGE" });
        startManagingCursor(mOutPutcouser);

        Cursor mPendingAppcursor = sqLiteDatabase_Application.rawQuery("SELECT APPLICATION_REF_NO , AP_FACILITY_AMT , AP_STAGE FROM LE_APPLICATION WHERE AP_STAGE = '000' OR AP_STAGE = '001' AND AP_MK_OFFICER = '" + LoginUser + "'",null );
        if (mPendingAppcursor.getCount() != 0)
        {
            while (mPendingAppcursor.moveToNext())
            {
                mApplicationNo = mPendingAppcursor.getString(0);
                mFacAmt        = mPendingAppcursor.getString(1);
                mInpApStage    = mPendingAppcursor.getString(2);

                if (mApplicationNo != null)
                {
                    Cursor mClientCurser = sqLiteDatabase_Application.rawQuery("SELECT CL_TITLE , CL_NIC , CL_FULLY_NAME  FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '"  + mApplicationNo + "'",null);
                    if (mClientCurser.getCount() != 0)
                    {
                        mClientCurser.moveToFirst();
                        mClNic      =  mClientCurser.getString(1);
                        mClientName =  mClientCurser.getString(0) + " " + mClientCurser.getString(2);
                    }
                    mClientCurser.close();
                }
                mOutPutcouser.addRow(new String[] {mApplicationNo , mClientName , mFacAmt , mClNic , mInpApStage});
            }
        }
        mPendingAppcursor.close();
        return mOutPutcouser;
    }

    /*===== Remove This Code - Volly Class
    public void DataPOReqiest () {

         String mClNic="" , mBranch="" , mMkOfficer="" , mApplication="";

        //=== Client Data Tranfer ========
        progressDialog.show();
        SQLiteDatabase sqLiteDatabase = sqlliteCreateLeasing.getReadableDatabase();
        Cursor mClientCurser = sqLiteDatabase.rawQuery("SELECT APPLICATION_REF_NO,CL_NIC,CL_TITLE,CL_FULLY_NAME,CL_ADDERS_1" +
                ",CL_ADDERS_2,CL_ADDERS_3,CL_ADDERS_4,CL_OCCUPATION,CL_MOBILE_NO,CL_CREATE_DATE,CL_CREATE_USER FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mApp.getText() + "'", null);
        JSONObject ClientDataObject = new JSONObject();

        mClientCurser.moveToFirst();
        if (mClientCurser.getCount() != 0) {
            try {
                ClientDataObject.put("APPLICATION_REF_NO", mClientCurser.getString(0));
                ClientDataObject.put("CL_NIC", mClientCurser.getString(1));
                ClientDataObject.put("CL_TITLE", mClientCurser.getString(2));
                ClientDataObject.put("CL_FULLY_NAME", mClientCurser.getString(3));
                ClientDataObject.put("CL_ADDERS_1", mClientCurser.getString(4));
                ClientDataObject.put("CL_ADDERS_2", mClientCurser.getString(5));
                ClientDataObject.put("CL_ADDERS_3", mClientCurser.getString(6));
                ClientDataObject.put("CL_ADDERS_4", mClientCurser.getString(7));
                ClientDataObject.put("CL_OCCUPATION", mClientCurser.getString(8));
                ClientDataObject.put("CL_MOBILE_NO", mClientCurser.getString(9));
                ClientDataObject.put("CL_CREATE_DATE", mClientCurser.getString(10));
                ClientDataObject.put("CL_CREATE_USER", mClientCurser.getString(11));

                mClNic = mClientCurser.getString(1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mClientCurser.close();

        String UrlClient = PHP_URL_SQL + "MOBILE-PO-CLIENTCREATE.php";
        JsonDataTranferToLive jsonDataTranferToLive = new JsonDataTranferToLive(this);
        //jsonDataTranferToLive.SendDataToLive("http://203.115.12.125:82//core//mobnew//MOBILE-PO-CLIENTCREATE.php", ClientDataObject);
        jsonDataTranferToLive.SendDataToLive(UrlClient, ClientDataObject);
        mClientCurser.close();
        //============================================================================================================

        //=== Send Application Details to live ================

        //======= Change Sql Lite Sts
        mApplication = mApp.getText().toString();
        sqlliteCreateLeasing.UpdateAppSts(mApplication , "001" , "S");

        SQLiteDatabase sqLiteDatabase_poRequestApplication = sqlliteCreateLeasing.getReadableDatabase();
        Cursor mApplicationCurser = sqLiteDatabase_poRequestApplication.rawQuery("SELECT APPLICATION_REF_NO,AP_PRODUCT,AP_INVOICE_AMT,AP_DOWN_PAY,AP_FACILITY_AMT,AP_ETV,AP_RATE," +
                "AP_PERIOD,AP_RENTAL_AMT,AP_MK_OFFICER,AP_BRANCH,AP_PO_STS,AP_PO_SEND_DELEAR,AP_PO_DELEAR_EMAIL,AP_ENTDATE,AP_STAGE,AP_MAIN_SUPPLIR,AP_MAIN_SUPPLIR_EMAIL,AP_PO_RQ_USER FROM LE_APPLICATION WHERE APPLICATION_REF_NO = '" + mApp.getText() + "'", null);

        mApplicationCurser.moveToFirst();
        if (mApplicationCurser.getCount() != 0) {
            JSONObject ApplicationDataObject = new JSONObject();
            try {
                ApplicationDataObject.put("APPLICATION_REF_NO", mApplicationCurser.getString(0));
                ApplicationDataObject.put("AP_PRODUCT", mApplicationCurser.getString(1));
                ApplicationDataObject.put("AP_INVOICE_AMT", mApplicationCurser.getString(2));
                ApplicationDataObject.put("AP_DOWN_PAY", mApplicationCurser.getString(3));
                ApplicationDataObject.put("AP_FACILITY_AMT", mApplicationCurser.getString(4));
                ApplicationDataObject.put("AP_ETV", mApplicationCurser.getString(5));
                ApplicationDataObject.put("AP_RATE", mApplicationCurser.getString(6));
                ApplicationDataObject.put("AP_PERIOD", mApplicationCurser.getString(7));
                ApplicationDataObject.put("AP_RENTAL_AMT", mApplicationCurser.getString(8));
                ApplicationDataObject.put("AP_MK_OFFICER", mApplicationCurser.getString(9));
                ApplicationDataObject.put("AP_BRANCH", mApplicationCurser.getString(10));
                ApplicationDataObject.put("AP_PO_STS", mApplicationCurser.getString(11));
                ApplicationDataObject.put("AP_PO_SEND_DELEAR", mApplicationCurser.getString(12));
                ApplicationDataObject.put("AP_PO_DELEAR_EMAIL", mApplicationCurser.getString(13));
                ApplicationDataObject.put("AP_ENTDATE", mApplicationCurser.getString(14));
                ApplicationDataObject.put("AP_STAGE", mApplicationCurser.getString(15));
                ApplicationDataObject.put("AP_MAIN_SUPPLIR", mApplicationCurser.getString(16));
                ApplicationDataObject.put("AP_MAIN_SUPPLIR_EMAIL", mApplicationCurser.getString(17));
                ApplicationDataObject.put("AP_PO_RQ_USER", mApplicationCurser.getString(18));

                mBranch = mApplicationCurser.getString(10);
                mMkOfficer = mApplicationCurser.getString(9);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonDataTranferToLive jsonDataTranferToLiveApplication = new JsonDataTranferToLive(this);
            String Urlapplication = PHP_URL_SQL + "MOBILE-PO-APPLICATIONCREATE.php";
            //jsonDataTranferToLiveApplication.SendDataToLive("http://203.115.12.125:82//core//mobnew//MOBILE-PO-APPLICATIONCREATE.php", ApplicationDataObject);
            jsonDataTranferToLiveApplication.SendDataToLive(Urlapplication, ApplicationDataObject);
            mApplicationCurser.close();
        }

        //===========================================================================================
        //=== To send Asset Data to live Server
        SQLiteDatabase sqLiteDatabase1Asset = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursorAsset = sqLiteDatabase1Asset.rawQuery("SELECT APPLICATION_REF_NO,AS_EQ_TYPE,AS_EQ_CATAGE,AS_EQ_MAKE,AS_EQ_MODEL,AS_EQ_REGNO,AS_EQ_ENG_NO,AS_EQ_CHAS_NO,AS_EQ_YEAR," +
                "AS_INV_DELER,AS_INV_SUPPLIER,AP_INV_DATE,AP_INSURANCE_COMPANY,AP_ENT_DATE,AP_ENT_USER,MK_VAL ,AP_INTDU FROM LE_ASSET_DETAILS WHERE APPLICATION_REF_NO = '" + mApp.getText() + "'", null);

        cursorAsset.moveToFirst();
        if (cursorAsset.getCount() != 0)
        {
            JSONObject JsonAsset = new JSONObject();
            try {
                JsonAsset.put("APPLICATION_REF_NO" , cursorAsset.getString(0));
                JsonAsset.put("AS_EQ_TYPE" , cursorAsset.getString(1));
                JsonAsset.put("AS_EQ_CATAGE" , cursorAsset.getString(2));
                JsonAsset.put("AS_EQ_MAKE" , cursorAsset.getString(3));
                JsonAsset.put("AS_EQ_MODEL" , cursorAsset.getString(4));
                JsonAsset.put("AS_EQ_REGNO" , cursorAsset.getString(5));
                JsonAsset.put("AS_EQ_ENG_NO" , cursorAsset.getString(6));
                JsonAsset.put("AS_EQ_CHAS_NO" , cursorAsset.getString(7));
                JsonAsset.put("AS_EQ_YEAR" , cursorAsset.getString(8));
                JsonAsset.put("AS_INV_DELER" , cursorAsset.getString(9));
                JsonAsset.put("AS_INV_SUPPLIER" , cursorAsset.getString(10));
                //JsonAsset.put("AP_INV_DATE" , cursorAsset.getString(11));
                JsonAsset.put("AP_INV_DATE" , "2019-01-01");
                JsonAsset.put("AP_INSURANCE_COMPANY" , cursorAsset.getString(12));
                JsonAsset.put("AP_ENT_DATE" , cursorAsset.getString(13));
                JsonAsset.put("AP_ENT_USER" , cursorAsset.getString(14));
                JsonAsset.put("AP_MKVAL" , cursorAsset.getString(15));
                JsonAsset.put("AP_INTDU" , cursorAsset.getString(16));

                JsonDataTranferToLive jsonDataTranferToLive1Asset = new JsonDataTranferToLive(this);
                String Urlapplication = PHP_URL_SQL + "MOBILE-PO-ASSETCREATE-NEW.php";
                //jsonDataTranferToLive1Asset.SendDataToLive("http://203.115.12.125:82//core//mobnew//MOBILE-PO-ASSETCREATE-NEW.php" , JsonAsset);
                jsonDataTranferToLive1Asset.SendDataToLive(Urlapplication , JsonAsset);
                cursorAsset.close();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //==== Tanfer Charge Details ------

        SQLiteDatabase sqLiteDatabase1charges = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursorcharge = sqLiteDatabase1charges.rawQuery("SELECT APPLICATION_REF_NO,OT_CHARGE_NAME,OT_CHARGE_AMT,OT_CHARGE_TYPE FROM LE_CHARGS WHERE APPLICATION_REF_NO = '" + mApp.getText() + "'", null);
        String UrlAsset = PHP_URL_SQL + "MOBILE-PO-CHARGESCREATE.php";
        if (cursorcharge.getCount() != 0)
        {
            if (cursorcharge.moveToFirst())
            {
                do{
                    JSONObject jsonObjectCharge = new JSONObject();
                    try {
                        jsonObjectCharge.put("APPLICATION_REF_NO" , cursorcharge.getString(0));
                        jsonObjectCharge.put("OT_CHARGE_NAME" , cursorcharge.getString(1));
                        jsonObjectCharge.put("OT_CHARGE_AMT" , cursorcharge.getString(2));
                        jsonObjectCharge.put("OT_CHARGE_TYPE" , cursorcharge.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonDataTranferToLive jsonDataTranferToLive1Charge = new JsonDataTranferToLive(this);
                    //jsonDataTranferToLive1Charge.SendDataToLive("http://203.115.12.125:82//core//mobnew//MOBILE-PO-CHARGESCREATE.php" , jsonObjectCharge);
                    jsonDataTranferToLive1Charge.SendDataToLive(UrlAsset , jsonObjectCharge);


                }while (cursorcharge.moveToNext());
            }
        }
        cursorcharge.close();
        //=================================


        //==== Send Image Details To Live Database.
        if (mApp.getText() != null)
        {
            SQLiteDatabase sqLiteDatabase1_doc_upload = sqlliteCreateLeasing.getReadableDatabase();
            Cursor cursor_doc_image = sqLiteDatabase1_doc_upload.rawQuery("SELECT * FROM AP_DOC_IMAGE WHERE APP_REF_NO = '" + mApp.getText() + "'" , null);

            String UrlDocUpload = PHP_URL_SQL + "MOBILE-COMPLETE-DOC-IMAGE.php";
            if (cursor_doc_image.getCount() != 0)
            {
                cursor_doc_image.moveToFirst();

                do{
                    JSONObject jsonObjectImage = new JSONObject();
                    try {

                        jsonObjectImage.put("APPLICATION_REF_NO" , cursor_doc_image.getString(0));
                        jsonObjectImage.put("DOC_REF" , cursor_doc_image.getString(1));
                        jsonObjectImage.put("DOC_TYPE" , cursor_doc_image.getString(3));
                        jsonObjectImage.put("DOC_STS" , "A");
                        jsonObjectImage.put("DOC_DATE" , cursor_doc_image.getString(2));
                        jsonObjectImage.put("DOC_USER" , cursor_doc_image.getString(4));
                        jsonObjectImage.put("DOC_IMAGE" , cursor_doc_image.getString(5));
                        jsonObjectImage.put("DOC_REMARKS" , cursor_doc_image.getString(6));
                        jsonObjectImage.put("DOC_DOC_REF_NIC" , cursor_doc_image.getString(3));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonDataTranferToLive jsonDataTranferToLive1Charge = new JsonDataTranferToLive(this);
                    jsonDataTranferToLive1Charge.SendDataToLive(UrlDocUpload , jsonObjectImage);

                }while (cursor_doc_image.moveToNext());

            }
            cursor_doc_image.close();
            sqLiteDatabase1_doc_upload.close();
        }


        //=====================================================================
        //=== Send PO Request To live data base
        if (mApp.getText() != null)
        {
            JSONObject jsonObjectPorequest = new JSONObject();
            try {
                jsonObjectPorequest.put("APPLICATION_REF_NO" , mApp.getText());
                jsonObjectPorequest.put("CL_NIC" , mClNic);
                jsonObjectPorequest.put("REQ_BRANCH" , mBranch);
                jsonObjectPorequest.put("REQ_OFFICER" , mMkOfficer);
                jsonObjectPorequest.put("MGR_CODE" , "");
                jsonObjectPorequest.put("REQ_PO_STS" , "001");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonDataTranferToLive jsonDataTranferToLive1Porequest = new JsonDataTranferToLive(this);
            String UrlPorequest = PHP_URL_SQL + "MOBILE-PO-REQUEST.php";
            //jsonDataTranferToLive1Porequest.SendDataToLive("http://203.115.12.125:82//core//mobnew//MOBILE-PO-REQUEST.php" , jsonObjectPorequest);
            jsonDataTranferToLive1Porequest.SendDataToLive(UrlPorequest , jsonObjectPorequest);
        }

        progressDialog.dismiss();
        mAdapter.swapCursor(GetPendingDataApp());
        mApp.setText("");

        String Msg = "You have succesfully Request PO To " + mApp.getText();
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
    }
    */

    private String ImagetoString(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,30,outputStream);
        byte[] imagebyte = outputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imagebyte , Base64.DEFAULT);
        return encodeImage;
    }
}
