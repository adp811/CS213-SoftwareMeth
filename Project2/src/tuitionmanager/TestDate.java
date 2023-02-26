package tuitionmanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * This is the class for testing the date method isValid().
 *
 * @author Rushi Patel
 */
public class TestDate {

    /**
     * Test method1 for invalid date
     */
    @Test
    public void invalidTest1_daysInFeb_notLeapYear() {
        assertFalse(new Date("2/29/2003").isValid());
    }

    /**
     * Test method2 for invalid date
     */
    @Test
    public void invalidTest2_daysInApril_outOfRange() {
        assertFalse(new Date("4/31/2003").isValid());
    }

    /**
     * Test method3 for invalid date
     */
    @Test
    public void invalidTest3_monthValue_outOfRangeMax() {
        assertFalse(new Date("13/31/2003").isValid());
    }

    /**
     * Test method4 for invalid date
     */
    @Test
    public void invalidTest4_daysInMarch_outOfRange() {
        assertFalse(new Date("3/32/2003").isValid());
    }

    /**
     * Test method5 for invalid date
     */
    @Test
    public void invalidTest5_monthValue_outOfRangeMin() {
        assertFalse(new Date("-1/31/2003").isValid());
    }

    /**
     * Test method1 for valid date
     */
    @Test
    public void validTest1_normalDate() {
        assertTrue(new Date("01/22/2002").isValid());
    }

    /**
     * Test method2 for valid date
     */
    @Test
    public void validTest2_isLeapYear() {
        assertTrue(new Date("2/29/2000").isValid());
    }

}
