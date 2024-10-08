package com.gnsstracker.mainapp.ui.satellitelist;

import static java.lang.Math.min;

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

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gnsstracker.mainapp.R;
import com.gnsstracker.mainapp.SatelliteWidgetEntryData;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SatelliteListPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SatelliteListPage extends Fragment {
    private List<SatelliteWidgetEntryData> CurrentSatelliteData;



    private SatelliteRecyclerViewHandler mAdapter;
    private CountDownTimer dataTimer;
    public static GnssMeasurementsEvent lastEvent = null;

    private RecyclerView SatellitesView;
    private SatelliteRecyclerViewHandler SatellitesViewHandler;

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
    CircularProgressIndicator TimerProgressBar;
    TextView TimerTextView;


    public SatelliteListPage() {
        CurrentSatelliteData = new ArrayList<>();
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_satellite_list_page, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ModalBottomFiltersPanel = new SatelliteFiltersModalPane();
        FiltersButton = view.findViewById(R.id.floatingActionButton);
        LoadingIndicator = view.findViewById(R.id.progressBar);
        TimerTextView = view.findViewById(R.id.timerTextView);
        TimerProgressBar = view.findViewById(R.id.timerProgressBar);

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

        FiltersButton.extend();
        FiltersButton.setOnClickListener(v -> {
            ModalBottomFiltersPanel.show(getParentFragmentManager(), "SatelliteFiltersModalPane");
        });

        PullToRefresh = view.findViewById(R.id.SwipeToRefreshSatellites);
        PullToRefresh.setOnRefreshListener(() -> {
            PrintEventDataToLayout();
            PullToRefresh.setRefreshing(false);
        });


        SatellitesViewHandler = new SatelliteRecyclerViewHandler(CurrentSatelliteData, getActivity());
        SatellitesView = view.findViewById(R.id.SatellitesRecyclerView);
        SatellitesView.setAdapter(SatellitesViewHandler);
        SatellitesView.setLayoutManager(new LinearLayoutManager(getContext()));

        Activity act = getActivity();
        if(act != null) act.runOnUiThread(this::UpdateSpeed);
    }

    private void UpdateSpeed()
    {
        // Set timers again
        if(dataTimer != null) dataTimer.cancel();

        if(Speed != 0) {
            PullToRefresh.setEnabled(false);
            int MIN = 0;
            int MAX = (11-Speed)*333;
            dataTimer = new CountDownTimer(MAX, 10) {
                @Override
                public void onTick(long l) {
                        TimerProgressBar.setProgress(/*MAX - */(int)l);
                }

                @Override
                public void onFinish() {
                    PrintEventDataToLayout();
                    dataTimer.start();
                }
            };
            dataTimer.start();
            TimerProgressBar.setMin(MIN);
            TimerProgressBar.setMax(MAX);
        }
        else
        {
            PullToRefresh.setEnabled(true);
            TimerProgressBar.setMin(0);
            TimerProgressBar.setMax(1);
            TimerProgressBar.setProgress(0);
        }

        PrintEventDataToLayout();
    }

    private void PrintEventDataToLayout() {
        Activity activity = getActivity();
        if (activity == null) {
            Log.i("SatelliteListPage", "The page is not selected, skipping");
            return;
        }


        if (SatellitesView == null) {
            Log.wtf("SatelliteListPage", "RecyclerView is null but the action is not!");
            return;
        }

        if (lastEvent != null) {
            activity.runOnUiThread(() ->
            {
                String lastUpdatedString = getString(R.string.last_updated);
                TimerTextView.setText(lastUpdatedString + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
                TimerProgressBar.setVisibility(View.VISIBLE);
                LoadingIndicator.setVisibility(View.INVISIBLE);
                CurrentSatelliteData.clear();

                int currentScrollPosition = SatellitesView.getVerticalScrollbarPosition();

                for (GnssMeasurement measurement : lastEvent.getMeasurements()) {
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

                    CurrentSatelliteData.add(item);
                    SatellitesViewHandler.updateData(CurrentSatelliteData);

                    SatellitesView.setVerticalScrollbarPosition(currentScrollPosition);
                }
            });
        }
        else {
            activity.runOnUiThread(() -> LoadingIndicator.setVisibility(View.VISIBLE));
        }

    }
}