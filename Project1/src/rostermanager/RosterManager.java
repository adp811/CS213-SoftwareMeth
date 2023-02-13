package rostermanager;

import java.util.Scanner;

/**
 * This class is the main user interface class which processes the line
 * commands entered on the console and displays the results on the console as output. An
 * instance of this class can process a single line command, or a sequence of line commands.
 * <p>
 * "Q" command must be used to terminate this program normally.
 *
 * @author Aryan Patel
 */
public class RosterManager {

    /**
     * This method returns a Major enum object associated with the inputted major code
     * that is a String. The method loops through the existing values in the Major enum and
     * finds a value that matches the input major. If a value is found then it is returned, else
     * null is returned.
     *
     * @param inputMajor String which contains a major code, case-insensitive ("CS", "Cs", "cs")
     * @return Major enum object which represents the inputted major code
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
     * This method attempts to validate the inputted major and creditsCompleted Strings. First, we
     * check if the value entered for creditsCompleted is a non-negative integer. If it is, we continue
     * else we return false. Next we use the determineMajor() method to check if the inputted
     * major code is valid and exists. We return true if the major is valid and false otherwise.
     *
     * @param major String which contains a major code, case-insensitive ("CS", "Cs", "cs")
     * @param creditsCompleted String which contains the value of credits completed
     * @return boolean which represents whether the method parameters are valid or not
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
     * This method attempts to validate a date of birth entered as a String input. First we
     * create a new Date object using the String date input and check if it is a
     * valid calendar date. Next we check to make sure this date is not the current date or a
     * future date. Lastly, we check if the age associated with the birthdate is older than 16
     * years old. If all of these cases are true, the date entered is validated.
     *
     * @return boolean which represents whether the birthdate is valid or not
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
     * This method takes in a String array which represents the argument body of an
     * entered "A" command, and tries to create a new Student object if the argument body
     * is valid. We use the validateStudentDateOfBirth() and validateStudentMajor methods to
     * first check if the birthdate, major, and credits completed arguments are valid. If
     * they are, we proceed to create a new Student object using the argument body. If
     * not, we return null.
     *
     * @param commandBody String array representing an "A" command argument body
     *                    ("A Aryan Patel 1/22/2002 CS 90")
     * @return Student object created from the given body arguments
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
     * This method takes in a String array which represents the argument body of an
     * entered "R" or "C" command, and returns a Student object associated with the
     * arguments if it is found in the roster array. First we create a new profile instance
     * using the body arguments representing the first and last names and birthdate. Next we
     * search through the roster array to find a matching profile associated with a student. If
     * one is found, we return the associated Student object and null otherwise.
     *
     * @param commandBody String array representing an "R" or "C" command argument body
     *                    ("R Aryan Patel 1/22/2002") or ("C Aryan Patel 1/22/2002 BAIT")
     * @param roster Roster object which contains the roster array we are searching through
     * @return Student object that is associated with the profile we are searching for
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
     * This method takes care of printing the entire roster array associated with the
     * inputted Roster object.
     *
     * @param roster Roster object which contains the roster array we want to print
     */
    private void printRoster(Roster roster) {
        for (int i = 0; i < roster.getRosterSize(); i++) {
            System.out.println(roster.getRoster()[i].toString());
        }
    }

    /**
     * This is an overloaded printRoster() method which uses an extra String parameter,
     * school, to print only the students in the roster array that are associated with
     * the given school. We first loop through the Major enum values to check if the given
     * school actually exists. If it does, we proceed to loop through the roster array and
     * print only the students that are enrolled in the given school.
     *
     * @param roster Roster object which contains the roster array we want to filter
     *               through and print
     * @param school String which contains the school name that we want use as a filter,
     *               case-insensitive ("SAS", "SaS", "sas")
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
     * This method is used to run the entire project. We first indicate to the
     * user that the program has started running, and then we create a new instance of
     * Scanner and Roster. Next we enter a loop that takes input at the command line in order
     * to interact with the program. The program continues to take commands until "Q" is entered,
     * which terminates the entire program with a message. This method processes single line
     * commands as well as a sequence of line commands.
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
