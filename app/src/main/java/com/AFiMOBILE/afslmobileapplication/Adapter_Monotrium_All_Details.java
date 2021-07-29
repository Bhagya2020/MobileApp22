package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;

public class Adapter_Monotrium_All_Details extends RecyclerView.Adapter<Adapter_Monotrium_All_Details.PendingFacList> {


    private Context mContex;
    private Cursor mCursor;

    public Adapter_Monotrium_All_Details(Context context , Cursor cursor)
    {
        mContex     =   context;
        mCursor     =   cursor;
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        TextView mSerealNo ,  mFacilityno , mFacilityAount , mRentalDue , mRentalArrays , mLastPayDate , mLastPayamount , mAvaragePayment , mTotaalArrays , mSlletement ;
        Button mBtnPayment , mBtnNotepadi , mBtnCoapp , mBtnAllDetails;
        TableRow mbtntable;
        RelativeLayout mDataScreen;

        public PendingFacList(@NonNull View itemView) {
            super(itemView);

            mFacilityno         =   itemView.findViewById(R.id.txtfacilityno);
            mFacilityAount      =   itemView.findViewById(R.id.txtfacamount);
            mRentalDue          =   itemView.findViewById(R.id.txtrntmmature);
            mRentalArrays       =   itemView.findViewById(R.id.txtrntarrays);
            mLastPayDate        =   itemView.findViewById(R.id.txtlastpaydate);
            mLastPayamount      =   itemView.findViewById(R.id.txtlastpayamt);
            mAvaragePayment     =   itemView.findViewById(R.id.txtavgpayment);
            mTotaalArrays       =   itemView.findViewById(R.id.txttotarrays);
            mSlletement         =   itemView.findViewById(R.id.txttotsettlement);

            mBtnPayment         =   itemView.findViewById(R.id.btnpayment);
            mBtnNotepadi        =   itemView.findViewById(R.id.btnnotepaid);
            mBtnCoapp           =   itemView.findViewById(R.id.btnCodetails);
            mBtnAllDetails      =   itemView.findViewById(R.id.btnallDetails);

            mbtntable           =   itemView.findViewById(R.id.tablebutton);

            mDataScreen         =   itemView.findViewById(R.id.relativeetails);
        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.card_view_link_all_facility_details, parent , false);
        return new Adapter_Monotrium_All_Details.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacList holder, int position) {

        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");

        holder.mRentalArrays.setText(mCursor.getString(3));


        holder.mFacilityno.setText(mCursor.getString(0));

        if (mCursor.getString(1).length() != 0)
        {
            holder.mFacilityAount.setText(nf.format(Double.parseDouble(mCursor.getString(1)))  );
        }


        holder.mRentalDue.setText(mCursor.getString(2));
        holder.mLastPayDate.setText(mCursor.getString(4));

        if (mCursor.getString(5).length() != 0)
        {
            holder.mLastPayamount.setText(nf.format(Double.parseDouble(mCursor.getString(5))));
        }
        holder.mAvaragePayment.setText(mCursor.getString(6));

        if (mCursor.getString(7).length() != 0)
        {
            holder.mTotaalArrays.setText(nf.format(Double.parseDouble(mCursor.getString(7))));
        }

        if (mCursor.getString(8).length() != 0)
        {
            holder.mSlletement.setText(nf.format(Double.parseDouble(mCursor.getString(8))));
        }


        if (mCursor.getString(0).equals("TOTAL"))
        {
            Log.e("total" , "done");
            holder.mDataScreen.setBackgroundResource(R.drawable.recovery_recy_lock);
            holder.mFacilityno.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mFacilityAount.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mRentalDue.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mRentalArrays.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mLastPayDate.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mLastPayamount.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mAvaragePayment.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mTotaalArrays.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mSlletement.setBackgroundColor(Color.parseColor("#ffb9ba"));
        }
        else
        {
            holder.mDataScreen.setBackgroundResource(R.drawable.recovery_card_new);
            /*holder.mDataScreen.setBackgroundColor(Color.parseColor("#FFF"));
            holder.mFacilityno.setBackgroundColor(Color.parseColor("#FFF"));
            holder.mFacilityAount.setBackgroundColor(Color.parseColor("#FFF"));
            holder.mRentalDue.setBackgroundColor(Color.parseColor("#FFF"));
            holder.mRentalArrays.setBackgroundColor(Color.parseColor("#FFF"));
            holder.mLastPayDate.setBackgroundColor(Color.parseColor("#FFF"));
            holder.mLastPayamount.setBackgroundColor(Color.parseColor("#FFF"));
            holder.mAvaragePayment.setBackgroundColor(Color.parseColor("#FFF"));
            holder.mTotaalArrays.setBackgroundColor(Color.parseColor("#FFF"));
            holder.mSlletement.setBackgroundColor(Color.parseColor("#FFF"));*/
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
