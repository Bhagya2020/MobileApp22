package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.internal.operators.OperatorBufferWithTime;

public class Recovery_Call_action_home  extends AppCompatActivity {

    private Button mBtnPtp , mBTnUncontact , mBrPTP , mMyWallet;
    private ImageButton mImgBtnMassterUpdte , mImgDatasync;
    private String LoginUser , LoginDate , LoginBranch , LoginUserName , mBranchCode ;
    private SqlliteCreateLeasing sqlliteCreateLeasing_call;
    private SqlliteCreateRecovery sqlliteCreateRecovery_count;
    private TextView mCountPTP , mCountUncontact , mCountBroken , mActionCall , mActionVisit , mActionPaid;
    private Integer mCurPTP , mCurUncontact , mBRokenPTP , mCallCount , mVistCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_center_action);

        mBtnPtp = (Button)findViewById(R.id.btnptp);
        mBTnUncontact = (Button)findViewById(R.id.btnuncontact);
        mBrPTP = (Button)findViewById(R.id.btnbrokenptpdetails);
        mMyWallet = (Button)findViewById(R.id.btnmycase) ;


        mImgBtnMassterUpdte =   (ImageButton)findViewById(R.id.Imgupdate);
        mImgDatasync =   (ImageButton)findViewById(R.id.ImgDataSync);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();

        sqlliteCreateLeasing_call = new SqlliteCreateLeasing(this);

        sqlliteCreateRecovery_count = new SqlliteCreateRecovery(this);

        SQLiteDatabase sqLiteDatabase_call = sqlliteCreateLeasing_call.getReadableDatabase();
        Cursor cursor_data = sqLiteDatabase_call.rawQuery("select BRANCH_CODE from USER_MANAGEMENT where OFFIER_ID = '" + LoginUser + "'" , null);
        if (cursor_data.getCount() != 0)
        {
            cursor_data.moveToFirst();
            mBranchCode = cursor_data.getString(0);

        }

        //==== Load Count Data
        GetCountDetails();


        //=== Load PTP details
        mBtnPtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //=== Load Details Screen
        mBtnPtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_broken = new Intent("android.intent.Recovery_Call_Fac_Details");
                intent_broken.putExtra("TYPE" , "PTP");
                startActivity(intent_broken);
            }
        });

        mBTnUncontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_broken = new Intent("android.intent.Recovery_Call_Fac_Details");
                intent_broken.putExtra("TYPE" , "UNCONTACT");
                startActivity(intent_broken);
            }
        });


        mBrPTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_broken = new Intent("android.intent.Recovery_Call_Fac_Details");
                intent_broken.putExtra("TYPE" , "BROKEN");
                startActivity(intent_broken);
            }
        });

        mMyWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_broken = new Intent("android.intent.Call_center_mywallet_main");
                startActivity(intent_broken);
            }
        });

        //========================================================

        //=== Get Call CenterData - Branch Wise
        mImgDatasync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Volly_Recovery_Get_DataSync volly_recovery_get_dataSync = new Volly_Recovery_Get_DataSync(Recovery_Call_action_home.this);
                volly_recovery_get_dataSync.GetCallCenterData(mBranchCode);
            }
        });
    }

    private void GetCountDetails()
    {

        mCountPTP           =   (TextView)findViewById(R.id.txtcurrentptp) ;
        mCountUncontact     =   (TextView)findViewById(R.id.txtuncontact) ;
        mCountBroken        =   (TextView)findViewById(R.id.txtbrokenPTP) ;

        mCurPTP = 0; mCurUncontact = 0; mBRokenPTP=0; mCallCount = 0 ; mVistCount = 0;
        SQLiteDatabase sqLiteDatabase_count = sqlliteCreateRecovery_count.getReadableDatabase() ;
        Cursor cursor_count = sqLiteDatabase_count.rawQuery("select CALL_ACTION_CODE , CALL_PTP_DATE , FACNO from recovery_call_center_action where ALOCATE_BRANCH = '" + mBranchCode + "' and ACTION_STS = ''" ,null);
        if (cursor_count.getCount() != 0)
        {
            cursor_count.moveToFirst();
            do {
                if (cursor_count.getString(0).length() != 0)
                {
                    if (cursor_count.getString(0).equals("001") || cursor_count.getString(0).equals("002") || cursor_count.getString(0).equals("002") )
                    {
                        mCurPTP = mCurPTP + 1;
                    }

                    if (cursor_count.getString(0).equals("006") || cursor_count.getString(0).equals("007") || cursor_count.getString(0).equals("004") ||
                            cursor_count.getString(0).equals("011") )
                    {
                        mCurUncontact = mCurUncontact + 1;
                    }
                }


                //== get broken ptp
                if (cursor_count.getString(1).length() != 0)
                {
                    Date dateptp=new Date();
                    Date datecurrent=new Date();
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String datenow=cursor_count.getString(1);
                    try {
                        dateptp = (Date) formatter.parse(datenow);
                        datecurrent = (Date) formatter.parse(LoginDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long diff_date = datecurrent.getTime() - dateptp.getTime();
                    int diff = (int) (diff_date / (24 * 60 * 60 * 1000));

                    if (diff>0)
                    {
                        mBRokenPTP = mBRokenPTP + 1;
                    }

                }



            }while (cursor_count.moveToNext());
        }

        mCountPTP.setText(String.valueOf(mCurPTP));
        mCountUncontact.setText(String.valueOf(mCurUncontact));
        mCountBroken.setText(String.valueOf(mBRokenPTP));
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        sqlliteCreateLeasing_call.close();
        sqlliteCreateRecovery_count.close();
        super.onDestroy();
    }
}
