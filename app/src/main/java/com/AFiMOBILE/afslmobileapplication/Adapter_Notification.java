package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Notification extends RecyclerView.Adapter <Adapter_Notification.Notification_view_holder>
{
    private Context mContex;
    private Cursor mCursor;

    public Adapter_Notification (Context context , Cursor cursor)
    {
        mContex = context;
        mCursor = cursor;
    }

    public class Notification_view_holder extends RecyclerView.ViewHolder
    {
        TextView TxtNotMsg;
        TextView TxtClname;
        TextView TxtAppno;
        TextView TxtAmt;

        public Notification_view_holder(@NonNull View itemView) {
            super(itemView);

            TxtNotMsg   =   itemView.findViewById(R.id.txtshowNotification);
            TxtClname   =   itemView.findViewById(R.id.txtclname);
            TxtAppno    =   itemView.findViewById(R.id.txtappno);
            TxtAmt      =   itemView.findViewById(R.id.txtamt);
        }
    }

    @NonNull
    @Override
    public Notification_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.view_data_syns , parent , false);
        return new Notification_view_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Notification_view_holder holder, int position)
    {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        String mMsg     =   mCursor.getString(0);
        String mClname  =   mCursor.getString(1);
        String mAppno   =   mCursor.getString(2);
        String mAmt     =   mCursor.getString(3);

        holder.TxtNotMsg.setText(mMsg);
        holder.TxtClname.setText(mClname);
        holder.TxtAppno.setText(mAppno);
        holder.TxtAmt.setText(mAmt);

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
