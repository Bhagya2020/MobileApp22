package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Draft_Application extends RecyclerView.Adapter<Adapter_Draft_Application.PendingAppViewHolder> {

    private Context mContex;
    private Cursor mCursor;

    public Adapter_Draft_Application(Context context , Cursor cursor)
    {
        mContex =   context;
        mCursor =   cursor;
    }

    public class PendingAppViewHolder extends RecyclerView.ViewHolder
    {

        private TextView mNIC , mCL_NAME , m_ADDERS;
        private ImageButton mBtnCreateApp;



        public PendingAppViewHolder(@NonNull View itemView) {
            super(itemView);

            mNIC        =   itemView.findViewById(R.id.textview_nic) ;
            mCL_NAME    =   itemView.findViewById(R.id.textview_clname) ;
            m_ADDERS    =   itemView.findViewById(R.id.textview_adders) ;

            mBtnCreateApp   =  itemView.findViewById(R.id.ImgViewdoc);

        }
    }

    @NonNull
    @Override
    public PendingAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.draft_app_adapter , parent , false);
        return  new Adapter_Draft_Application.PendingAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAppViewHolder holder, int position) {


        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.mNIC.setText(mCursor.getString(0));
        holder.mCL_NAME.setText(mCursor.getString(1));
        holder.m_ADDERS.setText(mCursor.getString(2) + "," + mCursor.getString(3) + "," + mCursor.getString(3) + "," + mCursor.getString(4));



        holder.mBtnCreateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent modifyintent = new Intent("android.intent.action.CreateApplication");
                modifyintent.putExtra("ApplicationNo" , mCursor.getString(0));
                modifyintent.putExtra("Type" , "D");
                mContex.startActivity(modifyintent);
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
