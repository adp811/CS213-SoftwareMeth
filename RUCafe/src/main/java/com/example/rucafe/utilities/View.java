package com.example.rucafe.utilities;

/**
 * This is the View Enum class to handle the file names. Designed to be
 * scale able for adding further views.
 *
 * @author Aryan Patel
 */
public enum View {

    MAIN_MENU("main-view.fxml"),
    ORDER_DONUT("order-donut-view.fxml"),
    ORDER_COFFEE("order-coffee-view.fxml"),
    SHOPPING_BASKET("shopping-basket-view.fxml"),
    STORE_ORDERS("store-orders-view.fxml");

    private String fileName;

    /**
     * This constructs a View Enum object with the given file name.
     *
     * @param fileName String which is the filename.
     */
    View(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Getter method for the file namm.
     *
     * @return String which is the file name.
     */
    public String getFileName() {
        return fileName;
    }
}
