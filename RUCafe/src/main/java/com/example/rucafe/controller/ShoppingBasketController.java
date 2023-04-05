package com.example.rucafe.controller;

import com.example.rucafe.model.Coffee;
import com.example.rucafe.model.Donut;
import com.example.rucafe.model.MenuItem;
import com.example.rucafe.model.Order;
import com.example.rucafe.utilities.AlertBox;
import com.example.rucafe.utilities.View;
import com.example.rucafe.utilities.ViewSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class ShoppingBasketController {

    /* Instance Variables */
    private Order order;
    private ObservableList<String> orderItems;

    private static final double NJ_SALES_TAX_RATE = 0.06625;

    /* FXML Elements */
    @FXML
    private ListView<String> orderItemsListView;

    @FXML
    private TextField subTotalTextField, salesTaxTextField, orderTotalTextField;

    /**
     *
     * @param order
     */
    public void setOrder(Order order) {
        this.order = order;
        updateOrderItems();
    }

    /**
     *
     * @param subTotal
     * @param taxRate
     */
    private void setAmounts(double subTotal, double taxRate) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        Double salesTax = subTotal * taxRate;
        Double orderTotal = subTotal + salesTax;

        subTotalTextField.setText("Subtotal: \t$" + decimalFormat.format(subTotal));
        salesTaxTextField.setText("Sales Tax: \t$" + decimalFormat.format(salesTax));
        orderTotalTextField.setText("Order Total: \t$" + decimalFormat.format(orderTotal));
    }

    /**
     *
     */
    private void updateAmounts() {
        double subTotal = 0.00;

        if (this.order.getOrderItems().isEmpty()) {
            setAmounts(subTotal, NJ_SALES_TAX_RATE);
            return;
        }

        for (MenuItem item: this.order.getOrderItems()) {
            double unitPrice = 0.00;
            if (item instanceof Donut) {
                switch(((Donut) item).getType()){
                    case Donut.YEAST -> unitPrice = Donut.YEAST_PRICE;
                    case Donut.CAKE -> unitPrice = Donut.CAKE_PRICE;
                    case Donut.HOLE -> unitPrice = Donut.HOLE_PRICE;
                }
            } else if (item instanceof Coffee) {
                switch (((Coffee) item).getCupSize()) {
                    case Coffee.SHORT -> unitPrice = Coffee.SHORT_PRICE;
                    case Coffee.TALL -> unitPrice = Coffee.TALL_PRICE;
                    case Coffee.GRANDE -> unitPrice = Coffee.GRANDE_PRICE;
                    case Coffee.VENTI -> unitPrice = Coffee.VENTI_PRICE;
                }
                unitPrice = unitPrice + (Coffee.SINGLE_ADD_IN_PRICE * ((Coffee) item).getAddIns().size());
            }

            subTotal += unitPrice * item.getQuantity();
        }

        setAmounts(subTotal, NJ_SALES_TAX_RATE);
    }

    /**
     *
     */
    public void updateOrderItems() {
        if (this.order.getOrderItems().isEmpty()) {
            orderItemsListView.getItems().clear();
            subTotalTextField.clear();
            salesTaxTextField.clear();
            orderTotalTextField.clear();
            return;
        }

        ArrayList<String> items = new ArrayList<>();
        for (MenuItem item: this.order.getOrderItems()) {
            String itemInformation = null;
            if (item instanceof Donut) {
                itemInformation = item.getQuantity() + " - "
                        + ((Donut) item).getType()
                        + ", " + ((Donut) item).getFlavor();
            } else if (item instanceof Coffee) {
                itemInformation = item.getQuantity() + " - "
                        + "(" + ((Coffee) item).getCupSize() + ")"
                        + " " + String.join(", ", ((Coffee) item).getAddIns());
            }
            if (itemInformation != null) {
                items.add(itemInformation);
            }
        }

        orderItems = FXCollections.observableArrayList(items);
        orderItemsListView.setItems(orderItems);
        updateAmounts();
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
     * @param event
     */
    @FXML
    private void handleRemoveItemButtonClick(ActionEvent event) {
        if (orderItemsListView.getSelectionModel().getSelectedIndex() == -1) {
            AlertBox.showAlert(Alert.AlertType.WARNING,
                    "",
                    "No Item Selected!",
                    "Please choose the menu item you would like to remove above.");
            return;
        }

        int selectedIndex = orderItemsListView.getSelectionModel().getSelectedIndex();
        order.getOrderItems().remove(selectedIndex);
        updateOrderItems();

        AlertBox.showAlert(Alert.AlertType.INFORMATION,
                "",
                "Item Removed Successfully!",
                "The selected item has been removed from your order. ");
    }

    /**
     *
     * @param event
     */
    @FXML
    private void handlePlaceOrderButtonClick(ActionEvent event) {

    }


    /**
     *
     */
    public void initialize() {
    }
}
