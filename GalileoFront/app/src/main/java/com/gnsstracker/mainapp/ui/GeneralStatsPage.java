package com.gnsstracker.mainapp.ui;

import android.app.Activity;
import android.location.GnssCapabilities;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnsstracker.mainapp.R;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GeneralStatsPage extends Fragment {

    public static GnssCapabilities Capabilities = null;
    public static String GnssModelName = null;
    public static int GnssModelYear = 0; // 0 is Unknown
    public static double Latitude = 0.0, Longitude = 0.0, Altitude = 0.0, Accuracy = 0.0;
    public static float Bearing = 0.0f, BearingAccuracy = 0.0f;

    TextView supportsNavigationMessages = null;
    TextView supportsMeasurements = null;
    TextView hasADR = null;
    TextView hasMsa = null;
    TextView supportsSatellitePvt = null;
    TextView hardwareName = null;
    TextView hardwareYear = null;
    TextView deviceLatitude = null;
    TextView deviceLongitude = null;
    TextView deviceAltitude = null;
    TextView deviceAccuracy = null;
    TextView deviceBearing = null;
    TextView deviceBearingAccuracy = null;

    private Timer mRefreshTimer;

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
        super.onViewCreated(view, savedInstanceState);

        // Setup UI
        supportsNavigationMessages = view.findViewById(R.id.supportsNavigationMessages);
        supportsMeasurements = view.findViewById(R.id.supportsMeasurements);
        hasADR = view.findViewById(R.id.hasADR);
        hasMsa = view.findViewById(R.id.hasMsa);
        supportsSatellitePvt = view.findViewById(R.id.supportsSatellitePvt);
        hardwareName = view.findViewById(R.id.gnssHardwareName);
        hardwareYear = view.findViewById(R.id.gnssHardwareYear);
        deviceLatitude = view.findViewById(R.id.deviceLatitude);
        deviceAccuracy = view.findViewById(R.id.deviceAccuracy);
        deviceBearing = view.findViewById(R.id.deviceBearing);
        deviceLongitude = view.findViewById(R.id.deviceLongitude);
        deviceAltitude = view.findViewById(R.id.deviceAltitude);
        deviceBearingAccuracy = view.findViewById(R.id.deviceBearingAccuracy);

        Activity act = getActivity();
        if (act != null) act.runOnUiThread(this::Update);
    }

    private void Update() {
        // Set timers again
        if (mRefreshTimer != null) mRefreshTimer.cancel();

        mRefreshTimer = new Timer("GeneralStatsTimer");
        mRefreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Activity act = getActivity();
                if (act != null) {
                    act.runOnUiThread(() -> DrawCapabilities());
                }
            }
        }, 0, (6 * 333L));
    }

    private void DrawCapabilities() {

        try {
            String unknown = getString(R.string.unknown);
            String supported = getString(R.string.supported);
            String unsupported = getString(R.string.unsupported);

            if (Capabilities != null) {
                // Navigation messages

                // API 34 Data
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    int support = Capabilities.hasAccumulatedDeltaRange();
                    String adrText = unknown;
                    switch (support) {
                        case GnssCapabilities.CAPABILITY_UNKNOWN -> adrText = unknown;
                        case GnssCapabilities.CAPABILITY_SUPPORTED -> adrText = supported;
                        case GnssCapabilities.CAPABILITY_UNSUPPORTED -> adrText = unsupported;
                    }

                    if (hasADR != null) {
                        hasADR.setText(adrText);
                    }
                    if (hasMsa != null) {
                        hasMsa.setText(Capabilities.hasMsa() ? supported : unsupported);
                    }
                    if (supportsSatellitePvt != null) {
                        supportsSatellitePvt.setText(Capabilities.hasSatellitePvt() ? supported : unsupported);
                    }
                } else {
                    if (hasADR != null) {
                        hasADR.setText(unknown);
                    }
                    if (hasMsa != null) {
                        hasMsa.setText(unknown);
                    }
                    if (supportsSatellitePvt != null) {
                        supportsSatellitePvt.setText(unknown);
                    }
                }

                // API 31 data
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (supportsNavigationMessages != null) {
                        supportsNavigationMessages.setText(Capabilities.hasNavigationMessages() ? supported : unsupported);
                    }
                    if (supportsMeasurements != null) {
                        supportsMeasurements.setText(Capabilities.hasMeasurements() ? supported : unsupported);
                    }
                } else {
                    if (supportsNavigationMessages != null) {
                        supportsNavigationMessages.setText(unknown);
                    }
                    if (supportsMeasurements != null) {
                        supportsMeasurements.setText(unknown);
                    }
                }
            }

            // < API 30 data (supported by minimum)
            if (hardwareYear != null) {
                hardwareYear.setText(GnssModelYear != 0 ? String.format(Locale.US, "%d", GnssModelYear) : getString(R.string.unknown_before_2016));
            }
            if (hardwareName != null) {
                hardwareName.setText(GnssModelName == null ? unknown : GnssModelName);
            }
            if (deviceLatitude != null) {
                deviceLatitude.setText(String.valueOf(Latitude));
            }
            if (deviceLongitude != null) {
                deviceLongitude.setText(String.valueOf(Longitude));
            }
            if (deviceAltitude != null) {
                deviceAltitude.setText(String.valueOf(Altitude));
            }
            if (deviceAccuracy != null) {
                deviceAccuracy.setText(String.valueOf(Accuracy));
            }
            if (deviceBearing != null) {
                deviceBearing.setText(String.valueOf(Bearing));
            }
            if (deviceBearingAccuracy != null) {
                deviceBearingAccuracy.setText(String.valueOf(BearingAccuracy));
            }
        } catch (Exception e) {
            Debug.logStack("GeneralStatsPage", "Could not update Stats UI: " + e, 0);
        }
    }
}