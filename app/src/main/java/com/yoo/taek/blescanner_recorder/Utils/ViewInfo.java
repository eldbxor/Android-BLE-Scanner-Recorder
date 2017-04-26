package com.yoo.taek.blescanner_recorder.Utils;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.yoo.taek.blescanner_recorder.R;

/**
 * Created by eldbx on 2017-03-26.
 */

public class ViewInfo {
    public LinearLayout childLayout;
    public TableLayout tableLayout;
    public TableRow trDeviceName, trDeviceAddress, trUuid, trMajor, trMinor, trAll, trRssi;
    public TextView tvDeviceName, tvDeviceAddress, tvUuid, tvMajor, tvMinor, tvAll, tvRssi;
    public String strDeviceName, strDeviceAddress, strUuid, strMajor, strMinor, strAll, strRssi;
    public View devisionLine;
    public int removeCount;

    public ViewInfo(View rootView) {
        // count that didn't scan BLE device.
        removeCount = 0;

        // initialize the views
        childLayout = new LinearLayout(rootView.getContext());

        tableLayout = new TableLayout(rootView.getContext());

        trDeviceName = new TableRow(rootView.getContext());
        trDeviceAddress = new TableRow(rootView.getContext());
        trUuid = new TableRow(rootView.getContext());
        trMajor = new TableRow(rootView.getContext());
        trMinor = new TableRow(rootView.getContext());
        trAll = new TableRow(rootView.getContext());
        trRssi = new TableRow(rootView.getContext());

        tvDeviceName = new TextView(rootView.getContext());
        tvDeviceAddress = new TextView(rootView.getContext());
        tvUuid = new TextView(rootView.getContext());
        tvMajor = new TextView(rootView.getContext());
        tvMinor = new TextView(rootView.getContext());
        tvAll = new TextView(rootView.getContext());
        tvRssi = new TextView(rootView.getContext());

        devisionLine = new View(rootView.getContext());

        tvDeviceName.setTextColor(Color.WHITE);
        tvDeviceAddress.setTextColor(Color.WHITE);
        tvUuid.setTextColor(Color.WHITE);
        tvMajor.setTextColor(Color.WHITE);
        tvMinor.setTextColor(Color.WHITE);
        tvAll.setTextColor(Color.WHITE);
        tvRssi.setTextColor(Color.WHITE);
        tvDeviceName.setText("DeviceName");
        tvDeviceAddress.setText("DeviceAddress");
        tvUuid.setText("UUID");
        tvMajor.setText("Major");
        tvMinor.setText("Minor");
        tvAll.setText("AllData");
        tvRssi.setText("Rssi");

        // initialize label textView
        TextView tvLabelDeviceName, tvLabelDeviceAddress, tvLabelUuid, tvLabelMajor, tvLabelMinor, tvLabelAll, tvLabelRssi;
        tvLabelDeviceName = new TextView(rootView.getContext());
        tvLabelDeviceAddress = new TextView(rootView.getContext());
        tvLabelUuid = new TextView(rootView.getContext());
        tvLabelMajor = new TextView(rootView.getContext());
        tvLabelMinor = new TextView(rootView.getContext());
        tvLabelAll = new TextView(rootView.getContext());
        tvLabelRssi = new TextView(rootView.getContext());
        tvLabelDeviceName.setPadding(0, 0, 15, 0);
        tvLabelDeviceAddress.setPadding(0, 0, 15, 0);
        tvLabelUuid.setPadding(0, 0, 15, 0);
        tvLabelMajor.setPadding(0, 0, 15, 0);
        tvLabelMinor.setPadding(0, 0, 15, 0);
        tvLabelAll.setPadding(0, 0, 15, 0);
        tvLabelRssi.setPadding(0, 0, 15, 0);
        tvLabelDeviceName.setTextColor(Color.WHITE);
        tvLabelDeviceAddress.setTextColor(Color.WHITE);
        tvLabelUuid.setTextColor(Color.WHITE);
        tvLabelMajor.setTextColor(Color.WHITE);
        tvLabelMinor.setTextColor(Color.WHITE);
        tvLabelAll.setTextColor(Color.WHITE);
        tvLabelRssi.setTextColor(Color.WHITE);
        tvLabelDeviceName.setText("DeviceName");
        tvLabelDeviceAddress.setText("DeviceAddress");
        tvLabelUuid.setText("UUID");
        tvLabelMajor.setText("Major");
        tvLabelMinor.setText("Minor");
        tvLabelAll.setText("AllData");
        tvLabelRssi.setText("Rssi");

        // add textView to tableRow
        trDeviceName.addView(tvLabelDeviceName);
        trDeviceName.addView(tvDeviceName);
        trDeviceAddress.addView(tvLabelDeviceAddress);
        trDeviceAddress.addView(tvDeviceAddress);
        trUuid.addView(tvLabelUuid);
        trUuid.addView(tvUuid);
        trMajor.addView(tvLabelMajor);
        trMajor.addView(tvMajor);
        trMinor.addView(tvLabelMinor);
        trMinor.addView(tvMinor);
        trAll.addView(tvLabelAll);
        trAll.addView(tvAll);
        trRssi.addView(tvLabelRssi);
        trRssi.addView(tvRssi);

        // set up ColumnStretchable
        TableRow.LayoutParams tvLayoutParams = new TableRow.LayoutParams();
        tvLayoutParams.weight = 1;
        tvUuid.setLayoutParams(tvLayoutParams);
        tvAll.setLayoutParams(tvLayoutParams);
        tableLayout.setColumnStretchable(2, true);
        tableLayout.setColumnStretchable(5, true);

        // add tableRow to tableLayout
        tableLayout.addView(trDeviceName);
        tableLayout.addView(trDeviceAddress);
        tableLayout.addView(trUuid);
        tableLayout.addView(trMajor);
        tableLayout.addView(trMinor);
        tableLayout.addView(trAll);
        tableLayout.addView(trRssi);

        // add division line to tableLayout
        if (Build.VERSION.SDK_INT >= 23)
            devisionLine.setBackgroundColor(rootView.getResources().getColor(R.color.colorGray, null));
        else
            devisionLine.setBackgroundColor(rootView.getResources().getColor(R.color.colorGray));
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3);
        devisionLine.setLayoutParams(layoutParams);
        tableLayout.addView(devisionLine);

