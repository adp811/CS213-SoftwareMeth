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
import javafx.scene.control.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class OrderCoffeeController {

    /* Instance Variables */
    private Order order;
    private LinkedHashMap<Integer, Order> storeOrders;
    private LinkedHashMap<String, Integer> coffeeItems;
    private ObservableList<String> cupSizes, selectedCoffeeItems;
    private ObservableList<Integer> quantityAmounts;

    /* FXML Elements */
    @FXML
    private ComboBox<String> cupSizeComboBox;

    @FXML
    private ComboBox<Integer> quantityComboBox;

    @FXML
    private ListView<String> coffeeItemListView;

    @FXML
    private CheckBox sweetCreamCheckBox, irishCreamCheckBox, frenchVanillaCheckBox,
                     caramelCheckBox, mochaCheckBox, peppermintCheckBox;
    @FXML
    private TextField subtotalTextField;

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
     * @return
     */
    private String getSelectedAddIns() {
        ArrayList<String> addIns = new ArrayList<>();
        if (sweetCreamCheckBox.isSelected()) addIns.add(Coffee.SWEET_CREAM);
        if (irishCreamCheckBox.isSelected()) addIns.add(Coffee.IRISH_CREAM);
        if (frenchVanillaCheckBox.isSelected()) addIns.add(Coffee.FRENCH_VANILLA);
        if (caramelCheckBox.isSelected()) addIns.add(Coffee.CARAMEL);
        if (mochaCheckBox.isSelected()) addIns.add(Coffee.MOCHA);
        if (peppermintCheckBox.isSelected()) addIns.add(Coffee.PEPPERMINT);

        return String.join(",", addIns);
    }

    /**
     *
     * @param cupSize
     * @param addins
     * @param quantity
     */
    private void addToCoffeeItems(String cupSize, String addins, int quantity) {
        String key = cupSize;
        if (!addins.isEmpty()) key = key + "," + addins;

        if (coffeeItems.containsKey(key)) {
            coffeeItems.replace(key, quantity);
        } else {
            coffeeItems.put(key, quantity);
        }
    }

    /**
     *
     * @param cupSize
     * @param addins
     */
    private void removeFromCoffeeItems(String cupSize, String addins) {
        String key = cupSize;
        if (!addins.isEmpty()) key = key + "," + addins;
        coffeeItems.remove(key);
    }

    /**
     *
     */
    private void updateCoffeeItems() {
        ArrayList<String> items = new ArrayList<>();
        for (Map.Entry<String, Integer> item : coffeeItems.entrySet()) {
            int quantity = item.getValue();
            if (item.getKey().contains(",")) { /* contains addins */
                String cupSize = item.getKey().split(",", 2)[0];
                String[] addins = item.getKey().split(",", 2)[1].split(",");
                items.add("(" + quantity + ", " + cupSize + ") - " + String.join(", ", addins));

            } else { /* no addins */
                String cupSize = item.getKey();
                items.add("(" + quantity + ", " + cupSize + ")");
            }
        }

        selectedCoffeeItems = FXCollections.observableArrayList(items);
        coffeeItemListView.setItems(selectedCoffeeItems);
        updateSubtotal();
    }

    /**
     *
     */
    private void updateSubtotal() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        Double subTotal = 0.00;

        if(coffeeItems.isEmpty()) {
            subtotalTextField.setText("$" + decimalFormat.format(subTotal));
            return;
        }

        for (Map.Entry<String, Integer> item : coffeeItems.entrySet()) {
            int quantity = item.getValue();
            String cupSize;
            double itemTotal = 0.00;

            if (item.getKey().contains(",")) { /* contains addins */
                cupSize = item.getKey().split(",", 2)[0];
                String[] addins = item.getKey().split(",", 2)[1].split(",");
                itemTotal = itemTotal + (Coffee.SINGLE_ADD_IN_PRICE * addins.length);
            } else { /* no addins */
                cupSize = item.getKey();
            }

            switch (cupSize) {
                case Coffee.SHORT -> itemTotal += Coffee.SHORT_PRICE;
                case Coffee.TALL -> itemTotal += Coffee.TALL_PRICE;
                case Coffee.GRANDE -> itemTotal += Coffee.GRANDE_PRICE;
                case Coffee.VENTI -> itemTotal += Coffee.VENTI_PRICE;
            }

            itemTotal *= quantity;
            subTotal += itemTotal;
        }

        subtotalTextField.setText("$" + decimalFormat.format(subTotal));
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
    private void handleAddCoffeeItemButtonClick(ActionEvent event) {
        if (cupSizeComboBox.getSelectionModel().getSelectedItem() == null) {
            AlertBox.showAlert(Alert.AlertType.WARNING, "", "Missing Cup Size Selection!",
                    "Please choose a cup size before adding item to the list."); return;
        }

        int quantity = quantityComboBox.getSelectionModel().getSelectedItem();
        String cupSize = cupSizeComboBox.getSelectionModel().getSelectedItem();
        String addIns = getSelectedAddIns();

        addToCoffeeItems(cupSize, addIns, quantity);
        updateCoffeeItems();
    }

    /**
     *
     * @param event
     */
    @FXML
    private void handleRemoveCoffeeItemButtonClick(ActionEvent event) {
        if (coffeeItemListView.getSelectionModel().getSelectedItem() == null) {
            AlertBox.showAlert(Alert.AlertType.WARNING, "", "No Coffee Item is Selected!",
                    "Please choose the coffee item you want to remove in the list below.");
            return;
        }

        String itemInfo = coffeeItemListView.getSelectionModel().getSelectedItem();

        String cupSize, addIns;
        if(itemInfo.contains("-")) { /* contains addins  */
            addIns = itemInfo.split(" - ")[1].replaceAll(",\\s*", ",");
            cupSize = itemInfo.split(" - ")[0].split(",")[1].trim().replaceAll("\\)", "");;
            System.out.println(addIns);
            System.out.println(cupSize);
            removeFromCoffeeItems(cupSize, addIns);

        } else { /* no addins */
            cupSize = itemInfo.split(",")[1].trim().replaceAll("\\)", "");
            System.out.println(cupSize);
            removeFromCoffeeItems(cupSize, "");
        }

        updateCoffeeItems();
    }

    /**
     *
     * @param event
     */
    @FXML
    private void handleAddToOrderButtonClick(ActionEvent event) {
        if (coffeeItems.isEmpty()) {
            AlertBox.showAlert(Alert.AlertType.WARNING, "", "No Items Are Selected!",
                    "Please make sure to select items to add to your order."); return;
        }

        for (Map.Entry<String, Integer> item : coffeeItems.entrySet()) {
            MenuItem coffeeItem;
            int quantity = item.getValue();

            if (item.getKey().contains(",")) { /* contains addins */
                String cupSize = item.getKey().split(",", 2)[0];
                String[] addInsArray = item.getKey().split(",", 2)[1].split(",");
                Set<String> addIns = new HashSet<>(Arrays.asList(addInsArray));
                coffeeItem = new Coffee(quantity, cupSize, (HashSet<String>) addIns);

            } else { /* no addins */
                String cupSize = item.getKey();
                coffeeItem = new Coffee(quantity, cupSize);

            }

            this.order.addToOrderItems(coffeeItem);
        }

        coffeeItems.clear();
        updateCoffeeItems();

        AlertBox.showAlert(Alert.AlertType.INFORMATION, "", "Selections Submitted Successfully!",
                "Your coffee selections have been added to your order. Please navigate to the " +
                        "shopping basket to view and edit the items.");
    }

    /**
     *
     */
    public void initialize() {

        coffeeItems = new LinkedHashMap<String, Integer>();

        cupSizes = FXCollections.observableArrayList(Coffee.SHORT, Coffee.TALL, Coffee.GRANDE,
                Coffee.VENTI);

        quantityAmounts = FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList()));

        cupSizeComboBox.setItems(cupSizes);
        quantityComboBox.setItems(quantityAmounts);
        quantityComboBox.setValue(1);

    }
}
