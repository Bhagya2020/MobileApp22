package com.AFiMOBILE.afslmobileapplication;

import android.content.Context;
import android.telephony.SmsManager;

public class SendSms
{
    private Context mContex;

    public SendSms (Context context)
    {
        mContex = context;
    }

    public void SendSmsUser (String Phoneno , String mMassage)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(Phoneno,null,mMassage,null,null);
    }
}
