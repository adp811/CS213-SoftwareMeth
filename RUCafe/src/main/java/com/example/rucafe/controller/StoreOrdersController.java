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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class StoreOrdersController {

    /* Instance Variables */
    private Order order;
    private LinkedHashMap<Integer, Order> storeOrders;
    private ObservableList<Integer> orderNumbers;
    private ObservableList<String> orderItems;

    /* FXML Elements */
    @FXML
    private ComboBox<Integer> orderNumberComboBox;

    @FXML
    private ListView<String> orderItemsListView;

    @FXML
    private TextField subTotalTextField, salesTaxTextField, orderTotalTextField;

    /**
     *
     * @param order
     */
    public void setOrders(Order order, LinkedHashMap<Integer, Order> storeOrders) {
        this.order = order;
        this.storeOrders = storeOrders;
        updateOrderNumbers();
    }

    /**
     *
     * @return
     */
    public void updateOrderNumbers() {
        ArrayList<Integer> keys;
        if (this.storeOrders.isEmpty()) {
            keys = new ArrayList<Integer>();
        } else {
            keys = new ArrayList<Integer>(storeOrders.keySet());
        }

        orderNumbers = FXCollections.observableArrayList(keys);
        orderNumberComboBox.setItems(orderNumbers);
    }

    public void updateOrderAmounts(Order order) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        subTotalTextField.setText("Subtotal: \t$" + decimalFormat.format(order.getOrderSubtotal()));
        salesTaxTextField.setText("Sales Tax: \t$" + decimalFormat.format(order.getOrderSalesTax()));
        orderTotalTextField.setText("Order Total: \t$" + decimalFormat.format(order.getOrderTotalAmount()));
    }

    public void updateOrderItems(int orderNumber) {
        if (!this.storeOrders.containsKey(orderNumber)){
            return;
        }

        Order selectedOrder = storeOrders.get(orderNumber);
        ArrayList<String> items = new ArrayList<String>();
        for (MenuItem item: selectedOrder.getOrderItems()) {
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
        updateOrderAmounts(selectedOrder);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void onBackButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.MAIN_MENU, this.order, this.storeOrders);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void handleCancelOrderButtonClick(ActionEvent event) {
        Integer orderNumber = orderNumberComboBox.getSelectionModel().getSelectedItem();

        if (orderNumber == null) {
            AlertBox.showAlert(Alert.AlertType.WARNING, "", "No Order Selected!",
                    "Please select the order you would like to cancel.");
            return;
        }

        if (!this.storeOrders.containsKey(orderNumber)) {
            return;
        }

        this.storeOrders.remove(orderNumber);
        orderNumberComboBox.getSelectionModel().clearSelection();
        subTotalTextField.clear();
        salesTaxTextField.clear();
        orderTotalTextField.clear();
        orderItemsListView.getItems().clear();
        updateOrderNumbers();
    }

    /**
     *
     * @param event
     */
    @FXML
    private void handleExportButtonClick(ActionEvent event) {
    }

    /**
     *
     */
    @FXML
    public void initialize() {
        orderNumberComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (this.storeOrders.containsKey(newValue)) {
                updateOrderItems(newValue);
            }
        });
    }
}
