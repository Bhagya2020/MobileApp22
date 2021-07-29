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

public class Adapter_Callcenter_facDetails extends RecyclerView.Adapter<Adapter_Callcenter_facDetails.PendingFacList> {


    private Context mContex;
    private Cursor mCursor;
    private SqlliteCreateRecovery sqlliteCreateRecovery_Adapter_Recovery;
    private TableRow mDataTable;

    public Adapter_Callcenter_facDetails (Context context , Cursor cursor)
    {
        mContex = context;
        mCursor = cursor;
        sqlliteCreateRecovery_Adapter_Recovery = new SqlliteCreateRecovery(mContex);
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        TextView mFacilityno , mPtpdate , mBalamt , mSeriealNo;
        ImageButton BthInfromation;
        public PendingFacList(@NonNull View itemView) {
            super(itemView);


            mSeriealNo     =   itemView.findViewById(R.id.Txtseralno);
            mFacilityno     =   itemView.findViewById(R.id.Txtfacilityno);
            mPtpdate        =   itemView.findViewById(R.id.Txtdateptp);
            mBalamt         =   itemView.findViewById(R.id.Txtoutamt);
            BthInfromation  =   itemView.findViewById(R.id.Imginformastion);

            mDataTable  = itemView.findViewById(R.id.mdatatable) ;
        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.card_view_call_center_facility, parent , false);
        return new Adapter_Callcenter_facDetails.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacList holder, int position) {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        DecimalFormat nf = new DecimalFormat("###,###,###,##0.00");

        holder.mSeriealNo.setText(mCursor.getString(0));


        holder.mFacilityno.setText(mCursor.getString(1));
        holder.mPtpdate.setText(mCursor.getString(2));

        if (mCursor.getString(0).length() != 0)
        {
            holder.mBalamt.setText(nf.format(Double.parseDouble(mCursor.getString(3))));
        }

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
            holder.mSeriealNo.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mFacilityno.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mPtpdate.setBackgroundColor(Color.parseColor("#ffb9ba"));
            holder.mBalamt.setBackgroundColor(Color.parseColor("#ffb9ba"));
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
