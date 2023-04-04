package com.example.rucafe;

import javafx.scene.control.Alert;

/**
 *
 * @author Aryan Patel
 */
public class AlertBox {

    /**
     *
     * @param type
     * @param title
     * @param header
     * @param content
     */
    public static void showAlert (Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
