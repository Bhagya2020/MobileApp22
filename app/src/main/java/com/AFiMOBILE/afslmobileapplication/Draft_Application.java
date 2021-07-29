package com.AFiMOBILE.afslmobileapplication;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;
import java.util.List;

public class Draft_Application extends AppCompatActivity {

    private EditText mCl_Nic , mCl_Name , mCl_Adders , mCl_Adders_2 , mCl_Adders_3 , mCl_Adders_4;
    private Spinner Cl_Title;
    private Button mBtnUpdateTag;
    private String mError="" , LoginUser="" , LoginDate="" , LoginBranch , mValidate;
    public View layout_sucress , layout_error;
    private TextView tv , tv_sucess;
    private SqlliteCreateLeasing sqlliteCreateLeasing_draft;
    public double current_lattitude ,  current_longitude;
    private ArrayAdapter<String> arrayTITLE;
    private RecyclerView mRecycleCoApp;
    private Adapter_Draft_Application myPenadapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draft_application_save);


        mCl_Nic         =   findViewById(R.id.txtclNIC);
        Cl_Title        =   findViewById(R.id.txtTITLE);
        mCl_Name        =   findViewById(R.id.txtclname);
        mCl_Adders      =   findViewById(R.id.txtadd1);
        mCl_Adders_2    =   findViewById(R.id.txtadd2);
        mCl_Adders_3    =   findViewById(R.id.txtadd3);
        mCl_Adders_4    =   findViewById(R.id.txtadd4);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        mBtnUpdateTag   =   findViewById(R.id.btnTagUpdate);

        //==== Get GEO locations
        GetLo();

        sqlliteCreateLeasing_draft = new SqlliteCreateLeasing(this);

        mCl_Nic.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mCl_Name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mCl_Adders.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mCl_Adders_2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mCl_Adders_3.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mCl_Adders_4.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        mRecycleCoApp       =  (RecyclerView)findViewById(R.id.rcgurdetails);
        mRecycleCoApp.setHasFixedSize(true);
        mRecycleCoApp.setLayoutManager(new LinearLayoutManager(Draft_Application.this));
        myPenadapter = new Adapter_Draft_Application(Draft_Application.this , GetCoAppDetils()) ;
        mRecycleCoApp.setAdapter(myPenadapter);


        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);

        List<String> listTITLE = new ArrayList<String>();
        listTITLE.add("MR.");
        listTITLE.add("MRS.");
        arrayTITLE = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listTITLE);
        arrayTITLE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Cl_Title.setAdapter(arrayTITLE);


        mBtnUpdateTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateDraftApplication();
            }
        });

    }

    public void UpdateDraftApplication()
    {
        mValidate = "YES";
        if (mCl_Nic.getText().length() == 0)
        {
            mValidate   = "NO";
            tv.setText("<NIC> No is Blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mCl_Name.getText().length() == 0)
        {
            mValidate   = "NO";
            tv.setText("<Client Name> No is Blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mCl_Adders.getText().length() == 0)
        {
            mValidate   = "NO";
            tv.setText("<Adders> No is Blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }

        if (mValidate   == "YES")
        {
            //==== Update the record ====

            SQLiteDatabase sqLiteDatabase_draft = sqlliteCreateLeasing_draft.getWritableDatabase();
            ContentValues contentValues_draft = new ContentValues();
            contentValues_draft.put("NIC" , mCl_Nic.getText().toString());
            contentValues_draft.put("CL_NAME" , mCl_Name.getText().toString());
            contentValues_draft.put("ADDERS_1" , mCl_Adders.getText().toString());
            contentValues_draft.put("ADDERS_2" , mCl_Adders_2.getText().toString());
            contentValues_draft.put("ADDERS_3" , mCl_Adders_3.getText().toString());
            contentValues_draft.put("ADDERS_4" , mCl_Adders_4.getText().toString());
            contentValues_draft.put("CURENT_LATTITUDE" , current_lattitude);
            contentValues_draft.put("CURRENT_LONGGITUDE" , current_longitude);
            contentValues_draft.put("ENT_DATE" , LoginDate);
            contentValues_draft.put("ENT_USER" , LoginUser);
            contentValues_draft.put("APP_REF_NO" , "");
            contentValues_draft.put("UPD_FLG" , "");
            contentValues_draft.put("GEO_FLG_UPDATE" , "");
            sqLiteDatabase_draft.insert("DRAFT_APPLICATION" , null , contentValues_draft);

            tv_sucess.setText("Record Successfully Submitted.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_sucress);
            toast.show();

            myPenadapter.swapCursor(GetCoAppDetils());
            mCl_Nic.setText("");    mCl_Name.setText("");   mCl_Adders.setText("");
            mCl_Adders_2.setText("");   mCl_Adders_3.setText("");   mCl_Adders_3.setText("");   mCl_Adders_4.setText("");
        }

    }

    public Cursor GetCoAppDetils()
    {

        SQLiteDatabase sqLiteDatabase_getdata = sqlliteCreateLeasing_draft.getReadableDatabase();
        Cursor cursor_getData =  sqLiteDatabase_getdata.rawQuery("SELECT NIC,CL_NAME,ADDERS_1,ADDERS_2,ADDERS_3,ADDERS_4 FROM DRAFT_APPLICATION WHERE ENT_USER = '" + LoginUser + "' AND APP_REF_NO = ''" , null);

        Log.d("count" , String.valueOf(cursor_getData.getCount()));

        return cursor_getData;
    }


    public void GetLo()
    {
        GPSTracker gps = new GPSTracker(this);
        int status = 0;
        if(gps.canGetLocation())

        {
            //status = GooglePlayServicesUtil
              //      .isGooglePlayServicesAvailable(getApplicationContext());

             status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

            if (status == ConnectionResult.SUCCESS) {
                current_lattitude = gps.getLatitude();
                current_longitude = gps.getLongitude();
                Log.d("GEO-TAG", "" + current_lattitude + "-"
                        + current_longitude);

                if (current_lattitude == 0.0 && current_longitude == 0.0) {
                    current_lattitude = 22.22;
                    current_longitude = 22.22;

                }

            } else {
                current_lattitude = 22.22;
                current_longitude = 22.22;
            }

        }
        else
        {
            gps.showSettingsAlert();
        }
    }
}
