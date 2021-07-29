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

import java.security.PrivateKey;
import java.util.PriorityQueue;

public class Adapter_Rmv_Update extends RecyclerView.Adapter <Adapter_Rmv_Update.PendingAppViewHolder>{

    private Context mContex;
    private Cursor mCursor;

    public Adapter_Rmv_Update(Context context , Cursor cursor)
    {
        mContex = context;
        mCursor = cursor;
    }

    public class PendingAppViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mRegno;
        private TextView mChassNo;
        private TextView mEngineNo;
        private Button mDocView;
        public PendingAppViewHolder(@NonNull View itemView) {
            super(itemView);

            mRegno      =   itemView.findViewById(R.id.txtREG_NO);
            mChassNo    =   itemView.findViewById(R.id.txtCHASSI_NO);
            mEngineNo   =   itemView.findViewById(R.id.txtENGINE_NO);
            mDocView    =   itemView.findViewById(R.id.btnviewdata) ;
        }
    }

    @NonNull
    @Override
    public PendingAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.rmv_update_data_cardview , parent , false);
        return new Adapter_Rmv_Update.PendingAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingAppViewHolder holder, int position)
    {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }


        final String mInpRegno    = mCursor.getString(0);
        final String mInpEngine   = mCursor.getString(1);
        final String mInpChassi   = mCursor.getString(2);

        holder.mRegno.setText(mInpRegno);
        holder.mEngineNo.setText(mInpEngine);
        holder.mChassNo.setText(mInpChassi);

        holder.mDocView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_rmv_upddate = new Intent("android.intent.action.Rmv_Facility_Update_View");
                intent_rmv_upddate.putExtra("REG_NO" , mInpRegno);
                intent_rmv_upddate.putExtra("CHS_NO" , mInpChassi);
                mContex.startActivity(intent_rmv_upddate);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor)
    {
        if (mCursor != null)
        {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null)
        {
            notifyDataSetChanged();
        }
    }



}
