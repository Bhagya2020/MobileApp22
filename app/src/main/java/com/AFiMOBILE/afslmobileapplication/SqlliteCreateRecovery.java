package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SqlliteCreateRecovery extends SQLiteOpenHelper {

    //============ CREATE DATABSE ==============================
    public static final String DATABASE_NAME_RECOVERY = "AFSL_MOB_RECOVERY.db";

    public SqlliteCreateRecovery(Context ctx) {
        super(ctx, DATABASE_NAME_RECOVERY, null, 1);

        SQLiteDatabase db_Recovery_Recovery = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db_Recovery)
    {
        db_Recovery.execSQL("create table masr_actioncode" + "(action_code TEXT , action_descr TEXT)");
        db_Recovery.execSQL("create table masr_actioncodeptp" + "(actionptp_code TEXT , actionptp_descr TEXT)");
        db_Recovery.execSQL("create table masr_channelmode" + "(channelmode_code TEXT , channelmode_descr TEXT)");
        db_Recovery.execSQL("create table masr_drivenby" + "(drivenby_code TEXT , drivenby_descr TEXT)");
        db_Recovery.execSQL("create table masr_ownership" + "(ownership_code TEXT , ownership_descr TEXT)");
        db_Recovery.execSQL("create table masr_paymentmode" + "(paymentmode_code TEXT , paymentmode_descr TEXT)");
        db_Recovery.execSQL("create table masr_relationship" + "(relationship_code TEXT , relationship_descr TEXT)");
        db_Recovery.execSQL("create table masr_vehiclecondition" + "(vehiclecon_code TEXT , vehiclecon_descr TEXT)");
        db_Recovery.execSQL("create table masr_sizer" + "(sizer_Code TEXT , sizer_name TEXT , sizer_nic TEXT , sizer_adders TEXT , sizer_area TEXT , sizer_clcode TEXT , sizer_sts TEXT)");



        db_Recovery.execSQL("create table recovery_generaldetail" + "(Client_Name TEXT , Product_Type TEXT , Asset_Model TEXT , Vehicle_Number TEXT , Due_Day TEXT , Current_Month_Rental TEXT ," +
            "OD_Arrear_Amount TEXT , Total_Arrear_Amount TEXT , Last_Payment_Date TEXT , Last_Payment_Amount TEXT , Facility_Payment_Status TEXT , Letter_Demand_Date TEXT , Client_Code TEXT ," +
            "NIC TEXT , Customer_Full_Name TEXT , Gender TEXT , Occupation TEXT , Work_Place_Name TEXT , Work_Place_Address TEXT , Mobile_No1 TEXT , Mobile_No2 TEXT , Home_No TEXT ,C_Address_Line_1 TEXT ," +
            "C_Address_Line_2 TEXT , C_Address_Line_3 TEXT , C_Address_Line_4 TEXT , C_Postal_Town TEXT , P_Address_Line_1 TEXT , P_Address_Line_2 TEXT , P_Address_Line_3 TEXT , P_Address_Line_4 TEXT ," +
            "P_Postal_Town TEXT , Facility_Number TEXT , Facility_Amount TEXT , Facility_Branch TEXT , Tenure TEXT , Recovery_Executive TEXT , Recovery_Executive_Name TEXT , CC_Executive TEXT , Marketing_Executive TEXT ," +
            "Follow_Up_Officer TEXT , Recovery_Executive_Phone TEXT , CC_Executive_Phone TEXT , Marketing_Executive_Phone TEXT , Follow_Up_Officer_Phone TEXT , Activation_Date TEXT , Expiry_Date TEXT , Total_Facility_Amount TEXT ," +
            "Disbursed_Amount TEXT , Disbursement_Status TEXT , Interest_Rate TEXT , Rental TEXT , Contract_Status TEXT , Facility_Status TEXT , Down_Payment_No TEXT , Rentals_Matured_No TEXT , Rentals_Paid_No TEXT , Arrears_Rental_No TEXT ," +
            "SPDC_Available TEXT , Total_Outstanding TEXT , Capital_Outstanding TEXT , Interest_Outstanding TEXT , Arrears_Rental_Amount TEXT , OD_Interest TEXT , Interest_Arrears TEXT , Insurance_Arrears TEXT , OD_Interest_OS TEXT , Seizing_Charges TEXT ," +
            "OC_Arrears TEXT , FUTURE_CAPITAL TEXT , FUTURE_INTEREST TEXT , TOTAL_SETTL_AMT TEXT )");


        db_Recovery.execSQL("create table recovery_coapp_guarantor" + "(Client_Code TEXT , NIC TEXT , Customer_Full_Name TEXT , Gender TEXT , Occupation TEXT , Work_Place_Name TEXT , Work_Place_Address TEXT , Mobile_No1 TEXT , Mobile_No2 TEXT , " +
                "Home_No TEXT , C_Address_Line_1 TEXT , C_Address_Line_2 TEXT , C_Address_Line_3 TEXT , C_Address_Line_4 TEXT , C_Postal_Town TEXT , P_Address_Line_1 TEXT , P_Address_Line_2 TEXT , P_Address_Line_3 TEXT , P_Address_Line_4 TEXT , " +
                "P_Postal_Town TEXT , Relationship_Applicant TEXT , Facility_Number TEXT )");

        db_Recovery.execSQL("create table recovery_notepad_history" + "(FACILITY_NO TEXT , EXECUTIVE_ID TEXT , NOTE_DATE TEXT , NOTE_DESC TEXT , NOTE_SER TEXT)");

        db_Recovery.execSQL("create table recovery_paymentdetail" + "(Receipt_Number TEXT , Payment_Date_Time TEXT , Payment_Mode TEXT , Amount TEXT , Payment_Channel TEXT , Remarks TEXT , Realized TEXT , Facility_Number TEXT)");

        db_Recovery.execSQL("create table recovery_geotagging" + "(Facility_Number TEXT , Latitude TEXT , Longitude TEXT , Address TEXT)");

        db_Recovery.execSQL("create table recovery_ptp_details" + "(ID TEXT , FACILITY_NO TEXT , ACTION_TAKEN TEXT , CLOSURE_ACTION TEXT , CLOSURE_COMMENTS TEXT , PROMISE_TO_PAY_DATE TEXT , PTP_AMOUNT TEXT , PTP_PAYMENT_MODE TEXT , PTP_CHANNEL_MODE TEXT , PTP_BRANCH TEXT , " +
                "PROMISE_MADE_BY TEXT , COMMENTS TEXT , ACTION_DATE TEXT)");

        db_Recovery.execSQL("create table recovery_action_details" + "(Facility_Number TEXT , ActionCode TEXT ,ActionAssignSizer TEXT , ActionAssDate TEXT , ApproveMgrCode TEXT , BranchCode Text , ActionStatus TEXT , AcctionComment Text , Userid Text , ReqDate TEXT  )");

        db_Recovery.execSQL("create table nemf_form_updater" + "(ID TEXT , FACILITY_NO TEXT , ACTIONCODE TEXT , ACTION_DATE TEXT , MADE_BY TEXT , CURRENT_BUCKET TEXT , LINKED_ID TEXT , CNAME TEXT , COLLECTION_ENTRY_TYPE TEXT , MODE_OF_PAYMENT TEXT , COLLECTION_AMOUNT TEXT , COLLECTION_ENTRY_DATE TEXT , " +
                "MANUAL_RECEIPT_NO TEXT, PROVISIONAL_RECEIPT_NO TEXT, CHEQUE_NO TEXT, CHEQUE_BANK TEXT, BRANCH TEXT, CHEQUE_DATE TEXT, REFERENCE_NO TEXT, PAYMENT_DATE TEXT, OPERATOR TEXT, PAYMENT_RECEIVED_FROM_THIRD_PARTY TEXT, NAME TEXT, RELATIONSHIP TEXT, PHONE_NUMBER TEXT, ADDRESS TEXT, PROMISE_TO_PAY_DATE TEXT, " +
                "PTP_AMOUNT TEXT , PTP_CHANNEL_MODE TEXT , PTP_BRANCH TEXT , PROMISE_MADE_BY TEXT , PICK_UP_DATE_AND_TIME TEXT , ADDRESS_FOR_PICK_UP TEXT , PICK_UP_REQUIRED_BRANCH TEXT , PICK_UP_REQUIRED_COLLECTION_EXECUTIVE TEXT , PICK_UP_AMOUNT TEXT , ENTRY_NO TEXT , COMPLAINT_DATE TEXT , POLICE_STATION TEXT , INQUIRY_DATE TEXT , " +
                "VEHICLE_MAKE TEXT , MODEL TEXT , VARIANT TEXT , VEHICLE_NO TEXT , ASSET_DATE TEXT , DRIVEN_BY TEXT , VEHICLE_CONDITION TEXT , RENTAL_PAYING_BY TEXT , OWNERSHIP TEXT , RTP_REASON TEXT , PAID_AMOUNT TEXT , PAID_CHANNEL_MODE TEXT , PAID_RECEIPT_NO TEXT , PAID_COLLECTION_EXECUTIVE_NAME TEXT , FOLLOW_UP_DATE TEXT , MEETING_DATE TEXT , " +
                "COMMENTS TEXT , GEO_TAG_ADDRESS TEXT , GEO_TAG_LAT TEXT , GEO_TAG_LONG TEXT , GEO_TAG_PINCODE TEXT , CORE_STATUS TEXT , PAYMENT_TYPE TEXT , SERVICE_PROVIDER TEXT , CONTACTNO_TYPE TEXT , ADDLINE1 TEXT , ADDLINE2 TEXT , ADDLINE3 TEXT , ADDLINE4 TEXT , POSTAL_TOWN TEXT , POSTAL_CODE TEXT , MEETING_TIME TEXT , VENUE TEXT , " +
                "HANDOVER_BY TEXT , HANDOVER_NAME TEXT , HANDOVER_NIC TEXT , INFORMED_TO TEXT , FOLLOW_UP_TIME TEXT , PICKUP_TIME TEXT , UPDATE_TIME TEXT , FAC_LOCK TEXT , LIVE_SERVER_UPDATE TEXT )");


        db_Recovery.execSQL("create table recovery_search_data" + "(Client_Name TEXT , Product_Type TEXT , Asset_Model TEXT , Vehicle_Number TEXT , Due_Day TEXT , Current_Month_Rental TEXT ," +
                "OD_Arrear_Amount TEXT , Total_Arrear_Amount TEXT , Last_Payment_Date TEXT , Last_Payment_Amount TEXT , Facility_Payment_Status TEXT , Letter_Demand_Date TEXT , Client_Code TEXT ," +
                "NIC TEXT , Customer_Full_Name TEXT , Gender TEXT , Occupation TEXT , Work_Place_Name TEXT , Work_Place_Address TEXT , Mobile_No1 TEXT , Mobile_No2 TEXT , Home_No TEXT ,C_Address_Line_1 TEXT ," +
                "C_Address_Line_2 TEXT , C_Address_Line_3 TEXT , C_Address_Line_4 TEXT , C_Postal_Town TEXT , P_Address_Line_1 TEXT , P_Address_Line_2 TEXT , P_Address_Line_3 TEXT , P_Address_Line_4 TEXT ," +
                "P_Postal_Town TEXT , Facility_Number TEXT , Facility_Amount TEXT , Facility_Branch TEXT , Tenure TEXT , Recovery_Executive TEXT , Recovery_Executive_Name TEXT , CC_Executive TEXT , Marketing_Executive TEXT ," +
                "Follow_Up_Officer TEXT , Recovery_Executive_Phone TEXT , CC_Executive_Phone TEXT , Marketing_Executive_Phone TEXT , Follow_Up_Officer_Phone TEXT , Activation_Date TEXT , Expiry_Date TEXT , Total_Facility_Amount TEXT ," +
                "Disbursed_Amount TEXT , Disbursement_Status TEXT , Interest_Rate TEXT , Rental TEXT , Contract_Status TEXT , Facility_Status TEXT , Down_Payment_No TEXT , Rentals_Matured_No TEXT , Rentals_Paid_No TEXT , Arrears_Rental_No TEXT ," +
                "SPDC_Available TEXT , Total_Outstanding TEXT , Capital_Outstanding TEXT , Interest_Outstanding TEXT , Arrears_Rental_Amount TEXT , OD_Interest TEXT , Interest_Arrears TEXT , Insurance_Arrears TEXT , OD_Interest_OS TEXT , Seizing_Charges TEXT ," +
                "OC_Arrears TEXT , FUTURE_CAPITAL TEXT , FUTURE_INTEREST TEXT , TOTAL_SETTL_AMT TEXT )");


        db_Recovery.execSQL("create table recovery_user_group" + "(rec_manager TEXT , rec_manager_name TEXT , rec_manager_phoneno TEXT , ass_officer TEXT , ass_offier_name TEXT , ass_officer_phoneno TEXT , " +
                "reginal_manger TEXT , reginal_manager_name TEXT , reginal_manager_phoneno TEXT , user_sts TEXT)");

        db_Recovery.execSQL("create table manager_assign_data" + "(Facility_no TEXT , Clode TEXT , Assign_MgrCode TEXT , Assign_MgrName TEXT , Assign_Date TEXT , Assign_Reason TEXT , Req_User_Id TEXT , Req_User_Name TEXT ," +
                " Req_Comment TEXT , Lock_Sts TEXT , Mgr_Responces Text , Mgr_Responces_Date TEXT , Mgr_Attend TEXT , Mgr_Attend_Date TEXT , Mgr_Action_Code TEXT , Req_Done_Sts TEXT , LIVE_SERVER_UPDATE TEXT )");


        db_Recovery.execSQL("create table recovery_request_mgr" + "(Facility_No TEXT , Assign_MgrCode TEXT , Request_userid TEXT , Request_user_name TEXT , Request_Date TEXT , Requet_Reason TEXT , Request_comment text , Mgr_Respoce_Sts TEXT ," +
                "Mgr_Action TEXT , Req_done_sts TEXT , Live_Server_update TEXT   )");


        db_Recovery.execSQL("create table recovery_repocess_order" + "(facility_no TEXT , clcode TEXT , client_name TEXT , vehicle_no TEXT , sizer_code TEXT , sizer_name TEXT , sizer_clcode TEXT , approve_mgr_code TEXT , approve_mgr_name TEXT , Live_Server_Update TEXT , req_date TEXT , req_user TEXT , request_reason TEXT , Comment TEXT )");

        db_Recovery.execSQL("create table recovery_visit_plan" + "(facility_no TEXT , user_id TEXT , plan_Date TEXT , action_code TEXT , plan_sts TEXT )");

        db_Recovery.execSQL("create table recovery_distance_data" + "(facility_no TEXT , user_id TEXT , ent_date TEXT , allowance_km_lkr TEXT , food_allow TEXT , no_distance_km TEXT , in_time TEXT , out_time TEXT , visit_date TEXT , Lock_Record TEXT , Live_Server_update TEXT )");

        db_Recovery.execSQL("create table recovery_recipt" + "(rcpt_refno TEXT , rcpt_date TEXT , rcpt_amt TEXT , rcpt_time TEXT , rcpt_pay_mode TEXT , rcpt_facno TEXT , rcpt_branch_code TEXT , rcpt_user_id TEXT , rcpt_des TEXT , manual_rcptno TEXT , rcpt_bank_code TEXT , Live_server_update TEXT , dep_sts TEXT)");

        db_Recovery.execSQL("create table masr_sequence" + "(seq_code TEXT , seq_count TEXT)");

        db_Recovery.execSQL("create table recoveery_doc_uplaod" + "(referance_no Text , facility_no  TEXT , doc_type TEXT , doc_image TEXT , doc_date TEXT , doc_useruid TEXT , Live_Server_Update TEXT )");

        db_Recovery.execSQL("create table masr_cash_wallet" + "(offier_code TEXT , officer_name TEXT , officer_branch TEXT , offier_manager TEXT , officer_wallet_limit TEXT , offier_nic TEXT , Sts TEXT)");

        db_Recovery.execSQL("create table recovery_cash_deposit" + "(dep_refno TEXT , dep_date TEXT , dep_time TEXT , dep_bank TEXT , dep_amt TEXT , dep_sts TEXT , dep_user TEXT , dep_user_br_code TEXT , Live_server_update TEXT)");

        db_Recovery.execSQL("create table recovery_deposit_details" + "(dep_refno TEXT , receipt_entry_no TEXT , receipt_date TEXT , receipt_amt TEXT , dep_user TEXT , br_code TEXT , Live_server_update TEXT)");

        db_Recovery.execSQL("create table masr_data_sync_update" + "(sync_date TEXT , sync_type TEXT)");

        db_Recovery.execSQL("create table Recovery_action_history" + "(Facility_no TEXT , ActionDate TEXT , ActionUser TEXT , ActionCode TEXT , ActionComment TEXT)");

        db_Recovery.execSQL("create table Recovery_speical_backet" + "( facility_no TEXT , client_name TEXT , recovery_executive TEXT , branch_code TEXT , recovery_bucket TEXT ,  Live_server_update TEXT)");

        db_Recovery.execSQL("create table Recovery_Month_Begin" + "( facility_no TEXT , No_Rnt_arrays TEXT , recovery_executive TEXT , product_code TEXT , duedate TEXT , Total_arrays TEXT , OD_arrays TEXT , Rental_arrays TEXT , Other_arrays TEXT , Process_Month TEXT , CO_system_Flg TEXT )");

        db_Recovery.execSQL("create table Officer_recoey_visit" + "(Officer_id TEXT , Id_sequence TEXT , Table_id TEXT , Facility_no TEXT , Action_code TEXT , Action_date TEXT , Vehicle_no TEXT , Get_tag_lat TEXT , Geo_Tag_lon TEXT )");

        db_Recovery.execSQL("create table Distance_Details" + "(Officer_id TEXT , Id_sequence TEXT ,  Facility_no TEXT , Action_code TEXT , Action_date TEXT  )");

        db_Recovery.execSQL("create table Monotrium_facility" + "(Main_Facility TEXT , Link_facility_le TEXT , Link_Facility_lt TEXT )");


        db_Recovery.execSQL("create table CallCenter_recovery_generaldetail" + "(Client_Name TEXT , Product_Type TEXT , Asset_Model TEXT , Vehicle_Number TEXT , Due_Day TEXT , Current_Month_Rental TEXT ," +
                "OD_Arrear_Amount TEXT , Total_Arrear_Amount TEXT , Last_Payment_Date TEXT , Last_Payment_Amount TEXT , Facility_Payment_Status TEXT , Letter_Demand_Date TEXT , Client_Code TEXT ," +
                "NIC TEXT , Customer_Full_Name TEXT , Gender TEXT , Occupation TEXT , Work_Place_Name TEXT , Work_Place_Address TEXT , Mobile_No1 TEXT , Mobile_No2 TEXT , Home_No TEXT ,C_Address_Line_1 TEXT ," +
                "C_Address_Line_2 TEXT , C_Address_Line_3 TEXT , C_Address_Line_4 TEXT , C_Postal_Town TEXT , P_Address_Line_1 TEXT , P_Address_Line_2 TEXT , P_Address_Line_3 TEXT , P_Address_Line_4 TEXT ," +
                "P_Postal_Town TEXT , Facility_Number TEXT , Facility_Amount TEXT , Facility_Branch TEXT , Tenure TEXT , Recovery_Executive TEXT , Recovery_Executive_Name TEXT , CC_Executive TEXT , Marketing_Executive TEXT ," +
                "Follow_Up_Officer TEXT , Recovery_Executive_Phone TEXT , CC_Executive_Phone TEXT , Marketing_Executive_Phone TEXT , Follow_Up_Officer_Phone TEXT , Activation_Date TEXT , Expiry_Date TEXT , Total_Facility_Amount TEXT ," +
                "Disbursed_Amount TEXT , Disbursement_Status TEXT , Interest_Rate TEXT , Rental TEXT , Contract_Status TEXT , Facility_Status TEXT , Down_Payment_No TEXT , Rentals_Matured_No TEXT , Rentals_Paid_No TEXT , Arrears_Rental_No TEXT ," +
                "SPDC_Available TEXT , Total_Outstanding TEXT , Capital_Outstanding TEXT , Interest_Outstanding TEXT , Arrears_Rental_Amount TEXT , OD_Interest TEXT , Interest_Arrears TEXT , Insurance_Arrears TEXT , OD_Interest_OS TEXT , Seizing_Charges TEXT ," +
                "OC_Arrears TEXT , FUTURE_CAPITAL TEXT , FUTURE_INTEREST TEXT , TOTAL_SETTL_AMT TEXT , LAST_MON_ACTION TEXT , LAST_MON_ACT_DATE TEXT , CUR_MON_ACTION TEXT , CUR_MON_ACT_DATE TEXT )");


        db_Recovery.execSQL("create table recovery_link_facility_details" + "(Client_Name TEXT , Product_Type TEXT , Asset_Model TEXT , Vehicle_Number TEXT , Due_Day TEXT , Current_Month_Rental TEXT ," +
                "OD_Arrear_Amount TEXT , Total_Arrear_Amount TEXT , Last_Payment_Date TEXT , Last_Payment_Amount TEXT , Facility_Payment_Status TEXT , Letter_Demand_Date TEXT , Client_Code TEXT ," +
                "NIC TEXT , Customer_Full_Name TEXT , Gender TEXT , Occupation TEXT , Work_Place_Name TEXT , Work_Place_Address TEXT , Mobile_No1 TEXT , Mobile_No2 TEXT , Home_No TEXT ,C_Address_Line_1 TEXT ," +
                "C_Address_Line_2 TEXT , C_Address_Line_3 TEXT , C_Address_Line_4 TEXT , C_Postal_Town TEXT , P_Address_Line_1 TEXT , P_Address_Line_2 TEXT , P_Address_Line_3 TEXT , P_Address_Line_4 TEXT ," +
                "P_Postal_Town TEXT , Facility_Number TEXT , Facility_Amount TEXT , Facility_Branch TEXT , Tenure TEXT , Recovery_Executive TEXT , Recovery_Executive_Name TEXT , CC_Executive TEXT , Marketing_Executive TEXT ," +
                "Follow_Up_Officer TEXT , Recovery_Executive_Phone TEXT , CC_Executive_Phone TEXT , Marketing_Executive_Phone TEXT , Follow_Up_Officer_Phone TEXT , Activation_Date TEXT , Expiry_Date TEXT , Total_Facility_Amount TEXT ," +
                "Disbursed_Amount TEXT , Disbursement_Status TEXT , Interest_Rate TEXT , Rental TEXT , Contract_Status TEXT , Facility_Status TEXT , Down_Payment_No TEXT , Rentals_Matured_No TEXT , Rentals_Paid_No TEXT , Arrears_Rental_No TEXT ," +
                "SPDC_Available TEXT , Total_Outstanding TEXT , Capital_Outstanding TEXT , Interest_Outstanding TEXT , Arrears_Rental_Amount TEXT , OD_Interest TEXT , Interest_Arrears TEXT , Insurance_Arrears TEXT , OD_Interest_OS TEXT , Seizing_Charges TEXT ," +
                "OC_Arrears TEXT , FUTURE_CAPITAL TEXT , FUTURE_INTEREST TEXT , TOTAL_SETTL_AMT TEXT )");

        db_Recovery.execSQL("create table recovery_call_center_action" + "(FACNO TEXT , ALOCATE_OFF_CODE TEXT , ALOCATE_OFF_NAME TEXT , ALOCATE_BRANCH TEXT , ALOCATE_DATE TEXT , " +
                "CALL_ACTION_CODE TEXT , CALL_ACTION_DATE TEXT , CALL_ACTION_USER TEXT , CALL_PTP_DATE TEXT , CALL_PTP_AMOUNT TEXT , CALL_REMARKS TEXT , PAY_DATE TEXT , PAY_AMOUNT TEXT , ACTION_STS TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db_Recovery, int oldVersion, int newVersion)
    {

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_actioncode");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_actioncodeptp");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_channelmode");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_drivenby");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_ownership");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_paymentmode");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_relationship");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_vehiclecondition");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_generaldetail");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_coapp_guarantor");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_notepad_history");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_paymentdetail");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_geotagging");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_ptp_details");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_action_details");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS nemf_form_updater");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_search_data");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_user_group");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS manager_assign_data");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_request_mgr");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_sizer");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_repocess_order");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_visit_plan");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_distance_data");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_recipt");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recoveery_doc_uplaod");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_cash_wallet");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_cash_deposit");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_deposit_details");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS masr_data_sync_update");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS Recovery_action_history");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS Recovery_speical_backet");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS Recovery_Month_Begin");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS Officer_recoey_visit");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS Distance_Details");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS Monotrium_facility");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS CallCenter_recovery_generaldetail");
        onCreate(db_Recovery);

        db_Recovery.execSQL("DROP TABLE IF EXISTS recovery_call_center_action");
        onCreate(db_Recovery);
   }

    //=== Delete the table data
    public void DeleteData (String tablename)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(tablename , null , null);
    }

    //==== Master Data Upload ============
     // Recovery User Group
    public List<String> GetRecoveryUser(String mLoginCode)
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM recovery_user_group where ass_officer = '" + mLoginCode + "'" ;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }



    public List<String> GetPayPromise()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM masr_channelmode";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }



    public List<String> GetChanalMode()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM masr_channelmode";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public List<String> GetRelastionType()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM masr_relationship";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public List<String> GetPaymentMode()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM masr_ownership";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public List<String> GetVehicleOwnShip()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM masr_ownership";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public List<String> GetVehicleCondtion()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM masr_vehiclecondition";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public List<String> GetAllSizer(String Brcode)
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM masr_sizer where sizer_area = '" + Brcode + "'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }


    public List<String> GetAllDrivenBy()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM masr_drivenby";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public List<String> GetAllActionCode()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM masr_actioncode";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }


    public void InsertWallet(String Inpoffcode , String InpOffName , String InpOffBranch , String InpManager , String InpWallAmt , String inpOffnic , String InpSts)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues_wallet =   new ContentValues();
        contentValues_wallet.put("offier_code" , Inpoffcode);
        contentValues_wallet.put("officer_name" , InpOffName);
        contentValues_wallet.put("officer_branch" , InpOffBranch);
        contentValues_wallet.put("offier_manager" , InpManager);
        contentValues_wallet.put("officer_wallet_limit" , InpWallAmt);
        contentValues_wallet.put("offier_nic" , inpOffnic);
        contentValues_wallet.put("Sts" , InpSts);
        sqLiteDatabase.insert("masr_cash_wallet" , null , contentValues_wallet);
    }


    public void InsertSizerData(String Inputscode , String InpSName , String InputsNic , String InputsAdders , String InpSArea , String InputClcode , String InputSSts)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("sizer_code" , Inputscode);
        contentValues.put("sizer_name" , InpSName);
        contentValues.put("sizer_nic" , InputsNic);
        contentValues.put("sizer_adders" , InputsAdders);
        contentValues.put("sizer_area" , InpSArea);
        contentValues.put("sizer_clcode" , InputClcode);
        contentValues.put("sizer_sts" , InputSSts);
        sqLiteDatabase.insert("masr_sizer" , null , contentValues);
    }



    public void InsertRequestMgrData(String inpFacno , String MgrCode , String inpAssdate , String inpReason , String inpReqId , String inpReqName , String inpcomment)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("Facility_No" , inpFacno);
        contentValues.put("Assign_MgrCode" , MgrCode);
        contentValues.put("Request_userid" , inpReqId);
        contentValues.put("Request_user_name" , inpReqName);
        contentValues.put("Request_Date" , inpAssdate);
        contentValues.put("Requet_Reason" , inpReason);
        contentValues.put("Request_comment" , inpcomment);
        contentValues.put("Mgr_Respoce_Sts" , "");
        contentValues.put("Mgr_Action" , "");
        contentValues.put("Req_done_sts" , "");
        contentValues.put("Live_Server_update" , "");
        sqLiteDatabase.insert("recovery_request_mgr" , null , contentValues);

    }



    public void InsertRecoveryUser(String InpMgrCode , String InpMgrName , String InpMgrPhone , String InpUserCode , String InpUserName , String InpUserPhone , String InpReginacode , String InpReginalName , String InpREginbalPhone)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("rec_manager" , InpMgrCode);
        contentValues.put("rec_manager_name" , InpMgrName);
        contentValues.put("rec_manager_phoneno" , InpMgrPhone);
        contentValues.put("ass_officer" , InpUserCode);
        contentValues.put("ass_offier_name" , InpUserName);
        contentValues.put("ass_officer_phoneno" , InpUserPhone);
        contentValues.put("reginal_manger" , InpReginacode);
        contentValues.put("reginal_manager_name" , InpReginalName);
        contentValues.put("reginal_manager_phoneno" , InpREginbalPhone);
        sqLiteDatabase.insert("recovery_user_group" , null , contentValues);

    }


    public void InsertActionHistory(String mInpFacno , String mInpData , String mInpUser , String mInpActioncode , String mInpComment)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("Facility_no" , mInpFacno);
        contentValues.put("ActionDate" , mInpData);
        contentValues.put("ActionUser" , mInpUser);
        contentValues.put("ActionCode" , mInpActioncode);
        contentValues.put("ActionComment" , mInpComment);
        sqLiteDatabase.insert("Recovery_action_history" , null , contentValues);
    }


    public void InsertCoAppDetails(String mFacility_Number , String mRelationship_Applicant , String mCustomer_Full_Name , String mC_Address_Line_1 , String mC_Address_Line_2 , String mC_Address_Line_3 ,
                                   String mC_Address_Line_4 , String mNIC , String mClient_Code , String mGender , String mMobile_No1 , String mMobile_No2 , String mOccupation)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("Facility_Number" , mFacility_Number);
        contentValues.put("Relationship_Applicant" , mRelationship_Applicant);
        contentValues.put("Customer_Full_Name" , mCustomer_Full_Name);
        contentValues.put("C_Address_Line_1" , mC_Address_Line_1);
        contentValues.put("C_Address_Line_2" , mC_Address_Line_2);
        contentValues.put("C_Address_Line_3" , mC_Address_Line_2);
        contentValues.put("C_Address_Line_4" , mC_Address_Line_4);
        contentValues.put("NIC" , mNIC);
        contentValues.put("Client_Code" , mClient_Code);
        contentValues.put("Gender" , mGender);
        contentValues.put("Mobile_No1" , mMobile_No1);
        contentValues.put("Mobile_No2" , mMobile_No2);
        contentValues.put("Occupation" , mOccupation);
        sqLiteDatabase.insert("recovery_coapp_guarantor" , null , contentValues);
    }

    public void InsertnoteDetails(String mInpFacno , String mInpUserid , String mDate , String mNoteDes , String NoteSer )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("FACILITY_NO" , mInpFacno);
        contentValues.put("EXECUTIVE_ID" , mInpUserid);
        contentValues.put("NOTE_DATE" , mDate);
        contentValues.put("NOTE_DESC" , mNoteDes);
        contentValues.put("NOTE_SER" , NoteSer);
        sqLiteDatabase.insert("recovery_notepad_history" , null , contentValues);
    }


    public void InsertPaymentDetaiis (String mInpRcptno , String mInpRcptDate , String mInpRcptPaymode , String mInpRcptAmt , String mInpRcptChanal ,
                                      String mInpRcptRemarks , String mInpRcpRelized , String mInpRcpFacno)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("Receipt_Number" , mInpRcptno);
        contentValues.put("Payment_Date_Time" , mInpRcptDate);
        contentValues.put("Payment_Mode" , mInpRcptPaymode);
        contentValues.put("Amount" , mInpRcptAmt);
        contentValues.put("Payment_Channel" , mInpRcptAmt);
        contentValues.put("Remarks" , mInpRcptRemarks);
        contentValues.put("Realized" , mInpRcpRelized);
        contentValues.put("Facility_Number" , mInpRcpFacno);
        sqLiteDatabase.insert("recovery_paymentdetail" , null ,contentValues );
    }

    public void InsertVehicleCond(String InpConCode , String InpVehconDes)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("vehiclecon_code" , InpConCode);
        contentValues.put("vehiclecon_descr" , InpVehconDes);
        sqLiteDatabase.insert("masr_vehiclecondition" , null , contentValues);
        sqLiteDatabase.close();
    }

    public void InsertRelastionCode (String InpRelCode , String InpRelDes)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("relationship_code" , InpRelCode);
        contentValues.put("relationship_descr" , InpRelDes);
        sqLiteDatabase.insert("masr_relationship" , null , contentValues);
        sqLiteDatabase.close();
    }

    public void InsertPaymentMode(String InpPayMode , String PayDes)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("paymentmode_code" , InpPayMode);
        contentValues.put("paymentmode_descr" , PayDes);
        sqLiteDatabase.insert("masr_paymentmode" , null , contentValues);
        sqLiteDatabase.close();
    }

    public void InsertOwnship(String InpOwnCode , String OwnDes)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("ownership_code" , InpOwnCode);
        contentValues.put("ownership_descr" , OwnDes);
        sqLiteDatabase.insert("masr_ownership" , null , contentValues);
        sqLiteDatabase.close();

    }

    public void InsertDriveMode(String InpCde , String InpDriDes)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("drivenby_code" ,InpCde );
        contentValues.put("drivenby_descr" ,InpDriDes );
        sqLiteDatabase.insert("masr_drivenby" , null , contentValues);
        sqLiteDatabase.close();
    }

    public void InsertChanaaData(String ChanCode , String ChanaDes)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("channelmode_code" , ChanCode);
        contentValues.put("channelmode_descr" , ChanaDes);
        sqLiteDatabase.insert("masr_channelmode" , null ,contentValues );
        sqLiteDatabase.close();
    }


    public void InsertPTPCode(String InpPTPCode , String InpPtpdes)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("actionptp_code" , InpPTPCode);
        contentValues.put("actionptp_descr" , InpPtpdes);
        sqLiteDatabase.insert("masr_actioncodeptp" , null , contentValues);
        sqLiteDatabase.close();
    }


    public void InsertActionCode(String InpActionCode , String InpActuonDes)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put("action_code" , InpActionCode);
        contentValues.put("action_descr" , InpActuonDes);
        sqLiteDatabase.insert("masr_actioncode" , null ,contentValues);
        sqLiteDatabase.close();
    }

    //====================================

}
