package com.AFiMOBILE.afslmobileapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.itextpdf.text.pdf.PRIndirectReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Rmv_Facility_Update_View extends AppCompatActivity {

    private String PHP_URL_SQL , LoginUser , LoginDate, LoginBranch , LoginUserName , Inp_Regno , Inp_chass_no;
    private SqlliteCreateLeasing sqlliteCreateLeasing_fac_update_rmv;
    private TextView mbrcode , mVehno , mEngno , mChassino , mMeofficer , mPodate ,tv , tv_sucess;;
    private EditText mPidno , mRemarks;
    private Spinner mchecksts;
    private ArrayAdapter<String> arrayTITLE;
    public Button mUploadreceipt , mSave , muplaodserver;
    final int REQUEST_CODE_GALLERY = 999;
    public File SelectImageFile;
    private ImageView mIMGBROWSE;
    private View layout_sucress , layout_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rmv_facility_update_view);

        //=== Get Input Application No
        Intent intent = getIntent();
        Inp_Regno    =   intent.getStringExtra("REG_NO");
        Inp_chass_no     =   intent.getStringExtra("CHS_NO");

        //=== Create Toast Massage
        LayoutInflater inflater = getLayoutInflater();
        layout_error = inflater.inflate(R.layout.error_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv = (TextView) layout_error.findViewById(R.id.txtvw);

        LayoutInflater inflater_Sucess = getLayoutInflater();
        layout_sucress= inflater.inflate(R.layout.succes_toast,(ViewGroup) findViewById(R.id.custom_toast_container));
        tv_sucess = (TextView) layout_sucress.findViewById(R.id.txtvw);

        //=== Create Varible
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        PHP_URL_SQL = globleClassDetails.getPHP_Path();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName   = globleClassDetails.getOfficerName();

        sqlliteCreateLeasing_fac_update_rmv = new SqlliteCreateLeasing(this);

        //==== Assign varible
        mVehno      =   (TextView)findViewById(R.id.txtregno);
        mEngno      =   (TextView)findViewById(R.id.txtengno);
        mChassino   =   (TextView)findViewById(R.id.txtchassi);
        mMeofficer  =   (TextView)findViewById(R.id.txtmename);
        mPodate     =   (TextView)findViewById(R.id.txtpodate);
        mPidno      =   (EditText)findViewById(R.id.txtPIDNO);
        mbrcode     =   (TextView)findViewById(R.id.txtbrcode);
        mRemarks    =   (EditText)findViewById(R.id.txtremarks);
        mchecksts   =   (Spinner)findViewById(R.id.txtsts) ;
        mUploadreceipt  =   (Button)findViewById(R.id.btnBROWSE) ;
        mIMGBROWSE  =   (ImageView)findViewById(R.id.imgpicture) ;
        mSave       =   (Button)findViewById(R.id.btnsave) ;
        muplaodserver = (Button)findViewById(R.id.btnupload) ;

        //=== Load select data
        List<String> listTITLE = new ArrayList<String>();
        listTITLE.add("Ownership Conformation");
        listTITLE.add("Final CR Transfer payment");
        listTITLE.add("Ownership Not Conformation");
        arrayTITLE = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listTITLE);
        arrayTITLE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mchecksts.setAdapter(arrayTITLE);

        Getsummerydata();

        //==== Upload receipt
        mUploadreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        Rmv_Facility_Update_View.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

        //===Save Record
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });

        //==== Upload server
        muplaodserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uploaddata();
            }
        });

    }

    private void Uploaddata()
    {
        JSONArray jsonArray_RmvData        =   new JSONArray();
        JSONObject jsonObject_Final_rmv     =   new JSONObject();
        SQLiteDatabase sqLiteDatabase_uploadserver = sqlliteCreateLeasing_fac_update_rmv.getReadableDatabase();
        Cursor cursor_upload_serve = sqLiteDatabase_uploadserver.rawQuery("SELECT BRANCH_CODE,CHASSI_NO,ENGINE_NO,REG_NO,PIDNO,VARIFI_STS,REMARKS,VERIF_DATE,VERIFY_TIME," +
                "VERIFY_USER,PO_DATE,ME_CODE FROM RMV_DATA WHERE REG_NO = '" + mVehno.getText().toString() + "'" , null );
        if (cursor_upload_serve.getCount() != 0)
        {
            cursor_upload_serve.moveToFirst();
            do {
                JSONObject ApplicationDataJson = new JSONObject();
                try {
                    ApplicationDataJson.put("BRANCH_CODE" , cursor_upload_serve.getString(0));
                    ApplicationDataJson.put("CHASSI_NO" , cursor_upload_serve.getString(1));
                    ApplicationDataJson.put("ENGINE_NO" , cursor_upload_serve.getString(2));
                    ApplicationDataJson.put("REG_NO" , cursor_upload_serve.getString(3));
                    ApplicationDataJson.put("PIDNO" , cursor_upload_serve.getString(4));
                    ApplicationDataJson.put("VARIFI_STS" , cursor_upload_serve.getString(5));
                    ApplicationDataJson.put("REMARKS" , cursor_upload_serve.getString(6));
                    ApplicationDataJson.put("VERIF_DATE" , cursor_upload_serve.getString(7));
                    ApplicationDataJson.put("VERIFY_TIME" , cursor_upload_serve.getString(8));
                    ApplicationDataJson.put("VERIFY_USER" , cursor_upload_serve.getString(9));
                    ApplicationDataJson.put("PO_DATE" , cursor_upload_serve.getString(10));
                    ApplicationDataJson.put("PO_DATE" , cursor_upload_serve.getString(10));

                    jsonArray_RmvData.put(ApplicationDataJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }while (cursor_upload_serve.moveToNext());
        }

        //==== Create final Json Data
        try {
            jsonObject_Final_rmv.put("RMV_COMFORM" , jsonArray_RmvData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //==== Send Data to Live Server
        Volly_Send_Rmv_Data volly_send_rmv_data = new Volly_Send_Rmv_Data(Rmv_Facility_Update_View.this);
        volly_send_rmv_data.SendRmvData(jsonObject_Final_rmv , mVehno.getText().toString());

    }

    private void SaveData()
    {
        if (mPidno.equals(""))
        {
            tv.setText("<PID> is blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else if (mRemarks.equals(""))
        {
            tv.setText("<Remarks> is blank");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_error);
            toast.show();
        }
        else
        {
            Date currentTime = Calendar.getInstance().getTime();
            SQLiteDatabase sqLiteDatabase_save_rmv =   sqlliteCreateLeasing_fac_update_rmv.getWritableDatabase();
            ContentValues contentValues_rmv_create = new ContentValues();


            contentValues_rmv_create.put("BRANCH_CODE" , mbrcode.getText().toString());
            contentValues_rmv_create.put("CHASSI_NO" , mChassino.getText().toString());
            contentValues_rmv_create.put("ENGINE_NO" , mEngno.getText().toString());
            contentValues_rmv_create.put("REG_NO" , mVehno.getText().toString());
            contentValues_rmv_create.put("PIDNO" , mPidno.getText().toString());
            contentValues_rmv_create.put("VARIFI_STS" , mchecksts.getSelectedItem().toString());
            contentValues_rmv_create.put("REMARKS" , mRemarks.getText().toString());
            contentValues_rmv_create.put("VERIF_DATE" , LoginDate);
            contentValues_rmv_create.put("VERIFY_TIME" , currentTime.toString());
            contentValues_rmv_create.put("VERIFY_USER" , LoginUser);
            contentValues_rmv_create.put("PO_DATE" , mPodate.getText().toString());
            contentValues_rmv_create.put("ME_CODE" , mMeofficer.getText().toString());
            contentValues_rmv_create.put("SERVER_UPDTE" , "");
            sqLiteDatabase_save_rmv.insert("RMV_DATA" , null , contentValues_rmv_create);


            //=== Update Table Sts
            ContentValues contentValues_update_table = new ContentValues();
            contentValues_update_table.put("RMV_CONFORM_STS" , mchecksts.getSelectedItem().toString());
            contentValues_update_table.put("RMV_CONFORM_DATE" , LoginDate);
            contentValues_update_table.put("RMV_CONFORM_USER" , LoginUser);
            contentValues_update_table.put("RMV_REMARKS" , mRemarks.getText().toString());
            contentValues_update_table.put("CO_SYS_READ" , "");
            sqLiteDatabase_save_rmv.update("RMV_CONFORM_DATA" , contentValues_update_table , "REG_NO = ?" , new String[]{String.valueOf(Inp_Regno)});


            //==== Upload rmv receipt image
            //=== Run Image Save function
            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            if(null!=mIMGBROWSE.getDrawable())
            {
                File directory = new File(getFilesDir() + "/" + "RMV"  + "/");
                File file = new File(directory, mVehno.getText().toString().trim().replace(" " , "_") + ".jpg");
                //File file = new File(directory, "test.jpg");

                if (!directory.exists()) {
                    directory.mkdir();
                }

                if (!file.exists())
                {
                    Log.d("path", file.toString());
                    Log.d("director", directory.toString());
                    FileOutputStream fos = null;

                    try {
                        BitmapDrawable drawable = (BitmapDrawable) mIMGBROWSE.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();

                        fos = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                        fos.flush();
                        fos.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }

                //=== Get Select Nic
                String Refno = mVehno.getText().toString();
                String chassno =    mChassino.getText().toString();
                sqlliteCreateLeasing_fac_update_rmv.InsertDoc(Refno ,
                        chassno ,
                        "RMV_RECEIPT" ,
                        LoginDate ,
                        LoginUser ,
                        imageViewByte(mIMGBROWSE),
                        directory.toString() ,
                        mPidno.getText().toString());
            }

            mSave.setEnabled(false);
            mSave.setBackgroundResource(R.drawable.normalbuttondisable);

            tv_sucess.setText("Record Successfully Saved.");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout_sucress);
            toast.show();

            mRemarks.setText("");
            mPidno.setText("");
        }
    }


    private void Getsummerydata()
    {
        SQLiteDatabase sqLiteDatabase_getdate = sqlliteCreateLeasing_fac_update_rmv.getReadableDatabase();
        Cursor cursor_get_sum = sqLiteDatabase_getdate.rawQuery("SELECT REG_NO,ENGINE_NO,CHASSI_NO,MK_NAME,PO_DATE,BRANCH_CODE FROM RMV_CONFORM_DATA WHERE REG_NO = '" + Inp_Regno + "' and " +
                "CHASSI_NO = '" + Inp_chass_no + "'" , null  );
        if (cursor_get_sum.getCount() !=0)
        {
            cursor_get_sum.moveToFirst();
            mVehno.setText(cursor_get_sum.getString(0));
            mEngno.setText(cursor_get_sum.getString(1));
            mChassino.setText(cursor_get_sum.getString(2));
            mMeofficer.setText(cursor_get_sum.getString(3));
            mPodate.setText(cursor_get_sum.getString(4));
            mbrcode.setText(cursor_get_sum.getString(5));
        }
        cursor_get_sum.close();
        sqLiteDatabase_getdate.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //=== Galary Access Check
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

               //===== Load Galary Image
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            SelectImageFile = new File(getRealPathFromURI(uri));
            File mdocpath  = new File(SelectImageFile.getPath());

            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = Bitmap.createBitmap(1024, 780, Bitmap.Config.ARGB_8888);
            selectedImage = BitmapFactory.decodeStream(imageStream);
            mIMGBROWSE.setImageBitmap(selectedImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    protected void onDestroy(){

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

    private String imageViewByte(ImageView image)
    {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60,outputStream);
        byte[] imagebyte = outputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imagebyte , Base64.DEFAULT);

        return encodeImage;
    }
}
