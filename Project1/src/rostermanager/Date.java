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

    private static final int JANUARY = 1, FEBRUARY = 2, MARCH = 3, APRIL = 4, MAY = 5, JUNE = 6,
            JULY = 7, AUGUST = 8, SEPTEMBER = 9, OCTOBER = 10, NOVEMBER = 11, DECEMBER = 12;

    /**
     * Constructs a Date object using an instance of the current date
     * retrieved from the Java Calendar library.
     * <p>
     * Note: An additional month is added to the current Calendar instance to
     * account for the zero base indexed representation of the months in Java's Calendar library
     */
    public Date() {
        Calendar current = Calendar.getInstance();
        current.add(Calendar.MONTH, 1);

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
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) {
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

        if (this.month == FEBRUARY && this.day == 29 && !(isLeapYear(this.year))) {
            return false;
        } else if (this.month == FEBRUARY && this.day > 29) {
            return false;
        }

        return (this.day <= 30) || (this.month != APRIL && this.month != JUNE &&
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
     * Unit Tests Here
     * @param args no arguments passed
     */
    public static void main(String[] args) {
        System.out.println("TestBed Main Date() Class");
    }

}
