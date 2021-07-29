package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.AFiMOBILE.afslmobileapplication.SubmitApplication.mComplete;


public class Adapter_SubmitApplication extends RecyclerView.Adapter <Adapter_SubmitApplication.PendingAppViewHolder> {
    private Context mContex;
    private Cursor mCursor;
    int row_index = -1;

    public Adapter_SubmitApplication(Context context , Cursor cursor)
    {
        mContex = context;
        mCursor = cursor;
    }

    public class PendingAppViewHolder extends RecyclerView.ViewHolder{
        public TextView TxtAppno;
        public TextView TxtClname;
        public TextView TxtNic;
        public TextView TxtAmt;

        public PendingAppViewHolder(View itemView) {
            super(itemView);

            TxtAppno    =   itemView.findViewById(R.id.textview_appno);
            TxtClname   =   itemView.findViewById(R.id.textview_Clname);
            TxtNic      =   itemView.findViewById(R.id.textview_nic);
            TxtAmt      =   itemView.findViewById(R.id.textview_facamt);
        }
    }

    @Override
    public PendingAppViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.pending_app_layout_new , parent , false);
        return new PendingAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PendingAppViewHolder holder, final int postion)
    {
        if (!mCursor.moveToPosition(postion))
        {
            return;
        }

        final String mAppno   =   mCursor.getString(0);
        String mClname   =   mCursor.getString(1);
        String mFacamt   =   mCursor.getString(2);
        String mNic      =   mCursor.getString(3);
        String InpAppno  =   mCursor.getString(0);
        final String InpScreen =   mCursor.getString(4);

        holder.TxtAppno.setText(mAppno);
        holder.TxtClname.setText(mClname);
        holder.TxtNic.setText(mNic);
        holder.TxtAmt.setText(mFacamt);
        holder.itemView.setTag(InpAppno);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InpScreen.equals("SUBMIT"))
                {
                    SubmitApplication.mApp.setText(mAppno);
                    mComplete.setEnabled(true);
                    mComplete.setBackgroundResource(R.drawable.normalbutton);
                }

                if (InpScreen.equals("VISIT"))
                {
                    house_visit_application.mVISITSELAPPNO.setText(mAppno);
                }

                row_index = postion;
                notifyDataSetChanged();
            }
        });

        if(row_index==postion){

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
