package com.AFiMOBILE.afslmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;

import static com.AFiMOBILE.afslmobileapplication.Recovery_Search_Facility.mRecoverySearchSelectFacilityNo;

public class Adapter_Recover_FacList extends RecyclerView.Adapter<Adapter_Recover_FacList.PendingFacList>
{
    private Context mContex;
    private Cursor mCursor;
    public SqlliteCreateRecovery sqlliteCreateRecovery_Adapter_Recovery;
    public String mGeoLocastionAdders , mType , LoginUser , LoginDate , LoginBranch , LoginUserName ;
    int row_index = -1;


    public Adapter_Recover_FacList(Context context , Cursor cursor , String Type)
    {
        mContex =   context;
        mCursor =   cursor;
        mType   =   Type;
        sqlliteCreateRecovery_Adapter_Recovery = new SqlliteCreateRecovery(mContex);
        //=== Create Connection And Golble Varible

        GlobleClassDetails globleClassDetails = (GlobleClassDetails) mContex.getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        LoginUserName = globleClassDetails.getOfficerName();
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        public final TextView mFacNo , mClfullyName , mClfullyName2 , mArraysAmt , mArrayRental , mAdders  , mAdders2 , mAssetType , mVehNo , mLastPaidAmt , mLastPaidDate;
        public ImageButton mBtnCall , mBtnGeoTag , mBtnViewData , mReceipt , mVisitPaln;
        public RelativeLayout relativeLayout_check;

        public PendingFacList(@NonNull View itemView)
        {
            super(itemView);

            mFacNo              =   itemView.findViewById(R.id.TxtRecoveryFacno);
            mClfullyName        =   itemView.findViewById(R.id.TxtREcoveryFullyName);
            mClfullyName2       =   itemView.findViewById(R.id.TxtRecoveryFullyName2);
            mArraysAmt          =   itemView.findViewById(R.id.TxtRecoveryArrAmt);
            mArrayRental        =   itemView.findViewById(R.id.TxtRecoveryNoRnt);
            mAdders             =   itemView.findViewById(R.id.TxtAdders);
            mAdders2            =   itemView.findViewById(R.id.TxtAdders2);
            mAssetType          =   itemView.findViewById(R.id.TxtRecoveryAsset);
            mVehNo              =   itemView.findViewById(R.id.TxtRecoveryVehNo);
            mLastPaidAmt        =   itemView.findViewById(R.id.TxtlastPaidAmt);
            mLastPaidDate       =   itemView.findViewById(R.id.TxtlastPaidDate);

            mBtnCall            =   itemView.findViewById(R.id.ImgCall);
            mBtnGeoTag          =   itemView.findViewById(R.id.ImgGeoTag);
            mBtnViewData        =   itemView.findViewById(R.id.ImgViewdoc);
            mReceipt            =   itemView.findViewById(R.id.ImgvistReceipt);
            mVisitPaln          =   itemView.findViewById(R.id.ImgvistClient);

            relativeLayout_check    =   itemView.findViewById(R.id.facrelative);
        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.facilitylistview , parent , false);
        return new Adapter_Recover_FacList.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingFacList holder, final int position)
    {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.mFacNo.setText(mCursor.getString(0));
        mGeoLocastionAdders = mCursor.getString(6);

        if (mCursor.getString(1).length() <= 35)
        {
            holder.mClfullyName.setText(mCursor.getString(1));
        }
        else
        {
            holder.mClfullyName.setText(mCursor.getString(1).substring(0 , 35));
            holder.mClfullyName2.setText(mCursor.getString(1).substring(36));
        }

        holder.mArraysAmt.setText(mCursor.getString(2));
        holder.mAdders.setText(mCursor.getString(3) + "," + mCursor.getString(4) );
        holder.mAdders2.setText( mCursor.getString(5) + "'" + mCursor.getString(6));
        holder.mAssetType.setText(mCursor.getString(7));
        holder.mVehNo.setText(mCursor.getString(8));
        holder.mLastPaidAmt.setText(mCursor.getString(9));
        holder.mLastPaidDate.setText(mCursor.getString(10));

        Double mRntArr = Double.parseDouble(mCursor.getString(11));
        DecimalFormat precision = new DecimalFormat("0.00");
        holder.mArrayRental.setText(precision.format(mRntArr));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                row_index = position;
                notifyDataSetChanged();

                mRecoverySearchSelectFacilityNo = holder.mFacNo.getText().toString();
            }
        });

        if(row_index==position){

            holder.itemView.setBackgroundColor(Color.parseColor("#732170"));
            holder.mBtnCall.setBackgroundColor(Color.parseColor("#e1d93d"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.relativeLayout_check.setBackgroundResource(R.drawable.recovery_recycleview);
            holder.mBtnCall.setBackgroundColor(Color.parseColor("#F6F682"));
            holder.mBtnGeoTag.setBackgroundColor(Color.parseColor("#F6F682"));
            holder.mBtnViewData.setBackgroundColor(Color.parseColor("#F6F682"));
            holder.mReceipt.setBackgroundColor(Color.parseColor("#F6F682"));
            holder.mVisitPaln.setBackgroundColor(Color.parseColor("#F6F682"));
        }

        //==== Load Receipt Details Enter
        holder.mReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_AssetVerification = new Intent("android.intent.action.Recovery_Collection_Entry_View");
                intent_AssetVerification.putExtra("FACNO" , holder.mFacNo.getText().toString());
                mContex.startActivity(intent_AssetVerification);
            }
        });


        //=== Add Visit Plan Data
        holder.mVisitPaln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                androidx.appcompat.app.AlertDialog.Builder builderdelete = new androidx.appcompat.app.AlertDialog.Builder(mContex);
                builderdelete.setTitle("AFiMobile-Leasing");
                builderdelete.setMessage("Are you sure to Add Visit this Facility?");
                builderdelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        String mFacilityNo = holder.mFacNo.getText().toString();
                        SQLiteDatabase sqLiteDatabase_addVistPlan  = sqlliteCreateRecovery_Adapter_Recovery.getWritableDatabase();
                        ContentValues contentValues_isert_plan = new ContentValues();
                        contentValues_isert_plan.put("facility_no" , mFacilityNo);
                        contentValues_isert_plan.put("user_id" , LoginUser);
                        contentValues_isert_plan.put("plan_Date" , LoginDate);
                        contentValues_isert_plan.put("action_code" , "");
                        contentValues_isert_plan.put("plan_sts" , "");
                        sqLiteDatabase_addVistPlan.insert("recovery_visit_plan" , null , contentValues_isert_plan);
                    }
                });
                builderdelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(mContex,
                                "No Button Clicked",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builderdelete.create();
                dialog.show();
            }
        });


        //=== Holder Call Button Click
        holder.mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SQLiteDatabase sqLiteDatabase_PhoneNo = sqlliteCreateRecovery_Adapter_Recovery.getReadableDatabase();
                Cursor cursor_PhoneNo = sqLiteDatabase_PhoneNo.rawQuery("SELECT Mobile_No1 , Mobile_No2 FROM recovery_generaldetail WHERE Facility_Number = '" + holder.mFacNo.getText() + "'" , null);
                if (cursor_PhoneNo.getCount() != 0)
                {
                    cursor_PhoneNo.moveToFirst();
                    String MobileNo="";
                    if (cursor_PhoneNo.getString(0) != null)
                    {
                        MobileNo = cursor_PhoneNo.getString(0);
                    }
                    else
                    {
                        MobileNo = cursor_PhoneNo.getString(1);
                    }

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData( Uri.parse("tel:" + MobileNo));
                    mContex.startActivity(intent);
                }
            }
        });


        //==== Lock Facility change color
        SQLiteDatabase sqLiteDatabase_check_lock = sqlliteCreateRecovery_Adapter_Recovery.getReadableDatabase();
        Cursor cursor_check = sqLiteDatabase_check_lock.rawQuery("SELECT FAC_LOCK FROM nemf_form_updater WHERE FACILITY_NO = '" + mCursor.getString(0 ) + "'" , null);
        if (cursor_check.getCount() != 0)
        {
            cursor_check.moveToFirst();
            if (cursor_check.getString(0) != null)
            {
                if (cursor_check.getString(0).equals("Y"))
                {
                    holder.relativeLayout_check.setBackgroundResource(R.drawable.recovery_recy_lock);
                    holder.mBtnCall.setBackgroundColor(Color.parseColor("#ffb9ba"));
                    holder.mBtnGeoTag.setBackgroundColor(Color.parseColor("#ffb9ba"));
                    holder.mBtnViewData.setBackgroundColor(Color.parseColor("#ffb9ba"));
                    holder.mReceipt.setBackgroundColor(Color.parseColor("#ffb9ba"));
                    holder.mVisitPaln.setBackgroundColor(Color.parseColor("#ffb9ba"));
                }
                else
                {
                    holder.relativeLayout_check.setBackgroundResource(R.drawable.recovery_recycleview);
                    holder.mBtnCall.setBackgroundColor(Color.parseColor("#F6F682"));
                    holder.mBtnGeoTag.setBackgroundColor(Color.parseColor("#F6F682"));
                    holder.mBtnViewData.setBackgroundColor(Color.parseColor("#F6F682"));
                    holder.mReceipt.setBackgroundColor(Color.parseColor("#F6F682"));
                    holder.mVisitPaln.setBackgroundColor(Color.parseColor("#F6F682"));
                }
            }
            else
            {
                holder.relativeLayout_check.setBackgroundResource(R.drawable.recovery_recycleview);
                holder.mBtnCall.setBackgroundColor(Color.parseColor("#F6F682"));
                holder.mBtnGeoTag.setBackgroundColor(Color.parseColor("#F6F682"));
                holder.mBtnViewData.setBackgroundColor(Color.parseColor("#F6F682"));
                holder.mReceipt.setBackgroundColor(Color.parseColor("#F6F682"));
                holder.mVisitPaln.setBackgroundColor(Color.parseColor("#F6F682"));
            }
        }
        else
        {
            holder.relativeLayout_check.setBackgroundResource(R.drawable.recovery_recycleview);
            holder.mBtnCall.setBackgroundColor(Color.parseColor("#F6F682"));
            holder.mBtnGeoTag.setBackgroundColor(Color.parseColor("#F6F682"));
            holder.mBtnViewData.setBackgroundColor(Color.parseColor("#F6F682"));
            holder.mReceipt.setBackgroundColor(Color.parseColor("#F6F682"));
            holder.mVisitPaln.setBackgroundColor(Color.parseColor("#F6F682"));
        }

        // Holder Doc View Button
        holder.mBtnViewData.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent_Docview = new Intent("android.intent.action.Recovery_Facility_Details_View");
                intent_Docview.putExtra("FACNO" , holder.mFacNo.getText());
                intent_Docview.putExtra("TYPE" , mType);
                mContex.startActivity(intent_Docview);
            }
        });


        holder.mBtnGeoTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_Docview = new Intent("android.intent.action.Recovery_location_Map");
                intent_Docview.putExtra("Adders1" , mGeoLocastionAdders);
                mContex.startActivity(intent_Docview);
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
