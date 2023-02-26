package tuitionmanager;

/**
 * This is a subclass of NonResident with additional attribute of isStudyAbroad.
 *
 * @author Rushi Patel
 */
public class International extends NonResident {

    /**
     * variable and constants.
     */
    private boolean isStudyAbroad;
    private static final double TUITION_FEE = 29737;
    private static final double UNIVERSITY_FEE = 3268;
    private static final double HEALTH_INSURANCE = 2650;
    private static final double CREDIT_HOUR_FEE = 966;

    /**
     * constructor for creating the object of International Student.
     *
     * @param profile Profile object which contains the student's name and date
     * of birth
     * @param major Major enum which contains the student's major and associated
     * information
     * @param creditCompleted int which contains the student's credits completed
     * @param isStudyAbroad boolean value either true of false.
     */
    public International(Profile profile, Major major, int creditCompleted, boolean isStudyAbroad) {
        super(profile, major, creditCompleted);
        this.isStudyAbroad = isStudyAbroad;
    }

    /**
     * method for returning either student is study abroad or not.
     *
     * @return true or false.
     */
    public boolean getStudyAbroadStatus() {
        return this.isStudyAbroad;
    }

    /**
     * method for calculating and return tuition fee due.
     *
     * @param creditsEnrolled number of credits enrolled
     * @return tuition fee
     */
    @Override
    public double tuitionDue(int creditsEnrolled) {
        if (this.isStudyAbroad) {
            return UNIVERSITY_FEE + HEALTH_INSURANCE;
        } else {
            if (creditsEnrolled > 16) {
                return TUITION_FEE + UNIVERSITY_FEE + HEALTH_INSURANCE +
                        (CREDIT_HOUR_FEE * (creditsEnrolled - 16));
            }
            return TUITION_FEE + UNIVERSITY_FEE + HEALTH_INSURANCE;
        }
    }

    /**
     * toString method
     *
     * @return details of student.
     */
    @Override
    public String toString() {
        if (this.isStudyAbroad) {
            return super.toString() + "(international:study abroad)";
        }

        return super.toString() + "(international)";
    }
}
