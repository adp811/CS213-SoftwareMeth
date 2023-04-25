package com.example.rucafeandroid;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rucafeandroid.model.Order;
import com.example.rucafeandroid.model.OrderViewModel;
import com.example.rucafeandroid.model.StoreOrdersViewModel;
import com.example.rucafeandroid.utils.randomIDGenerator;

import java.util.LinkedHashSet;

/**
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

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        orderViewModel.getOrderLiveData().observe(getViewLifecycleOwner(),
                newOrder -> { order = newOrder; });

        storeOrdersViewModel.getStoreOrdersLiveData().observe(getViewLifecycleOwner(),
                newStoreOrders -> { storeOrders = newStoreOrders; });

        AppCompatImageButton orderDonutsButton = view.findViewById(R.id.orderDonutsButton);
        AppCompatImageButton orderCoffeeButton = view.findViewById(R.id.orderCoffeeButton);
        AppCompatImageButton shoppingBasketButton = view.findViewById(R.id.shoppingBasketButton);
        AppCompatImageButton storeOrdersButton = view.findViewById(R.id.storeOrdersButton);

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