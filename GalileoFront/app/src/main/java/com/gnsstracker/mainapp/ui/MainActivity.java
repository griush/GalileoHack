package com.gnsstracker.mainapp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Window;


import com.gnsstracker.mainapp.MeasurementProvider;
import com.gnsstracker.mainapp.R;
import com.gnsstracker.mainapp.SatelliteDataHandler;
import com.gnsstracker.mainapp.SatelliteWidgetEntryData;
import com.gnsstracker.mainapp.ui.satellitelist.SatelliteRecyclerViewHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gnsstracker.mainapp.databinding.ActivityMainBinding;
import com.google.android.material.elevation.SurfaceColors;

import java.util.List;

public class  MainActivity extends AppCompatActivity {

    private SatelliteRecyclerViewHandler mAdapter;
    private List<SatelliteWidgetEntryData> mData;
    private List<Integer>mIcon;

    private SatelliteDataHandler ddt;
    private static final int LOCATION_REQUEST_ID = 1;
    private static final String[] REQUIRED_PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private ActivityMainBinding binding;

    private MeasurementProvider mMeasurementProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.landingPage, R.id.generalStatsPage, R.id.satelliteListPage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        requestPermissionAndSetupFragments(this);

        int color = SurfaceColors.SURFACE_2.getColor(this);

        Window window = getWindow();
        window.setNavigationBarColor(color);
        window.setStatusBarColor(color);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setBackgroundColor(color);

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
        // Check for permissions to avoid crash on firs startup

        // TODO: There is a bug here when the first time launching the app
        // it asks for permissions and when the user accepts, Capabilities are
        // not updated, until fully restarting the app
        if (hasPermissions(this)) {
            GeneralStatsPage.Capabilities = locationManager.getGnssCapabilities();
            GeneralStatsPage.GnssModelName = locationManager.getGnssHardwareModelName();
            GeneralStatsPage.GnssModelYear = locationManager.getGnssYearOfHardware();
            mMeasurementProvider.registerAll();
        }
    }

    private boolean hasPermissions(Activity activity) {
        for (String p : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        // wtf is this here
        // startHere();

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