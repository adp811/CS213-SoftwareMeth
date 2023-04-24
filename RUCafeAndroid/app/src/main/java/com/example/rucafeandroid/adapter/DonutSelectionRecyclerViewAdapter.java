package com.example.rucafeandroid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rucafeandroid.R;
import com.example.rucafeandroid.model.Donut;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class DonutSelectionRecyclerViewAdapter extends RecyclerView.Adapter<DonutSelectionRecyclerViewAdapter.MyViewHolder> {

    public interface DonutSelectionListener {
        void onDeleteRowItemButtonClicked(int position);
    }

    private Context context;
    private ArrayList<Donut> donutSelections;
    private DonutSelectionListener listener;

    public DonutSelectionRecyclerViewAdapter(Context context, LinkedHashSet<Donut> donutSelections) {
        this.context = context;
        this.donutSelections = new ArrayList<>(donutSelections);
    }

    public void updateData(LinkedHashSet<Donut> newData) {
        this.donutSelections = new ArrayList<>(newData);
        notifyDataSetChanged();
    }

    public void setListener(DonutSelectionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DonutSelectionRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.donut_recycler_view_row, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull DonutSelectionRecyclerViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Donut rowItem = donutSelections.get(position);

        holder.quantityAmountTextView.setText("x" + rowItem.getQuantity());
        holder.donutInfoTextView.setText("(" + rowItem.getType() + ") " + rowItem.getFlavor());

        holder.deleteRowItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) { // Check if the listener is not null
                    listener.onDeleteRowItemButtonClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return donutSelections.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialButton deleteRowItemButton;
        TextView quantityAmountTextView, donutInfoTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            deleteRowItemButton = itemView.findViewById(R.id.deleteRowItemButton);
            quantityAmountTextView = itemView.findViewById(R.id.quantityAmountTextView);
            donutInfoTextView = itemView.findViewById(R.id.donutInfoTextView);
        }
    }
}
