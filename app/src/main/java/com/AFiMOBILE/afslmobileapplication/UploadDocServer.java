package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import ir.mahdi.mzip.zip.ZipArchive;

public class UploadDocServer {

    private Context mContex;
    private String mApplicationNo="" , mFileLocastion="" , upLoadServerUri="" , fileName="" , fileresponces="" ;
    private int serverResponseCode;


    public UploadDocServer (Context context)
    {
        mContex = context;
    }


    public String SendDocFolder (String mAppno , String mFilePath)
    {
        mApplicationNo  =   mAppno;
        mFileLocastion  =   mFilePath;
        fileName = mAppno;

        //===================================
        //========= Create Select folder to Zip

        ZipArchive zipArchive = new ZipArchive();
        zipArchive.zip(mFilePath,mFilePath + ".zip","");
        Log.d("Zip Path" , mFilePath + ".zip");

        //=== Send the Zip File to Server
        File file_upload = new File(mFilePath + ".zip");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //======================================
        //=== Upload Url
        upLoadServerUri = "http://203.115.12.125:82/core/mobnew/Image-File-Transer.php";

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        // open a URL connection to the Servlet
        try {
            FileInputStream fileInputStream = new FileInputStream(file_upload);
            URL url = new URL(upLoadServerUri);
            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + fileName + "" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.e("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200)
            {
                fileresponces="DONE";
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileresponces;
    }


}
