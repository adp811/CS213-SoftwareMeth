package com.example.rucafeandroid.model;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Class for Donut.
 *
 * @author Aryan Patel, Rushi Patel
 */
public class Donut extends MenuItem {

    /* Instance Variables */
    private String type;
    private String flavor;

    /* Donut Types */
    private static final String YEAST = "Yeast Donut";
    private static final String CAKE = "Cake Donut";
    private static final String HOLE = "Hole Donut";

    /* Donut Prices by Type */
    private static final double YEAST_PRICE = 1.59;
    private static final double CAKE_PRICE = 1.79;
    private static final double HOLE_PRICE = 0.39;
    private static final double UNDETERMINED_PRICE = 0.0;

    /**
     * constructor for initialize object.
     * @param quantity quantity of item.
     * @param type type of donut
     * @param flavor name of flavor
     */
    public Donut(int quantity, String type, String flavor) {
        super(quantity);
        this.type = type;
        this.flavor = flavor;
    }

    /**
     * getter of type.
     *
     * @return type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * getter of flavor.
     *
     * @return flavor.
     */
    public String getFlavor() {
        return this.flavor;
    }

    /**
     * method for price of donut.
     *
     * @return price.
     */
    @Override
    public double itemPrice() {
        String donutType = this.type;
        return switch (donutType) {
            case YEAST -> YEAST_PRICE;
            case CAKE -> CAKE_PRICE;
            case HOLE -> HOLE_PRICE;
            default -> UNDETERMINED_PRICE;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donut donut = (Donut) o;
        return type.equals(donut.type) && flavor.equals(donut.flavor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, flavor);
    }

    /**
     * toString() method.
     *
     * @return donut details.
     */
    @NonNull
    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return  "(Donut) "
                + super.toString()
                + " (Unit Price: $" + decimalFormat.format(itemPrice()) + ")"
                + " (Type: " + this.type + ")"
                + " (Flavor: " + this.flavor + ")";
    }
}
