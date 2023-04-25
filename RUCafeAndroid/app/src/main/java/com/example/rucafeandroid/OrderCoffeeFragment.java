package com.example.rucafeandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rucafeandroid.adapter.MenuItemRecyclerViewAdapter;
import com.example.rucafeandroid.model.Coffee;
import com.example.rucafeandroid.model.MenuItem;
import com.example.rucafeandroid.model.Order;
import com.example.rucafeandroid.model.OrderViewModel;
import com.example.rucafeandroid.utils.randomIDGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * @author Aryan Patel and Rushi Patel
 */
public class OrderCoffeeFragment extends Fragment implements MenuItemRecyclerViewAdapter.MenuItemListener{

    private OrderViewModel orderViewModel;
    private Order order;

    private MaterialButton addCoffeesToOrderButton;
    private MenuItemRecyclerViewAdapter adapter;
    private LinkedHashSet<MenuItem> coffeeSelections;
    private HashSet<String> chipGroupSelections;

    /**
     *
     */
    public OrderCoffeeFragment() {}

    /**
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        if (orderViewModel.getOrderLiveData().getValue() == null) {
            orderViewModel.setOrder(new Order(randomIDGenerator.generateRandomID(9)));
        }

        order = orderViewModel.getOrderLiveData().getValue();
    }

    /**
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_coffee, container, false);

        orderViewModel.getOrderLiveData().observe(getViewLifecycleOwner(), newOrder -> {
            order = newOrder;
        });

        coffeeSelections = new LinkedHashSet<>();
        chipGroupSelections = new HashSet<>();

        addChipGroupSelectionListener(view);

        addCoffeesToOrderButton = view.findViewById(R.id.addCoffeesToOrderButton);

        MaterialButton addCoffeeSelectionButton = view.findViewById(R.id.addCoffeeSelectionButton);
        addCoffeeSelectionButton.setOnClickListener(this::onAddCoffeeSelectionButtonClick);

        MaterialButton addCoffeesToOrderButton = view.findViewById(R.id.addCoffeesToOrderButton);
        addCoffeesToOrderButton.setOnClickListener(this::onAddCoffeesToOrderButtonClick);

        setCupSizeSpinnerValues(view);
        setQuantityAmountSpinnerValues(view);

        RecyclerView coffeeSelectionRecyclerView = view.findViewById(R.id.coffeeSelectionsRecyclerView);
        coffeeSelectionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MenuItemRecyclerViewAdapter(getContext(), coffeeSelections, false);
        adapter.setListener(this);
        coffeeSelectionRecyclerView.setAdapter(adapter);

        return view;
    }

    /**
     *
     * @param view
     */
    private void setCupSizeSpinnerValues(View view) {
        Spinner spinner = view.findViewById(R.id.cupSizeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.cup_size_spinner_values,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    /**
     *
     * @param view
     */
    private void setQuantityAmountSpinnerValues(View view) {
        Spinner spinner = view.findViewById(R.id.quantityAmountSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.quantity_spinner_values,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    /**
     *
     * @param view
     */
    private void addChipGroupSelectionListener(View view) {
        ChipGroup chipGroup = view.findViewById(R.id.flavorSelectionChipGroup);

        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String chipText = chip.getText().toString();
                if (isChecked) {
                    chipGroupSelections.add(chipText);
                } else {
                    chipGroupSelections.remove(chipText);
                }
            });
        }
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private void updateSubtotal() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        Double subTotal = 0.00;

        if(coffeeSelections.isEmpty()) {
            addCoffeesToOrderButton.setText("$" + decimalFormat.format(subTotal));
            return;
        }

        for (MenuItem selection : coffeeSelections) {
            int quantity = selection.getQuantity();
            double selectionTotal = quantity * selection.itemPrice();
            subTotal += selectionTotal;
        }

        addCoffeesToOrderButton.setText("$" + decimalFormat.format(subTotal));
    }

    /**
     *
     * @param v
     */
    @SuppressLint("NotifyDataSetChanged")
    private void onAddCoffeeSelectionButtonClick(View v) {
        Spinner cupSizeSpinner = requireView().findViewById(R.id.cupSizeSpinner);
        Spinner quantityAmountSpinner = requireView().findViewById(R.id.quantityAmountSpinner);

        int quantity = Integer.parseInt(quantityAmountSpinner.getSelectedItem().toString());
        String cupSize = cupSizeSpinner.getSelectedItem().toString();
        HashSet<String> flavorAddIns = new HashSet<>(chipGroupSelections);

        Coffee selection = new Coffee(quantity, cupSize, flavorAddIns);
        coffeeSelections.remove(selection);
        coffeeSelections.add(selection);

        adapter.updateData(coffeeSelections);
        adapter.notifyDataSetChanged();

        updateSubtotal();
    }

    /**
     *
     * @param v
     */
    @SuppressLint("NotifyDataSetChanged")
    private void onAddCoffeesToOrderButtonClick(View v) {
        if (coffeeSelections.isEmpty()) {
            return;
        }

        order.getOrderItems().addAll(coffeeSelections);
        coffeeSelections.clear();

        adapter.updateData(coffeeSelections);
        adapter.notifyDataSetChanged();

        addCoffeesToOrderButton.setText(getResources().getString(R.string.zero_total_));
    }

    /**
     *
     * @param position
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteRowItemButtonClicked(int position) {
        MenuItem selection = new ArrayList<>(coffeeSelections).get(position);
        coffeeSelections.remove(selection);

        adapter.updateData(coffeeSelections);
        adapter.notifyDataSetChanged();

        updateSubtotal();
    }
}