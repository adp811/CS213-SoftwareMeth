package com.example.rucafe.utilities;

import java.util.Random;

/**
 * This is utility class to handle generating a random integer ID number of
 * a specified length. This class uses the Java Random library for the seed.
 *
 * @author Aryan Patel
 */
public class IDGenerator {

    /**
     * Static method to generate a random integer ID number given a specified length.
     *
     * @param length int which contains the specified length of the ID number.
     * @return int which is the generated ID number.
     */
    public static int generateRandomID(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
