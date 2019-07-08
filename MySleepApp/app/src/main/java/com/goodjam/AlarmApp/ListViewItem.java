package com.goodjam.AlarmApp;

/**
 * Created by SungJun on 2017-01-07.
 */

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable iconDrawable ;
    private String hour ;
    private String minute ;
    private String str1;
    private String str2;
    private String str3;
    private String str4;
    private String str5;
    private String str6;
    private String str7;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setHour(String title) {
        hour = title+"시" ;
    }
    public void setMinute(String desc) {
        minute = desc+"분" ;
    }
    public String getStr1() {
        return "일";
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return "월";
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getStr3() {
        return "화";
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }

    public String getStr4() {
        return "수";
    }

    public void setStr4(String str4) {
        this.str4 = str4;
    }

    public String getStr5() {
        return "목";
    }

    public void setStr5(String str5) {
        this.str5 = str5;
    }

    public String getStr6() {
        return "금";
    }

    public void setStr6(String str6) {
        this.str6 = str6;
    }

    public String getStr7() {
        return "토";
    }

    public void setStr7(String str7) {
        this.str7 = str7;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getHour() {
        return this.hour ;
    }
    public String getMinute() {
        return this.minute ;
    }
}

