package tuitionmanager;

/**
 *
 * @author Aryan Patel
 */
public class TriState extends NonResident {

    private String state;

    public TriState(Profile profile, Major major, int creditCompleted, String state) {
        super(profile, major, creditCompleted);
        this.state = state;
    }
}
