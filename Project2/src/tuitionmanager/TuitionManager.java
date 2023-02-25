package tuitionmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
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
public class TuitionManager {

    private static final int MIN_AGE = 16;
    private static final int MIN_CREDITS = 0;

    private static final int A_COMMAND_L = 6;
    private static final int A_COMMAND_L_EX = 7;
    private static final int LS_COMMAND_L = 2;
    private static final int R_COMMAND_L = 4;
    private static final int L_COMMAND_L = 2;
    private static final int C_COMMAND_L = 5;
    private static final int E_COMMAND_L = 5;
    private static final int D_COMMAND_L = 4;
    private static final int MIN_COMMAND_L = 4;

    private static final int MIN_ROSTER_SIZE = 0;
    private static final int MIN_ENROLLMENT_SIZE = 0;

    private static final int MAX_SCHOLARSHIP_AMT = 10000, MIN_SCHOLARSHIP_AMT = 0;

    private static final int GRAD_ELIGIBLE_CREDITS = 120;
    private static final int MIN_SCHOLARSHIP_CREDITS = 12;
    private static final int DUMMY_VALUE_CREDITS = 15;


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
            if (credits < MIN_CREDITS) {
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
            System.out.println("DOB invalid: " + dob + " not a valid calendar date!");
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

        if (age < MIN_AGE) {
            System.out.println("DOB invalid: " + dob + " younger than 16 years old.");
            return false;
        }

        return true;
    }

    /**
     * needs comments
     *
     * @param student
     * @param creditsEnrolled
     * @return
     */
    private boolean validateEnrollmentCredits (Student student, String creditsEnrolled) {
        try {
            int credits = Integer.parseInt(creditsEnrolled);
            if (credits < MIN_CREDITS) {
                System.out.println("Credits enrolled cannot be negative.");
                return false;
            }
            boolean isValidAmount = student.isValid(credits);
            if (isValidAmount) return true;

        } catch (NumberFormatException e) {
            System.out.println("Credits enrolled is not an integer.");
            return false;
        }

        String status;
        if (student instanceof International) {
            if (((International) student).getStudyAbroadStatus()) {
                status = "(International studentstudy abroad)";
            } else {
                status = "(International student)";
            }
        } else if (student instanceof Resident) {
            status = "(Resident)";
        } else {
            status = "(Non-Resident)";
        }

        System.out.println(status + " " + creditsEnrolled + ": invalid credit hours.");
        return false;
    }

    /**
     * needs comments
     *
     * @param commandBody
     * @param roster
     * @return
     */
    private Resident getResidentStudent(String[] commandBody, Roster roster) {
        Profile searchProfile = new Profile(commandBody[2], commandBody[1],
                new Date(commandBody[3]));

        for (int i = 0; i < roster.getRosterSize(); i++) {
            Student student = roster.getRoster()[i];
            if ((student instanceof Resident) &&
                    (student.getProfile().equals(searchProfile))) {
                return (Resident) student;

            } else if ((student instanceof NonResident) &&
                    (student.getProfile().equals(searchProfile))) {
                System.out.println(searchProfile
                        + " (Non-Resident) is not eligible for the scholarship.");
                return null;

            } else if ((student instanceof International) &&
                    (student.getProfile().equals(searchProfile))) {
                System.out.println(searchProfile
                        + " (International) is not eligible for the scholarship.");
                return null;

            } else if ((student instanceof TriState) &&
                    (student.getProfile().equals(searchProfile))) {
                System.out.println(searchProfile
                        + " (TriState) is not eligible for the scholarship.");
                return null;
            }
        }

        System.out.println(searchProfile + " is not in the roster.");
        return null;
    }

    /**
     * needs comments
     *
     * @param student
     * @return
     */
    private boolean isScholarshipEligible(Resident student, Enrollment enrollment) {
        for (int i = 0; i < enrollment.getEnrollmentSize(); i++) {
            EnrollStudent enrollStudent = enrollment.getEnrollStudents()[i];
            if (student.getProfile().equals(enrollStudent.getProfile())) {
                if (enrollStudent.getCreditsEnrolled() < MIN_SCHOLARSHIP_CREDITS) {
                    System.out.println(student.getProfile().toString() +
                            " part time student is not eligible for the scholarship.");
                    return false;
                }
                return true;
            }
        }

        System.out.println(student.getProfile() + " is not enrolled.");
        return false;
    }

