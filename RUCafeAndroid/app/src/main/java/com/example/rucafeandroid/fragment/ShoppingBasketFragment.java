package com.example.rucafeandroid.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rucafeandroid.R;
import com.example.rucafeandroid.adapter.MenuItemRecyclerViewAdapter;
import com.example.rucafeandroid.model.MenuItem;
import com.example.rucafeandroid.model.Order;
import com.example.rucafeandroid.model.OrderViewModel;
import com.example.rucafeandroid.model.StoreOrdersViewModel;
import com.example.rucafeandroid.utils.ToastUtils;
import com.example.rucafeandroid.utils.randomIDGenerator;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.LinkedHashSet;

/**
 * This is the controlling class for the Shopping Basket Fragment. It contains
 * the life cycle and UI methods of the fragment.
 *
 * @author Aryan Patel and Rushi Patel
 */
public class ShoppingBasketFragment extends Fragment implements MenuItemRecyclerViewAdapter.MenuItemListener {

    private static final double NJ_SALES_TAX = 0.06625;
    private static final double ZERO_DOLLARS = 0.00;

    private OrderViewModel orderViewModel;
    private Order order;

    private StoreOrdersViewModel storeOrdersViewModel;
    private LinkedHashSet<Order> storeOrders;

    private MenuItemRecyclerViewAdapter adapter;
    private TextView subtotalTextViewSB, salesTaxTextViewSB, orderTotalTextViewSB;
    private MaterialToolbar shoppingBasketFragmentToolbar;

    /**
     * Required public empty constructor().
     */
    public ShoppingBasketFragment() {}

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
     *
     * @return root view that contains the UI for the ShoppingBasketFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_basket, container, false);

        orderViewModel.getOrderLiveData().observe(getViewLifecycleOwner(),
                newOrder -> order = newOrder);

        storeOrdersViewModel.getStoreOrdersLiveData().observe(getViewLifecycleOwner(),
                newStoreOrders -> storeOrders = newStoreOrders);

        RecyclerView orderItemsRecyclerView = view.findViewById(R.id.orderItemsRecyclerView);
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MenuItemRecyclerViewAdapter(getContext(), false, order.getOrderItems());
        adapter.setListener(this);
        orderItemsRecyclerView.setAdapter(adapter);

        subtotalTextViewSB = view.findViewById(R.id.subtotalTextViewSB);
        salesTaxTextViewSB = view.findViewById(R.id.salesTaxTextViewSB);
        orderTotalTextViewSB = view.findViewById(R.id.orderTotalTextViewSB);

        MaterialButton placeOrderButton = view.findViewById(R.id.placeOrderButton);
        placeOrderButton.setOnClickListener(this::onPlaceOrderButtonClick);

        shoppingBasketFragmentToolbar = view.findViewById(R.id.shoppingBasketFragmentToolbar);
        setToolBarTitleOrderNumber(order.getOrderNumberString());

        updateTotals();

        return view;
    }

    /**
     * This method sets the tool bar title to contain the given order number.
     *
     * @param orderNumber string which contains the 9-digit order number,
     */
    private void setToolBarTitleOrderNumber (String orderNumber) {
        shoppingBasketFragmentToolbar.setTitle(getString(R.string.shopping_basket_title)
                + orderNumber);
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

        subtotalTextViewSB.setText(MessageFormat.format("{0}{1}",
                getResources().getString(R.string.subtotal_),
                decimalFormat.format(subTotal)));

        salesTaxTextViewSB.setText(MessageFormat.format("{0}{1}",
                getResources().getString(R.string.sales_tax_),
                decimalFormat.format(salesTax)));

        orderTotalTextViewSB.setText(MessageFormat.format("{0}{1}",
                getResources().getString(R.string.order_total_),
                decimalFormat.format(orderTotal)));
    }

    /**
     * This method refreshes the totals displayed in UI text views.
     * The method is called whenever there is a change to the current
     * order items in the shopping basket.
     *
     */
    private void updateTotals() {
        double salesTax, orderTotal, subTotal = ZERO_DOLLARS;

        if (order.getOrderItems().isEmpty()) {
            setTotals(subTotal, subTotal, subTotal);
            return;
        }

        for (MenuItem item : order.getOrderItems()) {
            int quantity = item.getQuantity();
            double selectionTotal = quantity * item.itemPrice();
            subTotal += selectionTotal;
        }

        salesTax = subTotal * NJ_SALES_TAX;
        orderTotal = subTotal + salesTax;

        setTotals(subTotal, salesTax, orderTotal);
    }

    /**
     * This method is the on click action for the place order button.
     * Once clicked, the current order is stored in the set containing all
     * the current store orders. A new order is created and set as the current
     * order.
     *
     * @param v root view that contains the UI for the ShoppingBasketFragment.
     */
    @SuppressLint("NotifyDataSetChanged")
    private void onPlaceOrderButtonClick(View v) {
        storeOrders.add(order);

        order = new Order(randomIDGenerator.generateRandomID(9));
        orderViewModel.setOrder(order);

        adapter.updateData(order.getOrderItems());
        adapter.notifyDataSetChanged();

        setToolBarTitleOrderNumber(order.getOrderNumberString());
        updateTotals();

        ToastUtils.showToast(getContext(),
                getResources().getString(R.string.toast_order_placed),
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
        order.getOrderItems().remove(position);

        adapter.updateData(order.getOrderItems());
        adapter.notifyDataSetChanged();

        updateTotals();

        ToastUtils.showToast(getContext(),
                getResources().getString(R.string.toast_item_removed),
                Toast.LENGTH_SHORT);
    }
}