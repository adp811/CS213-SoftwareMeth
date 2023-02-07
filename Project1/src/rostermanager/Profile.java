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
        return false;
    }

    @Override
    public int compareTo(Profile o) {
        return 0;
    }

    @Override
    public String toString() {
        return this.fname + " " + this.lname + " " + this.dob.toString();
    }

}
