package tuitionmanager;

/**
 * This is a class that maintains a resizing array of Student objects
 * which represents and instance of a Roster object.
 * <p>
 * This class uses the Student class to define an array of Student objects which
 * will be in the Roster instance.
 *
 * @author Aryan Patel
 */
public class Roster {

    private Student[] roster;
    private int size;

    private static final int ROSTER_STARTING_SIZE = 0;
    private static final int ARR_STARTING_CAPACITY = 2;
    private static final int ARR_INCREASE_CAPACITY_BY = 4;

    private static final int NOT_FOUND = -1;

    /**
     * Constructs a new Roster object which stores an array, roster, that contains
     * Student objects. The size of the array is initialized to 2, and the size variable is set to 0,
     * representing the total Student objects in the roster so far.
     *
     */
    Roster() {
        this.roster = new Student[ARR_STARTING_CAPACITY];
        this.size = ROSTER_STARTING_SIZE;
    }

    /**
     * This method takes care of resizing the array of Student objects in a Roster. The capacity
     * is increased by 4 in the new roster array. Contents from the old array are copied over and
     * the reference to the old array is discarded.
     *
     */
    private void grow() {
        Student[] newRosterArray = new Student[this.size + ARR_INCREASE_CAPACITY_BY];
        for (int i = 0; i < this.size; i++) {
            newRosterArray[i] = this.roster[i];
        }

        this.roster = newRosterArray;
    }

    /**
     * This helper method takes care of sorting the roster array of Student objects by different
     * comparators. The method uses and in-place insertion sort in order to sort the array in
     * ascending order by either class standing, major and school, and the default order which
     * is sorted by a student's Profile. A switch statement is used to alternate between the attributes being compared.
     * <p>
     * Note: When comparing by Profile, the method uses the order of the attributes as follows: lname, fname, dob. When
     * comparing by school and major, the methods uses the order of the school name first, and then the Major name.
     *
     * @param type String which contains the filter type to sort by ("standing", "school_major"), passing in
     *             anything else yields the default filter type
     */
    private void sortInPlace(String type) {
        for (int i = 1; i < this.size; i++) {
            Student studentCompare = this.roster[i];
            int j = i - 1;
            switch (type) {
                case "standing" -> {
                    while (j >= 0 && this.roster[j].getClassStanding().compareTo(studentCompare.getClassStanding()) > 0) {
                        this.roster[j + 1] = this.roster[j];
                        j--;
                    }
                }
                case "school_major" -> {
                    while (j >= 0 && (this.roster[j].getMajor().getSchool().compareTo(studentCompare.getMajor().getSchool()) > 0
                            || (this.roster[j].getMajor().getSchool().compareTo(studentCompare.getMajor().getSchool()) == 0 &&
                                this.roster[j].getMajor().name().compareTo(studentCompare.getMajor().name()) > 0))) {
                        this.roster[j + 1] = this.roster[j];
                        j--;
                    }
                }
                default -> {
                    while (j >= 0 && this.roster[j].compareTo(studentCompare) > 0) {
                        this.roster[j + 1] = this.roster[j];
                        j--;
                    }
                }
            }

            this.roster[j + 1] = studentCompare;
        }
    }

    /**
     * This method gets the size of the roster array in a Roster object.
     *
     * @return int which contains the roster array size
     */
    public int getRosterSize() {
        return this.size;
    }

    /**
     * This method gets the roster array from a Roster object
     *
     * @return array of type Student containing Student objects
     */
    public Student[] getRoster() {
        return this.roster;
    }

    /**
     * This method finds a given Student in the Roster array, and returns the index of its position. If
     * the Student object is not in the array, then NOT_FOUND is returned.
     *
     * @param student Student object input that is the student which needs to be found
     * @return int which represents the consent value associated with NOT_FOUND
     */
    private int find(Student student) {
        for (int i = 0; i < this.size; i++) {
            if (this.roster[i].equals(student)) {
                return i;
            }
        }

        return NOT_FOUND;
    }

    /**
     * This method check if the given Student object is present in the roster array. Returning
     * a boolean value indicating whether the item is present. The method uses a linear search.
     *
     * @param student Student object input that is to be checked whether it is present in the roster array
     * @return boolean which represents whether the given Student is contained in the roster array in Roster
     */
    public boolean contains(Student student) {
        for (int i = 0; i < this.size; i++) {
            if (this.roster[i].equals(student)) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method adds a given Student object into the Roster array if it is not already present. The method
     * uses the contains() method to check if the given Student is in the roster array. If not, it proceeds to add
     * the given Student object at the end of the array. If the array is full, we utilize the grow()
     * method to resize the array before inserting. The method returns a boolean to indication whether
     * the addition of the Student object was successful or not.
     *
     * @param student Student object input that is to be added into the Roster array.
     * @return boolean which indicated whether the addition was successfully or not.
     */
    public boolean add(Student student) {
        if (this.contains(student)) {
            return false;
        }
        if (this.size == this.roster.length) {
            this.grow();
        }

        this.roster[this.size++] = student;

        return true;
    }

    /**
     * This method finds and removes a given Student from the Roster array. The method
     * returns a boolean indicating whether the object was found and removed successfully or
     * not. The method first uses find() to check if the student is in the array. It returns if
     * not found or continues to remove the student object from array if found. The method maintains the
     * order of the Roster array after removal.
     *
     * @param student Student object input which is the student that needs to be found and removed
     * @return boolean which indicated whether the operation to find and remove was successfull or not.
     */
    public boolean remove(Student student) {
        int removeIndex;

        if ((removeIndex = this.find(student)) == NOT_FOUND) {
            return false;
        }

        for (int i = removeIndex; i < this.size - 1; i++) {
            this.roster[i] = this.roster[i + 1];
        }

        this.size--;

        return true;
    }

    /**
     * This method takes care of sorting the Roster array using the student's Profile
     * to compare, preparing the array to be printed.
     *
     */
    public void print() {
        this.sortInPlace("profile");
    }

    /**
     * This method takes care of sorting the Roster array using the student's school and major
     * to compare, preparing the array to be printed.
     *
     */
    public void printBySchoolMajor() {
        this.sortInPlace("school_major");
    }

    /**
     * This method takes care of sorting the Roster array using the student's standing
     * to compare, preparing the array to be printed.
     *
     */
    public void printByStanding() {
        this.sortInPlace("standing");
    }
}
