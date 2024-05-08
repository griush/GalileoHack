//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;

package com.example.galileomastermindboilerplate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.GnssClock;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import static android.location.GnssMeasurement.STATE_GAL_E1C_2ND_CODE_LOCK;
import static android.location.GnssMeasurement.STATE_TOW_DECODED;
import static android.location.GnssMeasurement.STATE_TOW_KNOWN;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class LogFragment extends MainActivity implements MeasurementListener {

    public Activity activity;
    private GALIProcessor processer;

    private List<SatelliteWidgetEntryData> mData;
    private List<Integer> mIcon;
    private RecyclerView recyclerView;
    private CheckBox EuropeSwitch;
    private CheckBox AmericaSwitch;
    private CheckBox ChinaSwitch;
    private CheckBox JapanSwitch;
    private CheckBox RussiaSwitch;
    private CheckBox PauseToggle;
    private TextView AverageSignalStrength;
    private TextView CurrentSignalStrength;
    private TextView DeviceLocationDisplay;

    private CheckBox SlowPaceCheckBox;
    private int currentCounter = 0;

    // private WebView OpenStreetMap;
    private RecyclerViewAdapter adapter;

    public LogFragment(Activity _activity) {

        this.activity = _activity;
        mData = new ArrayList<>();
        mIcon = new ArrayList<>();
        processer = new GALIProcessor(_activity);
    }

    LocationManager locationManager;


    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onLocationStatusChanged(String provider, int status, Bundle extras) {

    }

    private static class FetchResponse {
        // {"lat":41.0543,"lon":2.34254,"signal":35.263653}
        public double signal;
    }

    // String ServerLocation = "UNSET";
    String DeviceLocation = "UNSET";
    String ServerSignalStrength = "UNSET";
    Location currentLocation = null;

    final int TOW_DECODED_MEASUREMENT_STATE_BIT = 3;

    //check long to double conversions
    public double[] computeGalileoPseudorange(GnssClock clock, GnssMeasurement measurement, boolean useE1BC) {
        final int SECONDS_PER_WEEK = 3600 * 24 * 7;
        final double C_METRES_PER_SECOND = 299792458.0;
        final long NANOSECONDS_PER_SECOND = 1000000000, NANOSECONDS_PER_E1C_2ND_CODE_PERIOD = 100000000;
        long moduloPeriod, WeekNumberNanos, WeekNumber = 0;
        double towRxSeconds = 0.0, towTxSeconds = 0.0, pseudoRange = 0.0;
        long towRxNanos, tRx;
        double tTx, tRxSec, tTxSec;

        boolean towAvail = ((measurement.getState() & STATE_TOW_DECODED) > 0) || ((measurement.getState() & STATE_TOW_KNOWN) > 0);

        if (towAvail)
            moduloPeriod = NANOSECONDS_PER_SECOND * SECONDS_PER_WEEK;
        else //PC
            if ((measurement.getState() & STATE_GAL_E1C_2ND_CODE_LOCK) > 0)
                moduloPeriod = NANOSECONDS_PER_E1C_2ND_CODE_PERIOD;
            else
                moduloPeriod = 4000000L;

        if (towAvail ||
                ((measurement.getState() & STATE_GAL_E1C_2ND_CODE_LOCK) > 0) ||
                (((measurement.getState() & (1024 + 32)) == (1024 + 32)) && useE1BC)) {

            long FullBiasNanos = clock.getFullBiasNanos();
            // double BiasNanos = clock.getBiasNanos(); removed because it is double and always 0

            WeekNumber = -clock.getFullBiasNanos() / (SECONDS_PER_WEEK * NANOSECONDS_PER_SECOND);
            //WeekNumber = (long) Math.floor(-clock.getFullBiasNanos()*1e-9/SECONDS_PER_WEEK);

            WeekNumberNanos = WeekNumber * NANOSECONDS_PER_SECOND * SECONDS_PER_WEEK;
            towRxNanos = clock.getTimeNanos() - FullBiasNanos - WeekNumberNanos;

            if (towRxNanos > 604800000000000L) {
                towRxNanos = towRxNanos - 604800000000000L;
                WeekNumber = WeekNumber + 1;
            }

            tRx = towRxNanos % moduloPeriod; // in ns
            towRxSeconds = towRxNanos * 1e-9; // ToW at RX
            tTx = measurement.getReceivedSvTimeNanos() + measurement.getTimeOffsetNanos(); // in ns

            tRxSec = tRx * 1e-9;
            tTxSec = tTx * 1e-9;

            pseudoRange = ((tRxSec - tTxSec) % (moduloPeriod * 1e-9)) * C_METRES_PER_SECOND; // in meters
            if (pseudoRange < 0.0) // mod operation can return negative values
                pseudoRange = pseudoRange + (moduloPeriod * 1e-9) * C_METRES_PER_SECOND;
        }

        double[] result = {pseudoRange, towRxSeconds, WeekNumber};
        return result;
    }

    @Override
    public void onGnssMeasurementsReceived(GnssMeasurementsEvent event) {

//        mData = new ArrayList<>();
//        mIcon = new ArrayList<>();
        PauseToggle = activity.findViewById(R.id.PauseCheckBox);
        if (PauseToggle.isChecked())
            return;

        SlowPaceCheckBox = activity.findViewById(R.id.SlowPaceCheckBox);
        if ((currentCounter++) % 4 != 0 && SlowPaceCheckBox.isChecked())
            return;

        BaseContent serializable = new BaseContent();

        EuropeSwitch = activity.findViewById(R.id.EuropeToggle);
        AmericaSwitch = activity.findViewById(R.id.AmericaToggle);
        RussiaSwitch = activity.findViewById(R.id.RussiaToggle);
        ChinaSwitch = activity.findViewById(R.id.ChinaToggle);
        JapanSwitch = activity.findViewById(R.id.JapanToggle);
        for (GnssMeasurement measurement : event.getMeasurements()) {
            if (!EuropeSwitch.isChecked() && measurement.getConstellationType() == GnssStatus.CONSTELLATION_GALILEO
                    || !AmericaSwitch.isChecked() && measurement.getConstellationType() == GnssStatus.CONSTELLATION_GPS
                    || !RussiaSwitch.isChecked() && measurement.getConstellationType() == GnssStatus.CONSTELLATION_GLONASS
                    || !ChinaSwitch.isChecked() && measurement.getConstellationType() == GnssStatus.CONSTELLATION_BEIDOU
                    || !JapanSwitch.isChecked() && measurement.getConstellationType() == GnssStatus.CONSTELLATION_QZSS)
                continue;

            if (((measurement.getState() & (1L << TOW_DECODED_MEASUREMENT_STATE_BIT)) == 0))
                continue;

            SatelliteWidgetEntryData item = new SatelliteWidgetEntryData();
            item.Svid = measurement.getSvid();
            item.CarrierFrequencyHz = measurement.getCarrierFrequencyHz();
            item.SnrInDb = measurement.getSnrInDb();
            item.ReceivedSvTimeNanos = measurement.getReceivedSvTimeNanos();
            item.TimeOffsetNanos = measurement.getTimeOffsetNanos();
            item.ReceivedSvTimeUncertaintyNanos = measurement.getReceivedSvTimeUncertaintyNanos();
            item.Cn0DbHz = measurement.getCn0DbHz();
            item.PseudorangeRateMetersPerSecond = measurement.getPseudorangeRateMetersPerSecond();
            item.PseudorangeRateUncertaintyMetersPerSeconds = measurement.getPseudorangeRateUncertaintyMetersPerSecond();
            item.AccumulatedDeltaRangeState = measurement.getAccumulatedDeltaRangeState();
            item.AccumulatedDeltaRangeMeters = measurement.getAccumulatedDeltaRangeMeters();
            item.AccumulatedDeltaRangeUncertaintyMeters = measurement.getAccumulatedDeltaRangeUncertaintyMeters();
            item.CarrierCycles = measurement.getCarrierCycles();
            item.CarrierPhase = measurement.getCarrierPhase();
            item.CarrierPhaseUncertainty = measurement.getCarrierPhaseUncertainty();
            item.ConstellationType = measurement.getConstellationType();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                item.FullInterSignalBiasNanos = measurement.getFullInterSignalBiasNanos();
                item.FullInterSignalBiasUncertaintyNanos = measurement.getFullInterSignalBiasUncertaintyNanos();
                item.SatelliteInterSignalBiasNanos = measurement.getSatelliteInterSignalBiasNanos();
            }

            GnssClock clock = event.getClock();

            item.timeNanos = clock.getTimeNanos();
            item.clockBiasedNanos = clock.getBiasNanos();
            item.clockFullBiasedNanos = clock.getFullBiasNanos();

            double rho = computeGalileoPseudorange(clock, measurement, true)[0];
            System.out.println("rho: " + rho);
            item.pseudorange = rho;

            serializable.Satellites.add(item);
            serializable.SatelliteCount++;
        }

        ServerSignalStrength = String.format("%.2f", 17.40);

        try {
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

            @SuppressLint("MissingPermission") Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                DeviceLocation = "Lat: " + String.format("%.4f", lastKnownLocation.getLatitude()) + "\nLon: " + String.format("%.4f", lastKnownLocation.getLongitude());
            }
        } catch (Exception ex) {
            DeviceLocation = ex.toString();
        }

        activity.runOnUiThread(() -> {

            // Find RecyclerView by ID
            recyclerView = activity.findViewById(R.id.main_list);

            CurrentSignalStrength = activity.findViewById(R.id.CurrentSignalStrengthDisplay);
            AverageSignalStrength = activity.findViewById(R.id.AverageSignalStrength);
            DeviceLocationDisplay = activity.findViewById(R.id.DeviceLocationDisplay);


            /*if (OpenStreetMap.getUrl() != openstreetmap_url && !openstreetmap_url.isEmpty()) {
                OpenStreetMap.loadUrl(openstreetmap_url);
            }*/


//                mData = new ArrayList<>();
//                mIcon = new ArrayList<>();
            // Set layout manager
            mData.clear();
            adapter = new RecyclerViewAdapter(mData, mIcon);
            recyclerView.setAdapter(adapter);


            double CurrentSignalAverage = 0;
            int CurrentSignalCount = 0;

            for (GnssMeasurement measurement : event.getMeasurements()) {

                if (!EuropeSwitch.isChecked() && measurement.getConstellationType() == GnssStatus.CONSTELLATION_GALILEO
                        || !AmericaSwitch.isChecked() && measurement.getConstellationType() == GnssStatus.CONSTELLATION_GPS
                        || !RussiaSwitch.isChecked() && measurement.getConstellationType() == GnssStatus.CONSTELLATION_GLONASS
                        || !ChinaSwitch.isChecked() && measurement.getConstellationType() == GnssStatus.CONSTELLATION_BEIDOU
                        || !JapanSwitch.isChecked() && measurement.getConstellationType() == GnssStatus.CONSTELLATION_QZSS) {
                    continue;
                }

                SatelliteWidgetEntryData item = new SatelliteWidgetEntryData();
                item.Svid = measurement.getSvid();
                item.CarrierFrequencyHz = measurement.getCarrierFrequencyHz();
                item.SnrInDb = measurement.getSnrInDb();
                item.ReceivedSvTimeNanos = measurement.getReceivedSvTimeNanos();
                item.TimeOffsetNanos = measurement.getTimeOffsetNanos();
                item.ReceivedSvTimeUncertaintyNanos = measurement.getReceivedSvTimeUncertaintyNanos();
                item.Cn0DbHz = measurement.getCn0DbHz();
                item.PseudorangeRateMetersPerSecond = measurement.getPseudorangeRateMetersPerSecond();
                item.PseudorangeRateUncertaintyMetersPerSeconds = measurement.getPseudorangeRateUncertaintyMetersPerSecond();
                item.AccumulatedDeltaRangeState = measurement.getAccumulatedDeltaRangeState();
                item.AccumulatedDeltaRangeMeters = measurement.getAccumulatedDeltaRangeMeters();
                item.AccumulatedDeltaRangeUncertaintyMeters = measurement.getAccumulatedDeltaRangeUncertaintyMeters();
                item.CarrierCycles = measurement.getCarrierCycles();
                item.CarrierPhase = measurement.getCarrierPhase();
                item.CarrierPhaseUncertainty = measurement.getCarrierPhaseUncertainty();
                item.ConstellationType = measurement.getConstellationType();
                item.AGC = measurement.getAutomaticGainControlLevelDb();

                mData.add(item);
                mIcon.add(R.drawable.rawmeas);

            }
            for (GnssMeasurement measurement : event.getMeasurements()) {

                CurrentSignalAverage += measurement.getCn0DbHz();
                CurrentSignalCount++;

            }
            adapter.notifyDataSetChanged();
            CurrentSignalStrength.setText(String.format("%.2f", (float) (CurrentSignalAverage / CurrentSignalCount)) + " dBHz");
            AverageSignalStrength.setText(ServerSignalStrength + " dBHz");
            DeviceLocationDisplay.setText(DeviceLocation);


            // recyclerView.scrollToPosition(adapter.getItemCount() -1);

        });
    }

    private String toStringMeasurement(GnssMeasurement measurement) {
        return "";
    }

    @Override
    public void onGnssMeasurementsStatusChanged(int status) {

    }

    @Override
    public void onGnssNavigationMessageReceived(GnssNavigationMessage event) throws MalformedURLException {
        System.out.println("NEW EVENT " + event.getSvid() + ", " + event.getType());
        //Log.i("DEB","NEW EVENT "+event.getSvid()+", "+event.getType());
        if (event.getType() == GnssNavigationMessage.TYPE_GAL_F || event.getType() == GnssNavigationMessage.TYPE_GAL_I)
            Log.i("DEB", "EVENT");
        if (event.getType() != GnssNavigationMessage.TYPE_GAL_I) return;
        processer.onNewPage(event.getData(), event.getSvid());
    }

    @Override
    public void onGnssNavigationMessageStatusChanged(int status) {

    }

    @Override
    public void onGnssStatusChanged(GnssStatus gnssStatus) {

    }

    @Override
    public void onListenerRegistration(String listener, boolean result) {

    }

    @Override
    public void onNmeaReceived(long l, String s) {

    }

    @Override
    public void onTTFFReceived(long l) {

    }
}


