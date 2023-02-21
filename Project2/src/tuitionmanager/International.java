package tuitionmanager;

/**
 *
 * @author Aryan Patel
 */
public class International extends NonResident {

    private boolean isStudyAbroad;

    public International(Profile profile, Major major, int creditCompleted, boolean isStudyAbroad) {
        super(profile, major, creditCompleted);
        this.isStudyAbroad = isStudyAbroad;
    }
}
