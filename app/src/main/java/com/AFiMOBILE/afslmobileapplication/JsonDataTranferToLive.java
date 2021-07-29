package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonDataTranferToLive
{
    private Context mContex;
    private RequestQueue mQueue;
    private android.app.AlertDialog progressDialog;

    public JsonDataTranferToLive(Context context )
    {
        mContex = context;
        //===== Create Volly Request Ques. =======
        mQueue   = VollySingleton.getInstance(mContex).getRequestQueue();
    }

    public void SendDataToLive (String Url , JSONObject jsonObject)
    {
        String PostUrl = Url;
        JSONObject jsonData = jsonObject;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PostUrl, jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.data tranfer")
                        .create();
                bmyAlert.show();
            }
        });


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        mQueue.add(jsonObjectRequest);

        //RequestQueue requestQueue = Volley.newRequestQueue(mContex);
        //requestQueue.add(jsonObjectRequest);

    }

    public void GetDatatoLive (String UrlGet , String ArryName)
    {
        final String PostUrl = UrlGet;
        final String Name = ArryName;
        JSONObject jsonGetData = new JSONObject();
        //RequestQueue requestQueue = Volley.newRequestQueue(mContex);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PostUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            JSONArray jsonArraygetdata = response.getJSONArray(Name);



                        }catch(Exception e){}


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex);
                bmyAlert.setMessage(error.toString()).setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                        .setTitle("AFSL Mobile Leasing.test")
                        .create();
                bmyAlert.show();
            }
        });

        /*
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                */

        mQueue.add(jsonObjectRequest);
        //requestQueue.add(jsonObjectRequest);
    }
}
