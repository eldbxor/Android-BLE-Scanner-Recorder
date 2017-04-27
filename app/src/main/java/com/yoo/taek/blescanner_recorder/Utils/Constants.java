package com.yoo.taek.blescanner_recorder.Utils;

/**
 * Created by eldbx on 2017-03-07.
 */

public class Constants {
    // Permission request code
    public static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 2;
    public static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    // Handler를 생성한 컴포넌트
    public static final int HANDLER_TYPE_ACTIVITY = 1;
    public static final int HANDLER_TYPE_SERVICE = 2;

    // 액티비티에서 받는 message
    public static final int HANDLE_MESSAGE_TYPE_SEND_ACTIVITY_BLE_DATA = 1;
    public static final int HANDLE_MESSAGE_TYPE_STOP_SERVICE = 2;

    // 서비스에서 받는 message
    public static final int HANDLE_MESSAGE_TYPE_BLE_SCAN = 1;
    public static final int HANDLE_MESSAGE_TYPE_STOP_SCAN = 2;
    public static final int HANDLE_MESSAGE_TYPE_CHANGE_THE_SCANNING_PERIOD = 3;

    // About database
    public static final int DATABASE_SCAN_PERIOD = 0;
    public static final int DATABASE_IS_RECORD = 1;
    public static final int DATABASE_FILE_NAME = 2;
    public static final int DATABASE_IS_AUTO_CLOSE = 3;
    public static final int DATABASE_AUTO_CLOSE_TIME = 4;

    public static final int RECORDING_SWITCH_ON = 1;
    public static final int RECORDING_SWITCH_OFF = 0;

    public static final int AUTO_CLOSE_SWITCH_ON = 1;
    public static final int AUTO_CLOSE_SWITCH_OFF = 0;
}
