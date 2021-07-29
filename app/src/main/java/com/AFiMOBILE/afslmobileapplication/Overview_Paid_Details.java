package com.AFiMOBILE.afslmobileapplication;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Overview_Paid_Details extends AppCompatActivity {


    public String LoginUser , LoginDate , LoginBranch , LoginUserName;
    public SqlliteCreateRecovery sqlliteCreateRecovery_Overview;
    public Button mBtnFullPaid , mBtnPartPaid , mBtnSettle;
    public RecyclerView mFacListView;
    public Adapter_Recover_FacList mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_paid_details);

        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName = globleClassDetails.getOfficerName();

        sqlliteCreateRecovery_Overview = new SqlliteCreateRecovery(this);


        mBtnFullPaid    =   (Button)findViewById(R.id.btnfullyPaid);
        mBtnPartPaid    =   (Button)findViewById(R.id.btnpartPaid);
        mBtnSettle      =   (Button)findViewById(R.id.btnsettle);
        mFacListView    =   (RecyclerView)findViewById(R.id.ReyFacList);


        //====  Run Process
        mBtnFullPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFacListView.setHasFixedSize(true);
                mFacListView.setLayoutManager(new LinearLayoutManager(Overview_Paid_Details.this));
                mAdapter = new Adapter_Recover_FacList(Overview_Paid_Details.this , FullyPaid() , "NORMAL");
                mFacListView.setAdapter(mAdapter);
            }
        });


        mBtnPartPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFacListView.setHasFixedSize(true);
                mFacListView.setLayoutManager(new LinearLayoutManager(Overview_Paid_Details.this));
                mAdapter = new Adapter_Recover_FacList(Overview_Paid_Details.this , PartPaid() , "NORMAL");
                mFacListView.setAdapter(mAdapter);
            }
        });

        mBtnSettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFacListView.setHasFixedSize(true);
                mFacListView.setLayoutManager(new LinearLayoutManager(Overview_Paid_Details.this));
                mAdapter = new Adapter_Recover_FacList(Overview_Paid_Details.this , FullySettle() , "NORMAL");
                mFacListView.setAdapter(mAdapter);
            }
        });


    }

    public Cursor FullySettle()
    {
        SQLiteDatabase sqLiteDatabase_setteld = sqlliteCreateRecovery_Overview.getReadableDatabase();
        Cursor cursor_settle = sqLiteDatabase_setteld.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser + "' AND Facility_Status = 'C'" , null);

        return cursor_settle;
    }


    public Cursor PartPaid()
    {
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"Facility_Number" , "Customer_Full_Name" , "Total_Arrear_Amount" , "C_Address_Line_1" , "C_Address_Line_2" , "C_Address_Line_3" , "C_Address_Line_4" , "Asset_Model" ,
                "Vehicle_Number" , "Last_Payment_Amount" , "Last_Payment_Date" , "Arrears_Rental_No"});

        SQLiteDatabase sqLiteDatabase_fullyPaid = sqlliteCreateRecovery_Overview.getReadableDatabase();
        Cursor cursor_getcollection = sqLiteDatabase_fullyPaid.rawQuery("SELECT Facility_No FROM nemf_form_updater WHERE MADE_BY = '" + LoginUser + "' AND ACTIONCODE = 'Collection Entry'" , null );
        if (cursor_getcollection.getCount() != 0)
        {
            cursor_getcollection.moveToFirst();
            do{
                Cursor cursor_Part_paid = sqLiteDatabase_fullyPaid.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                        "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Facility_Number = '" + cursor_getcollection.getString(0)  + "' AND Total_Arrear_Amount != '0.00' OR Total_Arrear_Amount != '0'" , null);
                if (cursor_Part_paid.getCount() != 0)
                {
                    cursor_Part_paid.moveToFirst();

                    mOutPutcouser.addRow(new String[] {cursor_Part_paid.getString(0) , cursor_Part_paid.getString(1) , cursor_Part_paid.getString(2) , cursor_Part_paid.getString(3) , cursor_Part_paid.getString(4) , cursor_Part_paid.getString(5) ,
                            cursor_Part_paid.getString(6) , cursor_Part_paid.getString(7) , cursor_Part_paid.getString(8) , cursor_Part_paid.getString(9) , cursor_Part_paid.getString(10) , cursor_Part_paid.getString(11) });
                }

            }while (cursor_getcollection.moveToNext());
        }

        return mOutPutcouser;
    }

    public Cursor FullyPaid()
    {
        SQLiteDatabase sqLiteDatabase_fullyPaid = sqlliteCreateRecovery_Overview.getReadableDatabase();
        Cursor cursor_Fully_paid = sqLiteDatabase_fullyPaid.rawQuery("SELECT Facility_Number,Customer_Full_Name,Total_Arrear_Amount,C_Address_Line_1,C_Address_Line_2,C_Address_Line_3,C_Address_Line_4,Asset_Model," +
                "Vehicle_Number,Last_Payment_Amount,Last_Payment_Date,Arrears_Rental_No FROM recovery_generaldetail WHERE Recovery_Executive = '" + LoginUser  + "' AND Total_Arrear_Amount = '0.00' OR Total_Arrear_Amount = '0'" , null);

        return cursor_Fully_paid;
    }
}
