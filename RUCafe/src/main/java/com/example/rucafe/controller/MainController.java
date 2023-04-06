package com.example.rucafe.controller;

import com.example.rucafe.model.Order;
import com.example.rucafe.utilities.View;
import com.example.rucafe.utilities.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.LinkedHashMap;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class MainController {

    private Order order;
    private LinkedHashMap<Integer, Order> storeOrders;

    /**
     *
     * @param order
     */
    public void setOrders(Order order, LinkedHashMap<Integer, Order> storeOrders) {
        this.order = order;
        this.storeOrders = storeOrders;
    }

    /**
     *
     * @param event
     */
    @FXML
    private void onOrderDonutsButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.ORDER_DONUT, this.order, this.storeOrders);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void onOrderCoffeeButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.ORDER_COFFEE, this.order, this.storeOrders);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void onShoppingBasketButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.SHOPPING_BASKET, this.order, this.storeOrders);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void onStoreOrdersButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.STORE_ORDERS, this.order, this.storeOrders);
    }

    /**
     *
     */
    public void initialize() {
    }
}