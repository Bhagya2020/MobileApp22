package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_distance_visit extends RecyclerView.Adapter <Adapter_distance_visit.PendingFacList> {

    private Context mContex;
    private Cursor mCursor;


    public Adapter_distance_visit(Context context , Cursor cursor)
    {
        mContex     =   context;
        mCursor     =   cursor;
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        TextView mSeriealno , mFacilityno , mActionCode;
        public PendingFacList(@NonNull View itemView) {
            super(itemView);

            mSeriealno  =   itemView.findViewById(R.id.Txtserieal);
            mFacilityno  =   itemView.findViewById(R.id.Txtfacilityno);
            mActionCode  =   itemView.findViewById(R.id.TxtActioncode);

        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.card_view_visit_update, parent , false);
        return new Adapter_distance_visit.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacList holder, int position) {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        //holder.mSerialno.setText(mCursor.getString(0));
        holder.mSeriealno.setText(mCursor.getString(0));
        holder.mFacilityno.setText(mCursor.getString(1));
        holder.mActionCode.setText(mCursor.getString(2));

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
