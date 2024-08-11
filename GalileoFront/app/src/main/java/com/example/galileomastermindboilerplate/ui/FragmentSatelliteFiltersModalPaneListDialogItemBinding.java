package com.example.galileomastermindboilerplate.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.galileomastermindboilerplate.R;

public class FragmentSatelliteFiltersModalPaneListDialogItemBinding {
    @NonNull
    public final TextView text;

    private FragmentSatelliteFiltersModalPaneListDialogItemBinding(@NonNull View rootView) {
        this.text = rootView.findViewById(R.id.textView1);
    }

    @NonNull
    public static FragmentSatelliteFiltersModalPaneListDialogItemBinding bind(@NonNull View rootView) {
        return new FragmentSatelliteFiltersModalPaneListDialogItemBinding(rootView);
    }

    @NonNull
    public static FragmentSatelliteFiltersModalPaneListDialogItemBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.fragment_satellite_filters_modal_pane_list_dialog, parent, attachToParent);
        return bind(root);
    }

    @NonNull
    public View getRoot() {
        return text.getRootView();
    }
}
