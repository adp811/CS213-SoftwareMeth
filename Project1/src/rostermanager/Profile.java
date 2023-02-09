package rostermanager;

/**
 *
 * Needs Comments
 *
 * @author Aryan Patel
 */
public class Profile implements Comparable<Profile> {

    private String lname;
    private String fname;
    private Date   dob;

    public Profile(String lname, String fname, Date dob) {
        this.lname = lname;
        this.fname = fname;
        this.dob = dob;
    }

    private String getLastName() {
        return this.lname;
    }

    private String getFirstname() {
        return this.fname;
    }

    private Date getDateOfBirth() {
        return this.dob;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof Profile profileCompare)) {
            return false;
        }

        return (this.lname.equals(profileCompare.lname) &&
                this.fname.equals(profileCompare.fname) &&
                this.dob.equals(profileCompare.dob));
    }

    @Override
    public int compareTo(Profile profileCompare) {

        int compLast, compFirst;

        if ((compLast = this.lname.compareTo(profileCompare.lname)) != 0) {
            return compLast;
        }
        if ((compFirst = this.fname.compareTo(profileCompare.fname)) != 0) {
            return compFirst;
        }

        return this.dob.compareTo(profileCompare.dob);
    }

    @Override
    public String toString() {
        return this.fname + " " + this.lname + " " + this.dob.toString();
    }

    public static void main(String[] args) {
        System.out.println("TestBed Main Profile() Class");
    }

}
