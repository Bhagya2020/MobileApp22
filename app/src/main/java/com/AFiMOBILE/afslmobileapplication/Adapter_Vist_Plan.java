package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Vist_Plan extends RecyclerView.Adapter<Adapter_Vist_Plan.PendingFacList> {

    private Context mContex;
    private Cursor mCursor;
    private String mType;

    public Adapter_Vist_Plan (Context context , Cursor cursor , String Type)
    {
        mContex = context;
        mCursor = cursor;
        mType   = Type;
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        public TextView mFacilityNo , mClname , mAdders ;
        public ImageButton mLoadFacilityDetails;

        public PendingFacList(@NonNull View itemView) {
            super(itemView);

            mFacilityNo =   itemView.findViewById(R.id.TxtFacilityno);
            mClname     =   itemView.findViewById(R.id.Txtclname);
            mAdders     =   itemView.findViewById(R.id.TxTADDERS);

            mLoadFacilityDetails = itemView.findViewById(R.id.dataLoad);
        }
    }


    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.recovery_visit_card_view , parent , false);
        return new Adapter_Vist_Plan.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingFacList holder, int position) {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.mFacilityNo.setText(mCursor.getString(0));
        holder.mClname.setText(mCursor.getString(1));
        holder.mAdders.setText(mCursor.getString(2));

        holder.mLoadFacilityDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mType.equals("Distance"))
                {
                    Intent intent_distance = new Intent("android.intent.action.Recovery_Distance_Update");
                    intent_distance.putExtra("FACNO" , holder.mFacilityNo.getText());
                    mContex.startActivity(intent_distance);
                }
                else
                {
                    Intent intent_Docview = new Intent("android.intent.action.Recovery_Facility_Details_View");
                    intent_Docview.putExtra("FACNO" , holder.mFacilityNo.getText());
                    intent_Docview.putExtra("TYPE" , "NORMAL");
                    mContex.startActivity(intent_Docview);
                }
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
