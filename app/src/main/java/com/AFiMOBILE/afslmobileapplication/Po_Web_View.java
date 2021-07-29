package com.AFiMOBILE.afslmobileapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URLEncoder;

public class Po_Web_View extends AppCompatActivity {

    public static WebView mWebView;
    public String mInpAppno , LoginUser,LoginDate,LoginBranch,PHP_URL_SQL;
    public static Po_Web_View instance_po_web = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.po_web_view);

        instance_po_web = this;

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        PHP_URL_SQL =   globleClassDetails.getPHP_Path();

        Intent intent = getIntent();
        mInpAppno    =   intent.getStringExtra("ApplicationNo");

        //==== Create Url Using Paramater
        StringBuffer buffer=new StringBuffer("http://afimobile.abansfinance.lk/mobilephp/Po-Details-Web-View.php");
        buffer.append("?refno="+ URLEncoder.encode(mInpAppno));
        buffer.append("&mgrcode="+URLEncoder.encode(LoginUser));

        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.addJavascriptInterface(new WebAppInterface(Po_Web_View.this) , "Android");
        mWebView.loadUrl(buffer.toString());
    }
}
