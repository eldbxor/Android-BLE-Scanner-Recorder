package com.yoo.taek.blescanner_recorder.Utils;

/**
 * Created by Taek on 2017-04-10.
 */

public class BLEData {
    public String deviceName;
    public String deviceAddress;
    public String uuid;
    public String major;
    public String minor;
    public String allData;
    public String rssi;
    public String currentTime;

    public BLEData(String deviceName, String deviceAddress, String uuid, String major, String minor, String allData, String rssi, String currentTime) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.allData = allData;
        this.rssi = rssi;
        this.currentTime = currentTime;
    }
}
