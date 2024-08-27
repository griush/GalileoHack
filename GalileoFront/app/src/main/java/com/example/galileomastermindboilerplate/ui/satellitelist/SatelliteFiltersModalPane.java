package com.example.galileomastermindboilerplate.ui.satellitelist;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.galileomastermindboilerplate.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.galileomastermindboilerplate.databinding.FragmentSatelliteFiltersModalPaneListDialogBinding;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.slider.Slider;

import java.util.function.Consumer;

import kotlin.Unit;

public class SatelliteFiltersModalPane extends BottomSheetDialogFragment {

    public Consumer<Integer> onValuesChanged;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return this.getLayoutInflater().inflate(
                R.layout.fragment_satellite_filters_modal_pane_list_dialog,
                container,
                false);
    }

    public MaterialSwitch EuropeToggle;
    public MaterialSwitch UnitedStatesToggle;
    public MaterialSwitch RussiaToggle;
    public MaterialSwitch ChinaToggle;
    public MaterialSwitch JapanToggle;
    public Button ApplyButton;

    public Slider SpeedSlider;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EuropeToggle = view.findViewById(R.id.EuropeToggle);
        UnitedStatesToggle = view.findViewById(R.id.UnitedStatesToggle);
        RussiaToggle = view.findViewById(R.id.RussiaToggle);
        ChinaToggle = view.findViewById(R.id.ChinaToggle);
        JapanToggle = view.findViewById(R.id.JapanToggle);
        SpeedSlider = view.findViewById(R.id.SpeedSlider);
        ApplyButton = view.findViewById(R.id.ApplyButton);

        EuropeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(onValuesChanged != null) onValuesChanged.accept(0);
        });
        UnitedStatesToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(onValuesChanged != null) onValuesChanged.accept(0);
        });
        RussiaToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(onValuesChanged != null) onValuesChanged.accept(0);
        });
        ChinaToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(onValuesChanged != null) onValuesChanged.accept(0);
        });
        JapanToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(onValuesChanged != null) onValuesChanged.accept(0);
        });
        SpeedSlider.addOnChangeListener((slider, value, fromUser) -> {
            if(onValuesChanged != null) onValuesChanged.accept(0);
        });
        ApplyButton.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

