package tuitionmanager;

/**
 *
 * @author Aryan Patel
 */
public class EnrollStudent {

    private Profile profile;
    private int creditsEnrolled;

    /**
     * needs comments
     *
     * @param profile
     * @param creditsEnrolled
     */
    EnrollStudent(Profile profile, int creditsEnrolled) {
        this.profile = profile;
        this.creditsEnrolled = creditsEnrolled;

    }

    /**
     * needs comments
     *
     * @return
     */
    public int getCreditsEnrolled() {
        return this.creditsEnrolled;
    }

    /**
     * needs comments
     *
     * @param creditsEnrolled
     */
    public void setCreditsEnrolled(int creditsEnrolled) {
        this.creditsEnrolled = creditsEnrolled;
    }

    /**
     * needs comments
     *
     * @param o
     * @return
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
     * needs comments
     *
     * @return
     */
    @Override
    public String toString() {
        return this.profile.toString() + ": credits enrolled: " + this.creditsEnrolled;
    }
}
