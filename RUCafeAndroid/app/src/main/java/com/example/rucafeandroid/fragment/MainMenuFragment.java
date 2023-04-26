package com.example.rucafeandroid.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rucafeandroid.R;
import com.example.rucafeandroid.model.Order;
import com.example.rucafeandroid.model.OrderViewModel;
import com.example.rucafeandroid.model.StoreOrdersViewModel;
import com.example.rucafeandroid.utils.randomIDGenerator;
import com.google.android.material.button.MaterialButton;

import java.util.LinkedHashSet;

/**
 * This is the controlling class for the Main Menu Fragment. It contains
 * the life cycle and UI methods of the fragment. The navigation actions
 * are also initialized in the class.
 *
 * @author Aryan Patel and Rushi Patel
 */
public class MainMenuFragment extends Fragment {

    private OrderViewModel orderViewModel;
    private Order order;

    private StoreOrdersViewModel storeOrdersViewModel;
    private LinkedHashSet<Order> storeOrders;

    /**
     * Required empty public constructor
     */
    public MainMenuFragment () {}

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
     * UI elements and navigation actions are initialized here.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return root view that contains the UI for the MainMenuFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        orderViewModel.getOrderLiveData().observe(getViewLifecycleOwner(),
                newOrder -> order = newOrder);

        storeOrdersViewModel.getStoreOrdersLiveData().observe(getViewLifecycleOwner(),
                newStoreOrders -> storeOrders = newStoreOrders);

        MaterialButton orderDonutsButton = view.findViewById(R.id.orderDonutsButton);
        MaterialButton orderCoffeeButton = view.findViewById(R.id.orderCoffeeButton);
        MaterialButton shoppingBasketButton = view.findViewById(R.id.shoppingBasketButton);
        MaterialButton storeOrdersButton = view.findViewById(R.id.storeOrdersButton);

        orderDonutsButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(
                R.id.action_mainMenuFragment_to_orderDonutFragment));

        orderCoffeeButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(
                R.id.action_mainMenuFragment_to_orderCoffeeFragment));

        shoppingBasketButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(
                R.id.action_mainMenuFragment_to_shoppingBasketFragment));

        storeOrdersButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(
                R.id.action_mainMenuFragment_to_storeOrdersFragment));

        return view;
    }
}