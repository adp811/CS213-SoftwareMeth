package com.example.rucafe.utilities;

import com.example.rucafe.controller.*;
import com.example.rucafe.model.Order;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Aryan Patel
 */
public class ViewSwitcher {

    /* instance variables */
    private static Scene scene;

    /**
     *
     * @param scene
     */
    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    /**
     *
     * @param view
     * @param order
     * @param storeOrders
     */
    public static void switchTo(View view, Order order, LinkedHashMap<Integer, Order> storeOrders) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    ViewSwitcher.class.getResource("/com/example/rucafe/" + view.getFileName())
            );

            Parent root = loader.load();
            Object controller = loader.getController();

            if (controller instanceof MainController) {
                ((MainController) controller).setOrders(order, storeOrders);
            } else if (controller instanceof OrderDonutController) {
                ((OrderDonutController) controller).setOrders(order, storeOrders);
            } else if (controller instanceof OrderCoffeeController) {
                ((OrderCoffeeController) controller).setOrders(order, storeOrders);
            } else if (controller instanceof ShoppingBasketController) {
                ((ShoppingBasketController) controller).setOrders(order, storeOrders);
            } else if (controller instanceof StoreOrdersController) {
                ((StoreOrdersController) controller).setOrders(order, storeOrders);
            }

            scene.setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
