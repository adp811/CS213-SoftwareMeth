package tuitionmanager;

/**
 *
 * @author Aryan Patel
 */
public class TriState extends NonResident {

    private String state;

    private final double FULL_TIME_TUITION = 29737.0;
    private final double PART_TIME_TUITION_HOURLY = 966.0;

    private final double UNIV_FEE_FULL_TIME = 3268.0;
    private final double UNIV_FEE_PART_TIME = 2614.4;

    private final double NY_DISCOUNT = 4000.0;
    private final double CT_DISCOUNT = 5000.0;


    /**
     * needs comments
     *
     * @param profile
     * @param major
     * @param creditCompleted
     * @param state
     */
    public TriState(Profile profile, Major major, int creditCompleted, String state) {
        super(profile, major, creditCompleted);
        this.state = state;
    }

    /**
     * needs comments
     *
     * @param creditsEnrolled
     * @return
     */
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
            if (this.state.equalsIgnoreCase("NY")) {
                tuitionDue = tuitionDue - NY_DISCOUNT;
            } else if (this.state.equalsIgnoreCase("CT")) {
                tuitionDue = tuitionDue - CT_DISCOUNT;
            }

        } else { // part-time non-resident
            tuitionDue = (creditsEnrolled * PART_TIME_TUITION_HOURLY) + UNIV_FEE_PART_TIME;
        }

        return tuitionDue;
    }
}
