package rostermanager;

/**
 * This is a class that creates a Student object given an associated Profile object,
 * Major enum, and total credits completed.
 * <p>
 * This class uses the Profile class and Major enum to store information about a student's profile
 * and their academic records.
 *
 * @author Aryan Patel
 */
public class Student implements Comparable<Student> {

    private Profile profile;
    private Major   major;
    private int     creditCompleted;

    private static final int MIN_SOPHOMORE = 30;
    private static final int MIN_JUNIOR = 60;
    private static final int MIN_SENIOR = 90;

    /**
     * Constructs a Student object given a Profile object, Major enum, and a int
     * representing the credits completed.
     *
     * @param profile Profile object which contains the student's name and date of birth
     * @param major Major enum which contains the student's major and associated information
     * @param creditCompleted int which contains the student's credits completed
     */
    public Student(Profile profile, Major major, int creditCompleted) {
        this.profile = profile;
        this.major = major;
        this.creditCompleted = creditCompleted;
    }

    /**
     * This method gets a student's Profile from a Student object.
     *
     * @return Profile object which contains the student's information
     */
    public Profile getProfile() {
        return this.profile;
    }

    /**
     * This method gets a student's Major from a Student object.
     *
     * @return Major enum object which contains the student's major
     */
    public Major getMajor() {
        return this.major;
    }

    /**
     * This method gets a student object's class standing (FRESHMAN, SOPHOMORE, JUNIOR, SENIOR) based
     * on the number of credits they have completed. The class standing is determined these conditions:
     * <p>
     * Freshman, if credit completed is less than 30.
     * Sophomore, if credit completed is greater or equal to 30 and less than 60.
     * Junior, if credit completed is greater or equal to 60 and less than 90.
     * Senior, if credit completed is greater or equal to 90.
     *
     * @return String which contains the student's class standing (FRESHMAN, SOPHOMORE, JUNIOR, SENIOR).
     */
    public String getClassStanding() {
        if (this.creditCompleted < MIN_SOPHOMORE) {
            return "Freshman";
        } else if (creditCompleted < MIN_JUNIOR) {
            return "Sophomore";
        } else if (creditCompleted < MIN_SENIOR) {
            return "Junior";
        } else {
            return "Senior";
        }
    }

    /**
     * This method sets a Student object's Major to the given major
     *
     * @param major Major enum object which represents the new major
     */
    public void setMajor(Major major) {
        this.major = major;
    }

    /**
     * This method determines if a Student object is equal to another given Object.
     * <p>
     * The process of checking equality involves first checking if the given Object
     * is the Student object itself. If not, it then checks if the given object is an
     * instance of the Student Class before it checks the equality of the Profile, Major enum,
     * and creditCompleted values between the two objects.
     * <p>
     * Note: This method uses the Profile class' equals() method to compare two Student objects
     * because the Profile object attribute is used to compare two Student objects.
     *
     * @param o Object input that is to be checked if it is equal to another Student object
     * @return boolean which refers to whether the given Object is equal to another Student object
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if(!(o instanceof Student studentCompare)) {
            return false;
        }

        return this.profile.equals(studentCompare.profile);
    }

    /**
     * This method compares a Student object with another Student object.
     * <p>
     * This method uses the Profile class' compareTo() method to compare the two Profile objects
     * between the two Student objects.
     * <p>
     * The order of comparison is as follows: lname, fname, dob. This is because we are comparing only
     * the Profiles associated with both Student objects. The method returns an int value which
     * represents the relative position (BEFORE, AFTER, SAME), of one Student object to the
     * other. Details can be found in the Profile class compareTo() method.
     *
     * @param studentCompare Student object input that is to be compared
     * @return int which contains a value representing the relative order.
     */
    @Override
    public int compareTo(Student studentCompare) {
        return this.profile.compareTo(studentCompare.profile);
    }

    /**
     * This method returns the Student object as a String containing the student's Profile, Major, and
     * credits completed.
     * <p>
     * Note: This method uses the Profile and Major class' toString() method to display the Profile object
     * representing the student's information and the Major enum representing the student's major. The method also
     * utilizes the getClassStanding() method to display the student's class standing along with
     * their credits completed.
     *
     * @return String which represents a Student object
     */
    @Override
    public String toString() {
        return this.profile.toString() + " " + this.major.toString() + " credits completed: " +
                this.creditCompleted + " (" + this.getClassStanding() + ")";
    }