    /**
     * This method takes in a String array which represents the argument body of an
     * entered "R" or "C" command, and returns a Student object associated with the
     * arguments if it is found in the roster array. First we create a new profile instance
     * using the body arguments representing the first and last names and birthdate. Next we
     * search through the roster array to find a matching profile associated with a student. If
     * one is found, we return the associated Student object and null otherwise.
     *
     * @param roster      Roster object which contains the roster array we are searching through
     * @param commandBody String array representing an "R" or "C" command argument body
     *                    ("R Aryan Patel 1/22/2002") or ("C Aryan Patel 1/22/2002 BAIT")
     * @return Student object that is associated with the profile we are searching for
     */
    private Student getStudent(Roster roster, String[] commandBody) {
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
     * needs comments
     *
     * @param roster
     * @param profile
     * @return
     */
    private Student getStudent(Roster roster, Profile profile){
        for(int i = 0; i < roster.getRosterSize(); i++) {
            Student student = roster.getRoster()[i];
            if (student.getProfile().equals(profile)) {
                return student;
            }
        }

        return null;
    }

    /**
     * needs comments
     *
     * @param commandBody
     * @return
     */
    private Student createStudent(String[] commandBody) {
        if (!validateStudentDateOfBirth(commandBody[3])
                || !validateStudentMajor(commandBody[4], commandBody[5])) return null;

        String resStatus = commandBody[0];
        Major major = determineMajor(commandBody[4]);
        Profile profile = new Profile(commandBody[2], commandBody[1], new Date(commandBody[3]));
        int credits = Integer.parseInt(commandBody[5]);

        switch (resStatus) {
            case "R", "AR" -> {
                return new Resident(profile, major, credits, 0);
            }
            case "N", "AN" -> {
                return new NonResident(profile, major, credits);
            }
            case "T", "AT" -> {
                if (commandBody[6].equalsIgnoreCase("NY")) {
                    return new TriState(profile, major, credits, "NY");
                } else if (commandBody[6].equalsIgnoreCase("CT")) {
                    return new TriState(profile, major, credits, "CT");
                }
                System.out.println(commandBody[6] + ": Invalid state code.");
                return null;
            }
            case "I", "AI" -> {
                if (commandBody.length == A_COMMAND_L) {
                    return new International(profile, major, credits, false);
                }
                if (commandBody[6].equalsIgnoreCase("true")) {
                    return new International(profile, major, credits, true);
                } else if (commandBody[6].equalsIgnoreCase("false")) {
                    return new International(profile, major, credits, false);
                }
                System.out.println(commandBody[6] + ": Invalid study abroad status.");
                return null;
            }
        }
        System.out.println("Error: could not create student.");
        return null;
    }

    /**
     * needs comments
     *
     * @param profile
     * @param roster
     * @param creditsEnrolled
     */
    private void updateCompletedCredits (Profile profile, Roster roster, int creditsEnrolled) {
        for (int i = 0; i < roster.getRosterSize(); i++) {
            Student student = roster.getRoster()[i];
            if (student.getProfile().equals(profile)) {
                int completedCredits = student.getCreditCompleted();
                student.setCreditCompleted(completedCredits + creditsEnrolled);
            }
        }
    }

    /**
     * needs comments
     *
     * @param commandBody
     * @param roster
     */
    private void executeCommandLS(String[] commandBody, Roster roster) {
        if (commandBody.length != LS_COMMAND_L) {
            System.out.println("Incorrect number of arguments, please try again!");
            return;
        }

        String fileName = commandBody[1];
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String[] body = scanner.nextLine().split(",");
                Student student;
                if ((student = createStudent(body)) == null) {
                    System.out.println("Fatal: Text file line format error.");
                    return;
                }
                try {
                    roster.add(student);
                } catch (Exception e) {
                    System.out.println("Fatal: Student could not be added, please check the text file.");
                    return;
                }
            }
            System.out.println("Students loaded to the roster.");
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
    }

