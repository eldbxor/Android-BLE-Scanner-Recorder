package com.example.taek.blescanner_bylyt.Utils;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.example.taek.blescanner_bylyt.Services.BLEScanService;
import com.example.taek.blescanner_bylyt.UI.MainActivity;

import java.util.ArrayList;

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
                    case Constants.HANDLE_MESSAGE_TYPE_SEND_ACTIVITY_BLE_DATA:
                        /*
                            arr.add(deviceName);
                            arr.add(deviceAddress);
                            arr.add(uuid);
                            arr.add(major);
                            arr.add(minor);
                            arr.add(all);
                            arr.add(rssi);
                         */
                        // ArrayList arr = (ArrayList) msg.obj;
                        String[] beaconData = (String[]) msg.obj;
                        Log.d(TAG, "handleMessageTypeActivity: receive beacon's data");
                        ((MainActivity) mContext).fragMain.viewUtils.updateViewInfo(beaconData);

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

                        // 최초에 Activity에 응답하기 위한 Messenger 등록
                        if (!((BLEScanService) mContext).isConnectedMessenger) {
                            ((BLEScanService) mContext).replyToActivityMessenger = msg.replyTo;
                            ((BLEScanService) mContext).isConnectedMessenger = true;
                        }
                        break;

                    case Constants.HANDLE_MESSAGE_TYPE_STOP_SCAN:
                        Log.d(TAG, "Service received handleMessage: stop scan");
                        mBLEScanService.scanBLEDevice(false);
                }
                break;
        }
    }
}
