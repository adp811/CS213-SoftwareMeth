package com.example.rucafe;

import com.example.rucafe.model.Order;
import com.example.rucafe.utilities.IDGenerator;
import com.example.rucafe.utilities.View;
import com.example.rucafe.utilities.ViewSwitcher;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class RUCafeApplication extends Application {

    /* Instance Variables */
    private Order order = new Order(IDGenerator.generateRandomID(9));
    private LinkedHashMap<Integer, Order> storeOrders = new LinkedHashMap<Integer, Order>();

    /**
     *
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        var scene = new Scene(new Pane());

        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.MAIN_MENU, order, storeOrders);

        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}