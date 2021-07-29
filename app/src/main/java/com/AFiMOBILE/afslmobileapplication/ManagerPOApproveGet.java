package com.AFiMOBILE.afslmobileapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class ManagerPOApproveGet
{
    public Context mContex;
    public String PHP_URL_SQL , mInputLoginCode="";
    public SqlliteCreateLeasing sqlliteCreateLeasing_GetManageappData;
    public SQLiteDatabase sqLiteDatabase_GetManageApprove;
    private android.app.AlertDialog progressDialog;
    public RequestQueue mQueue;
    public String mRespnces_GetAllData;
    private GetManagerPendingPO getManagerPendingPO;


    public ManagerPOApproveGet (Context context)
    {
        mContex =   context;
        sqlliteCreateLeasing_GetManageappData   =   new SqlliteCreateLeasing(mContex);
        progressDialog          =   new SpotsDialog(mContex, R.style.Custom);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();

        getManagerPendingPO = new GetManagerPendingPO(mContex);

        //=== Create Request
        final Cache cache = new DiskBasedCache(mContex.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        mQueue = new RequestQueue(cache, network);
        mQueue.getCache().clear();
    }

    public void GetManagerApprovePOData(String mLoginCode)
    {
        mInputLoginCode = mLoginCode;
        Log.d("GetManageApproveData" , "Start");
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("MGR_CODE" , mLoginCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String PostUrlPendingPO = PHP_URL_SQL + "MOBILE-GET-APPROVE-PO.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PostUrlPendingPO, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("GetManageApproveData" , "Responces-DONE");
                        try {
                            String InputAppRefno="" , mClnic="" , mClfullyName="" , mFacAmt="" , mMkOfficer="" , mBranchCode="",mPosts="";
                            JSONArray jsonArraygetdata = response.getJSONArray("TT-GET-PENDING-PO");
                            for (int i=0 ; i< jsonArraygetdata.length();i++)
                            {
                                JSONObject mydataJson = jsonArraygetdata.getJSONObject(i);
                                InputAppRefno        =   mydataJson.getString("APPLICATION_REF_NO") ;
                                mClnic          =   mydataJson.getString("CL_NIC") ;
                                mClfullyName    =   mydataJson.getString("CL_FULLY_NAME") ;
                                mFacAmt         =   mydataJson.getString("AP_FACILITY_AMT") ;
                                mMkOfficer      =   mydataJson.getString("AP_MK_OFFICER") ;

                                mBranchCode =   InputAppRefno.substring(1,2);
                                mPosts      =   "001";

                                //=== Manager Pending Application
                                sqlliteCreateLeasing_GetManageappData.CreatePendingPO(InputAppRefno,mClnic,mClfullyName,mFacAmt,mMkOfficer,mBranchCode,mPosts,mInputLoginCode);
                                Log.d("GetManageApproveData" , "DONE");
                            }
                            mRespnces_GetAllData="DONE";


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

                progressDialog.dismiss();
                String ErrorDescription="Responses Failure.(" + ErrorCode.toString() + ")";
                android.app.AlertDialog.Builder bmyAlert = new AlertDialog.Builder(mContex );
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
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
                if (mRespnces_GetAllData != null)
                {
                    if (mRespnces_GetAllData.equals("DONE"))
                    {
                        CheckResponce = mRespnces_GetAllData.equals("DONE");
                    }
                }

                if (CheckResponce == true)
                {

                    ManagerApprove managerApprove = new ManagerApprove();
                    managerApprove.myPenadapter.swapCursor(getManagerPendingPO.GetManagerApprove());

                    Log.d("GetManageApproveData", "COMPLETE");
                    progressDialog.dismiss();
                    //mQueue.getCache().clear();
                }
            }
        });
    }
}
