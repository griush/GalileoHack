package com.example.galileomastermindboilerplate.ui;

import android.app.Activity;
import android.location.GnssClock;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.galileomastermindboilerplate.BaseContent;
import com.example.galileomastermindboilerplate.R;
import com.example.galileomastermindboilerplate.SatelliteWidgetEntryData;
import com.example.galileomastermindboilerplate.databinding.RecyclerViewItem1Binding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;

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
    private List<SatelliteWidgetEntryData> mData;
    private List<Integer> mIcon;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerViewAdapter mAdapter;
    private Timer dataTimer;
    public static GnssMeasurementsEvent lastEvent = null;

    FloatingActionButton FiltersButton;
    Slider SpeedSelector;
    TextView SpeedLabel;
    ProgressBar LoadingIndicator;
    CheckBox AmericaSwitch;
    CheckBox EuropeSwitch;
    CheckBox RussiaSwitch;
    CheckBox ChinaSwitch;
    CheckBox JapanSwitch;


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

        return inflater.inflate(R.layout.fragment_satellite_list_page, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FiltersButton = view.findViewById(R.id.floatingActionButton);
        SpeedLabel = view.findViewById(R.id.speed_label);
        SpeedSelector = view.findViewById(R.id.speed_selector);
        LoadingIndicator = view.findViewById(R.id.progressBar);
        AmericaSwitch = view.findViewById(R.id.AmericaToggle);
        EuropeSwitch = view.findViewById(R.id.EuropeToggle);
        RussiaSwitch = view.findViewById(R.id.RussiaToggle);
        ChinaSwitch = view.findViewById(R.id.ChinaToggle);
        JapanSwitch = view.findViewById(R.id.JapanToggle);

        CompoundButton.OnCheckedChangeListener onCheckedChange = (buttonView, isChecked) -> {
            Activity act = getActivity();
            if(act != null) act.runOnUiThread(this::PrintEventDataToLayout);
        };

        AmericaSwitch.setOnCheckedChangeListener(onCheckedChange);
        EuropeSwitch.setOnCheckedChangeListener(onCheckedChange);
        RussiaSwitch.setOnCheckedChangeListener(onCheckedChange);
        ChinaSwitch.setOnCheckedChangeListener(onCheckedChange);
        JapanSwitch.setOnCheckedChangeListener(onCheckedChange);

        FiltersButton.setOnClickListener(v -> {});
        SpeedSelector.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                UpdateSpeed();
            }
        });

        FiltersButton.setOnClickListener(v -> {
            SatelliteFiltersModalPane modalBottomSheet = new SatelliteFiltersModalPane();
            modalBottomSheet.show(getParentFragmentManager(), "SatelliteFiltersModalPane");
        });

        Activity act = getActivity();
        if(act != null) act.runOnUiThread(this::UpdateSpeed);
    }

    private void UpdateSpeed()
    {
        int SPEED = (int)SpeedSelector.getValue();
        SpeedLabel.setText(String.valueOf(SPEED));

        // Set timers again
        if(dataTimer != null) dataTimer.cancel();

        if(SPEED != 0) {
            dataTimer = new Timer("SatelliteDataTimer");
            dataTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    PrintEventDataToLayout();
                }
            }, 0, ((11-SPEED)*333L));
        }
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

        if (lastEvent != null) {
            GnssMeasurementsEvent event = lastEvent;

            activity.runOnUiThread(() ->
            {
                LoadingIndicator.setVisibility(View.GONE);
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
            });
        }
        else {
            activity.runOnUiThread(() -> LoadingIndicator.setVisibility(View.VISIBLE));
        }

    }
}