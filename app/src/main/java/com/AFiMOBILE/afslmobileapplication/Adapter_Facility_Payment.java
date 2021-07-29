package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Facility_Payment extends RecyclerView.Adapter<Adapter_Facility_Payment.PendingFacList>
{
    private Context mContex;
    private Cursor mCursor;


    public Adapter_Facility_Payment(Context context , Cursor cursor)
    {
        mContex     =   context;
        mCursor     =   cursor;
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        public TextView mReciptDate , mRecptno , mRcptAmount ,mPaidMode ;

        public PendingFacList(@NonNull View itemView) {
            super(itemView);

            mReciptDate = itemView.findViewById(R.id.TxtRecyPayDate);
            mRecptno    = itemView.findViewById(R.id.TxtRecyPayRecNo);
            mRcptAmount = itemView.findViewById(R.id.TxtRecyPayaMT);
            mPaidMode   = itemView.findViewById(R.id.TxtPaymode);


        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.recovery_payment_cardviwer , parent , false);
        return new Adapter_Facility_Payment.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingFacList holder, int position)
    {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.mReciptDate.setText(mCursor.getString(0));
        holder.mRecptno.setText(mCursor.getString(1));
        holder.mRcptAmount.setText(mCursor.getString(2));
        holder.mPaidMode.setText(mCursor.getString(3));
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
