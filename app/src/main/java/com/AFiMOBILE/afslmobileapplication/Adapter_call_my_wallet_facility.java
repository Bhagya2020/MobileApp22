package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class Adapter_call_my_wallet_facility extends RecyclerView.Adapter<Adapter_call_my_wallet_facility.PendingFacList> {


    private Context mContex;
    private Cursor mCursor;
    private SqlliteCreateRecovery sqlliteCreateRecovery_Adapter_Recovery;
    private TableRow mDataTable;

    public Adapter_call_my_wallet_facility (Context context , Cursor cursor)
    {
        mContex = context;
        mCursor = cursor;
        sqlliteCreateRecovery_Adapter_Recovery = new SqlliteCreateRecovery(mContex);
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        TextView mSerno ,  mFacilityno , mPtpdate , mActionCode;
        ImageButton BthInfromation;
        public PendingFacList(@NonNull View itemView) {
            super(itemView);

            mSerno          =   itemView.findViewById(R.id.Txtseralno);
            mFacilityno     =   itemView.findViewById(R.id.Txtfacilityno);
            mPtpdate        =   itemView.findViewById(R.id.Txtdateptp);
            mActionCode     =   itemView.findViewById(R.id.txtactioncode);
            BthInfromation  =   itemView.findViewById(R.id.Imginformastion);
            mDataTable      =    itemView.findViewById(R.id.tabledata);
        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.card_view_call_mywallet_details, parent , false);
        return new Adapter_call_my_wallet_facility.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacList holder, int position) {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.mSerno.setText(mCursor.getString(0));
        holder.mFacilityno.setText(mCursor.getString(1));
        holder.mPtpdate.setText(mCursor.getString(2));
        holder.mActionCode.setText(mCursor.getString(3));

        //==== Load Details View Screen
        holder.BthInfromation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_details = new Intent("android.intent.action.Recovery_Facility_Details_View");
                intent_details.putExtra("FACNO" , holder.mFacilityno.getText());
                intent_details.putExtra("TYPE" , "CALL");
                mContex.startActivity(intent_details);
            }
        });

        SQLiteDatabase sqLiteDatabase_getdata = sqlliteCreateRecovery_Adapter_Recovery.getReadableDatabase();
        Cursor cursor_getdata = sqLiteDatabase_getdata.rawQuery("SELECT * FROM nemf_form_updater WHERE FACILITY_NO = '" + mCursor.getString(1) + "' AND " +
                "PTP_BRANCH = 'BACK-OFFICE'" , null );
        if (cursor_getdata.getCount() != 0)
        {
            mDataTable.setBackgroundResource(R.drawable.recovery_recy_lock);
            holder.mSerno.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mFacilityno.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mPtpdate.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mActionCode.setBackgroundColor(Color.parseColor("#ffb9ba"));
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
