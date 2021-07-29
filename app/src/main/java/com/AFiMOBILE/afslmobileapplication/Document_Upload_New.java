package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Document_Upload_New extends AppCompatActivity {


    private Spinner mReferance;
    private EditText mRemarks;
    private Button mBtnBrowse , mBtnCapture , mBtnuplaod;
    private SqlliteCreateLeasing sqlliteCreateLeasing_imageUpload;
    private TextView mApplicationno;
    private String mInpAppno , mApStage , LoginUser ,LoginDate , LoginBranch ;
    private CheckDataConnectionStatus checkDataConnectionStatus;
    private RecyclerView Rec_doc_type;
    public Adapter_doc_image_new mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_uplaod_new);


        mReferance = (Spinner)findViewById(R.id.spnDOCREFERANCE);
        mRemarks = (EditText)findViewById(R.id.txtmeremarks);

        mBtnBrowse  = (Button)findViewById(R.id.btnBROWSE);
        mBtnCapture = (Button)findViewById(R.id.btnTAKEIMAGE);
        mBtnuplaod  = (Button)findViewById(R.id.btnUPLOAD);
        mApplicationno = (TextView)findViewById(R.id.txtAPPNO) ;
        Rec_doc_type = (RecyclerView)findViewById(R.id.Rec_doc_type) ;

        //==== Get Goloble Varible Details
        //=== Globle Varible Details..
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        checkDataConnectionStatus = new CheckDataConnectionStatus(this);

        sqlliteCreateLeasing_imageUpload = new SqlliteCreateLeasing(this);

        Intent intent = getIntent();
        mInpAppno = intent.getStringExtra("ApplicationNo");
        mApStage  = intent.getStringExtra("AppStage");
        mApplicationno.setText(mInpAppno);

        Log.e("appno" , mApplicationno.getText().toString());

        //==== Load Referance Data
        List<String> labels = sqlliteCreateLeasing_imageUpload.SendDocAppRef(mInpAppno);
        ArrayAdapter<String> arrayAdapterDOCREF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        arrayAdapterDOCREF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mReferance.setAdapter(arrayAdapterDOCREF);

        mReferance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String mHolder_Type = mReferance.getSelectedItem().toString();
                mHolder_Type = mHolder_Type.substring(mHolder_Type.lastIndexOf("-") +1);

                Log.e("type" , mHolder_Type );

                if (mHolder_Type.equals("APPLICANT"))
                {
                    Rec_doc_type.setHasFixedSize(true);
                    Rec_doc_type.setLayoutManager(new LinearLayoutManager(Document_Upload_New.this));
                    mAdapter = new Adapter_doc_image_new(Document_Upload_New.this , PendingdocList("CLI"));
                    Rec_doc_type.setAdapter(mAdapter);
                }
                else
                {
                    Rec_doc_type.setHasFixedSize(true);
                    Rec_doc_type.setLayoutManager(new LinearLayoutManager(Document_Upload_New.this));
                    mAdapter = new Adapter_doc_image_new(Document_Upload_New.this , PendingdocList("GUR"));
                    Rec_doc_type.setAdapter(mAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Cursor PendingdocList (String Cl_Type)
    {
        Log.e("type-1" , Cl_Type);
        String ProductCode="";
        String EqCagery="";
        SQLiteDatabase sqLiteDatabase_doc_type = sqlliteCreateLeasing_imageUpload.getReadableDatabase();
        Cursor cursor_doc_procde = sqLiteDatabase_doc_type.rawQuery("SELECT LE_APPLICATION.AP_PRODUCT , LE_ASSET_DETAILS.AS_EQ_CATAGE FROM LE_APPLICATION " +
                "INNER JOIN LE_ASSET_DETAILS ON LE_ASSET_DETAILS.APPLICATION_REF_NO = LE_APPLICATION.APPLICATION_REF_NO WHERE LE_APPLICATION.APPLICATION_REF_NO = '" + mInpAppno + "'" , null);
            cursor_doc_procde.moveToFirst();
            ProductCode = cursor_doc_procde.getString(0);
            EqCagery    = cursor_doc_procde.getString(1);

        Cursor cursor_doc_type = sqLiteDatabase_doc_type.rawQuery("SELECT DOC_NAME , EQ_TYPE FROM AP_MAST_DOC_TYPE WHERE PRODUCT_CODE = '" + ProductCode + "' AND " +
                "EQ_TYPE = '" + EqCagery + "' and MAN_STAGE = '" + mApStage + "' and HOLDER_TYPE = '" + Cl_Type + "'" , null);
        cursor_doc_type.moveToFirst();
        return cursor_doc_type;
    }

}
