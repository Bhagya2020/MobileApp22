package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.widget.ListView;


import androidx.appcompat.app.AlertDialog;

import com.android.volley.toolbox.StringRequest;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SqlliteCreateLeasing extends SQLiteOpenHelper {

    //============ CREATE DATABSE ==============================
    public static final String DATABASE_NAME = "AFSL_MOB_LEASING.db";


    // ========= To define Master Table ========================
    public static final String TABLE_NAME_MASTER        = "AP_MAST_MAKE";
    public static final String TABLE_NAME_MASTER1       = "AP_MAST_MODEL";
    public static final String TABLE_NAME_MASTER2       = "AP_MAST_OWNER";
    public static final String TABLE_NAME_MASTER3       = "AP_MAST_SUPPLIER";
    public static final String TABLE_NAME_MASTER4       = "AP_MAST_BRANCH";
    public static final String TABLE_NAME_MASTER5       = "AP_MAST_INSURANCECOMPANY";
    public static final String TABLE_NAME_MASTER6       = "MAST_INTDUSER";
    public static final String TABLE_NAME_MASTER7       = "AP_DOC_IMAGE";


    private String makeCode;
    private String makeDes , mPendingAppno , mPenclNic , mPenClname ;

    //===============================================================
    public SqlliteCreateLeasing(Context ctx) {
        super(ctx, DATABASE_NAME, null, 1);

       // SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //=========== CREATE MASTER TABLE ===================
        db.execSQL("create table " + TABLE_NAME_MASTER   + " (MAKE_CODE TEXT , MAKE_DESCR TEXT )");
        db.execSQL("create table " + TABLE_NAME_MASTER1  + " (MAKE_CODE TEXT , MODEL_CODE TEXT , MODEL_DESC TEXT , MODEL_STS TEXT)");
        db.execSQL("create table " + TABLE_NAME_MASTER2  + " (AREA_CODE TEXT , SHOWROOM_N TEXT , ADDRESS TEXT , OWNERS_NAM TEXT , MOBILE_NO TEXT , MAKE TEXT , EMAIL_ID TEXT  , ID TEXT, NICBR TEXT , DELEAR_STS TEXT)");
        db.execSQL("create table " + TABLE_NAME_MASTER3  + " (CORP_CODE TEXT , SUPPLIER_N TEXT , MAKEESSesc TEXT , ADD1RS_NAM TEXT , ADD2LE_NO TEXT , ADD3 TEXT , ADD4L_ID TEXT , CONTACT_NO TEXT , EMAIL TEXT)");
        db.execSQL("create table " + TABLE_NAME_MASTER4  + " (BRANCH_COD TEXT , BRANCH_NAM TEXT , BRANCH_EMAIL_1 TEXT , BR_MANAGER_CODE TEXT , BR_MANAGER_NANE TEXT , BR_CONTACTNO TEXT , MGR_PHONE TEXT )");
        db.execSQL("create table " + TABLE_NAME_MASTER5  + " (INS_CODE TEXT , INS_NAME TEXT )");
        db.execSQL("create table " + TABLE_NAME_MASTER6  + " (BRANCH TEXT , ID_NO TEXT  , NAME TEXT , AREA TEXT , CLIENT_COD TEXT , CC_CREATED TEXT )");
        db.execSQL("create table " + TABLE_NAME_MASTER7  + " (APP_REF_NO TEXT , DOC_TYPE TEXT , DOC_DATE TEXT , DOC_STS TEXT , UPLOD_USER TEXT , IMAGE BLOB , REMARKS TEXT , CL_NIC TEXT)");
        //===================================================

        //========== CREATE MAIN DATA TABLE =========================================
        //===========================================================================
            //=== CLIENT DATA -- 'LE_CLIENT_DATA'
        db.execSQL("create table LE_CLIENT_DATA"  + " (APPLICATION_REF_NO TEXT  , CL_NIC TEXT  , CL_TITLE TEXT  , CL_FULLY_NAME TEXT  , CL_ADDERS_1 TEXT  , CL_ADDERS_2 TEXT  , CL_ADDERS_3 TEXT  , CL_ADDERS_4 TEXT  ," +
                "CL_GENDER TEXT  , CL_MARITAL_STATUS TEXT  , CL_INITIALS TEXT  , CL_LASTNAME TEXT  , CL_DATE_OF_BIRTH TEXT  , CL_AGE TEXT  , CL_MOBILE_NO TEXT  , CL_LAND_NO TEXT  ," +
                "CL_SECTOR TEXT  , CL_OCCUPATION TEXT  , CL_INCOME TEXT  , CL_CREATE_DATE TEXT  , CL_CREATE_USER TEXT  , CL_BRANCH TEXT  , SECTOR TEXT  , SUB_SECTOR TEXT  , INCOME_SOURCE TEXT  , INCOME_AMT TEXT  ," +
                " CL_TAX TEXT  , CL_TAX_CODE TEXT  , CL_COUNTRY TEXT  , CL_PROVINCE TEXT  , CL_DISTRICT TEXT  , CL_AREA_CODE TEXT  , CL_EMAIL TEXT  ,CL_NATION TEXT  , CL_MAIL_ADD1 TEXT  , CL_MAIL_ADD2 TEXT  ," +
                "CL_MAIL_ADD3 TEXT  , CL_MAIL_ADD4 TEXT  , CL_EMARKS TEXT  )");

        //=== GURANTE & JOIN CLIENT DATA -- 'LE_CO_CL_DATA'
        db.execSQL("create table LE_CO_CL_DATA" + "(APPLICATION_REF_NO TEXT , CO_CLTYPE TEXT , CO_NIC TEXT , CO_TITLE TEXT , CO_FULLY_NAME TEXT , CO_ADDERS_1 TEXT , CO_ADDERS_2 TEXT , CO_ADDERS_3 TEXT , CO_ADDERS_4 TEXT ," +
                "CO_GENDER TEXT , CO_MARITAL_STATUS TEXT , CO_INITIALS TEXT , CO_LASTNAME TEXT , CO_DATE_OF_BIRTH TEXT , CO_AGE TEXT , CO_MOBILE_NO TEXT , CO_LAND_NO TEXT , CO_SECTOR TEXT , CO_SUBSECTOR TEXT , CO_OCCUPATION TEXT , " +
                "CO_INCOME TEXT , CO_SEC_VALUE TEXT , CO_CREATE_DATE TEXT , CO_CREATE_USER TEXT , CO_BRANCH TEXT , RELATIONS_TYPE TEXT , CO_COUNTRY TEXT  , CO_PROVINCE TEXT  , CO_DISTRICT TEXT  , CO_AREA_CODE TEXT  )");

        //=== APPLICATION DATA -- 'LE_APPLICATION'
        db.execSQL("create table LE_APPLICATION" + "(APPLICATION_REF_NO TEXT , AP_PRODUCT TEXT , AP_INVOICE_AMT TEXT , AP_DOWN_PAY TEXT , AP_FACILITY_AMT TEXT , AP_ETV TEXT , AP_RATE TEXT , AP_PERIOD TEXT , AP_RENTAL_AMT TEXT , AP_MK_OFFICER TEXT , AP_FOL_OFFICER TEXT , AP_REC_OFFICER TEXT , " +
                "AP_BRANCH TEXT , AP_PO_RQ_DATE TEXT , AP_PO_RQ_TIME TEXT , AP_PO_RQ_USER TEXT ,AP_PO_STS TEXT , AP_PO_SEND_DATE_TIME TEXT , AP_PO_SEND_DELEAR TEXT , AP_PO_DELEAR_EMAIL TEXT , AP_APPROVE_OFFICER TEXT ," +
                "AP_APPROVE_DATE TEXT ,AP_APPROVE_TIME TEXT , AP_APPROVE_STS TEXT , AP_ENTDATE TEXT , AP_LAST_MOD_DATE DATE , AP_STAGE TEXT , AP_MAIN_SUPPLIR TEXT , AP_MAIN_SUPPLIR_EMAIL TEXT , AP_MK_OFF_REMARKS )");

        //=== ASSETS DATA -- 'LE_ASSET_DETAILS'
        db.execSQL("create table LE_ASSET_DETAILS" + "(APPLICATION_REF_NO TEXT ,AS_EQ_TYPE TEXT , AS_EQ_CATAGE TEXT , AS_EQ_MAKE TEXT , AS_EQ_MODEL TEXT , AS_EQ_REGNO TEXT , AS_EQ_ENG_NO TEXT , AS_EQ_CHAS_NO TEXT , AS_EQ_YEAR TEXT , AS_INV_NO TEXT , " +
                " AS_INV_DELER TEXT , AS_INV_SUPPLIER TEXT , AP_INV_DATE TEXT , AP_INTDU TEXT , AP_INRDU_NAME , AP_INTDU_RATE TEXT , AP_INSURANCE_COMPANY TEXT , AP_INSURANCE_NO TEXT , AP_ENT_DATE TEXT , AP_MOD_DATE TEXT , AP_ENT_USER TEXT , AP_BRANCH TEXT , MK_VAL TEXT , M_IND_CODE TEXT , M_SUP_CODE TEXT , M_DELEAR_CODE TEXT )");

        //=== CHARGES DATA -- 'LE_CHARGS'
        db.execSQL("create table LE_CHARGS " + "(APPLICATION_REF_NO TEXT , OT_CHARGE_NAME TEXT , OT_CHARGE_AMT TEXT , OT_CHARGE_TYPE TEXT)");

        //=== READ DATA -- 'LE_INBAK_READ'
        db.execSQL("create table LE_INBAK_READ" + "(APPLICATION_REF_NO TEXT , IN_READ_DATE TEXT , IN_READ_TIME TEXT , IN_READ_STS TEXT , IN_FACILITY_NO TEXT)");

        //=== READ DATA -- 'LE-USERMANAGMENT'
        db.execSQL("create table USER_MANAGEMENT" + "(OFFIER_ID TEXT , PWD TEXT , OFFICER_NAME TEXT , DEPARTMENT TEXT , INACTIVE TEXT , OFFICER_EPF TEXT , OFFICER_EMAIL TEXT , OFFIER_TYPE TEXT , USER_ROLL TEXT ," +
                "BRANCH_CODE TEXT , PHONE_NO TEXT)");

        //=== MANAGER PENDING APPROVE -- ''
        db.execSQL("create table PO_PENDING" + "(APP_REF_NO TEXT ,CL_NIC TEXT , CLNAME TEXT , AMT TEXT , MKOFFICER TEXT , BRANCH_CODE TEXT , APP_STS TEXT)");

        //=== MANAGER PENDING APPROVE APPLICATION DETAILS -- ''
        db.execSQL("create table PO_PENDING_DETAILS" + "(APP_REF_NO TEXT , FACILITY_AMT TEXT , RENTAL_AMT TEXT  , RATE TEXT " +
                "  , PERIOD TEXT , DOWN_PAY TEXT , MK_OFFICER TEXT , FULLY_NAME TEXT , ADDERS_1 TEXT , ADDERS_2 TEXT , ADDERS_3 TEXT , ADDERS_4 TEXT " +
                ",MOBILE_NO TEXT , OCCUPATION TEXT , EQ_TYPE TEXT , EQ_MAKE TEXT , EQ_MODEL TEXT , EQ_CHAS_NO TEXT , EQ_ENG_NO TEXT , EQ_YEAR TEXT , MkVal TEXT , MGRAPPROVE TEXT , POSEND TEXT ," +
                "EXP_RATE TEXT , MPRODUCT TEXT ,  MINV_VAL TEXT  , EQ_CATGERY TEXT , SUP_NAME TEXT , DELERE TEXT , INTDUSER_NAME TEXT , INSURANCECOM TEXT , REGNO TEXT , CL_NIC TEXT )");


        //=== APPLICATION REMARKS
        db.execSQL("create table APP_REMARKS" + "(APPNO TEXT , OFFICER_ID TEXT , RE_DATE TEXT , AP_REMARKS TEXT )");

        //==== Application Config Table
        db.execSQL("create table APP_CONFIG" + "(CONFIG_TYPE TEXT , CONFIG_VAL TEXT , CONFIG_VESION TEXT , CONFIG_DATE TEXT)");

        //==== Pending Manager Approve And Complete Application
        db.execSQL("create table APP_PENDING_COMPLETE" + "(APP_REF_NO TEXT , AP_STAGE TEXT , FACILITY_AMT TEXT , CLNAME TEXT)");

        //==== Province Details =====
        db.execSQL("create table AP_MAST_PROVINCE" + "(PRV_CODE TEXT , PRV_NAME TEXT)");

        //==== District Details =====
        db.execSQL("create table AP_MAST_DISTRICT" + "(PRV_CODE TEXT , DIS_CODE TEXT , DIS_NAME TEXT)");

        //==== Area Details =====
        db.execSQL("create table AP_MAST_AREA" + "(PRV_CODE TEXT , DIS_CODE TEXT , AR_CODE TEXT , AR_DES TEXT)");

        //==== Occpation Details =====
        db.execSQL("create table AP_MAST_OCCUPATION" + "(OCCUP_CODE TEXT , OCCUP_DES TEXT , CREDIT_RATE TEXT)");

        //==== PrODUCT config Details Details =====
        db.execSQL("create table MAST_PRODUCT_CONFIG" + "(APP_PR_CODE TEXT , APP_EQ_TYPE TEXT , ETV_RATE TEXT , INT_RATE_MIN TEXT , INT_RATE_MAX TEXT , MIN_FAC_AMT TEXT , MAX_FAC_AMT TEXT )");

        //==== Product Paramater =====
        db.execSQL("create table MAST_PR_PARAMATER" + "(PR_CODE TEXT , EQ_TYPE TEXT , PRRA_CODE TEXT , PARA_DES TEXT ,PARA_VAL TEXT)");

        //==== Manager View Document Paramater =====
        db.execSQL("create table MANAGER_VIEW_IMAGE" + "(APP_REF_NO TEXT , DOC_REF TEXT , DOCTYPE TEXT , DOC_IMAGE TEXT )");

        //==== Upload Crib Reprts to Server =====
        db.execSQL("create table CRIB_DOC" + "(APP_REF_NO TEXT , DOC_REF_NIC TEXT , CRIB_DATE TEXT , CRIB_IMAGE BLOB )");

        //==== Upload Doc Type =====
        db.execSQL("create table AP_MAST_DOC_TYPE" + "( PRODUCT_CODE TEXT , EQ_TYPE TEXT , DOC_CODE TEXT , DOC_NAME TEXT , MAY_TYPE TEXT , MAN_STAGE TEXT , HOLDER_TYPE TEXT )");

        //==== Update GEO Tag Details ====
        db.execSQL("create table LE_GEO_TAG_UPDATE" + "(APP_REF_NO TEXT , CL_ADDDER TEXT , MK_CODE TEXT , GEO_LATITUDE TEXT , GEO_LONGITUDE TEXT , UDATE TEXT , UTIME TEXT , SERVER_SEND TEXT )");

        //==== PO Approvel Details Details ====
        db.execSQL("create table PO_APPROVAL_USER" + "(BBRANCH_CODE TEXT , OFFICER_ID TEXT , OFFICER_NAME TEXT , OFFICER_PHONE TEXT)");

        //=== User Profile Picture Uplaod
        db.execSQL("create table PROFILE_PICTURE" + "(USER_ID TEXT , USER_NAME TEXT , PROFILE_IAME TEXT)");

        //=== User Notification Save
        db.execSQL("create table MY_NOTIFICARTION" + "(USER_ID TEXT , NOT_REF_ID TEXT , NOT_DATE TEXT , NOT_TYPE TEXT , NOT_DESCRI TEXT , NOT_READ TEXT)");

        //=== Mastre Sector
        db.execSQL("create table AP_MAST_SECTOR" + "(SEC_CODE TEXT , SEC_DES TEXT)");

        //=== Mastre Sub Sector
        db.execSQL("create table AP_MAST_SUB_SECTOR" + "(SEC_CODE TEXT , SUB_SEC_CODE TEXT , SUB_DES TEXT)");

        //=== Create Image Upload Foldr Details
        db.execSQL("create table AP_IMAGE_FOLDER_DETAILS" + "(APP_REFNO TEXT , CL_NIC TEXT , USER_ID TEXT , UPLOAD_DATE TEXT , FOLDER_PATH TEXT , ZIP_FOLDER_PATH TEXT , UPLOAD_STS TEXT , LIVE_UPD_DATE TEXT)");

        //=== Master - Occupation Type
        db.execSQL("create table AP_MAST_OCCUPATION_TYPE" + "(occupationtype_code TEXT , occupationtype_descr TEXT)");

        //=== Marketing Officer target
        db.execSQL("create table ME_TARGET_DETAILS" + "(mkt_officer TEXT, year TEXT , month TEXT , category TEXT , make TEXT , eq_type TEXT , mkt_officer_branch TEXT , mkt_officer_name TEXT , target_value TEXT , target_number TEXT , act_value TEXT , act_number TEXT)");

        //=== Marketing Officer granting details
        db.execSQL("create table ME_GRANTING_DETAILS" + "(fac_no TEXT , vehicle_no TEXT , rental_amount TEXT , product TEXT , product_type TEXT , " +
                " customer_full_name TEXT , facility_amount TEXT , activation_date TEXT , total_facility_amount TEXT , make TEXT , model TEXT ,  eq_type TEXT ,  category TEXT , maketing_code TEXT)");


        //=== Marketing Officer target
        db.execSQL("create table AP_MODEL_LTV" + "( MAKE_CODE TEXT , MODEL_CODE TEXT , EQ_TYPE TEXT , YEAR_AGE TEXT , LTV_RATE TEXT)");

        //=== Marketing Application Draft Save
        db.execSQL("create table DRAFT_APPLICATION" + "(NIC TEXT , CL_NAME TEXT , ADDERS_1 TEXT , ADDERS_2 TEXT , ADDERS_3 TEXT , ADDERS_4 TEXT , CURENT_LATTITUDE TEXT , CURRENT_LONGGITUDE TEXT , ENT_DATE TEXT , ENT_USER TEXT , APP_REF_NO TEXT , UPD_FLG TEXT , GEO_FLG_UPDATE TEXT)");

        //=== RMV data uplaod
        db.execSQL("create table RMV_CONFORM_DATA" + "( BRANCH_CODE TEXT , MK_CODE TEXT , MK_NAME TEXT , CHASSI_NO TEXT , ENGINE_NO TEXT , REG_NO TEXT , PO_DATE TEXT , PO_TIME TEXT , REQ_DATE TEXT , RMV_CONFORM_STS TEXT , RMV_CONFORM_DATE TEXT , RMV_CONFORM_USER TEXT , RMV_REMARKS TEXT , CO_SYS_READ TEXT)");

        //=== RMV conform update
        db.execSQL("create table RMV_DATA" + "( BRANCH_CODE TEXT , CHASSI_NO TEXT , ENGINE_NO TEXT , REG_NO TEXT , PIDNO TEXT , VARIFI_STS TEXT , REMARKS TEXT , VERIF_DATE TEXT , VERIFY_TIME TEXT , VERIFY_USER TEXT , PO_DATE TEXT , ME_CODE TEXT , SERVER_UPDTE TEXT )");


        //=== Data synv process sts
        db.execSQL("create table HOME_DATA_SYNC" + "(SYNC_DATE TEXT , SYNC_STS TEXT , SYNC_USER TEXT)");

        //=== Refinance Serach data save
        db.execSQL("create table REFINNCE_SEARC_DATA" + "(mCLIENT_NAME TEXT , mPRODUCT TEXT , mFACILITY_NO TEXT , mVEHICLE_NO TEXT , mFACILITY_AMT TEXT , mSETTLEMENT_AMT TEXT)");

        //=============================================================================
        //=============================================================================

        //==== Create Index table
        db.execSQL("CREATE UNIQUE INDEX idx_leapplication_appno ON LE_CLIENT_DATA (APPLICATION_REF_NO);");
        db.execSQL("CREATE UNIQUE INDEX idx_leasset_appno ON LE_ASSET_DETAILS (APPLICATION_REF_NO);");
        db.execSQL("CREATE UNIQUE INDEX idx_usermangment_appno ON USER_MANAGEMENT (OFFIER_ID);");
    }

    /*
    @Override
    public synchronized void close () {
        if (db != null) {
            db.close();
            super.close();
        }
    }
    */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MASTER);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MASTER1);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MASTER2);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MASTER3);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MASTER4);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MASTER5);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MASTER6);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MASTER7);
        onCreate(db);

        //=========== DROP MAIN DATA TABLE =====================
        db.execSQL("DROP TABLE IF EXISTS LE_CLIENT_DATA");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS LE_CO_CL_DATA");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS LE_APPLICATION");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS LE_ASSET_DETAILS");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS LE_CHARGS");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS LE_INBAK_READ");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS USER_MANAGEMENT");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS PO_PENDING");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS PO_PENDING_DETAILS");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS APP_REMARKS");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS APP_CONFIG");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS APP_PENDING_COMPLETE");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS AP_MAST_PROVINCE");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS AP_MAST_DISTRICT");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS AP_MAST_AREA");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS AP_MAST_OCCUPATION");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS MAST_PRODUCT_CONFIG");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS MAST_PR_PARAMATER");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS MANAGER_VIEW_IMAGE");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS CRIB_DOC");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS AP_MAST_DOC_TYPE");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS LE_GEO_TAG_UPDATE");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS PO_APPROVAL_USER");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS PROFILE_PICTURE");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS MY_NOTIFICARTION");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS AP_MAST_SECTOR");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS AP_MAST_SUB_SECTOR");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS AP_IMAGE_FOLDER_DETAILS    ");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS AP_MAST_OCCUPATION_TYPE");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS ME_TARGET_DETAILS");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS ME_GRANTING_DETAILS");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS AP_MODEL_LTV");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS DRAFT_APPLICATION");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS RMV_CONFORM_DATA");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS RMV_DATA");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS HOME_DATA_SYNC");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS HOME_DATA_SYNC");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS REFINNCE_SEARC_DATA");
        onCreate(db);
    }

    public List<String> getAllSubSector(String SecCode)
    {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursorGetSecCode = sqLiteDatabase.rawQuery("SELECT SEC_CODE FROM AP_MAST_SECTOR WHERE SEC_DES = '" + SecCode + "'" ,null);
        if (cursorGetSecCode.getCount() != 0)
        {
            cursorGetSecCode.moveToFirst();
            Cursor cursorGetSubCode = sqLiteDatabase.rawQuery("SELECT * FROM AP_MAST_SUB_SECTOR WHERE SEC_CODE = '" + cursorGetSecCode.getString(0) + "'" , null );
            if (cursorGetSubCode.getCount() !=0)
            {
                cursorGetSubCode.moveToFirst();
                do
                {
                    list.add(cursorGetSubCode.getString(2));//adding 2nd column data
                } while (cursorGetSubCode.moveToNext());
            }
            cursorGetSubCode.close();
        }
        cursorGetSecCode.close();
        sqLiteDatabase.close();
        return list;
    }


    public List<String> getAllSector()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_SECTOR ORDER BY SEC_DES asc";
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

    public void InsertSubSector(String mSec_code , String msubSecCode , String mSubDes)
    {
        SQLiteDatabase sqLiteDatabase_SubSector = this.getWritableDatabase();
        ContentValues contentValues_subSector = new ContentValues();
        contentValues_subSector.put("SEC_CODE" , mSec_code);
        contentValues_subSector.put("SUB_SEC_CODE" , msubSecCode);
        contentValues_subSector.put("SUB_DES" , mSubDes);
        sqLiteDatabase_SubSector.insert("AP_MAST_SUB_SECTOR" , null , contentValues_subSector);
    }


    public void InsertSector(String mSec_code , String mSec_des)
    {
        SQLiteDatabase sqLiteDatabase_sector = this.getWritableDatabase();
        ContentValues contentValues_Sector = new ContentValues();
        contentValues_Sector.put("SEC_CODE" , mSec_code);
        contentValues_Sector.put("SEC_DES" , mSec_des);
        sqLiteDatabase_sector.insert("AP_MAST_SECTOR" , null , contentValues_Sector);
    }

    public void InsetProfilePic(String UserId , String UserName , String Image)
    {

        SQLiteDatabase sqLiteDatabase_DDELETE = this.getWritableDatabase() ;
        sqLiteDatabase_DDELETE.execSQL("DELETE FROM PROFILE_PICTURE WHERE USER_ID = '" + UserId + "'" );

        SQLiteDatabase sqLiteDatabase_profile = this.getWritableDatabase();
        ContentValues contentValues_profile = new ContentValues();
        contentValues_profile.put("USER_ID" , UserId);
        contentValues_profile.put("USER_NAME" , UserName);
        contentValues_profile.put("PROFILE_IAME" , Image);
        sqLiteDatabase_profile.insert("PROFILE_PICTURE" , null , contentValues_profile);

    }

    public void GetAppOfficer(String BrCode , String mOffid , String mOffName , String mOffPhoneno)
    {
        SQLiteDatabase sqLiteDatabase_ap_off = this.getWritableDatabase();
        ContentValues contentValues_appoff = new ContentValues();
        contentValues_appoff.put("BBRANCH_CODE" , BrCode);
        contentValues_appoff.put("OFFICER_ID" , mOffid);
        contentValues_appoff.put("OFFICER_NAME" , mOffName);
        contentValues_appoff.put("OFFICER_PHONE" , mOffPhoneno);
        sqLiteDatabase_ap_off.insert("PO_APPROVAL_USER" , null , contentValues_appoff);
    }



    public void GeoTagSave(String AppNo , String ClAdders , String Mk_code , String mLatCode , String mLongCode , String Update_Date , String Spdate_Time)
    {
        SQLiteDatabase sqLiteDatabase_geo_tag = this.getWritableDatabase();
        sqLiteDatabase_geo_tag.execSQL("DELETE FROM LE_GEO_TAG_UPDATE WHERE APP_REF_NO = '" + AppNo + "'");

        ContentValues contentValues_geo = new ContentValues();
        contentValues_geo.put("APP_REF_NO" , AppNo);
        contentValues_geo.put("CL_ADDDER" , ClAdders);
        contentValues_geo.put("MK_CODE" , Mk_code);
        contentValues_geo.put("GEO_LATITUDE" , mLatCode);
        contentValues_geo.put("GEO_LONGITUDE" , mLongCode);
        contentValues_geo.put("UDATE" , Update_Date);
        contentValues_geo.put("UTIME" , Spdate_Time);
        contentValues_geo.put("SERVER_SEND" , "");
        sqLiteDatabase_geo_tag.insert("LE_GEO_TAG_UPDATE" , null , contentValues_geo);
    }



    public void AppDataSync(String mGetAppno ,  String mGetPOSts , String mGetPODateTime , String mGetMgrAppCode , String mGetAppDate , String mGetAppStage ,String UserID)
    {
        SQLiteDatabase sqLiteDatabase_data = this.getWritableDatabase();

        SimpleDateFormat curdate = new SimpleDateFormat("yyyy-mm-dd");
        String NewDate  = curdate.format(new Date());
        Calendar now = Calendar.getInstance();


        //=== Create Notification - MY_NOTIFICARTION
        String sysncDec="";
        if (mGetAppStage.equals("002"))
            sysncDec = "Application Approved - " + mGetAppno;

        if (mGetAppStage.equals("100"))
            sysncDec = "Application Reject - " + mGetAppno;

        ContentValues contentValues_sync = new ContentValues();
        contentValues_sync.put("USER_ID" , UserID);
        contentValues_sync.put("NOT_REF_ID" , mGetAppno);
        contentValues_sync.put("NOT_DATE" , NewDate);
        contentValues_sync.put("NOT_TYPE" , "AppDataSync");
        contentValues_sync.put("NOT_DESCRI" , sysncDec);
        contentValues_sync.put("NOT_READ" , "");
        sqLiteDatabase_data.insert("MY_NOTIFICARTION" , null , contentValues_sync);

        ContentValues contentValues_data = new ContentValues();
        contentValues_data.put("AP_PO_STS" , mGetPOSts);
        contentValues_data.put("AP_PO_SEND_DATE_TIME" , mGetPODateTime);
        contentValues_data.put("AP_APPROVE_OFFICER" , mGetMgrAppCode);
        contentValues_data.put("AP_APPROVE_DATE" , mGetAppDate);
        contentValues_data.put("AP_STAGE" , mGetAppStage);
        sqLiteDatabase_data.update("LE_APPLICATION" ,contentValues_data ,"APPLICATION_REF_NO = ?", new String[]{String.valueOf(mGetAppno)});
    }

    public void CreateMasteDocType (String mPrcode , String mEqType , String mDoctype , String mDocname , String mMan_type , String mManstage , String Inp_Hold_type)
    {
        SQLiteDatabase sqLiteDatabase_doc_type = this.getWritableDatabase();
        ContentValues contentValues_doc_type = new ContentValues();
        contentValues_doc_type.put("PRODUCT_CODE" , mPrcode);
        contentValues_doc_type.put("EQ_TYPE" , mEqType);
        contentValues_doc_type.put("DOC_CODE" , mDoctype);
        contentValues_doc_type.put("DOC_NAME" , mDocname);
        contentValues_doc_type.put("MAY_TYPE" , mMan_type);
        contentValues_doc_type.put("MAN_STAGE" , mManstage);
        contentValues_doc_type.put("HOLDER_TYPE" , Inp_Hold_type);

        sqLiteDatabase_doc_type.insert("AP_MAST_DOC_TYPE" , null , contentValues_doc_type);
    }


    public void CribSave (String mAppno , String mNic , String mDate , byte [] Doc )
    {
        SQLiteDatabase sqLiteDatabase_doc   = this.getWritableDatabase();
        ContentValues contentValues_doc_crib = new ContentValues();
        contentValues_doc_crib.put("APP_REF_NO" , mAppno);
        contentValues_doc_crib.put("DOC_REF_NIC" , mNic);
        contentValues_doc_crib.put("CRIB_DATE" , mDate);
        //contentValues_doc_crib.put("CRIB_IMAGE" , DOC);
        sqLiteDatabase_doc.insert("CRIB_DOC" , null , contentValues_doc_crib);
    }

    public void CreateManagerImage(String mappno , String mDocref , String mDocType , String DcoImage)
    {

        SQLiteDatabase sqLiteDatabase_DDELETE = this.getWritableDatabase() ;
        sqLiteDatabase_DDELETE.execSQL("DELETE FROM MANAGER_VIEW_IMAGE WHERE APP_REF_NO = '" + mappno + "' AND DOC_REF = '" + mDocref + "' AND DOCTYPE = '" +  mDocType + "'" );

        SQLiteDatabase sqLiteDatabase_image_view = this.getWritableDatabase();
        ContentValues contentValues_image = new ContentValues();
        contentValues_image.put("APP_REF_NO" , mappno);
        contentValues_image.put("DOC_REF" , mDocref);
        contentValues_image.put("DOCTYPE" , mDocType);
        contentValues_image.put("DOC_IMAGE" , DcoImage);
        sqLiteDatabase_image_view.insert("MANAGER_VIEW_IMAGE" , null , contentValues_image);

    }

    public void CreateParamater( String mPrcode , String mEqType , String mParacode , String mParaDes , String mParaVal )
    {
        SQLiteDatabase sqLiteDatabase_PARAMATER = this.getWritableDatabase();
        ContentValues contentValues_Para = new ContentValues();
        contentValues_Para.put("PR_CODE" , mPrcode);
        contentValues_Para.put("EQ_TYPE" , mEqType);
        contentValues_Para.put("PRRA_CODE" , mParacode);
        contentValues_Para.put("PARA_DES" , mParaDes);
        contentValues_Para.put("PARA_VAL" , mParaVal);
        sqLiteDatabase_PARAMATER.insert("MAST_PR_PARAMATER" , null , contentValues_Para);

    }


    public void CreateProuuct(String mPrCode , String mEqType , String mEtv , String  mIntRate_Min , String mIntRate_Max , String MinFac , String MaxFacAmt)
    {
        SQLiteDatabase sqLiteDatabase_PROD_CON = this.getWritableDatabase();
        ContentValues contentValues_PRODUCT = new ContentValues();
        contentValues_PRODUCT.put("APP_PR_CODE" , mPrCode);
        contentValues_PRODUCT.put("APP_EQ_TYPE" , mEqType);
        contentValues_PRODUCT.put("ETV_RATE" , mEtv);
        contentValues_PRODUCT.put("INT_RATE_MIN" , mIntRate_Min);
        contentValues_PRODUCT.put("INT_RATE_MAX" , mIntRate_Max);
        contentValues_PRODUCT.put("MIN_FAC_AMT" , MinFac);
        contentValues_PRODUCT.put("MAX_FAC_AMT" , MaxFacAmt);
        sqLiteDatabase_PROD_CON.insert("MAST_PRODUCT_CONFIG" , null , contentValues_PRODUCT);
    }

    public void CreateOccupa(String Occ_code , String Occ_Des , String Credit_rate)
    {
        SQLiteDatabase sqLiteDatabaseOCCPATION = this.getWritableDatabase();
        ContentValues contentValues_OCCPA = new ContentValues();
        contentValues_OCCPA.put("OCCUP_CODE" , Occ_code);
        contentValues_OCCPA.put("OCCUP_DES" , Occ_Des);
        contentValues_OCCPA.put("CREDIT_RATE" , Credit_rate);
        sqLiteDatabaseOCCPATION.insert("AP_MAST_OCCUPATION" , null , contentValues_OCCPA);
    }

    //=== Create Area Code Detals ===
    public void CreateAreaCode(String Prv_code , String Dis_code , String Code , String Des)
    {
        SQLiteDatabase sqLiteDatabaseAREA   =   this.getWritableDatabase();
        ContentValues contentValuesAREA  = new ContentValues();
        contentValuesAREA.put("PRV_CODE" , Prv_code);
        contentValuesAREA.put("DIS_CODE" , Dis_code);
        contentValuesAREA.put("AR_CODE" , Code);
        contentValuesAREA.put("AR_DES" , Des);
        sqLiteDatabaseAREA.insert("AP_MAST_AREA" , null , contentValuesAREA);
    }


    //=== Create District Detals ===
    public void CreateDistrict(String Prv_code , String code , String des)
    {
        SQLiteDatabase sqLiteDatabaseDISTRICT = this.getWritableDatabase();
        ContentValues contentValuesDISTRICT = new ContentValues();
        contentValuesDISTRICT.put("PRV_CODE" , Prv_code);
        contentValuesDISTRICT.put("DIS_CODE" , code);
        contentValuesDISTRICT.put("DIS_NAME" , des);
        sqLiteDatabaseDISTRICT.insert("AP_MAST_DISTRICT" , null , contentValuesDISTRICT);
    }


    //=== Create Province Detals ===
    public void CreateProvince(String Code , String Des)
    {
        SQLiteDatabase sqLiteDatabasePROVINCE = this.getWritableDatabase();
        ContentValues contentValuesPROVINCE = new ContentValues();
        contentValuesPROVINCE.put("PRV_CODE" , Code);
        contentValuesPROVINCE.put("PRV_NAME" , Des);
        sqLiteDatabasePROVINCE.insert("AP_MAST_PROVINCE" , null , contentValuesPROVINCE);
    }

    // === Create Pending Submit Applicatin
    public void CreateSubmitApp(String Appno , String ApStage , String mFacamt , String mClanme)
    {
        SQLiteDatabase sqLiteDatabase_serch_app = this.getReadableDatabase();
        Cursor cursorserch_app = sqLiteDatabase_serch_app.rawQuery("SELECT * FROM APP_PENDING_COMPLETE WHERE APP_REF_NO = '" + Appno + "'" , null);
        if (cursorserch_app.getCount() != 0)
        {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValuessubmit   = new ContentValues();
            contentValuessubmit.put("APP_REF_NO" , Appno);
            contentValuessubmit.put("AP_STAGE" , ApStage);
            contentValuessubmit.put("FACILITY_AMT" , mFacamt);
            contentValuessubmit.put("CLNAME" , mClanme);
            sqLiteDatabase.insert("APP_PENDING_COMPLETE" , null , contentValuessubmit);
        }
    }

    public void UpdateOccType(String mInputOccCode , String mInputOccDes)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValuesoccype = new ContentValues();
        contentValuesoccype.put("occupationtype_code" , mInputOccCode);
        contentValuesoccype.put("occupationtype_descr" , mInputOccDes);
        sqLiteDatabase.insert("AP_MAST_OCCUPATION_TYPE" , null , contentValuesoccype);
    }

    //=== Create App Config Table
    public void UpdateConfig(String ConType , String ConfigVal , String ConfigVer , String ConfigDate)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("APP_CONFIG","CONFIG_TYPE =?",new String[] {ConType});

        ContentValues contentValuesConfig = new ContentValues();
        contentValuesConfig.put("CONFIG_TYPE" , ConType);
        contentValuesConfig.put("CONFIG_VAL" , ConfigVal);
        contentValuesConfig.put("CONFIG_VESION" , ConfigVer);
        contentValuesConfig.put("CONFIG_DATE" , ConfigDate);
        sqLiteDatabase.insert("APP_CONFIG" , null , contentValuesConfig);
    }

    //====== To Create Image to the table ========================
    public void InsertDoc (String app_refno , String doc_type , String docsts , String NewDate , String uploaduser  , String imageViewByte , String mRem , String mCl_Nic)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues InputImage = new ContentValues();
        InputImage.put("APP_REF_NO" , app_refno);
        InputImage.put("DOC_TYPE" , doc_type);
        InputImage.put("DOC_STS" , docsts);
        InputImage.put("DOC_DATE",NewDate);
        InputImage.put("UPLOD_USER" , uploaduser);
        InputImage.put("IMAGE" , imageViewByte);
        InputImage.put("REMARKS" , mRem);
        InputImage.put("CL_NIC" , mCl_Nic);
        sqLiteDatabase.insert("AP_DOC_IMAGE" , null , InputImage);


        Cursor cursor_getdata = sqLiteDatabase.rawQuery("SELECT * FROM AP_IMAGE_FOLDER_DETAILS WHERE APP_REFNO = '"  + app_refno + "'" , null);
        if (cursor_getdata.getCount() == 0)
        {
            ContentValues contentValues_folder = new ContentValues();
            contentValues_folder.put("APP_REFNO" , app_refno);
            contentValues_folder.put("CL_NIC" , mCl_Nic);
            contentValues_folder.put("USER_ID" , uploaduser);
            contentValues_folder.put("UPLOAD_DATE" , NewDate);
            contentValues_folder.put("FOLDER_PATH" , mRem);
            contentValues_folder.put("ZIP_FOLDER_PATH" , "");
            contentValues_folder.put("UPLOAD_STS" , "");
            contentValues_folder.put("LIVE_UPD_DATE" , "");
            sqLiteDatabase.insert("AP_IMAGE_FOLDER_DETAILS" , null , contentValues_folder);
        }

        cursor_getdata.close();
        sqLiteDatabase.close();
    }


    //==== Update The Po - Sts 000 - Save Data / 001 - PO REQUEST / 002 - MANAGER APPROVE / 004 - HOUSE VISITE
    //                     005 - APPLICATION FINEASH / 100 - MANAGER REJECT

    public void UpdateAppSts(String Appno , String AppSts , String POsts)
    {
        String InpAppno     =   Appno;
        String InpAppSts    =   AppSts;
        String InpPOSts     =   POsts;

        String currentTime = Calendar.getInstance().getTime().toString();
        String NewDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("AP-STAGE" , InpAppSts);
        SQLiteDatabase sqLiteDatabaseupdate = this.getWritableDatabase();
        if (AppSts.equals("001"))
        {
            Log.d("AP-STAGE-1" , InpAppSts);
            ContentValues contentValues = new ContentValues();
            contentValues.put("AP_PO_STS" , InpPOSts);
            contentValues.put("AP_STAGE" , InpAppSts);
            contentValues.put("AP_PO_RQ_DATE" , NewDate);
            contentValues.put("AP_PO_RQ_TIME" , currentTime);
            sqLiteDatabaseupdate.update("LE_APPLICATION" ,contentValues ,"APPLICATION_REF_NO = ?", new String[]{String.valueOf(InpAppno)});
        }
        else
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("AP_PO_STS" , InpPOSts);
            contentValues.put("AP_STAGE" , InpAppSts);
            sqLiteDatabaseupdate.update("LE_APPLICATION" ,contentValues ,"APPLICATION_REF_NO = ?", new String[]{String.valueOf(InpAppno)});
        }
    }


    //=====To Delete Image
    public void DeleteImage(String mAppno , String mDocRef , String mDoc_type)
    {
        SQLiteDatabase sqLiteDatabase_DDELETE = this.getWritableDatabase() ;
        sqLiteDatabase_DDELETE.execSQL("DELETE FROM AP_DOC_IMAGE WHERE APP_REF_NO = '" + mAppno + "' AND DOC_TYPE = '" + mDocRef + "' AND DOC_STS = '" +  mDoc_type + "'" );
    }

    //=== Save Manager Pending Approve Application Details ====
    public void PoPendingApplicationDetails(String IN_APP_REF_NO  , String IN_FACILITY_AMT, String IN_RENTAL_AMT, String IN_PERIOD  , String IN_RATE
             , String IN_DOWN_PAY  , String IN_MK_OFFICER  , String IN_FULLY_NAME  , String IN_ADDERS_1  , String IN_ADDERS_2  , String IN_ADDERS_3  , String IN_ADDERS_4
            , String IN_MOBILE_NO  , String IN_OCCUPATION  , String IN_EQ_TYPE  , String IN_EQ_MAKE  , String IN_EQ_MODEL  , String IN_EQ_CHAS_NO  , String IN_EQ_ENG_NO  , String IN_EQ_YEAR  , String IN_MkVal  , String ExpRate , String Temp_product , String Inv_amt , String Eq_categery ,
              String mSuppler , String mDelear_name , String Intduse_name , String Ins_companey , String ClNic , String mRegno ,  String IN_MGRAPPROVE  , String IN_POSEND )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("PO_PENDING_DETAILS","APP_REF_NO =?",new String[] {IN_APP_REF_NO});


        ContentValues contentValuesPendingdetaisl = new ContentValues();
        contentValuesPendingdetaisl.put("APP_REF_NO" , IN_APP_REF_NO);
        contentValuesPendingdetaisl.put("FACILITY_AMT" , IN_FACILITY_AMT);
        contentValuesPendingdetaisl.put("RENTAL_AMT" , IN_RENTAL_AMT);
        contentValuesPendingdetaisl.put("PERIOD" , IN_PERIOD);
        contentValuesPendingdetaisl.put("RATE" , IN_RATE);
        contentValuesPendingdetaisl.put("DOWN_PAY" , IN_DOWN_PAY);
        contentValuesPendingdetaisl.put("MK_OFFICER" , IN_MK_OFFICER);
        contentValuesPendingdetaisl.put("FULLY_NAME" , IN_FULLY_NAME);
        contentValuesPendingdetaisl.put("ADDERS_1" , IN_ADDERS_1);
        contentValuesPendingdetaisl.put("ADDERS_2" , IN_ADDERS_2);
        contentValuesPendingdetaisl.put("ADDERS_3" , IN_ADDERS_3);
        contentValuesPendingdetaisl.put("ADDERS_4" , IN_ADDERS_4);
        contentValuesPendingdetaisl.put("MOBILE_NO" , IN_MOBILE_NO);
        contentValuesPendingdetaisl.put("OCCUPATION" , IN_OCCUPATION);
        contentValuesPendingdetaisl.put("EQ_TYPE" , IN_EQ_TYPE);
        contentValuesPendingdetaisl.put("EQ_MAKE" , IN_EQ_MAKE);
        contentValuesPendingdetaisl.put("EQ_MODEL" , IN_EQ_MODEL);
        contentValuesPendingdetaisl.put("EQ_CHAS_NO" , IN_EQ_CHAS_NO);
        contentValuesPendingdetaisl.put("EQ_ENG_NO" , IN_EQ_ENG_NO);
        contentValuesPendingdetaisl.put("EQ_YEAR" , IN_EQ_YEAR);
        contentValuesPendingdetaisl.put("MkVal" , IN_MkVal);

        contentValuesPendingdetaisl.put("EXP_RATE" , ExpRate);
        contentValuesPendingdetaisl.put("MPRODUCT" ,Temp_product);
        contentValuesPendingdetaisl.put("MINV_VAL" , Inv_amt);
        contentValuesPendingdetaisl.put("EQ_CATGERY" , Eq_categery);

        contentValuesPendingdetaisl.put("SUP_NAME" , mSuppler);
        contentValuesPendingdetaisl.put("DELERE" , mDelear_name);
        contentValuesPendingdetaisl.put("INTDUSER_NAME" , Intduse_name);
        contentValuesPendingdetaisl.put("INSURANCECOM" , Ins_companey);

        contentValuesPendingdetaisl.put("CL_NIC" , ClNic);
        contentValuesPendingdetaisl.put("REGNO" , mRegno);

        contentValuesPendingdetaisl.put("MGRAPPROVE" , IN_MGRAPPROVE);
        contentValuesPendingdetaisl.put("POSEND" , IN_POSEND);
        sqLiteDatabase.insert("PO_PENDING_DETAILS" , null , contentValuesPendingdetaisl);
    }


    //=== Get Pending Approve - PP
    public void CreatePendingPO (String mInpAppno , String mInpclNic , String mInpClanme , String mInpAmt ,
                                 String mInpMkOfficer , String mInpBrcode , String mInpPOSts , String UserID)
    {

        SimpleDateFormat curdate = new SimpleDateFormat("yyyy-mm-dd");
        String NewDate  = curdate.format(new Date());
        Calendar now = Calendar.getInstance();

        //==== Check Data Available ======
        SQLiteDatabase sqLiteDatabase_check = this.getReadableDatabase();
        Cursor cursor_chk_notify = sqLiteDatabase_check.rawQuery("SELECT NOT_REF_ID FROM MY_NOTIFICARTION WHERE NOT_REF_ID = '" + mInpAppno + "'" +
                "AND NOT_TYPE = 'MgrDataSync' AND NOT_READ = ''" , null);
        Cursor cursor_chk_application = sqLiteDatabase_check.rawQuery("SELECT * FROM PO_PENDING WHERE APP_REF_NO = '" + mInpAppno + "'" , null) ;

        //================================

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //=== Create Notification - MY_NOTIFICARTION
        if (cursor_chk_notify.getCount() ==0)
        {
            String sysncDec = "Pending Approval - " + mInpAppno;
            ContentValues contentValues_sync = new ContentValues();
            contentValues_sync.put("USER_ID" , UserID);
            contentValues_sync.put("NOT_REF_ID" , mInpAppno);
            contentValues_sync.put("NOT_DATE" , NewDate);
            contentValues_sync.put("NOT_TYPE" , "MgrDataSync");
            contentValues_sync.put("NOT_DESCRI" , sysncDec);
            contentValues_sync.put("NOT_READ" , "");
            sqLiteDatabase.insert("MY_NOTIFICARTION" , null , contentValues_sync);
            //Log.d("MY_NOTIFICARTION","DONE");
        }

        if (cursor_chk_application.getCount() == 0)
        {
            ContentValues contentValuesPendongpo = new ContentValues();
            contentValuesPendongpo.put("APP_REF_NO" , mInpAppno);
            contentValuesPendongpo.put("CL_NIC" , mInpclNic);
            contentValuesPendongpo.put("CLNAME" , mInpClanme);
            contentValuesPendongpo.put("AMT" , mInpAmt);
            contentValuesPendongpo.put("MKOFFICER" , mInpMkOfficer);
            contentValuesPendongpo.put("BRANCH_CODE" , mInpBrcode);
            contentValuesPendongpo.put("APP_STS" , mInpPOSts);
            sqLiteDatabase.insert("PO_PENDING" , null , contentValuesPendongpo);
            Log.d("PO_PENDING","DONE");
        }
    }

    //=== Create New User Details
    public void CreateNewUser (String mOFFIER_ID , String mPWD  , String mOFFICER_NAME , String mDEPARTMENT , String mINACTIVE , String mOFFICER_EPF , String mOFFICER_EMAIL ,
                               String mOFFTYPE , String mUROLL , String mBRCODE , String PHONO )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues InputVal = new ContentValues();
        InputVal.put("OFFIER_ID" , mOFFIER_ID );
        InputVal.put("PWD" , mPWD );
        InputVal.put("OFFICER_NAME" , mOFFICER_NAME);
        InputVal.put("DEPARTMENT" , mDEPARTMENT);
        InputVal.put("INACTIVE" , mINACTIVE);
        InputVal.put("OFFICER_EPF" , mOFFICER_EPF);
        InputVal.put("OFFICER_EMAIL" ,mOFFICER_EMAIL );
        InputVal.put("OFFIER_TYPE" ,mOFFTYPE );
        InputVal.put("USER_ROLL" ,mUROLL );
        InputVal.put("BRANCH_CODE" ,mBRCODE );
        InputVal.put("PHONE_NO" ,PHONO );

        sqLiteDatabase.insert("USER_MANAGEMENT" , null , InputVal);
        sqLiteDatabase.close();
    }

    //====== To Create Master Details Code Table Data =============
    public void InsertOwner (String Area_code , String  ShowRoomName , String Adders , String OwnerName ,
                             String MobileNo , String Maketype , String Emailid , String oid , String nic , String Sts)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues InputOwner   = new ContentValues();
        InputOwner.put("AREA_CODE" , Area_code);
        InputOwner.put("SHOWROOM_N" , ShowRoomName);
        InputOwner.put("ADDRESS" , Adders);
        InputOwner.put("OWNERS_NAM" , OwnerName);
        InputOwner.put("MOBILE_NO" , MobileNo);
        InputOwner.put("MAKE" , Maketype);
        InputOwner.put("EMAIL_ID" , Emailid);
        InputOwner.put("ID", oid);
        InputOwner.put("NICBR" , nic);
        InputOwner.put("DELEAR_STS" , Sts);
        sqLiteDatabase.insert("AP_MAST_OWNER" , null , InputOwner);
        sqLiteDatabase.close();

    }
    //===========================================================

    //====== To Create Master Make Code Table Data =============
    public void InsertInsurance(String Ins_code , String Ins_name)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues InputIns = new ContentValues();
        InputIns.put("INS_CODE" , Ins_code);
        InputIns.put("INS_NAME" , Ins_name);
        sqLiteDatabase.insert("AP_MAST_INSURANCECOMPANY" , null , InputIns);
        sqLiteDatabase.close();
    }
    //===========================================================

    //====== To Create Intduser Details Code Table Data =============
    public void InsertIntduser (String BRANCH , String ID_NO , String NAME , String AREA  , String CLIENT_COD , String CC_CREATED )
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues InputIntduser = new ContentValues();
        InputIntduser.put("BRANCH" , BRANCH);
        InputIntduser.put("ID_NO" , ID_NO);
        InputIntduser.put("NAME" , NAME);
        InputIntduser.put("AREA" , AREA);
        InputIntduser.put("CLIENT_COD" , CLIENT_COD);
        InputIntduser.put("CC_CREATED" , CC_CREATED);
        sqLiteDatabase.insert("MAST_INTDUSER" , null , InputIntduser);
        sqLiteDatabase.close();
    }
    //===========================================================

    //====== To Create Master Make Code Table Data =============
    public boolean InsertMakeData (String MakeCode , String MakeDes)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues InputMake = new ContentValues();
        InputMake.put("MAKE_CODE" ,MakeCode );
        InputMake.put("MAKE_DESCR" , MakeDes);

        long Result = sqLiteDatabase.insert("AP_MAST_MAKE" , null , InputMake);
        sqLiteDatabase.close();
        if (Result== -1)
            return false;
        else
            return true;
    }
    //===========================================================
    //====== To Create Master Model Code Table Data =============
    public void InsertModeData (String make_code , String model_code , String model_des , String model_sts)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues InputModel = new ContentValues();

        InputModel.put("MAKE_CODE" , make_code);
        InputModel.put("MODEL_CODE" , model_code);
        InputModel.put("MODEL_DESC" , model_des);
        InputModel.put("MODEL_STS" , model_sts);

        sqLiteDatabase.insert("AP_MAST_MODEL", null , InputModel);
        sqLiteDatabase.close();
    }
    //===========================================================
    //====== To Create Supplier Details Code Table Data =============
    public void InsertSupplierData (String CORP_CODE , String SUPPLIER_N , String MAKEESSesc , String ADD1RS_NAM
            , String ADD2LE_NO , String ADD3 , String ADD4L_ID , String CONTACT_NO , String EMAIL)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues Inputsupplier = new ContentValues();

        Inputsupplier.put("CORP_CODE" , CORP_CODE);
        Inputsupplier.put("SUPPLIER_N" , SUPPLIER_N);
        Inputsupplier.put("MAKEESSesc" , MAKEESSesc);
        Inputsupplier.put("ADD1RS_NAM" , ADD1RS_NAM);
        Inputsupplier.put("ADD2LE_NO" , ADD2LE_NO);
        Inputsupplier.put("ADD3" , ADD3);
        Inputsupplier.put("ADD4L_ID" , ADD4L_ID);
        Inputsupplier.put("CONTACT_NO" , CONTACT_NO);
        Inputsupplier.put("EMAIL" , EMAIL);
        sqLiteDatabase.insert("AP_MAST_SUPPLIER" , null , Inputsupplier);
        sqLiteDatabase.close();
    }
    //===========================================================
    //====== To Create Master Branch Code Table Data =============
    public void InsertBranch (String branch_cod , String branch_nam , String Inp_BRANCH_EMAIL_1 , String Inp_BrMgrCode , String Inp_MgrName , String Inp_BranchNo , String Inp_MgePhoneNo)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues InputBranch = new ContentValues();
        InputBranch.put("BRANCH_COD" , branch_cod);
        InputBranch.put("BRANCH_NAM" , branch_nam);
        InputBranch.put("BRANCH_EMAIL_1" , Inp_BRANCH_EMAIL_1);
        InputBranch.put("BR_MANAGER_CODE" , Inp_BrMgrCode);
        InputBranch.put("BR_MANAGER_NANE" , Inp_MgrName);
        InputBranch.put("BR_CONTACTNO" , Inp_BranchNo);
        InputBranch.put("MGR_PHONE" , Inp_MgePhoneNo);
        sqLiteDatabase.insert("AP_MAST_BRANCH" , null , InputBranch);
        sqLiteDatabase.close();
    }

    public void DeleteData (String tablename)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(tablename , null , null);
    }


    //=== Doc Type to upload
    public List<String> SendDocType(String AppRefno , String AppStage , String Holder_Type)
    {
        String ProductCode="";
        String EqCagery="";
        List<String> list = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase_doc_type = this.getReadableDatabase();
        Cursor cursor_doc_procde = sqLiteDatabase_doc_type.rawQuery("SELECT LE_APPLICATION.AP_PRODUCT , LE_ASSET_DETAILS.AS_EQ_CATAGE FROM LE_APPLICATION " +
                "INNER JOIN LE_ASSET_DETAILS ON LE_ASSET_DETAILS.APPLICATION_REF_NO = LE_APPLICATION.APPLICATION_REF_NO WHERE LE_APPLICATION.APPLICATION_REF_NO = '" + AppRefno + "'" , null);
        if (cursor_doc_procde.getCount() != 0)
        {
            cursor_doc_procde.moveToFirst();
            ProductCode = cursor_doc_procde.getString(0);
            EqCagery    = cursor_doc_procde.getString(1);

            Cursor cursor_doc_type = sqLiteDatabase_doc_type.rawQuery("SELECT DOC_NAME FROM AP_MAST_DOC_TYPE WHERE PRODUCT_CODE = '" + ProductCode + "' AND " +
                    "EQ_TYPE = '" + EqCagery + "' and MAN_STAGE = '" + AppStage + "' and HOLDER_TYPE = '" + Holder_Type + "'" , null);
            if (cursor_doc_type.getCount() != 0)
            {
                cursor_doc_type.moveToFirst();
                do{
                    list.add(cursor_doc_type.getString(0));
                }while (cursor_doc_type.moveToNext());
            }
            cursor_doc_type.close();

        }
        cursor_doc_procde.close();
        sqLiteDatabase_doc_type.close();
        return  list;
    }

    //==== Doc - Upload - Application Referance =====
    public List<String> SendDocAppRef(String AppRefno)
    {
        int gursereial=0;
        List<String> list = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursorCLIENT = sqLiteDatabase.rawQuery("SELECT CL_NIC , CL_FULLY_NAME FROM LE_CLIENT_DATA WHERE APPLICATION_REF_NO =  '" + AppRefno + "'" , null );
        if (cursorCLIENT.getCount() != 0)
        {
            cursorCLIENT.moveToFirst();
            list.add(cursorCLIENT.getString(0) + " - " + cursorCLIENT.getString(1) + " -APPLICANT");
        }
        cursorCLIENT.close();

        Cursor cursorGUR = sqLiteDatabase.rawQuery("SELECT CO_NIC ,CO_FULLY_NAME  FROM LE_CO_CL_DATA WHERE APPLICATION_REF_NO = '" + AppRefno + "'" , null);
        if (cursorGUR.getCount() != 0)
        {
            cursorGUR.moveToFirst();
            gursereial=0;
            do{
                gursereial = gursereial + 1;
                list.add(cursorGUR.getString(0) + " - " + cursorGUR.getString(1) + " -GUARANTOR" + String.valueOf(gursereial));
            }while (cursorGUR.moveToNext());
        }
        cursorGUR.close();
        sqLiteDatabase.close();
        return  list;
    }

    //======== Get Sql-Lite (Area Details) ========================
    public List<String> getAllArea(String mProCode , String DicCode)
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_AREA WHERE PRV_CODE = '" + mProCode + "' AND DIS_CODE = '" + DicCode + "' ORDER BY AR_DES asc";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(3));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    //===================================================================

    //======== Get Sql-Lite (District Details) ========================
    public List<String> getAllDistrict(String mProCode)
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_DISTRICT WHERE PRV_CODE = '" + mProCode + "' ORDER BY DIS_NAME asc";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(2));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    //===================================================================

    //======== Get Sql-Lite (Privince Details) ========================
    public List<String> getAllProvince()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_PROVINCE ORDER BY PRV_NAME asc";
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
        return list;
    }
    //===================================================================


    //======== Get Sql-Lite (Occupation) ========================
    public List<String> getOccupation()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_OCCUPATION";
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
        return list;
    }
    //=============================================================

    //======== Get Sql-Lite (Occupation) ========================
    public List<String> getProductConfig()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM MAST_PRODUCT_CONFIG";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(0));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }




    //======== Get Sql-Lite (Make Details) ========================
    public List<String> getAllMakeSqllite()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_MAKE ORDER BY MAKE_DESCR asc";
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
    //=============================================================

    //======== Get Sql-Lite (Insurance Details) ========================
    public List<String> getAllInsuranceSqllite()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_INSURANCECOMPANY ORDER BY INS_NAME asc";

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
    //=============================================================

    //======== Get Sql-Lite (Supplier Details) ========================
    public List<String> getAllSupplierSqllite()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_SUPPLIER ORDER BY SUPPLIER_N asc";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1) + "-" + cursor.getString(0))  ;//adding 2nd column data
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return list;
    }
    //=============================================================

    //======== Get Sql-Lite (Supplier Details) ========================
    public List<String> getAllIntduscerSqllite()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM MAST_INTDUSER ORDER BY NAME asc";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(2) + "-" + cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return list;
    }
    //=============================================================

    //======== Get Sql-Lite (Supplier Details) ========================
    public List<String> getAllOccupationType()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_OCCUPATION_TYPE";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1)) ;
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    //======== Get Sql-Lite (Supplier Details) ========================
    public List<String> getAllDelearSqllite()
    {
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_OWNER ORDER BY SHOWROOM_N asc";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                //list.add(cursor.getString(1) + "-" + cursor.getString(8));//adding 2nd column data
                list.add(cursor.getString(1) );//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }



    //======================================================================

    //======== Get Sql-Lite (Model Details) ========================
    public List<String> getAllModelSqllite(String codemake)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String selectQueryMake = "SELECT  * FROM AP_MAST_MAKE WHERE MAKE_DESCR = '" + codemake.toString() + "'";
        Cursor cursormake = sqLiteDatabase.rawQuery(selectQueryMake, null);
        cursormake.moveToFirst();
        String mMakeCode = cursormake.getString(0);

        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM AP_MAST_MODEL WHERE make_code = '" + mMakeCode + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(2));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public void DeleteAppData (String AppNo)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("LE_CLIENT_DATA","APPLICATION_REF_NO =?",new String[] {AppNo});
        sqLiteDatabase.delete("LE_CO_CL_DATA","APPLICATION_REF_NO =?",new String[] {AppNo});
        sqLiteDatabase.delete("LE_APPLICATION","APPLICATION_REF_NO =?",new String[] {AppNo});
        sqLiteDatabase.delete("LE_ASSET_DETAILS","APPLICATION_REF_NO =?",new String[] {AppNo});
        sqLiteDatabase.delete("LE_CHARGS","APPLICATION_REF_NO =?",new String[] {AppNo});
        sqLiteDatabase.delete("APP_REMARKS","APPNO =?",new String[] {AppNo});
        sqLiteDatabase.close();

    }

}
