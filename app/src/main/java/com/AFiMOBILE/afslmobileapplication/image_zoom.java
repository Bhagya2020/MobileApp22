package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.github.chrisbanes.photoview.PhotoView;

public class image_zoom extends AppCompatActivity {

    private PhotoView photoView;
    public String mAppno , mDocRf , MdocType , GetImage , LoadType;
    private ImageButton mBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        photoView       =  (PhotoView)findViewById(R.id.photo_view_doc);
        mBtnBack        =  (ImageButton)findViewById(R.id.imageback);
        Intent intent = getIntent();
        mAppno      =   intent.getStringExtra("AppNo");
        mDocRf      =   intent.getStringExtra("DocRefno");
        MdocType    =   intent.getStringExtra("DocType");
        LoadType    =   intent.getStringExtra("LoadType");


        if (LoadType.equals("UserLogin"))
        {
            SqlliteCreateLeasing sqlliteCreateLeasing = new SqlliteCreateLeasing(this);
            SQLiteDatabase sqLiteDatabase_Image = sqlliteCreateLeasing.getReadableDatabase();
            Cursor cursor_image =  sqLiteDatabase_Image.rawQuery("SELECT IMAGE FROM AP_DOC_IMAGE WHERE APP_REF_NO = '" +  mAppno + "' " +
                    "AND DOC_TYPE = '" + mDocRf + "' AND DOC_STS = '" +  MdocType + "'"  , null);
            if (cursor_image.getCount() != 0)
            {
                cursor_image.moveToFirst();

                byte[] encodeByte = Base64.decode(cursor_image.getString(0), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                photoView.setImageBitmap(bitmap);
            }
            else
            {
                photoView.setImageResource(R.mipmap.ic_launcher);
            }
            cursor_image.close();
            sqLiteDatabase_Image.close();
        }
        else
        {
            SqlliteCreateLeasing sqlliteCreateLeasing = new SqlliteCreateLeasing(this);
            SQLiteDatabase sqLiteDatabase_Image = sqlliteCreateLeasing.getReadableDatabase();
            Cursor cursor_image =  sqLiteDatabase_Image.rawQuery("SELECT DOC_IMAGE FROM MANAGER_VIEW_IMAGE WHERE APP_REF_NO = '" +  mAppno + "' " +
                    "AND DOC_REF = '" + mDocRf + "' AND DOCTYPE = '" +  MdocType + "'"  , null);
            if (cursor_image.getCount() != 0)
            {
                cursor_image.moveToFirst();

                byte[] encodeByte = Base64.decode(cursor_image.getString(0), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                photoView.setImageBitmap(bitmap);
            }
            else
            {
                photoView.setImageResource(R.mipmap.ic_launcher);
            }
            cursor_image.close();
            sqLiteDatabase_Image.close();
        }

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
