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
import com.example.rucafeandroid.model.Coffee;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class CoffeeSelectionRecyclerViewAdapter extends RecyclerView.Adapter<CoffeeSelectionRecyclerViewAdapter.MyViewHolderCoffee> {

    public interface CoffeeSelectionListener {
        void onDeleteRowItemButtonClicked(int position);
    }

    private Context context;
    private ArrayList<Coffee> CoffeeSelections;
    private CoffeeSelectionListener listener;

    public CoffeeSelectionRecyclerViewAdapter(Context context, LinkedHashSet<Coffee> CoffeeSelections) {
        this.context = context;
        this.CoffeeSelections = new ArrayList<>(CoffeeSelections);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(LinkedHashSet<Coffee> newData) {
        this.CoffeeSelections = new ArrayList<>(newData);
        notifyDataSetChanged();
    }

    public void setListener(CoffeeSelectionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CoffeeSelectionRecyclerViewAdapter.MyViewHolderCoffee onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.coffee_recycler_view_row, parent, false);

        return new MyViewHolderCoffee(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull CoffeeSelectionRecyclerViewAdapter.MyViewHolderCoffee holder, @SuppressLint("RecyclerView") int position) {
        Coffee rowItem = CoffeeSelections.get(position);

        holder.quantityAmountTextViewCoffee.setText("x" + rowItem.getQuantity());
        holder.infoTextViewCoffee.setText(rowItem.getCupSize() + "   " +
                rowItem.getAddIns().toString().replace("[", "(").replace("]", ")"));

        holder.deleteRowItemButtonCoffee.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteRowItemButtonClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CoffeeSelections.size();
    }

    public static class MyViewHolderCoffee extends RecyclerView.ViewHolder {

        MaterialButton deleteRowItemButtonCoffee;
        TextView quantityAmountTextViewCoffee, infoTextViewCoffee;

        public MyViewHolderCoffee(@NonNull View itemView) {
            super(itemView);

            deleteRowItemButtonCoffee = itemView.findViewById(R.id.deleteRowItemButtonCoffee);
            quantityAmountTextViewCoffee = itemView.findViewById(R.id.quantityAmountTextViewCoffee);
            infoTextViewCoffee = itemView.findViewById(R.id.infoTextViewCoffee);
        }
    }
}