package tuitionmanager;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This is the class for testing the Roster method add() for adding objects in
 * roster.
 *
 * @author Rushi Patel
 *
 */
public class TestRoster {

    /**
     * Test method1 for adding international student in roster, which return
     * true.
     */
    @Test
    public void testAddInternational1() {
        Roster roster1 = new Roster();
        Student iStudent1 = new International(
                new Profile("abc", "def", new Date("1/18/2002")),
                Major.CS, 96, false);
        assertTrue(roster1.add(iStudent1));
    }

    /**
     * Test method2 for adding international student in roster, which return
     * false.
     */
    @Test
    public void testAddInternational2() {
        Roster roster2 = new Roster();
        Student iStudent2 = new International(
                new Profile("abc", "def", new Date("1/18/2002")),
                Major.CS, 96, false);
        roster2.add(iStudent2);
        assertFalse(roster2.add(iStudent2));
    }

    /**
     * Test method1 for adding tristate student in roster, which return true.
     */
    @Test
    public void testAddTriState1() {
        Roster roster1 = new Roster();
        Student tStudent1 = new TriState(
                new Profile("aaa", "bbb", new Date("02/18/2002")),
                Major.EE, 78, "NY");
        assertTrue(roster1.add(tStudent1));
    }

    /**
     * Test method1 for adding tristate student in roster, which return false.
     */
    @Test
    public void testAddTriState2() {
        Roster roster2 = new Roster();
        Student tStudent2 = new TriState(
                new Profile("aaa", "bbb", new Date("02/18/2002")),
                Major.EE, 78, "NY");
        roster2.add(tStudent2);
        assertFalse(roster2.add(tStudent2));
    }

}
