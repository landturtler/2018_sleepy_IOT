package com.goodjam.AlarmApp;

/**
 * Created by SungJun on 2017-01-07.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class AlarmSelect extends Activity {

    private ListViewAdapter adapter;
    private ListView listView;
    private ImageButton btn;
    private DBHelper DB;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=savedInstanceState;
        setContentView(R.layout.activity_list);

        listView = (ListView)findViewById(R.id.listview);
        adapter = new ListViewAdapter(this);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        DB = new DBHelper(getApplicationContext());
        String str = DB.getResult();

        if(!str.isEmpty()) {

            String[] column = str.split("\n");

            for (int i = 0; i < column.length; i++) {
                String[] row = column[i].split(" ");
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.clock_size), row[7], row[8], row[0], row[1], row[2], row[3], row[4], row[5], row[6]);
            }
        }

    }

    public void Alarm_add(View view){
        Intent intent = new Intent(this,AlarmSetting.class);
        startActivity(intent);
    }
    public void deleteDB(View view){
        DB.delete();
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}

