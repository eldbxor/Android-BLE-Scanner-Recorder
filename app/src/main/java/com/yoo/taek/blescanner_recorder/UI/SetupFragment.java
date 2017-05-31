package com.yoo.taek.blescanner_recorder.UI;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.yoo.taek.blescanner_recorder.R;

/**
 * Created by eldbx on 2017-03-25.
 */

public class SetupFragment extends Fragment {
    private View rootView;
    Context context_mainActivity;
    private Button button_setupPeriod, button_saveData, button_about;
    public SetPeriodFragment fragSetPeriod;
    public SaveExcelFileFragment fragSaveExcelFile;
    public AboutFragment fragAbout;
    public FragmentManager fragmentManager;

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

        fragmentManager = getActivity().getFragmentManager();

        fragSetPeriod = new SetPeriodFragment();
        fragSaveExcelFile = new SaveExcelFileFragment();
        fragAbout = new AboutFragment();
        fragSetPeriod.sendContext(context_mainActivity);
        fragSaveExcelFile.sendContext(context_mainActivity);
        fragAbout.sendContext(context_mainActivity);

        button_setupPeriod = (Button) rootView.findViewById(R.id.button_set_period);
        button_saveData = (Button) rootView.findViewById(R.id.button_save_data);
        button_about = (Button) rootView.findViewById(R.id.button_about);

        button_setupPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 21) {
                    Toast.makeText(context_mainActivity, "It's only possible over sdk version 21", Toast.LENGTH_SHORT).show();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragSetPeriod)
                        .detach(fragSetPeriod).attach(fragSetPeriod)
                        .addToBackStack(null)
                        .commit();
            }
        });

        button_saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragSaveExcelFile)
                        .detach(fragSaveExcelFile).attach(fragSaveExcelFile)
                        .addToBackStack(null)
                        .commit();
            }
        });

        button_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragAbout)
                        .detach(fragAbout).attach(fragAbout)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}
