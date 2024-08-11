package com.example.galileomastermindboilerplate.ui;

import android.app.Activity;
import android.location.GnssClock;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.galileomastermindboilerplate.BaseContent;
import com.example.galileomastermindboilerplate.R;
import com.example.galileomastermindboilerplate.SatelliteWidgetEntryData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.collections.ArrayDeque;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SatelliteListPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SatelliteListPage extends Fragment {

    private int CURRENT_STEP = 0;
    private List<SatelliteWidgetEntryData> mData;
    private List<Integer> mIcon;
    private RecyclerViewAdapter mAdapter;
    private Timer dataTimer;
    public static GnssMeasurementsEvent lastEvent = null;

    public SatelliteListPage() {
        mData = new ArrayList<>();
        mIcon = new ArrayList<>();
    }

    public static SatelliteListPage newInstance() {
        SatelliteListPage fragment = new SatelliteListPage();
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
        // Inflate the layout for this fragment
        dataTimer = new Timer("SatelliteDataTimer");
        dataTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                PrintEventDataToLayout();
            }
        }, 0, 333);
        return inflater.inflate(R.layout.fragment_satellite_list_page, container, false);
    }

    private void PrintEventDataToLayout() {
        Activity activity = getActivity();
        if (activity == null) {
            Log.i("SatelliteListPage", "The page is not selected, skipping");
            return;
        }

        RecyclerView recyclerView = activity.findViewById(R.id.main_list);
        if (recyclerView == null) {
            Log.wtf("SatelliteListPage", "RecyclerView is null but the action is not!");
            return;
        }

        SeekBar speedSelector = activity.findViewById(R.id.speed_selector);
        TextView SpeedText = activity.findViewById(R.id.speed_label);
        activity.runOnUiThread(() -> SpeedText.setText(String.valueOf(speedSelector.getProgress())));

        CURRENT_STEP++;
        if(speedSelector.getProgress() == 0)
            return;
        else if(CURRENT_STEP%(11-speedSelector.getProgress()) != 0)
            return;

        ProgressBar loadingIndicator = activity.findViewById(R.id.progressBar);
        CheckBox AmericaSwitch = activity.findViewById(R.id.AmericaToggle);
        CheckBox EuropeSwitch = activity.findViewById(R.id.EuropeToggle);
        CheckBox RussiaSwitch = activity.findViewById(R.id.RussiaToggle);
        CheckBox ChinaSwitch = activity.findViewById(R.id.ChinaToggle);
        CheckBox JapanSwitch = activity.findViewById(R.id.JapanToggle);


        if (lastEvent != null) {
            GnssMeasurementsEvent event = lastEvent;

            activity.runOnUiThread(() ->
            {
                loadingIndicator.setVisibility(View.GONE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mData.clear();
                mIcon.clear();
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(mData, mIcon);
                recyclerView.setAdapter(adapter);

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
                // adapter.notifyDataSetChanged();
            });
        }
        else {
            activity.runOnUiThread(() -> loadingIndicator.setVisibility(View.VISIBLE));
        }

    }
}