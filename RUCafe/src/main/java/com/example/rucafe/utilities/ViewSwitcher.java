package com.example.rucafe.utilities;

import com.example.rucafe.controller.MainController;
import com.example.rucafe.controller.OrderCoffeeController;
import com.example.rucafe.controller.OrderDonutController;
import com.example.rucafe.controller.ShoppingBasketController;
import com.example.rucafe.controller.StoreOrdersController;
import com.example.rucafe.model.Order;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * This is a utility class to handle switching between different views of
 * the application while maintained the shared current order and store orders data.
 *
 * @author Aryan Patel
 */
public class ViewSwitcher {

    /* Instance Variables */
    private static Scene scene;

    /**
     * Setter method for the scene.
     *
     * @param scene Scene containing the new scene to set.
     */
    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    /**
     * This method takes care of switching the window view to a given view. The current
     * order and store orders are given as well since the data is shared between the views.
     *
     * @param view View containing the view to display in the window.
     * @param order Order containing the current shared order.
     * @param storeOrders LinkedHashMap containing the current shared store orders.
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
