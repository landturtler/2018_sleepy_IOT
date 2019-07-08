package com.goodjam.AlarmApp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class MyService extends Service {
    MediaPlayer mediaPlayer = null;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
            Log.i("Music", "노래 만듬");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        super.onDestroy();
    }
    public void stopgogo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent2 = new Intent("com.example.sungjun.alarmapp.MyService");
                while(true){
                    try{
                        Thread.sleep(200);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    DB_Chart DB = new DB_Chart("/Walk.txt",false);
                    ArrayList<String> str = DB.receiveDB();
                    if(str.size()!=0){
                        int now = Integer.parseInt(str.get(str.size() - 1));
                        Log.e("E", "NOW :" + now);
                        if (now > 10) {
                            stopService(intent2);
                            return;
                        }
                    }
                }
            }
        }).start();
    }
}
