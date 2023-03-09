package com.tuitionmanager.project3;

/**
 * This is a subclass of Student with additional variable of scholarship.
 *
 * @author Rushi Patel
 */
public class Resident extends Student {

    /**
     * variable and constants.
     */
    private int scholarship;
    private static final double TUITION_FEE = 12536;
    private static final double UNIVERSITY_FEE = 3268;
    private static final double CREDIT_HOUR_FEE = 404;
    private static final double UNIVERSITY_FEE_DISC = 0.8;
    private static final int MIN_FT_CREDITS = 12;
    private static final int MIN_AF_CREDITS = 16;

    /**
     * constructor for creating the object of Resident Student.
     *
     * @param profile Profile object which contains the student's name and date
     *                of birth
     * @param major Major enum which contains the student's major and associated
     *              information
     * @param creditCompleted int which contains the student's credits completed
     */
    public Resident(Profile profile, Major major, int creditCompleted, int scholarship) {
        super(profile, major, creditCompleted);
        this.scholarship = scholarship;
    }

    /**
     * method for awarding scholarship to a student.
     *
     * @param scholarship amount of scholarship
     */
    public void setScholarship(int scholarship) {
        this.scholarship = scholarship;
    }

    /**
     * method for returning that either student is resident or not.
     *
     * @return true or false
     */
    @Override
    public boolean isResident() {
        return true;
    }

    /**
     * method for calculating and return tuition fee due.
     *
     * @param creditsEnrolled number of credits enrolled
     * @return tuition fee
     */
    @Override
    public double tuitionDue(int creditsEnrolled) {
        if (creditsEnrolled >= MIN_FT_CREDITS) {
            if (creditsEnrolled > MIN_AF_CREDITS) {
                return TUITION_FEE + UNIVERSITY_FEE +
                        (CREDIT_HOUR_FEE * (creditsEnrolled - MIN_AF_CREDITS))
                        - this.scholarship;
            } else {
                return TUITION_FEE + UNIVERSITY_FEE - this.scholarship;
            }
        }
        return (creditsEnrolled * CREDIT_HOUR_FEE) + (UNIVERSITY_FEE * UNIVERSITY_FEE_DISC);
    }

    /**
     * toString method
     *
     * @return student details.
     */
    public String toString() {
        return super.toString() + "(resident)";
    }

}
