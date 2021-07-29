package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class Recovery_Cash_Wallet extends AppCompatActivity {

    public SqlliteCreateRecovery sqlliteCreateRecovery_cash_wallet;
    public String PHP_URL_SQL , LoginUser , LoginDate , LoginBranch ;
    public TextView mWalletLimite , mTotalcollection , mCollectionCount , mTotalInhandCash;
    public Button BtnCashDeposit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_cash_wallet);

        sqlliteCreateRecovery_cash_wallet = new SqlliteCreateRecovery(this);
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();


        //=== Load Cash Deposit
        BtnCashDeposit = (Button)findViewById(R.id.btnDeposit) ;
        BtnCashDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_csh_deposit = new Intent("android.intent.action.Recovery_cash_deposit");
                startActivity(intent_csh_deposit);
            }
        });




        //==== Load Main Data
        LoadData();

    }


    public void LoadData()
    {
        DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");

        mWalletLimite       =   (TextView)findViewById(R.id.TxtWalllimite);
        mTotalcollection    =   (TextView)findViewById(R.id.TxtTotalCollection);
        mCollectionCount    =   (TextView)findViewById(R.id.TxtTotalCount) ;
        mTotalInhandCash         =   (TextView)findViewById(R.id.TxtInhandCas);

        SQLiteDatabase sqLiteDatabase_getData =     sqlliteCreateRecovery_cash_wallet.getReadableDatabase();


        //== Wallet Amt
        Cursor cursor_wallet_limite = sqLiteDatabase_getData.rawQuery("SELECT officer_wallet_limit FROM masr_cash_wallet WHERE offier_code = '" + LoginUser + "'" , null);
        if (cursor_wallet_limite.getCount() != 0)
        {
            cursor_wallet_limite.moveToFirst();
            mWalletLimite.setText(nf.format(Double.parseDouble(cursor_wallet_limite.getString(0).replace("," , ""))));
        }
        cursor_wallet_limite.close();

        //=== Get Total Collection
        double mTotalCollection=0.00 , mInhandCash=0.00;  int mColCount=0;
        Cursor cursor_collection = sqLiteDatabase_getData.rawQuery("SELECT rcpt_amt FROM recovery_recipt WHERE rcpt_user_id = '" + LoginUser + "'" , null);
        if (cursor_collection.getCount() != 0)
        {
            mColCount = cursor_collection.getCount();
            cursor_collection.moveToFirst();
            do{

                if (cursor_collection.getString(0).toString().equals(""))
                {
                    mTotalCollection = mTotalCollection + 0.00;
                }
                else
                {
                    mTotalCollection     =   mTotalCollection + (Float.parseFloat(cursor_collection.getString(0).replace("," , "")));
                }
            }while (cursor_collection.moveToNext());
        }
        mTotalcollection.setText(nf.format(Double.parseDouble(String.valueOf(mTotalCollection))));
        mCollectionCount.setText(nf.format(Double.parseDouble(String.valueOf(mColCount))));

        cursor_collection.close();

        //=== Get Inhand Cash
        Cursor cursor_Inhand = sqLiteDatabase_getData.rawQuery("SELECT rcpt_amt FROM recovery_recipt WHERE rcpt_user_id = '" + LoginUser + "' and dep_sts = ''" , null);
        if (cursor_Inhand.getCount() != 0)
        {
            cursor_Inhand.moveToFirst();
            do{

                if (cursor_Inhand.getString(0).toString().equals(""))
                {
                    mInhandCash = mInhandCash + 0.00;
                }
                else
                {
                    mInhandCash     =   mInhandCash + (Float.parseFloat(cursor_Inhand.getString(0).replace("," , "")));
                }
            }while (cursor_Inhand.moveToNext());
        }
        mTotalInhandCash.setText(nf.format(Double.parseDouble(String.valueOf(mInhandCash))));
    }

    protected void onDestroy(){

        sqlliteCreateRecovery_cash_wallet.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

}
