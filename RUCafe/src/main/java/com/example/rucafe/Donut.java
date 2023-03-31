package com.example.rucafe;

import java.text.DecimalFormat;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class Donut extends MenuItem {

    /* Instance Variables */
    private String type;
    private String flavor;

    /* Donut Types */
    protected static final String YEAST = "Yeast Donut";
    protected static final String CAKE = "Cake Donut";
    protected static final String HOLE = "Hole Donut";

    /* Available Donut Flavors */
    protected static final String CHOCOLATE_FROSTED = "Chocolate Frosted";
    protected static final String STRAWBERRY_FROSTED = "Strawberry Frosted";
    protected static final String PUMPKIN_FROSTED = "Pumpkin Frosted";
    protected static final String BOSTON_KREME = "Boston Kreme";
    protected static final String SUGAR_GLAZED = "Sugar Glazed";
    protected static final String JELLY_FILLED = "Jelly Filled";

    protected static final String KEY_LIME = "Key Lime";
    protected static final String APPLE_CRUMB = "Apple Crumb";
    protected static final String RED_VELVET = "Red Velvet";
    protected static final String DOUBLE_CHOCOLATE = "Double Chocolate";

    protected static final String MAPLE = "Maple";
    protected static final String CINNAMON = "Cinnamon";
    protected static final String POWDERED_SUGAR = "Powdered Sugar";

    /* Donut Prices by Type */
    private static final Double YEAST_PRICE = 1.59;
    private static final Double CAKE_PRICE = 1.79;
    private static final Double HOLE_PRICE = 0.39;
    private static final Double UNDETERMINED_PRICE = 0.0;

    /**
     *
     * @param type
     * @param flavor
     */
    Donut(int quantity, String type, String flavor) {
        super(quantity);
        this.type = type;
        this.flavor = flavor;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     *
     * @return
     */
    public String getFlavor() {
        return this.flavor;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @param flavor
     */
    public void setFlavor(String flavor){
        this.flavor = flavor;
    }

    /**
     *
     * @return
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
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Donut donutCompare)) {
            return false;
        }
        return (this.type.equals(donutCompare.type) &&
                this.flavor.equals(donutCompare.flavor));
    }

    /**
     *
     * @return
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
