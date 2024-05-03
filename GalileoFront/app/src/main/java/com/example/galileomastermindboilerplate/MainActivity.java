package com.example.galileomastermindboilerplate;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galileomastermindboilerplate.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewAdapter mAdapter;
    private List<String> mData;
    private List<Integer>mIcon;

    private LogFragment ddt;
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
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        requestPermissionAndSetupFragments(this);

        RecyclerView recyclerView = findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        mData = new ArrayList<>();
        mIcon = new ArrayList<>();

        mAdapter = new RecyclerViewAdapter(mData,mIcon);
        recyclerView.setAdapter(mAdapter);

        // Initially populate mData with some data
        mData.add("State dummy...");
        mIcon.add(R.drawable.state);
        mData.add("Raw measurement dummy...");
        mIcon.add(R.drawable.rawmeas);
        mData.add("Navigation message dummy...");
        mIcon.add(R.drawable.navmsg);
        mData.add("NMEA dummy...");
        mIcon.add(R.drawable.nmea);
        mData.add("AGC dummy...");
        mIcon.add(R.drawable.agc);
        mData.add("Fix dummy...");
        mIcon.add(R.drawable.fix);


        // Add more items as needed
        mAdapter.notifyDataSetChanged();




    }

    @Override
    protected void onDestroy() {
        mMeasurementProvider.unregisterAll();
        super.onDestroy();
    }


    private void startHere() {

        ddt = new LogFragment(this);
        mMeasurementProvider = new MeasurementProvider(getApplicationContext(), ddt);
      //  mMeasurementProvider = new MeasurementProvider(getApplicationContext());

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