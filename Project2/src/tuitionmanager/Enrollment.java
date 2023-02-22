package tuitionmanager;

/**
 * needs comments
 *
 * @author Aryan Patel
 */
public class Enrollment {

    private EnrollStudent[] enrollStudents;
    private int size;

    private static final int ENROLLMENT_STARTING_SIZE = 0;
    private static final int ARR_STARTING_CAPACITY = 2;
    private static final int ARR_INCREASE_CAPACITY_BY = 4;

    private static final int NOT_FOUND = -1;

    /**
     * needs comments
     *
     */
    Enrollment() {
        this.enrollStudents = new EnrollStudent[ARR_STARTING_CAPACITY];
        this.size = ENROLLMENT_STARTING_SIZE;
    }

    /**
     * needs comments
     *
     */
    private void grow() {
        EnrollStudent[] newEnrollmentArray = new EnrollStudent[this.size + ARR_INCREASE_CAPACITY_BY];
        for (int i = 0; i < this.size; i++) {
            newEnrollmentArray[i] = this.enrollStudents[i];
        }

        this.enrollStudents = newEnrollmentArray;
    }

    /**
     * needs comments
     *
     * @param enrollStudent
     * @return
     */
    private int find(EnrollStudent enrollStudent) {
        for (int i = 0; i < this.size; i++) {
            if (this.enrollStudents[i].equals(enrollStudent)) {
                return i;
            }
        }

        return NOT_FOUND;
    }

    /**
     * needs comments
     *
     * @return
     */
    public int getEnrollmentSize() {
        return this.size;
    }

    /**
     * needs comments
     *
     * @param enrollStudent
     * @return
     */
    public boolean contains(EnrollStudent enrollStudent){
        return find(enrollStudent) != NOT_FOUND;
    }

    /**
     * needs comments
     *
     * @param enrollStudent
     */
    public void add(EnrollStudent enrollStudent){
        if (this.contains(enrollStudent)) {
            int index = find(enrollStudent);
            enrollStudents[index].setCreditsEnrolled(enrollStudent.getCreditsEnrolled());
            return;
        }

        if (this.size == this.enrollStudents.length) {
            this.grow();
        }

        this.enrollStudents[this.size++] = enrollStudent;
    }

    /**
     * needs comments
     *
     * @param enrollStudent
     */
    public void remove(EnrollStudent enrollStudent){
        if (!this.contains(enrollStudent) || this.size == 0) {
            return; // not found, empty
        }

        int index = find(enrollStudent);
        this.enrollStudents[index] = this.enrollStudents[this.size - 1];
        this.enrollStudents[this.size - 1] = null;

        this.size--;
    }

    /**
     * needs comments
     *
     */
    public void print() {
        if (this.size == 0) {
            System.out.println("Enrollment is empty!");
            return;
        }

        for (int i = 0; i < this.size; i++) {
            System.out.println(enrollStudents[i]);
        }
    }
}
