package com.gnsstracker.mainapp.ui.satellitelist;


import static android.location.GnssStatus.*;

import static androidx.core.content.res.ResourcesCompat.getCachedFont;
import static androidx.core.content.res.ResourcesCompat.getColor;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.gnsstracker.mainapp.R;
import com.gnsstracker.mainapp.SatelliteWidgetEntryData;
import com.gnsstracker.mainapp.ui.MainActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.elevation.SurfaceColors;

import java.util.List;

public class SatelliteRecyclerViewHandler extends RecyclerView.Adapter<SatelliteRecyclerViewHandler.ViewHolder> {

    private List<SatelliteWidgetEntryData> SatellitesData;
    private FragmentActivity Activity;

    public SatelliteRecyclerViewHandler(List<SatelliteWidgetEntryData> data, FragmentActivity activity) {
        this.SatellitesData = data;
        Activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.satellite_recycler_view_item, parent, false);
        int color = SurfaceColors.SURFACE_1.getColor(view.getContext());
        Drawable bg = view.getBackground();
        bg.setTint(color);
        view.setBackground(bg);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SatelliteWidgetEntryData item = SatellitesData.get(position);

        holder.SatelliteNumber.setText(GetFlagFromId(item.ConstellationType) + " id " + item.Svid);
        holder.SignalStrength.setText(String.format("%.2f", item.Cn0DbHz) + " dBHz");

        int RED = Activity.getColor(R.color.COLOR_RED);
        int YELLOW = Activity.getColor(R.color.COLOR_YELLOW);
        int GREEN = Activity.getColor(R.color.COLOR_GREEN);

        if(item.Cn0DbHz > 20)
            holder.SignalStrength.setTextColor(GREEN);
        else if (item.Cn0DbHz > 10)
            holder.SignalStrength.setTextColor(YELLOW);
        else
            holder.SignalStrength.setTextColor(RED);

        holder.Frequency.setText(formatDoubleAsGHz(item.CarrierFrequencyHz));

        holder.PowerShower.setMax(50);
        holder.PowerShower.setMin(10);
        holder.PowerShower.setProgress((int)item.Cn0DbHz);
        holder.AGCDisplay.setText(String.format("%.2f", item.AGC) + " dB");

        if(item.AGC < -3)
            holder.AGCDisplay.setTextColor(GREEN);
        else if (item.AGC <= 3)
            holder.AGCDisplay.setTextColor(YELLOW);
        else
            holder.AGCDisplay.setTextColor(RED);
    }

    @Override
    public int getItemCount() {
        return SatellitesData.size();
    }

    public void updateData(List<SatelliteWidgetEntryData> newData) {
        SatellitesData = newData;
        notifyDataSetChanged(); // Notify adapter that dataset has changed
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView SatelliteNumber;
        TextView Frequency;
        TextView SignalStrength;
        ProgressBar PowerShower;
        TextView AGCDisplay;


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

