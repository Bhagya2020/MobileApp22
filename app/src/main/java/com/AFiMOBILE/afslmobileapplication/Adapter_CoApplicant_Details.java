package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.arrayGENREAL;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.arrayRELASTION;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.arraySECTOR;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.arrayAdaptrCOUNTRY;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.arrayAdapterPROVSTION;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.ArraysAdapterDistric;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.arrayAdapterAREA;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.arrayAdapter_occupa;

import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mADD1;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mADD2;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mADD3;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mADD4;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mAREA;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mBrithDay;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mCOUNTRY;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mDISTRICT;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mFULLNAME;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mINCOMESOURCE;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mNIC;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mOCUPATION;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mPHONE;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mPRIVSTION;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mRELSTYPE;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mSECTOR;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mSECVAL;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.rdoJoin;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.rdogur;
import static com.AFiMOBILE.afslmobileapplication.ClientGurnterDetails.mGENREAL;


public class Adapter_CoApplicant_Details extends RecyclerView.Adapter <Adapter_CoApplicant_Details.PendingAppViewHolder> {

    private Context mContex;
    private Cursor mCursor;
    int row_index = -1;
    public RadioButton RdoJoin , RdoGur;


    public Adapter_CoApplicant_Details(Context context , Cursor cursor)
    {
        mContex =   context;
        mCursor =   cursor;
    }

    public class PendingAppViewHolder extends RecyclerView.ViewHolder
    {
        public TextView TxtClType;
        public TextView TxtNic;
        public TextView TxtName;
        public Button mEdit;

        public PendingAppViewHolder(@NonNull View itemView) {
            super(itemView);

            TxtClType   =   itemView.findViewById(R.id.textview_cltype);
            TxtNic      =   itemView.findViewById(R.id.textview_nic);
            TxtName     =   itemView.findViewById(R.id.textview_clname);
            mEdit       =   itemView.findViewById(R.id.btnedit);

        }
    }

    @NonNull
    @Override
    public PendingAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.coapplicatin_details_recycle , parent , false);
        return  new Adapter_CoApplicant_Details.PendingAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAppViewHolder holder, int position)
    {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        String mCLTYPE          = mCursor.getString(0) ;
        final String mCLNIC     = mCursor.getString(1);
        String mCLNAME          = mCursor.getString(2);

        holder.TxtClType.setText(mCLTYPE);
        holder.TxtNic.setText(mCLNIC);
        holder.TxtName.setText(mCLNAME);

        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SqlliteCreateLeasing sqlliteCreateLeasing = new SqlliteCreateLeasing(mContex);
                SQLiteDatabase sqLiteDatabase = sqlliteCreateLeasing.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM LE_CO_CL_DATA WHERE CO_NIC = '" + mCLNIC + "'" , null );
                if (cursor.getCount() != 0)
                {
                    cursor.moveToFirst();

                    Toast.makeText(mContex,cursor.getString(1), Toast.LENGTH_SHORT).show();

                    if (cursor.getString(1) == "Gurnteers")
                    {
                        rdogur.setChecked(true);
                    }
                    else
                    {
                        rdoJoin.setChecked(false);
                    }

                    if (cursor.getString(1) == "Join Client")
                    {
                        rdogur.setChecked(false);
                    }
                    else
                    {
                        rdoJoin.setChecked(true);
                    }

                    mNIC.setText(cursor.getString(2)) ;
                    mFULLNAME.setText(cursor.getString(4));
                    mADD1.setText(cursor.getString(5));
                    mADD2.setText(cursor.getString(6));
                    mADD3.setText(cursor.getString(7));
                    mADD4.setText(cursor.getString(8));
                    mPHONE.setText(cursor.getString(15));
                    mBrithDay.setText(cursor.getString(13));
                    mOCUPATION.setText(arrayAdapter_occupa.getPosition(cursor.getString(18)));
                    mINCOMESOURCE.setText(cursor.getString(19));
                    mSECVAL.setText(cursor.getString(10));
                    mGENREAL.setSelection(arrayGENREAL.getPosition(cursor.getString(9)));
                    mRELSTYPE.setSelection(arrayRELASTION.getPosition(cursor.getString(25)));
                    mSECTOR.setSelection(arraySECTOR.getPosition(cursor.getString(17)));
                    mCOUNTRY.setSelection(arrayAdaptrCOUNTRY.getPosition(cursor.getString(26)));
                    mPRIVSTION.setSelection(arrayAdapterPROVSTION.getPosition(cursor.getString(27)));
                    mDISTRICT.setSelection(ArraysAdapterDistric.getPosition(cursor.getString(28)));
                    mAREA.setSelection(arrayAdapterAREA.getPosition(cursor.getString(29)));

                }
                //cursor.close();
                //sqLiteDatabase.close();
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
