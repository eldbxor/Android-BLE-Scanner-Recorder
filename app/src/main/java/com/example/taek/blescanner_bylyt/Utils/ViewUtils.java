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
            viewInfo.removeCount++;

            if (viewInfo.removeCount > 5) { // 비콘 데이터가 5번 이상 스캔되지 않으면 레이아웃에서 삭제
                for (int i = 0; i < inflatedLocation.getChildCount(); i++) {
                    LinearLayout childLayout = (LinearLayout) inflatedLocation.getChildAt(i);
                    if (viewInfo.childLayout.equals(childLayout)) {
                        inflatedLocation.removeViewAt(i);
                        break;
                    }
                }

                viewInfos.remove(viewInfo);
                continue;
            } else {
                for (int i = 0; i < inflatedLocation.getChildCount(); i++) {
                    LinearLayout childLayout = (LinearLayout) inflatedLocation.getChildAt(i);
                    if (viewInfo.childLayout.equals(childLayout)) {
                        Log.d("childLayout", "already exist");
                        exist = true;
                    }
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

    public void updateViewInfo(String[] beaconData) {
        /* beaconData's structure :
           new String[]{
               deviceName,
               deviceAddress,
               uuid,
               major,
               minor,
               all,
               rssi
           }
           */
        // when view's size is 0 or the beaconData didn't exist.
        if (size() == 0 || !contains(beaconData)) {
            ViewInfo viewInfo = new ViewInfo(rootView);
            viewInfo.updateText(beaconData[0], beaconData[1], beaconData[2], beaconData[3], beaconData[4], beaconData[5], beaconData[6]);

            viewInfos.add(viewInfo);
        }
        // when beaconData already exist.
        else {
            ViewInfo viewInfo = viewInfos.get(indexOf(beaconData[2]));
            viewInfo.updateText(beaconData[0], beaconData[1], beaconData[2], beaconData[3], beaconData[4], beaconData[5], beaconData[6]);
        }


/*
        // when view's size is 0 or the beaconData didn't exist.
        if (size() == 0 || !contains(arr_BeaconData)) {
            ViewInfo viewInfo = new ViewInfo(rootView);
            viewInfo.updateText(String.valueOf(arr_BeaconData.get(0)), String.valueOf(arr_BeaconData.get(1)), String.valueOf(arr_BeaconData.get(2)),
                    String.valueOf(arr_BeaconData.get(3)), String.valueOf(arr_BeaconData.get(4)), String.valueOf(arr_BeaconData.get(5)), String.valueOf(arr_BeaconData.get(6)));

            viewInfos.add(viewInfo);
        }
        // when beaconData already exist.
        else {
            ViewInfo viewInfo = viewInfos.get(indexOf(String.valueOf(arr_BeaconData.get(2))));
            viewInfo.updateText(String.valueOf(arr_BeaconData.get(0)), String.valueOf(arr_BeaconData.get(1)), String.valueOf(arr_BeaconData.get(2)),
                    String.valueOf(arr_BeaconData.get(3)), String.valueOf(arr_BeaconData.get(4)), String.valueOf(arr_BeaconData.get(5)), String.valueOf(arr_BeaconData.get(6)));
        }
        */
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
    public int indexOf(String uuid) {
        int index = -1;

        for (ViewInfo viewInfo : viewInfos) {
            if (viewInfo.strUuid.equals(uuid)) {
                index = viewInfos.indexOf(viewInfo);
            }
        }

        // if index is -1, the index isn't exist.
        return index;
    }

    public boolean contains(String[] beaconData) {
        String uuid = beaconData[2];
        int index = indexOf(uuid);
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