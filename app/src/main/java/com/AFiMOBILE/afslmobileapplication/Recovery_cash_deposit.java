package com.AFiMOBILE.afslmobileapplication;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Recovery_cash_deposit extends AppCompatActivity {

    public SqlliteCreateRecovery sqlliteCreateRecovery_cash_depsit;
    public String PHP_URL_SQL , LoginUser , LoginDate , LoginBranch;
    public RecyclerView mRecycleviwer;
    public Adapter_Receipt_View mAdapter;
    public EditText mTotReceiptAmt , mDepositamt , mDepositBank;
    public Button mBtnAttach , mnBtnupdteDeposit , mBtnMarkDeposit;
    public ImageView mIMGBROWSE;
    public RelativeLayout mRelyDeposit;
    public double mTotReceipt=0.00;
    final int REQUEST_CODE_GALLERY = 999;
    public View layout_sucress , layout_error;
    public TextView tv , tv_sucess;
    public String RecptpSetial="" , RcptRefno="";


    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recovery_cash_deposit);


        sqlliteCreateRecovery_cash_depsit = new SqlliteCreateRecovery(this);
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();


        //=== Assign Varible
        mTotReceiptAmt      =   (EditText)findViewById(R.id.TxttotReciptAmt);
        mDepositamt         =   (EditText)findViewById(R.id.Txttotdeposit);
        mDepositBank        =   (EditText)findViewById(R.id.TxtDepositBank);
        mBtnAttach          =   (Button)findViewById(R.id.btnadddepositslip);
        mnBtnupdteDeposit   =   (Button)findViewById(R.id.btnDepositCash);
        mIMGBROWSE        =   (ImageView)findViewById(R.id.imgpicture);
        mRelyDeposit        =   (RelativeLayout)findViewById(R.id.RelativeDeposit);

        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);

        //=== Recycviwe load the data
        mRecycleviwer   =   (RecyclerView)findViewById(R.id.ReyFacList);
        mRecycleviwer.setHasFixedSize(true);
        mRecycleviwer.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_Receipt_View(this , LoadReceiptData());
        mRecycleviwer.setAdapter(mAdapter);

        //==== Disable Button
        mTotReceiptAmt.setText(""); mDepositamt.setText(""); mDepositBank.setText("");
        mTotReceiptAmt.setEnabled(false);
        mDepositamt.setEnabled(false);
        mDepositBank.setEnabled(false);
        mBtnAttach.setEnabled(false);
        mnBtnupdteDeposit.setEnabled(false);

        mBtnAttach.setBackgroundResource(R.drawable.recoverybutdisable);
        mnBtnupdteDeposit.setBackgroundResource(R.drawable.recoverybutdisable);

        LoadDepositData();


        //==== Save the Deposit Data
        mnBtnupdteDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });

        mBtnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        Recovery_cash_deposit.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });
    }


    public void SaveData()
    {
        if (mDepositamt.getText().toString().equals(""))
        {
            tv.setText("<Total Deposit Amount> is blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mDepositBank.getText().toString().equals(""))
        {
            tv.setText("<Deposit Bank> is blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            AlertDialog.Builder builderdelete = new AlertDialog.Builder(Recovery_cash_deposit.this);
            builderdelete.setTitle("AFiMobile-Leasing");
            builderdelete.setMessage("Are you sure to deposit this receipts ?");
            builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //===== Get Receipt Serial
                    SimpleDateFormat curdate = new SimpleDateFormat("yyyy-mm-dd");
                    String NewDate = curdate.format(new Date());
                    Calendar now = Calendar.getInstance();

                    RecptpSetial = "0";
                    SQLiteDatabase sqLiteDatabase_getSequence = sqlliteCreateRecovery_cash_depsit.getWritableDatabase();
                    Cursor cursor_getSequence = sqLiteDatabase_getSequence.rawQuery("SELECT seq_count FROM masr_sequence WHERE seq_code = 'DEPO'" , null);
                    if (cursor_getSequence.getCount() != 0)
                    {
                        cursor_getSequence.moveToFirst();
                        RecptpSetial = cursor_getSequence.getString(0);
                    }

                    RcptRefno="";
                    int mIntRefno;
                    if (RecptpSetial.toString().equals("0"))
                    {
                        RcptRefno = LoginBranch + LoginUser + "D" + now.get(Calendar.MONTH)  + "_" + "00001";
                        mIntRefno=2;
                    }
                    else
                    {
                        RcptRefno = LoginBranch + LoginUser + "D" + now.get(Calendar.MONTH) +  "_" + RecptpSetial;
                        mIntRefno = Integer.parseInt(RecptpSetial) + 1;
                    }

                    ContentValues contentValues_inputseriam = new ContentValues();
                    contentValues_inputseriam.put("seq_count" , String.valueOf(mIntRefno));
                    sqLiteDatabase_getSequence.update("masr_sequence" ,contentValues_inputseriam , "seq_code = ?" , new String[]{"DEPO"});
                    sqLiteDatabase_getSequence.close();

                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                    //==== Create Table
                    SQLiteDatabase sqLiteDatabase_dateInseet = sqlliteCreateRecovery_cash_depsit.getWritableDatabase();
                    ContentValues contentValues_deposit = new ContentValues();
                    contentValues_deposit.put("dep_refno" , RcptRefno);
                    contentValues_deposit.put("dep_date" , LoginDate);
                    contentValues_deposit.put("dep_time" , mydate);
                    contentValues_deposit.put("dep_amt" , mDepositamt.getText().toString());
                    contentValues_deposit.put("dep_bank" , mDepositBank.getText().toString());
                    contentValues_deposit.put("dep_sts" , "D");
                    contentValues_deposit.put("dep_user" , LoginUser);
                    contentValues_deposit.put("dep_user_br_code" , LoginBranch);
                    contentValues_deposit.put("Live_server_update" , "");
                    sqLiteDatabase_dateInseet.insert("recovery_cash_deposit" , null , contentValues_deposit);

                    //==== Receipt Details Create
                    Cursor cursor_get_receipt = sqLiteDatabase_dateInseet.rawQuery("SELECT rcpt_refno,rcpt_date,rcpt_amt FROM recovery_recipt WHERE rcpt_user_id = '" + LoginUser + "' AND dep_sts = ''" , null);
                    if (cursor_get_receipt.getCount() != 0)
                    {
                        cursor_get_receipt.moveToFirst();

                        do {
                            ContentValues contentValues_receipt_details = new ContentValues();
                            contentValues_receipt_details.put("dep_refno" , RcptRefno);
                            contentValues_receipt_details.put("receipt_entry_no" , cursor_get_receipt.getString(0));
                            contentValues_receipt_details.put("receipt_date" , cursor_get_receipt.getString(1));
                            contentValues_receipt_details.put("receipt_amt" , cursor_get_receipt.getString(2));
                            contentValues_receipt_details.put("dep_user" , LoginUser);
                            contentValues_receipt_details.put("br_code" , LoginBranch);
                            contentValues_receipt_details.put("Live_server_update" , "");
                            sqLiteDatabase_dateInseet.insert("recovery_deposit_details" , null , contentValues_receipt_details);

                            //==== Update Receipt Flag
                            ContentValues contentValues_update_receipt = new ContentValues();
                            contentValues_update_receipt.put("dep_sts" , "D");
                            sqLiteDatabase_dateInseet.update("recovery_recipt " , contentValues_update_receipt , "rcpt_refno = ?" , new String[]{cursor_get_receipt.getString(0)});

                        }while (cursor_get_receipt.moveToNext());
                    }


                    //=== Doument Uplaod UploadImage
                    UploadImage();
                    mTotReceiptAmt.setText(""); mDepositamt.setText(""); mDepositBank.setText("");
                    mTotReceiptAmt.setEnabled(false);
                    mDepositamt.setEnabled(false);
                    mDepositBank.setEnabled(false);
                    mBtnAttach.setEnabled(false);
                    mnBtnupdteDeposit.setEnabled(false);

                    mBtnAttach.setBackgroundResource(R.drawable.recoverybutdisable);
                    mnBtnupdteDeposit.setBackgroundResource(R.drawable.recoverybutdisable);

                    mDepositamt.setText("");   mDepositBank.setText("");
                    tv_sucess.setText("Record Successfully Saved.");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout_sucress);
                    toast.show();

                    mAdapter.swapCursor(LoadReceiptData());


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
    }

    public void UploadImage()
    {
        SQLiteDatabase sqLiteDatabase_DATAUPLOAD = sqlliteCreateRecovery_cash_depsit.getWritableDatabase();
        ContentValues contentValues_upload_data = new ContentValues();
        contentValues_upload_data.put("referance_no" , RcptRefno);
        contentValues_upload_data.put("facility_no" , "");
        contentValues_upload_data.put("doc_type" , "DEPOSIT SLIP");
        contentValues_upload_data.put("doc_image" , imageViewByte(mIMGBROWSE));
        contentValues_upload_data.put("doc_date" , LoginDate);
        contentValues_upload_data.put("doc_useruid" , LoginUser);
        contentValues_upload_data.put("Live_Server_Update" , "");
        sqLiteDatabase_DATAUPLOAD.insert("recoveery_doc_uplaod" , null , contentValues_upload_data);
    }

    public void LoadDepositData()
    {
        //=== Get Totla Receipt Details

        SQLiteDatabase sqLiteDatabase_receipt = sqlliteCreateRecovery_cash_depsit.getReadableDatabase();
        Cursor cursor_get_receipt = sqLiteDatabase_receipt.rawQuery("SELECT rcpt_amt FROM recovery_recipt WHERE rcpt_user_id = '" + LoginUser + "' AND dep_sts = ''" , null);
        if (cursor_get_receipt.getCount() != 0)
        {
            cursor_get_receipt.moveToFirst();
            do {
                mTotReceipt = mTotReceipt + (Float.parseFloat(cursor_get_receipt.getString(0).replace("," , "")));
            }while (cursor_get_receipt.moveToNext());
        }

        mBtnMarkDeposit     =   (Button)findViewById(R.id.btnmarkDeposit);
        mBtnMarkDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDepositamt.setEnabled(true);
                mDepositBank.setEnabled(true);
                mBtnAttach.setEnabled(true);
                mnBtnupdteDeposit.setEnabled(true);
                mTotReceiptAmt.setText(String.valueOf(mTotReceipt));

                mBtnAttach.setBackgroundResource(R.drawable.recoverybutton);
                mnBtnupdteDeposit.setBackgroundResource(R.drawable.recoverybutton);

            }
        });
    }

    protected void onStart()
    {
        super.onStart();
        //btnModifyData.setBackgroundResource(R.drawable.normalbuttondisable);
    }

    public Cursor LoadReceiptData()
    {
        SQLiteDatabase sqLiteDatabase_receipt = sqlliteCreateRecovery_cash_depsit.getReadableDatabase();
        Cursor cursor_get_receipt = sqLiteDatabase_receipt.rawQuery("SELECT rcpt_date,rcpt_refno,rcpt_amt FROM recovery_recipt WHERE rcpt_user_id = '" + LoginUser + "' AND dep_sts = ''" , null);
        return cursor_get_receipt;
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

    protected void onDestroy(){

        sqlliteCreateRecovery_cash_depsit.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

}
