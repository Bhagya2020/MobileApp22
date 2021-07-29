package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;

import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Adapter_PendingApplication extends RecyclerView.Adapter <Adapter_PendingApplication.PendingAppViewHolder> {
    private Context mContex;
    private Cursor mCursor;
    int row_index = -1;

    public Adapter_PendingApplication(Context context , Cursor cursor)
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
        final String AppStage = mCursor.getString(4);
        String mClname  =   mCursor.getString(1);
        String mFacamt  =   mCursor.getString(2);
        String InpAppno =   mCursor.getString(0);
        String mNIcno   =   mCursor.getString(3) ;


        holder.TxtAppno.setText(mAppno);
        holder.TxtClname.setText(mClname);
        holder.TxtNic.setText(mNIcno);
        holder.TxtAmt.setText(mFacamt);
        holder.itemView.setTag(InpAppno);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                row_index = postion;

                Application.mApp.setText(holder.TxtAppno.getText());
                notifyDataSetChanged();

                //=== Check Select App Stage
                SqlliteCreateLeasing sqlliteCreateLeasing_Pending = new SqlliteCreateLeasing(mContex);
                SQLiteDatabase sqLiteDatabase_Pending = sqlliteCreateLeasing_Pending.getReadableDatabase();
                Cursor cursor_Pending = sqLiteDatabase_Pending.rawQuery("SELECT AP_STAGE FROM LE_APPLICATION WHERE APPLICATION_REF_NO = '" + holder.TxtAppno.getText() + "'" , null);
                if (cursor_Pending.getCount()!= 0)
                {
                    cursor_Pending.moveToFirst();
                    String CheckAppStage = cursor_Pending.getString(0);

                    if (CheckAppStage.equals("001"))
                    {
                        Application.btnModifyData.setEnabled(false);
                        Application.btnmDelete.setEnabled(false);
                        Application.btnPoReq.setEnabled(false);
                        Application.btnDoc.setEnabled(false);

                        Application.btnModifyData.setBackgroundResource(R.drawable.normalbuttondisable);
                        Application.btnmDelete.setBackgroundResource(R.drawable.normalbuttondisable);
                        Application.btnPoReq.setBackgroundResource(R.drawable.normalbuttondisable);
                        Application.btnDoc.setBackgroundResource(R.drawable.normalbuttondisable);

                    }
                    else
                    {
                        Application.btnModifyData.setEnabled(true);
                        Application.btnmDelete.setEnabled(true);
                        Application.btnPoReq.setEnabled(true);
                        Application.btnDoc.setEnabled(true);

                        Application.btnModifyData.setBackgroundResource(R.drawable.normalbutton);
                        Application.btnmDelete.setBackgroundResource(R.drawable.normalbutton);
                        Application.btnPoReq.setBackgroundResource(R.drawable.normalbutton);
                        Application.btnDoc.setBackgroundResource(R.drawable.normalbutton);
                    }
                }
                cursor_Pending.close();
                sqLiteDatabase_Pending.close();
                sqlliteCreateLeasing_Pending.close();
            }
        });

        if (AppStage.equals("001"))
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#e54e10"));
        }
        else
        {
            if(row_index==postion)
            {
                holder.itemView.setBackgroundColor(Color.parseColor("#732170"));
            }
            else
            {
                holder.itemView.setBackgroundColor(Color.parseColor("#E1BEE7"));
            }

            //holder.itemView.setBackgroundColor(Color.parseColor("#E1BEE7"));
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
