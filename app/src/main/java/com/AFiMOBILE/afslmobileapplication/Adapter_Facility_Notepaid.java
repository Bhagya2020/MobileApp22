package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.PriorityQueue;

public class Adapter_Facility_Notepaid extends RecyclerView.Adapter<Adapter_Facility_Notepaid.PendingFacList>
{
    private Context mContex;
    private Cursor mCursor;
    public Adapter_Facility_Notepaid(Context context , Cursor cursor)
    {
        mContex = context;
        mCursor = cursor;
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        public TextView mDate , mNoteUser , mNote1 , mNote2;
        public PendingFacList(@NonNull View itemView) {
            super(itemView);

            mDate           =   itemView.findViewById(R.id.TxtRecyNoteDate);
            mNoteUser       =   itemView.findViewById(R.id.TxtRecyNoteUser);
            mNote1          =   itemView.findViewById(R.id.TxtRecyNoteDes1);
            mNote2          =   itemView.findViewById(R.id.TxtRecyNoteDes2);
        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.recovery_note_cardviwer , parent , false);
        return new Adapter_Facility_Notepaid.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacList holder, int position)
    {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }


        holder.mNote1.setText("");
        holder.mNote2.setText("");
        holder.mDate.setText(mCursor.getString(0));
        holder.mNoteUser.setText(mCursor.getString(1));

        if (mCursor.getString(2).length() <= 45)
        {
            holder.mNote1.setText(mCursor.getString(2));
        }
        else
        {
            holder.mNote1.setText(mCursor.getString(2).substring(0 , 46));
            holder.mNote2.setText(mCursor.getString(2).substring(46));
        }
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




