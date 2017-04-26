package com.yoo.taek.blescanner_recorder.Utils;

import android.bluetooth.le.ScanSettings;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by eldbx on 2017-04-24.
 */

public class DBUtils {
    public DBHelper helper;
    public static SQLiteDatabase db;

    // user setting
    public static int isRecord, isAutoClose, scanPeriod;
    public static String fileName, autoCloseTime;

    private String TAG = "DBUtils";

    public DBUtils(Context context) {
        // Database
        helper = new DBHelper(context);
        try {
            db = helper.getWritableDatabase();
            // Log.d(TAG, "getWritableDatabase() 호출");
            // 데이터베이스 객체를  얻기 위하여 getWritableDatabase()를 호출
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
            // Log.d(TAG, "getReadableDatabase() 호출");
        }
        Cursor cursor = db.rawQuery("SELECT scan_period, is_record, file_name, is_auto_close, auto_close_time FROM setting " +
                "WHERE _id='" + 1 + "';", null);
        /*
            public int isRecord, isAutoClose, scanPeriod;
            public String fileName, autoCloseTime;
         */
        if (cursor.getCount() == 0) {
            db.execSQL("INSERT INTO setting VALUES(null, '" +
                    ScanSettings.SCAN_MODE_LOW_LATENCY + "', '" +
                    Constants.RECORDING_SWITCH_OFF + "', '" +
                    "" + "', '" +
                    Constants.AUTO_CLOSE_SWITCH_OFF + "', '" +
                    "000000" + "');");
            cursor = db.rawQuery("SELECT scan_period, is_record, file_name, is_auto_close, auto_close_time FROM setting " +
                    "WHERE _id='" + 1 + "';", null);
        }
        cursor.moveToFirst();
        try {
            scanPeriod = cursor.getInt(0);
            isRecord = cursor.getInt(1);
            fileName = cursor.getString(2);
            isAutoClose = cursor.getInt(3);
            autoCloseTime = cursor.getString(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.d(TAG, "cursor's count = " + String.valueOf(cursor.getCount()));
        // Log.d(TAG, "scanPeriod = " + String.valueOf(scanPeriod) + ", isRecord = " + String.valueOf(isRecord) + ", fileName = " +
        //         fileName + ", isAutoClose = " + String.valueOf(isAutoClose) + ", autoCloseTime = " + autoCloseTime);
        cursor.close();
    }

    public static void update(int database_Number, Object obj) {
        ContentValues values = null;
        switch (database_Number) {
            case Constants.DATABASE_SCAN_PERIOD:
                values = new ContentValues();
                values.put("scan_period", (int) obj);
                db.update("setting", values, "_id=1", null);
                scanPeriod = (int) obj;
                break;

            case Constants.DATABASE_IS_RECORD:
                values = new ContentValues();
                values.put("is_record", (int) obj);
                db.update("setting", values, "_id=1", null);
                isRecord = (int) obj;
                break;

            case Constants.DATABASE_FILE_NAME:
                values = new ContentValues();
                values.put("file_name", String.valueOf(obj));
                db.update("setting", values, "_id=1", null);
                fileName = String.valueOf(obj);
                break;

            case Constants.DATABASE_IS_AUTO_CLOSE:
                values = new ContentValues();
                values.put("is_auto_close", (int) obj);
                db.update("setting", values, "_id=1", null);
                isAutoClose = (int) obj;
                break;

            case Constants.DATABASE_AUTO_CLOSE_TIME:
                values = new ContentValues();
                values.put("auto_close_time", String.valueOf(obj));
                db.update("setting", values, "_id=1", null);
                autoCloseTime = String.valueOf(obj);
                break;
        }
    }
}
