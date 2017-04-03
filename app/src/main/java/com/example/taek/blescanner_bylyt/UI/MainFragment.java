package com.example.taek.blescanner_bylyt.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by eldbx on 2017-03-22.
 */

public class MainFragment extends Fragment {
    private View rootView;
    public ViewUtils viewUtils;
    private Switch bLEScanSwitch;
    public Context context_mainActivity;
    private boolean isConnectedMessenger;
    public Timer timer;
    public TimerTask timerTask;

    private String TAG = "MainFragment";

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
        viewUtils = new ViewUtils(rootView, R.id.inflatedLayout);
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

                    timerStart();
                }
                // switch off
                else {
                    try {
                        ((MainActivity) context_mainActivity).mMessenger.send(Message.obtain(null, Constants.HANDLE_MESSAGE_TYPE_STOP_SCAN));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    timerStop();
                }
            }
        });



        return rootView;
    }

    public void timerStart() {
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerTextUpdate();
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    public void timerStop() {
        timer.cancel();
        timer.purge();
    }

    public void timerTextUpdate() {
        Log.d(TAG, "timerTextUpdate(): inflate beacons's data on MainFragment");
        Log.d(TAG, "timerTextUpdate(): viewInfos's size = " + String.valueOf(viewUtils.size()) + ", inflatedLayout's children count = " +
        String.valueOf(viewUtils.inflatedLocation.getChildCount()));
        viewUtils.inflateLayout();
    }

    @Override
    public void onActivityCreated(Bundle savedIntancestate ) {
        super.onActivityCreated(savedIntancestate);
    }
}
