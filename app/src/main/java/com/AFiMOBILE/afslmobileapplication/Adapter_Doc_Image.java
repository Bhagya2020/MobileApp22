package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;

public class Adapter_Doc_Image extends RecyclerView.Adapter<Adapter_Doc_Image.PendingDocImageViewHolder> {

    private Context mContex;
    private Cursor mCursor;
    public String LoadDataType;

    public Adapter_Doc_Image (Context context , Cursor cursor , String mLoginType)
    {
        mContex = context;
        mCursor = cursor;
        LoadDataType = mLoginType;
    }

    public class PendingDocImageViewHolder extends RecyclerView.ViewHolder
    {

        public TextView mTxtDocType;
        public ImageButton imgBtnDelete , imgZoombtn;

        public PendingDocImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mTxtDocType     =   itemView.findViewById(R.id.textview_doctype);
            imgBtnDelete    =   itemView.findViewById(R.id.imageBtndelete);
            imgZoombtn      =   itemView.findViewById(R.id.imagebtnzoom);
        }
    }

    @NonNull
    @Override
    public PendingDocImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.pending_image_view , parent , false);
        return new Adapter_Doc_Image.PendingDocImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingDocImageViewHolder holder, int position)
    {
        if (!mCursor.moveToPosition(position))
        {
           return;
        }

        final String mDoc_CL_Name     =   mCursor.getString(0) ;
        final String mDoc_type        =   mCursor.getString(1);
        final String mAppno           =   mCursor.getString(3);

        holder.mTxtDocType.setText(mDoc_type);

        holder.imgZoombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom = new Intent("android.intent.action.image_zoom");
                intent_zoom.putExtra("AppNo" , mAppno);
                intent_zoom.putExtra("DocRefno" , mDoc_CL_Name );
                intent_zoom.putExtra("DocType" , mDoc_type);
                intent_zoom.putExtra("LoadType" , LoadDataType);
                mContex.startActivity(intent_zoom);
            }
        });


        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SqlliteCreateLeasing sqlliteCreateLeasing  = new SqlliteCreateLeasing(mContex);
                sqlliteCreateLeasing.DeleteImage(mAppno , mDoc_CL_Name , mDoc_type);
                notifyDataSetChanged();

                //swapCursor(mCursor);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
