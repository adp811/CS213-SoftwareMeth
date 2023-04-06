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
import java.util.LinkedHashMap;

/**
 * This is the Application class. The class is used to set the stage for the application
 * window in order to launch it with the ViewSwitcher utility class. New instances of the
 * current order and store orders are also initialized here.
 *
 * @author Aryan Patel, Rushi Patel
 */
public class RUCafeApplication extends Application {

    /* Instance Variables */
    private Order order = new Order(IDGenerator.generateRandomID(9));
    private LinkedHashMap<Integer, Order> storeOrders = new LinkedHashMap<Integer, Order>();

    /**
     * This method launches the application with a new scene and stage. The scene
     * and initial view is passed to the ViewSwitcher to set.
     *
     * @param stage
     * @throws IOException if page is not found, throw an exception.
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
     * Main method to launch the application.
     *
     * @param args no arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}