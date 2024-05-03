package com.example.galileomastermindboilerplate;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private List<Integer> mIcon;


    public RecyclerViewAdapter(List<String> data, List<Integer> icon) {
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
        String item = mData.get(position);
        int icon = mIcon.get(position);

        holder.textView.setText(item);
        holder.imageView.setImageResource(icon);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(List<String> newData) {
        mData = newData;
        notifyDataSetChanged(); // Notify adapter that dataset has changed
    }

    public void addData(List<String> newData) {
        mData.addAll(newData);


        notifyDataSetChanged(); // Notify adapter that dataset has changed
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView1);
            imageView = itemView.findViewById(R.id.imageView1);
        }
    }
}

