package tuitionmanager;

/**
 *
 * This is a class that creates an EnrollStudent object given an associated Profile object,
 * total credits enrolled.
 * <p>
 * This class uses the Profile class to store information about a student's profile.
 *
 * @author Aryan Patel
 */
public class EnrollStudent {

    private Profile profile;
    private int creditsEnrolled;

    /**
     * Constructs a EnrollStudent object given a Profile object and an int
     * representing the credits enrolled by the student.
     *
     * @param profile Profile object which contains the student's name and date of birth
     * @param creditsEnrolled int which contains the student's current credits enrolled
     */
    EnrollStudent(Profile profile, int creditsEnrolled) {
        this.profile = profile;
        this.creditsEnrolled = creditsEnrolled;

    }

    /**
     * This method gets the value of the EnrollStudent's current credits enrolled.
     *
     * @return int which contains the student's current credits enrolled
     */
    public int getCreditsEnrolled() {
        return this.creditsEnrolled;
    }

    /**
     * This method gets the Profile associated with an EnrollStudent object.
     *
     * @return Profile object which contains information about a EnrollStudent object
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * This method takes care of setting an EnrollStudent object's total number
     * of credits enrolled.
     *
     * @param creditsEnrolled int which contains the amount of credits enrolled
     */
    public void setCreditsEnrolled(int creditsEnrolled) {
        this.creditsEnrolled = creditsEnrolled;
    }

    /**
     * This method determines if a EnrollStudent object is equal to another given Object.
     * <p>
     * The process of checking equality involves first checking if the given Object
     * is the EnrollStudent object itself. If not, it then checks if the given object is an
     * instance of the EnrollStudent Class before it checks the equality of the Profiles
     * between the two objects.
     * <p>
     * Note: This method uses the Profile class' equals() method to compare two
     * EnrollStudent objects because the Profile object attribute is used to compare two
     * EnrollStudent objects.
     *
     * @param o Object input that is to be checked if it is equal to another EnrollStudent object
     * @return boolean which refers to whether the given Object is equal to another
     *         EnrollStudent object
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if(!(o instanceof EnrollStudent enrollStudentCompare)) {
            return false;
        }

        return this.profile.equals(enrollStudentCompare.profile);
    }

    /**
     * This method returns the EnrollStudent object as a String containing the
     * student's Profile and credits enrolled.
     *
     * @return String which represents an EnrollStudent object
     */
    @Override
    public String toString() {
        return this.profile.toString() + ": credits enrolled: " + this.creditsEnrolled;
    }
}
