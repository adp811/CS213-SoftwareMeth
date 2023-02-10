package rostermanager;

/**
 *
 * Needs Comments, 
 *
 * @author Aryan Patel
 */
public class Roster {

    private Student[] roster;
    private int size;

    private boolean isEmpty() {
        return this.size == 0;
    }

    private void grow() {
        Student[] newRosterArray = new Student[this.size + 4];

        for (int i = 0; i < this.size; i++) {
            newRosterArray[i] = this.roster[i];
        }

        this.roster = newRosterArray;
    }

    public boolean contains(Student student) {
        for (int i = 0; i < this.size; i++) {
            if (this.roster[i].equals(student)) {
                return true;
            }
        }

        return false;
    }

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

    private int find(Student student) {
        //search the given student in roster
        return -1;
    }

    public boolean remove(Student student) {
        // maintain the order after remove
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
