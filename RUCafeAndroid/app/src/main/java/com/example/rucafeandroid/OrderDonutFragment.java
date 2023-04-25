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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.rucafeandroid.adapter.MenuItemRecyclerViewAdapter;
import com.example.rucafeandroid.model.Donut;
import com.example.rucafeandroid.model.MenuItem;
import com.example.rucafeandroid.model.Order;
import com.example.rucafeandroid.model.OrderViewModel;
import com.example.rucafeandroid.utils.randomIDGenerator;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class OrderDonutFragment extends Fragment implements MenuItemRecyclerViewAdapter.MenuItemListener {

    private OrderViewModel orderViewModel;
    private Order order;

    private MaterialButton addDonutsToOrderButton;
    private MenuItemRecyclerViewAdapter adapter;
    private LinkedHashSet<MenuItem> donutSelections;

    public OrderDonutFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        if (orderViewModel.getOrderLiveData().getValue() == null) {
            orderViewModel.setOrder(new Order(randomIDGenerator.generateRandomID(9)));
        }

        order = orderViewModel.getOrderLiveData().getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_donut, container, false);

        orderViewModel.getOrderLiveData().observe(getViewLifecycleOwner(), newOrder -> {
            order = newOrder;
        });

        donutSelections = new LinkedHashSet<>();

        addDonutSpinnerListener(view);

        EditText quantityTextField = view.findViewById(R.id.quantityTextField);
        quantityTextField.setText(R.string.min_donut_qty);

        addDonutsToOrderButton = view.findViewById(R.id.addDonutsToOrderButton);

        MaterialButton addDonutSelectionButton = view.findViewById(R.id.addDonutSelectionButton);
        addDonutSelectionButton.setOnClickListener(this::onAddDonutSelectionButtonClick);

        MaterialButton addDonutsToOrderButton = view.findViewById(R.id.addDonutsToOrderButton);
        addDonutsToOrderButton.setOnClickListener(this::onAddDonutsToOrderButtonClick);

        setDonutTypeSpinnerValues(view);
        setDonutFlavorSpinnerValues(view, getString(R.string.yeast_donut));

        RecyclerView donutSelectionRecyclerView = view.findViewById(R.id.donutSelectionsRecyclerView);
        donutSelectionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MenuItemRecyclerViewAdapter(getContext(), donutSelections, false);
        adapter.setListener(this);
        donutSelectionRecyclerView.setAdapter(adapter);

        return view;
    }

    public void setDonutTypeSpinnerValues(View view) {
        Spinner spinner = view.findViewById(R.id.donutTypeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.donut_type_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    public void setDonutFlavorSpinnerValues(View view, String donutType) {
        Spinner spinner = view.findViewById(R.id.donutFlavorSpinner);

        int typeID = 0;
        if(donutType.equals(getString(R.string.yeast_donut))) {
            typeID = R.array.donut_flavor_spinner_values_yeast;
        } else if (donutType.equals(getString(R.string.cake_donut))) {
            typeID = R.array.donut_flavor_spinner_values_cake;
        } else if (donutType.equals(getString(R.string.hole_donut))) {
            typeID = R.array.donut_flavor_spinner_values_hole;
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), typeID, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    public void setDonutTypeImageView(View view, String donutType) {
        ImageView imageView = view.findViewById(R.id.donutTypeImageView);

        if (donutType.equals(getString(R.string.yeast_donut))) {
            imageView.setImageResource(R.drawable.yeast_donut);
        } else if (donutType.equals(getString(R.string.cake_donut))) {
            imageView.setImageResource(R.drawable.cake_donut);
        } else if (donutType.equals(getString(R.string.hole_donut))) {
            imageView.setImageResource(R.drawable.hole_donut);
        } else {
            imageView.setImageResource(R.drawable.iconmonstr_candy_9);
        }
    }

    public void addDonutSpinnerListener(View view) {
        Spinner donutTypeSpinner = view.findViewById(R.id.donutTypeSpinner);
        Spinner donutFlavorSpinner = view.findViewById(R.id.donutFlavorSpinner);
        ImageView donutTypeImageView = view.findViewById(R.id.donutTypeImageView);

        donutTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String donutType = parent.getItemAtPosition(position).toString();
                setDonutFlavorSpinnerValues(donutFlavorSpinner, donutType);
                setDonutTypeImageView(donutTypeImageView, donutType);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                /* do nothing for now... */
            }
        });
    }

    public boolean validateInputs(Spinner donutTypeSpinner, Spinner donutFlavorSpinner,
                                  EditText quantityTextField) {
        String donutTypeSpinnerValue = donutTypeSpinner.getSelectedItem().toString();
        String donutFlavorSpinnerValue = donutFlavorSpinner.getSelectedItem().toString();

        try {
            int value = Integer.parseInt(quantityTextField.getText().toString().trim());
            if (value < getResources().getInteger(R.integer.min_selection_donut_qty) ||
                value > getResources().getInteger(R.integer.max_selection_donut_qty)) {
                // The input is negative, zero, or over max
                return false;
            }
        } catch (NumberFormatException e) {
            // The input is not an integer
            return false;
        }

        return !donutTypeSpinnerValue.equals("Select") && !donutFlavorSpinnerValue.equals("Select");
    }

    @SuppressLint("SetTextI18n")
    public void updateSubtotal() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        Double subTotal = 0.00;

        if(donutSelections.isEmpty()) {
            addDonutsToOrderButton.setText("$" + decimalFormat.format(subTotal));
            return;
        }

        for (MenuItem selection : donutSelections) {
            int quantity = selection.getQuantity();
            double selectionTotal = quantity * selection.itemPrice();
            subTotal += selectionTotal;
        }

        addDonutsToOrderButton.setText("$" + decimalFormat.format(subTotal));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onAddDonutSelectionButtonClick(View v) {
        Spinner donutTypeSpinner = requireView().findViewById(R.id.donutTypeSpinner);
        Spinner donutFlavorSpinner = requireView().findViewById(R.id.donutFlavorSpinner);
        EditText quantityTextField = requireView().findViewById(R.id.quantityTextField);

        if (!validateInputs(donutTypeSpinner, donutFlavorSpinner, quantityTextField)) return;

        int quantity = Integer.parseInt(quantityTextField.getText().toString().trim());
        String type = donutTypeSpinner.getSelectedItem().toString();
        String flavor = donutFlavorSpinner.getSelectedItem().toString();

        Donut selection = new Donut(quantity, type, flavor);
        donutSelections.remove(selection);
        donutSelections.add(selection);

        adapter.updateData(donutSelections);
        adapter.notifyDataSetChanged();

        updateSubtotal();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onAddDonutsToOrderButtonClick(View v) {
        if (donutSelections.isEmpty()) {
            return;
        }

        order.getOrderItems().addAll(donutSelections);
        donutSelections.clear();

        adapter.updateData(donutSelections);
        adapter.notifyDataSetChanged();

        addDonutsToOrderButton.setText(getResources().getString(R.string.zero_total_));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteRowItemButtonClicked(int position) {
        MenuItem selection = new ArrayList<>(donutSelections).get(position);
        donutSelections.remove(selection);

        adapter.updateData(donutSelections);
        adapter.notifyDataSetChanged();

        updateSubtotal();
    }
}
