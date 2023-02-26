package tuitionmanager;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This is the class for testing the International method tuitionDue() for both
 * cases is and not is study abroad.
 *
 * @author Rushi Patel
 */
public class TestInternational {

    /**
     * constant delta.
     */
    private static final double DELTA = 1e-15;

    /**
     * test method for is not study aboard.
     */
    @Test
    public void testIsNotStudyAbroad() {
        International iStudent = new International(
                new Profile("abc", "def", new Date("1/18/2002")),
                Major.CS, 96, false);
        assertEquals(35655, iStudent.tuitionDue(12), DELTA);
    }

    /**
     * test method for is study aboard.
     */
    @Test
    public void testIsStudyAbroad() {
        International iStudent = new International(
                new Profile("abc", "def", new Date("1/18/2002")),
                Major.CS, 96, true);
        assertEquals(5918, iStudent.tuitionDue(12), DELTA);
    }

}
