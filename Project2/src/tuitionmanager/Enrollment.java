package tuitionmanager;

/**
 * This is a class that maintains a resizing array of EnrollStudent objects
 * which represent an instance of a Enrollment object.
 * <p>
 * This class uses the EnrollStudent class to define an array of EnrollStudent
 * objects which will be in the Enrollment instance.
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
     * This method creates a new Enrollment object containing an array of EnrollStudent
     * objects and an int representing the count of EnrollStudent objects within
     * the array. This is a no argument constructor.
     *
     */
    Enrollment() {
        this.enrollStudents = new EnrollStudent[ARR_STARTING_CAPACITY];
        this.size = ENROLLMENT_STARTING_SIZE;
    }

    /**
     * This method takes care of resizing the array of EnrollStudent objects in Enrollment. The
     * capacity is increased by 4 in the new enrollStudents array. Contents from the old array
     * are copied over and the reference to the old array is discarded.
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
     * This method finds a given EnrollStudent in the enrollStudents array,
     * and returns the index of its position. If the EnrollStudent object is not in
     * the array, then NOT_FOUND is returned.
     *
     * @param enrollStudent object input that is the student which needs to be found
     * @return int which represents the index associated with the student position
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
     * This method gets the value of the size variable representing the size of
     * the enrollStudents array.
     *
     * @return int which contains the enrollStudent array size
     */
    public int getEnrollmentSize() {
        return this.size;
    }

    /**
     * This method returns the enrollStudent array associated with an Enrollment
     * object. This array contains objects of type EnrollStudent.
     *
     * @return array of type EnrollStudent containing EnrollStudent objects
     */
    public EnrollStudent[] getEnrollStudents() {
        return enrollStudents;
    }

    /**
     * This method checks if a given EnrollStudent objects is contained within
     * the enrollStudents array. This method utilizes the find() method and returns
     * a boolean indicating whether find() was successful or not.
     *
     * @param enrollStudent object input that is to be checked whether it is present
     *                      in the enrollStudent array or not
     * @return boolean true or false representing whether the given EnrollStudent
     *         is in the array or not
     */
    public boolean contains(EnrollStudent enrollStudent){
        return find(enrollStudent) != NOT_FOUND;
    }

    /**
     * This method takes care of adding a given EnrollStudent to the enrollStudents
     * array. It first checks if the student is already in the array before it adds the
     * student. If the array is at capacity, the grow() method is called to increase the
     * capacity.
     *
     * @param enrollStudent object input that is to be added into the enrollStudents array.
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
     * This method takes care of removing a given EnrollStudent object
     * from the enrollStudents array. First the index of removal is found and then
     * the last EnrollStudent object in the array is used to replace the removed
     * object.
     *
     * @param enrollStudent object input which is the EnrollStudent that needs to be
     *                      found and removed
     */
    public void remove(EnrollStudent enrollStudent){
        int index = find(enrollStudent);
        this.enrollStudents[index] = this.enrollStudents[this.size - 1];
        this.enrollStudents[this.size - 1] = null;

        this.size--;
    }

    /**
     * This method takes care of printing the enrollStudents array. It first checks
     * if the array is empty before proceeded to print each EnrollStudent line
     * by line.
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
