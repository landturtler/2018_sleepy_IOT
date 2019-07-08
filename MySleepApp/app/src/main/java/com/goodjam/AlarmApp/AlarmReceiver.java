package com.goodjam.AlarmApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by SungJun on 2017-01-02.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    MediaPlayer mPlayer=null;
    public void onReceive(final Context context, final Intent intent)
    {

        try {
            Log.d(TAG, "onReceive: alarm recived");
            Intent intent2 = new Intent("com.example.sungjun.alarmapp.MyService");
            intent2.setPackage("com.example.sungjun.alarmapp");
            context.startService(intent2);
            Thread.sleep(10000);
            context.stopService(intent2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}