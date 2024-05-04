package com.example.galileomastermindboilerplate;


import static android.location.GnssStatus.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.galileomastermindboilerplate.ui.home.HomeViewModel;

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

        holder.SatelliteNumber.setText(GetFlagFromId(item.ConstellationType) + " id " + String.valueOf(item.Svid));
        holder.SignalStrength.setText(String.valueOf(String.format("%.2f", item.Cn0DbHz) + " dBHz"));
        holder.Frequency.setText(formatDoubleAsGHz(item.CarrierFrequencyHz));
        holder.PowerShower.setMax(50);
        holder.PowerShower.setMin(10);
        holder.PowerShower.setProgress((int)item.Cn0DbHz);
        holder.AGCDisplay.setText(String.format("%.2f", item.AGC) + " dB");


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
        ProgressBar PowerShower;

        TextView AGCDisplay;

        TextView LatOT;
        TextView LonOT;

        public ViewHolder(View itemView) {
            super(itemView);
            SatelliteNumber = itemView.findViewById(R.id.SatelliteNumberDisplay);
            Frequency = itemView.findViewById(R.id.FrequencyDisplay);
            SignalStrength = itemView.findViewById(R.id.SignalStrengthDisplay);
            PowerShower = itemView.findViewById(R.id.ProgressBarCN_0Display);
            AGCDisplay = itemView.findViewById(R.id.AGCDisplay);

        }
    }

    private String formatDoubleAsGHz(double input)
    {
        return String.valueOf(input).replace("E9", " GHz").replace("E6", " MHz").replace("E3", " kHz");
    }
    private String GetFlagFromId(int id)
    {
        if(id == CONSTELLATION_BEIDOU)
            return "\uD83C\uDDE8\uD83C\uDDF3";
        else if(id == CONSTELLATION_GALILEO)
            return "\uD83C\uDDEA\uD83C\uDDFA";
        else if(id == CONSTELLATION_GLONASS)
            return "\uD83C\uDDF7\uD83C\uDDFA";
        else if(id == CONSTELLATION_GPS)
            return "\uD83C\uDDFA\uD83C\uDDF8";
        else if(id == CONSTELLATION_QZSS)
            return "\uD83C\uDDEF\uD83C\uDDF5";
        else if(id == CONSTELLATION_SBAS)
            return "\uD83C\uDDF8\uD83C\uDDE7";
        else if(id == CONSTELLATION_IRNSS)
            return "\uD83C\uDDEE\uD83C\uDDF3";
        else if(id == CONSTELLATION_UNKNOWN)
            return "⁉\uFE0F";
        else
            return String.valueOf(id);
    }
}

