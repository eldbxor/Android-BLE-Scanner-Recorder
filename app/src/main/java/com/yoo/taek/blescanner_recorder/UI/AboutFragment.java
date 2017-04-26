package com.yoo.taek.blescanner_recorder.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoo.taek.blescanner_recorder.R;

/**
 * Created by eldbx on 2017-04-21.
 */

public class AboutFragment extends Fragment {
    private View rootView;
    Context context_mainActivity;

    public AboutFragment() {}

    public void sendContext(Context context) {
        context_mainActivity = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about, container, false);

        return rootView;
    }
}
