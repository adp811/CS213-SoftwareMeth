package rostermanager;

import java.util.Scanner;

/**
 * Needs comments
 *
 * @author Aryan Patel
 */
public class RosterManager {

    /**
     *
     * @param inputMajor
     * @return
     */
    private Major determineMajor(String inputMajor) {
        for(Major major : Major.values()) {
            if(major.name().equals(inputMajor.toUpperCase())) {
                return major;
            }
        }
        return null;
    }

    /**
     *
     * @param major
     * @param creditsCompleted
     * @return
     */
    private boolean validateStudentMajor(String major, String creditsCompleted) {
        try {
            int credits = Integer.parseInt(creditsCompleted);
            if (credits < 0) {
                System.out.println("Credits completed invalid: cannot be negative!");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Credits completed invalid: not an integer!");
            return false;
        }

        if (determineMajor(major) == null) {
            System.out.println("Major code invalid: " + major);
            return false;
        }

        return true;
    }

    /**
     *
     * @return int
     */
    private boolean validateStudentDateOfBirth(String date) {
        Date dob = new Date(date), current = new Date();
        if (!dob.isValid()) {
            System.out.println("DOB invalid: " + dob.toString() + " not a valid calendar date!");
            return false;
        }
        if (dob.equals(current) || dob.compareTo(current) > 0) {
            System.out.println("DOB invalid: cannot be current or future date!");
            return false;
        }

        int age = current.getYear() - dob.getYear();
        if (current.getMonth() < dob.getMonth()) {
            age--;
        } else if (current.getMonth() == dob.getMonth() && current.getDay() < dob.getDay()) {
            age--;
        }

        if (age < 16) {
            System.out.println("DOB invalid: " + dob.toString() + " younger than 16 years old.");
            return false;
        }

        return true;
    }

    /**
     *
     * @param commandBody
     * @return
     */
    private Student createStudent(String[] commandBody) {
        String fname = commandBody[1], lname = commandBody[2], dob = commandBody[3];
        String major = commandBody[4], creditCompleted = commandBody[5];

        if (!validateStudentDateOfBirth(dob) || !validateStudentMajor(major, creditCompleted)) {
            return null;
        }

        Student newStudent;
        try {
            newStudent = new Student(
                    new Profile(lname, fname, new Date(dob)),
                    determineMajor(major),
                    Integer.parseInt(creditCompleted));

        } catch (Exception e) {
            System.out.println("New instance of Student cannot be created, please try again.");
            return null;
        }

        return newStudent;
    }

    /**
     *
     * @param commandBody
     * @param roster
     * @return
     */
    private Student getStudent(String[] commandBody, Roster roster) {
        Profile searchProfile = new Profile(commandBody[2], commandBody[1],
                new Date(commandBody[3]));

        for (int i = 0; i < roster.getRosterSize(); i++) {
            Profile profile = roster.getRoster()[i].getProfile();

            if (profile.equals(searchProfile)) {
                return roster.getRoster()[i];
            }
        }

        return null;
    }

    /**
     *
     * @param roster
     */
    private void printRoster(Roster roster) {
        for (int i = 0; i < roster.getRosterSize(); i++) {
            System.out.println(roster.getRoster()[i].toString());
        }
    }

    /**
     *
     * @param roster
     * @param school
     */
    private void printRoster(Roster roster, String school) {
        for(Major major : Major.values()) {
            if(major.getSchool().equalsIgnoreCase(school)) {
                System.out.println("* Students in " + school + " *");
                for (int i = 0; i < roster.getRosterSize(); i++) {
                    Student student = roster.getRoster()[i];

                    if(student.getMajor().getSchool().equalsIgnoreCase(school)){
                        System.out.println(student);
                    }
                }
                System.out.println("* end of list **");

                return;
            }
        }

        System.out.println("School doesn't exist: " + school);
    }

    /**
     *
     * @param commandBody
     * @param roster
     */
    private void executeCommandA(String[] commandBody, Roster roster) {
        if (commandBody.length != 6) {
            System.out.println("Incorrect number of arguments, please try again!");
            return;
        }

        Student student;
        if ((student = createStudent(commandBody)) == null) {
            return;
        }

        try {
            boolean status;
            if(!(status = roster.add(student))) {
                System.out.println(student.getProfile().toString() + " is already in the roster.");
            } else {
                System.out.println(student.getProfile().toString() + " added to the roster.");
            }

        } catch (Exception e) {
            System.out.println("ERROR: Student could not be added, please try again.");
        }
    }

    private void executeCommandR(String[] commandBody, Roster roster) {
        if (commandBody.length != 4) {
            System.out.println("Incorrect number of arguments, please try again!");
            return;
        }

        Student student;
        if((student = getStudent(commandBody, roster)) == null) {
            System.out.println(commandBody[1] + " " + commandBody[2] + " "
                    + new Date(commandBody[3]) + " is not in the roster.");
            return;
        }

        try {
            boolean status = roster.remove(student);
            if (status) {
                System.out.println(student.getProfile().toString() + " removed from the roster.");
            }

        } catch (Exception e) {
            System.out.println("ERROR: Student could not be removed, please try again.");
        }
    }

    /**
     *
     * @param commandBody
     * @param roster
     */
    private void executeCommandC(String[] commandBody, Roster roster) {
        if (commandBody.length != 5) {
            System.out.println("Incorrect number of arguments, please try again!");
            return;
        }

        Student student;
        if((student = getStudent(commandBody, roster)) == null) {
            System.out.println(commandBody[1] + " " + commandBody[2] + " "
                    + new Date(commandBody[3]) + " is not in the roster.");
            return;
        }

        Major major;
        if ((major = determineMajor(commandBody[4])) == null) {
            System.out.println("Major code invalid: " + commandBody[4]);
            return;
        }

        try {
            student.setMajor(major);
            System.out.println(student.getProfile().toString() + " major changed to " + major.name());

        } catch (Exception e) {
            System.out.println("ERROR: Student major could not be changed, please try again.");
        }
    }

    /**
     *
     * @param commandBody
     * @param roster
     */
    private void executeCommandL(String[] commandBody, Roster roster) {
        if (commandBody.length != 2) {
            System.out.println("Incorrect number of arguments, please try again!");
            return;
        }
        if (roster.getRosterSize() == 0) {
            System.out.println("Student roster is empty!");
            return;
        }

        roster.print();
        printRoster(roster, commandBody[1]);
    }

    /**
     *
     * @param roster
     */
    private void executeCommandP(Roster roster) {
        if (roster.getRosterSize() == 0) {
            System.out.println("Student roster is empty!");
        } else {
            System.out.println("* Student roster sorted by last name, first name, DOB **");
            roster.print();
            printRoster(roster);
            System.out.println("* end of roster **");
        }
    }

    /**
     *
     * @param roster
     */
    private void executeCommandPS(Roster roster) {
        if (roster.getRosterSize() == 0) {
            System.out.println("Student roster is empty!");
        } else {
            System.out.println("* Student roster sorted by standing **");
            roster.printByStanding();
            printRoster(roster);
            System.out.println("* end of roster **");
        }
    }

    /**
     *
     * @param roster
     */
    private void executeCommandPC(Roster roster) {
        if (roster.getRosterSize() == 0) {
            System.out.println("Student roster is empty!");
        } else {
            System.out.println("* Student roster sorted by school, major **");
            roster.printBySchoolMajor();
            printRoster(roster);
            System.out.println("* end of roster **");
        }
    }

    /**
     *
     * @param commandBody
     * @param roster
     */
    private void parseCommand (String[] commandBody, Roster roster) {
        String operation = commandBody[0];

        switch (operation) {
            case "A":
                executeCommandA(commandBody, roster);
                break;
            case "R":
                executeCommandR(commandBody, roster);
                break;
            case "P":
                executeCommandP(roster);
                break;
            case "PS":
                executeCommandPS(roster);
                break;
            case "PC":
                executeCommandPC(roster);
                break;
            case "L":
                executeCommandL(commandBody, roster);
                break;
            case "C":
                executeCommandC(commandBody, roster);
                break;
            case "":
                break;
            default:
                System.out.println(commandBody[0] + " is an invalid command!");
                break;
        }
    }

    /**
     *
     */
    public void run() {
        System.out.println("Roster Manager running...");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        Roster roster = new Roster();

        String input;
        while (!(input = scanner.nextLine()).equals("Q")) {
            String[] commandBody = input.split("\\s+");
            parseCommand(commandBody, roster);
        }

        scanner.close();
        System.out.println("Roster Manager terminated.");
    }

}
