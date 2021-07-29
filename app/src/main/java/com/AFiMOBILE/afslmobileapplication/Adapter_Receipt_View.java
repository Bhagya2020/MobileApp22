package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_Receipt_View extends RecyclerView.Adapter <Adapter_Receipt_View.PendingFacList>
{
    private Context mContex;
    private Cursor mCursor;

    public Adapter_Receipt_View(Context context , Cursor cursor)
    {
        mContex = context;
        mCursor = cursor;
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        public TextView mReciptDate , mReceiptNo , mReceiptAmount;
        public PendingFacList(@NonNull View itemView) {
            super(itemView);

            mReciptDate = itemView.findViewById(R.id.TxtReciptdate);
            mReceiptNo = itemView.findViewById(R.id.TxtReciptNo);
            mReceiptAmount = itemView.findViewById(R.id.TxtReciptamount);
        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.receipt_details_view , parent , false);
        return new Adapter_Receipt_View.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacList holder, int position) {

        if (!mCursor.moveToPosition(position))
        {
            return;
        }
        holder.mReciptDate.setText(mCursor.getString(0));
        holder.mReceiptNo.setText(mCursor.getString(1));
        holder.mReceiptAmount.setText(mCursor.getString(2));
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
