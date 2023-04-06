package com.example.rucafe.controller;

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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is the controller class for the Order Donut View.
 *
 * @author Aryan Patel, Rushi Patel
 */
public class OrderDonutController {

    /* Instance Variables */
    private Order order;
    private LinkedHashMap<Integer, Order> storeOrders;
    private LinkedHashMap<String, String> selectedDonuts;
    private ObservableList<String> yeastTypeFlavors, cakeTypeFlavors, holeTypeFlavors;

    /* FXML Elements */
    @FXML
    private ComboBox<String> donutTypeComboBox;

    @FXML
    private ComboBox<Integer> quantityComboBox;

    @FXML
    private ListView<String> availableFlavorsListView, selectedFlavorsListView;

    @FXML
    private ImageView donutTypeImageView;

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
     * This method adds a donut item key value pair into the hashmap representing
     * the list of donuts waiting to be submitted to the order. The key is the
     * flavor and the value is the quantity and donut type.
     *
     * @param selection String which contains information of the donut item.
     */
    private void addToSelectedDonuts(String selection) {
        String[] selectionInfo = selection.split(",");

        String quantity = selectionInfo[0];
        String type = selectionInfo[1];
        String flavor = selectionInfo[2];

        if(selectedDonuts.containsKey(flavor)){
            selectedDonuts.replace(flavor, quantity + "-" + type);
        } else {
            selectedDonuts.put(flavor, quantity + "-" + type);
        }
    }

    /**
     * This method removes a donut item key value pair from the hashmap representing
     * the list of donuts waiting to be submitted to the order. The key is the
     * flavor, which is used to retrieve the pair.
     *
     * @param flavor String which contains the donut flavor.
     */
    private void removeFromSelectedDonuts(String flavor) {
        selectedDonuts.remove(flavor);
    }

    /**
     * This method updates the subtotal for the items currently in the donut selection
     * item list.
     */
    private void updateSubtotal() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        Double subTotal = 0.00;

        if(selectedDonuts.isEmpty()) {
            subtotalTextField.setText("$" + decimalFormat.format(subTotal));
            return;
        }

        for (Map.Entry<String, String> selection : selectedDonuts.entrySet()) {
            int quantity = Integer.parseInt(selection.getValue().split("-")[0]);
            String type = selection.getValue().split("-")[1];
            double selectionTotal = 0.00;

            switch (type) {
                case "Yeast Donut" -> selectionTotal = Donut.YEAST_PRICE;
                case "Cake Donut" -> selectionTotal = Donut.CAKE_PRICE;
                case "Hole Donut" -> selectionTotal = Donut.HOLE_PRICE;
            }

            selectionTotal *= quantity;
            subTotal += selectionTotal;
        }

