package com.example.rucafe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private ObservableList<String> yeastTypeFlavors, cakeTypeFlavors, holeTypeFlavors, donutTypes,
                                   selectedFlavors;
    private ObservableList<Integer> quantityAmounts;

    /* FXML Elements */
    @FXML
    private ComboBox<String> donutTypeComboBox;

    @FXML
    private ComboBox<Integer> quantityComboBox;

    @FXML
    private ListView<String> availableFlavorsListView, selectedFlavorsListView;

    @FXML
    private Button addSelectionButton, removeSelectionButton, addToOrderButton;

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
    }

    /**
     *
     * @param donutType
     */
    private void setDonutTypeImage(String donutType) {
        String imagePath;

        switch (donutType){
            case Donut.YEAST -> imagePath = "images/yeast_donuts.jpg";
            case Donut.CAKE -> imagePath = "images/cake_donuts.jpg";
            case Donut.HOLE -> imagePath = "images/hole_donuts.jpg";
            default -> imagePath = "images/placeholder_donuts.png";
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
            AlertBox.showAlert(Alert.AlertType.INFORMATION,
                    "Selection Error", "Missing Donut Type!", "Please select a donut type before" +
                            " adding to donut selections."); return;
        }
        if ((selectedFlavor = availableFlavorsListView.getSelectionModel().getSelectedItem()) == null) {
            AlertBox.showAlert(Alert.AlertType.INFORMATION,
                    "Selection Error", "Missing Flavor Selection!", "Please select a flavor before" +
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
            AlertBox.showAlert(Alert.AlertType.INFORMATION,
                    "Remove Error", "Missing Flavor Selection!", "Please select an added flavor" +
                            " in order to remove donut selection."); return;
        }

        removeFromSelectedDonuts(selectedFlavor.replaceAll("\\([^\\)]*\\)\\s*", ""));
        updateSelectedDonuts();
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
