package com.example.taek.blescanner_bylyt.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.taek.blescanner_bylyt.R;
import com.example.taek.blescanner_bylyt.Utils.Constants;
import com.example.taek.blescanner_bylyt.Utils.DBUtils;

/**
 * Created by eldbx on 2017-04-21.
 */

public class SaveExcelFileFragment extends Fragment {
    private View rootView;
    Context context_mainActivity;
    public TextView tvExcelFileDirectory;
    public Switch switch_Recording, switch_AutoCloseTimer;
    public EditText editText_fileName, editText_Hours, editText_Minutes, editText_Seconds;

    public SaveExcelFileFragment() {}

    public void sendContext(Context context) {
        context_mainActivity = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_save_data, container, false);

        tvExcelFileDirectory = (TextView) rootView.findViewById(R.id.tvExcelFileDirectory);
        tvExcelFileDirectory.append("(" + context_mainActivity.getExternalFilesDir(null).getPath() + ")");
        switch_Recording = (Switch) rootView.findViewById(R.id.switch_recording);
        switch_AutoCloseTimer = (Switch) rootView.findViewById(R.id.switch_AutoCloseTimer);
        editText_fileName = (EditText) rootView.findViewById(R.id.editText_FileName);
        editText_Hours = (EditText) rootView.findViewById(R.id.editText_Hours);
        editText_Minutes = (EditText) rootView.findViewById(R.id.editText_Minutes);
        editText_Seconds = (EditText) rootView.findViewById(R.id.editText_Seconds);

        switch (DBUtils.isRecord) {
            case Constants.RECORDING_SWITCH_ON:
                switch_Recording.setChecked(true);
                break;

            case Constants.RECORDING_SWITCH_OFF:
                switch_Recording.setChecked(false);
                break;
        }

        switch_Recording.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText_fileName.setEnabled(true);
                    switch_AutoCloseTimer.setEnabled(true);
                    DBUtils.update(Constants.DATABASE_IS_RECORD, Constants.RECORDING_SWITCH_ON);
                } else {
                    editText_fileName.setEnabled(false);
                    switch_AutoCloseTimer.setEnabled(false);
                    DBUtils.update(Constants.DATABASE_IS_RECORD, Constants.RECORDING_SWITCH_OFF);
                }
            }
        });

        switch_AutoCloseTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText_Hours.setEnabled(true);
                    editText_Minutes.setEnabled(true);
                    editText_Seconds.setEnabled(true);
                } else {
                    editText_Hours.setEnabled(false);
                    editText_Minutes.setEnabled(false);
                    editText_Seconds.setEnabled(false);
                }
            }
        });

        editText_Hours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int i = Integer.valueOf(editText_Hours.getText().toString());
                    if (i < 10) {
                        String str = "0" + String.valueOf(i);
                        editText_Hours.setText(str);
                    }
                }
            }
        });

        editText_Minutes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int i = Integer.valueOf(editText_Minutes.getText().toString());
                    if (i < 10) {
                        String str = "0" + String.valueOf(i);
                        editText_Minutes.setText(str);
                    }
                }
            }
        });

        editText_Seconds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int i = Integer.valueOf(editText_Seconds.getText().toString());
                    if (i < 10) {
                        String str = "0" + String.valueOf(i);
                        editText_Seconds.setText(str);
                    }
                }
            }
        });

        return rootView;
    }
}
