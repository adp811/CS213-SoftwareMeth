package com.example.rucafeandroid.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rucafeandroid.R;
import com.example.rucafeandroid.adapter.MenuItemRecyclerViewAdapter;
import com.example.rucafeandroid.model.MenuItem;
import com.example.rucafeandroid.model.Order;
import com.example.rucafeandroid.model.StoreOrdersViewModel;
import com.example.rucafeandroid.utils.ToastUtils;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * This is the controlling class for the Store Orders Fragment. It contains
 * the life cycle and UI methods of the fragment.
 *
 * @author Aryan Patel and Rushi Patel
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
     * Required empty public constructor().
     */
    public StoreOrdersFragment() {}

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

        storeOrdersViewModel = new ViewModelProvider(requireActivity()).get(StoreOrdersViewModel.class);
        if (storeOrdersViewModel.getStoreOrdersLiveData().getValue() == null) {
            storeOrdersViewModel.setStoreOrders(new LinkedHashSet<>());
        }
        storeOrders = storeOrdersViewModel.getStoreOrdersLiveData().getValue();
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
     * @return root view that contains the UI for the StoreOrdersFragment.
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

        adapter = new MenuItemRecyclerViewAdapter(getContext(), true, new ArrayList<>());
        adapter.setListener(this);
        storeOrderItemsRecyclerView.setAdapter(adapter);
        updateRecyclerViewAdapterData();

        subtotalTextViewSO = view.findViewById(R.id.subtotalTextViewSO);
        salesTaxTextViewSO = view.findViewById(R.id.salesTaxTextViewSO);
        orderTotalTextViewSO = view.findViewById(R.id.orderTotalTextViewSO);

        MaterialButton cancelOrderButton = view.findViewById(R.id.cancelOrderButton);
        cancelOrderButton.setOnClickListener(this::onCancelOrderButtonClick);

        MaterialButton exportOrderButton = view.findViewById(R.id.exportOrderButton);
        exportOrderButton.setOnClickListener(this::onExportOrderButtonClick);

        return view;
    }

    /**
     * This helper method retrieves the order associated with the
     * given order number, from the set of current store orders. If the
     * order is not found, null is returned.
     *
     * @param orderNumber int which is the order number of the order to find.
     * @return Order representing the order associated with the given order number.
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
     * This helper method retrieves all the order numbers of the orders
     * contained in the current set of store orders. The method returns the
     * numbers contained in an array list of strings.
     *
     * @return array list of strings representing the orders numbers of the
     * current store orders.
     */
    private ArrayList<String> retrieveAllOrderNumbers () {
        ArrayList<String> orderNumbers = new ArrayList<>();

        if (storeOrders.isEmpty()) {
            orderNumbers.add(getString(R.string.no_orders_available));
        } else {
            for (Order placedOrder : storeOrders) {
                orderNumbers.add(getString(R.string.order_num) +
                        placedOrder.getOrderNumberString());
            }
        }

        return orderNumbers;
    }

    /**
     * This method sets the total text views to formatted dollar amounts that are
     * given as doubles. The method updates the subtotal, sales tax, and order
     * total text views.
     *
     * @param subTotal double containing the current order subtotal.
     * @param salesTax double containing the current order sales tax.
     * @param orderTotal double containing the current order total.
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
     * This method refreshes the totals displayed in UI text views.
     * The method is called whenever there is a change to the current
     * selected order.
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
     * This method sets the values of the order number spinner that are retrieved
     * from the current set of store orders.
     *
     * @param view root view that contains the UI for the StoreOrdersFragment.
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
     * This method adds a listener to the order number spinner to detect any changed to
     * the selected order value. Based on the selected order, the items in the recycler view
     * and the totals are updated to reflect the order information in the selected order.
     *
     * @param view root view that contains the UI for the StoreOrdersFragment.
     */
    public void addOrderNumberSpinnerListener(View view) {
        Spinner orderNumberSpinner = view.findViewById(R.id.orderNumberSpinner);
        orderNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOrderNumberString = parent.getItemAtPosition(position).toString();

                if (selectedOrderNumberString.equals(getString(R.string.no_orders_available))) {
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
     * This is a helper method to update the recycler view items based on
     * the selected order.
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
     * This method is the on click action for the cancel order button.
     * Once clicked, an confirmation dialogue is shown. If canceled, the
     * selected order from the order number spinner is removed from the set of
     * current store orders.
     *
     * @param v root view that contains the UI for the StoreOrdersFragment.
     */
    private void onCancelOrderButtonClick(View v) {
        if (selectedOrder != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.alert_cancel_order);
            builder.setMessage(R.string.alert_cancel_order_desc);

            builder.setPositiveButton(R.string.alert_yes, (dialog, which) -> {
                storeOrders.remove(selectedOrder);
                storeOrdersViewModel.getStoreOrdersLiveData().setValue(storeOrders);
                setOrderNumberSpinner(requireView());
            });

            builder.setNegativeButton(R.string.alert_no, (dialog, which) -> dialog.cancel());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    /**
     * This method is the on click action for the export order button.
     * <p>
     * (FEATURE IS NOT IMPLEMENTED)
     *
     * @param v root view that contains the UI for the StoreOrdersFragment.
     */
    private void onExportOrderButtonClick(View v) {
        /* feature not implemented, toast is show for now */
        ToastUtils.showToast(getContext(),
                getString(R.string.toast_feature_unavailable),
                Toast.LENGTH_SHORT);
    }

    /**
     * This method is the on click action implemented for the delete
     * row item button in each row item of the recycler view.
     * <p>
     * (BUTTON IS DISABLED FOR THIS FRAGMENT)
     *
     * @param position int which contains the index position of the
     *                 row item where the delete row item button was
     *                 clicked.
     */
    @Override
    public void onDeleteRowItemButtonClicked(int position) {
        /* button is disabled in this fragment */
    }
}