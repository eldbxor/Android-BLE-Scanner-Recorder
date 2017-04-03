package com.example.taek.blescanner_bylyt.Services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.example.taek.blescanner_bylyt.Utils.BLEServiceUtils;
import com.example.taek.blescanner_bylyt.Utils.Constants;
import com.example.taek.blescanner_bylyt.Utils.IncomingHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BLEScanService extends Service {
    private ScanSettings mScanSetting;
    private List<ScanFilter> mScanFilter;
    private String TAG = "BLEScanService";
    private BLEServiceUtils mBLEServiceUtils;
    private Context serviceContext;
    public Messenger replyToActivityMessenger; // Activity에 응답하기 위한 Messenger
    public boolean isConnectedMessenger; // Activity와 Service가 연결되었는지 확인
    public ArrayList<String[]> arr_beaconData;
    private Timer timer;
    private TimerTask timerTask;

    // Target we publish for clients to send messages to IncomingHandler.
    private Messenger incomingMessenger = new Messenger(new IncomingHandler(Constants.HANDLER_TYPE_SERVICE, BLEScanService.this));

    public BLEScanService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate(): service start");
        serviceContext = this;
        mBLEServiceUtils = new BLEServiceUtils(serviceContext);
        mScanFilter = new ArrayList<>();
        arr_beaconData = new ArrayList<>();
        isConnectedMessenger = false;

        mBLEServiceUtils.createBluetoothAdapter(getSystemService(this.BLUETOOTH_SERVICE)); // Bluetooth Adapter 생성
        mBLEServiceUtils.enableBluetooth(); // Bluetooth 사용

        // waiting for stating bluetooth on
        try{
            do {
                Log.d(TAG, "onCreate(): waiting for stating bluetooth on");
                Thread.sleep(100);
            } while (mBLEServiceUtils.mBluetoothAdapter.getState() != mBLEServiceUtils.mBluetoothAdapter.STATE_ON);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        if (mBLEServiceUtils.mBluetoothAdapter != null) { // && mBluetoothAdapter.isEnabled()){
            if (Build.VERSION.SDK_INT >= 21) {
                Log.d(TAG, "onCreate(): BLEScanner setting");
                mBLEServiceUtils.mBLEScanner = mBLEServiceUtils.mBluetoothAdapter.getBluetoothLeScanner();
                mScanSetting = mBLEServiceUtils.setPeriod(ScanSettings.SCAN_MODE_LOW_LATENCY);
                mScanFilter = new ArrayList<ScanFilter>();
            }

            // BLEScanner 객체 확인
            if (mBLEServiceUtils.mBLEScanner == null && Build.VERSION.SDK_INT >= 21) {
                Log.d(TAG, "onCreate(): mBLEScanner is null");
                Toast.makeText(serviceContext, "Can not find BLE Scanner", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    public void scanBLEDevice(final boolean enable) {
        if (enable){
            if (Build.VERSION.SDK_INT < 21) {
                // 롤리팝 이전버전
                mBLEServiceUtils.mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else{
                mBLEServiceUtils.mBLEScanner.startScan(null, mScanSetting, mScanCallback);
            }

            timerStart();
        }else{
            if (Build.VERSION.SDK_INT < 21) {
                // 롤리팝 이전버전
                mBLEServiceUtils.mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else{
                mBLEServiceUtils.mBLEScanner.stopScan(mScanCallback);
                mScanFilter.clear();
            }

            timerStop();
        }
    }

    public void restartScan(int scanMode) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (mScanSetting.getScanMode() == scanMode) {
                return;
            } else {
                Log.d(TAG, "restartScan(): change scanMode");
                mScanSetting = null;
                scanBLEDevice(false);
                mScanSetting = mBLEServiceUtils.setPeriod(scanMode);
                scanBLEDevice(true);
            }
        }
    }

    // arr_beaconData에 스캔된 비콘 데이터가 있는지 검사
    public boolean isContainsBeaconData(String deviceAddress, String uuid) {
        boolean contains = false;
        for (String[] beaconData : arr_beaconData) {
            if (beaconData[1].equals(deviceAddress) || beaconData[2].equals(uuid)) {
                contains = true;
            }
        }

        return contains;
    }

    public void timerStart() {
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                timerArrUpdate();
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    public void timerStop() {
        timer.cancel();
        timer.purge();
    }

    public void timerArrUpdate() {
        Log.d(TAG, "timerTextUpdate(): send activity beacons's data and clear arr_BeaconData");
        mBLEServiceUtils.sendBeaconDataToActivity(arr_beaconData);
    }

    // api 21 이상
    @SuppressLint("NewApi")
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            List<String> separatedData = mBLEServiceUtils.separate(result.getScanRecord().getBytes());

            // public DeviceInfo(BluetoothDevice device, String address, String scanRecord, String uuid, String major, String minor, int rssi)
            // separatedData's index 0: scanRecord, 1: uuid, 2: major, 3: minor
            /*
            mBLEServiceUtils.addDeviceInfo(Constants.CALLBACK_TYPE_BLE_SCAN_SERVICE, new DeviceInfo(result.getDevice(), result.getDevice().getAddress(), separatedData.get(0),
                    separatedData.get(1), separatedData.get(2), separatedData.get(3), result.getRssi()));

            mBLEServiceUtils.setCurrentBeacons(result.getDevice().getAddress(), result.getRssi());
        */

            if (!isContainsBeaconData(result.getDevice().getAddress(), separatedData.get(1))) {
                Log.d(TAG, "ScanCallback(): add beacon's data: device's name = " + result.getDevice().getName() + ", device's address = " + result.getDevice().getAddress() +
                        separatedData.get(1));
                arr_beaconData.add(new String[]{ result.getDevice().getName(), result.getDevice().getAddress(), separatedData.get(1),
                        separatedData.get(2), separatedData.get(3), separatedData.get(0), String.valueOf(result.getRssi()) });
            }
            /*
            // send activity beacon's data from service
            mBLEServiceUtils.sendBeaconDataToActivity(result.getDevice().getName(), result.getDevice().getAddress(), separatedData.get(1),
                    separatedData.get(2), separatedData.get(3), separatedData.get(0), result.getRssi()); */
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    // api 21 미만
    BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
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
/*
            Log.d("AllOfScanRecord", all + ", " + uuid + ", " + String.valueOf(major_int) + ", " + String.valueOf(minor_int));
*/
/*
            mBLEServiceUtils.addDeviceInfo(Constants.CALLBACK_TYPE_BLE_SCAN_SERVICE, new DeviceInfo(device, device.getAddress(), all,
                    uuid, String.valueOf(major_int), String.valueOf(minor_int), rssi));

            mBLEServiceUtils.setCurrentBeacons(device.getAddress(), rssi);
*/

            if (!isContainsBeaconData(device.getAddress(), uuid)) {
                Log.d(TAG, "ScanCallback(): add beacon's data: device's name = " + device.getName() + ", device's address = " + device.getAddress());
                arr_beaconData.add(new String[]{ device.getName(), device.getAddress(), uuid,
                        String.valueOf(major_int), String.valueOf(minor_int), all, String.valueOf(rssi) });
            }
            /*
            // send activity beacon's data from service
            mBLEServiceUtils.sendBeaconDataToActivity(device.getName(), device.getAddress(), uuid,
                    String.valueOf(major_int), String.valueOf(minor_int), all, rssi); */

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return incomingMessenger.getBinder();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "Service onDestroy");
        try {
            timerStop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
