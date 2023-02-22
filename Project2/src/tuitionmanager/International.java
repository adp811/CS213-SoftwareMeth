package tuitionmanager;

/**
 *
 * @author Aryan Patel
 */
public class International extends NonResident {

    private boolean isStudyAbroad;

    private final double FULL_TIME_TUITION = 29737.0;
    private final double PART_TIME_TUITION_HOURLY = 966.0;

    private final double UNIV_FEE_FULL_TIME = 3268.0;
    private final double UNIV_FEE_HEALTH_INSURANCE = 2650.0;


    /**
     * needs comments
     *
     * @param profile
     * @param major
     * @param creditCompleted
     * @param isStudyAbroad
     */
    public International(Profile profile, Major major, int creditCompleted, boolean isStudyAbroad) {
        super(profile, major, creditCompleted);
        this.isStudyAbroad = isStudyAbroad;
    }

    /**
     * needs comments
     *
     * @return
     */
    public boolean getStudyAbroadStatus() {
        return this.isStudyAbroad;
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

        if (this.isStudyAbroad) {
            tuitionDue = UNIV_FEE_FULL_TIME + UNIV_FEE_HEALTH_INSURANCE;
        } else {
            if (creditsEnrolled > 16) {
                int exceedingCredits = creditsEnrolled - 16;
                double additionalFee = PART_TIME_TUITION_HOURLY * exceedingCredits;
                tuitionDue = FULL_TIME_TUITION + UNIV_FEE_FULL_TIME + UNIV_FEE_HEALTH_INSURANCE + additionalFee;
            } else {
                tuitionDue = FULL_TIME_TUITION + UNIV_FEE_FULL_TIME + UNIV_FEE_HEALTH_INSURANCE;
            }
        }

        return tuitionDue;
    }
}
