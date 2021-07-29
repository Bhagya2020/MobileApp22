package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class Volly_Recovery_Post_DataSync
{
    private Context mContex;
    public SqlliteCreateRecovery sqlliteCreateRecovery_PostDataSync;
    public RequestQueue mQueue;
    private android.app.AlertDialog progressDialog;
    public String PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , mLoginName , mInputUserName , PostDataResponces;
    public JsonObjectRequest jsonObjectRequest_PostRecoveryData;

    public Volly_Recovery_Post_DataSync (Context context)
    {
        mContex =  context;
        sqlliteCreateRecovery_PostDataSync = new SqlliteCreateRecovery(mContex);

        //===== Create Volly Request Ques. =======
        mQueue   = VollySingleton.getInstance(mContex).getRequestQueue();
        progressDialog = new SpotsDialog(mContex, R.style.Custom1);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        PHP_URL_SQL = "http://afimobile.abansfinance.lk/mobilephp/";
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        mLoginName  =   globleClassDetails.getOfficerName();
    }

    public void PostOfficerData(String USER_ID) {
        //=== Create Json Data File ===
        //=============================
        mInputUserName = USER_ID;
        Log.d("PostOffierData", "START");

        JSONArray jsonArray_RecoveryCurserData = new JSONArray();
        JSONArray jsonArray_ManagerAssing = new JSONArray();
        JSONArray jsonArray_ManagerResponces = new JSONArray();
        JSONArray jsonArray_Repocess = new JSONArray();
        JSONArray jsonArray_Trvelling = new JSONArray();
        JSONArray jsonArray_recipt_Data = new JSONArray();
        JSONArray jsonArray_image_Data = new JSONArray();
        JSONArray jsonArray_Cash_deposit = new JSONArray();
        JSONArray jsonArray_Cash_deposit_details = new JSONArray();

        SQLiteDatabase sqLiteDatabase_PostRcData = sqlliteCreateRecovery_PostDataSync.getReadableDatabase();

        //==== Create Deposit Details
        Cursor cursor_deposit_details = sqLiteDatabase_PostRcData.rawQuery("SELECT * FROM recovery_deposit_details WHERE dep_user = '" + mInputUserName + "' AND Live_server_update = ''" ,null );
        if (cursor_deposit_details.getCount() != 0)
        {
            Log.d("PostDepositDetails", "CREATED");
            cursor_deposit_details.moveToFirst();

            do {
                JSONObject jsonObject_Post_deposit_details = new JSONObject();

                try {
                    jsonObject_Post_deposit_details.put("dep_refno" , cursor_deposit_details.getString(0));
                    jsonObject_Post_deposit_details.put("receipt_entry_no" , cursor_deposit_details.getString(1));
                    jsonObject_Post_deposit_details.put("receipt_date" , cursor_deposit_details.getString(2));
                    jsonObject_Post_deposit_details.put("receipt_amt" , cursor_deposit_details.getString(3));
                    jsonObject_Post_deposit_details.put("dep_user" , cursor_deposit_details.getString(4));
                    jsonObject_Post_deposit_details.put("br_code" , cursor_deposit_details.getString(5));

                    jsonArray_Cash_deposit_details.put(jsonObject_Post_deposit_details);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }while (cursor_deposit_details.moveToNext());
        }


        //==== Create Deposit Data
        Cursor cursor_depsit = sqLiteDatabase_PostRcData.rawQuery("SELECT * FROM recovery_cash_deposit WHERE dep_user = '" + mInputUserName + "' AND Live_server_update = ''", null);
        if (cursor_depsit.getCount() != 0)
        {
            Log.d("PostDepositData", "CREATED");
            cursor_depsit.moveToFirst();
            do {
                JSONObject jsonObject_Post_deposit = new JSONObject();
                try {
                    jsonObject_Post_deposit.put("dep_refno" , cursor_depsit.getString(0));
                    jsonObject_Post_deposit.put("dep_date" , cursor_depsit.getString(1));
                    jsonObject_Post_deposit.put("dep_time" , cursor_depsit.getString(2));
                    jsonObject_Post_deposit.put("dep_bank" , cursor_depsit.getString(3));
                    jsonObject_Post_deposit.put("dep_amt" , cursor_depsit.getString(4));
                    jsonObject_Post_deposit.put("dep_sts" , cursor_depsit.getString(5));
                    jsonObject_Post_deposit.put("dep_user" , cursor_depsit.getString(6));
                    jsonObject_Post_deposit.put("dep_user_br_code" , cursor_depsit.getString(7));

                    jsonArray_Cash_deposit.put(jsonObject_Post_deposit);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }while (cursor_depsit.moveToNext());
        }

        //====   Create Image Data
        Cursor cursor_image = sqLiteDatabase_PostRcData.rawQuery("SELECT * FROM recoveery_doc_uplaod WHERE Live_Server_Update = ''" , null);
        if (cursor_image.getCount() != 0)
        {
            Log.d("PostImageData", "CREATED");
            cursor_image.moveToFirst();
            do {
                JSONObject jsonObject_Post_image_data = new JSONObject();

                try {
                    jsonObject_Post_image_data.put("referance_no" , cursor_image.getString(0));
                    jsonObject_Post_image_data.put("facility_no" , cursor_image.getString(1));
                    jsonObject_Post_image_data.put("doc_type" , cursor_image.getString(2));
                    jsonObject_Post_image_data.put("doc_image" , cursor_image.getString(3));
                    jsonObject_Post_image_data.put("doc_date" , cursor_image.getString(4));
                    jsonObject_Post_image_data.put("doc_useruid" , cursor_image.getString(5));

                    jsonArray_image_Data.put(jsonObject_Post_image_data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }while (cursor_image.moveToNext());

        }

        //==== Create Receipt Data
        Cursor cursor_receipt_details = sqLiteDatabase_PostRcData.rawQuery("SELECT *FROM recovery_recipt WHERE Live_server_update = ''" , null);
        if (cursor_receipt_details.getCount() != 0)
        {
            cursor_receipt_details.moveToFirst();
            Log.d("PostReceiptData", "CREATED");
            do
            {
                JSONObject jsonObject_Post_receiptdata = new JSONObject();
                try {
                    jsonObject_Post_receiptdata.put("rcpt_refno" , cursor_receipt_details.getString(0));
                    jsonObject_Post_receiptdata.put("rcpt_date" , cursor_receipt_details.getString(1));
                    jsonObject_Post_receiptdata.put("rcpt_amt" , cursor_receipt_details.getString(2));
                    jsonObject_Post_receiptdata.put("rcpt_time" , cursor_receipt_details.getString(3));
                    jsonObject_Post_receiptdata.put("rcpt_pay_mode" , cursor_receipt_details.getString(4));
                    jsonObject_Post_receiptdata.put("rcpt_facno" , cursor_receipt_details.getString(5));
                    jsonObject_Post_receiptdata.put("rcpt_branch_code" , cursor_receipt_details.getString(6));
                    jsonObject_Post_receiptdata.put("rcpt_user_id" , cursor_receipt_details.getString(7));
                    jsonObject_Post_receiptdata.put("rcpt_des" , cursor_receipt_details.getString(8));
                    jsonObject_Post_receiptdata.put("manual_rcptno" , cursor_receipt_details.getString(9));
                    jsonArray_recipt_Data.put(jsonObject_Post_receiptdata);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }while (cursor_receipt_details.moveToNext());
        }

        //===Create RecoveryActionData
        Cursor cursor_PostRcData = sqLiteDatabase_PostRcData.rawQuery("SELECT * FROM nemf_form_updater where LIVE_SERVER_UPDATE = '' and MADE_BY = '" + mInputUserName + "'" , null);
        if (cursor_PostRcData.getCount() != 0)
        {
            Log.d("PostOffierData-Action", "CREATED");
            cursor_PostRcData.moveToFirst();
            do{
                JSONObject jsonObject_Post_RecData = new JSONObject();
                try {

                    if (cursor_PostRcData.getString(0) != null)
                        jsonObject_Post_RecData.put("ID" , cursor_PostRcData.getString(0));
                    else
                        jsonObject_Post_RecData.put("ID" , "");

                    if (cursor_PostRcData.getString(1) != null)
                        jsonObject_Post_RecData.put("FACILITY_NO" , cursor_PostRcData.getString(1));
                    else
                        jsonObject_Post_RecData.put("FACILITY_NO" , "");

                    if (cursor_PostRcData.getString(2) != null)
                        jsonObject_Post_RecData.put("ACTIONCODE" , cursor_PostRcData.getString(2));
                    else
                        jsonObject_Post_RecData.put("ACTIONCODE" , "");

                    if (cursor_PostRcData.getString(3) != null)
                        jsonObject_Post_RecData.put("ACTION_DATE" , cursor_PostRcData.getString(3));
                    else
                        jsonObject_Post_RecData.put("ACTION_DATE" , cursor_PostRcData.getString(3));


                    if (cursor_PostRcData.getString(4) != null)
                        jsonObject_Post_RecData.put("MADE_BY" , cursor_PostRcData.getString(4));
                    else
                        jsonObject_Post_RecData.put("MADE_BY" , "");

                    if (cursor_PostRcData.getString(5) != null)
                        jsonObject_Post_RecData.put("CURRENT_BUCKET" , cursor_PostRcData.getString(5));
                    else
                        jsonObject_Post_RecData.put("CURRENT_BUCKET" , "");

                    if (cursor_PostRcData.getString(6) != null)
                        jsonObject_Post_RecData.put("LINKED_ID" , cursor_PostRcData.getString(6));
                    else
                        jsonObject_Post_RecData.put("LINKED_ID" , "");

                    if (cursor_PostRcData.getString(7) != null)
                        jsonObject_Post_RecData.put("CNAME" , cursor_PostRcData.getString(7));
                    else
                        jsonObject_Post_RecData.put("CNAME" , "");

                    if (cursor_PostRcData.getString(8) != null)
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_TYPE" , cursor_PostRcData.getString(8));
                    else
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_TYPE" , "");

                    if (cursor_PostRcData.getString(9) != null)
                        jsonObject_Post_RecData.put("MODE_OF_PAYMENT" , cursor_PostRcData.getString(9));
                    else
                        jsonObject_Post_RecData.put("MODE_OF_PAYMENT" , "");

                    if (cursor_PostRcData.getString(10) != null)
                        jsonObject_Post_RecData.put("COLLECTION_AMOUNT" , cursor_PostRcData.getString(10));
                    else
                        jsonObject_Post_RecData.put("COLLECTION_AMOUNT" , "");

                    if (cursor_PostRcData.getString(11) != null)
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_DATE" , cursor_PostRcData.getString(11));
                    else
                        jsonObject_Post_RecData.put("COLLECTION_ENTRY_DATE" , "");

                    if (cursor_PostRcData.getString(12) != null)
                        jsonObject_Post_RecData.put("MANUAL_RECEIPT_NO" , cursor_PostRcData.getString(12));
                    else
                        jsonObject_Post_RecData.put("MANUAL_RECEIPT_NO" , "");

                    if (cursor_PostRcData.getString(13) != null)
                        jsonObject_Post_RecData.put("PROVISIONAL_RECEIPT_NO" , cursor_PostRcData.getString(13));
                    else
                        jsonObject_Post_RecData.put("PROVISIONAL_RECEIPT_NO" , "");

                    if (cursor_PostRcData.getString(14) != null)
                        jsonObject_Post_RecData.put("CHEQUE_NO" , cursor_PostRcData.getString(14));
                    else
                        jsonObject_Post_RecData.put("CHEQUE_NO" , "");

                    if (cursor_PostRcData.getString(15) != null)
                        jsonObject_Post_RecData.put("CHEQUE_BANK" , cursor_PostRcData.getString(15));
                    else
                        jsonObject_Post_RecData.put("CHEQUE_BANK" , "");

                    if (cursor_PostRcData.getString(16) != null)
                        jsonObject_Post_RecData.put("BRANCH" , cursor_PostRcData.getString(16));
                    else
                        jsonObject_Post_RecData.put("BRANCH" , "");

                    if (cursor_PostRcData.getString(17) != null)
                        jsonObject_Post_RecData.put("CHEQUE_DATE" , cursor_PostRcData.getString(17));
                    else
                        jsonObject_Post_RecData.put("CHEQUE_DATE" , "");

                    if (cursor_PostRcData.getString(18) != null)
                        jsonObject_Post_RecData.put("REFERENCE_NO" , cursor_PostRcData.getString(18));
                    else
                        jsonObject_Post_RecData.put("REFERENCE_NO" , "");

                    if (cursor_PostRcData.getString(19) != null)
                        jsonObject_Post_RecData.put("PAYMENT_DATE" , cursor_PostRcData.getString(19));
                    else
                        jsonObject_Post_RecData.put("PAYMENT_DATE" , "");

                    if (cursor_PostRcData.getString(20) != null)
                        jsonObject_Post_RecData.put("OPERATOR" , cursor_PostRcData.getString(20));
                    else
                        jsonObject_Post_RecData.put("OPERATOR" , "");

                    if (cursor_PostRcData.getString(21) != null)
                        jsonObject_Post_RecData.put("PAYMENT_RECEIVED_FROM_THIRD_PARTY" , cursor_PostRcData.getString(21));
                    else
                        jsonObject_Post_RecData.put("PAYMENT_RECEIVED_FROM_THIRD_PARTY" , "");

                    if (cursor_PostRcData.getString(22) != null)
                        jsonObject_Post_RecData.put("NAME" , cursor_PostRcData.getString(22));
                    else
                        jsonObject_Post_RecData.put("NAME" , "");

                    if (cursor_PostRcData.getString(23) != null)
                        jsonObject_Post_RecData.put("RELATIONSHIP" , cursor_PostRcData.getString(23));
                    else
                        jsonObject_Post_RecData.put("RELATIONSHIP" , "");

                    if (cursor_PostRcData.getString(24) != null)
                        jsonObject_Post_RecData.put("PHONE_NUMBER" , cursor_PostRcData.getString(24));
                    else
                        jsonObject_Post_RecData.put("PHONE_NUMBER" , "");

                    if (cursor_PostRcData.getString(25) != null)
                        jsonObject_Post_RecData.put("ADDRESS" , cursor_PostRcData.getString(25));
                    else
                        jsonObject_Post_RecData.put("ADDRESS" , "");

                    if (cursor_PostRcData.getString(26) != null)
                        jsonObject_Post_RecData.put("PROMISE_TO_PAY_DATE" , cursor_PostRcData.getString(26));
                    else
                        jsonObject_Post_RecData.put("PROMISE_TO_PAY_DATE" , "");

                    if (cursor_PostRcData.getString(27) != null)
                        jsonObject_Post_RecData.put("PTP_AMOUNT" , cursor_PostRcData.getString(27));
                    else
                        jsonObject_Post_RecData.put("PTP_AMOUNT" , cursor_PostRcData.getString(27));

                    if (cursor_PostRcData.getString(28) != null)
                        jsonObject_Post_RecData.put("PTP_CHANNEL_MODE" , cursor_PostRcData.getString(28));
                    else
                        jsonObject_Post_RecData.put("PTP_CHANNEL_MODE" , cursor_PostRcData.getString(28));

                    if (cursor_PostRcData.getString(29) != null)
                        jsonObject_Post_RecData.put("PTP_BRANCH" , cursor_PostRcData.getString(29));
                    else
                        jsonObject_Post_RecData.put("PTP_BRANCH" , "");

                    if (cursor_PostRcData.getString(30) != null)
                        jsonObject_Post_RecData.put("PROMISE_MADE_BY" , cursor_PostRcData.getString(30));
                    else
                        jsonObject_Post_RecData.put("PROMISE_MADE_BY" , "");

                    if (cursor_PostRcData.getString(31) != null)
                        jsonObject_Post_RecData.put("PICK_UP_DATE_AND_TIME" , cursor_PostRcData.getString(31));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_DATE_AND_TIME" , "");

                    if (cursor_PostRcData.getString(32) != null)
                        jsonObject_Post_RecData.put("ADDRESS_FOR_PICK_UP" , cursor_PostRcData.getString(32));
                    else
                        jsonObject_Post_RecData.put("ADDRESS_FOR_PICK_UP" , "");

                    if (cursor_PostRcData.getString(33) != null)
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_BRANCH" , cursor_PostRcData.getString(33));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_BRANCH" , "");

                    if (cursor_PostRcData.getString(34) != null)
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_COLLECTION_EXECUTIVE" , cursor_PostRcData.getString(34));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_REQUIRED_COLLECTION_EXECUTIVE" , "");

                    if (cursor_PostRcData.getString(35) != null)
                        jsonObject_Post_RecData.put("PICK_UP_AMOUNT" , cursor_PostRcData.getString(35));
                    else
                        jsonObject_Post_RecData.put("PICK_UP_AMOUNT" , "");

                    if (cursor_PostRcData.getString(36) != null)
                        jsonObject_Post_RecData.put("ENTRY_NO" , cursor_PostRcData.getString(36));
                    else
                        jsonObject_Post_RecData.put("ENTRY_NO" , "");

                    if (cursor_PostRcData.getString(37) != null)
                        jsonObject_Post_RecData.put("COMPLAINT_DATE" , cursor_PostRcData.getString(37));
                    else
                        jsonObject_Post_RecData.put("COMPLAINT_DATE" , "");

                    if (cursor_PostRcData.getString(38) != null)
                        jsonObject_Post_RecData.put("POLICE_STATION" , cursor_PostRcData.getString(38));
                    else
                        jsonObject_Post_RecData.put("POLICE_STATION" , "");

                    if (cursor_PostRcData.getString(39) != null)
                        jsonObject_Post_RecData.put("INQUIRY_DATE" , cursor_PostRcData.getString(39));
                    else
                        jsonObject_Post_RecData.put("INQUIRY_DATE" , "");

                    if (cursor_PostRcData.getString(40) != null)
                        jsonObject_Post_RecData.put("VEHICLE_MAKE" , cursor_PostRcData.getString(40));
                    else
                        jsonObject_Post_RecData.put("VEHICLE_MAKE" , "");

                    if (cursor_PostRcData.getString(41) != null)
                        jsonObject_Post_RecData.put("MODEL" , cursor_PostRcData.getString(41));
                    else
                        jsonObject_Post_RecData.put("MODEL" , "");

                    if (cursor_PostRcData.getString(42) != null)
                        jsonObject_Post_RecData.put("VARIANT" , cursor_PostRcData.getString(42));
                    else
                        jsonObject_Post_RecData.put("VARIANT" , "");

                    if (cursor_PostRcData.getString(43) != null)
                        jsonObject_Post_RecData.put("VEHICLE_NO" , cursor_PostRcData.getString(43));
                    else
                        jsonObject_Post_RecData.put("VEHICLE_NO" , "");

                    if (cursor_PostRcData.getString(44) != null)
                        jsonObject_Post_RecData.put("ASSET_DATE" , cursor_PostRcData.getString(44));
                    else
                        jsonObject_Post_RecData.put("ASSET_DATE" , "");

                    if (cursor_PostRcData.getString(45) != null)
                        jsonObject_Post_RecData.put("DRIVEN_BY" , cursor_PostRcData.getString(45));
                    else
                        jsonObject_Post_RecData.put("DRIVEN_BY" , "");

                    if (cursor_PostRcData.getString(46) != null)
                        jsonObject_Post_RecData.put("VEHICLE_CONDITION" , cursor_PostRcData.getString(46));
                    else
                        jsonObject_Post_RecData.put("VEHICLE_CONDITION" , "");

                    if (cursor_PostRcData.getString(47) != null)
                        jsonObject_Post_RecData.put("RENTAL_PAYING_BY" , cursor_PostRcData.getString(47));
                    else
                        jsonObject_Post_RecData.put("RENTAL_PAYING_BY" , "");

                    if (cursor_PostRcData.getString(48) != null)
                        jsonObject_Post_RecData.put("OWNERSHIP" , cursor_PostRcData.getString(48));
                    else
                        jsonObject_Post_RecData.put("OWNERSHIP" , "");

                    if (cursor_PostRcData.getString(49) != null)
                        jsonObject_Post_RecData.put("RTP_REASON" , cursor_PostRcData.getString(49));
                    else
                        jsonObject_Post_RecData.put("RTP_REASON" , "");

                    if (cursor_PostRcData.getString(50) != null)
                        jsonObject_Post_RecData.put("PAID_AMOUNT" , cursor_PostRcData.getString(50));
                    else
                        jsonObject_Post_RecData.put("PAID_AMOUNT" , "");

                    if (cursor_PostRcData.getString(51) != null)
                        jsonObject_Post_RecData.put("PAID_CHANNEL_MODE" , cursor_PostRcData.getString(51));
                    else
                        jsonObject_Post_RecData.put("PAID_CHANNEL_MODE" , "");

                    if (cursor_PostRcData.getString(52) != null)
                        jsonObject_Post_RecData.put("PAID_RECEIPT_NO" , cursor_PostRcData.getString(52));
                    else
                        jsonObject_Post_RecData.put("PAID_RECEIPT_NO" , "");

                    if (cursor_PostRcData.getString(53) != null)
                        jsonObject_Post_RecData.put("PAID_COLLECTION_EXECUTIVE_NAME" , cursor_PostRcData.getString(53));
                    else
                        jsonObject_Post_RecData.put("PAID_COLLECTION_EXECUTIVE_NAME" , "");

                    if (cursor_PostRcData.getString(54) != null)
                        jsonObject_Post_RecData.put("FOLLOW_UP_DATE" , cursor_PostRcData.getString(54));
                    else
                        jsonObject_Post_RecData.put("FOLLOW_UP_DATE" , "");

                    if (cursor_PostRcData.getString(55) != null)
                        jsonObject_Post_RecData.put("MEETING_DATE" , cursor_PostRcData.getString(55));
                    else
                        jsonObject_Post_RecData.put("MEETING_DATE" , "");

                    if (cursor_PostRcData.getString(56) != null)
                        jsonObject_Post_RecData.put("COMMENTS" , cursor_PostRcData.getString(56));
                    else
                        jsonObject_Post_RecData.put("COMMENTS" , cursor_PostRcData.getString(57));

                    if (cursor_PostRcData.getString(57) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_ADDRESS" , cursor_PostRcData.getString(57));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_ADDRESS" , "");

                    if (cursor_PostRcData.getString(58) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_LAT" , cursor_PostRcData.getString(58));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_LAT" , "");

                    if (cursor_PostRcData.getString(59) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_LONG" , cursor_PostRcData.getString(59));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_LONG" , "");

                    if (cursor_PostRcData.getString(60) != null)
                        jsonObject_Post_RecData.put("GEO_TAG_PINCODE" , cursor_PostRcData.getString(60));
                    else
                        jsonObject_Post_RecData.put("GEO_TAG_PINCODE" , "");

                    if (cursor_PostRcData.getString(61) != null)
                        jsonObject_Post_RecData.put("CORE_STATUS" , cursor_PostRcData.getString(61));
                    else
                        jsonObject_Post_RecData.put("CORE_STATUS" , "");

                    if (cursor_PostRcData.getString(62) != null)
                        jsonObject_Post_RecData.put("PAYMENT_TYPE" , cursor_PostRcData.getString(62));
                    else
                        jsonObject_Post_RecData.put("PAYMENT_TYPE" , "");

                    if (cursor_PostRcData.getString(63) != null)
                        jsonObject_Post_RecData.put("SERVICE_PROVIDER" , cursor_PostRcData.getString(63));
                    else
                        jsonObject_Post_RecData.put("SERVICE_PROVIDER" , "");

                    if (cursor_PostRcData.getString(64) != null)
                        jsonObject_Post_RecData.put("CONTACTNO_TYPE" , cursor_PostRcData.getString(64));
                    else
                        jsonObject_Post_RecData.put("CONTACTNO_TYPE" , "");

                    if (cursor_PostRcData.getString(65) != null)
                        jsonObject_Post_RecData.put("ADDLINE1" , cursor_PostRcData.getString(65));
                    else
                        jsonObject_Post_RecData.put("ADDLINE1" , "");

                    if (cursor_PostRcData.getString(66) != null)
                        jsonObject_Post_RecData.put("ADDLINE2" , cursor_PostRcData.getString(66));
                    else
                        jsonObject_Post_RecData.put("ADDLINE2" , "");

                    if (cursor_PostRcData.getString(67) != null)
                        jsonObject_Post_RecData.put("ADDLINE3" , cursor_PostRcData.getString(67));
                    else
                        jsonObject_Post_RecData.put("ADDLINE3" , "");

                    if (cursor_PostRcData.getString(68) != null)
                        jsonObject_Post_RecData.put("ADDLINE4" , cursor_PostRcData.getString(68));
                    else
                        jsonObject_Post_RecData.put("ADDLINE4" , "");

                    if (cursor_PostRcData.getString(69) != null)
                        jsonObject_Post_RecData.put("POSTAL_TOWN" , cursor_PostRcData.getString(69));
                    else
                        jsonObject_Post_RecData.put("POSTAL_TOWN" , "");

                    if (cursor_PostRcData.getString(70) != null)
                        jsonObject_Post_RecData.put("POSTAL_CODE" , cursor_PostRcData.getString(70));
                    else
                        jsonObject_Post_RecData.put("POSTAL_CODE" , "");

                    if (cursor_PostRcData.getString(71) != null)
                        jsonObject_Post_RecData.put("MEETING_TIME" , cursor_PostRcData.getString(71));
                    else
                        jsonObject_Post_RecData.put("MEETING_TIME" , "");

                    if (cursor_PostRcData.getString(72) != null)
                        jsonObject_Post_RecData.put("VENUE" , cursor_PostRcData.getString(72));
                    else
                        jsonObject_Post_RecData.put("VENUE" , "");

                    if (cursor_PostRcData.getString(73) != null)
                        jsonObject_Post_RecData.put("HANDOVER_BY" , cursor_PostRcData.getString(73));
                    else
                        jsonObject_Post_RecData.put("HANDOVER_BY" , "");

                    if (cursor_PostRcData.getString(74) != null)
                        jsonObject_Post_RecData.put("HANDOVER_NAME" , cursor_PostRcData.getString(74));
                    else
                        jsonObject_Post_RecData.put("HANDOVER_NAME" , "");

                    if (cursor_PostRcData.getString(75) != null)
                        jsonObject_Post_RecData.put("HANDOVER_NIC" , cursor_PostRcData.getString(75));
                    else
                        jsonObject_Post_RecData.put("HANDOVER_NIC" , "");

                    if (cursor_PostRcData.getString(76) != null)
                        jsonObject_Post_RecData.put("INFORMED_TO" , cursor_PostRcData.getString(76));
                    else
                        jsonObject_Post_RecData.put("INFORMED_TO" , "");

                    if (cursor_PostRcData.getString(77) != null)
                        jsonObject_Post_RecData.put("FOLLOW_UP_TIME" , cursor_PostRcData.getString(77));
                    else
                        jsonObject_Post_RecData.put("FOLLOW_UP_TIME" , "");

                    if (cursor_PostRcData.getString(78) != null)
                        jsonObject_Post_RecData.put("PICKUP_TIME" , cursor_PostRcData.getString(78));
                    else
                        jsonObject_Post_RecData.put("PICKUP_TIME" , "");

                    if (cursor_PostRcData.getString(79) != null)
                        jsonObject_Post_RecData.put("UPDATE_TIME" , cursor_PostRcData.getString(79));
                    else
                        jsonObject_Post_RecData.put("UPDATE_TIME" , "");

                    jsonArray_RecoveryCurserData.put(jsonObject_Post_RecData);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }while (cursor_PostRcData.moveToNext());
        }

        //===Create Manager Request Data
        Cursor cursor_ManagerData = sqLiteDatabase_PostRcData.rawQuery("SELECT * FROM manager_assign_data WHERE Req_User_Id = '" + mInputUserName + "' AND LIVE_SERVER_UPDATE = ''" , null );
        if (cursor_ManagerData.getCount() != 0)
        {
            Log.d("PostOffierData-MgrReg", "CREATED");
            cursor_ManagerData.moveToFirst();

            try {
                JSONObject jsonObject_Post_Manager = new JSONObject();
                jsonObject_Post_Manager.put("Facility_no" , cursor_ManagerData.getString(0));
                jsonObject_Post_Manager.put("Clode" , cursor_ManagerData.getString(1));
                jsonObject_Post_Manager.put("Assign_MgrCode" , cursor_ManagerData.getString(2));
                jsonObject_Post_Manager.put("Assign_MgrName" , cursor_ManagerData.getString(3));
                jsonObject_Post_Manager.put("Assign_Date" , cursor_ManagerData.getString(4));
                jsonObject_Post_Manager.put("Assign_Reason" , cursor_ManagerData.getString(5));
                jsonObject_Post_Manager.put("Req_User_Id" , cursor_ManagerData.getString(6));
                jsonObject_Post_Manager.put("Req_User_Name" , cursor_ManagerData.getString(7));
                jsonObject_Post_Manager.put("Req_Comment" , cursor_ManagerData.getString(8));
                jsonObject_Post_Manager.put("Mgr_Responces" , cursor_ManagerData.getString(10));
                jsonObject_Post_Manager.put("Mgr_Responces_Date" , cursor_ManagerData.getString(11));
                jsonObject_Post_Manager.put("Mgr_Attend" , cursor_ManagerData.getString(12));
                jsonObject_Post_Manager.put("Mgr_Attend_Date" , cursor_ManagerData.getString(13));
                jsonObject_Post_Manager.put("Mgr_Action_Code" , cursor_ManagerData.getString(14));
                jsonObject_Post_Manager.put("Req_Done_Sts" , cursor_ManagerData.getString(15));
                jsonArray_ManagerAssing.put(jsonObject_Post_Manager);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //===Create Manager Request Responces
        Cursor cursor_PostMmger = sqLiteDatabase_PostRcData.rawQuery("SELECT Facility_No,Mgr_Respoce_Sts FROM recovery_request_mgr WHERE Assign_MgrCode = '" + mInputUserName + "' and Mgr_Respoce_Sts != ''", null);
        if (cursor_PostMmger.getCount() != 0)
        {
            Log.d("PostOffierData-MgrResp", "CREATED");
            cursor_PostMmger.moveToFirst();
            do {

                try {
                    JSONObject jsonObject_Post_Manager = new JSONObject();
                    jsonObject_Post_Manager.put("Facility_No", cursor_PostMmger.getString(0));
                    jsonObject_Post_Manager.put("Mgr_Respoce_Sts", cursor_PostMmger.getString(1));
                    jsonArray_ManagerResponces.put(jsonObject_Post_Manager);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor_PostMmger.moveToNext());
        }


        //===Create Facility Repocess Data
        Cursor cursor_RepocessData = sqLiteDatabase_PostRcData.rawQuery("SELECT * FROM recovery_repocess_order where req_user = '" + mInputUserName + "' AND Live_Server_Update = ''" , null );
        if (cursor_RepocessData.getCount() != 0)
        {
            Log.d("PostOffierData-Repocess", "CREATED");
            cursor_RepocessData.moveToFirst();
            do{
                JSONObject jsonObject_Post_Repocess = new JSONObject();
                try {
                    jsonObject_Post_Repocess.put("facility_no" , cursor_RepocessData.getString(0));
                    jsonObject_Post_Repocess.put("clcode" , cursor_RepocessData.getString(1));
                    jsonObject_Post_Repocess.put("client_name" , cursor_RepocessData.getString(2));
                    jsonObject_Post_Repocess.put("vehicle_no" , cursor_RepocessData.getString(3));
                    jsonObject_Post_Repocess.put("sizer_code" , cursor_RepocessData.getString(4));
                    jsonObject_Post_Repocess.put("sizer_name" , cursor_RepocessData.getString(5));
                    jsonObject_Post_Repocess.put("sizer_clcode" , cursor_RepocessData.getString(6));
                    jsonObject_Post_Repocess.put("approve_mgr_code" , cursor_RepocessData.getString(7));
                    jsonObject_Post_Repocess.put("approve_mgr_name" , cursor_RepocessData.getString(8));
                    jsonObject_Post_Repocess.put("req_date" , cursor_RepocessData.getString(10));
                    jsonObject_Post_Repocess.put("req_user" , cursor_RepocessData.getString(11));
                    jsonObject_Post_Repocess.put("request_reason" , cursor_RepocessData.getString(12));
                    jsonObject_Post_Repocess.put("Comment" , cursor_RepocessData.getString(13));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArray_Repocess.put(jsonObject_Post_Repocess);

            }while (cursor_RepocessData.moveToNext());
        }


        //===Create Facility Trvelling
        Cursor cursor_distance = sqLiteDatabase_PostRcData.rawQuery("SELECT * FROM recovery_distance_data WHERE user_id = '" + mInputUserName + "' AND Live_Server_update = ''" , null  );
        if (cursor_distance.getCount() != 0)
        {
            Log.d("PostOffierData-Distance", "CREATED");
            cursor_distance.moveToFirst();
            do{
                JSONObject jsonObject_Post_Distance = new JSONObject();
                try {
                    jsonObject_Post_Distance.put("facility_no" , cursor_distance.getString(0));
                    jsonObject_Post_Distance.put("user_id" , cursor_distance.getString(1));
                    jsonObject_Post_Distance.put("ent_date" , cursor_distance.getString(2));
                    jsonObject_Post_Distance.put("allowance_km_lkr" , cursor_distance.getString(3));
                    jsonObject_Post_Distance.put("food_allow" , cursor_distance.getString(4));
                    jsonObject_Post_Distance.put("no_distance_km" , cursor_distance.getString(5));
                    jsonObject_Post_Distance.put("in_time" , cursor_distance.getString(6));
                    jsonObject_Post_Distance.put("out_time" , cursor_distance.getString(7));
                    jsonObject_Post_Distance.put("visit_date" , cursor_distance.getString(8));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray_Trvelling.put(jsonObject_Post_Distance);

            }while (cursor_distance.moveToNext());
        }

        JSONObject jsonObject_PostRecoveryData  = new JSONObject();
        //=========== Create Final Json Object ====
        try {
            jsonObject_PostRecoveryData.put("RECOVERY_ACTION" , jsonArray_RecoveryCurserData);
            jsonObject_PostRecoveryData.put("MANAGER_ASSIGN" , jsonArray_ManagerAssing);
            jsonObject_PostRecoveryData.put("MANAGER_RESPONCES" , jsonArray_ManagerResponces);
            jsonObject_PostRecoveryData.put("REC-REPOCESS-DATA" , jsonArray_Repocess);
            jsonObject_PostRecoveryData.put("REC-DISTANCE-DATA" , jsonArray_Trvelling);
            jsonObject_PostRecoveryData.put("REC-RECEIPT-DATA" , jsonArray_recipt_Data);
            jsonObject_PostRecoveryData.put("REC-IMAGE-DATA" , jsonArray_image_Data);
            jsonObject_PostRecoveryData.put("REC-DEPOSIT" , jsonArray_Cash_deposit);
            jsonObject_PostRecoveryData.put("REC-DEPOSIT-DETAILS" , jsonArray_Cash_deposit_details);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //===== Create Volly Request =====
        //==== Create Json Request To Send Live Database...
        String Urlapplication = PHP_URL_SQL + "RECOVERY-POST-HOME-SYNC-DATA.php";
        jsonObjectRequest_PostRecoveryData = new JsonObjectRequest(Request.Method.POST, Urlapplication, jsonObject_PostRecoveryData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            PostDataResponces = response.getString("RESULT");
                            Log.d("PostOffierData" , PostDataResponces);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String ErrorCode="";
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    ErrorCode = "No Internet Connection;";
                } else if (error instanceof AuthFailureError) {
                    ErrorCode = "An expired login session";
                } else if (error instanceof ServerError) {
                    ErrorCode = "Server is down or is unable to process the request;";
                } else if (error instanceof NetworkError) {
                    ErrorCode = "Very slow internet connection;";
                } else if (error instanceof ParseError) {
                    ErrorCode = "Client not able to parse(read) the response;";
                }

                progressDialog.dismiss();
                String ErrorDescription="Responses Failure.(" + ErrorCode.toString() + ")";
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex );
                bmyAlert.setMessage(ErrorDescription).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("Error... 1")
                        .create();
                bmyAlert.show();
            }
        });
        jsonObjectRequest_PostRecoveryData.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        progressDialog.show();
        mQueue.start();
        mQueue.add(jsonObjectRequest_PostRecoveryData);  //=== Appliocation request

        //==== Fineash Request
        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                boolean CheckResponce=false;
                if (PostDataResponces != null)
                {
                    CheckResponce = PostDataResponces.equals("DONE");
                }

                if (CheckResponce == true)
                {
                    progressDialog.dismiss();
                    mQueue.getCache().clear();


                    //==== Update Table Flag
                    SQLiteDatabase sqLiteDatabase_update_flag = sqlliteCreateRecovery_PostDataSync.getWritableDatabase();

                    //recovery_recipt
                    ContentValues contentValues_receipt = new ContentValues();
                    contentValues_receipt.put("Live_server_update" , "Y");
                    sqLiteDatabase_update_flag.update("recovery_recipt " ,contentValues_receipt , "Live_server_update  = ''" , null);

                    //nemf_form_updater
                    ContentValues contentValues_nemf_form_updater = new ContentValues();
                    contentValues_nemf_form_updater.put("LIVE_SERVER_UPDATE " , "Y");
                    sqLiteDatabase_update_flag.update("nemf_form_updater" ,contentValues_receipt , "MADE_BY  = ?" , new String[]{mInputUserName});

                    //manager_assign_data
                    ContentValues contentValues_manager_assign_data = new ContentValues();
                    contentValues_manager_assign_data.put("LIVE_SERVER_UPDATE" , "Y");
                    sqLiteDatabase_update_flag.update("manager_assign_data" , contentValues_manager_assign_data , "Req_User_Id  = ?" , new String[]{mInputUserName});

                    //recovery_request_mgr
                    ContentValues contentValues_recovery_request_mgr = new ContentValues();
                    contentValues_recovery_request_mgr.put("Live_Server_update" , "Y");
                    sqLiteDatabase_update_flag.update("recovery_request_mgr" , contentValues_manager_assign_data , "Assign_MgrCode = ?" , new String[]{mInputUserName});


                    //recovery_repocess_order
                    ContentValues contentValues_recovery_repocess_order  = new ContentValues();
                    contentValues_recovery_repocess_order.put("Live_Server_Update" , "Y");
                    sqLiteDatabase_update_flag.update("recovery_repocess_order " , contentValues_manager_assign_data , "req_user = ?" , new String[]{mInputUserName});

                    //recovery_distance_data
                    ContentValues contentValues_recovery_distance_data = new ContentValues();
                    contentValues_recovery_distance_data.put("Live_Server_update" , "Y");
                    sqLiteDatabase_update_flag.update("recovery_distance_data " , contentValues_manager_assign_data , "user_id = ?" , new String[]{mInputUserName});


                    //recovery_Image_data
                    ContentValues contentValues_image_data = new ContentValues();
                    contentValues_image_data.put("Live_Server_Update" , "Y");
                    sqLiteDatabase_update_flag.update("recoveery_doc_uplaod" , contentValues_image_data , "Live_Server_Update = ''" , null);

                    //recovery_cash_deposit
                    ContentValues contentValues_deposit = new ContentValues();
                    contentValues_deposit.put("Live_server_update" , "Y");
                    sqLiteDatabase_update_flag.update("recovery_cash_deposit" , contentValues_deposit , "Live_Server_Update = ''" , null);

                    //recovery_cash_deposit
                    ContentValues contentValues_deposit_details = new ContentValues();
                    contentValues_deposit_details.put("Live_server_update" , "Y");
                    sqLiteDatabase_update_flag.update("recovery_deposit_details" , contentValues_deposit_details , "Live_server_update = ''" , null);


                    //===Update sysnc time
                    ContentValues contentValues_ypdate_sync = new ContentValues();
                    contentValues_ypdate_sync.put("sync_date" , LoginDate);
                    contentValues_ypdate_sync.put("sync_type" , "Y");
                    sqLiteDatabase_update_flag.insert("masr_data_sync_update" , null , contentValues_ypdate_sync);

                    sqLiteDatabase_update_flag.close();

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContex);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("File Complete Successfully.");
                    builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        });
    }
}
