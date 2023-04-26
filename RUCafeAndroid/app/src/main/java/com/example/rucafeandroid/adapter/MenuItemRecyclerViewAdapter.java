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

/**
 * This is the Recycler View Adapter class for a MenuItem recycler view. This adapter
 * is used throughout all the RecyclerViews within the application.
 *
 * @author Aryan Patel
 */
public class MenuItemRecyclerViewAdapter extends RecyclerView.Adapter<MenuItemRecyclerViewAdapter.MyMenuItemViewHolder> {

    /**
     * MenuItemListener interface that needs to be implemented in order to
     * use the adapter. Defines header for the delete button action in each
     * row item.
     */
    public interface MenuItemListener {
        void onDeleteRowItemButtonClicked(int position);
    }

    private Context context;
    private ArrayList<MenuItem> items;
    private boolean hideDeleteButton;
    private MenuItemListener listener;

    /**
     * Overloaded constructor to create a new adapter instance
     * given a set of MenuItem objects.
     *
     * @param context context of the current application state.
     * @param hideDeleteButton boolean flag to indicate whether the recycler view
     *                         should display the delete button or not for each row item.
     * @param items linked hash set of menu items to pass into the adaptor.
     */
    public MenuItemRecyclerViewAdapter(Context context, boolean hideDeleteButton,
                                       LinkedHashSet<MenuItem> items) {
        this.context = context;
        this.items = new ArrayList<>(items);
        this.hideDeleteButton = hideDeleteButton;
    }

    /**
     * Overloaded constructor to create a new adapter instance
     * given an arraylist of MenuItem objects.
     *
     * @param context context of the current application state.
     * @param hideDeleteButton boolean flag to indicate whether the recycler view
     *                         should display the delete button or not for each row item.
     * @param items array list of menu items to pass into the adaptor.
     */
    public MenuItemRecyclerViewAdapter(Context context, boolean hideDeleteButton,
                                       ArrayList<MenuItem> items) {
        this.context = context;
        this.items = new ArrayList<>(items);
        this.hideDeleteButton = hideDeleteButton;
    }

    /**
     * This method updates the items class variable with the given
     * linked hash set containing the new data.
     *
     * @param newData set containing the new menu items data.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(LinkedHashSet<MenuItem> newData) {
        this.items = new ArrayList<>(newData);
        notifyDataSetChanged();
    }

    /**
     * This method updates the items class variable with the given
     * array list containing the new data.
     *
     * @param newData array list containing the new menu items data.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ArrayList<MenuItem> newData) {
        this.items = new ArrayList<>(newData);
        notifyDataSetChanged();
    }

    /**
     * Sets the listener for this adapter to the given MenuItemListener.
     *
     * @param listener the listener to set
     */
    public void setListener(MenuItemRecyclerViewAdapter.MenuItemListener listener) {
        this.listener = listener;
    }

    /**
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return MyMenuItemViewHolder object, that is a custom ViewHolder class which
     *         holds the views for each item in the RecyclerView.
     */
    @NonNull
    @Override
    public MenuItemRecyclerViewAdapter.MyMenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.menu_item_recycler_row, parent, false);

        return new MenuItemRecyclerViewAdapter.MyMenuItemViewHolder(view);
    }

    /**
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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

    /**
     * This method is used by the RecyclerView to determine the count of row
     * items needed.
     *
     * @return int containing the size of the items array list.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * This is the custom view holder class used to construct each
     * row item in the Recycler View
     *
     * @author Aryan Patel
     */
    public static class MyMenuItemViewHolder extends RecyclerView.ViewHolder {

        MaterialButton deleteRowItemButton;
        TextView quantityAmountTextView, infoTextView;

        /**
         * Constructor to create new row item from layout.
         *
         * @param itemView View that is holding the row item.
         */
        public MyMenuItemViewHolder(@NonNull View itemView) {
            super(itemView);

            deleteRowItemButton = itemView.findViewById(R.id.deleteRowItemButton);
            quantityAmountTextView = itemView.findViewById(R.id.quantityAmountTextView);
            infoTextView = itemView.findViewById(R.id.infoTextView);
        }
    }
}
