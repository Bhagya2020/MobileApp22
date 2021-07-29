package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Recover_CoDetails extends RecyclerView.Adapter <Adapter_Recover_CoDetails.PendingFacilityList>
{
    private Context mContex;
    private Cursor mCursor;

    public Adapter_Recover_CoDetails (Context context , Cursor cursor)
    {
        mContex = context;
        mCursor = cursor;
    }

    public class PendingFacilityList extends RecyclerView.ViewHolder
    {
        TextView mTxtFacno , mTxtCoType , mTxtFullyName , mTxtFullyName2 , mTxtAdders , mTxtNic , mTxtClode , mTxtGender , TxtPhoneNo , mTxtOcupation;

        public PendingFacilityList(@NonNull View itemView) {
            super(itemView);

            mTxtFacno           =       itemView.findViewById(R.id.TxtRecoveryFacno);
            mTxtCoType          =       itemView.findViewById(R.id.TxtClientTyoe);
            mTxtFullyName       =       itemView.findViewById(R.id.TxtREcoveryFullyName);
            mTxtFullyName2      =       itemView.findViewById(R.id.TxtRecoveryFullyName2);
            mTxtAdders          =       itemView.findViewById(R.id.TxtAdders);
            mTxtNic             =       itemView.findViewById(R.id.TxtGurNic);
            mTxtClode           =       itemView.findViewById(R.id.TxtGurClcode);
            mTxtGender          =       itemView.findViewById(R.id.TxtGurGender);
            TxtPhoneNo          =       itemView.findViewById(R.id.TxtGurPhoneNo);
            mTxtOcupation       =       itemView.findViewById(R.id.TxtGurOcupation);
        }
    }

    @NonNull
    @Override
    public PendingFacilityList onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.recovery_co_cardviwer , parent , false);
        return new Adapter_Recover_CoDetails.PendingFacilityList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacilityList holder, int position)
    {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.mTxtFacno.setText(mCursor.getString(0));
        holder.mTxtCoType.setText(mCursor.getString(1));

        if (mCursor.getString(2).length() <= 35)
        {
            holder.mTxtFullyName.setText(mCursor.getString(2));
        }
        else
        {
            holder.mTxtFullyName.setText(mCursor.getString(2).substring(0 , 35));
            holder.mTxtFullyName2.setText(mCursor.getString(2).substring(36));
        }

        holder.mTxtAdders.setText(mCursor.getString(3) + "," + mCursor.getString(4) + "," + mCursor.getString(5) + "," + mCursor.getString(6) );
        holder.mTxtNic.setText(mCursor.getString(7));
        holder.mTxtClode.setText(mCursor.getString(8));
        holder.mTxtGender.setText(mCursor.getString(9));
        holder.TxtPhoneNo.setText(mCursor.getString(10));
        holder.mTxtOcupation.setText(mCursor.getString(11));
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
