package com.example.taek.blescanner_bylyt.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.taek.blescanner_bylyt.R;
import com.example.taek.blescanner_bylyt.Utils.Constants;
import com.example.taek.blescanner_bylyt.Utils.ViewInfo;
import com.example.taek.blescanner_bylyt.Utils.ViewUtils;

/**
 * Created by eldbx on 2017-03-22.
 */

public class MainFragment extends Fragment {
    private View rootView;
    private ViewUtils viewUtils;
    private Switch bLEScanSwitch;
    public Context context_mainActivity;
    private boolean isConnectedMessenger;

/*
    public static MainFragment newInstance(Context context) {
        MainFragment fragment = new MainFragment();
        context_mainActivity = context;

        return fragment;
    }
*/
    public MainFragment() {
        isConnectedMessenger = false;
    }

    public void sendContext(Context context) {
        context_mainActivity = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        viewUtils = new ViewUtils(rootView);
        bLEScanSwitch = (Switch) rootView.findViewById(R.id.BLEScanSwitch);

        // register onclick listener on the switch
        bLEScanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // switch on
                if (isChecked) {
                    // first scanning
                    if (!isConnectedMessenger) {
                        ((MainActivity) context_mainActivity).connectMessenger();
                        isConnectedMessenger = true;
                    }
                    // not first scanning
                    else {
                        try {
                            ((MainActivity) context_mainActivity).mMessenger.send(Message.obtain(null, Constants.HANDLE_MESSAGE_TYPE_BLE_SCAN));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // switch off
                else {
                    try {
                        ((MainActivity) context_mainActivity).mMessenger.send(Message.obtain(null, Constants.HANDLE_MESSAGE_TYPE_STOP_SCAN));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 테스트
        //viewUtils.inflateLayout(inflater, R.layout.dynamic_beacon_data, R.id.inflatedLayout);
        //viewUtils.inflateLayout(inflater, R.layout.dynamic_beacon_data, R.id.inflatedLayout);

        TextView textView = new TextView(rootView.getContext());
        LinearLayout linearLayout = new LinearLayout(rootView.getContext());
        linearLayout.addView(textView);

        container.addView(linearLayout);
        textView.setText("테스트");

        Log.d("viewInfos's size", String.valueOf(viewUtils.size()));
        // ViewInfo viewInfo = viewUtils.viewInfos.get(0);
        // ViewInfo viewInfo2 = viewUtils.viewInfos.get(1);

        // viewInfo.tvDeviceName.setText("첫번째 뷰");
        // viewInfo2.tvDeviceName.setText("두번째 뷰");

        // viewUtils.removeViewAt(0);
        // viewUtils.removeAllViewsInLayout();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedIntancestate ) {
        super.onActivityCreated(savedIntancestate);
    }
}
