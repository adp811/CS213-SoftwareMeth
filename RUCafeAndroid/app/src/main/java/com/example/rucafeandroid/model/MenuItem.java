package com.example.rucafeandroid.model;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Super class of menu item.
 *
 * @author Rushi Patel
 */
public abstract class MenuItem {
    /**
     * variable quantity.
     */
    private int quantity;

    /**
     * abstract method for getting price of item.
     *
     * @return price.
     */
    public abstract double itemPrice();

    /**
     * constructor of class.
     *
     * @param quantity quantity of item.
     */
    public MenuItem(int quantity) {
        this.quantity = quantity;
    }

    /**
     * getter of quantity.
     *
     * @return quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return quantity == menuItem.quantity;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

    /**
     * toString() method.
     *
     * @return menu item details.
     */
    @NonNull
    @Override
    public String toString() {
        return "(Quantity: " + this.quantity + ")";
    }
}
