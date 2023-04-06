package com.example.rucafe.model;

import java.util.ArrayList;

/**
 *  This is the class for the Order object. It is used to
 *  help keep track of all the store orders.
 *
 * @author Aryan Patel, Rushi Patel
 */
public class Order {

    /* Instance Variables */
    private int orderNumber;
    private double orderSubtotal;
    private double orderSalesTax;
    private double orderTotalAmount;
    private ArrayList<MenuItem> orderItems;

    /**
     * Constructor for the Order class. Order number is passed
     * as a randomly generated ID integer.
     *
     * @param orderNumber int which contains the ID number of the order.
     */
    public Order(int orderNumber) {
        this.orderNumber = orderNumber;
        this.orderSubtotal = 0.00;
        this.orderSalesTax = 0.00;
        this.orderTotalAmount = 0.00;
        this.orderItems = new ArrayList<MenuItem>();
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
     * Getter method for the order subtotal.
     *
     * @return double which is the order subtotal.
     */
    public double getOrderSubtotal() {
        return orderSubtotal;
    }

    /**
     * Setter method for the order subtotal.
     *
     * @param orderSubtotal double which contains the new order subtotal.
     */
    public void setOrderSubtotal(double orderSubtotal) {
        this.orderSubtotal = orderSubtotal;
    }

    /**
     * Getter method for the order sales tax.
     *
     * @return double which is the order sales tax.
     */
    public double getOrderSalesTax() {
        return orderSalesTax;
    }

    /**
     * Setter method for the order sales tax.
     *
     * @param orderSalesTax double which contains the new order sales tax.
     */
    public void setOrderSalesTax(double orderSalesTax) {
        this.orderSalesTax = orderSalesTax;
    }

    /**
     * Getter method for the order total.
     *
     * @return double which is the order total.
     */
    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    /**
     * Setter method for the order total.
     *
     * @param orderTotalAmount double which contains the new order total.
     */
    public void setOrderTotalAmount(double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    /**
     * Adds a new MenuItem to the orderItems array.
     *
     * @param item MenuItem to be added into the array.
     */
    public void addToOrderItems (MenuItem item) {
        orderItems.add(item);
    }

    /**
     *  Removes a MenuItem from the orderItems array at the given index.
     *
     * @param index int which contains the index of the item to remove.
     */
    public void removeFromOrderItems (int index) {
        orderItems.remove(index);
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
     * toString() method for the Order object.
     *
     * @return order information as a String.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order number: ").append(orderNumber).append("\n");
        sb.append("Order items: ").append(orderItems.toString()).append("\n");
        return sb.toString();
    }
}
