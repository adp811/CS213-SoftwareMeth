package tuitionmanager;

/**
 * This is a subclass of Student.
 *
 * @author Rushi Patel
 */
public class NonResident extends Student {

    /**
     * constants
     */
    private static final double TUITION_FEE = 29737;
    private static final double UNIVERSITY_FEE = 3268;
    private static final double CREDIT_HOUR_FEE = 966;
    private static final double UNIVERSITY_FEE_DISC = 0.8;
    private static final int MIN_FT_CREDITS = 12;
    private static final int MIN_AF_CREDITS = 16;


    /**
     * constructor for creating the object of NonResident Student.
     *
     * @param profile Profile object which contains the student's name and date
     *                of birth
     * @param major Major enum which contains the student's major and associated
     *              information
     * @param creditCompleted int which contains the student's credits completed
     */
    public NonResident(Profile profile, Major major, int creditCompleted) {
        super(profile, major, creditCompleted);
    }

    /**
     * method for returning that either student is resident or not.
     *
     * @return true or false.
     */
    @Override
    public boolean isResident() {
        return false;
    }

    /**
     * method for calculating and return tuition fee due.
     *
     * @param creditsEnrolled number of credits enrolled
     * @return tuition fee
     */
    @Override
    public double tuitionDue(int creditsEnrolled) {
        if (creditsEnrolled >= MIN_FT_CREDITS) {
            if (creditsEnrolled > MIN_AF_CREDITS) {
                return TUITION_FEE + UNIVERSITY_FEE +
                        (CREDIT_HOUR_FEE * (creditsEnrolled - MIN_AF_CREDITS));
            } else {
                return TUITION_FEE + UNIVERSITY_FEE;
            }
        }
        return (creditsEnrolled * CREDIT_HOUR_FEE) + (UNIVERSITY_FEE * UNIVERSITY_FEE_DISC);
    }

    /**
     * toString method
     *
     * @return student details.
     */
    @Override
    public String toString() {
        return super.toString() + "(non-resident)";
    }

}
