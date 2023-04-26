package com.example.rucafeandroid.fragment;

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
import android.widget.Toast;

import com.example.rucafeandroid.R;
import com.example.rucafeandroid.adapter.MenuItemRecyclerViewAdapter;
import com.example.rucafeandroid.model.Donut;
import com.example.rucafeandroid.model.MenuItem;
import com.example.rucafeandroid.model.Order;
import com.example.rucafeandroid.model.OrderViewModel;
import com.example.rucafeandroid.utils.ToastUtils;
import com.example.rucafeandroid.utils.randomIDGenerator;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * This is the controlling class for the Order Donut Fragment. It contains
 * the life cycle and UI methods of the fragment.
 *
 * @author Aryan Patel and Rushi Patel
 */
public class OrderDonutFragment extends Fragment implements MenuItemRecyclerViewAdapter.MenuItemListener {

    private static final int MIN_DONUT_QTY = 1;
    private static final int MAX_DONUT_QTY = 50;

    private OrderViewModel orderViewModel;
    private Order order;

    private MaterialButton addDonutsToOrderButton;
    private MenuItemRecyclerViewAdapter adapter;
    private LinkedHashSet<MenuItem> donutSelections;

    /**
     * Required empty public constructor().
     *
     */
    public OrderDonutFragment() {}

    /**
     * This method is called when the fragment is created. The shared data
     * is initialized here.
     *
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
     * This method is called when the view within the fragment is created. The
     * UI elements are initialized here.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return root view that contains the UI for the OrderDonutFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_donut, container, false);

        orderViewModel.getOrderLiveData().observe(
                getViewLifecycleOwner(), newOrder -> order = newOrder);

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
        adapter = new MenuItemRecyclerViewAdapter(getContext(), false, donutSelections);
        adapter.setListener(this);
        donutSelectionRecyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * This method sets the donut type spinner values retrieved from
     * the string resource file.
     *
     * @param view root view that contains the UI for the OrderDonutFragment.
     */
    public void setDonutTypeSpinnerValues(View view) {
        Spinner spinner = view.findViewById(R.id.donutTypeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.donut_type_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    /**
     * This method sets the donut type image view based on the given donut
     * type as a string.
     *
     * @param view root view that contains the UI for the OrderDonutFragment.
     * @param donutType String containing the type of donut to display in the image.
     */
    public void setDonutTypeImageView(View view, String donutType) {
        ImageView imageView = view.findViewById(R.id.donutTypeImageView);

        if (donutType.equals(getString(R.string.yeast_donut))) {
            imageView.setImageResource(R.drawable.yeast_donut);
        } else if (donutType.equals(getString(R.string.cake_donut))) {
            imageView.setImageResource(R.drawable.cake_donut);
        } else if (donutType.equals(getString(R.string.hole_donut))) {
            imageView.setImageResource(R.drawable.hole_donut);
        } else {
            imageView.setImageResource(R.drawable.donut_sprinkled_icon);
        }
    }

    /**
     * This method sets the donut flavor spinner values retrieved from
     * the string resource file given the donut type. The method to update
     * the donut type image is called here.
     *
     * @param view root view that contains the UI for the OrderDonutFragment.
     * @param donutType String containing the type of donut to display in the image.
     */
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

    /**
     * This method adds a lister to the donut type spinner, in order to detect
     * any value changes. Any value change will update the donut flavor spinner
     * with the available flavors for that donut type.
     *
     * @param view root view that contains the UI for the OrderDonutFragment.
     */
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

    /**
     * This method validates the user inputs for the UI elements.
     *
     * @param donutTypeSpinner Spinner object representing the donut type spinner.
     * @param donutFlavorSpinner Spinner object representing the donut flavor spinner.
     * @param quantityTextField EditText object representing the quantity text field.
     * @return boolean indicating if the inputs are valid or not.
     */
    public boolean validateInputs(Spinner donutTypeSpinner, Spinner donutFlavorSpinner,
                                  EditText quantityTextField) {
        String donutTypeSpinnerValue = donutTypeSpinner.getSelectedItem().toString();
        String donutFlavorSpinnerValue = donutFlavorSpinner.getSelectedItem().toString();

        try {
            int value = Integer.parseInt(quantityTextField.getText().toString().trim());
            if (value < MIN_DONUT_QTY ||
                value > MAX_DONUT_QTY) {
                ToastUtils.showToast(getContext(),
                        getString(R.string.toast_invalid_amount),
                        Toast.LENGTH_SHORT);
                return false;
            }
        } catch (NumberFormatException e) {
            ToastUtils.showToast(getContext(),
                    getString(R.string.toast_invalid_input),
                    Toast.LENGTH_SHORT);
            return false;
        }

        return !donutTypeSpinnerValue.equals("Select") && !donutFlavorSpinnerValue.equals("Select");
    }

    /**
     * This method refreshes the subtotal displayed in the add to order
     * button. The method is called whenever there is a change to the current
     * selected donut items.
     *
     */
    @SuppressLint("SetTextI18n")
    public void updateSubtotal() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        Double subTotal = 0.00;

        if(donutSelections.isEmpty()) {
            addDonutsToOrderButton.setText(getString(R.string.dollar_sign)
                    + decimalFormat.format(subTotal));
            return;
        }

        for (MenuItem selection : donutSelections) {
            int quantity = selection.getQuantity();
            double selectionTotal = quantity * selection.itemPrice();
            subTotal += selectionTotal;
        }

        addDonutsToOrderButton.setText(getString(R.string.dollar_sign)
                + decimalFormat.format(subTotal));
    }

    /**
     * This method is the on click action for the add donut selection
     * button. If the same item is added again with a different quantity, the
     * quantity will simply be updated.
     *
     * @param v root view that contains the UI for the OrderDonutFragment.
     */
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

    /**
     * This method is the on click action for the add donuts to order button. If there
     * are no donut items selected, the user is notified and the action is cancelled.
     *
     * @param v root view that contains the UI for the OrderDonutFragment.
     */
    @SuppressLint("NotifyDataSetChanged")
    private void onAddDonutsToOrderButtonClick(View v) {
        if (donutSelections.isEmpty()) {
            ToastUtils.showToast(getContext(),
                    getString(R.string.toast_please_select_items),
                    Toast.LENGTH_SHORT);
            return;
        }

        order.getOrderItems().addAll(donutSelections);
        donutSelections.clear();

        adapter.updateData(donutSelections);
        adapter.notifyDataSetChanged();

        addDonutsToOrderButton.setText(getResources().getString(R.string.zero_total_));

        ToastUtils.showToast(getContext(),
                getResources().getString(R.string.toast_added_to_order),
                Toast.LENGTH_SHORT);
    }

    /**
     * This method is the on click action implemented for the delete
     * row item button in each row item of the recycler view.
     *
     * @param position int which contains the index position of the
     *                 row item where the delete row item button was
     *                 clicked.
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteRowItemButtonClicked(int position) {
        MenuItem selection = new ArrayList<>(donutSelections).get(position);
        donutSelections.remove(selection);

        adapter.updateData(donutSelections);
        adapter.notifyDataSetChanged();

        updateSubtotal();

        ToastUtils.showToast(getContext(),
                getResources().getString(R.string.toast_item_removed),
                Toast.LENGTH_SHORT);
    }
}
