package com.goodjam.AlarmApp;

/**
 * Created by SungJun on 2017-01-07.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private DBHelper DB;
    private String str;
    private String[] column = null;
    private int index=0;

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter(Context context) {
        this.context=context;
        DB = new DBHelper(context);
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    public void reload(){index=0;}

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.alarm_on_off) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2) ;
        TextView Str1 = (TextView) convertView.findViewById(R.id.textView3);
        TextView Str2 = (TextView) convertView.findViewById(R.id.textView4);
        TextView Str3 = (TextView) convertView.findViewById(R.id.textView5);
        TextView Str4 = (TextView) convertView.findViewById(R.id.textView6);
        TextView Str5 = (TextView) convertView.findViewById(R.id.textView7);
        TextView Str6 = (TextView) convertView.findViewById(R.id.textView8);
        TextView Str7 = (TextView) convertView.findViewById(R.id.textView9);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        //

        str = DB.getResult();
        if(!str.isEmpty()) {
            column = str.split("\n");
        }

        int i = column.length;
        Log.e("e","HELLO");
        Log.e("e","Index = "+index+" "+i);
        String[] row = column[index].split(" ");

        if(row[0].equals("1")) Str1.setTextColor(Color.parseColor("#80DEEA"));
        if(row[1].equals("1")) Str2.setTextColor(Color.parseColor("#80DEEA"));
        if(row[2].equals("1")) Str3.setTextColor(Color.parseColor("#80DEEA"));
        if(row[3].equals("1")) Str4.setTextColor(Color.parseColor("#80DEEA"));
        if(row[4].equals("1")) Str5.setTextColor(Color.parseColor("#80DEEA"));
        if(row[5].equals("1")) Str6.setTextColor(Color.parseColor("#80DEEA"));
        if(row[6].equals("1")) Str7.setTextColor(Color.parseColor("#80DEEA"));

        if(index < i) {
            index++;
        }
        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getHour());
        descTextView.setText(listViewItem.getMinute());
        Str1.setText(listViewItem.getStr1());
        Str2.setText(listViewItem.getStr2());
        Str3.setText(listViewItem.getStr3());
        Str4.setText(listViewItem.getStr4());
        Str5.setText(listViewItem.getStr5());
        Str6.setText(listViewItem.getStr6());
        Str7.setText(listViewItem.getStr7());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable icon, String hour, String minute,String str1,String str2,String str3,String str4,String str5,String str6, String str7) {
        ListViewItem item = new ListViewItem();

        item.setIcon(icon);
        item.setHour(hour);
        item.setMinute(minute);
        item.setStr1(str1);
        item.setStr2(str2);
        item.setStr3(str3);
        item.setStr4(str4);
        item.setStr5(str5);
        item.setStr6(str6);
        item.setStr7(str7);



        listViewItemList.add(item);
    }
}

