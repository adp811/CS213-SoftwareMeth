package com.example.rucafe.controller;

import com.example.rucafe.model.Order;
import com.example.rucafe.utilities.View;
import com.example.rucafe.utilities.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class ShoppingBasketController {

    /* Instance Variables */
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
    private void onBackButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.MAIN_MENU, this.order);
    }

    /**
     *
     */
    public void initialize() {
    }
}
