package com.example.rucafeandroid;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @author Aryan Patel and Rushi Patel
 */
public class MainMenuFragment extends Fragment {

    public MainMenuFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

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