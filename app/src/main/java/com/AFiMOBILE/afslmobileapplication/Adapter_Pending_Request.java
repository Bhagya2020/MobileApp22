package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.AFiMOBILE.afslmobileapplication.Recovery_Search_Facility.mRecoverySearchSelectFacilityNo;

public class Adapter_Pending_Request extends RecyclerView.Adapter<Adapter_Pending_Request.PendingRequestFac>
{
    private Context mContex;
    private Cursor mCursor;
    int row_index = -1;

    public Adapter_Pending_Request(Context context , Cursor cursor)
    {
        mContex =   context;
        mCursor =   cursor;
    }

    public class PendingRequestFac extends RecyclerView.ViewHolder
    {
        public TextView mFacilityNo , mRequetsOfficer , mReqDate , mReqReson , mRequestComment;
        public ImageButton mDocView;

        public PendingRequestFac(@NonNull View itemView) {
            super(itemView);

            mFacilityNo         =       itemView.findViewById(R.id.TxtFacno) ;
            mRequetsOfficer     =       itemView.findViewById(R.id.TextRequestby) ;
            mReqDate            =       itemView.findViewById(R.id.TextRequestDate) ;
            mReqReson           =       itemView.findViewById(R.id.TextRequestReason) ;
            mRequestComment     =       itemView.findViewById(R.id.TextRequestcomment) ;

            mDocView            =       itemView.findViewById(R.id.ImgViewdoc);

        }
    }

    @NonNull
    @Override
    public PendingRequestFac onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.pending_request_recycle , parent , false);
        return new Adapter_Pending_Request.PendingRequestFac(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingRequestFac holder, final int position)
    {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            row_index = position;
            notifyDataSetChanged();

            Recovery_Request_Pending_Case.mRequestManagerFacno = holder.mFacilityNo.getText().toString();
            }
        });

        if(row_index==position){

            holder.itemView.setBackgroundColor(Color.parseColor("#732170"));
        }
        else
        {
           holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.mFacilityNo.setText(mCursor.getString(0));
        holder.mRequetsOfficer.setText(mCursor.getString(1));
        holder.mReqDate.setText(mCursor.getString(2));
        holder.mReqReson.setText(mCursor.getString(3));
        holder.mRequestComment.setText(mCursor.getString(4));

        holder.mDocView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_Docview = new Intent("android.intent.action.Recovery_Facility_Details_View");
                intent_Docview.putExtra("FACNO" , holder.mFacilityNo.getText().toString());
                intent_Docview.putExtra("TYPE" , "SEARCH");
                mContex.startActivity(intent_Docview);
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
