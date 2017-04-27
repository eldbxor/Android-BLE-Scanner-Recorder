package com.yoo.taek.blescanner_recorder.Services;

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
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.yoo.taek.blescanner_recorder.Utils.BLEServiceUtils;
import com.yoo.taek.blescanner_recorder.Utils.Constants;
import com.yoo.taek.blescanner_recorder.Utils.DBUtils;
import com.yoo.taek.blescanner_recorder.Utils.IncomingHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class BLEScanService extends Service {
    private ScanSettings mScanSetting;
    private List<ScanFilter> mScanFilter;
    private String TAG = "BLEScanService";
    public BLEServiceUtils mBLEServiceUtils;
    private Context serviceContext;
    public Messenger replyToActivityMessenger; // Activity에 응답하기 위한 Messenger
    public boolean isConnectedMessenger; // Activity와 Service가 연결되었는지 확인
    public boolean isScanning; // 스캔 중인지 확인
    public ArrayList<String[]> arr_beaconData;
    private Timer timer;
    private TimerTask timerTask;
    private int timerSecond;
    private int closeSecond;

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
        isScanning = false;

        mBLEServiceUtils.createBluetoothAdapter(getSystemService(this.BLUETOOTH_SERVICE)); // Bluetooth Adapter 생성
        mBLEServiceUtils.enableBluetooth(); // Bluetooth 사용

        if (mBLEServiceUtils.mBluetoothAdapter != null) { // Bluetooth를 사용 가능한 기기일 때
            // waiting for stating bluetooth on
            try {
                do {
                    Log.d(TAG, "onCreate(): waiting for stating bluetooth on");
                    Thread.sleep(100);
                }
                while (mBLEServiceUtils.mBluetoothAdapter.getState() != mBLEServiceUtils.mBluetoothAdapter.STATE_ON);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (mBLEServiceUtils.mBluetoothAdapter != null) { // && mBluetoothAdapter.isEnabled()){
                if (Build.VERSION.SDK_INT >= 21) {
                    Log.d(TAG, "onCreate(): BLEScanner setting");
                    mBLEServiceUtils.mBLEScanner = mBLEServiceUtils.mBluetoothAdapter.getBluetoothLeScanner();
                    mScanSetting = mBLEServiceUtils.setPeriod(DBUtils.scanPeriod); // DB에 저장된 스캔 주기로 설정
                    mScanFilter = new ArrayList<ScanFilter>();
                }

                // BLEScanner 객체 확인
                if (mBLEServiceUtils.mBLEScanner == null && Build.VERSION.SDK_INT >= 21) {
                    Log.d(TAG, "onCreate(): mBLEScanner is null");
                    Toast.makeText(serviceContext, "Can not find BLE Scanner", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else {
            Toast.makeText(serviceContext, "Can not use bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    public void scanBLEDevice(final boolean enable) {
        if (enable){
            isScanning = true;

            if (Build.VERSION.SDK_INT < 21) {
                // 롤리팝 이전버전
                if (mBLEServiceUtils.mBluetoothAdapter != null)
                    mBLEServiceUtils.mBluetoothAdapter.startLeScan(mLeScanCallback);
                else
                    return;
            } else{
                if (mBLEServiceUtils.mBLEScanner != null)
                    mBLEServiceUtils.mBLEScanner.startScan(null, mScanSetting, mScanCallback);
                else
                    return;
            }

            timerStart();
            if (DBUtils.isRecord == Constants.RECORDING_SWITCH_ON) {
                mBLEServiceUtils.excelWriter.readFile(DBUtils.fileName);
            }
        }else{
            isScanning = false;

            if (Build.VERSION.SDK_INT < 21) {
                // 롤리팝 이전버전
                if (mBLEServiceUtils.mBluetoothAdapter != null)
                    mBLEServiceUtils.mBluetoothAdapter.stopLeScan(mLeScanCallback);
                else
                    return;
            } else{
                if (mBLEServiceUtils.mBLEScanner != null) {
                    mBLEServiceUtils.mBLEScanner.stopScan(mScanCallback);
                    mScanFilter.clear();
                } else
                    return;
            }

            timerStop();
            if (DBUtils.isRecord == Constants.RECORDING_SWITCH_ON)
                mBLEServiceUtils.writeExcelFile(true);
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
        timerSecond = 0;
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                timerArrUpdate();
            }
        };
        timer.schedule(timerTask, 1000, 2000);

        // closeSecond 계산
        if (DBUtils.isAutoClose == Constants.AUTO_CLOSE_SWITCH_ON) {
            String strHour, strMin, strSec, strAll;
            int hour, min, sec;
            strAll = DBUtils.autoCloseTime;
            if (strAll.equals("000000")) {
                closeSecond = 60 * 60 * 60;
            } else {
                strHour = strAll.substring(0, 2);
                strMin = strAll.substring(2, 4);
                strSec = strAll.substring(4);
                hour = Integer.valueOf(strHour);
                min = Integer.valueOf(strMin);
                sec = Integer.valueOf(strSec);
                closeSecond = sec + (min * 60) + (hour * 60 * 60);
            }
        } else {
            closeSecond = 60 * 60 * 60;
        }
    }

    public void timerStop() {
        timerSecond = 0;
        timer.cancel();
        timer.purge();
    }

    public void timerArrUpdate() {
        // Log.d(TAG, "timerTextUpdate(): send activity beacons's data and clear arr_BeaconData");
        mBLEServiceUtils.sendBeaconDataToActivity(arr_beaconData);
        timerSecond += 2;

        if (timerSecond >= closeSecond) { // closeTime이 되어 스캔 종료
            try {
                replyToActivityMessenger.send(
                        Message.obtain(null, Constants.HANDLE_MESSAGE_TYPE_STOP_SERVICE));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // api 21 이상
    @SuppressLint("all")
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            String deviceName;
            if (result.getDevice().getName() == null)
                deviceName = "Unknown Device";
            else
                deviceName = result.getDevice().getName();
            List<String> separatedData = mBLEServiceUtils.separate(result.getScanRecord().getBytes());

            // public DeviceInfo(BluetoothDevice device, String address, String scanRecord, String uuid, String major, String minor, int rssi)
            // separatedData's index 0: scanRecord, 1: uuid, 2: major, 3: minor

            if (!isContainsBeaconData(result.getDevice().getAddress(), separatedData.get(1))) {
                // Log.d(TAG, "ScanCallback(): add beacon's data: device's name = " + result.getDevice().getName() + ", device's address = " + result.getDevice().getAddress() +
                //        separatedData.get(1));
                arr_beaconData.add(new String[]{ deviceName, result.getDevice().getAddress(), separatedData.get(1),
                        separatedData.get(2), separatedData.get(3), separatedData.get(0), String.valueOf(result.getRssi()) });
            }

            mBLEServiceUtils.addBLEData(deviceName, result.getDevice().getAddress(), separatedData.get(1), separatedData.get(2),
                    separatedData.get(3), separatedData.get(0), String.valueOf(result.getRssi()), getCurrentTime());

            if (DBUtils.isRecord == Constants.RECORDING_SWITCH_ON) {
                if (mBLEServiceUtils.list_BLEData.size() > 100) {
                    mBLEServiceUtils.writeExcelFile(false);
                }
            }
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
            String deviceName;
            if (device.getName().equals(null))
                deviceName = "Unknown Device";
            else
                deviceName = device.getName();

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

            if (!isContainsBeaconData(device.getAddress(), uuid)) {
                // Log.d(TAG, "ScanCallback(): add beacon's data: device's name = " + device.getName() + ", device's address = " + device.getAddress());
                arr_beaconData.add(new String[]{ deviceName, device.getAddress(), uuid,
                        String.valueOf(major_int), String.valueOf(minor_int), all, String.valueOf(rssi) });
            }

            mBLEServiceUtils.addBLEData(deviceName, device.getAddress(), uuid, String.valueOf(major_int), String.valueOf(minor_int),
                    all, String.valueOf(rssi), getCurrentTime());

            if (DBUtils.isRecord == Constants.RECORDING_SWITCH_ON) {
                if (mBLEServiceUtils.list_BLEData.size() > 100) {
                    mBLEServiceUtils.writeExcelFile(false);
                }
            }
            /*
            // send activity beacon's data from service
            mBLEServiceUtils.sendBeaconDataToActivity(device.getName(), device.getAddress(), uuid,
                    String.valueOf(major_int), String.valueOf(minor_int), all, rssi); */

        }
    };

    public String getCurrentTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String strDate = dateFormat.format(date);

        return strDate;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Log.d(TAG, "onBind()");
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
