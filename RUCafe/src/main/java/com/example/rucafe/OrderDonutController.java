package com.example.rucafe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private ObservableList<Integer> quantityAmounts;
    private ObservableList<String> yeastTypeFlavors, cakeTypeFlavors, holeTypeFlavors, donutTypes;

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
     * @param donutType
     */
    public void setDonutTypeImage(String donutType) {
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
     */
    @FXML
    public void initialize() {

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
