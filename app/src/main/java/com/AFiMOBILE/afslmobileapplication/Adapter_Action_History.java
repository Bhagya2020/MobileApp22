package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Action_History extends RecyclerView.Adapter <Adapter_Action_History.PendingFacList> {

    private Context mContex;
    private Cursor mCursor;

    public Adapter_Action_History(Context context , Cursor cursor)
    {
        mContex     =   context;
        mCursor     =   cursor;
    }

    public class PendingFacList extends RecyclerView.ViewHolder
    {
        public final TextView mActionDate , mActionOff , mActionCode , mActionComment;
        public PendingFacList(@NonNull View itemView) {
            super(itemView);


            mActionDate     =   itemView.findViewById(R.id.TxtActionDate);
            mActionOff      =   itemView.findViewById(R.id.TxtActionOfficer);
            mActionCode     =   itemView.findViewById(R.id.TxtActionCode);
            mActionComment  =   itemView.findViewById(R.id.TxtActionComment);
        }
    }

    @NonNull
    @Override
    public PendingFacList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContex);
        View view = inflater.inflate(R.layout.recovery_action_history_cardview, parent , false);
        return new Adapter_Action_History.PendingFacList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingFacList holder, int position)
    {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }

        holder.mActionDate.setText(mCursor.getString(0));
        holder.mActionOff.setText(mCursor.getString(1));
        holder.mActionCode.setText(mCursor.getString(2));
        holder.mActionComment.setText(mCursor.getString(3));


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
