package com.example.rucafeandroid.model;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Objects;

/**
 * Class for Coffee.
 *
 * @author Aryan Patel, Rushi Patel
 */
public class Coffee extends MenuItem {

    /* Instance Variables */
    private String cupSize;
    private HashSet<String> addIns;

    /* Cup Sizes */
    public static final String SHORT = "Short";
    public static final String TALL = "Tall";
    public static final String GRANDE = "Grande";
    public static final String VENTI = "Venti";

    /* Coffee Prices by Cup Size */
    private static final Double COFFEE_PRICE_INCREMENT = 0.40;
    public static final Double SHORT_PRICE = 1.89;
    public static final Double TALL_PRICE = SHORT_PRICE + COFFEE_PRICE_INCREMENT;
    public static final Double GRANDE_PRICE = TALL_PRICE + COFFEE_PRICE_INCREMENT;
    public static final Double VENTI_PRICE = GRANDE_PRICE + COFFEE_PRICE_INCREMENT;
    public static final Double UNDETERMINED_PRICE = 0.0;

    /**
     * constructor of class.
     * @param quantity quantity of item.
     * @param cupSize size of cup.
     */
    public Coffee(int quantity, String cupSize) {
        super(quantity);
        this.cupSize = cupSize;
        this.addIns = new HashSet<>();
    }

    /**
     * overloaded constructor of class.
     *
     * @param quantity quantity of item.
     * @param cupSize size of cup.
     * @param addIns list of add ins.
     */
    public Coffee(int quantity, String cupSize, HashSet<String> addIns) {
        super(quantity);
        this.cupSize = cupSize;
        this.addIns = addIns;
    }

    /**
     * getter of cup size.
     *
     * @return cup size.
     */
    public String getCupSize() {
        return this.cupSize;
    }

    /**
     * getter of add ins.
     *
     * @return set of add ins.
     */
    public HashSet<String> getAddIns() {
        return this.addIns;
    }

    /**
     * method for getting price of coffee.
     *
     * @return price of coffee.
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coffee coffee = (Coffee) o;
        return cupSize.equals(coffee.cupSize) && addIns.equals(coffee.addIns);
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(cupSize, addIns);
    }

    /**
     * toString() method.
     *
     * @return details of coffee.
     */
    @NonNull
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
