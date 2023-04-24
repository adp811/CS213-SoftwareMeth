package com.example.rucafeandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rucafeandroid.model.Coffee;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Aryan Patel and Rushi Patel
 */
public class OrderCoffeeFragment extends Fragment {

    private LinkedHashSet<Coffee> coffeeSelections;
    private HashSet<String> chipGroupSelections;

    public OrderCoffeeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_coffee, container, false);

        setCupSizeSpinnerValues(view);
        setQuantityAmountSpinnerValues(view, getResources().getInteger(R.integer.min_selection_coffee_qty),
                getResources().getInteger(R.integer.max_selection_coffee_qty));

        addChipGroupSelectionListener(view);

        return view;
    }

    private void setCupSizeSpinnerValues(View view) {
        Spinner spinner = view.findViewById(R.id.cupSizeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.cup_size_spinner_values,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setQuantityAmountSpinnerValues(View view, int min_qty, int max_qty) {
        Spinner spinner = view.findViewById(R.id.quantityAmountSpinner);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(),
                android.R.layout.simple_spinner_item);
        for (int i = min_qty; i <= max_qty; i++) adapter.add(i);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void addChipGroupSelectionListener(View view) {
        ChipGroup chipGroup = view.findViewById(R.id.flavorSelectionChipGroup);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                chipGroupSelections = new HashSet<String>();
                List<Integer> selectedIds = group.getCheckedChipIds();

                for (int id : selectedIds) {
                    Chip flavorChip = group.findViewById(id);
                    chipGroupSelections.add(flavorChip.getText().toString());
                }
            }
        });
    }

    private boolean validateInputs(View view) {
        return false;
    }

    private void updateSubtotal(View view) {
        return;
    }

    private void onAddCoffeeSelectionButtonClick(View view) {
        return;
    }

    public void onDeleteRowItemButtonClicked(int position) {
        return;
    }
}