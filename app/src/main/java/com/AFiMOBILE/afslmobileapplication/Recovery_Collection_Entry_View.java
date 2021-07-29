package com.AFiMOBILE.afslmobileapplication;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Recovery_Collection_Entry_View  extends AppCompatActivity {

    public SqlliteCreateRecovery sqlliteCreateRecovery_collectionentry;
    public ArrayAdapter<String> arrayPaymentMode , mPaidThriParty , mArrayDuplicate;
    public Spinner mPaymentMode , mSpnPayThridParty , mDupReceipt;
    public Button mLockFacility , mSubmitFacility , mBtnAttachReceipt;
    public String mInputFacno , PHP_URL_SQL , LoginUser , LoginDate , LoginBranch , RecptpSetial , RcptRefno;
    public TextView mColDate , mFacno , tv , tv_sucess ;
    public EditText mColamt  ,mManualRcptno , mPrvRcptno , mComment;
    public double current_lattitude ,  current_longitude;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public View layout_sucress , layout_error;
    public int mYear , mMonth , mDay;
    public DatePickerDialog datePickerDialog , dialog;
    final int REQUEST_CODE_GALLERY = 999;
    public ImageView mIMGBROWSE;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_collection_entry_view);

        sqlliteCreateRecovery_collectionentry = new SqlliteCreateRecovery(this);
        //=== Get Globle PHP Code
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();

        Intent intent = getIntent();
        mInputFacno     =   intent.getStringExtra("FACNO");
        mFacno          =   (TextView)findViewById(R.id.TxtAssingFacno);
        mColamt         =   (EditText)findViewById(R.id.TxtcolAmt) ;
        mManualRcptno   =   (EditText)findViewById(R.id.TxtManualRept);
        mPrvRcptno      =   (EditText)findViewById(R.id.TxtProReceipt);
        mComment        =   (EditText)findViewById(R.id.TxtComment) ;

        mColDate        =   (TextView)findViewById(R.id.TxtcolDate);
        mBtnAttachReceipt = (Button)findViewById(R.id.btnAttachReceipt);
        mIMGBROWSE      =   (ImageView) findViewById(R.id.imgpicture);

        //=== Get Location access
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //=== Load Date Selection
        //====== Get Date Selection ======================
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(Recovery_Collection_Entry_View.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mColDate.setText(day + "-" + month + "-" + year);
            }
        }, mYear, mMonth, mDay);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mColDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });


        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);


        mFacno.setText("Facility No - " + mInputFacno);

        mManualRcptno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mPrvRcptno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mComment.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        //=== Load Master Data
        mSpnPayThridParty  = (Spinner)findViewById(R.id.spnPaymentReceied);
        List<String> listpaid = new ArrayList<String>();
        listpaid.add("Yes");
        listpaid.add("No");
        if (listpaid != null)
        {
            mPaidThriParty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listpaid);
            mPaidThriParty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnPayThridParty.setAdapter(mPaidThriParty);
        }

        mPaymentMode = (Spinner)findViewById(R.id.spnPaymentMode) ;
        List<String> labelsVehilce = sqlliteCreateRecovery_collectionentry.GetPaymentMode();
        labelsVehilce.add("");
        if (labelsVehilce != null) {
            arrayPaymentMode = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsVehilce);
            arrayPaymentMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mPaymentMode.setAdapter(arrayPaymentMode);
            mPaymentMode.setSelection(arrayPaymentMode.getPosition(""));
        }

        //=== Dublicate REceipt
        mDupReceipt  = (Spinner)findViewById(R.id.spnDuplicateReceipt);
        List<String> lisdup = new ArrayList<String>();
        lisdup.add("Yes");
        lisdup.add("No");
        if (lisdup != null)
        {
            mArrayDuplicate = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lisdup);
            mArrayDuplicate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDupReceipt.setAdapter(mArrayDuplicate);
        }

                //=== Locak Facilit
        mLockFacility = (Button)findViewById(R.id.btnLock);
        mLockFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LockFacility();
            }
        });

        checkDataConnectionStatus = new CheckDataConnectionStatus(this);
        mSubmitFacility = (Button)findViewById(R.id.btnsubmit);
        mSubmitFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (checkDataConnectionStatus.IsConnected() == true)
                {
                    AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_Collection_Entry_View.this);
                    builderdelete.setTitle("AFiMobile-Leasing");
                    builderdelete.setMessage("Are you sure to update this record?");
                    builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SubmitFacility();
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Collection_Entry_View.this);
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


        //==== Attach Receipt Data
        mBtnAttachReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        Recovery_Collection_Entry_View.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

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
                mIMGBROWSE.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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


    public void SubmitFacility()
    {
        LockFacility();
        Volly_Request_Recovery_Data volly_request_recovery_data = new Volly_Request_Recovery_Data(this);
        volly_request_recovery_data.PostFacilityRecoveryActionData(mInputFacno);
        mSubmitFacility.setEnabled(false);
        mSubmitFacility.setBackgroundResource(R.drawable.normalbuttondisable);

        tv_sucess.setText("Record Successfully Submitted.");
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout_sucress);
        toast.show();

        mLockFacility.setEnabled(false);
        mLockFacility.setBackgroundResource(R.drawable.normalbuttondisable);
    }

    public void GetLo()
    {
        GPSTracker gps = new GPSTracker(this);
        int status = 0;
        if(gps.canGetLocation())

        {
           // status = GooglePlayServicesUtil
             //       .isGooglePlayServicesAvailable(getApplicationContext());

            status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Recovery_Collection_Entry_View.this);

            if (status == ConnectionResult.SUCCESS) {
                current_lattitude = gps.getLatitude();
                current_longitude = gps.getLongitude();
                Log.d("dashlatlongon", "" + current_lattitude + "-"
                        + current_longitude);

                if (current_lattitude == 0.0 && current_longitude == 0.0) {
                    current_lattitude = 22.22;
                    current_longitude = 22.22;

                }

            } else {
                current_lattitude = 22.22;
                current_longitude = 22.22;
            }

        }
        else
        {
            gps.showSettingsAlert();
        }
    }

    public void UpdateRecipt()
    {
        //===== Get Receipt Serial
        SimpleDateFormat curdate = new SimpleDateFormat("yyyy-mm-dd");
        String NewDate = curdate.format(new Date());
        Calendar now = Calendar.getInstance();

        RecptpSetial = "0";
        SQLiteDatabase sqLiteDatabase_getSequence = sqlliteCreateRecovery_collectionentry.getWritableDatabase();
        Cursor cursor_getSequence = sqLiteDatabase_getSequence.rawQuery("SELECT seq_count FROM masr_sequence WHERE seq_code = 'RCPT'" , null);
        if (cursor_getSequence.getCount() != 0)
        {
            cursor_getSequence.moveToFirst();
            RecptpSetial = cursor_getSequence.getString(0);
        }

        RcptRefno="";
        int mIntRefno;
        if (RecptpSetial.toString().equals("0"))
        {
            RcptRefno = LoginBranch + LoginUser + "R" + now.get(Calendar.MONTH) + mInputFacno.substring(10) + "_" + "00001";
            mIntRefno=2;
        }
        else
        {
            RcptRefno = LoginBranch + LoginUser + "R" + now.get(Calendar.MONTH) + mInputFacno.substring(10) + "_" + RecptpSetial;
            mIntRefno = Integer.parseInt(RecptpSetial) + 1;
        }

        ContentValues contentValues_inputseriam = new ContentValues();
        contentValues_inputseriam.put("seq_count" , String.valueOf(mIntRefno));
        sqLiteDatabase_getSequence.update("masr_sequence" ,contentValues_inputseriam , "seq_code = ?" , new String[]{"RCPT"});
        sqLiteDatabase_getSequence.close();
        //==============================================

        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());



        SQLiteDatabase sqLiteDatabase_receipt = sqlliteCreateRecovery_collectionentry.getWritableDatabase();
        ContentValues contentValues_recipt = new ContentValues();
        contentValues_recipt.put("rcpt_refno" , RcptRefno);
        contentValues_recipt.put("rcpt_date" , LoginDate);
        contentValues_recipt.put("rcpt_amt" , mColamt.getText().toString());
        contentValues_recipt.put("rcpt_time" , mydate);
        contentValues_recipt.put("rcpt_pay_mode" , mPaymentMode.getSelectedItem().toString());
        contentValues_recipt.put("rcpt_facno" , mInputFacno);
        contentValues_recipt.put("rcpt_branch_code" , LoginBranch);
        contentValues_recipt.put("rcpt_user_id" , LoginUser);
        contentValues_recipt.put("rcpt_des" , mComment.getText().toString());
        contentValues_recipt.put("manual_rcptno" , mManualRcptno.getText().toString());
        contentValues_recipt.put("rcpt_bank_code" , "");
        contentValues_recipt.put("Live_server_update" , "");
        contentValues_recipt.put("dep_sts" , "");
        sqLiteDatabase_receipt.insert("recovery_recipt" , null ,contentValues_recipt);
    }

    public void UploadImage()
    {
        SQLiteDatabase sqLiteDatabase_DATAUPLOAD = sqlliteCreateRecovery_collectionentry.getWritableDatabase();
        ContentValues contentValues_upload_data = new ContentValues();
        contentValues_upload_data.put("referance_no" , RcptRefno);
        contentValues_upload_data.put("facility_no" , mInputFacno);
        contentValues_upload_data.put("doc_type" , "RECEIPT");
        contentValues_upload_data.put("doc_image" , imageViewByte(mIMGBROWSE));
        contentValues_upload_data.put("doc_date" , LoginDate);
        contentValues_upload_data.put("doc_useruid" , LoginUser);
        contentValues_upload_data.put("Live_Server_Update" , "");
        sqLiteDatabase_DATAUPLOAD.insert("recoveery_doc_uplaod" , null , contentValues_upload_data);
    }

    public void LockFacility()
    {
        if (mPaymentMode.getSelectedItem().toString().equals(""))
        {
            tv.setText("<Payment Mode> is blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            //=== To Lock the Record;
            GetLo(); //== Get GEo Locastion

            Log.e("current_lattitude" , String.valueOf(current_lattitude));
            Log.e("current_longitude" , String.valueOf(current_longitude));


            String mClientName="" , mClAdders="";
            SQLiteDatabase sqLiteDatabase_Get_Details = sqlliteCreateRecovery_collectionentry.getReadableDatabase();
            Cursor cursor_get_vehicle = sqLiteDatabase_Get_Details.rawQuery("SELECT Asset_Model ,Vehicle_Number ,Customer_Full_Name , C_Address_Line_1 , C_Address_Line_2 , C_Address_Line_3 , C_Address_Line_4 FROM recovery_generaldetail WHERE Facility_Number = '" + mInputFacno + "'" , null);
            if (cursor_get_vehicle.getCount() != 0)
            {
                cursor_get_vehicle.moveToFirst();
                mClientName = cursor_get_vehicle.getString(2);
                mClAdders = cursor_get_vehicle.getString(3) + "," + cursor_get_vehicle.getString(4) + "," + cursor_get_vehicle.getString(5) + "," + cursor_get_vehicle.getString(6);
            }
            sqLiteDatabase_Get_Details.close();
            cursor_get_vehicle.close();

            if (String.valueOf(current_lattitude).equals("22.22"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Recovery_Collection_Entry_View.this);
                builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                builder.setMessage("GEO Tag not update. Please try again.. ");
                builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
            }
            else
            {

                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String dateToStr = format.format(today);
                Log.e("date" , dateToStr);

                SQLiteDatabase sqLiteDatabase_DataUpdate = sqlliteCreateRecovery_collectionentry.getWritableDatabase();
                ContentValues contentValues_DataUpdate = new ContentValues();
                contentValues_DataUpdate.put("FACILITY_NO" , mInputFacno);
                contentValues_DataUpdate.put("ACTIONCODE" , "Collection Entry");
                contentValues_DataUpdate.put("ACTION_DATE" , dateToStr);
                contentValues_DataUpdate.put("MADE_BY" , LoginUser);
                contentValues_DataUpdate.put("COLLECTION_ENTRY_TYPE" , "Normal");
                contentValues_DataUpdate.put("MODE_OF_PAYMENT" , mPaymentMode.getSelectedItem().toString());
                contentValues_DataUpdate.put("COLLECTION_ENTRY_TYPE" , "Normal");
                contentValues_DataUpdate.put("COLLECTION_AMOUNT" , mColamt.getText().toString());
                contentValues_DataUpdate.put("COLLECTION_ENTRY_DATE" , mColDate.getText().toString());
                contentValues_DataUpdate.put("MANUAL_RECEIPT_NO" , mManualRcptno.getText().toString());
                contentValues_DataUpdate.put("PROVISIONAL_RECEIPT_NO" , mPrvRcptno.getText().toString());
                contentValues_DataUpdate.put("PAYMENT_RECEIVED_FROM_THIRD_PARTY" , mSpnPayThridParty.getSelectedItem().toString());
                contentValues_DataUpdate.put("NAME" , mClientName);
                contentValues_DataUpdate.put("ADDRESS" , mClAdders);
                contentValues_DataUpdate.put("COMMENTS" , mComment.getText().toString());
                contentValues_DataUpdate.put("UPDATE_TIME" , LoginDate);
                contentValues_DataUpdate.put("GEO_TAG_LAT" , String.valueOf(current_lattitude));
                contentValues_DataUpdate.put("GEO_TAG_LONG" , String.valueOf(current_longitude));
                contentValues_DataUpdate.put("FAC_LOCK" , "Y");
                contentValues_DataUpdate.put("LIVE_SERVER_UPDATE" , "");
                sqLiteDatabase_DataUpdate.insert("nemf_form_updater" , null , contentValues_DataUpdate)  ;

                //=== Update Recipt Table
                UpdateRecipt();

                //===  Document Upload - Comment by bhagya - not uplaod path in server side
                //UploadImage();

                Toast.makeText(getApplicationContext(), "Record Successfully Saved.", Toast.LENGTH_SHORT).show();
                mColamt.setText("");  mColDate.setText(""); mManualRcptno.setText(""); mPrvRcptno.setText(""); mComment.setText("");

                mSubmitFacility.setEnabled(false);
                mSubmitFacility.setBackgroundResource(R.drawable.normalbuttondisable);

                tv_sucess.setText("Record Successfully Saved.");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout_sucress);
                toast.show();

                mLockFacility.setEnabled(false);
                mLockFacility.setBackgroundResource(R.drawable.normalbuttondisable);
            }
        }
    }

    protected void onDestroy(){
        sqlliteCreateRecovery_collectionentry.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        super.onDestroy();
    }

}
