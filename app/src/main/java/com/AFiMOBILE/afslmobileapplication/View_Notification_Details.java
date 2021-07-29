package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class View_Notification_Details extends AppCompatActivity {

    public String PHP_URL_SQL="" , LoginUser="" , LoginDate = "" , LoginBranch= "";
    private RecyclerView mRecycleviwer;
    public Adapter_Notification mAdapter;
    public Button BtnClose;
    public ImageView mClose;
    public SqlliteCreateLeasing sqlliteCreateLeasing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__notification__details);


        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        mClose      = (ImageView)findViewById(R.id.imageclose);

        sqlliteCreateLeasing = new SqlliteCreateLeasing(this);
        mRecycleviwer = (RecyclerView)findViewById(R.id.recyleNotificationView);
        mRecycleviwer.setHasFixedSize(true);
        mRecycleviwer.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_Notification(this , GetNotifyData());
        mRecycleviwer.setAdapter(mAdapter);


        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void onDestroy(){
        super.onDestroy();
        UpdateREadSts();
        sqlliteCreateLeasing.close();
        Log.d("Log", "onDestroy-ManagerApprove");
    }


    public void UpdateREadSts()
    {
        SQLiteDatabase sqLiteDatabase_update_sts = sqlliteCreateLeasing.getWritableDatabase();
        Cursor cursor_update_sts = sqLiteDatabase_update_sts.rawQuery("SELECT NOT_REF_ID FROM MY_NOTIFICARTION WHERE NOT_READ = '' AND USER_ID = '" + LoginUser + "'" , null );
        if (cursor_update_sts.getCount()!=0)
        {
            cursor_update_sts.moveToFirst();
            do
            {
                ContentValues contentValues_sts = new ContentValues();
                contentValues_sts.put("NOT_READ" , "Y");
                sqLiteDatabase_update_sts.update("MY_NOTIFICARTION" , contentValues_sts , "NOT_REF_ID = ?" , new String[]{String.valueOf(cursor_update_sts.getString(0))} );

            } while (cursor_update_sts.moveToNext());
        }
        cursor_update_sts.close();
    }

    public Cursor GetNotifyData()
    {
        String mAppno="" , msts = "" , mClname="" , FacAmt="";
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"NotDes" , "Clname" , "Appno" , "Amt"});
        startManagingCursor(mOutPutcouser);

        Log.d("Data Sync" , "DONE");
        SQLiteDatabase sqLiteDatabase = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursor =  sqLiteDatabase.rawQuery("SELECT * FROM MY_NOTIFICARTION WHERE NOT_READ = '' AND USER_ID = '" + LoginUser + "'" , null );

        if (cursor.getCount()!=0)
        {
            cursor.moveToFirst();
            do
            {
                mAppno = cursor.getString(1);
                msts   =   cursor.getString(4);

                Log.d("NotifyAppno" , mAppno);

                Cursor cursor1_amt = sqLiteDatabase.rawQuery("SELECT AP_FACILITY_AMT FROM LE_APPLICATION WHERE APPLICATION_REF_NO = '" + mAppno + "'" , null);
                if (cursor1_amt.getCount()!=0)
                {
                    cursor1_amt.moveToFirst();
                    FacAmt = cursor1_amt.getString(0);
                }

                Cursor cursor_get_clname = sqLiteDatabase.rawQuery("SELECT CL_FULLY_NAME FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO = '" + mAppno + "'" , null);
                if (cursor_get_clname.getCount()!=0)
                {
                    cursor_get_clname.moveToFirst();
                    mClname = cursor_get_clname.getString(0);
                }

                mOutPutcouser.addRow(new String[] {msts , mClname , mAppno , FacAmt});
                cursor_get_clname.close();
            }while (cursor.moveToNext());
        }
        cursor.close();
        return mOutPutcouser;
    }
}
