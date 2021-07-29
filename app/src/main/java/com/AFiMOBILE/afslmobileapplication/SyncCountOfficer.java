package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ConcurrentModificationException;

public class SyncCountOfficer
{
    public Context mContex;
    public SqlliteCreateLeasing sqlliteCreateLeasing;
    public String mUserId;
    public int SysCount;


    public SyncCountOfficer (Context context)
    {
        mContex = context;
        sqlliteCreateLeasing    =   new SqlliteCreateLeasing(mContex);
    }

    public int GetSystemSyncCount(String LoginCode)
    {
        mUserId = LoginCode;

        SQLiteDatabase sqLiteDatabase_syn_counbt = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursor_sync_count = sqLiteDatabase_syn_counbt.rawQuery("SELECT NOT_REF_ID FROM MY_NOTIFICARTION WHERE NOT_READ = '' AND  USER_ID = '" + mUserId + "'" , null );

        if (cursor_sync_count.getCount()!=0)
        {
            SysCount = cursor_sync_count.getCount();
        }
        else
        {
            SysCount = 0;
        }
        cursor_sync_count.close();
        sqLiteDatabase_syn_counbt.close();

        return SysCount;
    }


}