    /**
     * rework
     * This method takes care of executing the "A" with the given body arguments and
     * Roster object. First we check if the number of body arguments is correct. If it is
     * then we continue and try to create a new Student object with the createStudent() method.
     * If creating a new Student object fails, we return from method. Otherwise, we attempt to add
     * the new Student object to the roster array. The student is not added if it is already in
     * the roster array.
     *
     * @param commandBody String array representing an "A" command argument body
     *                    ("A Aryan Patel 1/22/2002 CS 90")
     * @param roster Roster object which contains the roster array that we are adding to
     */
    private void executeCommandA(String[] commandBody, Roster roster) {
        String operation = commandBody[0]; int cmdLength = commandBody.length;
        if (cmdLength < MIN_COMMAND_L) {
            System.out.println("Missing data in line command.");
            return;
        }
        if (operation.equals("AT") && cmdLength != A_COMMAND_L_EX) {
            if(commandBody.length == A_COMMAND_L) System.out.println("Missing the state code.");
            else System.out.println("Missing data in command line.");
            return;
        } else if (operation.equals("AI") && cmdLength < A_COMMAND_L) {
            System.out.println("Missing data in command line.");
            return;
        } else if ((operation.equals("AR") || operation.equals("AN")) && cmdLength != A_COMMAND_L) {
            System.out.println("Missing data in command line.");
            return;
        }

        Student student;
        if ((student = createStudent(commandBody)) == null) {
            return;
        }
        try {
            if(!roster.add(student)) {
                System.out.println(student.getProfile().toString() + " is already in the roster.");
            } else {
                System.out.println(student.getProfile().toString() + " added to the roster.");
            }

        } catch (Exception e) {
            System.out.println("ERROR: Student could not be added, please try again.");
        }
    }

