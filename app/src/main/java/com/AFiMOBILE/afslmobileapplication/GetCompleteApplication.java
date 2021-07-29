package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;

public class GetCompleteApplication {

    private Context mContex;
    private SqlliteCreateLeasing sqlliteCreateLeasing_get_com_app;

    public GetCompleteApplication (Context context)
    {
        mContex =  context;
        sqlliteCreateLeasing_get_com_app = new SqlliteCreateLeasing(mContex);
    }

    public Cursor GetCompleteAppliation(String LoginName)
    {
        String mAppno="" , mClanme="", mFacamt="" , mNic="";
        MatrixCursor mOutPutcouser = new MatrixCursor(new String[] {"Appno" , "ClientName" , "Amount" , "ClNic" , "APPTYPE"});

        SQLiteDatabase sqLiteDatabase_AppSubmit = sqlliteCreateLeasing_get_com_app.getReadableDatabase();
        Cursor cursor_complete = sqLiteDatabase_AppSubmit.rawQuery("SELECT APPLICATION_REF_NO , AP_FACILITY_AMT FROM LE_APPLICATION WHERE AP_STAGE = '002' AND AP_MK_OFFICER = '" + LoginName + "'" , null );
        if (cursor_complete.getCount() != 0)
        {
            cursor_complete.moveToFirst();
            do
            {
                mAppno   =  cursor_complete.getString(0);
                mFacamt  =  cursor_complete.getString(1);

                Cursor cursor_client = sqLiteDatabase_AppSubmit.rawQuery("SELECT CL_TITLE , CL_NIC , CL_FULLY_NAME FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO ='" + mAppno + "'" , null);
                if (cursor_client.getCount() != 0)
                {
                    cursor_client.moveToFirst();
                    mClanme  =  cursor_client.getString(0) + " " + cursor_client.getString(2);
                    mNic     =  cursor_client.getString(1);

                }
                mOutPutcouser.addRow(new String[] {mAppno , mClanme , mFacamt , mNic , "SUBMIT"});
                cursor_client.close();
            }while (cursor_complete.moveToNext());
        }
        cursor_complete.close();
        return mOutPutcouser;
    }

}
