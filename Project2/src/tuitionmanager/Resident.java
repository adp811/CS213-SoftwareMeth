package tuitionmanager;

/**
 *
 * @author Aryan Patel
 */
public class Resident extends Student {

    private int scholarship;

    private final double FULL_TIME_TUITION = 12536.0;
    private final double PART_TIME_TUITION_HOURLY = 404.0;

    private final double UNIV_FEE_FULL_TIME = 3268.0;
    private final double UNIV_FEE_PART_TIME = 2614.4;

    /**
     * needs comments
     *
     * @param profile
     * @param major
     * @param creditCompleted
     * @param scholarship
     */
    public Resident(Profile profile, Major major, int creditCompleted, int scholarship) {
        super(profile, major, creditCompleted);
        this.scholarship = scholarship;
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

        if (creditsEnrolled >= 12) { // full-time resident
            if (creditsEnrolled > 16) {
                int exceedingCredits = creditsEnrolled - 16;
                double additionalFee = PART_TIME_TUITION_HOURLY * exceedingCredits;
                tuitionDue = FULL_TIME_TUITION + UNIV_FEE_FULL_TIME + additionalFee - this.scholarship;
            } else {
                tuitionDue = FULL_TIME_TUITION + UNIV_FEE_FULL_TIME - this.scholarship;
            }
        } else { // part-time resident
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
        return true;
    }
}
