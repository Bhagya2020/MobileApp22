package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class Recovery_Home extends AppCompatActivity {

    public ImageButton ImgMyWallet , ImgHomeUpdate , ImgDataSyns , ImgRecoveryAction , ImgVisiplan , ImgInformtion;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public String PHP_URL_SQL , LoginUser , LoginDate , LoginUserName , LoginBranch;
    public TextView mLoginName;
    public SqlliteCreateRecovery sqlliteCreateRecovery_LeasingHome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_home);

        //=== Class Define ========================
        checkDataConnectionStatus = new CheckDataConnectionStatus(this);
        sqlliteCreateRecovery_LeasingHome = new SqlliteCreateRecovery(this);

        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName = globleClassDetails.getOfficerName();
        //===========================================

        //=== Assign Varible   ======
        mLoginName = (TextView)findViewById(R.id.TxtLoagiName);
        mLoginName.setText(LoginUserName);


        //==== Memory Check
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));


        //===== Process Run ====
        GetChartData();
        UpdateRecoveryMaster();   // Master Update
        RecoveryDataSync();       // Recovery Officer Data Get


        ImgMyWallet = (ImageButton)findViewById(R.id.imaBtnWALLET);
        ImgMyWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_mywallet = new Intent("android.intent.action.Recovery_Cash_Wallet");
                startActivity(intent_mywallet);
            }
        });

        //=== Load Recovery Action
        ImgRecoveryAction   =   (ImageButton)findViewById(R.id.imaBtnACTION);
        ImgRecoveryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_mywallet = new Intent("android.intent.action.Recovery_Mywallet_Main");
                startActivity(intent_mywallet);
            }
        });

        ImgVisiplan  =   (ImageButton)findViewById(R.id.imgBtnPAYMENT);
        ImgVisiplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_visitplan = new Intent("android.intent.action.Recovery_Visit_Plan_Facility");
                startActivity(intent_visitplan);
            }
        });

        ImgInformtion   =   (ImageButton)findViewById(R.id.imgBtnINFFOR);
        ImgInformtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_rec_infor = new Intent("android.intent.action.Recovery_Informaton");
                startActivity(intent_rec_infor);
            }
        });
    }


    public void RecoveryDataSync()
    {
        ImgDataSyns = (ImageButton)findViewById(R.id.ImgDataSync);
        ImgDataSyns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (checkDataConnectionStatus.IsConnected() == true)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Home.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to Request Update ?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        Volly_Recovery_Get_DataSync volly_recovery_get_dataSync = new Volly_Recovery_Get_DataSync(Recovery_Home.this);
                        volly_recovery_get_dataSync.GetOffierData(LoginUser);

                        //Volly_Recovery_Post_DataSync volly_recovery_post_dataSync = new Volly_Recovery_Post_DataSync(Recovery_Home.this);
                        //volly_recovery_post_dataSync.PostOfficerData(LoginUser);


                        /*
                        Volly_Request_Recovery_Data volly_request_recovery_getdata = new Volly_Request_Recovery_Data(Recovery_Home.this);

                        //=== Get Recovery Data - DONE
                       volly_request_recovery_getdata.GetRecoveryGenrealData(LoginUser);

                        //=== Post Action Code - DONE
                       volly_request_recovery_getdata.PostRecoveryActionData();

                        //=== Get Manager Assign Data - DONE
                        volly_request_recovery_getdata.GetRequestManagerAction(LoginUser);

                        //=== Post Manager Responces - DONE
                       volly_request_recovery_getdata.PostManagerResponces(LoginUser);

                        //=== Post Repocess Data - DONE
                       volly_request_recovery_getdata.PostRepocessData(LoginUser);


                        //=== Post Travelling Distance Data
                        volly_request_recovery_getdata.PostTravellingDistacne(LoginUser);

                        */

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Home.this);
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

    public void UpdateRecoveryMaster()
    {
        ImgHomeUpdate = (ImageButton)findViewById(R.id.Imgupdate);
        ImgHomeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Home.this);
                builderdelete.setTitle("AFiMobile-Leasing");
                builderdelete.setMessage("Are you sure to Request Update ?");
                builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Volly_Request_Recovery_Data volly_request_recovery_data = new Volly_Request_Recovery_Data(Recovery_Home.this);
                        volly_request_recovery_data.GetRecoveryMasterData();

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

    protected void onStart()
    {
        super.onStart();

        //=== Check Last sync Date
        SQLiteDatabase sqLiteDatabase_check_datasync = sqlliteCreateRecovery_LeasingHome.getReadableDatabase();
        Cursor cursor_get_sync = sqLiteDatabase_check_datasync.rawQuery("SELECT * FROM masr_data_sync_update WHERE sync_date = '" + LoginDate + "' AND sync_type = 'Y'" , null );
        if (cursor_get_sync.getCount() == 0)
        {
            AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Home.this);
            builderdelete.setTitle("AFiMobile-Leasing");
            builderdelete.setMessage("Please synchronized data. Do you want synchronized  ?");
            builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                    Volly_Recovery_Get_DataSync volly_recovery_get_dataSync = new Volly_Recovery_Get_DataSync(Recovery_Home.this);
                    volly_recovery_get_dataSync.GetOffierData(LoginUser);


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


        //mApp.setText("");
    }


    public void GetChartData()
    {
        //====== Get Visit Count Details ====
        String [] months = {"Visits","Pending"};

        int TotlaCount=0 , TotalVist=0 ;
        float mTotalArraysAmt=0 , mTotalPaidAmt=0;
        //=== Get total Case
        SQLiteDatabase sqLiteDatabase_getChatData = sqlliteCreateRecovery_LeasingHome.getReadableDatabase();
        Cursor cursor_TotalCount = sqLiteDatabase_getChatData.rawQuery("SELECT Total_Arrear_Amount , Last_Payment_Amount FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "'" , null);
        TotlaCount = cursor_TotalCount.getCount();
        if (cursor_TotalCount.getCount() != 0)
        {
            cursor_TotalCount.moveToFirst();
            do {

                if (cursor_TotalCount.getString(0).equals(""))
                {
                    mTotalArraysAmt = 0;
                }
                else
                {
                    mTotalArraysAmt     =   mTotalArraysAmt + (Float.parseFloat(cursor_TotalCount.getString(0).replace("," , "")));
                }

                if (cursor_TotalCount.getString(1).equals(""))
                {
                    mTotalPaidAmt = 0;
                }
                else
                {
                    mTotalPaidAmt       =   mTotalPaidAmt + (Float.parseFloat(cursor_TotalCount.getString(1).replace("," , "")));
                }
            }while (cursor_TotalCount.moveToNext());
        }

        Cursor cursor_visit_case = sqLiteDatabase_getChatData.rawQuery("SELECT * FROM nemf_form_updater WHERE MADE_BY = '" + LoginUser + "'" , null  );
        TotalVist = cursor_visit_case.getColumnCount();
        int [] eraning = {TotlaCount , TotalVist};

        List<PieEntry> pieEntriesVisit = new ArrayList<>();
        for (int i=0 ; i < months.length;i++)
        {
            pieEntriesVisit.add(new PieEntry(eraning[i] , months[i] ));
        }

        final int[] MY_COLORS = {Color.rgb(231,247,25), Color.rgb(18,197,7)};
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c: MY_COLORS) colors.add(c);

        PieDataSet dataSet = new PieDataSet(pieEntriesVisit , "Month Visits");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart)findViewById(R.id.VisteChart);
        chart.setEntryLabelColor(Color.WHITE);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
        //===========================

        //==== Get Collection Chart

        //---- Total Amount update --

        String [] collection = {"Total Arrears","Total Collections"};
        Float [] coltiondata = {mTotalArraysAmt , mTotalPaidAmt};

        List<PieEntry> pieEntriescollection = new ArrayList<>();
        for (int i=0 ; i < collection.length;i++)
        {
            pieEntriescollection.add(new PieEntry( (float)coltiondata[i] , collection[i]));
        }

        PieDataSet dataSetCollection = new PieDataSet(pieEntriescollection , "Month Collections");
        dataSetCollection.setColors(colors);
        PieData dataSetcol = new PieData(dataSetCollection);

        PieChart chart_col = (PieChart)findViewById(R.id.CollectionPieChar);
        chart_col.setData(dataSetcol);
        chart_col.setEntryLabelColor(Color.WHITE);
        chart_col.animateY(1000);
        chart_col.invalidate();
    }

    protected void onDestroy(){

        sqlliteCreateRecovery_LeasingHome.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

}
