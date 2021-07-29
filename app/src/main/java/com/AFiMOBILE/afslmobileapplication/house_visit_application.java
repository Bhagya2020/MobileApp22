package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.renderer.scatter.ChevronUpShapeRenderer;


public class house_visit_application extends AppCompatActivity {

    private RecyclerView mPendingVisit;
    public static TextView mVISITSELAPPNO;
    public SqlliteCreateLeasing sqlliteCreateLeasing = new SqlliteCreateLeasing(this);
    private Button mBtnViewDEtails;
    public String LoginUser="" , LoginDate="" , LoginBranch="" , PHP_URL_SQL="";
    private Adapter_SubmitApplication mAdapter;
    public static Toolbar toolbar;
    public Handler handler;
    public TextView ConnectionSts;
    public CheckDataConnectionStatus checkDataConnectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_visit_application);

        mPendingVisit       =   (RecyclerView)findViewById(R.id.rcppendingsubmit);
        mVISITSELAPPNO        =   (TextView)findViewById(R.id.txtSelectAppnpo);
        mBtnViewDEtails     =   (Button)findViewById(R.id.btnviewdetails);

        //=== Globle Varible Details..
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        checkDataConnectionStatus = new CheckDataConnectionStatus(this);

        //=== Check Data Connection status Toolbra every 5 second.
        ConnectionSts = (TextView)findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();


        mPendingVisit.setHasFixedSize(true);
        mPendingVisit.setLayoutManager(new LinearLayoutManager(house_visit_application.this));
        mAdapter = new Adapter_SubmitApplication(house_visit_application.this , GetVisitapplication());
        mPendingVisit.setAdapter(mAdapter);

        mBtnViewDEtails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_details = new Intent("android.intent.action.House_Visit_update");
                intent_details.putExtra("ApplicationNo" , mVISITSELAPPNO.getText());
                startActivity(intent_details);
            }
        });
    }

    protected void onStart()
    {
        super.onStart();
        mAdapter.swapCursor(GetVisitapplication());
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


    public Cursor GetVisitapplication()
    {
        String mAppno="" , mClanme="", mFacamt="" , mNic="";
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"Appno" , "ClientName" , "Amount" , "ClNic" , "APPTYPE"});
        startManagingCursor(mOutPutcouser);

        SQLiteDatabase sqLiteDatabase_complete = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursor_complete = sqLiteDatabase_complete.rawQuery("SELECT APPLICATION_REF_NO , AP_FACILITY_AMT FROM LE_APPLICATION WHERE AP_MK_OFFICER = '" + LoginUser + "'" , null );
        if (cursor_complete.getCount() != 0)
        {
            cursor_complete.moveToFirst();
            do
            {
                Cursor cursor_check_geo = sqLiteDatabase_complete.rawQuery("SELECT * FROM LE_GEO_TAG_UPDATE WHERE APP_REF_NO = '" +  cursor_complete.getString(0) + "' and SERVER_SEND = 'Y'" , null);
                if (cursor_check_geo.getCount()==0)
                {
                    mAppno   =  cursor_complete.getString(0);
                    mFacamt  =  cursor_complete.getString(1);

                    Cursor cursor_client = sqLiteDatabase_complete.rawQuery("SELECT CL_TITLE , CL_NIC , CL_FULLY_NAME FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO ='" + mAppno + "'" , null);
                    if (cursor_client.getCount() != 0)
                    {
                        cursor_client.moveToFirst();
                        mClanme  =  cursor_client.getString(0) + " " + cursor_client.getString(2);
                        mNic     =  cursor_client.getString(1);

                    }
                    mOutPutcouser.addRow(new String[] {mAppno , mClanme , mFacamt , mNic , "VISIT"});
                }

            }while (cursor_complete.moveToNext());
        }
        return mOutPutcouser;
    }


    @Override
    protected void onDestroy() {

        sqlliteCreateLeasing.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }
}
