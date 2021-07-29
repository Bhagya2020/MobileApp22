package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

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

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Recovery_Password extends AppCompatActivity {

    private Button mBtnLogin;
    public EditText mRecUserName , mRecPassword;
    public TextView mErrormsg;
    public Switch SweRemebme;
    public SqlliteCreateLeasing sqlliteCreateLeasing_RecLogin;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "userid";
    public String mSaveUserId;
    public LoadToast MsgWait;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public RequestQueue requestQueue_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery__password);

        mErrormsg = (TextView)findViewById(R.id.txterrorshow) ;
        sqlliteCreateLeasing_RecLogin = new SqlliteCreateLeasing(this);
        checkDataConnectionStatus   = new CheckDataConnectionStatus(Recovery_Password.this);

        mRecUserName    = (EditText)findViewById(R.id.txtUSERID);
        mRecPassword    =  (EditText)findViewById(R.id.txtPASSWORD);
        SweRemebme      = (Switch)findViewById(R.id.remuserid) ;

        mRecUserName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        //=== Please Wait Massagwe
        MsgWait = new LoadToast(Recovery_Password.this);
        MsgWait.setText(" Please Wait..." );
        MsgWait.setTextColor(Color.rgb(173,56,109)).setBackgroundColor(Color.WHITE).setProgressColor(Color.BLUE);
        MsgWait.setTextDirection(true);
        MsgWait.setTranslationY(820);

        //=== Set Rember User name
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        mSaveUserId = sharedPreferences.getString(TEXT , "");
        mRecUserName.setText(mSaveUserId);


        mBtnLogin = (Button)findViewById(R.id.btnLOGIN);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CheckLogin();
            }
        });
    }


    public void CheckLogin()
    {
        if (mRecUserName.length() != 0)
        {
            if (mRecPassword.length() != 0)
            {
               //=== Check Login Details
                SQLiteDatabase sqLiteDatabase = sqlliteCreateLeasing_RecLogin.getReadableDatabase();
                Cursor cursor_login = sqLiteDatabase.rawQuery("SELECT * FROM USER_MANAGEMENT WHERE TRIM (OFFIER_ID) = '" + mRecUserName.getText().toString().trim() + "'" , null);
                if (cursor_login.getCount() != 0)
                {
                    cursor_login.moveToFirst();

                    String mPassword="" , LoginPasword="";
                    mPassword = cursor_login.getString(1).trim();
                    LoginPasword = mRecPassword.getText().toString();


                    if (mPassword.equals(LoginPasword))
                    {
                        // Client Details Insert ===============
                        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
                        String NewDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        globleClassDetails.setUserid(cursor_login.getString(0));
                        globleClassDetails.setOfficerName(cursor_login.getString(2));
                        globleClassDetails.setLoginBranch(cursor_login.getString(3));
                        globleClassDetails.setLoginDate(NewDate);

                        //=== Run system config
                        //GetSystemConfig getSystemConfig = new GetSystemConfig(Recovery_Password.this);
                        //getSystemConfig.UpdateSystemConfig();

                        //=== Set Save Sharable Data ("User name")
                        if (SweRemebme.isChecked() == true)
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(TEXT , mRecUserName.getText().toString());
                            editor.apply();
                        }

                        Intent intent = new Intent("android.intent.action.Recovery_Home");
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        mErrormsg.setText("Password is Wrong");
                    }


                }
                else
                {
                    postData();
                }
            }
            else
            {
                mErrormsg.setText("Password is blank.");
            }
        }
        else
        {
            mErrormsg.setText("User Name is blank.");
        }


    }

    public void postData()
    {
        boolean mCheckSts = checkDataConnectionStatus.IsConnected();
        if (mCheckSts == true)
        {
            //=== Run system config

            MsgWait.show();
            JSONObject object = new JSONObject();
            //== Create Json File To Send User ID
            try {
                //input your API parameters
                object.put("NEW_USER_ID",mRecUserName.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = "http://afimobile.abansfinance.lk/mobilephp/MOBILE-PO-GET-NEWUSER.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            String OFFIER_ID="" , PWD="" , OFFICER_NAME="" , DEPARTMENT="", INACTIVE="" , OFFICER_EPF="" , OFFICER_EMAIL="" ,
                                    OFF_TYPE="" , MUSER_ROLL="" , MBRCODE ="" , MPHONENO="";
                            try{

                                JSONArray myjson = response.getJSONArray("TT-NEW-USER");
                                for (int i = 0; i <= myjson.length(); i++)
                                {

                                    JSONObject userid = myjson.getJSONObject(i) ;

                                    OFFIER_ID       =   userid.getString("OFFIER_ID");
                                    PWD             =   userid.getString("PWD");
                                    OFFICER_NAME    =   userid.getString("OFFICER_NAME");
                                    DEPARTMENT      =   userid.getString("DEPARTMENT");
                                    INACTIVE        =   userid.getString("INACTIVE");
                                    OFFICER_EPF     =   userid.getString("OFFICER_EPF");
                                    OFFICER_EMAIL   =   userid.getString("OFFICER_EMAIL");

                                    OFF_TYPE        =   userid.getString("OFFIER_TYPE");
                                    MUSER_ROLL      =   userid.getString("USER_ROLL");
                                    MBRCODE         =   userid.getString("BRANCH_CODE");
                                    MPHONENO        =   userid.getString("PHONE_NO");

                                    sqlliteCreateLeasing_RecLogin.CreateNewUser(OFFIER_ID , PWD , OFFICER_NAME , DEPARTMENT, INACTIVE , OFFICER_EPF , OFFICER_EMAIL , OFF_TYPE ,
                                            MUSER_ROLL , MBRCODE , MPHONENO);
                                    MsgWait.hide();

                                    //=== Call Login Deatils Class
                                    CheckLogin();
                                }
                            }catch(Exception e){}

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

                    MsgWait.hide();
                    String ErrorDescription="Responses Failure.(" + ErrorCode.toString() + ")";
                    AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Recovery_Password.this );
                    bmyAlert.setMessage(ErrorDescription).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                            .setTitle("Error...")
                            .create();
                    bmyAlert.show();
                }
            });

            //requestQueue_app.getCache().clear();
            //requestQueue_app.add(jsonObjectRequest);

            VollySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
        }
        else
        {
            MsgWait.hide();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
            builder.setMessage("Data Connection Not available. Please Turn on Connection. ** Note - This is your First Login.It is Need to Mobile Data On.");
            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();

        }
    }
}
