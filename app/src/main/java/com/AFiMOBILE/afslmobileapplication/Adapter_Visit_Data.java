package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Visit_Data extends RecyclerView.Adapter <Adapter_Visit_Data.PendingFacList> {

    private Context mContex;
    private Cursor mCursor;

    public Adapter_Visit_Data(Context context , Cursor cursor)
    {
        mContex     =   context;
        mCursor     =   cursor;
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        TextView mSerialno , mActiondate , mFaclityno , mActioncdoe , mLoca_lat , mLoca_Long;
        public PendingFacList(@NonNull View itemView) {
            super(itemView);

            mSerialno       =   itemView.findViewById(R.id.Txtserieal) ;
            mActiondate     =   itemView.findViewById(R.id.Txtactiondate) ;
            mFaclityno      =   itemView.findViewById(R.id.Txtfacilityno) ;
            mActioncdoe     =   itemView.findViewById(R.id.TxtACTIONCODE) ;
            mLoca_Long       =   itemView.findViewById(R.id.txtlocaLon) ;
            mLoca_lat       =   itemView.findViewById(R.id.TxtLocLat) ;

        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.card_view_visit_data, parent , false);
        return new Adapter_Visit_Data.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacList holder, int position) {

        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.mSerialno.setText(mCursor.getString(0));
        holder.mActiondate.setText(mCursor.getString(1));
        holder.mFaclityno.setText(mCursor.getString(2));
        holder.mActioncdoe.setText(mCursor.getString(3));
        holder.mLoca_Long.setText(mCursor.getString(4));
        holder.mLoca_lat.setText(mCursor.getString(5));

    }

    @Override
    public int getItemCount() {return mCursor.getCount();}

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
