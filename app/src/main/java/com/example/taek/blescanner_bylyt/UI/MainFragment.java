package com.example.taek.blescanner_bylyt.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.taek.blescanner_bylyt.R;

/**
 * Created by eldbx on 2017-03-22.
 */

public class MainFragment extends Fragment {
    private View rootView;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
/*
        LinearLayout inflatedLayout = (LinearLayout) rootView.findViewById(R.id.inflatedLayout);
        LayoutInflater layoutInflater = (LayoutInflater) rootView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.dynamic_beacon_data, inflatedLayout);
*/
        addDynamicView(container);

        return rootView;
    }

    public void addDynamicView(ViewGroup container) {
        TableLayout tableLayout = new TableLayout(rootView.getContext());
        TableRow tableRow = new TableRow(rootView.getContext());
        TextView textView1 = new TextView(rootView.getContext());
        TextView textView2 = new TextView(rootView.getContext());

        // textview setting
        textView1.setText("Device Name");
        textView2.setText("taek");

        // tablerow setting

        // tableLayout setting

        tableRow.addView(textView1);
        tableRow.addView(textView2);
        tableLayout.addView(tableRow);

        container.addView(tableLayout);
    }

    @Override
    public void onActivityCreated(Bundle savedIntancestate ) {
        super.onActivityCreated(savedIntancestate);
    }
}
