package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.pdf.PRIndirectReference;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recovery_Call_Fac_Details extends AppCompatActivity {

    private TextView mShowFacilityDetails;
    private Spinner mSpnSortOrder;
    private ArrayAdapter<String> arrayTITLE;
    private String LoginUser , LoginDate , LoginBranch , LoginUserName , mInputType;
    private SqlliteCreateRecovery sqlliteCreateRecovery_getdata;
    private RecyclerView mDataRecyView;
    public Adapter_Callcenter_facDetails mCalAdater;
    private SQLiteDatabase sqLiteDatabase_broken;
    private Button mBtnsort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_call_fac_details_view);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();

        mShowFacilityDetails    =   (TextView) findViewById(R.id.txtShowDetails);
        mSpnSortOrder           =   (Spinner)findViewById(R.id.txtsortorder);
        mBtnsort                =   (Button)findViewById(R.id.btnsort);

        sqlliteCreateRecovery_getdata = new SqlliteCreateRecovery(this);
        sqLiteDatabase_broken =  sqlliteCreateRecovery_getdata.getReadableDatabase();

        Intent intent = getIntent();
        mInputType    =   intent.getStringExtra("TYPE");

        Log.e("input" , mInputType);

        if (mInputType.equals("UNCONTACT"))
        {
            mShowFacilityDetails.setText("UN-CONTACT FACILITY LIST");
            mDataRecyView   =   (RecyclerView)findViewById(R.id.ReyFacList);
            mDataRecyView.setHasFixedSize(true);
            mDataRecyView.setLayoutManager(new LinearLayoutManager(this));
            mCalAdater = new Adapter_Callcenter_facDetails (this , LoadInputData_uncontact());
            mDataRecyView.setAdapter(mCalAdater);
        }
        else if (mInputType.equals("PTP"))
        {
            mShowFacilityDetails.setText("PTP FACILITY LIST");
            mDataRecyView   =   (RecyclerView)findViewById(R.id.ReyFacList);
            mDataRecyView.setHasFixedSize(true);
            mDataRecyView.setLayoutManager(new LinearLayoutManager(this));
            mCalAdater = new Adapter_Callcenter_facDetails (this , LoadInputData_ptp());
            mDataRecyView.setAdapter(mCalAdater);
        }
        else if (mInputType.equals("BROKEN"))
        {
            mShowFacilityDetails.setText("BROKEN PTP FACILITY LIST");
            mDataRecyView   =   (RecyclerView)findViewById(R.id.ReyFacList);
            mDataRecyView.setHasFixedSize(true);
            mDataRecyView.setLayoutManager(new LinearLayoutManager(this));
            mCalAdater = new Adapter_Callcenter_facDetails (this , BrokenPTP());
            mDataRecyView.setAdapter(mCalAdater);
        }

        //=== Load ShotOrder
        List<String> listTITLE = new ArrayList<String>();
        listTITLE.add("DATE");
        listTITLE.add("AMOUNT");
        arrayTITLE = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listTITLE);
        arrayTITLE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnSortOrder.setAdapter(arrayTITLE);
    }

    private Cursor LoadInputData_ptp()
    {
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] { "Seriel_no" , "Facility_Number" , "ACTION_DATE" , "TOTAL_OUTSTANDING"});
        int mCount=0;

        Cursor cursor_boken = null;
        Cursor cursor_ptp = sqLiteDatabase_broken.rawQuery("select FACNO , CALL_ACTION_DATE from recovery_call_center_action where CALL_ACTION_CODE = '001' or CALL_ACTION_CODE = '002' and ACTION_STS = ''" , null);
        if (cursor_ptp.getCount() != 0)
        {
            cursor_ptp.moveToFirst();
            do {
                cursor_boken = sqLiteDatabase_broken.rawQuery("select Facility_Number , Total_Outstanding  from CallCenter_recovery_generaldetail where Facility_Number = '" + cursor_ptp.getString(0) + "'" , null);
                cursor_boken.moveToFirst();
                mCount = mCount + 1;
                mOutPutcouser.addRow(new String[] { String.valueOf(mCount) ,  cursor_boken.getString(0) , cursor_ptp.getString(1) , cursor_boken.getString(1)  });
            }while (cursor_ptp.moveToNext());
        }
        return mOutPutcouser;
    }

    private Cursor LoadInputData_uncontact()
    {
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"Seriel_no" , "Facility_Number" , "ACTION_DATE" , "TOTAL_OUTSTANDING"});
        Cursor cursor_uncontact = null;
        int mCount=0;

        Cursor cursor_uncon = sqLiteDatabase_broken.rawQuery("select FACNO , CALL_ACTION_DATE from recovery_call_center_action where CALL_ACTION_CODE = '006' or CALL_ACTION_CODE = '007' or " +
                "CALL_ACTION_CODE = '004' or CALL_ACTION_CODE = '011' and ACTION_STS = ''" , null);
        if (cursor_uncon.getCount() != 0)
        {
            cursor_uncon.moveToFirst();
            do {
                mCount = mCount + 1;
                cursor_uncontact = sqLiteDatabase_broken.rawQuery("select Facility_Number , Total_Outstanding  from CallCenter_recovery_generaldetail where Facility_Number = '" + cursor_uncon.getString(0) + "'" , null);
                cursor_uncontact.moveToFirst();

                mOutPutcouser.addRow(new String[] { String.valueOf(mCount) , cursor_uncontact.getString(0) , cursor_uncon.getString(1) , cursor_uncontact.getString(1)  });


            }while (cursor_uncon.moveToNext());
        }
        return mOutPutcouser;
    }

    private Cursor BrokenPTP()
    {
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"Seriel_no" , "Facility_Number" , "ACTION_DATE" , "TOTAL_OUTSTANDING"});
        Cursor cursor_boken = null;
        int mCount=0;

        Cursor cursor_ptp = sqLiteDatabase_broken.rawQuery("select FACNO , CALL_PTP_DATE  from recovery_call_center_action where CALL_ACTION_CODE = '001' or CALL_ACTION_CODE = '002' and ACTION_STS = ''" , null);
        if (cursor_ptp.getCount() != 0)
        {
            cursor_ptp.moveToFirst();
            do {
                Date dateptp=new Date();
                Date datecurrent=new Date();

                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String datenow=cursor_ptp.getString(1);


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
                    mCount = mCount + 1;
                    cursor_boken = sqLiteDatabase_broken.rawQuery("select Facility_Number , Total_Outstanding  from CallCenter_recovery_generaldetail where Facility_Number = '" + cursor_ptp.getString(0) + "'" , null);
                    cursor_boken.moveToFirst();
                    mOutPutcouser.addRow(new String[] { String.valueOf(mCount) , cursor_boken.getString(0) , cursor_ptp.getString(1) , cursor_boken.getString(1)  });
                }
            }while (cursor_ptp.moveToNext());
        }

        return mOutPutcouser;
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        sqLiteDatabase_broken.close();
        sqlliteCreateRecovery_getdata.close();
        super.onDestroy();
    }
}
