package com.example.taek.blescanner_bylyt.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.taek.blescanner_bylyt.R;

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

        RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener() {
            public void onClick(View v) {
                if (rbLowLatency.isChecked()) {
                    Toast.makeText(context_mainActivity, "Low Latency is checked", Toast.LENGTH_SHORT).show();
                }
                else if (rbBalanced.isChecked()) {
                    Toast.makeText(context_mainActivity, "Balanced is checked", Toast.LENGTH_SHORT).show();
                }
                else if (rbLowPower.isChecked()) {
                    Toast.makeText(context_mainActivity, "Low Power is checked", Toast.LENGTH_SHORT).show();
                }
                else if (rbOpportunistic.isChecked()) {
                    Toast.makeText(context_mainActivity, "Opportunistic is checked", Toast.LENGTH_SHORT).show();
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
