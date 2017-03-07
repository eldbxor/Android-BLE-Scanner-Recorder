package com.example.taek.blescanner_bylyt.Utils;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.example.taek.blescanner_bylyt.Services.BLEScanService;

/**
 * Created by eldbx on 2017-03-07.
 */

public class IncomingHandler extends android.os.Handler {
    int mHandlerType;
    Context mContext;
    private String TAG = "IncomingHandler";

    public IncomingHandler(int handlerType, Context context) {
        mHandlerType = handlerType;
        mContext = context;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (mHandlerType) {
            // Activity receive handleMessage
            case Constants.HANDLER_TYPE_ACTIVITY:
                switch (msg.what) {
                    case 1:

                        break;
                }
                break;


            // Service receive handleMessage
            case Constants.HANDLER_TYPE_SERVICE:
                BLEScanService mBLEScanService = (BLEScanService) mContext;
                switch (msg.what) {
                    case Constants.HANDLE_MESSAGE_TYPE_BLE_SCAN:
                        Log.d(TAG, "Service received handleMessage: BLE Scan");
                        mBLEScanService.scanBLEDevice(true);
                        break;

                    case Constants.HANDLE_MESSAGE_TYPE_STOP_SCAN:
                        Log.d(TAG, "Service received handleMessage: stop scan");
                        mBLEScanService.scanBLEDevice(false);
                }
                break;
        }
    }
}
