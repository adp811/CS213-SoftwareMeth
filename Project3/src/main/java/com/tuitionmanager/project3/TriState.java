package com.tuitionmanager.project3;

/**
 * This is a subclass of NonResident with additional variable of state.
 *
 * @author Rushi Patel
 */
public class TriState extends NonResident {

    /**
     * variable and constants.
     */
    private String state;
    private static final double TUITION_FEE = 29737;
    private static final double UNIVERSITY_FEE = 3268;
    private static final double CREDIT_HOUR_FEE = 966;
    private static final double NY_DISCOUNT = 4000;
    private static final double CT_DISCOUNT = 5000;
    private static final double UNIVERSITY_FEE_DISC = 0.8;
    private static final int MIN_FT_CREDITS = 12;
    private static final int MIN_AF_CREDITS = 16;

    /**
     * constructor for creating the object of TriState Student.
     *
     * @param profile Profile object which contains the student's name and date
     *                of birth
     * @param major Major enum which contains the student's major and associated
     *              information
     * @param creditCompleted int which contains the student's credits completed
     * @param state state of the student.
     *
     */
    public TriState(Profile profile, Major major, int creditCompleted, String state) {
        super(profile, major, creditCompleted);
        this.state = state;
    }

    /**
     * needs comments
     *
     * @return state String
     */
    public String getState(){
        return this.state;
    }

    /**
     * method for calculating and return tuition fee due.
     *
     * @param creditsEnrolled number of credits enrolled
     * @return tuition fee
     */
    @Override
    public double tuitionDue(int creditsEnrolled) {
        double fee;
        if (creditsEnrolled >= MIN_FT_CREDITS) {
            if (creditsEnrolled > MIN_AF_CREDITS) {
                fee = TUITION_FEE + UNIVERSITY_FEE +
                        (CREDIT_HOUR_FEE * (creditsEnrolled - MIN_AF_CREDITS));
            } else {
                fee = TUITION_FEE + UNIVERSITY_FEE;
            }
            if (state.equalsIgnoreCase("NY")) {
                fee -= NY_DISCOUNT;
            } else if (state.equalsIgnoreCase("CT")) {
                fee -= CT_DISCOUNT;
            }
        } else {
            fee = (creditsEnrolled * CREDIT_HOUR_FEE) + (UNIVERSITY_FEE * UNIVERSITY_FEE_DISC);
        }

        return fee;
    }

    /**
     * toString method
     *
     * @return student details.
     */
    @Override
    public String toString() {
        return super.toString() + "(tri-state:" + this.state + ")";
    }
}
