package com.example.rucafe;

import java.text.DecimalFormat;
import java.util.HashSet;

/**
 *
 * @author Aryan Patel, Rushi Patel
 */
public class Coffee extends MenuItem {

    /* Instance Variables */
    private String cupSize;
    private HashSet<String> addIns;

    /* Cup Sizes */
    protected static final String SHORT = "Short";
    protected static final String TALL = "Tall";
    protected static final String GRANDE = "Grande";
    protected static final String VENTI = "Venti";

    /* Coffee Add Ins */
    protected static final String SWEET_CREAM = "Sweet Cream";
    protected static final String FRENCH_VANILLA = "French Vanilla";
    protected static final String IRISH_CREAM = "Irish Cream";
    protected static final String CARAMEL = "Caramel";
    protected static final String MOCHA = "Mocha";

    /* Coffee Prices by Cup Size */
    private static final Double COFFEE_PRICE_INCREMENT = 0.40;
    private static final Double SHORT_PRICE = 1.89;
    private static final Double TALL_PRICE = SHORT_PRICE + COFFEE_PRICE_INCREMENT;
    private static final Double GRANDE_PRICE = TALL_PRICE + COFFEE_PRICE_INCREMENT;
    private static final Double VENTI_PRICE = GRANDE_PRICE + COFFEE_PRICE_INCREMENT;
    private static final Double UNDETERMINED_PRICE = 0.0;

    /* Coffee Add In Price */
    private static final Double SINGLE_ADD_IN_PRICE = 0.30;

    /**
     *
     * @param cupSize
     */
    Coffee(int quantity, String cupSize) {
        super(quantity);
        this.cupSize = cupSize;
        this.addIns = new HashSet<String>();
    }

    /**
     *
     * @return
     */
    public String getCupSize() {
        return this.cupSize;
    }

    /**
     *
     */
    public void setCupSize(String cupSize) {
        this.cupSize = cupSize;
    }

    /**
     *
     * @param item
     */
    public void addAddIn(String item) {
        this.addIns.add(item);
    }

    /**
     *
     * @param item
     */
    public void removeAddIn(String item) {
        this.addIns.remove(item);
    }

    /**
     *
     * @return
     */
    public HashSet<String> getAddIns() {
        return this.addIns;
    }

    /**
     *
     * @return
     */
    @Override
    public double itemPrice() {
        double price = UNDETERMINED_PRICE;

        String coffeeSize = this.cupSize;
        switch(coffeeSize){
            case SHORT -> price += SHORT_PRICE;
            case TALL -> price += TALL_PRICE;
            case GRANDE -> price += GRANDE_PRICE;
            case VENTI -> price += VENTI_PRICE;
        }

        int totalAddIns = this.addIns.size();
        price += (0.30 * totalAddIns);

        return price;
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
        if (!(o instanceof Coffee coffeeCompare)) {
            return false;
        }
        return (this.cupSize.equals(coffeeCompare.cupSize) &&
                this.addIns.equals(coffeeCompare.addIns));
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        return "(Black Coffee) "
                + super.toString()
                + " (Unit Price: $" + decimalFormat.format(itemPrice()) + ")"
                + " (Cup Size: " + this.cupSize + ")"
                + " (Add Ins: " + addIns.toString() + ")";
    }
}
