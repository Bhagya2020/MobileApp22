package com.AFiMOBILE.afslmobileapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import id.zelory.compressor.Compressor;



public class DocumentUpload extends AppCompatActivity {

    public String mInpAppno="" ,mApStage="";
    private Button mBROWSE , mTAKEPIC , mADD , btnCribupload ;
    private EditText mREMARKS ;
    private Spinner mDOCTYPE , mDOCREFRANCE;
    private ImageView mIMGBROWSE;
    private TextView mAPPNO ;
    private CheckDataConnectionStatus checkDataConnectionStatus;
    private TextView ConnectionSts;
    private Handler handler;
    private RecyclerView mRecycleDocView;
    private String LoginBranch="", LoginDate="" , LoginUser = "";
    private Adapter_Doc_Image mMyDocAdapter;
    SqlliteCreateLeasing db = new SqlliteCreateLeasing(this);
    private Bitmap OriginalImageBitAmp;
    Uri outPutfileUri;
    static int TAKE_PIC =1;
    private static final int REQUEST_CODE = 0x11;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

    final int REQUEST_CODE_GALLERY = 999;
    SqlliteCreateLeasing sqlliteCreateLeasing = new SqlliteCreateLeasing(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long totalSize = (runtime.totalMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("TOTAL - " , String.valueOf(totalSize));
        Log.d("USED - " , String.valueOf(usedMemInMB));
        Log.d("FREE - " , String.valueOf(availHeapSizeInMB));

        mBROWSE         =       (Button)findViewById(R.id.btnBROWSE);
        mTAKEPIC        =       (Button)findViewById(R.id.btnTAKEIMAGE);
        mADD            =       (Button)findViewById(R.id.btnUPLOAD);
        mREMARKS        =       (EditText)findViewById(R.id.txtmeremarks);
        mDOCTYPE        =       (Spinner)findViewById(R.id.spndoctype);
        final Spinner mDOCREFRANCE    =       (Spinner)findViewById(R.id.spnDOCREFERANCE);
        mIMGBROWSE      =       (ImageView)findViewById(R.id.imgpicture);
        mAPPNO          =       (TextView)findViewById(R.id.txtAPPNO) ;
        mRecycleDocView =       (RecyclerView)findViewById(R.id.RcyImage);

        Intent intent = getIntent();
        mInpAppno = intent.getStringExtra("ApplicationNo");
        mApStage  = intent.getStringExtra("AppStage");
        mAPPNO.setText(mInpAppno);

        //=== Globle Varible Details..
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        checkDataConnectionStatus = new CheckDataConnectionStatus(this);

        //=== Check Data Connection status Toolbra every 5 second.
        ConnectionSts = (TextView)findViewById(R.id.TxtDataSts);
        handler=new Handler();
        startCounting();

        //=== Load Camera to take picture
        mTAKEPIC.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),
                            mInpAppno + "_" + mDOCTYPE.getSelectedItem().toString().trim().replace(" " , "_") + ".jpg");
                    outPutfileUri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
                    startActivityForResult(intent, TAKE_PIC);

