package com.AFiMOBILE.afslmobileapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Rmv_Verification_Update extends AppCompatActivity {

    private String PHP_URL_SQL, LoginUser , LoginDate , LoginBranch , LoginUserName;
    private SqlliteCreateLeasing sqlliteCreateLeasing_rmv_update;
    private RecyclerView mRmv_updateapplication;
    public static Adapter_Rmv_Update mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rmv_verification_update);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();

        sqlliteCreateLeasing_rmv_update = new SqlliteCreateLeasing(Rmv_Verification_Update.this);


        mRmv_updateapplication  =   (RecyclerView)findViewById(R.id.rcppendingapp);

        //=== Load Pending RMV Update data
        //=== Recycviwe load the data
        mRmv_updateapplication.setHasFixedSize(true);
        mRmv_updateapplication.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_Rmv_Update(this , GetRmvpplication());
        mRmv_updateapplication.setAdapter(mAdapter);
    }

    public Cursor GetRmvpplication()
    {
        SQLiteDatabase sqLiteDatabase_getdata = sqlliteCreateLeasing_rmv_update.getReadableDatabase();
        Cursor cursor_getdata = sqLiteDatabase_getdata.rawQuery("SELECT REG_NO,ENGINE_NO,CHASSI_NO FROM RMV_CONFORM_DATA WHERE RMV_CONFORM_STS = ''" , null);
        return cursor_getdata;
    }
}
