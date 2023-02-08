package rostermanager;

public class Roster {

    private Student[] roster;
    private int size;

    private int find(Student student) {
        //search the given student in roster
        return -1;
    }

    private void grow() {
        // increase array cap by 4
    }

    public boolean add(Student student) {
        // add student to end of array
        return false;
    }

    public boolean remove(Student student) {
        // maintain the order after remove
        return false;
    }

    public boolean contains(Student student) {
        // if student is in roster
        return false;
    }

    public void print() {
        // print roster sorted by profiles
    }

    public void printBySchoolMajor() {
        // print roster sorted by school major
    }

    public void printByStanding() {
        // print roster sorted by standing
    }

}
