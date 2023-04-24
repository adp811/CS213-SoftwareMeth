package com.example.rucafeandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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

import com.example.rucafeandroid.adapter.DonutSelectionRecyclerViewAdapter;
import com.example.rucafeandroid.model.Donut;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class OrderDonutFragment extends Fragment implements DonutSelectionRecyclerViewAdapter.DonutSelectionListener {

    private static final int MIN_DONUT_QUANTITY = 1;

    private Spinner donutTypeSpinner, donutFlavorSpinner;
    private EditText quantityTextField;
    private ImageView donutTypeImageView;
    private MaterialButton addDonutsToOrderButton;

    private DonutSelectionRecyclerViewAdapter adapter;
    private LinkedHashSet<Donut> donutSelections = new LinkedHashSet<>();

    public OrderDonutFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_donut, container, false);

        donutTypeSpinner = view.findViewById(R.id.donutTypeSpinner);
        donutFlavorSpinner = view.findViewById(R.id.donutFlavorSpinner);
        setDonutTypeSpinnerValues(donutTypeSpinner);
        setDonutFlavorSpinnerValues(donutFlavorSpinner, getString(R.string.yeast_donut));
        addDonutSpinnerListener(donutTypeSpinner, donutFlavorSpinner);

        quantityTextField = view.findViewById(R.id.quantityTextField);
        quantityTextField.setText(R.string.min_donut_qty);

        MaterialButton addDonutSelectionButton = view.findViewById(R.id.addDonutSelectionButton);
        addDonutSelectionButton.setOnClickListener(this::onAddDonutSelectionButtonClick);

        RecyclerView donutSelectionRecyclerView = view.findViewById(R.id.donutSelectionsRecyclerView);
        donutSelectionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DonutSelectionRecyclerViewAdapter(getContext(), donutSelections);
        adapter.setListener(this);
        donutSelectionRecyclerView.setAdapter(adapter);

        donutTypeImageView = view.findViewById(R.id.donutTypeImageView);
        addDonutsToOrderButton = view.findViewById(R.id.addDonutsToOrderButton);

        return view;
    }

    public void setDonutTypeSpinnerValues(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.donut_type_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setDonutFlavorSpinnerValues(Spinner spinner, String donutType) {
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
    }

    public void setDonutTypeImageView(ImageView imageView, String donutType) {
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

    public void addDonutSpinnerListener (Spinner donutTypeSpinner, Spinner donutFlavorSpinner) {
        donutTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String donutType = parent.getItemAtPosition(position).toString();
                setDonutFlavorSpinnerValues(donutFlavorSpinner, donutType);
                setDonutTypeImageView(donutTypeImageView, donutType);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing for now...
            }
        });
    }

    public boolean validateInputs() {
        String donutTypeSpinnerValue = donutTypeSpinner.getSelectedItem().toString();
        String donutFlavorSpinnerValue = donutFlavorSpinner.getSelectedItem().toString();

        try {
            int value = Integer.parseInt(quantityTextField.getText().toString().trim());
            if (value < MIN_DONUT_QUANTITY) {
                // The input is negative, or zero
                return false;
            }
        } catch (NumberFormatException e) {
            // The input is not an integer
            return false;
        }

        return !donutTypeSpinnerValue.equals("Select") && !donutFlavorSpinnerValue.equals("Select");
    }

    @SuppressLint("SetTextI18n")
    public void updateSubtotal(MaterialButton addDonutsToOrderButton) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        Double subTotal = 0.00;

        if(donutSelections.isEmpty()) {
            addDonutsToOrderButton.setText("$" + decimalFormat.format(subTotal));
            return;
        }

        for (Donut selection : donutSelections) {
            int quantity = selection.getQuantity();
            double selectionTotal = quantity * selection.itemPrice();
            subTotal += selectionTotal;
        }

        addDonutsToOrderButton.setText("$" + decimalFormat.format(subTotal));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onAddDonutSelectionButtonClick(View view) {
        if(!validateInputs()) return;

        String type = donutTypeSpinner.getSelectedItem().toString();
        String flavor = donutFlavorSpinner.getSelectedItem().toString();
        int quantity = Integer.parseInt(quantityTextField.getText().toString().trim());

        Donut selection = new Donut(quantity, type, flavor);
        donutSelections.remove(selection);
        donutSelections.add(selection);

        adapter.updateData(donutSelections);
        adapter.notifyDataSetChanged();

        updateSubtotal(addDonutsToOrderButton);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteRowItemButtonClicked(int position) {
        Donut selection = new ArrayList<>(donutSelections).get(position);
        donutSelections.remove(selection);

        adapter.updateData(donutSelections);
        adapter.notifyDataSetChanged();

        updateSubtotal(addDonutsToOrderButton);
    }
}
