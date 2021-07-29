package com.AFiMOBILE.afslmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Travel_Distance_updte extends AppCompatActivity {

    private TextView mSelectDate;
    private Button mBtnUpdateData;
    private SqlliteCreateRecovery sqlliteCreateRecovery_get_data;
    private String LoginUser , LoginDate , LoginBranch;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_distance_update);


        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        mSelectDate     = (TextView)findViewById(R.id.TxtDateTraveld) ;
        mBtnUpdateData  = (Button)  findViewById(R.id.btnvisitupdate) ;

        sqlliteCreateRecovery_get_data = new SqlliteCreateRecovery(this);

        //=== Get Globle PHP Code
        GlobleClassDetails globleClassDetails = (GlobleClassDetails) getApplicationContext();
        LoginUser   =   globleClassDetails.getUserid();
        LoginDate   =   globleClassDetails.getLoginDate();
        LoginBranch =   globleClassDetails.getLoginBranch();
        //==== Get Select Calander date

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();

                String strdate = null;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                if (clickedDayCalendar != null) {
                    strdate = sdf.format(clickedDayCalendar.getTime());
                    mSelectDate.setText(strdate);
                }

            }
        });

        LoadEvent();

        mBtnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_visit_upload = new Intent("android.intent.Update_Travel_distance");
                intent_visit_upload.putExtra("INPUT-DATA" , mSelectDate.getText().toString());
                startActivity(intent_visit_upload);
            }
        });

    }


    private void LoadEvent()
    {
        /*
        List<EventDay> events = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_MONTH,1);
        events.add(new EventDay(calendar1, R.drawable.sample_three_icons));
        events.add(2021-02-25);

         */

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<EventDay> events = new ArrayList<>();
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);



        SQLiteDatabase sqLiteDatabase_load = sqlliteCreateRecovery_get_data.getReadableDatabase();
        Cursor cursor_load = sqLiteDatabase_load.rawQuery("SELECT visit_date FROM recovery_distance_data WHERE user_id = '" + LoginUser + "'" , null);
        if (cursor_load.getCount() != 0)
        {
            cursor_load.moveToFirst();
            Date date = null;
            do {
                try {
                    date = sdf.parse(cursor_load.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                events.add(new EventDay(cal, R.drawable.sample_three_icons));
                calendarView.setEvents(events);
            }while (cursor_load.moveToNext());
        }
    }

}