    /**
     * This method takes care of executing the "R" command with the given body arguments and
     * Roster object. First we check if the number of body arguments is correct. Then we try to find
     * and return the Student object associated with the first name, last name, and date of birth
     * provided with the body arguments using the getStudent() method. If no such student is found,
     * we return from the method. Otherwise, we attempt to remove the Student object from the roster
     * array.
     *
     * @param commandBody String array representing an "R" command argument body
     *                   ("R Aryan Patel 1/22/2002")
     * @param roster Roster object which contains the roster array that we are removing from
     */
    private void executeCommandR(String[] commandBody, Roster roster) {
        if (commandBody.length != R_COMMAND_L) {
            System.out.println("Incorrect number of arguments, please try again!");
            return;
        }

        Student student;
        if((student = getStudent(roster, commandBody)) == null) {
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
     * This method takes care of executing the "C" command with the given body arguments and
     * Roster object. First we check if the number of body arguments is correct. Then we try to find
     * and return the Student object associated with the first name, last name, and date of birth
     * provided with the body arguments using the getStudent() method. If no such student is found,
     * we return from the method. Otherwise, we attempt to change the Major associated with the Student
     * object returned from getStudent(). The major provided in the body argument is validated before
     * doing so. The Major is not changed if the provided major in the body arguments is not part of
     * the Major enum class.
     *
     * @param commandBody String array representing an "C" command argument body
     *                    ("C Aryan Patel 1/22/2002 BAIT")
     * @param roster Roster object which contains the roster array that we are modifying
     *               a student's major in
     */
    private void executeCommandC(String[] commandBody, Roster roster) {
        if (commandBody.length != C_COMMAND_L) {
            System.out.println("Incorrect number of arguments, please try again!");
            return;
        }

        Student student;
        if((student = getStudent(roster, commandBody)) == null) {
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
     * needs comments
     *
     * @param commandBody
     * @param roster
     * @param enrollment
     */
    private void executeCommandE(String[] commandBody, Roster roster, Enrollment enrollment) {
        if (commandBody.length < E_COMMAND_L) {
            System.out.println("Missing data in line command.");
            return;
        }

        Student student;
        if ((student = getStudent(roster, commandBody)) == null) {
            System.out.println("Cannot enroll: " + commandBody[1] + " " + commandBody[2] +
                    " " + commandBody[3] + " is not in the roster.");
            return;
        }

        String creditsEnrolled = commandBody[4];
        if (!(validateEnrollmentCredits(student, creditsEnrolled))) {
            return;
        }

        EnrollStudent enrollStudent = new EnrollStudent(
                student.getProfile(), Integer.parseInt(creditsEnrolled));

        enrollment.add(enrollStudent);

        System.out.println(student.getProfile().toString() + " enrolled "
                + creditsEnrolled + " credits");
    }

    /**
     * needs comments
     *
     * @param commandBody
     * @param enrollment
     */
    private void executeCommandD(String[] commandBody, Enrollment enrollment) {
        if (commandBody.length < D_COMMAND_L) {
            System.out.println("Missing data in line command.");
            return;
        }

        String fname = commandBody[1], lname = commandBody[2], dob = commandBody[3];
        Profile profile = new Profile(lname, fname, new Date(dob));
        EnrollStudent student = new EnrollStudent(profile, DUMMY_VALUE_CREDITS);

        if (enrollment.getEnrollmentSize() == MIN_ENROLLMENT_SIZE) {
            System.out.println("Enrollment is empty!");
            return;
        }
        if (!enrollment.contains(student)) {
            System.out.println(profile + " is not enrolled.");
            return;
        }

        enrollment.remove(student);
        System.out.println(profile + " dropped.");
    }

    /**
     * needs comments
     *
     * @param commandBody
     * @param roster
     */
    private void executeCommandS(String[] commandBody, Roster roster, Enrollment enrollment) {
        if (commandBody.length < MIN_COMMAND_L) {
            System.out.println("Missing data in line command.");
            return;
        }
        Resident student;
        if ((student = getResidentStudent(commandBody, roster)) == null) {
            return;
        }
        if (commandBody.length == MIN_COMMAND_L) {
            System.out.println("Missing scholarship amount.");
            return;
        }

        if (!(isScholarshipEligible(student, enrollment))) {
            return;
        }

        try {
            int scholarshipAmount = Integer.parseInt(commandBody[4]);
            if (scholarshipAmount > MAX_SCHOLARSHIP_AMT || scholarshipAmount <= MIN_SCHOLARSHIP_AMT) {
                System.out.println(scholarshipAmount + ": invalid amount.");
                return;
            }
            student.setScholarship(scholarshipAmount);
            System.out.println(student.getProfile() + ": scholarship amount updated.");

        } catch (NumberFormatException e) {
            System.out.println("Amount is not an integer.");
        }
    }

    /**
     * needs comments
     *
     * @param roster
     * @param enrollment
     */
    private void executeCommandSE(Roster roster, Enrollment enrollment) {
        for (int i = 0; i < enrollment.getEnrollmentSize(); i++) {
            EnrollStudent enrollStudent = enrollment.getEnrollStudents()[i];
            updateCompletedCredits(enrollStudent.getProfile(), roster,
                    enrollStudent.getCreditsEnrolled());
        }

        System.out.println("Credit completed has been updated.");
        System.out.println("** list of students eligible for graduation **");

        for(int i = 0; i < roster.getRosterSize(); i++) {
            Student student = roster.getRoster()[i];
            if(student.getCreditCompleted() >= GRAD_ELIGIBLE_CREDITS) {
                System.out.println(student);
            }
        }
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
     * This method takes care of executing the "L" command which prints students in the roster array that
     * are enrolled within the school provided in the command body arguments. We check to see if the
     * number of arguments in the command body is correct and also if the roster array is empty.
     * If the array is not empty and the correct number of arguments are entered, we sort the roster
     * array by a student's Profile (last name, first name, dob) using the Roster print() method, and
     * then print only the students associated with the given school in the body arguments using the
     * overloaded printRoster() method.
     *
     * @param commandBody String array representing an "L" command argument body ("L SAS")
     * @param roster Roster object which contains the roster array that we are printing from
     */
    private void executeCommandL(String[] commandBody, Roster roster) {
        if (commandBody.length != L_COMMAND_L) {
            System.out.println("Incorrect number of arguments, please try again!");
            return;
        }
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            System.out.println("Student roster is empty!");
            return;
        }

        roster.print();
        printRoster(roster, commandBody[1]);
    }

    /**
     * This method takes care of executing the "P" command which prints students in the
     * roster array in ascending order by Profile (last name, first name, dob). First we check if
     * the roster array is empty. If it is not, we proceed to order the roster array by using
     * the Roster object's print() method. Then, we print the roster using the non-overloaded
     * method printRoster() which prints all students in the now sorted roster array.
     *
     * @param roster Roster object which contains the roster array that we are printing from
     */
    private void executeCommandP(Roster roster) {
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            System.out.println("Student roster is empty!");
        } else {
            System.out.println("** Student roster sorted by last name, first name, DOB **");
            roster.print();
            printRoster(roster);
            System.out.println("* end of roster *");
        }
    }

    /**
     * This method takes care of executing the "PS" command which prints students in the
     * roster array in ascending order by class standing (Freshman, Junior, Senior, Sophomore).
     * First we check if the roster array is empty. If it is not, we proceed to order the roster
     * array by using the Roster object's printByStanding() method. Then, we print the roster using
     * the non-overloaded method printRoster() which prints all students in the now sorted
     * roster array.
     *
     * @param roster Roster object which contains the roster array that we are printing from
     */
    private void executeCommandPS(Roster roster) {
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            System.out.println("Student roster is empty!");
        } else {
            System.out.println("** Student roster sorted by standing **");
            roster.printByStanding();
            printRoster(roster);
            System.out.println("* end of roster *");
        }
    }

    /**
     * This method takes care of executing the "PC" command which prints students in the
     * roster array in ascending order by major and school (BAIT, RBS). First we check if the
     * roster array is empty. If it is not, we proceed to order the roster array by using the
     * Roster object's printBySchoolMajor() method. Then, we print the roster using the
     * non-overloaded method printRoster() which prints all students in the now sorted
     * roster array.
     *
     * @param roster Roster object which contains the roster array that we are printing from
     */
    private void executeCommandPC(Roster roster) {
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            System.out.println("Student roster is empty!");
        } else {
            System.out.println("** Student roster sorted by school, major **");
            roster.printBySchoolMajor();
            printRoster(roster);
            System.out.println("* end of roster *");
        }
    }

    /**
     * needs comments
     *
     * @param enrollment
     */
    private void executeCommandPE(Enrollment enrollment) {
        if (enrollment.getEnrollmentSize() == MIN_ENROLLMENT_SIZE) {
            System.out.println("Enrollment is empty!");
        } else {
            System.out.println("** Enrollment **");
            enrollment.print();
            System.out.println("* end of enrollment *");
        }
    }

    /**
     * needs comments
     *
     * @param student
     * @param creditsEnrolled
     */
    private void printTuitionDueInline (Student student, int creditsEnrolled) {
        DecimalFormat df = new DecimalFormat("$###,###.00");

        if (student instanceof Resident residentStudent) {
            System.out.println(residentStudent.getProfile()
                    + " (Resident)"
                    + " enrolled " + creditsEnrolled
                    + " credits: tuition due: "
                    + df.format(residentStudent.tuitionDue(creditsEnrolled)));

        } else if (student instanceof NonResident nonResidentStudent) {
            if (nonResidentStudent instanceof International internationalStudent) {
                System.out.println(internationalStudent.getProfile()
                        + (internationalStudent.getStudyAbroadStatus()
                        ? " (International studentstudy abroad)" : " (International student)")
                        + " enrolled " + creditsEnrolled
                        + " credits: tuition due: "
                        + df.format(internationalStudent.tuitionDue(creditsEnrolled)));

            } else if (nonResidentStudent instanceof TriState triStateStudent) {
                System.out.println(triStateStudent.getProfile()
                        + " (Tri-state " + triStateStudent.getState() + ")"
                        + " enrolled " + creditsEnrolled
                        + " credits: tuition due: "
                        + df.format(triStateStudent.tuitionDue(creditsEnrolled)));

            } else {
                System.out.println(nonResidentStudent.getProfile()
                        + " (Non-Resident)"
                        + " enrolled " + creditsEnrolled
                        + " credits: tuition due: "
                        + df.format(nonResidentStudent.tuitionDue(creditsEnrolled)));
            }
        }
    }

    /**
     * needs comments
     *
     * @param enrollment
     */
    private void executeCommandPT(Roster roster, Enrollment enrollment) {
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            System.out.println("Student roster is empty!");
            return;
        }
        if (enrollment.getEnrollmentSize() == MIN_ENROLLMENT_SIZE) {
            System.out.println("Enrollment is empty!");
            return;
        }

        System.out.println("** Tuition due **");
        for (int i = 0; i < enrollment.getEnrollmentSize(); i++) {
            EnrollStudent enrollStudent = enrollment.getEnrollStudents()[i];

            Student student;
            if ((student = getStudent(roster, enrollStudent.getProfile())) == null) {
                System.out.println("Fatal: " + enrollStudent.getProfile() +
                        " is enrolled but not in the roster.");
                return;
            }

            printTuitionDueInline(student, enrollStudent.getCreditsEnrolled());
        }

        System.out.println("* end of tuition due *");
    }

