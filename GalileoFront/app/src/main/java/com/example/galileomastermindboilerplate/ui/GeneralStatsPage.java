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
    public static String GnssModelName = null;
    public static int GnssModelYear = 0; // 0 is Unknown

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
        TextView hasADR = view.findViewById(R.id.hasADR);
        TextView hasMsa = view.findViewById(R.id.hasMsa);
        TextView supportsSatellitePvt = view.findViewById(R.id.supportsSatellitePvt);

        // API 34 Data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            assert hasADR != null;
            int support = Capabilities.hasAccumulatedDeltaRange();
            String adrText = "Unknown";
            switch (support) {
                case GnssCapabilities.CAPABILITY_UNKNOWN -> adrText = "Unknown";
                case GnssCapabilities.CAPABILITY_SUPPORTED -> adrText = "Supported";
                case GnssCapabilities.CAPABILITY_UNSUPPORTED -> adrText = "Unsupported";
            }
            hasADR.setText(adrText);

            hasMsa.setText(Capabilities.hasMsa() ? "Supported" : "Unsupported");
            supportsSatellitePvt.setText(Capabilities.hasSatellitePvt() ? "Supported" : "Unsupported");
        }
        else {
            hasADR.setText("Unknown");
            hasMsa.setText("Unknown");
            supportsSatellitePvt.setText("Unknown");
        }

        // API 31 data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            assert supportsNavigationMessages != null;
            supportsNavigationMessages.setText(Capabilities.hasNavigationMessages() ? "Supported" : "Unsupported");

            assert supportsMeasurements != null;
            supportsMeasurements.setText(Capabilities.hasMeasurements() ? "Supported" : "Unsupported");
        }
        else {
            supportsNavigationMessages.setText("Unknown");
            supportsMeasurements.setText("Unknown");
        }

        TextView hardwareYear = view.findViewById(R.id.gnssHardwareYear);
        assert hardwareYear != null;
        hardwareYear.setText(GnssModelYear != 0 ? String.format(Locale.US , "%d", GnssModelYear) : "Unknown (before 2016)");

        TextView hardwareName = view.findViewById(R.id.gnssHardwareName);
        assert hardwareName != null;
        hardwareName.setText(GnssModelName == null ? "Unknown" : GnssModelName);
    }
}