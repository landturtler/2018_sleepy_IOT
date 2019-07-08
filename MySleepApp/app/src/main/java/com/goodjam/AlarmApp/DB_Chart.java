package com.goodjam.AlarmApp;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hyun on 2016. 12. 30..
 */
public class DB_Chart {

    private String PATH = Environment.getExternalStorageDirectory()+"";

    private static File file;
    private BufferedWriter bw;
    private BufferedReader br;

    public DB_Chart(String path,boolean end){
        PATH+=path;
        file = new File(PATH);

        Log.e("e",file.toString());

        if(!file.exists()){
            try {
                file.createNewFile();
                Log.e("MAKING","FILE CREATED");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bw = new BufferedWriter(new FileWriter(file,end));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertDB(String data){
        try {
            bw.write(data);
            Log.e("Insert Data", "Insert DB : "+data);
            bw.newLine();
//            bw.newLine();
            bw.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void delete(){
        file.delete();
    }

    public ArrayList<String> receiveDB(){
        ArrayList<String> data = new ArrayList<String>();

        String str;
        try{
            br = new BufferedReader(new FileReader(file));
            while((str = br.readLine()) != null){
                data.add(str);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return data;
    }

    public void removeDB() throws IOException {
        bw.close();
        br.close();
    }

}