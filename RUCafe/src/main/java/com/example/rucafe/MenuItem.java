package com.example.rucafe;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public abstract class MenuItem {

    /* instance variables */
    private int quantity;

    /**
     *
     */
    MenuItem(int quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     */
    public abstract double itemPrice();

    /**
     *
     * @return
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     *
     * @param itemQuantity
     */
    public void setQuantity(int itemQuantity) {
        this.quantity = itemQuantity;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "(Quantity: " + this.quantity + ")";
    }
}
