package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.AFiMOBILE.afslmobileapplication.ManagerApprove.btnViewDetails;
import static com.AFiMOBILE.afslmobileapplication.ManagerApprove.mSelectAppno;

public class Adapter_MgrApprove_app extends RecyclerView.Adapter <Adapter_MgrApprove_app.PendingAppViewHolder> {

    private Context mContex;
    private Cursor mCursor;
    int row_index = -1;


    public Adapter_MgrApprove_app (Context context , Cursor cursor)
    {
        mContex = context;
        mCursor = cursor;
    }

    public class PendingAppViewHolder extends RecyclerView.ViewHolder{

        public TextView mTxtAppno;
        public TextView mTxtClname;
        public TextView mTxtNic;
        public TextView mTxtAmt;
        public TextView mTxtMKofficer;


        public PendingAppViewHolder(@NonNull View itemView) {
            super(itemView);

            mTxtAppno       =   itemView.findViewById(R.id.textview_appno);
            mTxtClname      =   itemView.findViewById(R.id.textview_Clname)  ;
            mTxtNic         =   itemView.findViewById(R.id.textview_nic);
            mTxtAmt         =   itemView.findViewById(R.id.textview_amount);
            mTxtMKofficer   =   itemView.findViewById(R.id.textview_mkofficer);
        }
    }

    @NonNull
    @Override
    public PendingAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.pending_po_approve_layout , parent , false);
        return new Adapter_MgrApprove_app.PendingAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAppViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        final String mAppno   =   mCursor.getString(0);
        String mClname  =   mCursor.getString(1) ;
        String mNic     =   mCursor.getString(2);
        String mAmt     =   mCursor.getString(3);
        String mMkoff   =   mCursor.getString(4);

        holder.mTxtAppno.setText(mAppno);
        holder.mTxtClname.setText(mClname);
        holder.mTxtNic .setText(mNic);
        holder.mTxtAmt.setText(mAmt);
        holder.mTxtMKofficer.setText(mMkoff);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mSelectAppno.setText(mAppno);
                row_index = position;
                notifyDataSetChanged();

                //=== Check Data =====
                btnViewDetails.setEnabled(true);
                btnViewDetails.setBackgroundResource(R.drawable.normalbutton);
            }
        });


        if(row_index==position){

            holder.itemView.setBackgroundColor(Color.parseColor("#732170"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#E1BEE7"));
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
