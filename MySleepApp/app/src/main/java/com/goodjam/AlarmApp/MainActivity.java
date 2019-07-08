package com.goodjam.AlarmApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {

    static int REQUEST_ENABLE_BT = 1;
    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothSocket mSocket;
    OutputStream mOutputStream;
    InputStream mInputStream;
    Set<BluetoothDevice> mDevices;
    byte[] readBuffer;
    int readBufferPositon;
    Thread mWorkerThread;
    String mDelimiter = "\n";
    int readBufferPosition;
    private DB_Chart DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 블루투스 어댑터 가져오기
        if(mBluetoothAdapter == null) {
            finish();
        }

        else {
            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else {
                selectDevice();
            }
        }

    }

    public void Alarm_set(View view){
        Intent intent = new Intent(this,AlarmSelect.class);
        startActivity(intent);
    }

    public void UserGraph(View v) {
        Intent intent = new Intent(this,GraphActivity.class);
        startActivity(intent);
    }
    void selectDevice() {
        mDevices = mBluetoothAdapter.getBondedDevices();
        final int mPairedDeviceCount = mDevices.size();

        if (mPairedDeviceCount == 0) {
            //  페어링 된 장치가 없는 경우
            finish();    // 어플리케이션 종료
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("밴드 선택");

        List<String> listItems = new ArrayList<String>();
        for (BluetoothDevice device : mDevices) {
            listItems.add(device.getName());
        }
        listItems.add("취소");    // 취소 항목 추가

        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == mPairedDeviceCount) {
                    // 연결할 장치를 선택하지 않고 '취소'를 누른 경우
                    //finish();
                } else {
                    // 연결할 장치를 선택한 경우
                    // 선택한 장치와 연결을 시도함
                    connectToSelectdDevice(items[item].toString());
                    sendData("1");
                }
            }
        });


        builder.setCancelable(false);    // 뒤로 가기 버튼 사용 금지
        AlertDialog alert = builder.create();
        alert.show();


    }

    BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;
        Set<BluetoothDevice> mDevices = mBluetoothAdapter.getBondedDevices();

        for(BluetoothDevice device : mDevices) {
            if(name.equals(device.getName())) {
                selectedDevice = device;
                break;
            }
        }
        return selectedDevice;
    }

    void connectToSelectdDevice(String selectedDeviceName) {
        BluetoothDevice mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // 소켓 생성
            mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);
            // RFCOMM 채널을 통한 연결
            mSocket.connect();

            // 데이터 송수신을 위한 스트림 열기
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();

            // 데이터 수신 준비
            //beginListenForData();
        }catch(Exception e) {
            // 블루투스 연결 중 오류 발생
            finish();   // 어플 종료
        }
    }
    void sendData(String msg) {
        msg += mDelimiter;    // 문자열 종료 표시

        try {
            mOutputStream.write(msg.getBytes());    // 문자열 전송
        } catch(Exception e) {
            // 문자열 전송 도중 오류가 발생한 경우.
            finish();    //  APP 종료
        }
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();

        readBuffer = new byte[1024] ;  //  수신 버퍼
        readBufferPositon = 0;        //   버퍼 내 수신 문자 저장 위치

        // 문자열 수신 쓰레드
        mWorkerThread = new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted()){

                    try {
                        int bytesAvailable = mInputStream.available();    // 수신 데이터 확인
                        if(bytesAvailable > 0) {                     // 데이터가 수신된 경우
                            byte[] packetBytes = new byte[bytesAvailable];
                            mInputStream.read(packetBytes);
                            for(int i=0 ; i<bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if(b == '\n') {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            Log.i("SEX",data);
                                            ArrayList<String> str = DB.receiveDB();

                                        }
                                    });
                                }
                                else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex) {
                        // 데이터 수신 중 오류 발생.
                        finish();
                    }
                }
            }
        });

        mWorkerThread.start();
    }

    private class CloseTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected Object doInBackground(Void... params) {
            try {
                try{mOutputStream.close();}catch(Throwable t){/*ignore*/}
                try{mInputStream.close();}catch(Throwable t){/*ignore*/}
                mSocket.close();
            } catch (Throwable t) {
                return t;
            }
            return null;
        }

    }

    public void doClose() {
        new CloseTask().execute();
    }

    public void Shutdown(View v){
        Intent intent = new Intent(this,ShutDown.class);
        startActivity(intent);
    }

    public void Mypage(View v){
        Intent intent = new Intent(this,MyPageActivity.class);
        startActivity(intent);
    }

}