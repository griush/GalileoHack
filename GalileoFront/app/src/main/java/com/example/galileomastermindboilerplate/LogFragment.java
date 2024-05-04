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

import static android.net.wifi.WifiConfiguration.Status.strings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssClock;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.Switch;
import android.widget.TextView;


import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LogFragment extends MainActivity implements MeasurementListener {

    public Activity activity;

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
    private TextView ServerLocationDisplay;
    private WebView OpenStreetMap;
    private RecyclerViewAdapter adapter;

    public LogFragment(Activity _activity) {

        this.activity = _activity;
        mData = new ArrayList<>();
        mIcon = new ArrayList<>();

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
        public double lat;
        public double lon;
        public double signal;
    }

    String ServerLocation = "UNSET";
    String DeviceLocation = "UNSET";
    String ServerSignalStrength = "UNSET";
    Location currentLocation = null;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onGnssMeasurementsReceived(GnssMeasurementsEvent event) {

//        mData = new ArrayList<>();
//        mIcon = new ArrayList<>();
        try {
            PauseToggle = activity.findViewById(R.id.PauseCheckBox);
            if(PauseToggle.isChecked())
                return;

            String json;
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
                System.out.println("Time: " + item.timeNanos);
                System.out.println("BIAS: " + item.clockBiasedNanos);
                System.out.println("FuBi: " + item.clockFullBiasedNanos);
                System.out.println("SvTi: " + item.ReceivedSvTimeNanos);
                System.out.println("Offs: " + measurement.getTimeOffsetNanos());
                System.out.println("tRx_GNSS: " + (item.timeNanos - (item.clockFullBiasedNanos + item.clockBiasedNanos)));
                long tow = ((long)(clock.getTimeNanos() - (item.clockFullBiasedNanos + item.clockBiasedNanos))) % (7*24*60*60*1000000000);
                System.out.println("tRx_GNSS_TOW: " + (tow));
                double tTx = measurement.getReceivedSvTimeNanos();
                System.out.println("tTx: " + tTx);
                System.out.println("rho: " + ((tow - tTx) * 300000000 * 1e-9));

                serializable.Satellites.add(item);
                serializable.SatelliteCount++;
            }

            try {
                json = new ObjectMapper().writeValueAsString(serializable);
                System.out.println(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }


            URL url = new URL(ServerHostname.HOSTNAME);

            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            // on below line setting method as post.
            client.setRequestMethod("POST");

            // on below line setting content type and accept type.
            client.setRequestProperty("Content-Type", "application/json");
            client.setRequestProperty("Accept", "application/json");

            // on below line setting client.
            client.setDoOutput(true);

            // on below line we are creating an output stream and posting the data.
            try (OutputStream os = client.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            String JsonContent = "";

            // on below line creating and initializing buffer reader.
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(client.getInputStream(), "utf-8"))) {

                // on below line creating a string builder.
                StringBuilder response = new StringBuilder();

                // on below line creating a variable for response line.
                String responseLine = null;

                // on below line writing the response
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonContent = response.toString();

                ObjectMapper objectMapper = new ObjectMapper();
                FetchResponse dataResponse = objectMapper.readValue(JsonContent, FetchResponse.class);

                ServerLocation = "Lat: " + dataResponse.lat + "\nLon: " + dataResponse.lon;
                ServerSignalStrength = String.format("%.2f", dataResponse.signal);

                try {
                    locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }*/
                    @SuppressLint("MissingPermission") Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    DeviceLocation = "Lat: " + String.format("%.4f", lastKnownLocation.getLatitude()) + "\nLon: " + String.format("%.4f",lastKnownLocation.getLongitude());
                }
            } catch (Exception ex)
            {
                DeviceLocation = ex.toString();
            }


        } catch (Exception e) {
            e.printStackTrace();
            ServerLocation = e.toString();
            ServerSignalStrength = e.toString();
        }



        activity.runOnUiThread(() -> {

            // Find RecyclerView by ID
            recyclerView = activity.findViewById(R.id.main_list);

            CurrentSignalStrength = activity.findViewById(R.id.CurrentSignalStrengthDisplay);
            AverageSignalStrength = activity.findViewById(R.id.AverageSignalStrength);
            ServerLocationDisplay = activity.findViewById(R.id.CurrentLocationDisplay);
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
                // builder.append(toStringMeasurement(measurement));
                // System.out.println(event.getMeasurements().size());
                // builder.append("\n");

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
            ServerLocationDisplay.setText(ServerLocation);
            DeviceLocationDisplay.setText(DeviceLocation);


            // recyclerView.scrollToPosition(adapter.getItemCount() -1);

        });
    } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String toStringMeasurement(GnssMeasurement measurement) {
       return "";
    }

    @Override
    public void onGnssMeasurementsStatusChanged(int status) {

    }

    @Override
    public void onGnssNavigationMessageReceived(GnssNavigationMessage event) {


        // here
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


