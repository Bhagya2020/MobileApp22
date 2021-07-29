package com.AFiMOBILE.afslmobileapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Setting extends AppCompatActivity {

    private Button btnMasterUpdate , BtnChangeProfilePic;
    public String PHP_URL_SQL , LoginUser="" , LoginDate = "" , LoginBranch= "" , mLoginName="";
    SQLiteDatabase sqLiteDatabase;
    SqlliteCreateLeasing sqlliteCreateLeasing;
    public RequestQueue mQueue;
    public ImageView imgProfile;
    public ProgressDialog progressDialog;
    public TextView mFullyName , mNic , mEPFNO , mPhoneNo , mUserId , mBranch;
    final int REQUEST_CODE_GALLERY = 999;
    public static Toolbar toolbar;
    public Handler handler;
    public TextView ConnectionSts;
    private int numberOfRequestsToMake ;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public JsonObjectRequest jsonObjectRequest_GetOccupation,jsonObjectRequest_GetDealer,jsonObjectRequest_GetInsurance,jsonObjectRequest_GetInd,
            jsonObjectRequest_GetMake,jsonObjectRequest_GetModel,jsonObjectRequest_GetSupplier,jsonObjectRequest_GetBranch,jsonObjectRequest_GetProvince,
            jsonObjectRequest_GetDistrict,jsonObjectRequest_GetAreaCode,jsonObjectRequest_GetProductConfig,jsonObjectRequest_GetParamater,
            jsonObjectRequest_OCCUPATIONTYPE ,jsonObjectRequest_GetDocType,jsonObjectRequest_GetPoAppUser,jsonObjectRequest_GetSector,jsonObjectRequest_GetSubSector , jsonObjectRequest_systemconfig ,
            jsonObjectRequest_modelltv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sqlliteCreateLeasing = new SqlliteCreateLeasing(this);

        btnMasterUpdate         =   (Button)findViewById(R.id.btnmaspteupdate);
        BtnChangeProfilePic     =   (Button)findViewById(R.id.btnchangepic);
        imgProfile              =   (ImageView)findViewById(R.id.imgProfilePic);

        mFullyName              =   (TextView)findViewById(R.id.txtFULLYNAME);
        mNic                    =   (TextView)findViewById(R.id.txtNIC) ;
        mEPFNO                  =   (TextView)findViewById(R.id.txtEPFNO) ;
        mPhoneNo                =   (TextView)findViewById(R.id.txtPHONENO) ;
        mUserId                 =   (TextView)findViewById(R.id.txtUSERID) ;
        mBranch                 =   (TextView)findViewById(R.id.txtBranch);

        //=== Get Globle PHP Code Path
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        mLoginName  =   globleClassDetails.getOfficerName();
        checkDataConnectionStatus = new CheckDataConnectionStatus(this);
        PHP_URL_SQL = "http://afimobile.abansfinance.lk/mobilephp/";

        //=== Check Data Connection status Toolbra every 5 second.
        //setSupportActionBar(toolbar);
        ConnectionSts = (TextView) findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();


        //=== Get User Login Details
        SQLiteDatabase sqLiteDatabase_GetLogin = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursor_GetLogin = sqLiteDatabase_GetLogin.rawQuery("SELECT * FROM USER_MANAGEMENT WHERE TRIM (OFFIER_ID) = '" + LoginUser + "'" , null);
        if (cursor_GetLogin.getCount() != 0)
        {
            cursor_GetLogin.moveToFirst();
            mFullyName.setText(cursor_GetLogin.getString(2));
            mNic.setText(cursor_GetLogin.getString(5));
            mEPFNO.setText(cursor_GetLogin.getString(5));
            mPhoneNo.setText(cursor_GetLogin.getString(10));
            mUserId.setText(cursor_GetLogin.getString(0));
            mBranch.setText(cursor_GetLogin.getString(9));
        }

        //===== Create Volly Request Ques. =======
        mQueue   = VollySingleton.getInstance(this).getRequestQueue();

        //=== Create All no of Volly Request
        numberOfRequestsToMake = 0;


        progressDialog = new ProgressDialog(Setting.this);
        progressDialog.setTitle("AFi Mobile");
        progressDialog.setMessage("Loading application View, please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);


        //=== Json Request fineash event
        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>()
        {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                if (numberOfRequestsToMake == 17)
                {
                    progressDialog.dismiss();
                    mQueue.getCache().clear();

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Setting.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("File Upload Successfully");
                    builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                    numberOfRequestsToMake = 0;
                }
            }
        });

        //=====Get Profile Picture =======
        SQLiteDatabase sqLiteDatabase_get_image = sqlliteCreateLeasing.getWritableDatabase();
        Cursor cursor_get_Image = sqLiteDatabase_get_image.rawQuery("SELECT PROFILE_IAME FROM PROFILE_PICTURE WHERE USER_ID = '" + LoginUser + "'" , null);
        if (cursor_get_Image.getCount()!=0)
        {
            cursor_get_Image.moveToFirst();
            byte[] encodeByte = Base64.decode(cursor_get_Image.getString(0), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imgProfile.setImageBitmap(bitmap);
        }

        //==== Error come disable tempery
        BtnChangeProfilePic.setEnabled(false);
        BtnChangeProfilePic.setBackgroundResource(R.drawable.normalbuttondisable);


        BtnChangeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //=== Browse Image Path
                ActivityCompat.requestPermissions(
                        Setting.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

        btnMasterUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //=== check Data connection
                boolean ChekCon = checkDataConnectionStatus.IsConnected();
                if (ChekCon == true)
                {
                    //=== Delete the old Master Data
                    sqlliteCreateLeasing.DeleteData("AP_MAST_OWNER");
                    sqlliteCreateLeasing.DeleteData("AP_MAST_INSURANCECOMPANY");
                    sqlliteCreateLeasing.DeleteData("MAST_INTDUSER");
                    sqlliteCreateLeasing.DeleteData("AP_MAST_MAKE");
                    sqlliteCreateLeasing.DeleteData("AP_MAST_MODEL");
                    sqlliteCreateLeasing.DeleteData("AP_MAST_SUPPLIER");
                    sqlliteCreateLeasing.DeleteData("AP_MAST_BRANCH");
                    sqlliteCreateLeasing.DeleteData("AP_MAST_PROVINCE");
                    sqlliteCreateLeasing.DeleteData("AP_MAST_DISTRICT");
                    sqlliteCreateLeasing.DeleteData("AP_MAST_AREA");
                    sqlliteCreateLeasing.DeleteData("AP_MAST_OCCUPATION");
                    sqlliteCreateLeasing.DeleteData("MAST_PRODUCT_CONFIG");
                    sqlliteCreateLeasing.DeleteData("MAST_PR_PARAMATER");
                    sqlliteCreateLeasing.DeleteData("AP_MAST_DOC_TYPE");
                    sqlliteCreateLeasing.DeleteData("PO_APPROVAL_USER");
                    sqlliteCreateLeasing.DeleteData("APP_CONFIG");
                    sqlliteCreateLeasing.DeleteData("AP_MODEL_LTV");

                    //=== Get Master New Data

                    GetOccupation();
                    GetDealer();
                    GetInsurance();
                    GetInd();
                    GetMake();
                    GetModel();
                    GetSupplier();
                    GetBranch();
                    GetProvince();
                    GetDistrict();
                    GetAreaCode();
                    GetProductConfig();
                    GetParamater();
                    GetDocType();
                    GetPoAppUser();
                    GetSector();
                    GetSubSector();
                    GetSystemConfig();
                    GetOccType();
                    GetModelLtv();

                    AddRequest();
                }
                else
                {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Setting.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("No Data Connection Available.");
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



    private void startCounting() {
        handler.post(run);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run()
        {

            boolean CheckConnection = checkDataConnectionStatus.IsConnected();

            if (CheckConnection ==true)
            {
                ConnectionSts.setText("ONLINE");
                ConnectionSts.setTextColor(Color. parseColor("#2cb72c"));
            }
            else
            {
                ConnectionSts.setText("OFFLINE");
                ConnectionSts.setTextColor(Color. parseColor("#ffffff"));
            }
            handler.postDelayed(this, 2000);
        }
    };

    protected void onDestroy(){
        super.onDestroy();
        mQueue.getCache().clear();
        sqlliteCreateLeasing.close();
        Log.d("Log", "onDestroy-Setting");
    }

    public void AddRequest()
    {
        progressDialog.show();
        mQueue.start();
        mQueue.add(jsonObjectRequest_GetOccupation);
        mQueue.add(jsonObjectRequest_GetDealer);
        mQueue.add(jsonObjectRequest_GetInsurance);
        mQueue.add(jsonObjectRequest_GetInd);
        mQueue.add(jsonObjectRequest_GetMake);
        mQueue.add(jsonObjectRequest_GetModel);
        mQueue.add(jsonObjectRequest_GetSupplier);
        mQueue.add(jsonObjectRequest_GetBranch);
        mQueue.add(jsonObjectRequest_GetProvince);
        mQueue.add(jsonObjectRequest_GetDistrict);
        mQueue.add(jsonObjectRequest_GetAreaCode);
        mQueue.add(jsonObjectRequest_GetProductConfig);
        mQueue.add(jsonObjectRequest_GetParamater);
        mQueue.add(jsonObjectRequest_GetDocType);
        mQueue.add(jsonObjectRequest_GetPoAppUser);
        mQueue.add(jsonObjectRequest_GetSector);
        mQueue.add(jsonObjectRequest_GetSubSector);
        mQueue.add(jsonObjectRequest_systemconfig);
        mQueue.add(jsonObjectRequest_OCCUPATIONTYPE);
        mQueue.add(jsonObjectRequest_modelltv);

    }

   public void GetModelLtv()
   {
       String url = "https://afimobile.abansfinance.lk/mobilephp/MOBILE-MASTER-MODEL-LTV.php";
       jsonObjectRequest_modelltv = new JsonObjectRequest(Request.Method.GET, url, null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {

                       try{

                           SQLiteDatabase sqLiteDatabase_model = sqlliteCreateLeasing.getWritableDatabase();
                           JSONArray myjson = response.getJSONArray("TT-NEW-MODEL-LTV");

                           for (int i = 0; i <= myjson.length(); i++)
                           {
                               JSONObject userid = myjson.getJSONObject(i) ;

                               ContentValues contentValues_mode = new ContentValues();
                               contentValues_mode.put("MAKE_CODE" , userid.getString("make_code"));
                               contentValues_mode.put("MODEL_CODE" , userid.getString("model_code"));
                               contentValues_mode.put("EQ_TYPE" , userid.getString("eq_type"));
                               contentValues_mode.put("YEAR_AGE" , userid.getString("age_catgery"));
                               contentValues_mode.put("LTV_RATE" , userid.getString("ltv_rate"));
                               sqLiteDatabase_model.insert("AP_MODEL_LTV" , null , contentValues_mode);
                           }

                       }catch(Exception e){}

                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

               AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
               bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();

                   }
               })
                       .setTitle("AFSL Mobile Leasing.")
                       .create();
               bmyAlert.show();

           }
       });
   }


    public void GetOccType()
    {
        String url = "https://afimobile.abansfinance.lk/mobilephp/MOBILE-MASTER-OCCTYPE.php";
        jsonObjectRequest_OCCUPATIONTYPE = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String OCC_TYPE="" , OCC_DES="";
                        try{

                            JSONArray myjson = response.getJSONArray("TT-NEW-OCC-TYPE");

                            for (int i = 0; i <= myjson.length(); i++)
                            {
                                JSONObject userid = myjson.getJSONObject(i) ;

                                OCC_TYPE    =   userid.getString("occupationtype_code");
                                OCC_DES    =   userid.getString("occupationtype_descr");

                                sqlliteCreateLeasing.UpdateOccType(OCC_TYPE , OCC_DES);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();

            }
        });
    }

    public void GetSystemConfig()
    {
        String url = "http://afimobile.abansfinance.lk/mobilephp/SystemConfig.php";
        jsonObjectRequest_systemconfig = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String SYS_CON_TYPE="" , CONFIG_VAL="" , CONFIG_VER="" , CONFIG_DATE="";
                        try{

                            JSONArray myjson = response.getJSONArray("TT-SYS-CONFIG");

                            for (int i = 0; i <= myjson.length(); i++)
                            {
                                JSONObject userid = myjson.getJSONObject(i) ;
                                SYS_CON_TYPE =  userid.getString("CONFIG_TYPE");
                                CONFIG_VAL   =  userid.getString("CONFIG_VAL");
                                CONFIG_VER   =  userid.getString("CON_VERSION");
                                CONFIG_DATE  =  userid.getString("CON_DATE");

                                sqlliteCreateLeasing.UpdateConfig(SYS_CON_TYPE , CONFIG_VAL , CONFIG_VER , CONFIG_DATE);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();

            }
        });
    }

    public void GetSubSector()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-SUBSECTOR.php";
        jsonObjectRequest_GetSubSector = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetSubSector");
                        String mSecCode , mSubSecCode , mSubDes;
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-SUBSECTOR");
                            for (int i = 0; i < myjson.length(); i++)
                            {

                                JSONObject JOPRCODE = myjson.getJSONObject(i);

                                mSecCode       =       JOPRCODE.getString("SEC_CODE");
                                mSubSecCode    =       JOPRCODE.getString("SUB_SECCODE");
                                mSubDes        =       JOPRCODE.getString("SUB_SECDES");
                                sqlliteCreateLeasing.InsertSubSector(mSecCode , mSubSecCode , mSubDes);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
        //         100000,
        //       DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        //        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //
        //requestQueue.add(jsonObjectRequest);

        //mQueue.add(jsonObjectRequest_GetSubSector);
    }

    protected void onStart()
    {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public void GetSector()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-SECTOR.php";
        jsonObjectRequest_GetSector = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetSector");
                        String mSecCode , mSecDes;
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-SECTOR");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject JOPRCODE = myjson.getJSONObject(i);

                                mSecCode         =       JOPRCODE.getString("SEC_CODE");
                                mSecDes        =       JOPRCODE.getString("SEC_DES");
                                sqlliteCreateLeasing.InsertSector(mSecCode , mSecDes);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
        //         100000,
        //       DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        //        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //
        //requestQueue.add(jsonObjectRequest);

       // mQueue.add(jsonObjectRequest_GetSector);
    }


    private String imageViewByte(ImageView image)
    {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imagebyte = outputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imagebyte , Base64.DEFAULT);
        return encodeImage;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgProfile.setImageBitmap(bitmap);

                //==== Save Sql Lite ====
                sqlliteCreateLeasing.InsetProfilePic(LoginUser , mLoginName , imageViewByte(imgProfile));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void GetPoAppUser()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-APP-OFFICER.php";
        jsonObjectRequest_GetPoAppUser = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetPoAppUser");
                        String mBrcode="" , mOffCode="" , mOffName="" , mPhoneNo="";
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-APPOFF");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject JOPRCODE = myjson.getJSONObject(i);

                                mBrcode         =       JOPRCODE.getString("BRANCH_CODE");
                                mOffCode        =       JOPRCODE.getString("OFFIER_ID");
                                mOffName        =       JOPRCODE.getString("OFFICER_NAME");
                                mPhoneNo        =       JOPRCODE.getString("PHONE_NO");
                                sqlliteCreateLeasing.GetAppOfficer(mBrcode , mOffCode , mOffName , mPhoneNo);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
        //         100000,
        //       DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        //        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //
        //requestQueue.add(jsonObjectRequest);

        //mQueue.add(jsonObjectRequest_GetPoAppUser);
    }



    public void GetDocType()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-DOC-TYPE.php";

        jsonObjectRequest_GetDocType = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Log", "GetDocType");
                        String mPrcode="" , mEqType="" , mDoctype="" , mDocname="" , mMan_type="" , mManstage="" , mholdType;
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-DOCTYPE");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject JOPRCODE = myjson.getJSONObject(i);

                                mPrcode         =       JOPRCODE.getString("PRODUCT_CODE");
                                mEqType         =       JOPRCODE.getString("EQ_TYPE");
                                mDoctype        =       JOPRCODE.getString("DOC_CODE");
                                mDocname        =       JOPRCODE.getString("DOC_NAME");
                                mMan_type       =       JOPRCODE.getString("MAY_TYPE");
                                mManstage       =       JOPRCODE.getString("MAN_STAGE");
                                mholdType       =       JOPRCODE.getString("HOLDER_TYPE");

                                sqlliteCreateLeasing.CreateMasteDocType(mPrcode , mEqType , mDoctype , mDocname , mMan_type ,mManstage , mholdType );
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
       //         100000,
         //       DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        //        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
        //requestQueue.add(jsonObjectRequest);

        //mQueue.add(jsonObjectRequest_GetDocType);
    }

    public void GetParamater()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-PARAMATER.php";
        jsonObjectRequest_GetParamater = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetParamater");
                        String MPR_CODE = "" , MEQ_TYPE="" , MPAPA_CODE="" , MPARA_DES , MPARA_VAL="";
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-PARAMATER");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject JOPRCODE = myjson.getJSONObject(i) ;

                                MPR_CODE        =   JOPRCODE.getString("PR_CODE");
                                MEQ_TYPE        =   JOPRCODE.getString("EQ_TYPE");
                                MPAPA_CODE      =   JOPRCODE.getString("PRRA_CODE");
                                MPARA_DES       =   JOPRCODE.getString("PARA_DES");
                                MPARA_VAL       =   JOPRCODE.getString("PARA_VAL");
                                sqlliteCreateLeasing.CreateParamater(MPR_CODE , MEQ_TYPE , MPAPA_CODE , MPARA_DES , MPARA_VAL);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

        //mQueue.add(jsonObjectRequest_GetParamater);
    }

    public void GetProductConfig()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-PRCONFIG.php";
        jsonObjectRequest_GetProductConfig = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetProductConfig");
                        String MPR_CODE = "" , MEQ_TYPE="" , METV_RATE="" , MINT_RATE="" , MINT_MAX_RATE , MMIN_FAC="" , MMAX_FAC="";
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-PRODUCT-CIN");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject JOPRCODE = myjson.getJSONObject(i) ;

                                MPR_CODE        =   JOPRCODE.getString("PR_CODE");
                                MEQ_TYPE        =   JOPRCODE.getString("EQ_TYPE");
                                METV_RATE       =   JOPRCODE.getString("ETV_RATE");
                                MINT_RATE       =   JOPRCODE.getString("INT_RATE");
                                MINT_MAX_RATE   =  JOPRCODE.getString("INT_RATE_MAX");
                                MMIN_FAC        =   JOPRCODE.getString("MIN_FAC_AMT");
                                MMAX_FAC        =   JOPRCODE.getString("MAX_FAC_AMT");
                                sqlliteCreateLeasing.CreateProuuct(MPR_CODE , MEQ_TYPE , METV_RATE , MINT_RATE , MINT_MAX_RATE , MMIN_FAC , MMAX_FAC);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

        //mQueue.add(jsonObjectRequest_GetProductConfig);
    }


    public void GetOccupation()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-OCCUPATION.php";
        jsonObjectRequest_GetOccupation = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetOccupation");
                        String OCC_CODE="" , OCC_DES = "" , CRE_RATE="" ;
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-OCCPATION");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject JOAREA = myjson.getJSONObject(i) ;

                                OCC_CODE        =       JOAREA.getString("OCCUP_CODE");
                                OCC_DES         =       JOAREA.getString("OCCUP_DES");
                                CRE_RATE        =       JOAREA.getString("CREDIT_RATE");
                                sqlliteCreateLeasing.CreateOccupa(OCC_CODE , OCC_DES , CRE_RATE);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

       // mQueue.add(jsonObjectRequest_GetOccupation);
    }

    public void GetAreaCode()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-AREA-CODE.php";
        jsonObjectRequest_GetAreaCode = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetAreaCode");
                        String PRV_CODE="" , DES_CODE = "" , ARE_CODE="" , DES = "" ;
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-AREA");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject JOAREA = myjson.getJSONObject(i) ;

                                PRV_CODE        =       JOAREA.getString("PRV_CODE");
                                DES_CODE        =       JOAREA.getString("DIS_CODE");
                                ARE_CODE        =       JOAREA.getString("AR_CODE");
                                DES             =       JOAREA.getString("AR_DES");

                                sqlliteCreateLeasing.CreateAreaCode(PRV_CODE , DES_CODE , ARE_CODE , DES);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

        //mQueue.add(jsonObjectRequest_GetAreaCode);
    }

    public void GetDistrict()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-DISTRICT.php";
        jsonObjectRequest_GetDistrict = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetDistrict");
                        String PRV_CODE="" , DES_CODE = "" , DES="" ;
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-DISTRICT");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject JODISTRICT = myjson.getJSONObject(i) ;

                                PRV_CODE        =       JODISTRICT.getString("PRV_CODE");
                                DES_CODE        =       JODISTRICT.getString("DIS_CODE");
                                DES             =       JODISTRICT.getString("DIS_NAME");

                                sqlliteCreateLeasing.CreateDistrict(PRV_CODE , DES_CODE , DES);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */
        //mQueue.add(jsonObjectRequest_GetDistrict);
    }

    public  void GetProvince()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-PROVINCE.php";
        //String url = "http://203.115.12.125:82//core//mobnew//MOBILE-MASTER-DELER.php";
        jsonObjectRequest_GetProvince = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetProvince");
                        String CODE="" , DES="" ;
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-PROVINCE");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject JOPROVINCE = myjson.getJSONObject(i) ;

                                CODE       =       JOPROVINCE.getString("CODE");
                                DES        =       JOPROVINCE.getString("PRO_NAME");


                                sqlliteCreateLeasing.CreateProvince(CODE , DES);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

        //mQueue.add(jsonObjectRequest_GetProvince);
    }

    public void GetDealer ()
    {
        String url = PHP_URL_SQL + "MOBILE-MASTER-DELER.php";
        //String url = "http://203.115.12.125:82//core//mobnew//MOBILE-MASTER-DELER.php";
        jsonObjectRequest_GetDealer = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetDealer");
                        String AREA_CODE="" , SHOWROOM_N="" , ADDRESS="" , OWNERS_NAM="", MOBILE_NO="" , MAKE="" , EMAIL_ID="" , ID="" , NICBR="" , STS="";
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-DELERA");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject delere = myjson.getJSONObject(i) ;

                                AREA_CODE       =       delere.getString("AREA_CODE");
                                SHOWROOM_N      =       delere.getString("SHOWROOM_N");
                                ADDRESS         =       delere.getString("ADDRESS");
                                OWNERS_NAM      =       delere.getString("OWNERS_NAM");
                                MOBILE_NO       =       delere.getString("MOBILE_NO");
                                MAKE            =       delere.getString("MAKE");
                                EMAIL_ID        =       delere.getString("EMAIL_ID");
                                ID              =       delere.getString("ID");
                                NICBR           =       delere.getString("NICBR");
                                STS             =       delere.getString("DELEAR_STS");

                                sqlliteCreateLeasing.InsertOwner(AREA_CODE,SHOWROOM_N,ADDRESS,OWNERS_NAM,MOBILE_NO,MAKE,EMAIL_ID,ID,NICBR,STS);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

        //mQueue.add(jsonObjectRequest_GetDealer);
    }

    public void GetInsurance ()
    {
        //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //String url = "http://203.115.12.125:82//core//mobnew//MOBILE-MASTER-INSURANCE.php";
        String url = PHP_URL_SQL + "MOBILE-MASTER-INSURANCE.php";

        jsonObjectRequest_GetInsurance = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetInsurance");
                        String INS_CODE="" , INS_NAME="" ;
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-INSURANCE");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject mydatajson = myjson.getJSONObject(i) ;

                                INS_CODE       =      mydatajson.getString("insurancec");
                                INS_NAME      =       mydatajson.getString("insurance1");

                                sqlliteCreateLeasing.InsertInsurance(INS_CODE,INS_NAME);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

        //mQueue.add(jsonObjectRequest_GetInsurance);
    }

    public void GetInd ()
    {
        //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //String url = "http://203.115.12.125:82//core//mobnew//MOBILE-MASTER-INTDUSER.php";
        String url = PHP_URL_SQL + "MOBILE-MASTER-INTDUSER.php";
        jsonObjectRequest_GetInd = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetInd");
                        String BRANCH="" , ID_NO="" , NAME="" , AREA="" , CLIENT_COD="" , CC_CREATED=""  ;
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-INTERDU");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject mydatajson = myjson.getJSONObject(i) ;

                                BRANCH          =       mydatajson.getString("BRANCH");
                                ID_NO           =       mydatajson.getString("ID_NO");
                                NAME            =       mydatajson.getString("NAME");
                                AREA            =       mydatajson.getString("AREA");
                                CLIENT_COD      =       mydatajson.getString("CLIENT_COD");
                                CC_CREATED      =       mydatajson.getString("CC_CREATED");


                                sqlliteCreateLeasing.InsertIntduser(BRANCH,ID_NO,NAME,AREA,CLIENT_COD,CC_CREATED);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

        //mQueue.add(jsonObjectRequest_GetInd);
    }

    public void GetMake ()
    {

        //String url = "http://203.115.12.125:82//core//mobnew//MOBILE-MASTER-MAKE.php";
        String url = PHP_URL_SQL + "MOBILE-MASTER-MAKE.php";

        jsonObjectRequest_GetMake = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetMake");
                        String MAKE_CODE="" , MAKE_DESCR=""  ;
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-MAKE");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject mydatajson = myjson.getJSONObject(i) ;

                                MAKE_CODE          =       mydatajson.getString("make_code");
                                MAKE_DESCR         =       mydatajson.getString("make_descr");

                                sqlliteCreateLeasing.InsertMakeData(MAKE_CODE,MAKE_DESCR);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);

        */

        //mQueue.add(jsonObjectRequest_GetMake);
    }

    public void GetModel ()
    {

        //String url = "http://203.115.12.125:82//core//mobnew//MOBILE-MASTER-MODEL.php";
        String url = PHP_URL_SQL + "MOBILE-MASTER-MODEL.php";

        jsonObjectRequest_GetModel = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetModel");
                        String MAKE_CODE="" , MODEL_CODE="" , MODEL_DESC="" , MODEL_STS="";
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-MODEL");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject mydatajson = myjson.getJSONObject(i) ;

                                MAKE_CODE          =        mydatajson.getString("make_code");
                                MODEL_CODE         =        mydatajson.getString("model_code");
                                MODEL_DESC         =        mydatajson.getString("model_desc");
                                MODEL_STS          =        mydatajson.getString("model_sts");

                                sqlliteCreateLeasing.InsertModeData(MAKE_CODE,MODEL_CODE,MODEL_DESC,MODEL_STS);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

        //mQueue.add(jsonObjectRequest_GetModel);
    }

    public void GetSupplier()
    {
        //String url = "http://203.115.12.125:82//core//mobnew//MOBILE-MASTER-SUPPLIER.php";
        String url = PHP_URL_SQL + "MOBILE-MASTER-SUPPLIER.php";

        jsonObjectRequest_GetSupplier = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetSupplier");
                        String CORP_CODE="" , SUPPLIER_N="" , MAKEESSesc="" , ADD1RS_NAM="" , ADD2LE_NO="" , ADD3="" ,ADD4L_ID="" ,CONTACT_NO="",
                                EMAIL="";
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-SUPPLIER");

                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject mydatajson = myjson.getJSONObject(i) ;

                                CORP_CODE           =       mydatajson.getString("CORP_CODE");
                                SUPPLIER_N          =       mydatajson.getString("SUPPLIER_N");
                                MAKEESSesc          =       mydatajson.getString("MAKEESSesc");
                                ADD1RS_NAM          =       mydatajson.getString("ADD1RS_NAM");
                                ADD2LE_NO           =       mydatajson.getString("ADD2LE_NO");
                                ADD3                =       mydatajson.getString("ADD3");
                                ADD4L_ID            =       mydatajson.getString("ADD4L_ID");
                                CONTACT_NO          =       mydatajson.getString("CONTACT_NO");
                                EMAIL          =       mydatajson.getString("EMAIL");

                                sqlliteCreateLeasing.InsertSupplierData(CORP_CODE,SUPPLIER_N,MAKEESSesc,ADD1RS_NAM,ADD2LE_NO,ADD3,ADD4L_ID,CONTACT_NO,EMAIL);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

        //mQueue.add(jsonObjectRequest_GetSupplier);
    }

    public void GetBranch()
    {
        //String url = "http://203.115.12.125:82//core//mobnew//MOBILE-MASTER-BRANCH.php";

        String url = PHP_URL_SQL + "MOBILE-MASTER-BRANCH.php";
        jsonObjectRequest_GetBranch = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Log", "GetBranch");
                        String BRANCH_COD="" , BRANCH_NAM="" , BRANCH_EMAIL="" , BR_MGRCODE="" , MGR_NAME="",BRANCH_NO="",MGR_PHONENO="";
                        try{
                            numberOfRequestsToMake = numberOfRequestsToMake + 1;
                            JSONArray myjson = response.getJSONArray("TT-NEW-BRANCH");
                            for (int i = 0; i < myjson.length(); i++)
                            {
                                JSONObject mydatajson = myjson.getJSONObject(i) ;

                                BRANCH_COD          =       mydatajson.getString("BRANCH_CODE");
                                BRANCH_NAM          =       mydatajson.getString("BRANCH_NAME");
                                BRANCH_EMAIL        =       mydatajson.getString("BRANCH_EMAIL_1");
                                BR_MGRCODE          =       mydatajson.getString("BR_MANAGER_CODE");
                                MGR_NAME            =       mydatajson.getString("BR_MANAGER_NANE");
                                BRANCH_NO           =       mydatajson.getString("BR_CONTACTNO");
                                MGR_PHONENO         =       mydatajson.getString("MGR_PHONE");

                                sqlliteCreateLeasing.InsertBranch(BRANCH_COD,BRANCH_NAM,BRANCH_EMAIL,BR_MGRCODE,MGR_NAME,BRANCH_NO,MGR_PHONENO);
                            }

                        }catch(Exception e){}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Setting.this);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
        */

        //mQueue.add(jsonObjectRequest_GetBranch);
    }
}
