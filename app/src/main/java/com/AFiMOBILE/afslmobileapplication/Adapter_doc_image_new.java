package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_doc_image_new extends RecyclerView.Adapter<Adapter_doc_image_new.PendingDocImageViewHolder> {

    private Context mContex;
    private Cursor mCursor;

    public Adapter_doc_image_new (Context context , Cursor cursor)
    {
        mContex = context;
        mCursor = cursor;
    }

    public class PendingDocImageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mDoc_type;
        public ImageButton mBtnPrive , mBtnDelete;

        public PendingDocImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mDoc_type   =   itemView.findViewById(R.id.textview_doctype_new) ;
            mBtnPrive   =   itemView.findViewById(R.id.Imgview) ;
            mBtnDelete   =   itemView.findViewById(R.id.Imedelete) ;

        }
    }

    @NonNull
    @Override
    public PendingDocImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.pending_image_new , parent , false);
        return new Adapter_doc_image_new.PendingDocImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingDocImageViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        final String mDocType =  mCursor.getString(0) ;
        final String mStatus  =  mCursor.getString(1) ;

        holder.mDoc_type.setText(mDocType);

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
