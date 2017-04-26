package com.yoo.taek.blescanner_recorder.Utils;

import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eldbx on 2017-04-24.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_setting.db";
    private static final int DATABASE_VERSION = 1;

    /*
     *먼저 SQLiteOpenHelper클래스를 상속받은 DBHelper클래스가 정의 되어 있다. 데이터베이스 파일 이름은 "user_setting.db"가되고,
     *데이터베이스 버전은 1로 되어있다. 만약 데이터베이스가 요청되었는데 데이터베이스가 없으면 onCreate()를 호출하여 데이터베이스
     *파일을 생성해준다.
     */

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE setting (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "scan_period INTEGER," +
                "is_record INTEGER, " +
                "file_name TEXT, " +
                "is_auto_close INTEGER, " +
                "auto_close_time TEXT);");

        // 초기 설정 값: scan_period = 0(Low Latency), is_record = 0(false), file_name = scanResult, is_auto_close = 0(false), auto_close_time = 000000
        db.execSQL("INSERT INTO setting VALUES(null, '" +
                ScanSettings.SCAN_MODE_LOW_LATENCY + "', '" +
                Constants.RECORDING_SWITCH_OFF + "', '" +
                "" + "', '" +
                Constants.AUTO_CLOSE_SWITCH_OFF + "', '" +
                "000000" + "');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS setting");
        onCreate(db);
    }
}
