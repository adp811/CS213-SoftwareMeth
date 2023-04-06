package com.example.rucafe.model;

import com.example.rucafe.model.MenuItem;

import java.util.ArrayList;

/**
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
     *
     * @param orderNumber
     */
    public Order(int orderNumber) {
        this.orderNumber = orderNumber;
        this.orderSubtotal = 0.00;
        this.orderSalesTax = 0.00;
        this.orderTotalAmount = 0.00;
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
     * @return
     */
    public double getOrderSubtotal() {
        return orderSubtotal;
    }

    /**
     *
     * @param orderSubtotal
     */
    public void setOrderSubtotal(double orderSubtotal) {
        this.orderSubtotal = orderSubtotal;
    }

    /**
     *
     * @return
     */
    public double getOrderSalesTax() {
        return orderSalesTax;
    }

    /**
     *
     * @param orderSalesTax
     */
    public void setOrderSalesTax(double orderSalesTax) {
        this.orderSalesTax = orderSalesTax;
    }

    /**
     *
     * @return
     */
    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    /**
     *
     * @param orderTotalAmount
     */
    public void setOrderTotalAmount(double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
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