        // add tableLayout to LinearLayout
        childLayout.addView(tableLayout);
    }

    public void setText() {
        tvDeviceName.setText(strDeviceName);
        tvDeviceAddress.setText(strDeviceAddress);
        tvUuid.setText(strUuid);
        tvMajor.setText(strMajor);
        tvMinor.setText(strMinor);
        tvAll.setText(strAll);
        tvRssi.setText(strRssi);
    }

    public void updateText(String deviceName, String deviceAddress, String uuid, String major, String minor, String all, String rssi) {
        removeCount = 0;
        strDeviceName = deviceName;
        strDeviceAddress = deviceAddress;
        strUuid = uuid;
        strMajor = major;
        strMinor = minor;
        strAll = all;
        strRssi = rssi;
    }
}

/*
public class ViewInfo {
    public LinearLayout childLayout;
    public TextView tvDeviceName, tvDeviceAddress, tvUuid, tvMajor, tvMinor, tvAll, tvRssi;

    public ViewInfo() {}

    public void inflateLayout(LayoutInflater inflater, int inflatedLayout, ViewGroup inflatedLocation) {
        childLayout = (LinearLayout) inflater.inflate(inflatedLayout, inflatedLocation, true);

        tvDeviceName = (TextView) childLayout.findViewById(R.id.tvDeviceName);
        tvDeviceAddress = (TextView) childLayout.findViewById(R.id.tvDeviceAddress);
        tvUuid = (TextView) childLayout.findViewById(R.id.tvUuid);
        tvMajor = (TextView) childLayout.findViewById(R.id.tvMajor);
        tvMinor = (TextView) childLayout.findViewById(R.id.tvMinor);
        tvAll = (TextView) childLayout.findViewById(R.id.tvAllData);
        tvRssi = (TextView) childLayout.findViewById(R.id.tvRssi);

        tvDeviceName.setText("테스트");
    }
}
*/