                    /*
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    */

                }
            }
        });

        //=== Load Doc Referance
        List<String> labels = db.SendDocAppRef(mInpAppno);
        ArrayAdapter<String> arrayAdapterDOCREF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        arrayAdapterDOCREF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDOCREFRANCE.setAdapter(arrayAdapterDOCREF);

        mRecycleDocView =       (RecyclerView)findViewById(R.id.RcyImage);
        mRecycleDocView.setHasFixedSize(true);
        mRecycleDocView.setLayoutManager(new LinearLayoutManager(DocumentUpload.this));
        mMyDocAdapter   = new Adapter_Doc_Image(DocumentUpload.this , mPendingImage(mDOCREFRANCE.getSelectedItem().toString().substring(0,mDOCREFRANCE.getSelectedItem().toString().indexOf("-"))) , "UserLogin");
        mRecycleDocView.setAdapter(mMyDocAdapter);

        mDOCREFRANCE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String mHolder_Type = mDOCREFRANCE.getSelectedItem().toString();
                mHolder_Type = mHolder_Type.substring(mHolder_Type.lastIndexOf("-") +1);

                Log.e("select" , mHolder_Type);

                if (mHolder_Type.equals("APPLICANT"))
                {
                    List<String> mListDocType = db.SendDocType(mInpAppno , mApStage , "CLI");
                    ArrayAdapter<String> arrayDOCTYPE = new ArrayAdapter<String>(DocumentUpload.this,android.R.layout.simple_spinner_item,mListDocType);
                    arrayDOCTYPE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mDOCTYPE.setAdapter(arrayDOCTYPE);
                }
                else
                {
                    Log.e("AP_STAGE" , mApStage);
                    List<String> mListDocType = db.SendDocType(mInpAppno , mApStage , "GUR");
                    ArrayAdapter<String> arrayDOCTYPE = new ArrayAdapter<String>(DocumentUpload.this,android.R.layout.simple_spinner_item,mListDocType);
                    arrayDOCTYPE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mDOCTYPE.setAdapter(arrayDOCTYPE);
                }

                mMyDocAdapter.swapCursor(mPendingImage(mDOCREFRANCE.getSelectedItem().toString().substring(0,mDOCREFRANCE.getSelectedItem().toString().indexOf("-"))));
                mPendingImage(mDOCREFRANCE.getSelectedItem().toString().substring(0,mDOCREFRANCE.getSelectedItem().toString().indexOf("-")));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //=== Browse Image Path
        mBROWSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        DocumentUpload.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

        //=== Image Save ====
        mADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    //=== Run Image Save function
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    //File directory = cw.getDir(mInpAppno, Context.MODE_PRIVATE);

                    String mClidno = mDOCREFRANCE.getSelectedItem().toString().substring(0,mDOCREFRANCE.getSelectedItem().toString().indexOf("-"));
                    Log.e("ID" , mClidno);

                    File directory = new File(getFilesDir() + "/" + mInpAppno  + "/");
                    File file = new File(directory, mInpAppno + "_" + mClidno.replace(" " , "") + "_" + mDOCTYPE.getSelectedItem().toString().trim().replace(" " , "_") + ".jpg");

                    if (!directory.exists()) {
                        directory.mkdir();
                    }

                    if (!file.exists())
                    {
                        Log.d("path", file.toString());
                        Log.d("director", directory.toString());
                        FileOutputStream fos = null;

                        try {
                            //BitmapDrawable drawable = (BitmapDrawable) mIMGBROWSE.getDrawable();
                            Bitmap bitmap = OriginalImageBitAmp;
                            //OriginalImageBitAmp.recycle();

                            fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                            fos.flush();
                            fos.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }

                    //==========================================

                    //=== Get Select Nic
                    String Refno = mDOCREFRANCE.getSelectedItem().toString();
                    String Nic =   Refno.substring(0,Refno.indexOf("-"));
                    sqlliteCreateLeasing.InsertDoc(mInpAppno ,
                            mDOCREFRANCE.getSelectedItem().toString() ,
                            mDOCTYPE.getSelectedItem().toString() ,
                            LoginDate ,
                            LoginUser ,
                            imageViewByte(mIMGBROWSE),
                            directory.toString() ,
                            Nic);

                    mMyDocAdapter.swapCursor(mPendingImage(mDOCREFRANCE.getSelectedItem().toString().substring(0,mDOCREFRANCE.getSelectedItem().toString().indexOf("-"))));
                    Toast.makeText(getApplicationContext(), "Image Successfully Saved.", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
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

    public void onStart()
    {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public Cursor mPendingImage(String mInpNic)
    {
        SQLiteDatabase sqLiteDatabase = sqlliteCreateLeasing.getReadableDatabase();
        Cursor cursorDoc = sqLiteDatabase.rawQuery("SELECT DOC_TYPE , DOC_STS , IMAGE ,APP_REF_NO   FROM AP_DOC_IMAGE WHERE APP_REF_NO = '" + mInpAppno + "' and CL_NIC = '" +  mInpNic + "'", null );
        cursorDoc.moveToFirst();
        return cursorDoc;
    }

    private String imageViewByte(ImageView image)
    {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,40,outputStream);
        byte[] imagebyte = outputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imagebyte , Base64.DEFAULT);

        return encodeImage;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //==== Camera Access Check
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

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

        if (requestCode == TAKE_PIC && resultCode == Activity.RESULT_OK)
        {
            String uri = outPutfileUri.toString();

            //Bitmap myBitmap = BitmapFactory.decodeFile(uri);
            // mImageView.setImageURI(Uri.parse(uri));   OR drawable make image strechable so try bleow also

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
            //Drawable d = new BitmapDrawable(getResources(), bitmap);

            //==== compress image
            File mdocpath_capture  = new File(getRealPathFromURI(outPutfileUri));
            File compressedImage_capture = new Compressor.Builder(DocumentUpload.this)
                    .setMaxWidth(768)
                    .setMaxHeight(1024)
                    .setQuality(60)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .build()
                    .compressToFile(mdocpath_capture);

            Bitmap myBitmap = BitmapFactory.decodeFile(compressedImage_capture.getAbsolutePath());
            OriginalImageBitAmp = myBitmap;
            mIMGBROWSE.setImageBitmap(myBitmap);

            /*
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mIMGBROWSE.setImageBitmap(photo);
             */
        }

        //===== Load Galary Image
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null)
        {

            Uri uri = data.getData();
            //SelectImageFile = new File(getRealPathFromURI(uri));
            File mdocpath  = new File(getRealPathFromURI(uri));

            /*

            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = Bitmap.createBitmap(1024, 780, Bitmap.Config.ARGB_8888);
            selectedImage = BitmapFactory.decodeStream(imageStream);
            mIMGBROWSE.setImageBitmap(selectedImage);
            */

            File compressedImage = new Compressor.Builder(DocumentUpload.this)
                    .setMaxWidth(768)
                    .setMaxHeight(1024)
                    .setQuality(60)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .build()
                    .compressToFile(mdocpath);


            //Bitmap myBitmap = Bitmap.createBitmap(1024, 780, Bitmap.Config.ARGB_8888);
            //myBitmap = BitmapFactory.decodeFile(mdocpath.getAbsolutePath());

            Bitmap myBitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
            OriginalImageBitAmp = myBitmap;
            mIMGBROWSE.setImageBitmap(myBitmap);

            //SelectImageFile = compressedImage.getAbsolutePath();
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


    @Override
    protected void onDestroy() {
        sqlliteCreateLeasing.close();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        super.onDestroy();

    }
}
