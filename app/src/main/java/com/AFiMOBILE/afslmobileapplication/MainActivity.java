package com.AFiMOBILE.afslmobileapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mLeasingButton , mRecoveryButton;
    ImageSlider imageSlider;
    TextView mVersionShow;
    public CheckDataConnectionStatus checkDataConnectionStatus;
    URL url ;
    List<SlideModel> slideModels = new ArrayList<>();
    public String mPakageName;
    private Document document;

    private String latestVersion;
    private String currentVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLeasingButton  =  findViewById(R.id.btnleasing);
        mRecoveryButton =  findViewById(R.id.btnrecovery);
        imageSlider = findViewById(R.id.image_slider);

        //==== Check The Version Details
        mVersionShow    =   findViewById(R.id.txtversion);
        PackageManager manager = this.getPackageManager();


        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            mVersionShow.setText("Version - "  +  info.versionName);
            mPakageName = getApplicationInfo().packageName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //==== Add image
        slideModels.add(new SlideModel(R.drawable.side1 , ""));
        slideModels.add(new SlideModel(R.drawable.side2 , ""));
        slideModels.add(new SlideModel(R.drawable.side3  , ""));
        slideModels.add(new SlideModel(R.drawable.side4  , ""));
        slideModels.add(new SlideModel(R.drawable.side5  , ""));
        imageSlider.setImageList(slideModels , true);


        mLeasingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent("android.intent.action.Leasing_Password");
                startActivity(intent);
                finish();

            }
        });

        mRecoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.Recovery_Password");
                startActivity(intent);
                finish();

            }
        });
    }

    protected void onStart() {
        super.onStart();

        //==== Check google play strore version check
        checkDataConnectionStatus = new CheckDataConnectionStatus(MainActivity.this);
        boolean ChkSts = checkDataConnectionStatus.IsConnected();

        mLeasingButton.setEnabled(true);
        mRecoveryButton.setEnabled(true);

        mLeasingButton.setBackgroundResource(R.drawable.reoveryhomebutton);
        mRecoveryButton.setBackgroundResource(R.drawable.reoveryhomebutton);


        if (ChkSts)
        {
            mLeasingButton.setEnabled(false);
            mRecoveryButton.setEnabled(false);

            mLeasingButton.setBackgroundResource(R.drawable.normalbuttondisable);
            mRecoveryButton.setBackgroundResource(R.drawable.normalbuttondisable);

             forceUpdate();
        }
        else
        {
            mLeasingButton.setEnabled(true);
            mRecoveryButton.setEnabled(true);

            mLeasingButton.setBackgroundResource(R.drawable.reoveryhomebutton);
            mRecoveryButton.setBackgroundResource(R.drawable.reoveryhomebutton);
        }

    }

    protected void onDestroy(){

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));

        slideModels.clear();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        super.onDestroy();
    }


    public void forceUpdate(){
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo =packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new ForceUpdateAsync(currentVersion,MainActivity.this).execute();
    }

    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

        private String latestVersion;
        private String currentVersion;
        private Context context;
        public ForceUpdateAsync(String currentVersion, Context context){
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {

                document = (Document) Jsoup.connect("https://play.google.com/store/apps/details?id=" + mPakageName + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .get();

                Element element = document.select("div:matchesOwn(^Current Version$)").first().parent().select("span").first();
                latestVersion = element.text();


                Log.e("latestversion","---"+ latestVersion);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(latestVersion!=null){
                if(!currentVersion.equalsIgnoreCase(latestVersion)){

                    mVersionShow.setText("App Version out of date.");
                    Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
                    if(!((Activity)context).isFinishing()){
                        showForceUpdateDialog();
                    }
                }
                else
                {
                    mLeasingButton.setEnabled(true);
                    mRecoveryButton.setEnabled(true);

                    mLeasingButton.setBackgroundResource(R.drawable.reoveryhomebutton);
                    mRecoveryButton.setBackgroundResource(R.drawable.reoveryhomebutton);
                }
            }
            super.onPostExecute(jsonObject);
        }

        public void showForceUpdateDialog(){
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
        }
    }

}
