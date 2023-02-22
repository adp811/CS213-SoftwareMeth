package tuitionmanager;

/**
 *
 * @author Aryan Patel
 */
public class NonResident extends Student {

    private final double FULL_TIME_TUITION = 29737.0;
    private final double PART_TIME_TUITION_HOURLY = 966.0;

    private final double UNIV_FEE_FULL_TIME = 3268.0;
    private final double UNIV_FEE_PART_TIME = 2614.4;

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

        if (creditsEnrolled >= 12) { // full-time non-resident
            if (creditsEnrolled > 16) {
                int exceedingCredits = creditsEnrolled - 16;
                double additionalFee = PART_TIME_TUITION_HOURLY * exceedingCredits;
                tuitionDue = FULL_TIME_TUITION + UNIV_FEE_FULL_TIME + additionalFee;
            } else {
                tuitionDue = FULL_TIME_TUITION + UNIV_FEE_FULL_TIME;
            }

        } else { // part-time non-resident
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