    /**
     * This testbed main() method tests the compareTo() method using different
     * possible combinations of equal fields. The order of comparison is as follows:
     * last name, first name, date of birth. Therefore, we have written tests in that
     * order. The compareTo() method returns an int value which represents the relative
     * position (BEFORE, AFTER, SAME), of one Student object to the other.
     * Test status and total test cases passed are shown as output.
     *
     * @param args no arguments passed
     */
    public static void main(String[] args) {
        System.out.println("\nTesting compareTo() method... \n\n");

        /* not compared in compareTo() method */
        Major major = Major.CS;
        int creditsCompleted = 90;

        /* keep track of passed tests */
        int passedCount = 0, totalTests = 11;

        /* init Student objects */
        Student studentA, studentB;

        System.out.println("last name not equal:");
        studentA = new Student(new Profile("Mehta", "Aryan", new Date("01/22/2002")),
                               major, creditsCompleted);
        studentB = new Student(new Profile("Patel", "Aryan", new Date("01/22/2002")),
                               major, creditsCompleted);

        /* Test 1 */
        System.out.print("Test 1: Compare (Mehta, Aryan 1/22/2002) to (Patel, Aryan 1/22/2002) -> ");
        if (!(studentA.compareTo(studentB) < 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 2 */
        System.out.print("Test 2: Compare (Patel, Aryan 1/22/2002) to (Mehta, Aryan 1/22/2002) -> ");
        if (!(studentB.compareTo(studentA) > 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }


        System.out.println("\n\nlast name equal, first name not equal:");
        studentA = new Student(new Profile("Patel", "Aryan", new Date("01/22/2002")),
                               major, creditsCompleted);
        studentB = new Student(new Profile("Patel", "Raj", new Date("01/22/2002")),
                               major, creditsCompleted);

        /* Test 3 */
        System.out.print("Test 3: Compare (Patel, Aryan 1/22/2002) to (Patel, Raj 1/22/2002) -> ");
        if (!(studentA.compareTo(studentB) < 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 4 */
        System.out.print("Test 4: Compare (Patel, Raj 1/22/2002) to (Patel, Aryan 1/22/2002) -> ");
        if (!(studentB.compareTo(studentA) > 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }


        System.out.println("\n\nlast name equal, first name equal, date of birth not equal (MM):");
        studentA = new Student(new Profile("Patel", "Aryan", new Date("01/22/2002")),
                major, creditsCompleted);
        studentB = new Student(new Profile("Patel", "Aryan", new Date("02/22/2002")),
                major, creditsCompleted);

        /* Test 5 */
        System.out.print("Test 5: Compare (Patel, Aryan 1/22/2002) to (Patel, Aryan 2/22/2002) -> ");
        if (!(studentA.compareTo(studentB) < 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 6 */
        System.out.print("Test 6: Compare (Patel, Aryan 2/22/2002) to (Patel, Aryan 1/22/2002) -> ");
        if (!(studentB.compareTo(studentA) > 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }


        System.out.println("\n\nlast name equal, first name equal, date of birth not equal (DD):");
        studentA = new Student(new Profile("Patel", "Aryan", new Date("01/22/2002")),
                major, creditsCompleted);
        studentB = new Student(new Profile("Patel", "Aryan", new Date("01/23/2002")),
                major, creditsCompleted);

        /* Test 7 */
        System.out.print("Test 7: Compare (Patel, Aryan 1/22/2002) to (Patel, Aryan 1/23/2002) -> ");
        if (!(studentA.compareTo(studentB) < 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 8 */
        System.out.print("Test 8: Compare (Patel, Aryan 1/23/2002) to (Patel, Aryan 1/22/2002) -> ");
        if (!(studentB.compareTo(studentA) > 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }


        System.out.println("\n\nlast name equal, first name equal, date of birth not equal (YYYY):");
        studentA = new Student(new Profile("Patel", "Aryan", new Date("01/22/2002")),
                major, creditsCompleted);
        studentB = new Student(new Profile("Patel", "Aryan", new Date("01/22/2003")),
                major, creditsCompleted);

        /* Test 9 */
        System.out.print("Test 9:  Compare (Patel, Aryan 1/22/2002) to (Patel, Aryan 1/22/2003)  -> ");
        if (!(studentA.compareTo(studentB) < 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }

        /* Test 10 */
        System.out.print("Test 10: Compare (Patel, Aryan 1/22/2003) to (Patel, Aryan 1/22/2002) -> ");
        if (!(studentB.compareTo(studentA) > 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }


        System.out.println("\n\nlast name equal, first name equal, date of birth equal:");
        studentA = new Student(new Profile("Patel", "Aryan", new Date("01/22/2002")),
                major, creditsCompleted);
        studentB = new Student(new Profile("Patel", "Aryan", new Date("01/22/2002")),
                major, creditsCompleted);

        /* Test 11 */
        System.out.print("Test 11: Compare (Patel, Aryan 1/22/2002) to (Patel, Aryan 1/22/2002) -> ");
        if (!(studentA.compareTo(studentB) == 0)) System.out.println("Failed");
        else { System.out.println("Passed"); passedCount++; }


        System.out.println("\n\n" + passedCount + " out of " +
                totalTests + " test cases passed.");
    }
}
