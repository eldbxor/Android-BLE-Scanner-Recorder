package com.example.taek.blescanner_bylyt.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.taek.blescanner_bylyt.R;

/**
 * Created by eldbx on 2017-03-26.
 */

public class ViewInfo {
    public LinearLayout childLayout;
    public TextView tvDeviceName, tvDeviceAddress, tvUuid, tvMajor, tvMinor, tvAll, tvRssi;

    public ViewInfo() {}

    public void inflateLayout(LayoutInflater inflater, int inflatedLayout, ViewGroup inflatedLocation) {
        childLayout = (LinearLayout) inflater.inflate(inflatedLayout, inflatedLocation);

        tvDeviceName = (TextView) childLayout.findViewById(R.id.tvDeviceName);
        tvDeviceAddress = (TextView) childLayout.findViewById(R.id.tvDeviceAddress);
        tvUuid = (TextView) childLayout.findViewById(R.id.tvUuid);
        tvMajor = (TextView) childLayout.findViewById(R.id.tvMajor);
        tvMinor = (TextView) childLayout.findViewById(R.id.tvMinor);
        tvAll = (TextView) childLayout.findViewById(R.id.tvAllData);
        tvRssi = (TextView) childLayout.findViewById(R.id.tvRssi);
    }
}
