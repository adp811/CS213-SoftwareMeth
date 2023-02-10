package rostermanager;

import javax.sql.RowSet;
import java.sql.Struct;

/**
 *
 * Needs Comments, 
 *
 * @author Aryan Patel
 */
public class Roster {

    private Student[] roster;
    private int size;

    private static final int NOT_FOUND = -1;

    Roster() {
        this.roster = new Student[2];
        this.size = 0;
    }

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

    private void sortInPlace(String type) {
        switch (type) {
            case "profile":
                for (int i = 1; i < this.size; i++) {
                    Student studentCompare = this.roster[i];
                    int j = i - 1;
                    while (j >= 0 && this.roster[j].compareTo(studentCompare) > 0) {
                        this.roster[j + 1] = this.roster[j];
                        j--;
                    }
                    this.roster[j + 1] = studentCompare;
                }
                break;

            case "standing":
                for (int i = 1; i < this.size; i++) {
                    Student studentCompare = this.roster[i];
                    int j = i - 1;
                    while (j >= 0 && this.roster[j].getCreditCompleted() > studentCompare.getCreditCompleted()) {
                        this.roster[j + 1] = this.roster[j];
                        j--;
                    }
                    this.roster[j + 1] = studentCompare;
                }
                break;

            case "school_major":
                break;

            default:
                System.out.println("default");
                break;
        }
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
        for (int i = 0; i < this.size; i++) {
            if (this.roster[i].equals(student)) {
                return i;
            }
        }

        return NOT_FOUND;
    }

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

    public void print() {
        this.sortInPlace("school_major");

        for (int i = 0; i < size; i++) {
            System.out.println(this.roster[i].toString());
        }
    }

    public void printBySchoolMajor() {
        // print roster sorted by school major
    }

    public void printByStanding() {
        // print roster sorted by standing
    }

    public static void main(String[] args) {
        System.out.println("TestBed Main Roster() Class");

        Roster roster01 = new Roster();

        Student student01 = new Student(new Profile("Patel","Aryan", new Date("01/22/2002")),
                Major.ITI, 30);
        roster01.add(student01);

        Student student02 = new Student(new Profile("Shah","Raj", new Date("04/12/2001")),
                Major.EE, 60);
        roster01.add(student02);

        Student student03 = new Student(new Profile("Mehta","Tej", new Date("03/25/2000")),
                Major.MATH, 90);
        roster01.add(student03);

        Student student04 = new Student(new Profile("Patel","Amar", new Date("12/26/2001")),
                Major.BAIT, 105);
        roster01.add(student04);

        Student student05 = new Student(new Profile("Patel","Rushi", new Date("07/02/2002")),
                Major.CS, 75);
        roster01.add(student05);

        System.out.println(roster01.size);
        System.out.println(roster01.roster.length);
        roster01.print();

    }
}
