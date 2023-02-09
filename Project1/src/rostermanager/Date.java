package rostermanager;

import java.util.Calendar;

/**
 *
 * Needs Comments
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

    public Date() {

        Calendar current = Calendar.getInstance();
        current.add(Calendar.MONTH, 1);

        this.year = current.get(Calendar.YEAR);
        this.month = current.get(Calendar.MONTH);
        this.day = current.get(Calendar.DAY_OF_MONTH);

    }

    public Date(String date) {

        String[] dateSplit = date.split("/");
        this.month = Integer.parseInt(dateSplit[0]);
        this.day = Integer.parseInt(dateSplit[1]);
        this.year = Integer.parseInt(dateSplit[2]);

    }

    private int getYear() {
        return this.year;
    }

    private int getMonth() {
        return this.month;
    }

    private int getDay() {
        return this.day;
    }

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

    @Override
    public String toString(){
        return this.month + "/" + this.day + "/" + this.year;
    }

    public static void main(String[] args) {
        System.out.println("TestBed Main Date() Class");
    }

}
