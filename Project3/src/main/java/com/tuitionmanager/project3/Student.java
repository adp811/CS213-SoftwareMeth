package com.tuitionmanager.project3;

/**
 * This is a class that creates a Student object given an associated Profile object,
 * Major enum, and total credits completed.
 * <p>
 * This class uses the Profile class and Major enum to store information about a student's profile
 * and their academic records.
 *
 * @author Aryan Patel
 */
public abstract class Student implements Comparable<Student> {

    private Profile profile;
    private Major   major;
    private int     creditCompleted;

    private static final int MIN_SOPHOMORE = 30;
    private static final int MIN_JUNIOR = 60;
    private static final int MIN_SENIOR = 90;

    private static final int MAX_ENROLLMENT_CREDITS = 24;
    private static final int MIN_ENROLLMENT_CREDITS = 3;
    private static final int FULL_TIME_ENROLLMENT_CREDITS = 12;


    /**
     * Constructs a Student object given a Profile object, Major enum, and an int
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
     * This method gets the amount of credits completed by a Student object.
     *
     * @return int which contains the total number of credits completed
     */
    public int getCreditCompleted() {
        return this.creditCompleted;
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
     * This method sets a Student object's number of credits completed to
     * the given number of credits completed.
     *
     * @param creditCompleted int which contains the new amount of credits
     *                        completed
     */
    public void setCreditCompleted(int creditCompleted) {
        this.creditCompleted = creditCompleted;
    }

    /**
     * This method determines whether a Student object has a valid amount of credits
     * enrolled or not. It determines this based on the type of Student through it's
     * extending classes.
     *
     * @param creditEnrolled int which contains a student's amount of credits enrolled
     * @return boolean which represents whether the amount of credits enrolled by the
     *         student is valid or not.
     */
    public boolean isValid(int creditEnrolled) {
        if(this instanceof International internationalStudent) {
            if(internationalStudent.getStudyAbroadStatus()){
                return creditEnrolled <= FULL_TIME_ENROLLMENT_CREDITS
                        && creditEnrolled >= MIN_ENROLLMENT_CREDITS;
            } else {
                return creditEnrolled <= MAX_ENROLLMENT_CREDITS
                        && creditEnrolled >= FULL_TIME_ENROLLMENT_CREDITS;
            }
        } else {
            return creditEnrolled <= MAX_ENROLLMENT_CREDITS
                    && creditEnrolled >= MIN_ENROLLMENT_CREDITS;
        }
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
     * This is an abstract method that is required to be implemented by any class
     * that extends this Student class. It calculates the tuition due based on the
     * given credits enrolled and the type of student which is indicated by
     * any extending class of Student.
     *
     * @param creditsEnrolled int which contains the amount of credits enrolled by the student
     * @return double which contains the dollar amount representing the tuition due for
     *         a student.
     */
    public abstract double tuitionDue(int creditsEnrolled);

    /**
     * This is an abstract method that is required to be implemented by any class
     * that extends this Student class. It determines whether the class that is extended is
     * considered to be a resident student or not.
     *
     * @return boolean which represents whether the extending class is considered a
     *         resident student or not
     */
    public abstract boolean isResident();
}
