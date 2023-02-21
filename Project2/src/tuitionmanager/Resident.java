package tuitionmanager;

/**
 *
 * @author Aryan Patel
 */
public class Resident extends Student {

    private int scholarship;

    public Resident(Profile profile, Major major, int creditCompleted, int scholarship) {
        super(profile, major, creditCompleted);
        this.scholarship = scholarship;
    }

    @Override
    public double tuitionDue(int creditsEnrolled) {
        return 0;
    }

    @Override
    public boolean isResident() {
        return false;
    }
}
