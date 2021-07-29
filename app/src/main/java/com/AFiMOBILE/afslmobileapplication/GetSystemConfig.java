package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetSystemConfig
{
    private Context mContex;
    public SqlliteCreateLeasing sqlliteCreateLeasing_GetSystemconfig;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public SQLiteDatabase sqLiteDatabase_GetConfig;
    public String PHP_url_version;

    public GetSystemConfig (Context context)
    {
        mContex = context;
        Log.d("Systemconfig-" , "RUN");
        sqlliteCreateLeasing_GetSystemconfig = new SqlliteCreateLeasing(mContex);
        checkDataConnectionStatus = new CheckDataConnectionStatus(mContex);
    }


    public void UpdateSystemConfig()
    {
        sqLiteDatabase_GetConfig  = sqlliteCreateLeasing_GetSystemconfig.getReadableDatabase();
        Cursor cursordata = sqLiteDatabase_GetConfig.rawQuery("SELECT * FROM APP_CONFIG" , null);
        if (cursordata.getCount() != 0)
        {
            cursordata.moveToFirst();
            //=== Get PHP Url Version ===
            if (cursordata.getString(0) == "PHP_URL")
            {
                PHP_url_version = cursordata.getString(2);
            }
        }
        else
        {
            PHP_url_version = "0";
        }
        cursordata.close();
        sqLiteDatabase_GetConfig.close();

        if (checkDataConnectionStatus.IsConnected() == true)
        {
            String url = "http://afimobile.abansfinance.lk/mobilephp/SystemConfig.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
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


                                    sqlliteCreateLeasing_GetSystemconfig.UpdateConfig(SYS_CON_TYPE , CONFIG_VAL , CONFIG_VER , CONFIG_DATE);

                                    /*
                                    //=== Not Data Availble in Sqllite Config Table
                                    if (PHP_url_version.equals("0"))
                                    {
                                        sqlliteCreateLeasing_GetSystemconfig.UpdateConfig(SYS_CON_TYPE , CONFIG_VAL , CONFIG_VER , CONFIG_DATE);
                                    }
                                    else //=== Check System config Version
                                        if (PHP_url_version != CONFIG_VER )
                                        {
                                            sqlliteCreateLeasing_GetSystemconfig.UpdateConfig(SYS_CON_TYPE , CONFIG_VAL , CONFIG_VER , CONFIG_DATE);
                                        }
                                        */
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

                    /*
                    String ErrorDescription="Responses Failure.(" + ErrorCode.toString() + ")";
                    AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex );
                    bmyAlert.setMessage(ErrorDescription).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                            .setTitle("Error...")
                            .create();
                    bmyAlert.show();
                    */
                }
            });

            VollySingleton.getInstance(mContex).getRequestQueue().add(jsonObjectRequest);
        }
        else
        {
            if (PHP_url_version == "0")
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContex);
                builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                builder.setMessage("System Version Old. Please turn on data and Re-Login.");
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

}
