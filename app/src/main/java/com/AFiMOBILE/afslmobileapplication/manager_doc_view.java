package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

public class manager_doc_view extends AppCompatActivity {

    public TextView mAppno;
    public RecyclerView recyclerView_Image;
    public SqlliteCreateLeasing sqlliteCreateLeasing_ManagerImageView;
    public SQLiteDatabase sqLiteDatabase_ManagerDocView;
    public Adapter_Doc_Image mMyDocAdapter;
    public static Toolbar toolbar;
    public Handler handler;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    public TextView ConnectionSts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_doc_view);

        mAppno              =   (TextView)findViewById(R.id.txtappno);
        recyclerView_Image  =   (RecyclerView)findViewById(R.id.RcyImage);

        //=== DataBase Connection
        sqlliteCreateLeasing_ManagerImageView = new SqlliteCreateLeasing(this);
        sqLiteDatabase_ManagerDocView = sqlliteCreateLeasing_ManagerImageView.getReadableDatabase();
        checkDataConnectionStatus = new CheckDataConnectionStatus(this);


        Intent intent = getIntent();
        mAppno.setText(intent.getStringExtra("ApplicationNo"));

        recyclerView_Image =       (RecyclerView)findViewById(R.id.RcyImage);
        recyclerView_Image.setHasFixedSize(true);
        recyclerView_Image.setLayoutManager(new LinearLayoutManager(manager_doc_view.this));
        mMyDocAdapter   = new Adapter_Doc_Image(manager_doc_view.this , GetImageData() , "ManagerLogin");
        recyclerView_Image.setAdapter(mMyDocAdapter);

        //=== Check Data Connection status Toolbra every 5 second.
        //setSupportActionBar(toolbar);
        ConnectionSts = (TextView) findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();


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


    public Cursor GetImageData()
    {
        Cursor cursor_image =  sqLiteDatabase_ManagerDocView.rawQuery("SELECT DOC_REF , DOCTYPE , DOC_IMAGE , APP_REF_NO FROM MANAGER_VIEW_IMAGE WHERE APP_REF_NO = '" + mAppno.getText() + "'" , null);
        return cursor_image;
    }

    protected void onDestroy(){
        super.onDestroy();
        sqLiteDatabase_ManagerDocView.close();
        sqlliteCreateLeasing_ManagerImageView.close();
        Log.d("Log", "onDestroy-Application");
    }
}
