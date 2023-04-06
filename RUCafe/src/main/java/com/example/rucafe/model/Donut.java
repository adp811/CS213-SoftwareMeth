package com.example.rucafe.model;

import java.text.DecimalFormat;

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
    public static final String YEAST = "Yeast Donut";
    public static final String CAKE = "Cake Donut";
    public static final String HOLE = "Hole Donut";

    /* Available Donut Flavors */
    public static final String CHOCOLATE_FROSTED = "Chocolate Frosted";
    public static final String STRAWBERRY_FROSTED = "Strawberry Frosted";
    public static final String PUMPKIN_FROSTED = "Pumpkin Frosted";
    public static final String BOSTON_KREME = "Boston Kreme";
    public static final String SUGAR_GLAZED = "Sugar Glazed";
    public static final String JELLY_FILLED = "Jelly Filled";

    public static final String KEY_LIME = "Key Lime";
    public static final String APPLE_CRUMB = "Apple Crumb";
    public static final String RED_VELVET = "Red Velvet";
    public static final String DOUBLE_CHOCOLATE = "Double Chocolate";

    public static final String MAPLE = "Maple";
    public static final String CINNAMON = "Cinnamon";
    public static final String POWDERED_SUGAR = "Powdered Sugar";

    /* Donut Prices by Type */
    public static final Double YEAST_PRICE = 1.59;
    public static final Double CAKE_PRICE = 1.79;
    public static final Double HOLE_PRICE = 0.39;
    public static final Double UNDETERMINED_PRICE = 0.0;

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

    /**
     * toString() method.
     *
     * @return donut details.
     */
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
