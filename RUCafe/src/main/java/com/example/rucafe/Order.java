package com.example.rucafe;

import java.util.ArrayList;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class Order {

    /* Instance Variables */
    private int orderNumber;
    private ArrayList<MenuItem> orderItems;

    /**
     *
     * @param orderNumber
     */
    Order(int orderNumber) {
        this.orderNumber = orderNumber;
        this.orderItems = new ArrayList<MenuItem>();
    }

    /**
     *
     * @return
     */
    public int getOrderNumber() {
        return this.orderNumber;
    }

    /**
     *
     * @param orderNumber
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     *
     * @param item
     */
    public void addToOrderItems (MenuItem item) {
        orderItems.add(item);
    }

    /**
     *
     * @param item
     */
    public void removeFromOrderItems (MenuItem item) {
        orderItems.remove(item);
    }

    /**
     *
     * @return
     */
    public ArrayList<MenuItem> getOrderItems() {
        return this.orderItems;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order number: ").append(orderNumber).append("\n");
        sb.append("Order items: ").append(orderItems.toString()).append("\n");
        return sb.toString();
    }
}