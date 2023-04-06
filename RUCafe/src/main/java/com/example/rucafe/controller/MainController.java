package com.example.rucafe.controller;

import com.example.rucafe.model.Order;
import com.example.rucafe.utilities.View;
import com.example.rucafe.utilities.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.LinkedHashMap;

/**
 * This is the controller class for the Main Menu View.
 *
 * @author Aryan Patel, Rushi Patel
 */
public class MainController {

    /* Instance Variables */
    private Order order;
    private LinkedHashMap<Integer, Order> storeOrders;

    /**
     * Setter method for the current order and store orders.
     *
     * @param order current order of the application instance.
     * @param storeOrders current store orders of the application instance.
     */
    public void setOrders(Order order, LinkedHashMap<Integer, Order> storeOrders) {
        this.order = order;
        this.storeOrders = storeOrders;
    }

    /**
     * Event handler for the order donuts button click. Navigates to the Order Donut
     * View to select donuts for the current order.
     *
     * @param event ActionEvent of the button click.
     */
    @FXML
    private void onOrderDonutsButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.ORDER_DONUT, this.order, this.storeOrders);
    }

    /**
     * Event handler for the order coffee button click. Navigates to the Order Coffee
     * View to select coffee items for the current order.
     *
     * @param event ActionEvent of the button click.
     */
    @FXML
    private void onOrderCoffeeButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.ORDER_COFFEE, this.order, this.storeOrders);
    }

    /**
     * Event handler for the shopping basket button click. Navigates to the Shopping Basket
     * View to manage the items in the current order.
     *
     * @param event ActionEvent of the button click.
     */
    @FXML
    private void onShoppingBasketButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.SHOPPING_BASKET, this.order, this.storeOrders);
    }

    /**
     * Event handler for the store orders button click. Navigates to the Store Orders View
     * to manage the current orders of the store.
     *
     * @param event ActionEvent of the button click.
     */
    @FXML
    private void onStoreOrdersButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.STORE_ORDERS, this.order, this.storeOrders);
    }
}