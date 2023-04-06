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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * This is the controller class for the Store Orders View.
 *
 * @author Aryan Patel, Rushi Patel
 */
public class StoreOrdersController {

    /* Instance Variables */
    private Order order;
    private LinkedHashMap<Integer, Order> storeOrders;

    /* FXML Elements */
    @FXML
    private ComboBox<Integer> orderNumberComboBox;

    @FXML
    private ListView<String> orderItemsListView;

    @FXML
    private TextField subTotalTextField, salesTaxTextField, orderTotalTextField;

    @FXML
    private Button exportButton;

    /**
     * Setter method for the current order and store orders.
     *
     * @param order current order of the application instance.
     * @param storeOrders current store orders of the application instance.
     */
    public void setOrders(Order order, LinkedHashMap<Integer, Order> storeOrders) {
        this.order = order;
        this.storeOrders = storeOrders;

        updateOrderNumbers();
    }

    /**
     * This method updates the order numbers currently available to view.
     */
    public void updateOrderNumbers() {
        ArrayList<Integer> keys;
        if (this.storeOrders.isEmpty()) {
            keys = new ArrayList<>();
        } else {
            keys = new ArrayList<>(storeOrders.keySet());
        }

        ObservableList<Integer> orderNumbers = FXCollections.observableArrayList(keys);
        orderNumberComboBox.setItems(orderNumbers);
    }

    /**
     * This method takes care of updating the order amount text fields given a
     * specific order.
     *
     * @param order Order object representing the order to print the amounts for.
     */
    public void updateOrderAmounts(Order order) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        subTotalTextField.setText("Subtotal: \t$" + decimalFormat.format(order.getOrderSubtotal()));
        salesTaxTextField.setText("Sales Tax: \t$" + decimalFormat.format(order.getOrderSalesTax()));
        orderTotalTextField.setText("Order Total: \t$" + decimalFormat.format(order.getOrderTotalAmount()));
    }

    /**
     * This method lists all the order items in the list view. The order items are
     * retrieved using the given order number.
     *
     * @param orderNumber int which represents the order ID of the order.
     */
    public void updateOrderItems(int orderNumber) {
        if (!this.storeOrders.containsKey(orderNumber)){
            return;
        }

        Order selectedOrder = storeOrders.get(orderNumber);
        ArrayList<String> items = new ArrayList<>();
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

        ObservableList<String> orderItems = FXCollections.observableArrayList(items);
        orderItemsListView.setItems(orderItems);
        updateOrderAmounts(selectedOrder);
    }

    /**
     * Event handler for the back button to switch the view
     * back to the main menu.
     *
     * @param event ActionEvent of the button click.
     */
    @FXML
    private void onBackButtonClick(ActionEvent event) {
        ViewSwitcher.switchTo(View.MAIN_MENU, this.order, this.storeOrders);
    }

    /**
     * Event handler for the cancel order button click to cancel the current
     * selected order, removing it from the current store orders.
     *
     * @param event ActionEvent of the button click.
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
     * Event handler for the export order button click to export the current
     * selected order as a text file. A File Chooser is prompted to the user in order
     * to select the destination path of the file that being exported.
     *
     * @param event ActionEvent of the button click.
     */
    @FXML
    private void handleExportButtonClick(ActionEvent event) {
        Integer orderNumber = orderNumberComboBox.getSelectionModel().getSelectedItem();
        if (orderNumber == null) {
            AlertBox.showAlert(Alert.AlertType.WARNING, "", "No Order Selected!",
                    "Please select the order you would like to export.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("ORDER_" + orderNumber + "_INFO.txt");

        Stage stage = (Stage) exportButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("Order Number: " + orderNumber);
                writer.newLine(); writer.newLine(); writer.newLine();

                for (String item : orderItemsListView.getItems()) {
                    writer.write(item + "\n");
                }

                writer.newLine(); writer.newLine(); writer.newLine();
                writer.write("Order Subtotal: " + this.storeOrders.get(orderNumber).getOrderSubtotal() + "\n");
                writer.write("Order Sales Tax: " + this.storeOrders.get(orderNumber).getOrderSalesTax() + "\n");
                writer.write("Order Total: " + this.storeOrders.get(orderNumber).getOrderTotalAmount() + "\n");
                writer.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method takes care of adding a listener to the order number combobox for any new
     * value changes. The listener updates the order items list based on the selected order
     * number.
     */
    private void addOrderNumberComboBoxListener() {
        orderNumberComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (this.storeOrders.containsKey(newValue)) {
                updateOrderItems(newValue);
            }
        });
    }

    /**
     * This method takes care any initial setup when loading the view.
     */
    @FXML
    public void initialize() {
        orderItemsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addOrderNumberComboBoxListener();
    }
}
