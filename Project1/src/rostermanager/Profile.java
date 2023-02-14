package rostermanager;

/**
 * This is a class that creates a Profile object given a student's last name,
 * first name, and date of birth. The date of birth is stored as a Date object within this
 * class.
 * <p>
 * This class is used to store a student's last name, first name, and date of
 * within the Student class.
 *
 * @author Aryan Patel
 */
public class Profile implements Comparable<Profile> {

    private String lname;
    private String fname;
    private Date   dob;

    /**
     * Constructs a Profile object given two Strings and a Date object.
     *
     * @param lname String input which is the student's last name
     * @param fname String input which is the student's first name
     * @param dob Date object input which contains the students date of birth
     */
    public Profile(String lname, String fname, Date dob) {
        this.lname = lname;
        this.fname = fname;
        this.dob = dob;
    }

    /**
     * This method gets the student's last name from a Profile object
     *
     * @return String which contains the last name
     */
    public String getLastName() {
        return this.lname;
    }

    /**
     * This method gets the student's first name from a Profile object
     *
     * @return String which contains the first name
     */
    public String getFirstname() {
        return this.fname;
    }

    /**
     * This method gets the student's date of birth as a Date object from a Profile object
     *
     * @return Date object which represents the date of birth
     */
    public Date getDateOfBirth() {
        return this.dob;
    }

    /**
     * This method determines if a Profile object is equal to another given Object.
     * <p>
     * The process of checking equality involves first checking if the given Object
     * is the Profile object itself. If not, it then checks if the given object is an
     * instance of the Profile Class before it checks the equality of the last name, first name,
     * and date of birth values between the two objects.
     *
     * @param o Object input that is to be checked if it is equal to another Profile object
     * @return boolean which refers to whether the given Object is equal to another Profile Object
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Profile profileCompare)) {
            return false;
        }

        return (this.lname.equalsIgnoreCase(profileCompare.lname) &&
                this.fname.equalsIgnoreCase(profileCompare.fname) &&
                this.dob.equals(profileCompare.dob));
    }

    /**
     * This method compares a Profile object with another Profile object.
     * <p>
     * The order of comparison is as follows: lname, fname, dob. The method returns an int value
     * which represents the relative position (BEFORE, AFTER, SAME), of one Profile Object to the
     * other.
     * <p>
     * Note: This method uses the Date class compareTo() method to compare the two Date objects
     * between the two Profiles
     *
     * @param profileCompare Profile object input that is to be compared
     * @return int which contains a value representing the relative order.
     */
    @Override
    public int compareTo(Profile profileCompare) {
        int compLast, compFirst;

        if ((compLast = this.lname.compareToIgnoreCase(profileCompare.lname)) != 0) {
            return compLast;
        }
        if ((compFirst = this.fname.compareToIgnoreCase(profileCompare.fname)) != 0) {
            return compFirst;
        }

        return this.dob.compareTo(profileCompare.dob);
    }

    /**
     * This method returns the Profile object as a String containing the student's first name,
     * last name, and date of birth.
     * <p>
     * Note: This method uses the Date class' toString() method to display the Date object representing
     * the student's date of birth.
     *
     * @return String which represents a Profile object
     */
    @Override
    public String toString() {
        return this.fname + " " + this.lname + " " + this.dob.toString();
    }
}
