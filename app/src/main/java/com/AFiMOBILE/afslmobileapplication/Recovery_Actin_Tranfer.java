package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.AFiMOBILE.afslmobileapplication.R;


public class Recovery_Actin_Tranfer extends AppCompatActivity
{
    public Button mSearchFacilty , mManagerAssign , mRequestResponce , mRepocessAssing , mRecoveryVisPlan , mRecoveryDistance , mActionHistory , mBtnCallcenter;
    public String mFacilityNo;
    public TextView mInputFAciliotyNo;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_action_tranfer);

        //=== Get Varible
        Intent intent = getIntent();
        mFacilityNo     =   intent.getStringExtra("FACNO");

        mInputFAciliotyNo = (TextView)findViewById(R.id.TxtAssingFacno);
        mInputFAciliotyNo.setText(mFacilityNo);

        mManagerAssign      =   (Button)findViewById(R.id.btnMnagerTranfer);
        mRepocessAssing     =   (Button)findViewById(R.id.btnRepocess);
        mActionHistory      =   (Button)findViewById(R.id.btnActionHistry);
        mBtnCallcenter      =   (Button)findViewById(R.id.btncallcenter);

        //=== Call Center Details
        mBtnCallcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_history = new Intent("android.intent.Recovery_Call_action_home");
                startActivity(intent_history);
            }
        });

        if (mInputFAciliotyNo.getText().equals(""))
        {
            mManagerAssign.setEnabled(false);
            mRepocessAssing.setEnabled(false);
            mActionHistory.setEnabled(false);
            mManagerAssign.setBackgroundResource(R.drawable.normalbuttondisable);
            mRepocessAssing.setBackgroundResource(R.drawable.normalbuttondisable);
            mActionHistory.setBackgroundResource(R.drawable.normalbuttondisable);
        }
        else
        {
            mManagerAssign.setEnabled(true);
            mRepocessAssing.setEnabled(true);
            mActionHistory.setEnabled(true);
            mManagerAssign.setBackgroundColor(Color.parseColor("#720b98"));
            mRepocessAssing.setBackgroundColor(Color.parseColor("#720b98"));
            mActionHistory.setBackgroundColor(Color.parseColor("#720b98"));
        }

        //=== Load Screen
        LoadLayout();
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

    public void LoadLayout()
    {
        //=== Action History
        mActionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_history = new Intent("android.intent.action.Recovery_Action_History");
                intent_history.putExtra("FACNO" , mFacilityNo);
                startActivity(intent_history);

            }
        });


        //=== Search Facility
        mSearchFacilty  =   (Button)findViewById(R.id.btnSerchOnline);
        mSearchFacilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_SearchFacility = new Intent("android.intent.action.Recovery_Search_Facility");
                intent_SearchFacility.putExtra("FacilityNo" , "");
                startActivity(intent_SearchFacility);
            }
        });

        //=== Manager Assign

        mManagerAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_ManagerAssign = new Intent("android.intent.action.Recovery_Manager_Assign");
                intent_ManagerAssign.putExtra("FACNO" , mFacilityNo);
                startActivity(intent_ManagerAssign);
            }
        });

        //=== Responce Manager
        mRequestResponce    =   (Button)findViewById(R.id.btnRequestFacility);
        mRequestResponce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_ManagerAssign = new Intent("android.intent.action.Recovery_Request_Fac_List");
                startActivity(intent_ManagerAssign);
            }
        });


        //=== Assign Repocess

        mRepocessAssing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_RepocessTranser = new Intent("android.intent.action.Recovery_Repocess_Transer");
                intent_RepocessTranser.putExtra("FACNO" , mFacilityNo);
                startActivity(intent_RepocessTranser);
            }
        });

        //=== Recovery Visit Plan
        mRecoveryVisPlan = (Button)findViewById(R.id.btnRecoveryVistPlan);
        mRecoveryVisPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_RepocessTranser = new Intent("android.intent.action.Recovery_Visit_Plan_Facility");
                startActivity(intent_RepocessTranser);
            }
        });

        //=== Recovery Distance Update
        mRecoveryDistance = (Button)findViewById(R.id.btnRecoveryDistance);
        mRecoveryDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_RepocessTranser = new Intent("android.intent.Travel_Distance_updte");
                startActivity(intent_RepocessTranser);
            }
        });

    }

}