    /**
     *  rework
     *  This method is the main driver of executing the commands entered by the user in
     *  the command line. The String array representing the command body and a Roster object is
     *  passed as arguments to this method. These arguments are further passed down depending on
     *  which execute method is called. The correct execute method is determined by the first index
     *  in the body arguments. This value is case-sensitive. If nothing is found for this value, we don't
     *  do anything and prompt for a new input from the user. If an incorrect or unknown value is found
     *  we let the user known and prompt for a new input.
     *
     * @param commandBody String array representing a "A", "R", "L", "C", "P", "PS", or "PC"
     *                    command argument body (see execute methods for commandBody examples)
     * @param roster Roster object which we are executing the commands on
     */
    private void parseCommand (String[] commandBody, Roster roster, Enrollment enrollment) {
        String operation = commandBody[0];
        switch (operation) {
            case "LS": executeCommandLS(commandBody, roster);
                break;
            case "R":  executeCommandR(commandBody, roster);
                break;
            case "C":  executeCommandC(commandBody, roster);
                break;
            case "L":  executeCommandL(commandBody, roster);
                break;
            case "P":  executeCommandP(roster);
                break;
            case "PS": executeCommandPS(roster);
                break;
            case "PC": executeCommandPC(roster);
                break;
            case "PE": executeCommandPE(enrollment);
                break;
            case "PT": executeCommandPT(roster, enrollment);
                break;
            case "E":  executeCommandE(commandBody, roster, enrollment);
                break;
            case "D":  executeCommandD(commandBody, enrollment);
                break;
            case "S":  executeCommandS(commandBody, roster, enrollment);
                break;
            case "SE": executeCommandSE(roster, enrollment);
                break;
            case "AR": case "AN": case "AT": case "AI": executeCommandA(commandBody, roster);
                break;
            case "":
                break;
            default: System.out.println(commandBody[0] + " is an invalid command!");
                break;
        }
    }

    /**
     * This method is used to run the entire project. We first indicate to the
     * user that the program has started running, and then we create a new instance of
     * Scanner, Roster, and Enrollment. Next we enter a loop that takes input at the command line in
     * order to interact with the program. The program continues to take commands until "Q" is entered,
     * which terminates the entire program with a message. This method processes single line
     * commands as well as a sequence of line commands. Each line that is entered is split into a
     * String array that contains each argument seperated by one or more spaces in the command body.
     * The array is passed into parseCommand() to execute the command.
     *
     */
    public void run() {
        System.out.println("Tuition Manager running...");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        Enrollment enrollment = new Enrollment();
        Roster roster = new Roster();

        String input;
        while (!(input = scanner.nextLine()).equals("Q")) {
            String[] commandBody = input.split("\\s+");
            parseCommand(commandBody, roster, enrollment);
        }

        scanner.close();
        System.out.println("Tuition Manager terminated.");
    }
}
