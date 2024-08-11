package com.example.galileomastermindboilerplate.ui;

import android.app.Activity;
import android.location.GnssCapabilities;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.galileomastermindboilerplate.R;

public class GeneralStatsPage extends Fragment {

    public static GnssCapabilities Capabilities = null;


    public static GeneralStatsPage newInstance() {
        GeneralStatsPage fragment = new GeneralStatsPage();
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
        View view = inflater.inflate(R.layout.fragment_general_stats_page, container, false);
        DrawCapabilities(view);
        return view;
    }

    private void DrawCapabilities(View view) {
        Activity activity = getActivity();
        if (activity == null) {
            Log.i("GeneralStatsPage", "The page is not selected, skipping");
            return;
        }
        if (Capabilities == null) {
            return;
        }

        // Navigations messages
        TextView supportsNavigationMessages = view.findViewById(R.id.supportsNavigationMessages);
        assert supportsNavigationMessages != null;
        supportsNavigationMessages.setText(Capabilities.hasNavigationMessages() ? "true" : "false");

        TextView supportsMeasurements = view.findViewById(R.id.supportsMeasurements);
        assert supportsMeasurements != null;
        supportsMeasurements.setText(Capabilities.hasMeasurements() ? "true" : "false");
    }
}