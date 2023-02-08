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

    public boolean isValid(){
        /* check if date is valid */
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int compareTo(Date o) {
        return 0;
    }

    @Override
    public String toString(){
        return this.month + "/" + this.day + "/" + this.year;
    }

    public static void main(String[] args) {
        System.out.println("TestBed Main Date() Class");

        Date date01 = new Date("01/22/2002");
        System.out.println(date01.toString());

        /* Note: handle one digit days and months when printing */
        Date date02 = new Date();
        System.out.println((date02));

    }

}
