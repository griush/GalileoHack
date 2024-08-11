package com.example.galileomastermindboilerplate.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;


import com.example.galileomastermindboilerplate.MeasurementProvider;
import com.example.galileomastermindboilerplate.R;
import com.example.galileomastermindboilerplate.SatelliteDataHandler;
import com.example.galileomastermindboilerplate.SatelliteWidgetEntryData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.galileomastermindboilerplate.databinding.ActivityMainBinding;

import java.util.List;

public class  MainActivity extends AppCompatActivity {

    private RecyclerViewAdapter mAdapter;
    private List<SatelliteWidgetEntryData> mData;
    private List<Integer>mIcon;

    private SatelliteDataHandler ddt;
    private static final int LOCATION_REQUEST_ID = 1;
    private static final String[] REQUIRED_PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ActivityMainBinding binding;

    private MeasurementProvider mMeasurementProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.landingPage, R.id.generalStatsPage, R.id.satelliteListPage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        requestPermissionAndSetupFragments(this);
    }

    @Override
    protected void onDestroy() {
        mMeasurementProvider.unregisterAll();
        super.onDestroy();
    }


    private void startHere() {

        ddt = new SatelliteDataHandler(this);
        mMeasurementProvider = new MeasurementProvider(getApplicationContext(), ddt);
      //  mMeasurementProvider = new MeasurementProvider(getApplicationContext());

        // Pass capabilities to stats page
        LocationManager locationManager = mMeasurementProvider.getLocationManager();
        GeneralStatsPage.Capabilities = locationManager.getGnssCapabilities();

        mMeasurementProvider.registerAll();

    }

    private boolean hasPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Permissions granted at install time.
            return true;
        }
        for (String p : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        startHere();

        return true;
    }

    private void requestPermissionAndSetupFragments(final Activity activity) {
        if (hasPermissions(activity)) {
            startHere();
        } else {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, LOCATION_REQUEST_ID);
            startHere();
        }
    }


}