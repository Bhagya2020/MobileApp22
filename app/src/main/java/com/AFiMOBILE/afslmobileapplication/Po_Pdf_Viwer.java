package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Po_Pdf_Viwer extends AppCompatActivity {

    private File pdfFile;
    private Button mbtn , mBtnViewImage , mBtnViewVrib  , btnApprovre , btnReject;
    private PDFView pdfView_doc;
    public String mInpAppno;
    private TextView mSelAppno ;
    public String LoginUser="" , LoginDate = "" , LoginBranch= "" , PHP_URL_SQL = "";
    public SqlliteCreateLeasing sqlliteCreateLeasing_ManagerDocView;
    public SQLiteDatabase sqLiteDatabase_ManagerDocView;
    public EditText mManagerRemarks , mNIC , mFULLYNAME , mADDERS , mOCCUPATION , mPHONENO , MREMARKS;
    public EditText mPRODUCT , mMKVAL , mINV_CAL , mCLIENT_CON , mFAC_AMT , mEXPRATE , mRATE , mPERIOD , mRENTAL;
    public EditText mASSTYPE , mNATURE , mMAKE , mMODEL , mYEAR , mREGNO , mENFNO , mCHASSNO, mSUPP , mDELEAR , mINTDUSER , mINSURANCE;
    public EditText mIND_AMT , mSCH_AMT , mREV_AMT , mINS_AMT , mTRP_AMT , mSTAM_AMT , mOTH_AMT , mMK_COMMENT;
    CheckDataConnectionStatus checkDataConnectionStatus;
    public static Toolbar toolbar;
    public Handler handler;
    public TextView ConnectionSts;
    private ImageButton BtnCallClient;


    private RequestQueue mQueue;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po__pdf__viwer);


        //=== Create Data Base Connection
        sqlliteCreateLeasing_ManagerDocView = new SqlliteCreateLeasing(this);
        sqLiteDatabase_ManagerDocView = sqlliteCreateLeasing_ManagerDocView.getReadableDatabase();

        mSelAppno  = (TextView)findViewById(R.id.txtSelectappno2);

        mNIC            =   (EditText)findViewById(R.id.txtnic);
        mFULLYNAME      =   (EditText)findViewById(R.id.txtfullyname);
        mADDERS         =   (EditText)findViewById(R.id.txtadders);
        mOCCUPATION     =   (EditText)findViewById(R.id.txtocupation);
        mPHONENO        =   (EditText)findViewById(R.id.txtphoneno);
        MREMARKS        =   (EditText)findViewById(R.id.txtremarks);

        mPRODUCT        =   (EditText)findViewById(R.id.txtproduct);
        mMKVAL          =   (EditText)findViewById(R.id.txtMKVAL);
        mINV_CAL        =   (EditText)findViewById(R.id.txtinv_val);
        mCLIENT_CON     =   (EditText)findViewById(R.id.txtcocon);
        mFAC_AMT        =   (EditText)findViewById(R.id.txtleamt);
        mEXPRATE        =   (EditText)findViewById(R.id.txtexprate);
        mRATE           =   (EditText)findViewById(R.id.txtrate);
        mPERIOD         =   (EditText)findViewById(R.id.txtperiod);
        mRENTAL         =   (EditText)findViewById(R.id.txtrantal);

        mASSTYPE        =   (EditText)findViewById(R.id.txtasstype);
        mNATURE         =   (EditText)findViewById(R.id.txtnature);
        mMAKE           =   (EditText)findViewById(R.id.txtmake);
        mMODEL          =   (EditText)findViewById(R.id.txtmodel);
        mYEAR           =   (EditText)findViewById(R.id.txtyear);
        mREGNO          =   (EditText)findViewById(R.id.txtregno);
        mENFNO          =   (EditText)findViewById(R.id.txtengno);
        mCHASSNO        =   (EditText)findViewById(R.id.txtchno);
        mSUPP           =   (EditText)findViewById(R.id.txtsupp);
        mDELEAR         =   (EditText)findViewById(R.id.txtdelear);
        mINTDUSER       =   (EditText)findViewById(R.id.txtintduser);
        mINSURANCE      =   (EditText)findViewById(R.id.txtins);

        mMK_COMMENT     =   (EditText)findViewById(R.id.txtmecomment);
        mManagerRemarks =   (EditText)findViewById(R.id.txtmanagerremarks2);

        mBtnViewImage   =   (Button)findViewById(R.id.btnimageview);
        mBtnViewVrib    =   (Button)findViewById(R.id.btnviewcrib);
        BtnCallClient   = (ImageButton)findViewById(R.id.ImgCallBtn);

        btnApprovre     = (Button)findViewById(R.id.btnapprove);
        btnReject     = (Button)findViewById(R.id.btnreject);


        checkDataConnectionStatus = new CheckDataConnectionStatus(this);
        //===== Create Volly Request Ques. =======
        mQueue   = VollySingleton.getInstance(this).getRequestQueue();

        //=== Check Data Connection status Toolbra every 5 second.
        //setSupportActionBar(toolbar);
        ConnectionSts = (TextView)findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();

        mNIC.setEnabled(false);
        mFULLYNAME.setEnabled(false);
        mADDERS.setEnabled(false);
        mOCCUPATION.setEnabled(false);
        mPHONENO.setEnabled(false);


        mPRODUCT.setEnabled(false);
        mMKVAL.setEnabled(false);
        mINV_CAL.setEnabled(false);
        mCLIENT_CON.setEnabled(false);
        mFAC_AMT.setEnabled(false);
        mEXPRATE.setEnabled(false);
        mRATE.setEnabled(false);
        mPERIOD.setEnabled(false);
        mRENTAL.setEnabled(false);

        mASSTYPE.setEnabled(false);
        mNATURE.setEnabled(false);
        mMAKE.setEnabled(false);
        mMODEL.setEnabled(false);
        mYEAR.setEnabled(false);
        mREGNO.setEnabled(false);
        mENFNO.setEnabled(false);
        mCHASSNO.setEnabled(false);
        mSUPP.setEnabled(false);
        mDELEAR.setEnabled(false);
        mINTDUSER.setEnabled(false);
        mINSURANCE.setEnabled(false);

        progressDialog = new ProgressDialog(Po_Pdf_Viwer.this);
        progressDialog.setTitle("AFi Mobile");
        progressDialog.setMessage("Application Submit , please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);

        Intent intent = getIntent();
        mInpAppno    =   intent.getStringExtra("ApplicationNo");
        mSelAppno.setText(mInpAppno);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        PHP_URL_SQL =   globleClassDetails.getPHP_Path();

        //=== Load Phone Dial Paid to Phone to call client
        BtnCallClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData( Uri.parse("tel:" + mPHONENO.getText().toString()));
                startActivity(intent);
            }
        });

        //==== Manager Approve ==
        btnApprovre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                boolean ChkSts = checkDataConnectionStatus.IsConnected();
                if (ChkSts == true)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Po_Pdf_Viwer.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to Approve Application ?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ManagerAction("Approved");
                            DeleteRecord();
                            SendRemarks();

                            btnReject.setEnabled(false);
                            btnApprovre.setEnabled(false);

                            btnReject.setBackgroundResource(R.drawable.normalbuttondisable);
                            btnApprovre.setBackgroundResource(R.drawable.normalbuttondisable);

                        }
                    });
                    builderdelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked
                            Toast.makeText(getApplicationContext(),
                                    "No Button Clicked",Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builderdelete.create();
                    dialog.show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Po_Pdf_Viwer.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Data Connection Not available. Please Turn on Connection.");
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


        //===== Manager Reject   =========
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                boolean ChkSts = checkDataConnectionStatus.IsConnected();
                if (ChkSts == true)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Po_Pdf_Viwer.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to Reject Application ?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ManagerAction("Rejected");
                            DeleteRecord();
                            SendRemarks();
                            btnReject.setEnabled(false);
                            btnApprovre.setEnabled(false);

                            btnReject.setBackgroundResource(R.drawable.normalbuttondisable);
                            btnApprovre.setBackgroundResource(R.drawable.normalbuttondisable);

                        }
                    });
                    builderdelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked
                            Toast.makeText(getApplicationContext(),
                                    "No Button Clicked",Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builderdelete.create();
                    dialog.show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Po_Pdf_Viwer.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Data Connection Not available. Please Turn on Connection.");
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

        mBtnViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1_view_image = new Intent("android.intent.action.manager_doc_view");
                intent1_view_image.putExtra("ApplicationNo" , mSelAppno.getText());
                startActivity(intent1_view_image);

            }
        });

        GetData();
        ActivityCompat.requestPermissions(Po_Pdf_Viwer.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
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

    protected void onStart()
    {
        super.onStart();

        /*
        if (mSelAppno != null)
        {
            btnReject.setEnabled(true);
            btnApprovre.setEnabled(true);

            btnReject.setBackgroundResource(R.drawable.normalbutton);
            btnApprovre.setBackgroundResource(R.drawable.normalbutton);
        }
        else
        {
            btnReject.setEnabled(false);
            btnApprovre.setEnabled(false);

            btnReject.setBackgroundResource(R.drawable.normalbuttondisable);
            btnApprovre.setBackgroundResource(R.drawable.normalbuttondisable);
        }
        */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void onDestroy(){
        super.onDestroy();
        sqLiteDatabase_ManagerDocView.close();
        sqlliteCreateLeasing_ManagerDocView.close();
        Log.d("Log", "onDestroy-ManagerDocView");
    }

    public void SendRemarks()
    {
        JSONObject jsonObject_remarks = new JSONObject();
        try {
            jsonObject_remarks.put("APPLICATION_REF_NO" , mSelAppno.getText());
            jsonObject_remarks.put("USER_ID" , LoginUser);
            jsonObject_remarks.put("REQ_BRANCH" , LoginBranch);
            jsonObject_remarks.put("REMARKS" , mManagerRemarks.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String PhpUrl = PHP_URL_SQL + "Mobile-App-Remarks.php";
        JsonDataTranferToLive jsonDataTranferToLive = new JsonDataTranferToLive(this);
        jsonDataTranferToLive.SendDataToLive(PhpUrl , jsonObject_remarks);
    }

    public void DeleteRecord()
    {
        SQLiteDatabase sqLiteDatabase_delete_record = sqlliteCreateLeasing_ManagerDocView.getWritableDatabase();
        sqLiteDatabase_delete_record.delete("MANAGER_VIEW_IMAGE","APP_REF_NO =?",new String[] {mSelAppno.getText().toString()});
        sqLiteDatabase_delete_record.delete("PO_PENDING_DETAILS","APP_REF_NO =?",new String[] {mSelAppno.getText().toString()});
        sqLiteDatabase_delete_record.delete("PO_PENDING","APP_REF_NO =?",new String[] {mSelAppno.getText().toString()});
        sqLiteDatabase_delete_record.close();
    }


    public void ManagerAction (String MgrAction)
    {
        progressDialog.show();
        Cursor cursor_po_details = sqLiteDatabase_ManagerDocView.rawQuery("SELECT APP_REF_NO , CL_NIC , MKOFFICER , BRANCH_CODE FROM PO_PENDING WHERE APP_REF_NO = '" + mSelAppno.getText() + "'" , null);
        if (cursor_po_details.getCount() !=0)
        {
            cursor_po_details.moveToFirst();
            JSONObject jsonObject_client = new JSONObject();

            try {
                jsonObject_client.put("APPLICATION_REF_NO" , cursor_po_details.getString(0));
                jsonObject_client.put("CL_NIC" , cursor_po_details.getString(1));
                jsonObject_client.put("MK_CODE" , cursor_po_details.getString(2));
                jsonObject_client.put("REQ_BRANCH" , cursor_po_details.getString(3));
                jsonObject_client.put("MGR_CODE" , LoginUser);
                jsonObject_client.put("MGR_REMATKS" , mManagerRemarks.getText());
                jsonObject_client.put("MGR_STS" , MgrAction);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cursor_po_details.close();

            String url = PHP_URL_SQL + "Mobile-Mgr-Action.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject_client,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            String Resp="";

                            try {
                                Resp = response.getString("RESULT");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (Resp.equals("DONE"))
                            {
                                progressDialog.dismiss();

                                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Po_Pdf_Viwer.this);
                                bmyAlert.setMessage("File Action Successfully.").setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                })
                                        .setTitle("AFSL Mobile Leasing.")
                                        .create();
                                bmyAlert.show();
                            }
                            else
                            {
                                AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Po_Pdf_Viwer.this);
                                bmyAlert.setMessage("Please Try Again...").setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                })
                                        .setTitle("AFSL Mobile Leasing.")
                                        .create();
                                bmyAlert.show();
                            }



                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder bmyAlert = new AlertDialog.Builder(Po_Pdf_Viwer.this);
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

            mQueue.add(jsonObjectRequest);
        }
    }

    public void GetData()
    {
        //==== Client Data ====
        Cursor cursor_cldata = sqLiteDatabase_ManagerDocView.rawQuery("SELECT * FROM PO_PENDING_DETAILS WHERE APP_REF_NO = '" + mInpAppno + "'" , null);
        if (cursor_cldata.getCount() != 0)
        {
            cursor_cldata.moveToFirst();

            mNIC.setText(cursor_cldata.getString(32));
            mFULLYNAME.setText(cursor_cldata.getString(7));
            mADDERS.setText(cursor_cldata.getString(8) + "," + cursor_cldata.getString(9) + "," + cursor_cldata.getString(10) + "," + cursor_cldata.getString(11) );
            mOCCUPATION.setText(cursor_cldata.getString(13));
            mPHONENO.setText(cursor_cldata.getString(12));


            mPRODUCT.setText(cursor_cldata.getString(24));
            mMKVAL.setText(cursor_cldata.getString(20));
            mINV_CAL.setText(cursor_cldata.getString(25));
            mCLIENT_CON.setText(cursor_cldata.getString(5));
            mFAC_AMT.setText(cursor_cldata.getString(1));
            mEXPRATE.setText(cursor_cldata.getString(23));
            mRATE.setText(cursor_cldata.getString(3));
            mPERIOD.setText(cursor_cldata.getString(4));
            mRENTAL.setText(cursor_cldata.getString(2));

            mASSTYPE.setText(cursor_cldata.getString(14));
            mNATURE.setText(cursor_cldata.getString(26));
            mMAKE.setText(cursor_cldata.getString(15));
            mMODEL.setText(cursor_cldata.getString(16));
            mYEAR.setText(cursor_cldata.getString(19));
            mREGNO.setText(cursor_cldata.getString(31));
            mENFNO.setText(cursor_cldata.getString(18));
            mCHASSNO.setText(cursor_cldata.getString(17));
            mSUPP.setText(cursor_cldata.getString(27));
            mDELEAR.setText(cursor_cldata.getString(28));
            mINTDUSER.setText(cursor_cldata.getString(29));
            mINSURANCE.setText(cursor_cldata.getString(30));
        }
    }

    public void PfdCreate()
    {
        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "mypdffile.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);
            doc.open();


            //==== Create Document Setting Varibale
            Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

            //==== Write Word  ====

            /*
            Paragraph p1 = new Paragraph("PO Summery Details");

            Font paraFont= new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);
            */



            Paragraph prPersinalInfo = new Paragraph();
            prPersinalInfo.setFont(smallBold);
            prPersinalInfo.add("Address 1\n");
            prPersinalInfo.add("Address 2\n");
            prPersinalInfo.add("City: SanFran.  State: CA\n");
            prPersinalInfo.add("City: SanFran.  State: CA\n");
            prPersinalInfo.setAlignment(Element.ALIGN_CENTER);
            doc.add(prPersinalInfo);


            //add paragraph to document
            //doc.add(p1);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }

        viewPdf("mypdffile.pdf", "PDF");


    }

    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        pdfView_doc.fromUri(path)
                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null).load();


        /*

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
        */
    }





}
