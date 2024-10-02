package com.gnsstracker.mainapp.ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gnsstracker.mainapp.R;
import com.google.android.material.elevation.SurfaceColors;

import java.util.ArrayList;

public class DataLogPage extends Fragment {

    // Messages are added here
    // mainapp/SatelliteDataHandler.java at line 328
    public static ArrayList<String> consoleContents = new ArrayList<>();
    private TextView logView;
    private ScrollView scrollView;
    private CountDownTimer dataTimer;

    public DataLogPage() {}

    public static DataLogPage newInstance(String param1, String param2) {
        DataLogPage fragment = new DataLogPage();
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
        var view = inflater.inflate(R.layout.fragment_data_log_page, container, false);
        logView = view.findViewById(R.id.DataLogTextView);

        scrollView = view.findViewById(R.id.DataLogScrollView);
        int color = SurfaceColors.SURFACE_1.getColor(this.getContext());
        scrollView.setBackgroundColor(color);

        dataTimer = new CountDownTimer(500, 10) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                LoadLog();
                dataTimer.start();
            }
        };
        dataTimer.start();
        return view;
    }

    public void LoadLog()
    {
        if(consoleContents.isEmpty()) return;

        // BEGIN maintain on bottom if user is on bottom
        View lastChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
        int bottom = lastChild.getBottom() + scrollView.getPaddingBottom();
        int sy = scrollView.getScrollY();
        int sh = scrollView.getHeight();
        int delta = bottom - (sy + sh);

        if(delta < 100 && delta > 0)
        {
            scrollView.scrollBy(0, delta);
        }
        // END maintain on bottom if user is on bottom



        StringBuilder sb = new StringBuilder();
        sb.append(logView.getText());
        for(int i = 0; i < consoleContents.size(); i++)
        {
            sb.append(consoleContents.get(i));

            // Per que cony aixo no cambia el resultat >:(
            // sb.append(consoleContents.get(consoleContents.size() - i - 1));
            sb.append("\n");
        }
        consoleContents.clear();
        logView.setText(sb.toString());

    }
}