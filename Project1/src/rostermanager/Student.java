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

    public Profile getProfile() {
        return this.profile;
    }

    public Major getMajor() {
        return this.major;
    }

    public String getClassStanding() {
        if (this.creditCompleted < 30) {
            return "Freshman";
        } else if (creditCompleted < 60) {
            return "Sophomore";
        } else if (creditCompleted < 90) {
            return "Junior";
        } else {
            return "Senior";
        }
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
        return this.profile.toString() + " " + this.major.toString() + " credits completed: " +
                this.creditCompleted + " (" + this.getClassStanding() + ")";
    }

    public static void main(String[] args) {
        System.out.println("TestBed Main Student() Class");
    }
}
