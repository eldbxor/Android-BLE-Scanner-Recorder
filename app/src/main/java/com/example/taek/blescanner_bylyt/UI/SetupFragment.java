package com.example.taek.blescanner_bylyt.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.taek.blescanner_bylyt.R;

/**
 * Created by eldbx on 2017-03-25.
 */

public class SetupFragment extends Fragment {
    private View rootView;
    Context context_mainActivity;
    private Button button_setupPeriod, button_saveData, button_autoShutdown, button_about;

/*
    public static SetupFragment newInstance() {
        SetupFragment fragment = new SetupFragment();
        return fragment;
    }
*/
    public SetupFragment() {}

    public void sendContext(Context context) {
        context_mainActivity = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setup, container, false);

        button_setupPeriod = (Button) rootView.findViewById(R.id.button_set_period);
        button_saveData = (Button) rootView.findViewById(R.id.button_save_data);
        button_autoShutdown = (Button) rootView.findViewById(R.id.button_set_auto_shutdown);
        button_about = (Button) rootView.findViewById(R.id.button_about);

        button_setupPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(rootView.getContext(), "Not yet created", Toast.LENGTH_SHORT).show();
            }
        });

        button_saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(rootView.getContext(), "Not yet created", Toast.LENGTH_SHORT).show();
            }
        });

        button_autoShutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(rootView.getContext(), "Not yet created", Toast.LENGTH_SHORT).show();
            }
        });

        button_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(rootView.getContext(), "Not yet created", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
