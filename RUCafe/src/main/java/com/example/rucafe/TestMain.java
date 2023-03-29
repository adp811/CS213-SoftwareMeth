package com.example.rucafe;

import java.util.ArrayList;

public class TestMain {
    public static void main (String[] args) {
        Coffee coffee1 = new Coffee(1, "Venti");

        coffee1.addAddIn(Coffee.IRISH_CREAM);
        coffee1.addAddIn(Coffee.MOCHA);
        coffee1.addAddIn(Coffee.CARAMEL);
        coffee1.addAddIn(Coffee.CARAMEL);

        Donut donut1 = new Donut(2, Donut.YEAST, Donut.APPLE_CRUMB);
        Donut donut2 = new Donut(4, Donut.CAKE, Donut.BOSTON_KREME);

        ArrayList<MenuItem> menu = new ArrayList<MenuItem>();

        menu.add(coffee1);
        menu.add(donut1);
        menu.add(donut2);

        for (MenuItem item: menu) {
            System.out.println(item);
        }
    }
}
