package com.example.taek.blescanner_bylyt.UI;

import android.app.Fragment;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.taek.blescanner_bylyt.R;
import com.example.taek.blescanner_bylyt.Utils.Constants;

/**
 * Created by eldbx on 2017-04-21.
 */

public class SetPeriodFragment extends Fragment {
    private View rootView;
    Context context_mainActivity;
    RadioButton rbLowLatency, rbBalanced, rbLowPower, rbOpportunistic;

    public SetPeriodFragment() {}

    public void sendContext(Context context) {
        context_mainActivity = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_set_period, container, false);

        rbLowLatency = (RadioButton) rootView.findViewById(R.id.radioButton_LowLatency);
        rbBalanced = (RadioButton) rootView.findViewById(R.id.radioButton_Balanced);
        rbLowPower = (RadioButton) rootView.findViewById(R.id.radioButton_LowPower);
        rbOpportunistic = (RadioButton) rootView.findViewById(R.id.radioButton_Opportunistic);

        // ((MainActivity) context_mainActivity).mMessenger.send(Message.obtain(null, Constants.HANDLE_MESSAGE_TYPE_BLE_SCAN));

        RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener() {
            public void onClick(View v) {
                if (rbLowLatency.isChecked()) {
                    try {
                        ((MainActivity) context_mainActivity).mMessenger.send(Message.obtain(null,
                                Constants.HANDLE_MESSAGE_TYPE_CHANGE_THE_SCANNING_PERIOD, ScanSettings.SCAN_MODE_LOW_LATENCY));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context_mainActivity, "Scan mode is Low Latency", Toast.LENGTH_SHORT).show();
                }
                else if (rbBalanced.isChecked()) {
                    try {
                        ((MainActivity) context_mainActivity).mMessenger.send(Message.obtain(null,
                                Constants.HANDLE_MESSAGE_TYPE_CHANGE_THE_SCANNING_PERIOD, ScanSettings.SCAN_MODE_BALANCED));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context_mainActivity, "Scan mode is Balanced", Toast.LENGTH_SHORT).show();
                }
                else if (rbLowPower.isChecked()) {
                    try {
                        ((MainActivity) context_mainActivity).mMessenger.send(Message.obtain(null,
                                Constants.HANDLE_MESSAGE_TYPE_CHANGE_THE_SCANNING_PERIOD, ScanSettings.SCAN_MODE_LOW_POWER));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context_mainActivity, "Scan mode is Low Power", Toast.LENGTH_SHORT).show();
                }
                else if (rbOpportunistic.isChecked()) {
                    try {
                        ((MainActivity) context_mainActivity).mMessenger.send(Message.obtain(null,
                                Constants.HANDLE_MESSAGE_TYPE_CHANGE_THE_SCANNING_PERIOD, ScanSettings.SCAN_MODE_OPPORTUNISTIC));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context_mainActivity, "Scan mode is Opportunistic", Toast.LENGTH_SHORT).show();
                }
            }
        };
        rbLowLatency.setOnClickListener(optionOnClickListener);
        rbBalanced.setOnClickListener(optionOnClickListener);
        rbLowPower.setOnClickListener(optionOnClickListener);
        rbOpportunistic.setOnClickListener(optionOnClickListener);

        return rootView;
    }
}
