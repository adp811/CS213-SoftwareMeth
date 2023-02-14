package rostermanager;

import java.util.Calendar;

/**
 * This is a class that creates a Date object given a String input or a Calendar instance.
 * Also contains functionality to validate a Date object.
 *
 * @author Aryan Patel
 */
public class Date implements Comparable<Date> {

    private int year;
    private int month;
    private int day;

    private static final int MIN_YEAR = 1;
    private static final int MIN_MONTH = 1, MAX_MONTH = 12;
    private static final int MIN_DAY_OM = 1, MAX_DAY_OM = 31;
    private static final int MAX_DAY_OM_30 = 30, MAX_DAY_OM_LY = 29;

    private static final int MONTH_OFFSET = 1;

    private static final int FEBRUARY = 2, APRIL = 4, JUNE = 6,
                             SEPTEMBER = 9, NOVEMBER = 11;

    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;

    /**
     * Constructs a Date object using an instance of the current date
     * retrieved from the Java Calendar library.
     * <p>
     * Note: An additional month is added to the current Calendar instance to
     * account for the zero base indexed representation of the months in Java's Calendar library
     */
    public Date() {
        Calendar current = Calendar.getInstance();
        current.add(Calendar.MONTH, MONTH_OFFSET);

        this.year = current.get(Calendar.YEAR);
        this.month = current.get(Calendar.MONTH);
        this.day = current.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Constructs a Date object from a String input that is split by '/' using
     * the Java String Library. The resulting split output contains the
     * deconstructed MM, DD, YYYY which is indexed into and stored.
     *
     * @param date String input which is a date in the format 'MM/DD/YYYY'
     */
    public Date(String date) {
        String[] dateSplit = date.split("/");

        this.month = Integer.parseInt(dateSplit[0]);
        this.day = Integer.parseInt(dateSplit[1]);
        this.year = Integer.parseInt(dateSplit[2]);
    }

    /**
     * This method gets the year stored in a Date object.
     *
     * @return int which contains the year
     */
    public int getYear() {
        return this.year;
    }

    /**
     * This method gets the month stored in a Date object.
     *
     * @return int which contains the month
     */
    public int getMonth() {
        return this.month;
    }

    /**
     *
     * This method gets the day stored in a Date object.
     *
     * @return int which contains the day
     */
    public int getDay() {
        return this.day;
    }

    /**
     * This method determines if an inputted year is a leap year or not based on the following steps:
     * <p>
     * Step 1: If the year is evenly divisible by 4, go to step 2. Otherwise, go to step 5.
     * Step 2: If the year is evenly divisible by 100, go to step 3. Otherwise, go to step 4.
     * Step 3: If the year is evenly divisible by 400, go to step 4. Otherwise, go to step 5.
     * Step 4: The year is a leap year.
     * Step 5: The year is not a leap year.
     *
     * @param year int input that is to be validated as a leap year or not
     * @return boolean which refers to whether the inputted year is a leap year or not
     */
    private boolean isLeapYear(int year) {
        if (year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                if (year % QUATERCENTENNIAL == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * This method determines if a Date object is a valid date or not.
     * <p>
     * The method makes use of standard conventions and knowledge to determine
     * if the Date object is valid or not.
     *
     * @return boolean which refers to whether the Date object is a valid date or not
     */
    public boolean isValid() {
        if (this.year < MIN_YEAR) {
            return false;
        }
        if (this.month > MAX_MONTH || this.month < MIN_MONTH) {
            return false;
        }
        if (this.day > MAX_DAY_OM || this.day < MIN_DAY_OM) {
            return false;
        }

        if (this.month == FEBRUARY && this.day == MAX_DAY_OM_LY && !(isLeapYear(this.year))) {
            return false;
        } else if (this.month == FEBRUARY && this.day > MAX_DAY_OM_LY) {
            return false;
        }

        return (this.day <= MAX_DAY_OM_30) || (this.month != APRIL && this.month != JUNE &&
                this.month != SEPTEMBER && this.month != NOVEMBER);
    }

    /**
     * This method determines if a Date object is equal to another given Object.
     * <p>
     * The process of checking equality involves first checking if the given Object
     * is the Date object itself. If not, it then checks if the given object is an
     * instance of the Date Class before it checks the equality of the month, day, and year values
     * between the two objects.
     *
     * @param o Object input that is to be checked if it is equal to another Date object
     * @return boolean which refers to whether the given Object is equal to another Date Object
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Date dateCompare)) {
            return false;
        }

        return (this.year == dateCompare.year && this.month == dateCompare.month && this.day == dateCompare.day);
    }

    /**
     * This method compares a Date object with another Date object.
     * <p>
     * The order of comparison is as follows: YYYY, MM, DD. The method returns an int value
     * which represents the relative position (BEFORE, AFTER, SAME), of one Date Object to the
     * other.
     *
     * @param date Date input that is to be compared
     * @return int which contains a value (< 0, > 0, or == 0) representing relative the order.
     */
    @Override
    public int compareTo(Date date) {
        if (date.year != this.year) {
            return (this.year - date.year);
        }
        if (date.month != this.month) {
            return (this.month - date.month);
        }

        return (this.day - date.day);
    }

    /**
     * This method returns the Date object as a string containing the year,
     * month, and day.
     *
     * @return String which represents a Date object in the format 'MM/DD/YYYY'
     */
    @Override
    public String toString(){
        return this.month + "/" + this.day + "/" + this.year;
    }

    /**
     * This testbed main() method tests the isValid() method using a combination
     * of valid and invalid dates. Test status and total test cases passed are shown
     * as output.
     *
     * @param args no arguments passed
     */
    public static void main(String[] args) {
        System.out.println("\nTesting isValid() method... \n");

        /* keep track of passed tests */
        int passedCount = 0, totalTests = 14;

        System.out.println("Invalid Dates: ");

        /* Test 1 - Month value is out of range (should be between 01 and 12) */
        System.out.print("Test 1: 13/30/2022   -> ");
        Date test1 = new Date("13/30/2022");
        if (test1.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 2 - February 29th only exists in leap years */
        System.out.print("Test 2: 2/29/1900    -> ");
        Date test2 = new Date("2/29/1900");
        if (test2.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 3 - Day value is out of range for January */
        System.out.print("Test 3: 01/32/2006   -> ");
        Date test3 = new Date("01/32/2006");
        if (test3.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 4 - Month value cannot be 0 */
        System.out.print("Test 4: 00/01/2002   -> ");
        Date test4 = new Date("00/01/2002");
        if (test4.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 5 - Day value is out of range for February */
        System.out.print("Test 5: 02/30/2002   -> ");
        Date test5 = new Date("02/30/2002");
        if (test5.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 6 - Day value is out of range for September */
        System.out.print("Test 6: 09/31/1990   -> ");
        Date test6 = new Date("09/31/1990");
        if (test6.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 7 - Day value cannot be 0) */
        System.out.print("Test 7: 04/00/2003   -> ");
        Date test7 = new Date("04/00/2003");
        if (test7.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 8 - Year value cannot be 0000 */
        System.out.print("Test 8: 1/15/0000    -> ");
        Date test8 = new Date("1/15/0000");
        if (test8.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        System.out.println("\nValid Dates: ");

        /* Test 9 - Valid Regular Date */
        System.out.print("Test 9: 01/22/2002   -> ");
        Date test9 = new Date("01/22/2002");
        if (!test9.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 10 - Valid Regular Date */
        System.out.print("Test 10: 2/28/1990   -> ");
        Date test10 = new Date("2/28/1990");
        if (!test10.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 11 - Valid Leap Year Date */
        System.out.print("Test 11: 02/29/2020  -> ");
        Date test11 = new Date("02/29/2020");
        if (!test11.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 12 - Valid Date (max 31 days in July) */
        System.out.print("Test 12: 7/31/2009   -> ");
        Date test12 = new Date("7/31/2009");
        if (!test12.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 13 - Valid Date (max 30 days in November) */
        System.out.print("Test 13: 11/30/2005  -> ");
        Date test13 = new Date("11/30/2005");
        if (!test13.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 14 - No argument constructor for Date class */
        System.out.print("Test 14: Date()      -> ");
        Date test14 = new Date();
        if (!test14.isValid()) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        System.out.println("\n" + passedCount + " out of " +
                totalTests + " test cases passed.");
    }
}
