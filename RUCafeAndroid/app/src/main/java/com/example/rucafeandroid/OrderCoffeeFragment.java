package com.example.rucafeandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rucafeandroid.adapter.CoffeeSelectionRecyclerViewAdapter;

import com.example.rucafeandroid.model.Coffee;
import com.example.rucafeandroid.model.Donut;
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
public class OrderCoffeeFragment extends Fragment implements CoffeeSelectionRecyclerViewAdapter.CoffeeSelectionListener{

    private MaterialButton addCoffeesToOrderButton;
    private CoffeeSelectionRecyclerViewAdapter adapter;
    private LinkedHashSet<Coffee> coffeeSelections;
    private HashSet<String> chipGroupSelections;

    public OrderCoffeeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_coffee, container, false);

        coffeeSelections = new LinkedHashSet<>();
        chipGroupSelections = new HashSet<>();

        addChipGroupSelectionListener(view);

        addCoffeesToOrderButton = view.findViewById(R.id.addCoffeesToOrderButton);

        MaterialButton addCoffeeSelectionButton = view.findViewById(R.id.addCoffeeSelectionButton);
        addCoffeeSelectionButton.setOnClickListener(this::onAddCoffeeSelectionButtonClick);

        setCupSizeSpinnerValues(view);
        setQuantityAmountSpinnerValues(view);

        RecyclerView coffeeSelectionRecyclerView = view.findViewById(R.id.coffeeSelectionsRecyclerView);
        coffeeSelectionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CoffeeSelectionRecyclerViewAdapter(getContext(), coffeeSelections);
        adapter.setListener(this);
        coffeeSelectionRecyclerView.setAdapter(adapter);

        return view;
    }

    private void setCupSizeSpinnerValues(View view) {
        Spinner spinner = view.findViewById(R.id.cupSizeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.cup_size_spinner_values,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    private void setQuantityAmountSpinnerValues(View view) {
        Spinner spinner = view.findViewById(R.id.quantityAmountSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.quantity_spinner_values,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

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

    @SuppressLint("SetTextI18n")
    private void updateSubtotal() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        Double subTotal = 0.00;

        if(coffeeSelections.isEmpty()) {
            addCoffeesToOrderButton.setText("$" + decimalFormat.format(subTotal));
            return;
        }

        for (Coffee selection : coffeeSelections) {
            int quantity = selection.getQuantity();
            double selectionTotal = quantity * selection.itemPrice();
            subTotal += selectionTotal;
        }

        addCoffeesToOrderButton.setText("$" + decimalFormat.format(subTotal));
    }

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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteRowItemButtonClicked(int position) {
        Coffee selection = new ArrayList<>(coffeeSelections).get(position);
        coffeeSelections.remove(selection);

        adapter.updateData(coffeeSelections);
        adapter.notifyDataSetChanged();

        updateSubtotal();
    }
}