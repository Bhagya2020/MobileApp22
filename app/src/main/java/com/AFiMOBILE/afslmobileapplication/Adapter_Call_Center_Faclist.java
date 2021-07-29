package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Call_Center_Faclist extends RecyclerView.Adapter<Adapter_Call_Center_Faclist.PendingFacList> {

    private Context mContex;
    private Cursor mCursor;

    public Adapter_Call_Center_Faclist(Context context , Cursor cursor)
    {
        mContex     =   context;
        mCursor     =   cursor;
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        TextView  mFacilityno ;
        public PendingFacList(@NonNull View itemView) {
            super(itemView);

            mFacilityno  =   itemView.findViewById(R.id.Txtfacilityno);
        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.card_view_link_facility, parent , false);
        return new Adapter_Call_Center_Faclist.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacList holder, int position) {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.mFacilityno.setText(mCursor.getString(0));
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
