package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ir.mahdi.mzip.zip.ZipArchive;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploadServer
{
    //===== To Upload the Image folder to hostgater

    private Context mContex;
    private String mApplicationNo="" ,mFileLocastion="" , fileuploadsts="";
    private  OkHttpClient client;

    public FileUploadServer (Context context)
    {
        mContex = context;
        client = null;
    }

    public String Upload_doc (String mAppno , String mFilePath)
    {
        mApplicationNo  =   mAppno;
        mFileLocastion  =   mFilePath;
        //===================================
        //========= Create Select folder to Zip


        ZipArchive zipArchive = new ZipArchive();
        zipArchive.zip(mFilePath,mFilePath + ".zip","");
        Log.d("Zip Path" , mFilePath + ".zip");

        //=== Send the Zip File to Server
        File file_upload = new File(mFilePath + ".zip");
        String content_type = getMimeType(file_upload.getPath());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String file_path = file_upload.getAbsolutePath();


        client = new OkHttpClient.Builder()
                .connectTimeout(350, TimeUnit.SECONDS)
                .writeTimeout(350, TimeUnit.SECONDS)
                .readTimeout(350, TimeUnit.SECONDS)
                .build();

        RequestBody file_body = RequestBody.create(MediaType.parse(content_type),file_upload);

        RequestBody request_body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type",content_type)
                .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                .build();

        Log.e("file-upload" , "done-1");

        Request request = new Request.Builder()
                .url("http://203.115.12.125:82/core/mobnew/Image-File-Transer.php")
                .post(request_body)
                .build();

        Request request_hostgater = new Request.Builder()
                .url("http://afimobile.abansfinance.lk/mobilephp/File_upload_server.php")
                .post(request_body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            client.newCall(request_hostgater).execute();
            Log.e("file-upload" , "done-2");
            if(!response.isSuccessful()){
                fileuploadsts="ERROR";
                Log.e("file-upload" , "done-3");
                throw new IOException("Error : "+response);
            }
            else
            {
                Log.e("file-upload" , "done-4");
                Log.d("FINAL" , "DONE");
                fileuploadsts="DONE";
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileuploadsts;
    }

    private String getMimeType(String path)
    {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
