package com.example.rucafe;

/**
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

    View(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
