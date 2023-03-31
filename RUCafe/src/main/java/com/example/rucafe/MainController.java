package com.example.rucafe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class MainController {

    private Order order;

    /**
     *
     * @param order
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     *
     * @param event
     */
    @FXML
    private void onOrderDonutsButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.ORDER_DONUT, this.order);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void onOrderCoffeeButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.ORDER_COFFEE, this.order);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void onShoppingBasketButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.SHOPPING_BASKET, this.order);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void onStoreOrdersButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.STORE_ORDERS, this.order);
    }

    /**
     *
     */
    public void initialize() {
    }
}