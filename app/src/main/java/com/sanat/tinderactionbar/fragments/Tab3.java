package com.sanat.tinderactionbar.fragments;// Created by Sanat Dutta on 2/17/2015.

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanat.tinderactionbar.R;

public class Tab3 extends Fragment {

    private String TAG = "tab3";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3, container, false);

        Log.i(TAG, "tab3: onCreateView");

        return view;
    }
}
