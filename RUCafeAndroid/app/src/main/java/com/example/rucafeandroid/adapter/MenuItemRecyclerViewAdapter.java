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
import com.example.rucafeandroid.model.Donut;
import com.example.rucafeandroid.model.MenuItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class MenuItemRecyclerViewAdapter extends RecyclerView.Adapter<MenuItemRecyclerViewAdapter.MyMenuItemViewHolder> {

    public interface MenuItemListener {
        void onDeleteRowItemButtonClicked(int position);
    }

    private Context context;
    private ArrayList<MenuItem> items;
    private boolean hideDeleteButton;
    private MenuItemListener listener;

    public MenuItemRecyclerViewAdapter(Context context, LinkedHashSet<MenuItem> items,
                                       boolean hideDeleteButton) {
        this.context = context;
        this.items = new ArrayList<>(items);
        this.hideDeleteButton = hideDeleteButton;
    }

    public MenuItemRecyclerViewAdapter(Context context, ArrayList<MenuItem> items,
                                       boolean hideDeleteButton) {
        this.context = context;
        this.items = new ArrayList<>(items);
        this.hideDeleteButton = hideDeleteButton;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(LinkedHashSet<MenuItem> newData) {
        this.items = new ArrayList<>(newData);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ArrayList<MenuItem> newData) {
        this.items = new ArrayList<>(newData);
        notifyDataSetChanged();
    }

    public void setListener(MenuItemRecyclerViewAdapter.MenuItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuItemRecyclerViewAdapter.MyMenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.menu_item_recycler_row, parent, false);

        return new MenuItemRecyclerViewAdapter.MyMenuItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MenuItemRecyclerViewAdapter.MyMenuItemViewHolder holder, int position) {
        MenuItem rowItem = items.get(position);

        if (rowItem instanceof Donut) {
            holder.quantityAmountTextView.setText("x" + rowItem.getQuantity());
            holder.infoTextView.setText("(" + ((Donut) rowItem).getType() + ") "
                    + ((Donut) rowItem).getFlavor());

        } else if (rowItem instanceof Coffee) {
            holder.quantityAmountTextView.setText("x" + rowItem.getQuantity());
            holder.infoTextView.setText(((Coffee) rowItem).getCupSize() + " " +
                    ((Coffee) rowItem).getAddIns().toString().replace("[", "(").replace("]", ")"));
        }

        if (hideDeleteButton) holder.deleteRowItemButton.setVisibility(View.GONE);
        else holder.deleteRowItemButton.setVisibility(View.VISIBLE);

        holder.deleteRowItemButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteRowItemButtonClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyMenuItemViewHolder extends RecyclerView.ViewHolder {

        MaterialButton deleteRowItemButton;
        TextView quantityAmountTextView, infoTextView;

        public MyMenuItemViewHolder(@NonNull View itemView) {
            super(itemView);

            deleteRowItemButton = itemView.findViewById(R.id.deleteRowItemButton);
            quantityAmountTextView = itemView.findViewById(R.id.quantityAmountTextView);
            infoTextView = itemView.findViewById(R.id.infoTextView);
        }
    }
}
