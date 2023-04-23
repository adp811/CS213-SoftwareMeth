package com.example.rucafeandroid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rucafeandroid.R;
import com.example.rucafeandroid.model.Donut;

import java.util.ArrayList;
import java.util.LinkedHashSet;


public class DonutSelectionRecyclerViewAdapter extends RecyclerView.Adapter<DonutSelectionRecyclerViewAdapter.MyViewHolder> {

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    private Context context;
    private ArrayList<Donut> donutSelections;
    private OnItemSelectedListener onItemSelectedListener;

    public DonutSelectionRecyclerViewAdapter(Context context, LinkedHashSet<Donut> donutSelections) {
        this.context = context;
        this.donutSelections = new ArrayList<>(donutSelections);
    }

    @NonNull
    @Override
    public DonutSelectionRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.donut_recycler_view_row, parent, false);

        return new DonutSelectionRecyclerViewAdapter.MyViewHolder(view);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DonutSelectionRecyclerViewAdapter.MyViewHolder holder, int position) {
        Donut rowItem = donutSelections.get(position);

        holder.quantityAmountTextView.setText("x" + String.valueOf(rowItem.getQuantity()));
        holder.donutInfoTextView.setText("(" + rowItem.getType() + ") " + rowItem.getFlavor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return donutSelections.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView quantityAmountTextView, donutInfoTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            quantityAmountTextView = itemView.findViewById(R.id.quantityAmountTextView);
            donutInfoTextView = itemView.findViewById(R.id.donutInfoTextView);
        }
    }

    public void updateData(LinkedHashSet<Donut> newData) {
        this.donutSelections = new ArrayList<>(newData);
    }
}
