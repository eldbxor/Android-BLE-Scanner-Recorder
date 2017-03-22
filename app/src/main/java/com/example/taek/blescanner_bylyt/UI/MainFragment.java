package com.example.taek.blescanner_bylyt.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedIntancestate ) {
        super.onActivityCreated(savedIntancestate);
    }
}
