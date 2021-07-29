package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;

public class ManagerApprove extends AppCompatActivity {

    public static Button btnReferData , btnViewDetails;
    private RecyclerView mPendingAppData;
    public static Adapter_MgrApprove_app myPenadapter;
    public static TextView mSelectAppno;
    public SqlliteCreateLeasing sqlliteCreateLeasing_ManagerApprove = new SqlliteCreateLeasing(this);
    public SQLiteDatabase sqLiteDatabase_ManagerApprove;
    public String LoginUser="" , LoginDate = "" , LoginBranch= "" , PHP_URL_SQL="";
    public ProgressDialog progressDialog;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    private RequestQueue mQueue;
    public static Toolbar toolbar;
    public Handler handler;
    public TextView ConnectionSts;
    private GetManagerPendingPO getManagerPendingPO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_approve);

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));

        btnReferData    =   (Button)findViewById(R.id.btnpendingpo);
        mSelectAppno    =   (TextView)findViewById(R.id.txtSelectappno);
        btnViewDetails  =   (Button)findViewById(R.id.btnaviewdetails);

        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        getManagerPendingPO = new GetManagerPendingPO(ManagerApprove.this);


        //=== Database Connection
        sqlliteCreateLeasing_ManagerApprove = new SqlliteCreateLeasing(this);
        sqLiteDatabase_ManagerApprove = sqlliteCreateLeasing_ManagerApprove.getReadableDatabase();



        //===== Data Sync the Manager Approvel PODetails And Image
        //DataSynchronizeManager dataSynchronizeManager = new DataSynchronizeManager(ManagerApprove.this);
        //dataSynchronizeManager.GetPendingPOApprove(LoginUser);
        //==============================================

        checkDataConnectionStatus = new CheckDataConnectionStatus(ManagerApprove.this);

        //===== Create Volly Request Ques. =======
        mQueue          = VollySingleton.getInstance(this).getRequestQueue();
        mPendingAppData = (RecyclerView)findViewById(R.id.rcpapprovepo) ;
        mPendingAppData.setHasFixedSize(true);
        mPendingAppData.setLayoutManager(new LinearLayoutManager(ManagerApprove.this));
        myPenadapter = new Adapter_MgrApprove_app(ManagerApprove.this , getManagerPendingPO.GetManagerApprove());
        mPendingAppData.setAdapter(myPenadapter);

        //==== Delete old Record
        //GetPendingApplicatiobDetails();


        //=== Check Data Connection status Toolbra every 5 second.
        //setSupportActonBar(toolbar);
        ConnectionSts = (TextView)findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();


        //=== Run View Details Button
        btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                boolean IsCheck = checkDataConnectionStatus.IsConnected();
                if (IsCheck == true)
                {
                    Intent intentviewdetails = new Intent("android.intent.action.Po_Web_View");
                    intentviewdetails.putExtra("ApplicationNo" , mSelectAppno.getText());
                    startActivity(intentviewdetails);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManagerApprove.this);
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
        });


        //=== Refersh Dayta Button
        btnReferData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean IsCheck = checkDataConnectionStatus.IsConnected();
                if (IsCheck == true)
                {
                    ManagerPOApproveGet managerPOApproveGet = new ManagerPOApproveGet(ManagerApprove.this);
                    managerPOApproveGet.GetManagerApprovePOData(LoginUser);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManagerApprove.this);
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
        myPenadapter.swapCursor(getManagerPendingPO.GetManagerApprove());

        btnViewDetails.setEnabled(false);
        btnViewDetails.setBackgroundResource(R.drawable.normalbuttondisable);

    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        sqLiteDatabase_ManagerApprove.close();
        sqlliteCreateLeasing_ManagerApprove.close();
        super.onDestroy();
    }

    /*
    public void GetPendingApplicatiobDetails()
    {
        SQLiteDatabase sqLiteDatabaseGetDetails = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursordetails = sqLiteDatabaseGetDetails.rawQuery("Select APP_REF_NO from PO_PENDING" , null);

        if (cursordetails.getCount() != 0)
        {
            if (cursordetails.moveToFirst())
            {
                do {
                    JSONObject jsonObjectappno = new JSONObject();
                    try {
                        jsonObjectappno.put("APPLICATION_REF_NO" , cursordetails.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //String PostUrl = "http://203.115.12.125:82//core//mobnew//MOBILE-PO-GET-APPLICATION-DETAILS.php";
                    String PostUrl = PHP_URL_SQL + "MOBILE-PO-GET-APPLICATION-DETAILS.php";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PostUrl, jsonObjectappno,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    String TempAPPLICATION_REF_NO ="" , TempAP_FACILITY_AMT = "" , TempAP_RENTAL_AMT = "" , TempAP_PERIOD="" , TempAP_RATE="" ,
                                            TempAP_DOWN_PAY="" , TempAP_MK_OFFICER="" , TempCL_FULLY_NAME="" , TempCL_ADDERS_1="" , TempCL_ADDERS_2="" ,
                                            TempCL_ADDERS_3 = "" , TempCL_ADDERS_4="" , TempCL_MOBILE_NO="" , TempCL_OCCUPATION="" , TempAS_EQ_TYPE="" ,
                                            TempAS_EQ_MAKE = "" , TempAS_EQ_MODEL = "" , TempAS_EQ_CHAS_NO="" , TempAS_EQ_ENG_NO="" , TempAS_EQ_YEAR="" , TempMkVal="" ,
                                            Temp_EXP_RATE="" , Temp_product="" , Temp_inv_val="" , Temp_Eq_categery="" ,
                                            Temp_supplier="" , Temp_delera="" , Temp_Intduser="" , Temp_inscompaney="" ,
                                            TempCl_nic="" , TEmp_regno="";

                                    try {
                                        JSONArray jsonArrayappdata = response.getJSONArray("TT-GET-PO-DETAILS");

                                        for (int i =0; i < jsonArrayappdata.length() ; i++)
                                        {
                                            JSONObject jsonObjectmydata =   jsonArrayappdata.getJSONObject(i);
                                            TempAPPLICATION_REF_NO      =   jsonObjectmydata.getString("APPLICATION_REF_NO") ;
                                            TempAP_FACILITY_AMT         =   jsonObjectmydata.getString("AP_FACILITY_AMT") ;
                                            TempAP_RENTAL_AMT           =   jsonObjectmydata.getString("AP_RENTAL_AMT") ;
                                            TempAP_PERIOD               =   jsonObjectmydata.getString("AP_PERIOD") ;
                                            TempAP_RATE                 =   jsonObjectmydata.getString("AP_RATE") ;
                                            TempAP_DOWN_PAY             =   jsonObjectmydata.getString("AP_DOWN_PAY") ;
                                            TempAP_MK_OFFICER           =   jsonObjectmydata.getString("AP_MK_OFFICER") ;
                                            TempCL_FULLY_NAME           =   jsonObjectmydata.getString("CL_FULLY_NAME") ;
                                            TempCL_ADDERS_1             =   jsonObjectmydata.getString("CL_ADDERS_1") ;
                                            TempCL_ADDERS_2             =   jsonObjectmydata.getString("CL_ADDERS_2") ;
                                            TempCL_ADDERS_3             =   jsonObjectmydata.getString("CL_ADDERS_3") ;
                                            TempCL_ADDERS_4             =   jsonObjectmydata.getString("CL_ADDERS_4") ;
                                            TempCL_MOBILE_NO            =   jsonObjectmydata.getString("CL_MOBILE_NO") ;
                                            TempCL_OCCUPATION           =   jsonObjectmydata.getString("CL_OCCUPATION") ;
                                            TempAS_EQ_TYPE              =   jsonObjectmydata.getString("AS_EQ_TYPE") ;
                                            TempAS_EQ_MAKE              =   jsonObjectmydata.getString("AS_EQ_MAKE") ;
                                            TempAS_EQ_MODEL             =   jsonObjectmydata.getString("AS_EQ_MAKE") ;
                                            TempAS_EQ_CHAS_NO           =   jsonObjectmydata.getString("AS_EQ_CHAS_NO") ;
                                            TempAS_EQ_ENG_NO            =   jsonObjectmydata.getString("AS_EQ_ENG_NO") ;
                                            TempAS_EQ_YEAR              =   jsonObjectmydata.getString("AS_EQ_YEAR") ;
                                            TempMkVal                   =   jsonObjectmydata.getString("AP_MKVAL") ;

                                            Temp_EXP_RATE               =   jsonObjectmydata.getString("AP_ETV") ;
                                            Temp_product                =   jsonObjectmydata.getString("AP_PRODUCT") ;
                                            Temp_inv_val                =   jsonObjectmydata.getString("AP_INVOICE_AMT") ;
                                            Temp_Eq_categery            =   jsonObjectmydata.getString("AS_EQ_CATAGE") ;

                                            Temp_supplier               =   jsonObjectmydata.getString("AS_INV_SUPPLIER") ;
                                            Temp_delera                 =   jsonObjectmydata.getString("AS_INV_DELER") ;
                                            Temp_Intduser               =   jsonObjectmydata.getString("AP_INTDU") ;
                                            Temp_inscompaney            =   jsonObjectmydata.getString("AP_INSURANCE_COMPANY") ;

                                            TempCl_nic                  =   jsonObjectmydata.getString("CL_NIC") ;
                                            TEmp_regno                  =   jsonObjectmydata.getString("AS_EQ_REGNO") ;

                                            sqlliteCreateLeasing.PoPendingApplicationDetails(TempAPPLICATION_REF_NO , TempAP_FACILITY_AMT , TempAP_RENTAL_AMT , TempAP_PERIOD , TempAP_RATE
                                            ,TempAP_DOWN_PAY , TempAP_MK_OFFICER , TempCL_FULLY_NAME , TempCL_ADDERS_1 , TempCL_ADDERS_2 , TempCL_ADDERS_3 , TempCL_ADDERS_4 ,  TempCL_MOBILE_NO ,
                                                    TempCL_OCCUPATION , TempAS_EQ_TYPE , TempAS_EQ_MAKE , TempAS_EQ_MODEL , TempAS_EQ_CHAS_NO , TempAS_EQ_ENG_NO , TempAS_EQ_YEAR , TempMkVal , Temp_EXP_RATE, Temp_product, Temp_inv_val ,  Temp_Eq_categery ,
                                                    Temp_supplier , Temp_delera , Temp_Intduser , Temp_inscompaney , TempCl_nic , TEmp_regno  , "NO" , "NO");
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AlertDialog.Builder bmyAlert = new AlertDialog.Builder(ManagerApprove.this);
                            bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            })
                                    .setTitle("AFSL Mobile Leasing.test")
                                    .create();
                            bmyAlert.show();
                        }
                    });

                    mQueue.add(jsonObjectRequest);

                } while (cursordetails.moveToNext());
            }
        }
        cursordetails.close();
    }

    public Cursor GetPendingPOApprove()
    {
        boolean Chksts = checkDataConnectionStatus.IsConnected();
        if (Chksts == true)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("MGR_CODE" , LoginUser);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //String PostUrl = "http://203.115.12.125:82//core//mobnew//MOBILE-GET-APPROVE-PO.php";
            String PostUrlPendingPO = PHP_URL_SQL + "MOBILE-GET-APPROVE-PO.php";

            progressDialog.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PostUrlPendingPO, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try {
                                String mAppno="" , mClnic="" , mClname="" , mAmt="" , mMkofficer="" , mBranchCode="", mPosts="";
                                JSONArray jsonArraygetdata = response.getJSONArray("TT-GET-PENDING-PO");

                                for (int i=0 ; i< jsonArraygetdata.length();i++)
                                {
                                    JSONObject mydataJson = jsonArraygetdata.getJSONObject(i);
                                    mAppno      =   mydataJson.getString("APPLICATION_REF_NO");
                                    mClnic      =   mydataJson.getString("CL_NIC");
                                    mClname     =   mydataJson.getString("CL_FULLY_NAME");
                                    mAmt        =   mydataJson.getString("AP_FACILITY_AMT");
                                    mMkofficer  =   mydataJson.getString("AP_MK_OFFICER");
                                    mBranchCode =   "HO";
                                    mPosts      =   "001";
                                    sqlliteCreateLeasing.CreatePendingPO(mAppno,mClnic,mClname,mAmt,mMkofficer,mBranchCode,mPosts);

                                    //=== Get Application Image Details
                                    GetImageToapprove(mAppno);

                                }
                                progressDialog.dismiss();
                            }catch(Exception e){}
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder bmyAlert = new AlertDialog.Builder(ManagerApprove.this);
                    bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                            .setTitle("AFSL Mobile Leasing.test")
                            .create();
                    bmyAlert.show();
                }
            });

            mQueue.add(jsonObjectRequest);
            GetPendingApplicatiobDetails();
        }

        //==== Get Sqllite date to curser====

        SQLiteDatabase sqLiteDatabasePending = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursorpending = sqLiteDatabasePending.rawQuery("SELECT APP_REF_NO,CLNAME,CL_NIC,AMT,MKOFFICER FROM PO_PENDING WHERE APP_STS = '001' " , null);
        return cursorpending;
    }

    public void GetImageToapprove(String mInpAppno)
    {
        JSONObject jsonObject_inpappno = new JSONObject();
        try {
            jsonObject_inpappno.put("APP_REF_NO" , mInpAppno);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String PostUrlPendingPO_Image = PHP_URL_SQL + "MOBILE-MGR-IMAGE-REQ.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PostUrlPendingPO_Image, jsonObject_inpappno,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            String mAppno="" , mDcoref="" , mdoctype="" , mdocimage="";

                            JSONArray jsonArraygetdata_image = response.getJSONArray("TT-NEW-MGR-IMAGE");

                            for (int i=0 ; i< jsonArraygetdata_image.length();i++)
                            {
                                JSONObject mydataJson = jsonArraygetdata_image.getJSONObject(i);
                                mAppno      =   mydataJson.getString("APPLICATION_REF_NO");
                                mDcoref      =   mydataJson.getString("DOC_REF");
                                mdoctype     =   mydataJson.getString("DOC_TYPE");
                                mdocimage        =   mydataJson.getString("DOC_IMAGE");

                                sqlliteCreateLeasing.CreateManagerImage(mAppno , mDcoref , mdoctype , mdocimage);
                            }
                            progressDialog.dismiss();
                        }catch(Exception e){}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(ManagerApprove.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.test")
                        .create();
                bmyAlert.show();
            }
        });

        mQueue.add(jsonObjectRequest);
        //requestQueue.add(jsonObjectRequest);

    }
    */
}
