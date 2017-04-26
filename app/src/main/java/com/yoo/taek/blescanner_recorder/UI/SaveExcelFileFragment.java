package com.yoo.taek.blescanner_recorder.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.yoo.taek.blescanner_recorder.R;
import com.yoo.taek.blescanner_recorder.Utils.Constants;
import com.yoo.taek.blescanner_recorder.Utils.DBUtils;

/**
 * Created by eldbx on 2017-04-21.
 */

public class SaveExcelFileFragment extends Fragment {
    private View rootView;
    Context context_mainActivity;
    public TextView tvExcelFileDirectory;
    public Switch switch_Recording, switch_AutoCloseTimer;
    public EditText editText_fileName, editText_Hours, editText_Minutes, editText_Seconds;
    private String TAG = "SaveExcelFileFragment";

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

        // Database에서 Recording 여부를 가져옴
        switch (DBUtils.isRecord) {
            case Constants.RECORDING_SWITCH_ON:
                switch_Recording.setChecked(true);
                break;

            case Constants.RECORDING_SWITCH_OFF:
                switch_Recording.setChecked(false);
                editText_fileName.setEnabled(false);
                switch_AutoCloseTimer.setEnabled(false);
                switch_AutoCloseTimer.setChecked(false);
                break;
        }

        // Database에서 AutoClose 여부를 가져옴
        switch (DBUtils.isAutoClose) {
            case Constants.AUTO_CLOSE_SWITCH_ON:
                switch_AutoCloseTimer.setChecked(true);
                break;

            case Constants.AUTO_CLOSE_SWITCH_OFF:
                switch_AutoCloseTimer.setChecked(false);
                editText_Hours.setEnabled(false);
                editText_Minutes.setEnabled(false);
                editText_Seconds.setEnabled(false);
                break;
        }

        // Database에서 저장할 파일명을 가져옴
        if (DBUtils.fileName != null) {
            editText_fileName.setText(DBUtils.fileName);
        }

        // Database에서 AutoTime을 가져옴
        String strAutoCloseTime = DBUtils.autoCloseTime;
        editText_Hours.setText(strAutoCloseTime.substring(0, 2));
        editText_Minutes.setText(strAutoCloseTime.substring(2, 4));
        editText_Seconds.setText(strAutoCloseTime.substring(4));

        switch_Recording.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // storage 관련 Permission 주기
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (((MainActivity) context_mainActivity).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(((MainActivity) context_mainActivity));
                            builder.setTitle("This app needs write external storage access");
                            builder.setMessage("Please grant write external storage access so this app can write excel file.");
                            builder.setPositiveButton("Ok", null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                                }
                            });
                            builder.show();
                        }
                    }

                    editText_fileName.setEnabled(true);
                    switch_AutoCloseTimer.setEnabled(true);
                    DBUtils.update(Constants.DATABASE_IS_RECORD, Constants.RECORDING_SWITCH_ON);

                    if (((MainActivity) context_mainActivity).fragMain.isScanning) {
                        Toast.makeText(context_mainActivity, "Write excel file to the external storage", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editText_fileName.setEnabled(false);
                    switch_AutoCloseTimer.setEnabled(false);
                    switch_AutoCloseTimer.setChecked(false);
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
                    DBUtils.update(Constants.DATABASE_IS_AUTO_CLOSE, Constants.AUTO_CLOSE_SWITCH_ON);
                } else {
                    editText_Hours.setEnabled(false);
                    editText_Minutes.setEnabled(false);
                    editText_Seconds.setEnabled(false);
                    DBUtils.update(Constants.DATABASE_IS_AUTO_CLOSE, Constants.AUTO_CLOSE_SWITCH_OFF);
                }
            }
        });

        editText_Hours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int i;
                    if (editText_Hours.getText().toString().equals("")) {
                        i = 0;
                    } else {
                        i = Integer.valueOf(editText_Hours.getText().toString());
                    }
                    if (i < 10 && i != 0) {
                        String str = "0" + String.valueOf(i);
                        editText_Hours.setText(str);
                    } else if (i == 0) {
                        editText_Hours.setText("00");
                    }
                }
            }
        });

        editText_Minutes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int i;
                    if (editText_Minutes.getText().toString().equals("")) {
                        i = 0;
                    } else {
                        i = Integer.valueOf(editText_Minutes.getText().toString());
                    }
                    if (i < 10 && i != 0) {
                        String str = "0" + String.valueOf(i);
                        editText_Minutes.setText(str);
                    } else if (i == 0) {
                        editText_Minutes.setText("00");
                    }
                }
            }
        });

        editText_Seconds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int i;
                    if (editText_Seconds.getText().toString().equals("")) {
                        i = 0;
                    } else {
                        i = Integer.valueOf(editText_Seconds.getText().toString());
                    }
                    if (i < 10 && i != 0) {
                        String str = "0" + String.valueOf(i);
                        editText_Seconds.setText(str);
                    } else if (i == 0) {
                        editText_Seconds.setText("00");
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Log.d(TAG, "onDestroy()");
        if (switch_Recording.isChecked()) {
            if (!editText_fileName.getText().toString().equals(DBUtils.fileName)) {
                DBUtils.update(Constants.DATABASE_FILE_NAME, editText_fileName.getText().toString());
            }

            if (switch_AutoCloseTimer.isChecked()) {
                String strHour, strMin, strSec, strAutoCloseTime;
                strHour = editText_Hours.getText().toString();
                strMin = editText_Minutes.getText().toString();
                strSec = editText_Seconds.getText().toString();
                while (strHour.length() < 2) {
                    strHour = "0" + strHour;
                }
                while (strMin.length() < 2) {
                    strMin = "0" + strMin;
                }
                while (strSec.length() < 2) {
                    strSec = "0" + strSec;
                }
                strAutoCloseTime = strHour + strMin + strSec;
                DBUtils.update(Constants.DATABASE_AUTO_CLOSE_TIME, strAutoCloseTime);
            }
        }
    }
}
