package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GetManagerPendingPO {

    private Context mContex;
    private SqlliteCreateLeasing sqlliteCreateLeasing_get_mgr;

    public GetManagerPendingPO (Context context)
    {
        mContex = context;
        sqlliteCreateLeasing_get_mgr = new SqlliteCreateLeasing(mContex);

    }

    public Cursor GetManagerApprove()
    {
        SQLiteDatabase sqLiteDatabase = sqlliteCreateLeasing_get_mgr.getReadableDatabase();
        Cursor cursorpending = sqLiteDatabase.rawQuery("SELECT APP_REF_NO,CLNAME,CL_NIC,AMT,MKOFFICER FROM PO_PENDING WHERE APP_STS = '001' " , null);
        return cursorpending;
    }
}
