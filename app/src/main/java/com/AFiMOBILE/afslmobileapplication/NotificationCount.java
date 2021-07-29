package com.AFiMOBILE.afslmobileapplication;

import android.view.View;
import android.widget.TextView;

import com.AFiMOBILE.afslmobileapplication.R;
import com.itextpdf.text.pdf.PRIndirectReference;

public class NotificationCount
{
    private TextView notificationNumber;

    public final int MAX_NUMBER = 99;
    private int notification_nouber_count=1;


    public NotificationCount(View view)
    {
        notificationNumber = view.findViewById(R.id.TxtCount);
    }

    public void Incress()
    {
        notification_nouber_count++;
        notificationNumber.setText(String.valueOf(notification_nouber_count));
    }
}
