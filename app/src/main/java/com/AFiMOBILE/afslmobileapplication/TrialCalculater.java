package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class TrialCalculater extends AppCompatActivity {

    TextView tv , tv_sucess ;
    EditText CalRentalAmount , mExpRate , mClconAmt ,  mInvoiceAmot , mLeasingAmt , mClientDown , mRate , mPerid , mIndAmt , mServiceAmt , mRmvAmt ,mInsAmt , mTrpAmt , mOthAmt ;
    Switch mIndCap , mSchCap , mRMVCap ,  mInsCap , mTransport , mOthCha ;
    String ErrorDescription;
    private TextView mFacilieAmt;
    View layout_sucress , layout_error;
    Button mCalculate , mDarftApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leasing_trial_cal);

        mLeasingAmt     =   findViewById(R.id.txtinvoiceamt);
        mClientDown     =   findViewById(R.id.txtclientcontbution);
        mRate           =   findViewById(R.id.txtrate);
        mPerid          =   findViewById(R.id.txtpeiod);
        mIndAmt         =   findViewById(R.id.txtindcom);
        mServiceAmt     =   findViewById(R.id.txtservicecharge);
        mRmvAmt         =   findViewById(R.id.txtrmvcharge);
        mInsAmt         =   findViewById(R.id.txtinsurancecharge);
        mTrpAmt         =   findViewById(R.id.txttransportcharge);
        mOthAmt         =   findViewById(R.id.txtothercharge);
        mFacilieAmt     =   findViewById(R.id.txtleasingamt);



        mInvoiceAmot    =   findViewById(R.id.txtinvoiceamt);
        mClconAmt       =   findViewById(R.id.txtclientcontbution);
        mExpRate        =   findViewById(R.id.txtexpourse);
        mCalculate      =   findViewById(R.id.btncalRental);

        CalRentalAmount = findViewById(R.id.txtrental);


        mIndCap         =   findViewById(R.id.SwtindCap);
        mSchCap         =   findViewById(R.id.SwtschCap);
        mRMVCap         =   findViewById(R.id.SwtrmvCap);
        mInsCap         =   findViewById(R.id.SwtincCap);
        mTransport      =   findViewById(R.id.SwttrfCap);
        mOthCha         =   findViewById(R.id.SwtothCap);

        mDarftApp       =   findViewById(R.id.btndraftapp);


        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);



        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculate();
            }
        });


        mDarftApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_draft = new Intent("android.intent.action.Draft_Application");
                startActivity(intent_draft);
            }
        });


        mInvoiceAmot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CalFacilityAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mClientDown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CalFacilityAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void CalFacilityAmount()
    {
        double GetInvAm=0.00;
        double GetClientDown=0.00;
        double GetFacilityAmt=0.00;

        try
        {
            DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");

            if (mInvoiceAmot.length() != 0)
            {
                GetInvAm = Double.parseDouble(mInvoiceAmot.getText().toString().replace("," , ""));
            }

            if (mClientDown.length() != 0)
            {
                GetClientDown = Double.parseDouble(mClientDown.getText().toString());
            }

            GetFacilityAmt = (GetInvAm - GetClientDown);

            Log.d("facamt " , String.valueOf(GetFacilityAmt) );
            mFacilieAmt.setText(nf.format(GetFacilityAmt));
        }
        catch (NumberFormatException e)
        {

        }
    }

    public void Calculate()
    {
        //===== Charges Varible ========

        Double mInvoiceAmt=0.00;
        Double mClientCont=0.00;

        Double mRentalamount = 0.00;
        Double mTotalCharge = 0.00;
        Double mInterestRate = 0.00;
        Double mLoanAmount = 0.00;
        Double mLoanPerid = 0.00;
        Double mFacilityAmount = 0.00;

        Double IntduserCharge = 0.00;
        Double ServiceCharge = 0.00;
        Double RMVCharge = 0.00;
        Double Inscharge = 0.00;
        Double TranportCharge = 0.00;
        Double Othercharge = 0.00;
        Double Stampcharge = 0.00;

        if (mInvoiceAmot.length() == 0 )
        {
            tv.setText("<Invoice Amount> Cannot be Zero.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mRate.length() == 0)
        {
            tv.setText("<<Rate> Cannot be Zero.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mPerid.length() == 0)
        {
            tv.setText("<Period> Cannot be Zero.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");
            if (mFacilieAmt.length() != 0)
            {
                mFacilityAmount = Double.parseDouble(mFacilieAmt.getText().toString().replace("," , ""));
            }

            if (mRate.length() != 0) {
                mInterestRate = Double.parseDouble(mRate.getText().toString());
            }

            if (mPerid.length() != 0) {
                mLoanPerid = Double.parseDouble(mPerid.getText().toString());
            }

            //=== Intduser Commsion
            if (mIndAmt.length() != 0)
            {
                if (mIndCap.isChecked()) {
                    IntduserCharge = Double.parseDouble(mIndAmt.getText().toString());
                }
            }

            //=== Service Charges
            if (mServiceAmt.length() != 0)
            {
                if (mSchCap.isChecked()) {
                    ServiceCharge = Double.parseDouble(mServiceAmt.getText().toString());
                }
            }

            //=== RMV Charges
            if (mRmvAmt.length() != 0)
            {
                if (mRMVCap.isChecked()) {
                    RMVCharge = Double.parseDouble(mRmvAmt.getText().toString());
                }
            }

            //=== Insurance Amt
            if (mInsAmt.length() != 0)
            {
                if (mInsCap.isChecked()) {
                    Inscharge = Double.parseDouble(mInsAmt.getText().toString());
                }
            }

            //=== Tranport Charges
            if (mTrpAmt.length() != 0)
            {
                if (mTransport.isChecked()) {
                    TranportCharge = Double.parseDouble(mTrpAmt.getText().toString());
                }
            }

            //=== Other Charges
            if (mOthAmt.length() != 0)
            {
                if (mOthCha.isChecked()) {
                    Othercharge = Double.parseDouble(mOthAmt.getText().toString());
                }
            }

            //===============================
            mTotalCharge = Stampcharge + Othercharge + TranportCharge + Inscharge + RMVCharge + ServiceCharge + IntduserCharge;

            //========== Calculate Rental Amount ============
            Double r = mInterestRate / 1200;
            Double r1 = Math.pow(r + 1, mLoanPerid);
            mRentalamount = (Double) ((r + (r / (r1 - 1))) * (mFacilityAmount + mTotalCharge));

            CalRentalAmount.setText(nf.format(mRentalamount));
        }
    }

}
