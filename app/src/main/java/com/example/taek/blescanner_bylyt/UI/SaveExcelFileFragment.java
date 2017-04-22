package com.example.taek.blescanner_bylyt.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taek.blescanner_bylyt.R;

/**
 * Created by eldbx on 2017-04-21.
 */

public class SaveExcelFileFragment extends Fragment {
    private View rootView;
    Context context_mainActivity;
    public TextView tvExcelFileDirectory;

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

        return rootView;
    }
}
