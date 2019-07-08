package com.goodjam.AlarmApp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by SungJun on 2017-01-02.
 */

public class AlarmSetting extends Activity {
    private ToggleButton _toggleSun, _toggleMon, _toggleTue, _toggleWed, _toggleThu, _toggleFri, _toggleSat;
    //    DB_Alarm DB;
    int year, month, day, hour, minute;
    GregorianCalendar cal;
    AlarmManager alarm;
    int dbhour,dbmin;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmsetting);
        DB = new DBHelper(this);
        _toggleSun = (ToggleButton) findViewById(R.id.toggle_sun);
        _toggleMon = (ToggleButton) findViewById(R.id.toggle_mon);
        _toggleTue = (ToggleButton) findViewById(R.id.toggle_tue);
        _toggleWed = (ToggleButton) findViewById(R.id.toggle_wed);
        _toggleThu = (ToggleButton) findViewById(R.id.toggle_thu);
        _toggleFri = (ToggleButton) findViewById(R.id.toggle_fri);
        _toggleSat = (ToggleButton) findViewById(R.id.toggle_sat);

        cal = new GregorianCalendar();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        Log.i("HelloAlarmActivity", cal.getTime().toString());
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(AlarmSetting.this, timeSetListener, hour, minute, false).show();
            }
        });
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 0);
            Log.i("HelloAlarmActivity", cal.getTime().toString());

        }
    };

    public void onRegist(View v) {
        alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AlarmSetting.this, AlarmReceiver.class);
        PendingIntent pender = PendingIntent.getBroadcast(AlarmSetting.this, 0, intent, 0);
        alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pender);
        Log.i("sadf", "알람 넘어감");
        Toast.makeText(AlarmSetting.this, "Alarm Sending!", Toast.LENGTH_LONG).show();
        Log.i("HelloAlarmActivity", cal.getTime().toString());

        fileInput();
        finish();
    }
    public void fileInput(){
        boolean[] weekDay = { false, _toggleSun.isChecked(), _toggleMon.isChecked(), _toggleTue.isChecked(), _toggleWed.isChecked(),
                _toggleThu.isChecked(), _toggleFri.isChecked(), _toggleSat.isChecked() };
        int[] week = new int[7];
        dbhour = cal.get(Calendar.HOUR_OF_DAY);
        dbmin = cal.get(Calendar.MINUTE);
        for(int i=1;i< weekDay.length;i++){
            week[i-1] = (weekDay[i])? 1: 0;
        }
        DB.insert(week[0],week[1],week[2],week[3],week[4],week[5],week[6],dbhour,dbmin);

    }
    public void back(View view){
        finish();
    }

    public void onUnregist(View view) {
        Intent intent2 = new Intent("com.example.sungjun.alarmapp.MyService");
        intent2.setPackage("com.example.sungjun.alarmapp");
        stopService(intent2);
    }
}
