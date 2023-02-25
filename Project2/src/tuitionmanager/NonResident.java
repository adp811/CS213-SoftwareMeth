package tuitionmanager;

/**
 *
 * @author Aryan Patel
 */
public class NonResident extends Student {

    private static final double FULL_TIME_TUITION = 29737.0;
    private static final double PART_TIME_TUITION_HOURLY = 966.0;

    private static final double UNIV_FEE_FULL_TIME = 3268.0;
    private static final double UNIV_FEE_PART_TIME = 2614.4;

    private static final int FULL_TIME_ENROLLMENT_CREDITS = 12;
    private static final int ADDITION_FEE_ENROLLMENT_CREDITS = 16;


    /**
     * needs comments
     *
     * @param profile
     * @param major
     * @param creditCompleted
     */
    public NonResident(Profile profile, Major major, int creditCompleted) {
        super(profile, major, creditCompleted);
    }

    /**
     * needs comments
     *
     * @param creditsEnrolled
     * @return
     */
    @Override
    public double tuitionDue(int creditsEnrolled) {
        double tuitionDue;

        if (creditsEnrolled >= FULL_TIME_ENROLLMENT_CREDITS) {
            if (creditsEnrolled > ADDITION_FEE_ENROLLMENT_CREDITS) {
                int exceedingCredits = creditsEnrolled - ADDITION_FEE_ENROLLMENT_CREDITS;
                double additionalFee = PART_TIME_TUITION_HOURLY * exceedingCredits;
                tuitionDue = FULL_TIME_TUITION + UNIV_FEE_FULL_TIME + additionalFee;
            } else {
                tuitionDue = FULL_TIME_TUITION + UNIV_FEE_FULL_TIME;
            }

        } else {
            tuitionDue = (creditsEnrolled * PART_TIME_TUITION_HOURLY) + UNIV_FEE_PART_TIME;
        }

        return tuitionDue;
    }

    /**
     * needs comments
     *
     * @return
     */
    @Override
    public boolean isResident() {
        return false;
    }

    /**
     * needs comments
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getProfile().toString() + " " + this.getMajor().toString() + " credits completed: " +
                this.getCreditCompleted() + " (" + this.getClassStanding() + ")(non-resident)";
    }
}
