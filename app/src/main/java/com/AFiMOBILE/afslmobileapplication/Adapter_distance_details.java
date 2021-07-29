package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_distance_details extends RecyclerView.Adapter<Adapter_distance_details.PendingFacList> {

    private Context mContex;
    private Cursor mCursor;


    public Adapter_distance_details(Context context , Cursor cursor)
    {
        mContex     =   context;
        mCursor     =   cursor;
    }
    public class PendingFacList extends RecyclerView.ViewHolder
    {
        TextView mSerialno , mActiondate , mFaclityno , mTotaldis;
        public PendingFacList(@NonNull View itemView) {
            super(itemView);

            mSerialno       =   itemView.findViewById(R.id.Txtserieal) ;
            mActiondate     =   itemView.findViewById(R.id.Txtactiondate) ;
            mFaclityno      =   itemView.findViewById(R.id.Txtfacilityno) ;
            mTotaldis     =   itemView.findViewById(R.id.Txttottaldistance) ;
        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.card_view_distanec_update, parent , false);
        return new Adapter_distance_details.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacList holder, int position) {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        //holder.mSerialno.setText(mCursor.getString(0));
        holder.mActiondate.setText(mCursor.getString(0));
        holder.mFaclityno.setText(mCursor.getString(1));
        holder.mTotaldis.setText(mCursor.getString(2));
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
