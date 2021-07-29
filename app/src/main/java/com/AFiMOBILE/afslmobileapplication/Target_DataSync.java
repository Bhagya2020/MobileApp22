package com.AFiMOBILE.afslmobileapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class Target_DataSync {

    private Context mContex;
    private RequestQueue mQueue;
    private android.app.AlertDialog progressDialog;
    private SqlliteCreateLeasing sqlliteCreateLeasing_me_reports;
    private SQLiteDatabase sqLiteDatabase_me_reports;
    private String JsonRsponces;

    public Target_DataSync(Context context )
    {
        mContex = context;
        mQueue   = VollySingleton.getInstance(mContex).getRequestQueue();
        progressDialog = new SpotsDialog(mContex, R.style.Custom);

        sqlliteCreateLeasing_me_reports = new SqlliteCreateLeasing(mContex);
    }


    public void Get_Target_data (final String InputLogin)
    {
        JSONObject jsonObject_data = new JSONObject();
        try {
            jsonObject_data.put("ME_CODE" , InputLogin);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sqLiteDatabase_me_reports = sqlliteCreateLeasing_me_reports.getWritableDatabase();
        sqLiteDatabase_me_reports.delete("ME_TARGET_DETAILS", "mkt_officer =?", new String[]{InputLogin});
        sqLiteDatabase_me_reports.delete("ME_GRANTING_DETAILS", "maketing_code =?", new String[]{InputLogin});

        String url = "http://afimobile.abansfinance.lk/mobilephp/MOBILE-ME-TARGET-DATA.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject_data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("GetDataLive-Respond" , "DONE");

                        try {
                            JSONArray jsonArray_Me_target         =  response.getJSONArray("TT-ME-TARGET");
                            JSONArray jsonArray_Me_granting       =  response.getJSONArray("TT-ME-GRANTING");

                            Log.d("count-target" , String.valueOf(jsonArray_Me_target.length()) );
                            Log.d("count-granting" , String.valueOf(jsonArray_Me_granting.length()) );

                            //=== Target Details
                            for (int i = 0; i < jsonArray_Me_target.length(); i++)
                            {
                                JSONObject jsonObject_target = jsonArray_Me_target.getJSONObject(i);

                                ContentValues contentValues_target = new ContentValues();
                                contentValues_target.put("mkt_officer" , jsonObject_target.getString("mkt_officer"));
                                contentValues_target.put("year" , jsonObject_target.getString("year"));
                                contentValues_target.put("month" , jsonObject_target.getString("month"));
                                contentValues_target.put("category" , jsonObject_target.getString("category"));
                                contentValues_target.put("make" , jsonObject_target.getString("make"));
                                contentValues_target.put("eq_type" , jsonObject_target.getString("eq_type"));
                                contentValues_target.put("mkt_officer_branch" , jsonObject_target.getString("mkt_officer_branch"));
                                contentValues_target.put("mkt_officer_name" , jsonObject_target.getString("mkt_officer_name"));
                                contentValues_target.put("target_value" , jsonObject_target.getString("target_value"));
                                contentValues_target.put("target_number" , jsonObject_target.getString("target_number"));
                                contentValues_target.put("act_value" , jsonObject_target.getString("act_value"));
                                contentValues_target.put("act_number" , jsonObject_target.getString("act_number"));
                                sqLiteDatabase_me_reports.insert("ME_TARGET_DETAILS" , null , contentValues_target);
                            }

                            //=== Grnting Details
                            for (int i = 0; i < jsonArray_Me_granting.length(); i++)
                            {
                                JSONObject jsonObject_granting = jsonArray_Me_granting.getJSONObject(i);

                                ContentValues contentValues_granting = new ContentValues();
                                contentValues_granting.put("fac_no" , jsonObject_granting.getString("fac_no"));
                                contentValues_granting.put("vehicle_no" , jsonObject_granting.getString("vehicle_no"));
                                contentValues_granting.put("rental_amount" , jsonObject_granting.getString("rental_amount"));
                                contentValues_granting.put("product" , jsonObject_granting.getString("product"));
                                contentValues_granting.put("product_type" , jsonObject_granting.getString("product_type"));
                                contentValues_granting.put("customer_full_name" , jsonObject_granting.getString("customer_full_name"));
                                contentValues_granting.put("facility_amount" , jsonObject_granting.getString("facility_amount"));
                                contentValues_granting.put("activation_date" , jsonObject_granting.getString("activation_date"));
                                contentValues_granting.put("total_facility_amount" , jsonObject_granting.getString("total_facility_amount"));
                                contentValues_granting.put("make" , jsonObject_granting.getString("make"));
                                contentValues_granting.put("model" , jsonObject_granting.getString("model"));
                                contentValues_granting.put("eq_type" , jsonObject_granting.getString("eq_type"));
                                contentValues_granting.put("category" , jsonObject_granting.getString("category"));
                                contentValues_granting.put("maketing_code" , InputLogin);
                                sqLiteDatabase_me_reports.insert("ME_GRANTING_DETAILS" , null , contentValues_granting);

                            }
                            Log.d("GetDataLive-TARGET" , "DONE");
                            JsonRsponces = "DONE";


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex);
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog.show();
        mQueue.start();
        mQueue.add(jsonObjectRequest);


        //==== Check Finsh Responces
        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request)
            {
                boolean CheckResponce = false;
                if (JsonRsponces != null)
                {
                    if (JsonRsponces.equals("DONE"))
                    {
                        CheckResponce = JsonRsponces.equals("DONE");
                    }
                }

                if (CheckResponce == true)
                {
                    Log.d("GetDataLive-TARGET" , "DONE");
                    progressDialog.dismiss();

                    mQueue.getCache().clear();

                    if(!((Activity) mContex).isFinishing())
                    {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContex);
                        builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                        builder.setMessage("Data Process Successfully.");
                        builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });
                        builder.create();
                        builder.show();
                        sqLiteDatabase_me_reports.close();
                    }

                }
            }
        });
    }
}
