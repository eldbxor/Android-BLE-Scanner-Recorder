package com.example.taek.blescanner_bylyt.Utils;

/**
 * Created by Taek on 2017-04-10.
 */

public class BLEData {
    private String deviceName;
    private String deviceAddress;
    private String uuid;
    private String major;
    private String minor;
    private String allData;
    private String rssi;

    public BLEData(String deviceName, String deviceAddress, String uuid, String major, String minor, String allData, String rssi) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.allData = allData;
        this.rssi = rssi;
    }
}
