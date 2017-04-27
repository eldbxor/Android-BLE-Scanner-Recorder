package com.yoo.taek.blescanner_recorder.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.yoo.taek.blescanner_recorder.Services.BLEScanService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taek on 2016-07-07.
 */
public class BLEServiceUtils {
    public BluetoothManager mBluetoothManager;
    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothLeScanner mBLEScanner; // BLE 스캐너(api 21 이상)
    public ArrayList<BLEData> list_BLEData;
    public ExcelWriter excelWriter;
    private Context context_BLEScanService;
    private String TAG = "BLEServiceUtils";

    // 생성자
    public BLEServiceUtils(Context context) {
        context_BLEScanService = context;
        list_BLEData = new ArrayList<>();
        excelWriter = new ExcelWriter(context);
        Log.d(TAG, "BLEServiceUtils(): 생성자");
    }

    public void createBluetoothAdapter(Object obj){
        mBluetoothManager = (BluetoothManager)obj;
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        Log.d(TAG, "createBluetoothAdapter():  BluetoothAdapter 객체 생성");
    }

    public void enableBluetooth(){
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
                Log.d(TAG, "enableBluetooth():  BluetoothAdapter 실행");
            }
        }
    }

    public void addBLEData(String deviceName, String deviceAddress, String uuid, String major, String minor, String allData, String rssi, String dateTime) {
        list_BLEData.add(new BLEData(deviceName, deviceAddress, uuid, major, minor, allData, rssi, dateTime));
    }

    public void writeExcelFile(final boolean scanStop) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (list_BLEData.size() > 100 || scanStop) {
                    ArrayList<BLEData> temp = (ArrayList<BLEData>) list_BLEData.clone();
                    excelWriter.writeFile(temp);
                    list_BLEData.clear();
                }
            }
        });
    }

    // send Activity Beacon data from service
    public void sendBeaconDataToActivity(ArrayList<String[]> arr_BeaconData) {
        ArrayList<String[]> temp = (ArrayList<String[]>) arr_BeaconData.clone();
        // Log.d(TAG, "sendBeaconDataToActivity: send arr_BeaconData to activity, arr_BeaconData's size = " + String.valueOf(temp.size()));

        if (temp.size() != 0) {
            for (String[] beaconData :temp) {
                try {
                    ((BLEScanService) context_BLEScanService).replyToActivityMessenger.send(
                            Message.obtain(null, Constants.HANDLE_MESSAGE_TYPE_SEND_ACTIVITY_BLE_DATA, beaconData));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            // clear beaconData's arrayList
            ((BLEScanService) context_BLEScanService).arr_beaconData.clear();
        }
    }

    // uuid, major, minor 나누는 메서드
    public List<String> separate(byte[] scanRecord) {
        List<String> result = new ArrayList<String>();
        String all = "";
        String uuid = "";
        int major_int;
        int minor_int;
        for (int i = 0; i <= 28; i++) {
            byte b = scanRecord[i];
            if (i > 8 && i < 28) {
                all += String.format("%02x ", b);
            } else if(i == 28) {
                all += String.format("%02x", b);
            }
            if (i > 8 && i <= 24) {
                if (i == 24) {
                    uuid += String.format("%02x", b);
                } else {
                    uuid += String.format("%02x ", b);
                }
            }
        }

        major_int = (scanRecord[25] & 0xff) * 0x100 + (scanRecord[26] & 0xff);
        minor_int = (scanRecord[27] & 0xff) * 0x100 + (scanRecord[28] & 0xff);

        result.add(all);
        result.add(uuid);
        result.add(String.valueOf(major_int));
        result.add(String.valueOf(minor_int));

        return result;
    }

    public ScanSettings setPeriod(int scanMode){
        ScanSettings settings = null;

        if(Build.VERSION.SDK_INT >= 21){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                settings = new ScanSettings.Builder()
                        .setScanMode(scanMode)
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                        .build();
            }else if(Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
                settings = new ScanSettings.Builder()
                        .setScanMode(scanMode)
                        .build();
            }
        }

        return settings;
    }
}
