package rostermanager;

/**
 *
 * Needs Comments
 *
 * @author Aryan Patel
 */
public class Student implements Comparable<Student> {

    private Profile profile;
    private Major   major;
    private int     creditCompleted;

    public Student(Profile profile, Major major, int creditCompleted) {
        this.profile = profile;
        this.major = major;
        this.creditCompleted = creditCompleted;
    }

    private Profile getProfile() {
        return this.profile;
    }

    private Major getMajor() {
        return this.major;
    }

    private int getCreditCompleted() {
        return this.creditCompleted;
    }

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

    @Override
    public int compareTo(Student studentCompare) {
        return this.profile.compareTo(studentCompare.profile);
    }

    @Override
    public String toString() {
        return this.profile.toString() + " " + this.major + " " + this.creditCompleted;
    }

    public static void main(String[] args) {
        System.out.println("TestBed Main Student() Class");
    }
}
