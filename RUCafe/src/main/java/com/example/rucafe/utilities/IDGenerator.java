package com.example.rucafe.utilities;

import java.util.Random;

/**
 *
 * @author Aryan Patel
 */
public class IDGenerator {

    /**
     *
     * @param length
     * @return
     */
    public static int generateRandomID(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
