package com.AFiMOBILE.afslmobileapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.webkit.JavascriptInterface;



public class WebAppInterface
{
    private Context mContex;
    public static String mWebViewActionCode;
    public SqlliteCreateLeasing sqlliteCreateLeasing_ManagerDocView;
    public String mapplicationno;

    public WebAppInterface (Context context)
    {
        mContex =   context;
        sqlliteCreateLeasing_ManagerDocView = new SqlliteCreateLeasing(mContex);
    }

    @JavascriptInterface
    public void GetMgeAction(String Action , String AppNo)
    {
        mapplicationno = AppNo;
        if (Action.equals("Approve"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContex);
            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
            builder.setMessage("PO Approval Successful!");
            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Po_Web_View.instance_po_web.finish();

                    //=== Delete Approve Recoed

                    SQLiteDatabase sqLiteDatabase_delete_record = sqlliteCreateLeasing_ManagerDocView.getWritableDatabase();
                    sqLiteDatabase_delete_record.delete("PO_PENDING","APP_REF_NO =?",new String[] {mapplicationno});
                    sqLiteDatabase_delete_record.close();

                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        }

        if (Action.equals("Rejected"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContex);
            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
            builder.setMessage("PO Rejected Successfully!");
            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Po_Web_View.instance_po_web.finish();
                    SQLiteDatabase sqLiteDatabase_delete_record = sqlliteCreateLeasing_ManagerDocView.getWritableDatabase();
                    sqLiteDatabase_delete_record.delete("PO_PENDING","APP_REF_NO =?",new String[] {mapplicationno});
                    sqLiteDatabase_delete_record.close();
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();


        }






    }
}
