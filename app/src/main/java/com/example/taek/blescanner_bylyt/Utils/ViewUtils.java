package com.example.taek.blescanner_bylyt.Utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.taek.blescanner_bylyt.R;

import java.util.ArrayList;

/**
 * Created by eldbx on 2017-03-26.
 */

public class ViewUtils {
    public ArrayList<ViewInfo> viewInfos;
    private View rootView;
    public LinearLayout inflatedLocation;

    public ViewUtils(View rootView, int inflatedLocation) {
        viewInfos = new ArrayList<>();
        this.rootView = rootView;
        this.inflatedLocation = (LinearLayout) rootView.findViewById(inflatedLocation);
    }

    // inflate the dynamic views at inflated location
    public void inflateLayout() {
        for (ViewInfo viewInfo : viewInfos) {
            boolean exist = false;
            for (int i = 0; i < inflatedLocation.getChildCount(); i++) {
                LinearLayout childLayout = (LinearLayout) inflatedLocation.getChildAt(i);
                if (viewInfo.childLayout.equals(childLayout)) {
                    Log.d("childLayout", "already exist");
                    exist = true;
                }
            }
            if (exist) {
                viewInfo.setText();
            } else {
                inflatedLocation.addView(viewInfo.childLayout);
                viewInfo.setText();
            }
        }

        /*
        // when view's size is 0 or the beaconData didn't exist.
        if (size() == 0 || !contains(beaconData)) {
            ViewInfo viewInfo = new ViewInfo(rootView);
            this.inflatedLocation = (LinearLayout) rootView.findViewById(inflatedLocation);
            viewInfo.updateText(String.valueOf(beaconData.get(0)), String.valueOf(beaconData.get(1)), String.valueOf(beaconData.get(2)),
                    String.valueOf(beaconData.get(4)), String.valueOf(beaconData.get(5)), String.valueOf(beaconData.get(6)), String.valueOf(beaconData.get(7)));

            this.inflatedLocation.addView(viewInfo.childLayout);
            viewInfos.add(viewInfo);
        }
        // when beaconData already exist.
        else {
            ViewInfo viewInfo = viewInfos.get(indexOf(String.valueOf(beaconData.get(1))));
            viewInfo.updateText(String.valueOf(beaconData.get(0)), String.valueOf(beaconData.get(1)), String.valueOf(beaconData.get(2)),
                    String.valueOf(beaconData.get(4)), String.valueOf(beaconData.get(5)), String.valueOf(beaconData.get(6)), String.valueOf(beaconData.get(7)));
        } */
    }

    public void updateViewInfo(ArrayList beaconData) {
        /* BeaconData
           arr.add(deviceName);
           arr.add(deviceAddress);
           arr.add(uuid);
           arr.add(major);
           arr.add(minor);
           arr.add(all);
           arr.add(rssi);
           */

        // when view's size is 0 or the beaconData didn't exist.
        if (size() == 0 || !contains(beaconData)) {
            ViewInfo viewInfo = new ViewInfo(rootView);
            viewInfo.updateText(String.valueOf(beaconData.get(0)), String.valueOf(beaconData.get(1)), String.valueOf(beaconData.get(2)),
                    String.valueOf(beaconData.get(4)), String.valueOf(beaconData.get(5)), String.valueOf(beaconData.get(6)), String.valueOf(beaconData.get(7)));

            viewInfos.add(viewInfo);
        }
        // when beaconData already exist.
        else {
            ViewInfo viewInfo = viewInfos.get(indexOf(String.valueOf(beaconData.get(1))));
            viewInfo.updateText(String.valueOf(beaconData.get(0)), String.valueOf(beaconData.get(1)), String.valueOf(beaconData.get(2)),
                    String.valueOf(beaconData.get(4)), String.valueOf(beaconData.get(5)), String.valueOf(beaconData.get(6)), String.valueOf(beaconData.get(7)));
        }
    }

    // remove all views in the layout
    public void removeAllViewsInLayout() {
        inflatedLocation.removeAllViewsInLayout();
    }

    public void removeViewAt(int index) {
        inflatedLocation.removeViewAt(index);
    }

    public void removeView(View view) {
        inflatedLocation.removeView(view);
    }

    public int size() {
        return viewInfos.size();
    }

    // find the index in viewInfos
    public int indexOf(String deviceAddress) {
        int index = -1;

        for (ViewInfo viewInfo : viewInfos) {
            if (viewInfo.tvDeviceName.getText().equals(deviceAddress)) {
                index = viewInfos.indexOf(viewInfo);
            }
        }

        // if index is -1, the index isn't exist.
        return index;
    }

    public boolean contains(ArrayList beaconData) {
        String deviceAddress = String.valueOf(beaconData.get(1));
        int index = indexOf(deviceAddress);
        if (index == -1)
            return false;
        else
            return true;
    }
}

/*
public class ViewUtils {
    public ArrayList<ViewInfo> viewInfos;
    private View rootView;
    public LinearLayout inflatedLocation;

    public ViewUtils(View rootView) {
        viewInfos = new ArrayList<>();
        this.rootView = rootView;
    }

    // inflate the dynamic views at inflated location
    public void inflateLayout(LayoutInflater inflater, int inflatedLayout, int inflatedLocation) {
        ViewInfo viewInfo = new ViewInfo();
        this.inflatedLocation = (LinearLayout) rootView.findViewById(inflatedLocation);
        viewInfo.inflateLayout(inflater, inflatedLayout, this.inflatedLocation);
        viewInfos.add(viewInfo);
    }

    // remove all views in the layout
    public void removeAllViewsInLayout() {
        inflatedLocation.removeAllViewsInLayout();
    }

    public void removeViewAt(int index) {
        inflatedLocation.removeViewAt(index);
    }

    public void removeView(View view) {
        inflatedLocation.removeView(view);
    }

    public int size() {
        return viewInfos.size();
    }

    // find the index in viewInfos
    public int indexOf(String DeviceAddress) {
        int index = -1;

        for (ViewInfo viewInfo : viewInfos) {
            if (viewInfo.tvDeviceName.getText().equals(DeviceAddress)) {
                index = viewInfos.indexOf(viewInfo);
            }
        }

        // if index is -1, the index isn't exist.
        return index;
    }
}
*/