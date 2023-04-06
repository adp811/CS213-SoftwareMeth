package com.example.rucafe.utilities;

import javafx.scene.control.Alert;

/**
 * This is a utility class to handle showing an informative prompt depending
 * on what action is being taken.
 *
 * @author Aryan Patel
 */
public class AlertBox {

    /**
     * Static method to show the alert prompt and wait for acknowledgement.
     *
     * @param type Alert.AlertType enum representing the type of alert to show.
     * @param title String which contains the title of the prompt.
     * @param header String which contains the header of the prompt.
     * @param content String which contains the content in the prompt.
     */
    public static void showAlert (Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
