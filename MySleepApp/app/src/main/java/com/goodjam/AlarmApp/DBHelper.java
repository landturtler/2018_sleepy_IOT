package com.goodjam.AlarmApp;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by hyun on 2017. 1. 4..
 */
public class DBHelper extends SQLiteOpenHelper {

    DBHelper(Context context){
        super(context,"Alarm",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alarm("
                + "sunday int not null,"
                + "monday int not null,"
                + "tuesday int not null,"
                + "wednesday int not null,"
                + "thursday int not null,"
                + "friday int not null,"
                + "saturday int not null,"
                + "hour int not null,"
                + "minute int not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS device";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert(int sunday, int monday, int tuesday , int wednseday , int thursday , int friday, int saturday , int hour , int minute) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        Log.e("e","WRITE");
        db.execSQL("INSERT INTO Alarm VALUES('"+sunday+"', '" + monday + "', " + tuesday + ", '" + wednseday + "', '"+thursday+"', '"+friday+"', '"+saturday+"', '"+hour+"', '"+minute+"');");
        db.close();

        Log.e("e","WRITE OK");
    }

//    public void update(String item, int price) {
//        SQLiteDatabase db = getWritableDatabase();
//        // 입력한 항목과 일치하는 행의 가격 정보 수정
//        db.execSQL("UPDATE ALARM SET price=" + price + " WHERE item='" + item + "';");
//        db.close();
//    }

    public void delete() {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM ALARM");
        db.close();
    }

    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM Alarm", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0) + " " + cursor.getInt(1) + " " + cursor.getInt(2) + " " + cursor.getInt(3) + " " + cursor.getInt(4) + " " + cursor.getInt(5) + " " + cursor.getInt(6) + " " + cursor.getInt(7) + " " + cursor.getInt(8) + "\n";

        }

        Log.e("e",result);

        return result;
    }


}