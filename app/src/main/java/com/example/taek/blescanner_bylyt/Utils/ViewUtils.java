package com.example.taek.blescanner_bylyt.Utils;

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
