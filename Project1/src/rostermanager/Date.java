package rostermanager;

/**
 * Needs Comments
 *
 * @author Aryan Patel
 */
public class Date implements Comparable<Date> {

    private int year;
    private int month;
    private int day;

    public Date() {
        /* create new object from current date */
    }

    public Date(String date) {
        /* deconstruct date into new object */
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

}
