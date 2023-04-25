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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rucafeandroid.adapter.MenuItemRecyclerViewAdapter;
import com.example.rucafeandroid.model.MenuItem;
import com.example.rucafeandroid.model.Order;
import com.example.rucafeandroid.model.OrderViewModel;
import com.example.rucafeandroid.model.StoreOrdersViewModel;
import com.example.rucafeandroid.utils.randomIDGenerator;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 *
 */
public class StoreOrdersFragment extends Fragment implements MenuItemRecyclerViewAdapter.MenuItemListener {

    private static final double NJ_SALES_TAX = 0.06625;
    private static final double ZERO_DOLLARS = 0.00;

    private StoreOrdersViewModel storeOrdersViewModel;
    private LinkedHashSet<Order> storeOrders;

    private Order selectedOrder;

    private MenuItemRecyclerViewAdapter adapter;
    private TextView subtotalTextViewSO, salesTaxTextViewSO, orderTotalTextViewSO;

    /**
     *
     */
    public StoreOrdersFragment() {}

    /**
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeOrdersViewModel = new ViewModelProvider(requireActivity()).get(StoreOrdersViewModel.class);
        if (storeOrdersViewModel.getStoreOrdersLiveData().getValue() == null) {
            storeOrdersViewModel.setStoreOrders(new LinkedHashSet<>());
        }
        storeOrders = storeOrdersViewModel.getStoreOrdersLiveData().getValue();
    }

    /**
     *
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
        View view = inflater.inflate(R.layout.fragment_store_orders, container, false);

        storeOrdersViewModel.getStoreOrdersLiveData().observe(getViewLifecycleOwner(), newStoreOrders -> {
            storeOrders = newStoreOrders;
            setOrderNumberSpinner(view);
        });

        addOrderNumberSpinnerListener(view);

        RecyclerView storeOrderItemsRecyclerView = view.findViewById(R.id.storeOrderItemsRecyclerView);
        storeOrderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MenuItemRecyclerViewAdapter(getContext(), new ArrayList<>(), true);
        adapter.setListener(this);
        storeOrderItemsRecyclerView.setAdapter(adapter);
        updateRecyclerViewAdapterData();

        subtotalTextViewSO = view.findViewById(R.id.subtotalTextViewSO);
        salesTaxTextViewSO = view.findViewById(R.id.salesTaxTextViewSO);
        orderTotalTextViewSO = view.findViewById(R.id.orderTotalTextViewSO);

        MaterialButton cancelOrderButton = view.findViewById(R.id.cancelOrderButton);
        cancelOrderButton.setOnClickListener(this::onCancelOrderButtonClick);

        return view;
    }

    /**
     *
     * @param orderNumber
     * @return
     */
    private Order retrieveOrderByNumber (int orderNumber) {
        for (Order placedOrder : storeOrders) {
            if (placedOrder.getOrderNumber() == orderNumber) {
                return placedOrder;
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    private ArrayList<String> retrieveAllOrderNumbers () {
        ArrayList<String> orderNumbers = new ArrayList<>();

        if (storeOrders.isEmpty()) {
            orderNumbers.add("No orders available");
        } else {
            for (Order placedOrder : storeOrders) {
                orderNumbers.add(getString(R.string.order_num) +
                        placedOrder.getOrderNumberString());
            }
        }

        return orderNumbers;
    }

    /**
     *
     * @param subTotal
     * @param salesTax
     * @param orderTotal
     */
    private void setTotals(double subTotal, double salesTax, double orderTotal) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        subtotalTextViewSO.setText(MessageFormat.format("{0}{1}",
                getResources().getString(R.string.subtotal_),
                decimalFormat.format(subTotal)));

        salesTaxTextViewSO.setText(MessageFormat.format("{0}{1}",
                getResources().getString(R.string.sales_tax_),
                decimalFormat.format(salesTax)));

        orderTotalTextViewSO.setText(MessageFormat.format("{0}{1}",
                getResources().getString(R.string.order_total_),
                decimalFormat.format(orderTotal)));
    }

    /**
     *
     */
    private void updateTotals() {
        double salesTax, orderTotal, subTotal = ZERO_DOLLARS;

        if (selectedOrder == null || selectedOrder.getOrderItems().isEmpty()) {
            setTotals(subTotal, subTotal, subTotal);
            return;
        }

        for (MenuItem item : selectedOrder.getOrderItems()) {
            int quantity = item.getQuantity();
            double selectionTotal = quantity * item.itemPrice();
            subTotal += selectionTotal;
        }

        salesTax = subTotal * NJ_SALES_TAX;
        orderTotal = subTotal + salesTax;

        setTotals(subTotal, salesTax, orderTotal);
    }

    /**
     *
     * @param view
     */
    private void setOrderNumberSpinner (View view) {
        Spinner spinner = view.findViewById(R.id.orderNumberSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, retrieveAllOrderNumbers()
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    /**
     *
     * @param view
     */
    public void addOrderNumberSpinnerListener(View view) {
        Spinner orderNumberSpinner = view.findViewById(R.id.orderNumberSpinner);
        orderNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOrderNumberString = parent.getItemAtPosition(position).toString();

                if (selectedOrderNumberString.equals("No orders available")) {
                    selectedOrder = null;
                    updateRecyclerViewAdapterData();
                    updateTotals();
                } else {
                    int orderNumber = Integer.parseInt(selectedOrderNumberString.replaceAll("\\D+", ""));
                    selectedOrder = retrieveOrderByNumber(orderNumber);
                    updateRecyclerViewAdapterData();
                    updateTotals();
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                /* do nothing for now... */
            }
        });
    }

    /**
     *
     */
    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerViewAdapterData() {
        if (selectedOrder != null) {
            adapter.updateData(selectedOrder.getOrderItems());
        } else {
            adapter.updateData(new ArrayList<>());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     *
     * @param v
     */
    private void onCancelOrderButtonClick(View v) {
        if (selectedOrder != null) {
            storeOrders.remove(selectedOrder);
            storeOrdersViewModel.getStoreOrdersLiveData().setValue(storeOrders);

            setOrderNumberSpinner(requireView());
        }
    }

    /**
     *
     * @param position
     */
    @Override
    public void onDeleteRowItemButtonClicked(int position) {
        /* button is disabled in this fragment */
    }
}