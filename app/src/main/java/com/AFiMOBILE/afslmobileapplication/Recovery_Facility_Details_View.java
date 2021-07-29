package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Recovery_Facility_Details_View extends AppCompatActivity
{
    public TextView mFacilityiput ,  mFacno , mFullyName , mFullyName2 , mAdders1 , mAdders2 , mWorkAdders , mWorkAdders1 , mNic , mClcode , mOcupation ,
            mMobileNo , mMobileNo1 , mGenreal , mProduct , mFacamt , mBranch , mDueDate , mRefOffName , mRefOffPhone , mMkOfficer , mMkOffPhoneno ,
            mFlowOffName , mFlowOffPhone , mActivateDate , mExpireDate , mDisbusAmt , mCaplizedAmt , mIntRate , mPeriod , mRentalamt , mFacSts ,
            mContractSts , mnoDownPay , mVehNo , mVehModel , mNoRentalMature , mNoRentaPaid , mNoRentaArrays , mRentalArrays , mInsuranceArrays ,
            mODArrays , mOtherarrays , mTotalArrays , mLastPaidDate , mLastPaidamt , mFutureCap , mFutureInt , mTotalSettlement;

    public Button mBtnGurDetails , mBtnPayment , mBtnNotePaid , mActionTranfer , mActionHistory , mIFRSDetails , mLinkFacility;
    public String mFacilityNo , mType;
    public SqlliteCreateRecovery sqlliteCreateRecovery;
    public ArrayAdapter<String> arrayActionCode;
    public Spinner mActionCode;
    public Cursor cursor_Fac_Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_facility_details_view);


        //=== Get Varible
        Intent intent = getIntent();
        mFacilityNo     =   intent.getStringExtra("FACNO");
        mType           =   intent.getStringExtra("TYPE");

        mFacilityiput  = (TextView)findViewById(R.id.TxtAssingCaseName);
        mFacilityiput.setText("Facility No - " + mFacilityNo);
        sqlliteCreateRecovery = new SqlliteCreateRecovery(this);

        mActionCode  = (Spinner)findViewById(R.id.spnActionUpdate) ;
        //===== Load Action Code
        if (mType.equals("CALL"))
        {
            List<String> labelsActon = new ArrayList<String>();
            labelsActon.add("CALL");
            labelsActon.add("VISIT");
            labelsActon.add(" ");
            arrayActionCode = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsActon);
            arrayActionCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mActionCode.setAdapter(arrayActionCode);
            mActionCode.setSelection(arrayActionCode.getPosition(" "));
        }
        else
        {
            List<String> labelsActon = sqlliteCreateRecovery.GetAllActionCode();
            labelsActon.add(" ");
            if (labelsActon != null) {
                arrayActionCode = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsActon);
                arrayActionCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mActionCode.setAdapter(arrayActionCode);
                mActionCode.setSelection(arrayActionCode.getPosition(" "));
            }
        }

        //==== Link Facility Details
        mLinkFacility = (Button)findViewById(R.id.btnlink_Facility);
        mLinkFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ifrs = new Intent("android.intent.Recovery_Monotrium_List");
                intent_ifrs.putExtra("FacilityNo" , mFacilityNo);
                startActivity(intent_ifrs);
            }
        });

        //=== IFRS DETAILS VIEW
        mIFRSDetails = (Button)findViewById(R.id.btnIfrsDetails) ;
        mIFRSDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ifrs = new Intent("android.intent.Recovery_IFRS_Details");
                intent_ifrs.putExtra("FacilityNo" , mFacilityNo);
                startActivity(intent_ifrs);
            }
        });

        //==== Action Tranfer
        mActionTranfer      =   (Button)findViewById(R.id.btnActionUpdate);
        mActionTranfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent1_Actiontranfer = new Intent("android.intent.action.Recovery_Actin_Tranfer");
                intent1_Actiontranfer.putExtra("FACNO" , mFacilityNo);
                startActivity(intent1_Actiontranfer);
            }
        });

        GetDataLoad();
        LoadPaymentDetails();
        LoadNoteDetails();
        LoadCoAppDetails();
        LoadActionLayout();
        LoadActionHistory();
    }


    public void LoadActionHistory()
    {
        mActionHistory  =   (Button)findViewById(R.id.btnActionHis);
        mActionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_history = new Intent("android.intent.action.Recovery_Action_History");
                intent_history.putExtra("FACNO" , mFacilityNo);
                startActivity(intent_history);
            }
        });
    }

    public void LoadActionLayout()
    {
        mActionCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Log.e("action" , mActionCode.getSelectedItem().toString());
                if (mActionCode.getSelectedItem().toString().equals("CALL"))
                {
                    Intent intent_AssetVerification = new Intent("android.intent.Recovery_Call_Center_Action_Update");
                    intent_AssetVerification.putExtra("FACNO" , mFacilityNo);
                    intent_AssetVerification.putExtra("ACTION" , mActionCode.getSelectedItem().toString());
                    startActivity(intent_AssetVerification);
                }
                else if (mActionCode.getSelectedItem().toString().equals("VISIT"))
                {
                    Intent intent_AssetVerification = new Intent("android.intent.Recovery_Call_Center_Action_Update");
                    intent_AssetVerification.putExtra("FACNO" , mFacilityNo);
                    intent_AssetVerification.putExtra("ACTION" , mActionCode.getSelectedItem().toString());
                    startActivity(intent_AssetVerification);
                }
                else if (mActionCode.getSelectedItem().toString().equals("Asset Verification"))
                {
                    Intent intent_AssetVerification = new Intent("android.intent.action.Recovery_Asset_Verfica_View");
                    intent_AssetVerification.putExtra("FACNO" , mFacilityNo);
                    startActivity(intent_AssetVerification);
                }
                else if (mActionCode.getSelectedItem().toString().equals("Collection Entry"))
                {
                    Intent intent_AssetVerification = new Intent("android.intent.action.Recovery_Collection_Entry_View");
                    intent_AssetVerification.putExtra("FACNO" , mFacilityNo);
                    startActivity(intent_AssetVerification);
                }
                else if (mActionCode.getSelectedItem().toString().equals("Meeting Request"))
                {
                    Intent intent_AssetVerification = new Intent("android.intent.action.Recovery_Meeting_Request_View");
                    intent_AssetVerification.putExtra("FACNO" , mFacilityNo);
                    startActivity(intent_AssetVerification);
                }
                else if (mActionCode.getSelectedItem().toString().equals("Police Complaint"))
                {
                    Intent intent_AssetVerification = new Intent("android.intent.action.Recovery_Police_Complaint_View");
                    intent_AssetVerification.putExtra("FACNO" , mFacilityNo);
                    startActivity(intent_AssetVerification);
                }
                else if (mActionCode.getSelectedItem().toString().equals("PTP"))
                {
                    Intent intent_AssetVerification = new Intent("android.intent.action.Recovery_PTP_Details_View");
                    intent_AssetVerification.putExtra("FACNO" , mFacilityNo);
                    startActivity(intent_AssetVerification);
                }
                else if (mActionCode.getSelectedItem().toString().equals("Refuse to Pay"))
                {
                    Intent intent_AssetVerification = new Intent("android.intent.action.Recovery_Refuse_Pay_View");
                    intent_AssetVerification.putExtra("FACNO" , mFacilityNo);
                    startActivity(intent_AssetVerification);
                }
                else if (mActionCode.getSelectedItem().toString().equals("Hand Over"))
                {
                    Intent intent_AssetVerification = new Intent("android.intent.action.Recovery_Hand_Over_Details");
                    intent_AssetVerification.putExtra("FACNO" , mFacilityNo);
                    startActivity(intent_AssetVerification);
                }
                else if (mActionCode.getSelectedItem().toString() != " ")
                {
                    Intent intent_AssetVerification = new Intent("android.intent.action.Recovery_Comment_Only_View");
                    intent_AssetVerification.putExtra("FACNO" , mFacilityNo);
                    intent_AssetVerification.putExtra("ACTION" , mActionCode.getSelectedItem().toString() );
                    startActivity(intent_AssetVerification);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void onDestroy(){
        sqlliteCreateRecovery.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();


        super.onDestroy();
        finish();
        Log.d("Log", "onDestroy-Application");
    }


    public void LoadCoAppDetails()
    {
        mBtnGurDetails = findViewById(R.id.btnGurDetails);
        mBtnGurDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_payment = new Intent("android.intent.action.Recovery_View_Facility_Other");
                intent_payment.putExtra("TYPE" , "COAPP");
                intent_payment.putExtra("FACNO" , mFacilityNo);
                startActivity(intent_payment);
            }
        });
    }

    public void LoadPaymentDetails()
    {
        mBtnPayment = (Button)findViewById(R.id.btnPaymentDetails);
        mBtnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_payment = new Intent("android.intent.action.Recovery_View_Facility_Other");
                intent_payment.putExtra("TYPE" , "PAYMENT");
                intent_payment.putExtra("FACNO" , mFacilityNo);
                startActivity(intent_payment);
            }
        });
    }

    public void LoadNoteDetails()
    {
        mBtnNotePaid   = findViewById(R.id.btnNitepadDetails);
        mBtnNotePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_payment = new Intent("android.intent.action.Recovery_View_Facility_Other");
                intent_payment.putExtra("TYPE" , "NOTE");
                intent_payment.putExtra("FACNO" , mFacilityNo);
                startActivity(intent_payment);
            }
        });
    }

    public void GetDataLoad()
    {
        // Assign Varible
        mFacno          =       (TextView)findViewById(R.id.TxtAssingCaseName);
        mFullyName      =       (TextView)findViewById(R.id.TxtClfullyName);
        mFullyName2     =       (TextView)findViewById(R.id.TxtClfullyName2);
        mAdders1        =       (TextView)findViewById(R.id.TxtClAdder);
        mAdders2        =       (TextView)findViewById(R.id.TxtClAdder2);
        mWorkAdders     =       (TextView)findViewById(R.id.TxtClworladders);
        mWorkAdders1    =       (TextView)findViewById(R.id.TxtClworladders2);
        mNic            =       (TextView)findViewById(R.id.Txtclnic);
        mClcode         =       (TextView)findViewById(R.id.Txtclcode);
        mOcupation      =       (TextView)findViewById(R.id.TxtFacOcupation);

        mMobileNo       =       (TextView)findViewById(R.id.TxtFacMobileNo1);
        mMobileNo1      =       (TextView)findViewById(R.id.TxtFacMobileNo2);
        mGenreal        =       (TextView)findViewById(R.id.TxtFacGender);
        mProduct        =       (TextView)findViewById(R.id.TxtFacProduct);
        mFacamt         =       (TextView)findViewById(R.id.TxtFacAmt);
        mBranch         =       (TextView)findViewById(R.id.TxtFacBranch);
        mDueDate        =       (TextView)findViewById(R.id.TxtFacDueDate);
        mRefOffName     =       (TextView)findViewById(R.id.TxtFacRecCode);
        mRefOffPhone    =       (TextView)findViewById(R.id.TxtFacRecPhoneno);
        mMkOfficer      =       (TextView)findViewById(R.id.TxtFacMakOfficer);
        mMkOffPhoneno   =       (TextView)findViewById(R.id.TxtFacMakPhoneNo);

        mFlowOffName    =       (TextView)findViewById(R.id.TxtFacFollwOfficer);
        mFlowOffPhone   =       (TextView)findViewById(R.id.TxtFacFollwOPhoneNo);
        mActivateDate   =       (TextView)findViewById(R.id.TxtFacActDate);
        mExpireDate     =       (TextView)findViewById(R.id.TxtFacExpireDate);
        mDisbusAmt      =       (TextView)findViewById(R.id.TxtFacDisamt);
        mCaplizedAmt    =       (TextView)findViewById(R.id.TxtFacCaplizedAmt);
        mIntRate        =       (TextView)findViewById(R.id.TxtFacIntRate);
        mPeriod         =       (TextView)findViewById(R.id.TxtFacPeriod);
        mRentalamt      =       (TextView)findViewById(R.id.TxtFacRentalamt);
        mFacSts         =       (TextView)findViewById(R.id.TxtFacFacsts);

        mContractSts    =       (TextView)findViewById(R.id.TxtFacConsts);
        mnoDownPay      =       (TextView)findViewById(R.id.TxtFacNoDown);
        mVehNo          =       (TextView)findViewById(R.id.TxtFacVehno);
        mVehModel       =       (TextView)findViewById(R.id.TxtFacAssModel);
        mNoRentalMature =       (TextView)findViewById(R.id.TxtFacRentalMat);
        mNoRentaPaid    =       (TextView)findViewById(R.id.TxtFacRentaPaid);
        mNoRentaArrays  =       (TextView)findViewById(R.id.TxtFacNoRentalArrays);
        mRentalArrays   =       (TextView)findViewById(R.id.TxtFacNoRentalArraysamt);
        mInsuranceArrays =       (TextView)findViewById(R.id.TxtFacNoInsArraysamt);

        mODArrays       =       (TextView)findViewById(R.id.TxtFacNoODArraysamt);
        mOtherarrays    =       (TextView)findViewById(R.id.TxtFacNoOtherArraysamt);
        mTotalArrays    =       (TextView)findViewById(R.id.TxtFacNoTotalArraysamt);
        mLastPaidDate   =       (TextView)findViewById(R.id.TxtFacLastPaidDate);
        mLastPaidamt    =       (TextView)findViewById(R.id.TxtFacLastPaidAmt);
        mFutureCap      =       (TextView)findViewById(R.id.TxtFacFutCapital);
        mFutureInt      =       (TextView)findViewById(R.id.TxtFacFutInterest);
        mTotalSettlement    =       (TextView)findViewById(R.id.TxtFacTotSettlement);

        //=== Get Value  ===========

        SQLiteDatabase sqLiteDatabase_GetFacility_Data = sqlliteCreateRecovery.getReadableDatabase();

        if (mType.equals("SEARCH"))
        {
            cursor_Fac_Data = sqLiteDatabase_GetFacility_Data.rawQuery("SELECT * FROM recovery_search_data WHERE TRIM (Facility_Number) = '" + mFacilityNo + "'" , null);
        }
        else if (mType.equals("CALL"))
        {
            cursor_Fac_Data = sqLiteDatabase_GetFacility_Data.rawQuery("SELECT * FROM CallCenter_recovery_generaldetail WHERE TRIM (Facility_Number) = '" + mFacilityNo + "'" , null);
        }else
        {
            cursor_Fac_Data = sqLiteDatabase_GetFacility_Data.rawQuery("SELECT * FROM recovery_generaldetail WHERE TRIM (Facility_Number) = '" + mFacilityNo + "'" , null);
        }

        if (cursor_Fac_Data.getCount() != 0)
        {
            cursor_Fac_Data.moveToFirst();

            DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");


            mProduct.setText(cursor_Fac_Data.getString(1));
            mVehModel.setText(cursor_Fac_Data.getString(2));
            mVehNo.setText(cursor_Fac_Data.getString(3));
            mDueDate.setText(cursor_Fac_Data.getString(4));

            mODArrays.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(6).replace("," , ""))));
            mTotalArrays.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(7).replace("," , ""))));
            mLastPaidDate.setText(cursor_Fac_Data.getString(8));
            mLastPaidamt.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(9).replace("," , ""))));
            String mTemp1 = (cursor_Fac_Data.getString(10));
            String mTemp2 = (cursor_Fac_Data.getString(11));
            mClcode.setText(cursor_Fac_Data.getString(12));
            mNic.setText(cursor_Fac_Data.getString(13));
            mGenreal.setText(cursor_Fac_Data.getString(15));
            mOcupation.setText(cursor_Fac_Data.getString(16));
            mWorkAdders.setText(cursor_Fac_Data.getString(17));
            mWorkAdders1.setText(cursor_Fac_Data.getString(18));
            mMobileNo.setText(cursor_Fac_Data.getString(19));
            mMobileNo1.setText(cursor_Fac_Data.getString(20));
            String Temp3 = cursor_Fac_Data.getString(21);
            mAdders1.setText(cursor_Fac_Data.getString(22) +","+ cursor_Fac_Data.getString(23) );
            mAdders2.setText(cursor_Fac_Data.getString(24) +","+ cursor_Fac_Data.getString(25) );
            String PostalTown = cursor_Fac_Data.getString(26);
            String PerAdders = cursor_Fac_Data.getString(27) + cursor_Fac_Data.getString(28) + cursor_Fac_Data.getString(29) + cursor_Fac_Data.getString(30);
            mFacamt.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(33).replace("," ,""))));
            mBranch.setText(cursor_Fac_Data.getString(34));
            mPeriod.setText(cursor_Fac_Data.getString(35));
            mRefOffName.setText(cursor_Fac_Data.getString(37));
            mMkOfficer.setText(cursor_Fac_Data.getString(39));
            mFlowOffName.setText(cursor_Fac_Data.getString(40));
            mRefOffPhone.setText(cursor_Fac_Data.getString(41));
            mMkOffPhoneno.setText(cursor_Fac_Data.getString(43));
            mFlowOffPhone.setText(cursor_Fac_Data.getString(44));
            mActivateDate.setText(cursor_Fac_Data.getString(45));
            mExpireDate.setText(cursor_Fac_Data.getString(46));
            mDisbusAmt.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(48).replace("," , ""))));
            mIntRate.setText(cursor_Fac_Data.getString(50));
            mRentalamt.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(51).replace("," , ""))));
            mContractSts.setText(cursor_Fac_Data.getString(52));
            mFacSts.setText(cursor_Fac_Data.getString(53));
            mnoDownPay.setText(cursor_Fac_Data.getString(54));
            mNoRentalMature.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(55).replace("," , ""))));
            mNoRentaPaid.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(56).replace("," , ""))));
            mNoRentaArrays.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(57).replace("," , ""))));
            mRentalArrays.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(62).replace("," , ""))));
            mInsuranceArrays.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(65).replace("," , ""))));
            mOtherarrays.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(68).replace("," , ""))));
            mFutureCap.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(69).replace("," , ""))));
            mFutureInt.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(70).replace("," , ""))));
            mTotalSettlement.setText(nf.format(Double.parseDouble(cursor_Fac_Data.getString(71).replace("," , ""))));

            if (cursor_Fac_Data.getString(14).length() < 36)
            {
                mFullyName.setText(cursor_Fac_Data.getString(14));
            }
            else
            {
                mFullyName.setText(cursor_Fac_Data.getString(14).substring(0 , 35));
                mFullyName2.setText(cursor_Fac_Data.getString(14).substring(36));
            }

        }
        cursor_Fac_Data.close();
        sqLiteDatabase_GetFacility_Data.close();
    }

}
