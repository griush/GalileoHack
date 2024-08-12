package com.example.galileomastermindboilerplate.ui.satellitelist;

import android.app.Activity;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import com.example.galileomastermindboilerplate.R;
import com.example.galileomastermindboilerplate.SatelliteWidgetEntryData;
import com.example.galileomastermindboilerplate.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private int Speed = 8;
    private boolean Europe = true;
    private boolean UnitedStates = true;
    private boolean Russia = true;
    private boolean China = true;
    private boolean Japan = true;

    SatelliteFiltersModalPane ModalBottomFiltersPanel;
    ExtendedFloatingActionButton FiltersButton;
    SwipeRefreshLayout PullToRefresh;
    ProgressBar LoadingIndicator;


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

        ModalBottomFiltersPanel = new SatelliteFiltersModalPane();
        FiltersButton = view.findViewById(R.id.floatingActionButton);
        LoadingIndicator = view.findViewById(R.id.progressBar);

        CompoundButton.OnCheckedChangeListener onCheckedChange = (buttonView, isChecked) -> {
            Activity act = getActivity();
            if(act != null) act.runOnUiThread(this::PrintEventDataToLayout);
        };

        ModalBottomFiltersPanel.setCancelable(true);
        ModalBottomFiltersPanel.onValuesChanged = (Integer result) -> {
            Europe = ModalBottomFiltersPanel.EuropeToggle.isChecked();
            UnitedStates = ModalBottomFiltersPanel.UnitedStatesToggle.isChecked();
            Russia = ModalBottomFiltersPanel.RussiaToggle.isChecked();
            China = ModalBottomFiltersPanel.ChinaToggle.isChecked();
            Japan = ModalBottomFiltersPanel.JapanToggle.isChecked();
            Speed = (int)ModalBottomFiltersPanel.SpeedSlider.getValue();

            Activity act = getActivity();
            if(act != null) act.runOnUiThread(this::UpdateSpeed);
        };

        FiltersButton.setOnClickListener(v -> {
            ModalBottomFiltersPanel.show(getParentFragmentManager(), "SatelliteFiltersModalPane");
        });

        PullToRefresh = view.findViewById(R.id.SwipeToRefreshSatellites);
        PullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh()
            {
                PrintEventDataToLayout();
                PullToRefresh.setRefreshing(false);//if false then refresh progress dialog will disappear when page loading finish, but if it is true then always the refresh load dialog show even after the page load or not
            }
        });


        Activity act = getActivity();
        if(act != null) act.runOnUiThread(this::UpdateSpeed);
    }

    private void UpdateSpeed()
    {
        // Set timers again
        if(dataTimer != null) dataTimer.cancel();

        if(Speed != 0) {
            PullToRefresh.setEnabled(false);
            dataTimer = new Timer("SatelliteDataTimer");
            dataTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    PrintEventDataToLayout();
                }
            }, 0, ((11-Speed)*333L));
        }
        else
        {
            PullToRefresh.setEnabled(true);
        }

        PrintEventDataToLayout();
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
                    int type = measurement.getConstellationType();
                    if (!Europe && type == GnssStatus.CONSTELLATION_GALILEO
                            || !UnitedStates && type == GnssStatus.CONSTELLATION_GPS
                            || !Russia && type == GnssStatus.CONSTELLATION_GLONASS
                            || !China && type == GnssStatus.CONSTELLATION_BEIDOU
                            || !Japan && type == GnssStatus.CONSTELLATION_QZSS)
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