package com.example.galileomastermindboilerplate.ui;

import android.app.Activity;
import android.location.GnssCapabilities;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    public static double Latitude = 0.0, Longitude = 0.0 , Altitude = 0.0, Accuracy = 0.0;
    public static float Bearing = 0.0f, BearingAccuracy = 0.0f;

    SwipeRefreshLayout PullToRefresh;

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
        return inflater.inflate(R.layout.fragment_general_stats_page, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        PullToRefresh = view.findViewById(R.id.SwipeToRefreshLocation);
        PullToRefresh.setOnRefreshListener(() -> {
            DrawCapabilities(view);
            PullToRefresh.setRefreshing(false);
        });

        DrawCapabilities(view);
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
        TextView hardwareName = view.findViewById(R.id.gnssHardwareName);
        TextView hardwareYear = view.findViewById(R.id.gnssHardwareYear);
        TextView deviceLatitude = view.findViewById(R.id.deviceLatitude);
        TextView deviceLongitude = view.findViewById(R.id.deviceLongitude);
        TextView deviceAltitude = view.findViewById(R.id.deviceAltitude);
        TextView deviceAccuracy = view.findViewById(R.id.deviceAccuracy);
        TextView deviceBearing = view.findViewById(R.id.deviceBearing);
        TextView deviceBearingAccuracy = view.findViewById(R.id.deviceBearingAccuracy);

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

        // < API 30 data (supported by minimum)
        assert hardwareYear != null;
        hardwareYear.setText(GnssModelYear != 0 ? String.format(Locale.US , "%d", GnssModelYear) : "Unknown (before 2016)");

        assert hardwareName != null;
        hardwareName.setText(GnssModelName == null ? "Unknown" : GnssModelName);

        assert deviceLatitude != null;
        deviceLatitude.setText(String.valueOf(Latitude));

        assert deviceLongitude != null;
        deviceLongitude.setText(String.valueOf(Longitude));

        assert deviceAltitude != null;
        deviceAltitude.setText(String.valueOf(Altitude));

        assert deviceAccuracy != null;
        deviceAccuracy.setText(String.valueOf(Accuracy));

        assert deviceBearing != null;
        deviceBearing.setText(String.valueOf(Bearing));

        assert deviceBearingAccuracy != null;
        deviceBearingAccuracy.setText(String.valueOf(BearingAccuracy));
    }
}