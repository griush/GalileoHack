package com.gnsstracker.mainapp.ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gnsstracker.mainapp.R;

import java.util.ArrayList;

public class DataLogPage extends Fragment {

    // Messages are added here
    // mainapp/SatelliteDataHandler.java at line 328
    public static ArrayList<String> consoleContents = new ArrayList<>();

    public DataLogPage() {
        // Required empty public constructor
    }

    public static DataLogPage newInstance(String param1, String param2) {
        DataLogPage fragment = new DataLogPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_log_page, container, false);
    }
}