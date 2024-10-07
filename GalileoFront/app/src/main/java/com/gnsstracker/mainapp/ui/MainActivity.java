package com.gnsstracker.mainapp.ui;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.material.color.DynamicColors;
import com.google.android.material.elevation.SurfaceColors;

import java.util.List;

public class  MainActivity extends AppCompatActivity {

    private SatelliteRecyclerViewHandler mAdapter;
    private List<SatelliteWidgetEntryData> mData;
    private List<Integer>mIcon;

    private SatelliteDataHandler ddt;
    private static final int LOCATION_REQUEST_ID = 1;
    private static final String[] REQUIRED_PERMISSIONS = {
    };
    private ActivityMainBinding binding;

    private MeasurementProvider mMeasurementProvider;

    private boolean hasPermissions(String[] permissions) {
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode != 33) return;

        int addition = 0;
        for(int value: grantResults) addition += value;

        if(addition != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.missing_permissions_dialog_title));
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.missing_permissions_dialog_explanation));
            builder.setPositiveButton(getString(R.string.dialog_ok), (dialog, which) -> {
                finish();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            recreate();
        }
    }

    private void AskForPermissionsIfNeeded() {
        String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        };

        if(hasPermissions(permissions)) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.permissions_dialog_title));
        builder.setMessage(getString(R.string.permissions_dialog_explanation));
        builder.setCancelable(false);
        builder.setNeutralButton(getString(R.string.dialog_more_details), (dialog, which) -> {
            Intent action = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/griush/GalileoHack/blob/1.0.0-b0/PRIVACY.md"));
            startActivity(action);
            AskForPermissionsIfNeeded();
        });
        builder.setPositiveButton(getString(R.string.dialog_continue), (dialog, which) -> {
            ActivityCompat.requestPermissions(this, permissions, 33);
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        AskForPermissionsIfNeeded();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.landingPage, R.id.generalStatsPage, R.id.satelliteListPage, R.id.dataLogPage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        RegisterGnssDataFetcher();

        int color = SurfaceColors.SURFACE_2.getColor(this);

        Window window = getWindow();
        window.setNavigationBarColor(color);
        window.setNavigationBarContrastEnforced(true);
        window.setStatusBarColor(color);
        window.setStatusBarContrastEnforced(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setBackgroundColor(color);

    }

    @Override
    protected void onDestroy() {
        mMeasurementProvider.unregisterAll();
        super.onDestroy();
    }

    private void RegisterGnssDataFetcher() {
        ddt = new SatelliteDataHandler(this);
        mMeasurementProvider = new MeasurementProvider(getApplicationContext(), ddt);

        LocationManager locationManager = mMeasurementProvider.getLocationManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            GeneralStatsPage.Capabilities = locationManager.getGnssCapabilities();
        }

        GeneralStatsPage.GnssModelName = locationManager.getGnssHardwareModelName();
        GeneralStatsPage.GnssModelYear = locationManager.getGnssYearOfHardware();
        mMeasurementProvider.registerAll();
    }
}