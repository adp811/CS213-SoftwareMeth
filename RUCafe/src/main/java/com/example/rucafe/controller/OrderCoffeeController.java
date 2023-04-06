package com.example.rucafe.controller;

import com.example.rucafe.model.Coffee;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is the controller class for the Order Coffee view.
 *
 * @author Aryan Patel, Rushi Patel
 */
public class OrderCoffeeController {

    /* Instance Variables */
    private Order order;
    private LinkedHashMap<Integer, Order> storeOrders;
    private LinkedHashMap<String, Integer> coffeeItems;

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
     * This method takes the current checkbox selections in the view and puts them
     * into an ArrayList of Strings. An empty array is returned if nothing is checked.
     *
     * @return ArrayList of Strings containing the checked flavor boxes.
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
     * This method adds a coffee item key value pair into the hashmap representing
     * the list of coffeeItems waiting to be submitted to the order. The key is the
     * cup size and add-in flavors, the value is the quantity.
     *
     * @param cupSize String which contins the cup size.
     * @param addins String which contains the add-in flavors, comma separated.
     * @param quantity int which contains the quantity of the coffee item
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
     * This method removes a coffee item key value pair from the hashmap representing
     * the list of coffeeItems waiting to be submitted to the order. The key is the
     * cup size and add-in flavors, the value is the quantity.
     *
     * @param cupSize String which contains the cup size.
     * @param addins String which contains the add-in flavors, comma separated.
     */
    private void removeFromCoffeeItems(String cupSize, String addins) {
        String key = cupSize;
        if (!addins.isEmpty()) key = key + "," + addins;
        coffeeItems.remove(key);
    }

    /**
     * This method updates the list view of coffee items waiting to be submitted.
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

        ObservableList<String> selectedCoffeeItems = FXCollections.observableArrayList(items);
        coffeeItemListView.setItems(selectedCoffeeItems);
        updateSubtotal();
    }

    /**
     * This method updates the subtotal for the items currently in the coffee item list.
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
     * Event handler for the add coffee button click to add the selected coffee item to
     * the list of coffee items.
     *
     * @param event ActionEvent of the button click.
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
     * Event handler for the remove coffee button click to remove the selected coffee
     * item from the list of coffee items.
     *
     * @param event ActionEvent of the button click.
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
     * Event handler to add the items in the list of coffee items into the
     * current order.
     *
     * @param event Action event of the button click.
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
     * This method takes care any initial setup when loading the view.
     */
    @FXML
    public void initialize() {
        coffeeItems = new LinkedHashMap<String, Integer>();

        ObservableList<String> cupSizes = FXCollections.observableArrayList(Coffee.SHORT, Coffee.TALL, Coffee.GRANDE,
                Coffee.VENTI);

        ObservableList<Integer> quantityAmounts = FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList()));

        cupSizeComboBox.setItems(cupSizes);
        quantityComboBox.setItems(quantityAmounts);
        quantityComboBox.setValue(1);
    }
}
