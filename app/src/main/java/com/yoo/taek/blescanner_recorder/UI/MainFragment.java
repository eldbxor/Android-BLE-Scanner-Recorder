package com.yoo.taek.blescanner_recorder.UI;

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
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.yoo.taek.blescanner_recorder.R;
import com.yoo.taek.blescanner_recorder.Utils.Constants;
import com.yoo.taek.blescanner_recorder.Utils.DBUtils;
import com.yoo.taek.blescanner_recorder.Utils.ViewUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by eldbx on 2017-03-22.
 */

public class MainFragment extends Fragment {
    private View rootView;
    public ViewUtils viewUtils;
    public Switch bLEScanSwitch;
    private ProgressBar progressBar;
    private TextView tv_numberOfScannedDevice, tv_ScanResult;
    public Context context_mainActivity;
    public boolean isConnectedMessenger, isScanning, isOnUi;
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
        isScanning = false;
        isOnUi = false;
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
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        tv_numberOfScannedDevice = (TextView) rootView.findViewById(R.id.numberOfScannedDevice);
        tv_ScanResult = (TextView) rootView.findViewById(R.id.tvBackgroundTextInMainFrag);
        isOnUi = true;

        progressBar.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);


        // register onclick listener on the switch
        bLEScanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // switch on
                if (isChecked) {
                    tv_ScanResult.setEnabled(false);
                    tv_ScanResult.setVisibility(View.INVISIBLE);

                    // first scanning
                    if (!isConnectedMessenger) {
                        ((MainActivity) context_mainActivity).connectMessenger();
                        isConnectedMessenger = true;
                    }
                    // not first scanning
                    else {
                        try {
                            Log.d(TAG, "bLEScanSwitch: send Handle message that type is BLE Scan");
                            if (!isScanning)
                                ((MainActivity) context_mainActivity).mMessenger.send(Message.obtain(null, Constants.HANDLE_MESSAGE_TYPE_BLE_SCAN));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    timerStart();
                    progressBar.setEnabled(true);
                    progressBar.setVisibility(View.VISIBLE);
                    if (DBUtils.isRecord == Constants.RECORDING_SWITCH_ON && !isScanning)
                        Toast.makeText(context_mainActivity, "Write excel file to the external storage", Toast.LENGTH_SHORT).show();

                    isScanning = true;
                }
                // switch off
                else {
                    isScanning = false;

                    try {
                        ((MainActivity) context_mainActivity).mMessenger.send(Message.obtain(null, Constants.HANDLE_MESSAGE_TYPE_STOP_SCAN));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    timerStop();
                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnUi = true;
        if (isScanning) {
            timerStop();
            timerStart();
        }
        // Log.d(TAG, "called onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnUi = false;
        // Log.d(TAG, "called onPause");
        if (timer != null) {
            timerStop();
        }
    }

    public void timerStart() {
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isOnUi) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerTextUpdate();
                        }
                    });
                }
            }
        };
        timer.schedule(timerTask, 1000, 2000);
    }

    public void timerStop() {
        timer.cancel();
        timer.purge();
    }

    public void timerTextUpdate() {
        // Log.d(TAG, "timerTextUpdate(): inflate beacons's data on MainFragment");
        // Log.d(TAG, "timerTextUpdate(): viewInfos's size = " + String.valueOf(viewUtils.size()) + ", inflatedLayout's children count = " +
        // String.valueOf(viewUtils.inflatedLocation.getChildCount()));
        viewUtils.inflateLayout();

        // setting a progressBar
        tv_numberOfScannedDevice.setText(String.valueOf(viewUtils.size()));
        if (viewUtils.size() > 0) {
            progressBar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            if (bLEScanSwitch.isChecked()) {
                progressBar.setEnabled(true);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timerStop();
    }

    @Override
    public void onActivityCreated(Bundle savedIntancestate ) {
        super.onActivityCreated(savedIntancestate);
    }
}