        subtotalTextField.setText("$" + decimalFormat.format(subTotal));
    }

    /**
     * This method updates the list view of donut selection items waiting to be submitted
     * to the current order.
     */
    private void updateSelectedDonuts() {
        ArrayList<String> selections = new ArrayList<>();
        for (Map.Entry<String, String> selection : selectedDonuts.entrySet()) {
            String quantity = selection.getValue().split("-")[0];
            String type = selection.getValue().split("-")[1];
            String flavor = selection.getKey();

            switch (type) {
                case "Yeast Donut" -> type = "Y";
                case "Cake Donut" -> type = "C";
                case "Hole Donut" -> type = "H";
            }

            selections.add("(" + quantity + ", " + type + ") " + flavor);
        }

        ObservableList<String> selectedFlavors = FXCollections.observableArrayList(selections);
        selectedFlavorsListView.setItems(selectedFlavors);
        updateSubtotal();
    }

    /**
     * This method sets the image view in the Order Donut view based on the donut type given.
     *
     * @param donutType String which contains the donut type.
     */
    private void setDonutTypeImage(String donutType) {
        String imagePath;

        switch (donutType){
            case Donut.YEAST -> imagePath = "/com/example/rucafe/images/yeast_donuts.jpg";
            case Donut.CAKE -> imagePath = "/com/example/rucafe/images/cake_donuts.jpg";
            case Donut.HOLE -> imagePath = "/com/example/rucafe/images/hole_donuts.jpg";
            default -> imagePath = "/com/example/rucafe/images/placeholder_donuts.png";
        }

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        donutTypeImageView.setImage(image);
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
     * Event handler for the add donut selection button click to add the selected
     * donut item to the list of donut selection items.
     *
     * @param event ActionEvent of the button click.
     */
    @FXML
    private void handleAddSelectionButtonClick(ActionEvent event) {
        if (donutTypeComboBox.getValue() == null) {
            AlertBox.showAlert(Alert.AlertType.WARNING,
                    "", "Missing Donut Type!", "Please select a donut type before" +
                            " adding to donut selections."); return;
        }
        if (availableFlavorsListView.getSelectionModel().getSelectedItem() == null) {
            AlertBox.showAlert(Alert.AlertType.WARNING,
                    "", "Missing Flavor Selection!", "Please select a flavor before" +
                            " adding to donut selections."); return;
        }

        String selection = quantityComboBox.getValue() +
                "," + donutTypeComboBox.getValue() +
                "," + availableFlavorsListView.getSelectionModel().getSelectedItem();

        addToSelectedDonuts(selection);
        updateSelectedDonuts();
    }

    /**
     * Event handler for the remove donut selection button click to remove the selected
     * donut item from the list of donut selection items.
     *
     * @param event ActionEvent of the button click.
     */
    @FXML
    private void handleRemoveSelectionButtonClick(ActionEvent event) {
        String selectedFlavor;
        if ((selectedFlavor = selectedFlavorsListView.getSelectionModel().getSelectedItem()) == null) {
            AlertBox.showAlert(Alert.AlertType.WARNING,
                    "", "Missing Flavor Selection!", "Please select an added flavor" +
                            " in order to remove donut selection."); return;
        }

        removeFromSelectedDonuts(selectedFlavor.replaceAll("\\([^)]*\\)\\s*", ""));
        updateSelectedDonuts();
    }

    /**
     * Event handler to add the items in the list of donut selection items into the
     * current order.
     *
     * @param event Action event of the button click.
     */
    @FXML
    private void handleAddToOrderButtonClick(ActionEvent event) {
        if (selectedDonuts.isEmpty()) {
            AlertBox.showAlert(Alert.AlertType.WARNING, "", "No Items Are Selected!",
                    "Please make sure to select items to add to your order."); return;
        }

        for (Map.Entry<String, String> selection : selectedDonuts.entrySet()) {
            int quantity = Integer.parseInt(selection.getValue().split("-")[0]);
            String type = selection.getValue().split("-")[1];
            String flavor = selection.getKey();

            MenuItem item = new Donut(quantity, type, flavor);
            this.order.addToOrderItems(item);
        }

        selectedDonuts.clear();
        updateSelectedDonuts();

        AlertBox.showAlert(Alert.AlertType.INFORMATION, "", "Selections Submitted Successfully!",
                "Your donut selections have been added to your order. Please navigate to the " +
                        "shopping basket to view and edit the items.");
    }

    /**
     * This method takes care of adding a listener to the donut type combobox for any new
     * value changes. The listener updates the image view and available flavors list view
     * depending on the selected donut type.
     */
    private void addDonutTypeComboBoxListener() {
        donutTypeComboBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            switch (newValue) {
                case Donut.YEAST -> {
                    availableFlavorsListView.setItems(yeastTypeFlavors);
                    setDonutTypeImage(Donut.YEAST);
                }
                case Donut.CAKE -> {
                    availableFlavorsListView.setItems(cakeTypeFlavors);
                    setDonutTypeImage(Donut.CAKE);
                }
                case Donut.HOLE -> {
                    availableFlavorsListView.setItems(holeTypeFlavors);
                    setDonutTypeImage(Donut.HOLE);
                }
            }
        });
    }

    /**
     * This method takes care any initial setup when loading the view.
     */
    @FXML
    private void initialize() {
        selectedDonuts = new LinkedHashMap<>();
        updateSelectedDonuts();

        availableFlavorsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        selectedFlavorsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        yeastTypeFlavors = FXCollections.observableArrayList(
                Donut.CHOCOLATE_FROSTED, Donut.STRAWBERRY_FROSTED, Donut.PUMPKIN_FROSTED,
                Donut.BOSTON_KREME, Donut.SUGAR_GLAZED, Donut.JELLY_FILLED);

        cakeTypeFlavors = FXCollections.observableArrayList(
                Donut.KEY_LIME, Donut.APPLE_CRUMB, Donut.RED_VELVET, Donut.DOUBLE_CHOCOLATE);

        holeTypeFlavors = FXCollections.observableArrayList(
                Donut.MAPLE, Donut.CINNAMON, Donut.POWDERED_SUGAR
        );

        ObservableList<String> donutTypes = FXCollections.observableArrayList(
                Donut.YEAST, Donut.CAKE, Donut.HOLE
        );

        ObservableList<Integer> quantityAmounts = FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 50).boxed().collect(Collectors.toList()));

        quantityComboBox.setValue(1);
        donutTypeComboBox.setItems(donutTypes);
        quantityComboBox.setItems(quantityAmounts);

        addDonutTypeComboBoxListener();
    }
}
