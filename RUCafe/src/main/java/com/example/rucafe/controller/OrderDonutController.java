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
import javafx.scene.control.*;
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
 *
 * @author Aryan Patel, Rushi Patel
 */
public class OrderDonutController {

    /* Instance Variables */
    private Order order;
    private LinkedHashMap<String, String> selectedDonuts;
    private ObservableList<String> yeastTypeFlavors, cakeTypeFlavors, holeTypeFlavors;
    private ObservableList<String> donutTypes, selectedFlavors;
    private ObservableList<Integer> quantityAmounts;

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
     *
     * @param order
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     *
     * @param selection
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
     *
     * @param flavor
     */
    private void removeFromSelectedDonuts(String flavor) {
        selectedDonuts.remove(flavor);
    }

    /**
     *
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
     *
     * @return
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

        selectedFlavors = FXCollections.observableArrayList(selections);
        selectedFlavorsListView.setItems(selectedFlavors);
        updateSubtotal();
    }

    /**
     *
     * @param donutType
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
    private void handleAddSelectionButtonClick(ActionEvent event) {
        String selectedType, selectedFlavor;
        int selectedQuantity;

        if ((selectedType = donutTypeComboBox.getValue()) == null) {
            AlertBox.showAlert(Alert.AlertType.WARNING,
                    "", "Missing Donut Type!", "Please select a donut type before" +
                            " adding to donut selections."); return;
        }
        if ((selectedFlavor = availableFlavorsListView.getSelectionModel().getSelectedItem()) == null) {
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
     *
     * @param event
     */
    @FXML
    private void handleRemoveSelectionButtonClick(ActionEvent event) {
        String selectedFlavor;
        if ((selectedFlavor = selectedFlavorsListView.getSelectionModel().getSelectedItem()) == null) {
            AlertBox.showAlert(Alert.AlertType.WARNING,
                    "", "Missing Flavor Selection!", "Please select an added flavor" +
                            " in order to remove donut selection."); return;
        }

        removeFromSelectedDonuts(selectedFlavor.replaceAll("\\([^\\)]*\\)\\s*", ""));
        updateSelectedDonuts();
    }

    /**
     *
     * @param event
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
     *
     */
    @FXML
    private void initialize() {
        selectedDonuts = new LinkedHashMap<String, String>();

        updateSelectedDonuts();

        yeastTypeFlavors = FXCollections.observableArrayList(
                Donut.CHOCOLATE_FROSTED, Donut.STRAWBERRY_FROSTED, Donut.PUMPKIN_FROSTED,
                Donut.BOSTON_KREME, Donut.SUGAR_GLAZED, Donut.JELLY_FILLED);

        cakeTypeFlavors = FXCollections.observableArrayList(
                Donut.KEY_LIME, Donut.APPLE_CRUMB, Donut.RED_VELVET, Donut.DOUBLE_CHOCOLATE);

        holeTypeFlavors = FXCollections.observableArrayList(
                Donut.MAPLE, Donut.CINNAMON, Donut.POWDERED_SUGAR
        );

        donutTypes = FXCollections.observableArrayList(
                Donut.YEAST, Donut.CAKE, Donut.HOLE
        );

        quantityAmounts = FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 50).boxed().collect(Collectors.toList()));

        quantityComboBox.setValue(1);
        donutTypeComboBox.setItems(donutTypes);
        quantityComboBox.setItems(quantityAmounts);

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
}
