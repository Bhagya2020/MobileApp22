package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;

import static com.AFiMOBILE.afslmobileapplication.Recovery_Search_Facility.mRecoverySearchSelectFacilityNo;

public class Adapter_Re_Finance_fac_list extends RecyclerView.Adapter<Adapter_Re_Finance_fac_list.PendingFacList>
{
    private Context mContex;
    private Cursor mCursor;

    public Adapter_Re_Finance_fac_list(Context context , Cursor cursor )
    {
        mContex =   context;
        mCursor =   cursor;
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        TextView mfacilityno , mSettlementamt , mclientName , mProduct , mVehocleNo , mFacilityAmount;
        ImageButton mSelect ;

        public PendingFacList(@NonNull View itemView)
        {
            super(itemView);

            mfacilityno         =   itemView.findViewById(R.id.TxtRecoveryFacno);
            mSettlementamt      =   itemView.findViewById(R.id.TxtRecoveryArrAmt);
            mclientName         =   itemView.findViewById(R.id.TxtREcoveryFullyName);
            mProduct            =   itemView.findViewById(R.id.TxtRecoveryproduct);
            mVehocleNo          =   itemView.findViewById(R.id.TxtRecoveryVehNo);
            mFacilityAmount     =   itemView.findViewById(R.id.TxtlastPaidAmt);

            mSelect             =   itemView.findViewById(R.id.Imgselect);
        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.re_finance_faclist , parent , false);
        return new Adapter_Re_Finance_fac_list.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingFacList holder, final int position)
    {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.mfacilityno.setText(mCursor.getString(0));
        holder.mSettlementamt.setText(mCursor.getString(1));
        holder.mclientName.setText(mCursor.getString(2));
        holder.mProduct.setText(mCursor.getString(3));
        holder.mVehocleNo.setText(mCursor.getString(4));
        holder.mFacilityAmount.setText(mCursor.getString(5));


        holder.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double NewfinaceAmt = 0.00 , mInhandBal = 0.00 , msettlement=0.00;
                Re_Finance_Check.mSettlement.setText(holder.mSettlementamt.getText().toString());


                if (Re_Finance_Check.mNewFinaance.getText().toString()!="")
                {
                    NewfinaceAmt = Double.parseDouble(Re_Finance_Check.mNewFinaance.getText().toString());
                }

                if (holder.mSettlementamt.getText().toString() != "")
                {
                    msettlement = Double.parseDouble(holder.mSettlementamt.getText().toString());
                }

                mInhandBal = NewfinaceAmt - msettlement;

                Re_Finance_Check.mBalances.setText(String.valueOf(msettlement));

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
