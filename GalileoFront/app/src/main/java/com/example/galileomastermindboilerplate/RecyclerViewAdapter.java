package com.example.galileomastermindboilerplate;


import android.location.GnssMeasurement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<SatelliteWidgetEntryData> mData;
    private List<Integer> mIcon;


    public RecyclerViewAdapter(List<SatelliteWidgetEntryData> data, List<Integer> icon) {
        this.mData = data;
        this.mIcon = icon;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SatelliteWidgetEntryData item = mData.get(position);
        int icon = mIcon.get(position);

        try {
                holder.SignalStrength.setText(item.ReceptionForce);

                holder.Frequency.setText(item.Frequency);

            holder.SatelliteNumber.setText(item.SatelliteNumber);
        } catch (Exception ex)
        {
            System.out.println("Exception: " + ex.toString());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(List<SatelliteWidgetEntryData> newData) {
        mData = newData;
        notifyDataSetChanged(); // Notify adapter that dataset has changed
    }

    public void addData(List<SatelliteWidgetEntryData> newData) {
        mData.addAll(newData);


        notifyDataSetChanged(); // Notify adapter that dataset has changed
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView SatelliteNumber;
        TextView Frequency;
        TextView SignalStrength;

        public ViewHolder(View itemView) {
            super(itemView);
            SatelliteNumber = itemView.findViewById(R.id.SatelliteNumberDisplay);
            Frequency = itemView.findViewById(R.id.FrequencyDisplay);
            SignalStrength = itemView.findViewById(R.id.SignalStrengthDisplay);
        }
    }
}

