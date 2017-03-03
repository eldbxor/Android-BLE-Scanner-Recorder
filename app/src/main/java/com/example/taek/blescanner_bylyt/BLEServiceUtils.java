package com.example.taek.blescanner_bylyt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taek on 2016-07-07.
 */
public class BLEServiceUtils {
    public BluetoothManager mBluetoothManager;
    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothLeScanner mBLEScanner; // BLE 스캐너(api 21 이상)
    private Context mContext;
    private String TAG = "BLEServiceUtils";

    // 생성자
    public BLEServiceUtils(Context context) {
        mContext = context;
        Log.d(TAG, "BLEServiceUtils(): 생성자");
    }

    public void createBluetoothAdapter(Object obj){
        mBluetoothManager = (BluetoothManager)obj;
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        Log.d(TAG, "createBluetoothAdapter():  BluetoothAdapter 객체 생성");
    }

    public void enableBluetooth(){
        if(!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
            Log.d(TAG, "enableBluetooth():  BluetoothAdapter 실행");
        }
    }
/*
    public void addDeviceInfo(int callbackType, DeviceInfo deviceInfo) {
        boolean isExisted = false;
        int index = 0;

        switch (callbackType) {
            case Constants.CALLBACK_TYPE_BLE_SCAN_SERVICE:
                for(DeviceInfo mInfo : ((BLEScanService) mContext).mBLEDevices){
                    if(mInfo.Address.equals(deviceInfo.Address)){
                        isExisted = true;
                        index = ((BLEScanService) mContext).mBLEDevices.indexOf(mInfo);
                        break;
                    }
                }

                if(isExisted == true){
                    ((BLEScanService) mContext).mBLEDevices.add(index, deviceInfo);
                    ((BLEScanService) mContext).mBLEDevices.remove(index + 1);
                }else{
                    ((BLEScanService) mContext).mBLEDevices.add(deviceInfo);
                }
                break;

            case Constants.CALLBACK_TYPE_CALIBRATION_SERVICE:
                for(DeviceInfo mInfo : ((CalibrationService) mContext).mBLEDevices){
                    if(mInfo.Address.equals(deviceInfo.Address)){
                        isExisted = true;
                        index = ((CalibrationService) mContext).mBLEDevices.indexOf(mInfo);
                        break;
                    }
                }

                if(isExisted == true){
                    ((CalibrationService) mContext).mBLEDevices.add(index, deviceInfo);
                    ((CalibrationService) mContext).mBLEDevices.remove(index + 1);
                }else{
                    ((CalibrationService) mContext).mBLEDevices.add(deviceInfo);
                }
                break;
        }
    }

    public void addEssentialData(int callbackType, Map<String, String> essentialData) {
        boolean isExisted = false;
        int index = 0;

        switch (callbackType) {
            case Constants.CALLBACK_TYPE_BLE_SCAN_SERVICE:
                for (Map<String, String> map : ((BLEScanService) mContext).EssentialDataArray) {
                    if (essentialData.get("id_workplace").equals(map.get("id_workplace"))) {
                        isExisted = true;
                        index = ((BLEScanService) mContext).EssentialDataArray.indexOf(map);
                        break;
                    }
                }

                if (isExisted == true) {
                    ((BLEScanService) mContext).EssentialDataArray.add(index, essentialData);
                    ((BLEScanService) mContext).EssentialDataArray.remove(index + 1);
                } else {
                    ((BLEScanService) mContext).EssentialDataArray.add(essentialData);
                }
                break;

            case Constants.CALLBACK_TYPE_CALIBRATION_SERVICE:
                for (Map<String, String> map : ((CalibrationService) mContext).EssentialDataArray) {
                    if (essentialData.get("id_workplace").equals(map.get("id_workplace"))) {
                        isExisted = true;
                        index = ((CalibrationService) mContext).EssentialDataArray.indexOf(map);
                        break;
                    }
                }

                if (isExisted == true) {
                    ((CalibrationService) mContext).EssentialDataArray.add(index, essentialData);
                    ((CalibrationService) mContext).EssentialDataArray.remove(index + 1);
                } else {
                    ((CalibrationService) mContext).EssentialDataArray.add(essentialData);
                }
                break;
        }
    }

    public void addFilterList(int callbackType, String beaconAddress){
        boolean isExisted = false;
        int index = 0;

        if(beaconAddress.equals(""))
            return;

        switch (callbackType) {
            case Constants.CALLBACK_TYPE_BLE_SCAN_SERVICE:
                for(String mac : ((BLEScanService) mContext).filterlist){
                    if(mac.equals(beaconAddress)){
                        isExisted = true;
                        index = ((BLEScanService) mContext).filterlist.indexOf(mac);
                        break;
                    }
                }

                if(isExisted == true){
                    ((BLEScanService) mContext).filterlist.add(index, beaconAddress);
                    ((BLEScanService) mContext).filterlist.remove(index + 1);
                }else{
                    ((BLEScanService) mContext).filterlist.add(beaconAddress);
                }
                break;

            case Constants.CALLBACK_TYPE_CALIBRATION_SERVICE:
                for(String mac : ((CalibrationService) mContext).filterlist){
                    if(mac.equals(beaconAddress)){
                        isExisted = true;
                        index = ((CalibrationService) mContext).filterlist.indexOf(mac);
                        break;
                    }
                }

                if(isExisted == true){
                    ((CalibrationService) mContext).filterlist.add(index, beaconAddress);
                    ((CalibrationService) mContext).filterlist.remove(index + 1);
                }else{
                    ((CalibrationService) mContext).filterlist.add(beaconAddress);
                }
                break;
        }

    }
*/
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
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.M){
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
