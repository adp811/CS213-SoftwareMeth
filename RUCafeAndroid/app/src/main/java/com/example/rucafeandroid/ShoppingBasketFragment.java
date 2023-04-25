package com.example.rucafeandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rucafeandroid.adapter.MenuItemRecyclerViewAdapter;
import com.example.rucafeandroid.model.MenuItem;

import java.util.LinkedHashSet;

/**
 * @author Aryan Patel and Rushi Patel
 */
public class ShoppingBasketFragment extends Fragment implements MenuItemRecyclerViewAdapter.MenuItemListener{

    private MenuItemRecyclerViewAdapter adapter;
    private LinkedHashSet<MenuItem> orderItems;
    private TextView subtotalTextView, salesTaxTextView, orderTotalTextView;

    public ShoppingBasketFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_basket, container, false);

        return view;
    }

    public void updateTotals() {
    }

    @Override
    public void onDeleteRowItemButtonClicked(int position) {

    }
}