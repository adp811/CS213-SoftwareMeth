package com.example.rucafeandroid.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Objects;

/**
 *  This is the class for the Order object. It is used to
 *  help keep track of all the store orders.
 *
 * @author Aryan Patel, Rushi Patel
 */
public class Order {

    /* Instance Variables */
    private int orderNumber;
    private ArrayList<MenuItem> orderItems;

    /**
     * Constructor for the Order class. Order number is passed
     * as a randomly generated ID integer.
     *
     * @param orderNumber int which contains the ID number of the order.
     */
    public Order(int orderNumber) {
        this.orderNumber = orderNumber;
        this.orderItems = new ArrayList<>();
    }

    /**
     * Getter method for the order number.
     *
     * @return int which is the order number.
     */
    public int getOrderNumber() {
        return this.orderNumber;
    }

    /**
     * Getter method for the order number.
     *
     * @return String which is the order number.
     */
    public String getOrderNumberString() {
        return String.valueOf(this.orderNumber);
    }

    /**
     * Getter method for the list of order items.
     *
     * @return ArrayList of MenuItems in the order.
     */
    public ArrayList<MenuItem> getOrderItems() {
        return this.orderItems;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orderNumber == order.orderNumber && orderItems.equals(order.orderItems);
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, orderItems);
    }

    /**
     * toString() method for the Order object.
     *
     * @return order information as a String.
     */
    @NonNull
    @Override
    public String toString() {
        return "Order number: " + orderNumber + "\n" +
                "Order items: " + orderItems.toString() + "\n";
    }
}
