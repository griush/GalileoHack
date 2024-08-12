package com.example.galileomastermindboilerplate.ui;

import android.app.Activity;
import android.location.GnssCapabilities;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.galileomastermindboilerplate.R;

import java.util.Locale;

public class GeneralStatsPage extends Fragment {

    public static GnssCapabilities Capabilities = null;
    public static int GnssModelYear = 0;


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

        // Navigation messages
        TextView supportsNavigationMessages = view.findViewById(R.id.supportsNavigationMessages);
        TextView supportsMeasurements = view.findViewById(R.id.supportsMeasurements);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            assert supportsNavigationMessages != null;
            supportsNavigationMessages.setText(Capabilities.hasNavigationMessages() ? "true" : "false");

            assert supportsMeasurements != null;
            supportsMeasurements.setText(Capabilities.hasMeasurements() ? "true" : "false");
        }
        else
        {
            supportsNavigationMessages.setText("Not supported by your OS version");
            supportsMeasurements.setText("Not supported by your OS version");
        }

        TextView hardwareYear = view.findViewById(R.id.gnssHardwareYear);
        assert hardwareYear != null;
        hardwareYear.setText(GnssModelYear != 0 ? String.format(Locale.US , "%d", GnssModelYear) : "Unknown (before 2016)");
    }
}