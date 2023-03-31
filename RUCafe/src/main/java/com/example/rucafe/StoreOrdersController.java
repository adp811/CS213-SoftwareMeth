package com.example.rucafe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class StoreOrdersController {

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
