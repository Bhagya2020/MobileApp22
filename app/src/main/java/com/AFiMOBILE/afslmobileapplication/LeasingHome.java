package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class LeasingHome extends AppCompatActivity {

    private TextView mLoginName , mLoginDate , mHomeNotify ;
    public TextView ConnectionSts;
    private Button mAppliocation , mApprove , mSetting , mSubmit , mVisit , mTrialCal;
    SQLiteDatabase sqLiteDatabase;
    SqlliteCreateLeasing sqlliteCreateLeasing;
    private String LoginUser , LoginUserType , checkLoginData;
    private ImageView mProfilePicture;
    public static  Toolbar toolbar;
    public ImageButton BtnImgDataSync , BtnImgViewSyns , BtnImgLogout , BtnImgInformation;
    public DataSynchronize dataSynchronize;
    public SyncCountOfficer syncCountOfficer;
    public FabSpeedDial fabSpeedDial;
    CheckDataConnectionStatus checkDataConnectionStatus;
    public Handler handler;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leasing_home);

        mAppliocation   =   findViewById(R.id.btnapplication);
        mApprove        =   findViewById(R.id.btnmanagerapprove);
        mSubmit         =   findViewById(R.id.btncomplete);
        mSetting        =   findViewById(R.id.btnsetting) ;
        mVisit          =   findViewById(R.id.btnvisit) ;
        mTrialCal       =   findViewById(R.id.btnTrialCal) ;
        //mImgSyncData    =   (ImageButton)findViewById(R.id.imdsyncbutton);
        mProfilePicture =   findViewById(R.id.propicture);
        mHomeNotify     =   findViewById(R.id.txtnotficationcount);
        fabSpeedDial    =    findViewById(R.id.flotbutton);

        BtnImgDataSync  =   (ImageButton)findViewById(R.id.ImgDataSyn);
        BtnImgViewSyns  =   (ImageButton)findViewById(R.id.ImgBtnsys);
        BtnImgLogout    =   (ImageButton)findViewById(R.id.Imglogout);

        BtnImgInformation = (ImageButton)findViewById(R.id.Imginformastion);

        syncCountOfficer = new SyncCountOfficer(LeasingHome.this);
        checkDataConnectionStatus = new CheckDataConnectionStatus(LeasingHome.this);
        sqlliteCreateLeasing = new SqlliteCreateLeasing(this);

        mLoginName       =   (TextView)findViewById(R.id.txtusername);
        mLoginDate       =   (TextView)findViewById(R.id.txtdate);

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        mLoginName.setText(globleClassDetails.getOfficerName());
        mLoginDate.setText(globleClassDetails.getLoginDate());
        LoginUser  =   globleClassDetails.getUserid();
        checkLoginData = globleClassDetails.getLoginDate();

        ConnectionSts = (TextView)findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();


        //=== check Master update available
        SQLiteDatabase sqLiteDatabase_check_master = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursor_chk_master = sqLiteDatabase_check_master.rawQuery("SELECT * FROM AP_MAST_BRANCH" , null);
        if (cursor_chk_master.getCount() == 0)
        {
            String Fline = "Master Data Not Available . Please First Update the Master.";
            String Fline2 = "Setting ---> Master Update.";
            AlertDialog.Builder builder_master = new AlertDialog.Builder(this);
            builder_master.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
            builder_master.setMessage(Fline + "\n" + Fline2);
            builder_master.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            builder_master.create();
            builder_master.show();
        }
        sqLiteDatabase_check_master.close();
        cursor_chk_master.close();

        //=== Information Button


        BtnImgInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_infor = new Intent("android.intent.action.Marketing_Reports");
                startActivity(intent_infor);
            }
        });



        //=== Flot Icon Load
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {


                if (menuItem.getTitle().equals("Call"))
                {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    startActivity(callIntent);
                }
                if (menuItem.getTitle().equals("Chat"))
                {
                    Intent callIntent = new Intent(Intent.ACTION_VIEW);
                    startActivity(callIntent);
                }
                return false;
            }
        });

        //=== Log Out Button
        BtnImgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //=== View Sync Data Details
        BtnImgViewSyns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_sync = new Intent("android.intent.action.View_Notification_Details");
                startActivity(intent_sync);
            }
        });


        BtnImgDataSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean mChkSts = checkDataConnectionStatus.IsConnected();
                if (mChkSts)
                {
                    //==== Get ME Target Details
                    //Target_DataSync target_dataSync = new Target_DataSync(LeasingHome.this);
                    //target_dataSync.Get_Target_data(LoginUser);

                    //==== Daily Data sync
                    DataSynchronize dataSynchronize = new DataSynchronize(LeasingHome.this);
                    dataSynchronize.AppDataSync(LoginUser);

                    //=== Get Count Data ===
                    SQLiteDatabase sqLiteDatabase_count = sqlliteCreateLeasing.getReadableDatabase();
                    Cursor cursor_sync_count = sqLiteDatabase_count.rawQuery("SELECT NOT_REF_ID FROM MY_NOTIFICARTION WHERE NOT_READ = '' AND  USER_ID = '" + LoginUser + "'" , null );

                    if (cursor_sync_count.getCount() != 0)
                    {
                        mHomeNotify.setVisibility(View.VISIBLE);
                        mHomeNotify.setText(String.valueOf(cursor_sync_count.getCount()));
                    }
                    else
                    {
                        mHomeNotify.setVisibility(View.INVISIBLE);
                    }
                    sqLiteDatabase_count.close();
                    cursor_sync_count.close();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LeasingHome.this);
                    builder.setTitle(Html.fromHtml("<font color='#FF7F27'>AFi Mobile</font>"));
                    builder.setMessage("Data Connection Not available. Please Turn on Connection.");
                    builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                }
            }
        });

        //==== Check Acces Detaiols (Approvels)
        //CheckMgrAcces();


        //=== Set PHP Url Path
        sqLiteDatabase = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursordata = sqLiteDatabase.rawQuery("SELECT * FROM APP_CONFIG where CONFIG_TYPE = 'PHP_URL'" , null);
        cursordata.moveToFirst();
        if (cursordata.getCount() != 0)
        {
            globleClassDetails.setPHP_Path(cursordata.getString(1));
        }
        cursordata.close();

        mTrialCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inttrical = new Intent("android.intent.action.TrialCalculater");
                startActivity(inttrical);
            }
        });

        mVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmgrpprove = new Intent("android.intent.action.house_visit_application");
                startActivity(intentmgrpprove);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmgrpprove = new Intent("android.intent.action.SubmitApplication");
                startActivity(intentmgrpprove);

            }
        });

        mApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentmgrpprove = new Intent("android.intent.action.ManagerApprove");
                startActivity(intentmgrpprove);
            }
        });

        mAppliocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intentsetting  = new Intent("android.intent.action.Application");
                startActivity(intentsetting);
            }
        });

        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentsetting  = new Intent("android.intent.action.Setting");
                startActivity(intentsetting);

            }
        });
    }

    private void startCounting() {
        handler.post(run);
    }

    protected void onDestroy(){
        sqLiteDatabase.close();
        sqlliteCreateLeasing.close();

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();

        super.onDestroy();
    }

    private Runnable run = new Runnable() {
        @Override
        public void run()
        {

            boolean CheckConnection = checkDataConnectionStatus.IsConnected();

            if (CheckConnection)
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

    public void GetProfilePic()
    {
        //=====Get Profile Picture =======
        SQLiteDatabase sqLiteDatabase_get_image = sqlliteCreateLeasing.getWritableDatabase();
        Cursor cursor_get_Image = sqLiteDatabase_get_image.rawQuery("SELECT PROFILE_IAME FROM PROFILE_PICTURE WHERE USER_ID = '" + LoginUser + "'" , null);
        if (cursor_get_Image.getCount()!=0)
        {
            cursor_get_Image.moveToFirst();
            byte[] encodeByte = Base64.decode(cursor_get_Image.getString(0), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            mProfilePicture.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    protected void onStart()
    {
        super.onStart();
        CheckMgrAcces();
        GetProfilePic();

        //=== Run Daily Sync Process
        boolean CheckConnection = checkDataConnectionStatus.IsConnected();
        if (CheckConnection)
        {
            GetDatasyn();
        }

        mHomeNotify.setVisibility(View.INVISIBLE);
    }


    public void GetDatasyn()
    {
        SQLiteDatabase sqLiteDatabase_check_sync = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursor_check_sync = sqLiteDatabase_check_sync.rawQuery("SELECT * FROM HOME_DATA_SYNC WHERE SYNC_DATE = '" + checkLoginData + "' and SYNC_USER = '" + LoginUser + "'" , null );
        if (cursor_check_sync.getCount()!=0)
        {
            //===== Process ok.
        }
        else
        {
            DataSynchronize dataSynchronize_firstlogin = new DataSynchronize(LeasingHome.this);
            dataSynchronize_firstlogin.AppDataSync(LoginUser);
        }
    }

    public void CheckMgrAcces()
    {

        SQLiteDatabase sqLiteDatabase_acces =  sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursor_acces = sqLiteDatabase_acces.rawQuery("SELECT USER_ROLL FROM USER_MANAGEMENT WHERE OFFIER_ID = '" + LoginUser + "'" , null);
        if (cursor_acces.getCount() != 0)
        {
            cursor_acces.moveToFirst();

            String UserRoll = cursor_acces.getString(0);
            if (UserRoll.equals("APP"))
            {
                mApprove.setVisibility(View.VISIBLE);
            }
            else
            {
                mApprove.setVisibility(View.INVISIBLE);
            }
        }
    }
}
