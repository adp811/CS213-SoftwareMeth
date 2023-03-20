package com.tuitionmanager.project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.text.DecimalFormat;

/**
 * This is the Tuition Manager user interface controller class. It contains
 * injected fxml java elements, and handler methods neccesary to give a user an
 * interface to interact with the Tuition Manager API.
 *
 * @author Aryan Patel & Rushi Patel
 */
public class TuitionManagerController {

    private static final int MIN_AGE = 16;
    private static final int MIN_CREDITS = 0;
    private static final int MIN_ROSTER_SIZE = 0;
    private static final int MIN_ENROLLMENT_SIZE = 0;
    private static final int MAX_SCHOLARSHIP_AMT = 10000;
    private static final int MIN_SCHOLARSHIP_AMT = 0;
    private static final int GRAD_ELIGIBLE_CREDITS = 120;
    private static final int MIN_SCHOLARSHIP_CREDITS = 12;
    private static final int DUMMY_VALUE_CREDITS = 15;

    private static final int UNDETERMINED_STUDENT = -1;
    private static final int RESIDENT = 0;
    private static final int NON_RESIDENT = 1;
    private static final int INTERNATIONAL = 2;
    private static final int INTERNATIONAL_SA = 3;
    private static final int TRISTATE_NY = 4;
    private static final int TRISTATE_CT = 5;

    private static final int ROSTER_TAB = 0;
    private static final int ENROLLMENT_TAB = 1;
    private static final int SCHOLARSHIP_TAB = 2;

    private static final int TXT_IDX_STATUS = 0;
    private static final int TXT_IDX_FNAME = 1;
    private static final int TXT_IDX_LNAME = 2;
    private static final int TXT_IDX_DOB = 3;
    private static final int TXT_IDX_MAJOR = 4;
    private static final int TXT_IDX_CREDITS = 5;
    private static final int TXT_IDX_SA_ST = 6;

    private static final int TXT_MIN_LINE_LEN = 6;
    private static final int TXT_MAX_LINE_LEN = 7;

    private static final String EMPTY_STRING = "";

    private Roster roster;
    private Enrollment enrollment;


    @FXML
    private TextField firstNameTextField, lastNameTextField, creditsCompletedTextField,
                      fileNameTextField;
    @FXML
    private RadioButton triStateRadioButton, nyStateRadioButton, ctStateRadioButton;

    @FXML
    private RadioButton baitRadioButton, residentRadioButton;

    @FXML
    private ToggleGroup statusGroup, nonResStatusGroup, stateGroup, majorGroup;

    @FXML
    private HBox triStateSelectionView, internationalSelectionView;

    @FXML
    private CheckBox studyAbroadCheckBox;

    @FXML
    private DatePicker dateOfBirthPicker;


    @FXML
    private TextField firstNameTextField_E, lastNameTextField_E, creditsEnrolledTextField;

    @FXML
    private DatePicker dateOfBirthPicker_E;


    @FXML
    private TextField firstNameTextField_S, lastNameTextField_S, scholarshipAmountTextField;

    @FXML
    private DatePicker dateOfBirthPicker_S;


    @FXML
    private ChoiceBox<String> sortTypeChoiceBox, schoolChoiceBox;

    @FXML
    private TextArea printTextArea;


    @FXML
    private TextArea printTextArea_Sm;


    @FXML
    private TextArea consoleTextArea;


    /**
     * This method takes care of validating the first and last name text
     * fields across all tabs. The validation process involved checking if none, one
     * or both text fields are empty.
     *
     * @param tab int value identifying which tab we are validating the text fields in
     * @return boolean value representing if the validation was successful or not
     */
    private boolean validateFullName(int tab) {
        TextField firstName;
        TextField lastName;

        if (tab == ENROLLMENT_TAB) {
            firstName = firstNameTextField_E;
            lastName = lastNameTextField_E;
        } else if (tab == SCHOLARSHIP_TAB) {
            firstName = firstNameTextField_S;
            lastName = lastNameTextField_S;
        } else {
            firstName = firstNameTextField;
            lastName = lastNameTextField;
        }

        if (firstName.getText().trim().isEmpty() && lastName.getText().trim().isEmpty()) {
            consoleTextArea.setText("Name Invalid: missing first and last name!");
            return true;
        } else if (firstName.getText().trim().isEmpty()) {
            consoleTextArea.setText("Name Invalid: missing first name!");
            return true;
        } else if (lastName.getText().trim().isEmpty()) {
            consoleTextArea.setText("Name Invalid: missing last name!");
            return true;
        }

        return false;
    }

    /**
     * This method takes care of validating the date picker selection
     * across all tabs. The validation process involved checking first if the
     * selected date is null. Then it checks to see if the date is a future or current
     * date using the custom Date(). Finally, it checks to see if the age represented
     * by the given birthdate is younger than 16 years old.
     *
     * @param tab int value identifying which tab we are validating the date picker in
     * @return boolean value representing if the validation was successful or not
     */
    private boolean validateDateOfBirth(int tab) {
        DatePicker dateOfBirth;

        if (tab == ENROLLMENT_TAB) {
            dateOfBirth = dateOfBirthPicker_E;
        } else if (tab == SCHOLARSHIP_TAB) {
            dateOfBirth = dateOfBirthPicker_S;
        } else {
            dateOfBirth = dateOfBirthPicker;
        }

        if (dateOfBirth.getValue() == null) {
            consoleTextArea.setText("DOB Invalid: no date selected!");
            return true;
        }

        String dateString = "" + dateOfBirth.getValue().getDayOfMonth()
                + "/" + dateOfBirth.getValue().getDayOfMonth()
                + "/" + dateOfBirth.getValue().getYear();

        Date dob = new Date(dateString), current = new Date();

        if (dob.equals(current) || dob.compareTo(current) > 0) {
            consoleTextArea.setText("DOB Invalid: cannot be current or future date!");
            return true;
        }

        int age = current.getYear() - dob.getYear();
        if (current.getMonth() < dob.getMonth()) {
            age--;
        } else if (current.getMonth() == dob.getMonth() && current.getDay() < dob.getDay()) {
            age--;
        }
        if (age < MIN_AGE) {
            consoleTextArea.setText("DOB Invalid: " + dob + " younger than 16 years old.");
            return true;
        }

        return false;
    }

    /**
     * This method takes care of validating the credit completed text field. The
     * validation process involves checking to make sure the value entered is a non-negative
     * integer.
     *
     * @return boolean value representing if the validation was successful or not
     */
    private boolean validateCreditsCompleted() {
        if (creditsCompletedTextField.getText().isEmpty()) {
            consoleTextArea.setText("Credits completed invalid: no value entered!");
            return false;
        }
        try {
            int credits = Integer.parseInt(creditsCompletedTextField.getText());
            if (credits < MIN_CREDITS) {
                consoleTextArea.setText("Credits completed invalid: cannot be negative!");
                return false;
            }
        } catch (NumberFormatException e) {
            consoleTextArea.setText("Credits completed invalid: not an integer!");
            return false;
        }

        return true;
    }

    /**
     * This method takes care of updating the credits completed for a given student's Profile.
     * We use the given Profile to find the student in the roster array within the
     * Roster object. Once we find the student, we add the number of credits enrolled to their
     * total credits completed.
     *
     * @param profile Profile object of the student we are trying to update the completed credits for
     * @param roster Roster object containing the roster array that we are searching through
     * @param creditsEnrolled int containing the number of credits enrolled by the student
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
     * This method takes care of validating the number of credits enrolled by a Student
     * given a Student object and the credits enrolled as a String. It checks for any incorrect
     * input such as negative or non-integer values. If there is an error with the
     * value given, then a message is displayed.
     *
     * @param student Student object representing the student for which we are validating the
     *                enrolled credits for
     * @param creditsEnrolled String which represents the number of credits enrolled
     * @return boolean which represents whether the given value of enrolled credits is valid
     *         or not
     */
    private boolean validateEnrollmentCredits (Student student, String creditsEnrolled) {
        try {
            int credits = Integer.parseInt(creditsEnrolled);
            if (credits < MIN_CREDITS) {
                consoleTextArea.setText("Credits enrolled cannot be negative.");
                return false;
            }
            boolean isValidAmount = student.isValid(credits);
            if (isValidAmount) return true;

        } catch (NumberFormatException e) {
            consoleTextArea.setText("Credits enrolled is not an integer.");
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

        consoleTextArea.setText(status + " " + creditsEnrolled + ": invalid credit hours.");
        return false;
    }

    /**
     * This method takes care of validating the radio button selections in the Roster tab
     * that determine the status of a Student that needs to be added to the roster. It handles
     * logic for the correct radio selections and outputs a message if there are any errors or
     * missing selections. The method returns an int representing the status determined by the
     * radio button selections.
     *
     * @return an int value representing the student status determined by the radio selections
     */
    private int validateStudentStatusSelection () {
        RadioButton selectedStatusButton = (RadioButton) statusGroup.getSelectedToggle();
        if (selectedStatusButton != null) {
            switch (selectedStatusButton.getText()) {
                case "Resident" -> { return RESIDENT; }
                case "Non-Resident" -> {
                    RadioButton selectedNonResStatusButton = (RadioButton) nonResStatusGroup.getSelectedToggle();
                    if (selectedNonResStatusButton != null) {
                        switch (selectedNonResStatusButton.getText()) {
                            case "TriState" -> {
                                RadioButton selectedStateButton = (RadioButton) stateGroup.getSelectedToggle();
                                if (selectedStateButton != null) {
                                    switch (selectedStateButton.getText()) {
                                        case "NY" -> { return TRISTATE_NY; }
                                        case "CT" -> { return TRISTATE_CT; }
                                    }
                                }
                                consoleTextArea.setText("Status Selection Invalid: please choose a state!");
                                return UNDETERMINED_STUDENT;
                            }
                            case "International" -> {
                                if (studyAbroadCheckBox.isSelected()) return INTERNATIONAL_SA;
                                else return INTERNATIONAL;
                            }
                        }
                    }
                    return NON_RESIDENT;
                }
            }
        }
        consoleTextArea.setText("Status Selection Invalid: please choose resident or non-resident!");
        return UNDETERMINED_STUDENT;
    }

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
            if(major.name().equalsIgnoreCase(inputMajor)) {
                return major;
            }
        }
        return null;
    }

    /**
     * This method creates a new Student object with the given studentInformation String. The
     * string parameter contains information gathered from the request to create a new student
     * from the elements in the user interface. The status integer is used to ultimately determine
     * which type of student is returned from the method.
     *
     * @param studentInformation String object containing the information needed to create a
     *                           new student. It follows the format:
     *                           ("Aryan Patel 01/22/2002 CS 75 0")
     * @return a Student object derived from the abstract Student class and the given information
     */
    private Student createStudent(String studentInformation) {
        String[] arrInfo = studentInformation.split("\\s+");

        Profile profile = new Profile(arrInfo[1], arrInfo[0], new Date(arrInfo[2]));
        Major major = determineMajor(arrInfo[3]);

        int creditCompleted = Integer.parseInt(arrInfo[4]);
        int status = Integer.parseInt(arrInfo[5]);

        return switch (status) {
            case RESIDENT -> new Resident(profile, major, creditCompleted, MIN_SCHOLARSHIP_AMT);
            case NON_RESIDENT -> new NonResident(profile, major, creditCompleted);
            case INTERNATIONAL -> new International(profile, major, creditCompleted, false);
            case INTERNATIONAL_SA -> new International(profile, major, creditCompleted, true);
            case TRISTATE_NY -> new TriState(profile, major, creditCompleted, "NY");
            case TRISTATE_CT -> new TriState(profile, major, creditCompleted, "CT");
            default -> null;
        };
    }

    /** This method takes care of finding and returning a Student object in the given
     * Roster that matches the given Profile. It looks through the Student array contained in the
     * roster object and looks for a student with a matching Profile attribute.
     *
     * @param roster Roster object containing roster array that we are searching through
     * @param profile Profile object containing a student's information that we are trying
     *                to look for
     * @return Student object associated with the given Profile or null if it doesn't exist
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

    /** This method takes care of finding and returning a EnrollStudent object in the given
     * Enrollment that matches the given Profile. It looks through the EnrollStudent array
     * contained in the enrollment object and looks for a student with a matching Profile
     * attribute.
     *
     * @param enrollment Enrollment object containing enrollStudents array that we are
     *                   searching through
     * @param profile Profile object containing a student's information that we are trying
     *                to look for
     * @return EnrollStudent object associated with the given Profile or null if it doesn't exist
     */
    private EnrollStudent getEnrollStudent(Enrollment enrollment, Profile profile){
        for(int i = 0; i < enrollment.getEnrollmentSize(); i++) {
            EnrollStudent student = enrollment.getEnrollStudents()[i];
            if (student.getProfile().equals(profile)) {
                return student;
            }
        }
        return null;
    }

    /**
     * This method takes care of retrieving a Resident student object specifically for
     * awarding a scholarship. It searches through the given Roster object array and
     * finds the profile associated with the inputted information. If the Resident student is
     * not found or the input information is tied to a different type of student, an error
     * message is displayed.
     *
     * @param searchProfile Profile object representing the student Profile that we are searching
     *                      for
     * @param roster Roster object which contains the roster array which we are searching
     *               through
     * @return Resident object if it is found, or null if it does not exist or is the wrong type
     */
    private Resident getResidentStudent(Profile searchProfile, Roster roster) {
        for (int i = 0; i < roster.getRosterSize(); i++) {
            Student student = roster.getRoster()[i];
            if ((student instanceof Resident) &&
                    (student.getProfile().equals(searchProfile))) {
                return (Resident) student;

            } else if ((student instanceof NonResident) &&
                    (student.getProfile().equals(searchProfile))) {
                consoleTextArea.setText(searchProfile
                        + " (Non-Resident) is not eligible for the scholarship.");
                return null;

            } else if ((student instanceof International) &&
                    (student.getProfile().equals(searchProfile))) {
                consoleTextArea.setText(searchProfile
                        + " (International) is not eligible for the scholarship.");
                return null;

            } else if ((student instanceof TriState) &&
                    (student.getProfile().equals(searchProfile))) {
                consoleTextArea.setText(searchProfile
                        + " (TriState) is not eligible for the scholarship.");
                return null;
            }
        }

        consoleTextArea.setText(searchProfile + " is not in the roster.");
        return null;
    }

    /**
     * This method takes care of checking if a Resident student is eligible for
     * a scholarship. First we check if the given Resident student is in the enrollStudent
     * array contained within the given Enrollment object. If it is, then we continue to
     * check if the Resident student is a full time student based on the credits enrolled.
     * If the student is full-time, we return true for scholarship eligibility.
     *
     * @param student Resident object for which we are determining scholarship eligibility
     * @param enrollment Enrollment object containing the enrollStudents array we are
     *                   searching through
     * @return boolean which represents whether the given Resident student is eligible for
     *         a scholarship or not based on their enrolled credits
     */
    private boolean isScholarshipEligible(Resident student, Enrollment enrollment) {
        for (int i = 0; i < enrollment.getEnrollmentSize(); i++) {
            EnrollStudent enrollStudent = enrollment.getEnrollStudents()[i];
            if (student.getProfile().equals(enrollStudent.getProfile())) {
                if (enrollStudent.getCreditsEnrolled() < MIN_SCHOLARSHIP_CREDITS) {
                    consoleTextArea.setText(student.getProfile().toString() +
                            " part time student is not eligible for the scholarship.");
                    return false;
                }
                return true;
            }
        }

        consoleTextArea.setText(student.getProfile() + " is not enrolled.");
        return false;
    }

    /**
     * This method takes care of printing the entire roster array associated with the
     * inputted Roster object.
     *
     * @param roster Roster object which contains the roster array we want to print
     * */
    private void printRoster(Roster roster) {
        for (int i = 0; i < roster.getRosterSize(); i++) {
            printTextArea.appendText(roster.getRoster()[i] + "\n");
        }
    }

    /**
     * THis method takes care of printing the entire enrollment array associated with the
     * inputted Enrollment object.
     *
     * @param enrollment Enrollment object which contains the enrollment array we want to print
     */
    private void printEnrollment(Enrollment enrollment) {
        for (int i = 0; i < enrollment.getEnrollmentSize(); i++) {
            printTextArea.appendText(enrollment.getEnrollStudents()[i] + "\n");
        }
    }

    /**
     * This method takes care of printing the Student objects in the roster array associated
     * with the given Roster object. The output is sorted by the Student's profile
     * (lname, fname, dob) in ascending order. An error message is shown if the given Roster
     * object contains an empty roster array.
     *
     * @param roster Roster object which contains the roster array that we are printing from
     */
    private void executePrintRosterProfile(Roster roster) {
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            consoleTextArea.setText("Print Error: student roster is empty!");
        } else {
            printTextArea.appendText("** Student roster sorted by last name, first name, DOB **" + "\n");
            roster.print();
            printRoster(roster);
            printTextArea.appendText("* end of roster *" + "\n");
            consoleTextArea.setText("Print successful!");
        }
    }

    /**
     * This method takes care of printing the Student objects in the roster array associated
     * with the given Roster object. The output is sorted by the Student's class standing
     * (Freshman, Junior, Senior, Sophomore) in ascending order. An error message is shown
     * if the given Roster object contains an empty roster array.
     *
     * @param roster Roster object which contains the roster array that we are printing from
     */
    private void executePrintRosterStanding(Roster roster) {
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            consoleTextArea.setText("Print Error: student roster is empty!");
        } else {
            printTextArea.appendText("** Student roster sorted by standing **" + "\n");
            roster.printByStanding();
            printRoster(roster);
            printTextArea.appendText("* end of roster *" + "\n");
            consoleTextArea.setText("Print successful!");
        }
    }

    /**
     * This method takes care of printing the Student objects in the roster array associated
     * with the given Roster object. The output is sorted by the Student's major and school
     * in ascending order. An error message is shown if the given Roster object contains an
     * empty roster array.
     *
     * @param roster Roster object which contains the roster array that we are printing from
     */
    private void executePrintRosterMajorSchool(Roster roster) {
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            consoleTextArea.setText("Print Error: student roster is empty!");
        } else {
            printTextArea.appendText("** Student roster sorted by school, major **" + "\n");
            roster.printBySchoolMajor();
            printRoster(roster);
            printTextArea.appendText("* end of roster *" + "\n");
            consoleTextArea.setText("Print successful!");
        }
    }

    /**
     * This method takes care of printing the Student objects in the roster array associated
     * with the given Roster object. The output shows students that are registered to
     * the given school representing by the school parameter. We check to make sure that
     * the school String represents a valid school name. An error message is shown
     * if the given Roster object contains an empty roster array.
     *
     * @param roster Roster object which contains the roster array that we are printing from
     * @param school String object representing the school which we want to print students
     *               from
     */
    private void executePrintRosterSelectSchool(Roster roster, String school) {
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            consoleTextArea.setText("Print Error: student roster is empty!");
            return;
        }

        roster.print();
        printTextArea.appendText("* Students in " + school + " *" + "\n");

        for (int i = 0; i < roster.getRosterSize(); i++) {
            Student student = roster.getRoster()[i];
            if(student.getMajor().getSchool().equalsIgnoreCase(school)){
                printTextArea.appendText(student + "\n");
            }
        }

        printTextArea.appendText("* end of list **" + "\n");
    }

    /**
     * This method takes care of printing the enrollStudent array contained
     * within a given Enrollment object. The order of printing is the same order
     * of the enrollStudents array. If the array is empty, an error message is
     * displayed.
     *
     * @param enrollment Enrollment object which contains the enrollStudent array
     *                   that we want to print from
     */
    private void executePrintEnrollment(Enrollment enrollment) {
        if (enrollment.getEnrollmentSize() == MIN_ENROLLMENT_SIZE) {
            consoleTextArea.setText("Print Error: enrollment is empty!");
        } else {
            printTextArea.appendText("** Enrollment **" + "\n");
            printEnrollment(enrollment);
            printTextArea.appendText("* end of enrollment *" + "\n");
            consoleTextArea.setText("Print successful!");
        }
    }

    /**
     * This helper method takes in an Abstract Student object and the amount of credits they are
     * enrolled in, to then print out their tuition due based on their credits enrolled and
     * the type of student they are. We check which type of Student they are before outputting
     * this information. The DecimalFormat class is used to format the double value returned from
     * the abstract tuitionDue() method to display as a dollar amount.
     *
     * @param student Abstract Student object that we are outputting the tuition information for
     * @param creditsEnrolled int which contains the number of credits the student is enrolled for
     */
    private void printTuitionDueHelper (Student student, int creditsEnrolled) {
        DecimalFormat df = new DecimalFormat("$###,###.00");

        if (student instanceof Resident residentStudent) {
            printTextArea.appendText(residentStudent.getProfile()
                    + " (Resident)"
                    + " enrolled " + creditsEnrolled
                    + " credits: tuition due: "
                    + df.format(residentStudent.tuitionDue(creditsEnrolled)) + "\n");

        } else if (student instanceof NonResident nonResidentStudent) {
            if (nonResidentStudent instanceof International internationalStudent) {
                printTextArea.appendText(internationalStudent.getProfile()
                        + (internationalStudent.getStudyAbroadStatus()
                        ? " (International student: study abroad)" : " (International student)")
                        + " enrolled " + creditsEnrolled
                        + " credits: tuition due: "
                        + df.format(internationalStudent.tuitionDue(creditsEnrolled)) + "\n");

            } else if (nonResidentStudent instanceof TriState triStateStudent) {
                printTextArea.appendText(triStateStudent.getProfile()
                        + " (Tri-state: " + triStateStudent.getState() + ")"
                        + " enrolled " + creditsEnrolled
                        + " credits: tuition due: "
                        + df.format(triStateStudent.tuitionDue(creditsEnrolled)) + "\n");

            } else {
                printTextArea.appendText(nonResidentStudent.getProfile()
                        + " (Non-Resident)"
                        + " enrolled " + creditsEnrolled
                        + " credits: tuition due: "
                        + df.format(nonResidentStudent.tuitionDue(creditsEnrolled)) + "\n");
            }
        }
    }

    /**
     * This method takes care of printing all students, with their total tuition due,
     * in the enrollStudents array within the given Enrollment object. The total tuition
     * due for each student is printed for each student using the printTuitionDueHelper()
     * method. We check to see if the enrollStudent array is empty before printing, and if
     * the student is enrolled but not in the roster.
     *
     * @param roster Roster object containing the roster array of Student objects that contain
     *               information for students that we are examining in the enrollStudents array
     * @param enrollment Enrollment object containing the enrollStudents array of EnrollStudent
     *                   objects that we are printing the tuition due for.
     */
    private void executePrintTuitionDue(Roster roster, Enrollment enrollment) {
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            consoleTextArea.setText("Print Error: student roster is empty!");
            return;
        }
        if (enrollment.getEnrollmentSize() == MIN_ENROLLMENT_SIZE) {
            consoleTextArea.setText("Print Error: enrollment is empty!");
            return;
        }

        printTextArea.appendText("** Tuition due **" + "\n");
        for (int i = 0; i < enrollment.getEnrollmentSize(); i++) {
            EnrollStudent enrollStudent = enrollment.getEnrollStudents()[i];

            Student student;
            if ((student = getStudent(roster, enrollStudent.getProfile())) == null) {
                consoleTextArea.setText("Fatal: " + enrollStudent.getProfile() +
                        " is enrolled but not in the roster.");
                return;
            }

            printTuitionDueHelper(student, enrollStudent.getCreditsEnrolled());
        }

        printTextArea.appendText("* end of tuition due *" + "\n");
        consoleTextArea.setText("Print Successful!");
    }

    /**
     * This helper method takes a String input line from a loaded text file, and converts
     * it into a command that can be used to create a new student with the createStudent()
     * method.
     *
     * @param line String object representing an information line extracted from a loaded
     *             text file. It follows the format ("R,Jane,Doe,5/1/1996,CS,30")
     * @return String object containing student information that can be used to create
     *         a new student with the createStudent() method.
     */
    private String loadFromFileHelper(String line) {
        String[] lineInfo = line.split(",");

        String status = lineInfo[TXT_IDX_STATUS];
        String studentInformation = lineInfo[TXT_IDX_FNAME]
                + " " + lineInfo[TXT_IDX_LNAME]
                + " " + lineInfo[TXT_IDX_DOB]
                + " " + lineInfo[TXT_IDX_MAJOR]
                + " " + lineInfo[TXT_IDX_CREDITS];

        switch (status) {
            case "R" -> studentInformation = studentInformation.concat(" 0");
            case "N" -> studentInformation = studentInformation.concat(" 1");
            case "T" -> {
                String state = lineInfo[TXT_IDX_SA_ST];
                if (state.equalsIgnoreCase("NY")) {
                    studentInformation = studentInformation.concat(" 4");
                } else if (state.equalsIgnoreCase("CT")) {
                    studentInformation = studentInformation.concat(" 5");
                } else return null;
            }
            case "I" -> {
                if (lineInfo.length == TXT_MIN_LINE_LEN) {
                    studentInformation = studentInformation.concat(" 2");
                } else if (lineInfo.length == TXT_MAX_LINE_LEN) {
                    String isStudyAbroad = lineInfo[TXT_IDX_SA_ST];
                    if (isStudyAbroad.equalsIgnoreCase("true")) {
                        studentInformation = studentInformation.concat(" 3");
                    } else if (isStudyAbroad.equalsIgnoreCase("false")){
                        studentInformation = studentInformation.concat(" 2");
                    } else return null;
                }
            }
        }

        return studentInformation;
    }

    /**
     * This method initializes the choice boxes in the print tab with pre-determined
     * values.
     *
     */
    private void initPrintChoiceBoxes() {
        for(Major major : Major.values()) {
            String school = major.getSchool();
            if (!schoolChoiceBox.getItems().contains(school)) {
                schoolChoiceBox.getItems().add(school);
            }
        }

        sortTypeChoiceBox.getItems().addAll(
                "Roster: Profile - Ascending",
                "Roster: Standing - Ascending",
                "Roster: Major, School - Ascending",
                "Roster: Select School",
                "Enrollment",
                "Tuition Due" );
    }

    /**
     * This method adds a listener to handle visibility for the Non_Resident status
     * selection buttons in the roster tab.
     *
     */
    private void addListenerStatusSelectionGroup() {
        statusGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (((RadioButton) newValue).getText().equals("Non-Resident")) {
                    triStateSelectionView.setVisible(true);
                    internationalSelectionView.setVisible(true);
                } else {
                    triStateSelectionView.setVisible(false);
                    internationalSelectionView.setVisible(false);
                }
            }
        });
    }

    /**
     * This method adds a listener to handle visibility for the international and
     * tristate selection buttons in the roster tab.
     *
     */
    private void addListenerNonResStatusSelectionGroup() {
        nonResStatusGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (((RadioButton) newValue).getText().equals("TriState")) {
                    nyStateRadioButton.setVisible(true);
                    ctStateRadioButton.setVisible(true);
                    studyAbroadCheckBox.setVisible(false);

                } else {
                    nyStateRadioButton.setVisible(false);
                    ctStateRadioButton.setVisible(false);
                    studyAbroadCheckBox.setVisible(true);
                }
            }
        });
    }

    /**
     * This method adds a listener to handle visibility for the international study
     * abroad check box in the roster tab.
     *
     */
    private void addListenerStateSelectionGroup() {
        stateGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (((RadioButton) newValue).getText().equals("NY")) {
                    triStateRadioButton.setSelected(true);
                } else if (((RadioButton) newValue).getText().equals("CT")) {
                    triStateRadioButton.setSelected(true);
                }
            }
        });
    }

    /**
     * This method adds a listener to handle visibility for the school name
     * choice box in the print tab.
     *
     */
    private void addListenerPrintTypeChoiceBox() {
        sortTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    schoolChoiceBox.setVisible(newValue.equals("Roster: Select School"));
                });
    }

    /**
     * This is an event handler method used to handle a button click on the "Add" button
     * in the roster tab. We first validate all the necessary fields needed to add the student
     * to the roster, then we convert all input into a String object, which is then used to create
     * a new Student object. We then try to add the student to the roster, display any error
     * messages that are applicable.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleAddButtonClick (ActionEvent event) {
        if(validateFullName(ROSTER_TAB) || validateDateOfBirth(ROSTER_TAB)
                || !validateCreditsCompleted()) {
            return;
        }

        int status;
        if ((status = validateStudentStatusSelection()) == UNDETERMINED_STUDENT) {
            return;
        }

        String studentInformation = firstNameTextField.getText()
                + " " + lastNameTextField.getText()
                + " " + dateOfBirthPicker.getValue().getMonth().getValue()
                + "/" + dateOfBirthPicker.getValue().getDayOfMonth()
                + "/" + dateOfBirthPicker.getValue().getYear()
                + " " + ((RadioButton) majorGroup.getSelectedToggle()).getText()
                + " " + creditsCompletedTextField.getText()
                + " " + status;

        Student student;
        if ((student = createStudent(studentInformation)) == null) {
            return;
        }
        try {
            if(!roster.add(student)) {
                consoleTextArea.setText(student.getProfile().toString() + " is already in the roster.");
            } else {
                consoleTextArea.setText(student.getProfile().toString() + " added to the roster.");
            }
        } catch (Exception e) {
            consoleTextArea.setText("Fatal Error: Student could not be added, please try again.");
        }
    }

    /**
     * This is an event handler method used to handle a button click on the "Remove" button
     * in the roster tab. We first validate all the necessary fields needed to remove the student
     * from the roster, then we convert all input into a String object, which is then used to
     * remove the Student object from the roster if they exist inside it. We display any error
     * messages that are applicable.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleRemoveButtonClick (ActionEvent event) {
        if(validateFullName(ROSTER_TAB) || validateDateOfBirth(ROSTER_TAB)) {
            return;
        }

        Profile searchProfile = new Profile(
                lastNameTextField.getText().trim(),
                firstNameTextField.getText().trim(),
                new Date(
                        dateOfBirthPicker.getValue().getMonth().getValue()
                        + "/" + dateOfBirthPicker.getValue().getDayOfMonth()
                        + "/" + dateOfBirthPicker.getValue().getYear()
                )
        );

        Student student;
        if ((student = getStudent(roster, searchProfile)) == null) {
            consoleTextArea.setText(searchProfile + " is not in the roster.");
            return;
        }

        EnrollStudent enrollStudent;
        if ((enrollStudent = getEnrollStudent(enrollment, searchProfile)) != null) {
            consoleTextArea.setText(searchProfile + " is currently enrolled!");
            return;
        }

        try {
            if (roster.remove(student)) {
                consoleTextArea.setText(student.getProfile().toString() + " removed from the roster.");
            }
        } catch (Exception e) {
            consoleTextArea.setText("Fatal Error: Student could not be removed, please try again.");
        }
    }

    /**
     * This is an event handler method used to handle a button click on the "Change Major" button
     * in the roster tab. We first validate the information of the student we are trying to change
     * the major for and then check to see if they are registered in the roster. If both are valid
     * then we update their major to the selected major. We display any error messages that are
     * applicable.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleChangeMajorButtonClick (ActionEvent event) {
        if(validateFullName(ROSTER_TAB) || validateDateOfBirth(ROSTER_TAB)) {
            return;
        }

        Profile searchProfile = new Profile(
                lastNameTextField.getText().trim(),
                firstNameTextField.getText().trim(),
                new Date(
                        dateOfBirthPicker.getValue().getMonth().getValue()
                                + "/" + dateOfBirthPicker.getValue().getDayOfMonth()
                                + "/" + dateOfBirthPicker.getValue().getYear()
                )
        );

        Student student;
        if((student = getStudent(roster, searchProfile)) == null) {
            consoleTextArea.setText(searchProfile + " is not in the roster.");
            return;
        }
        try {
            RadioButton selectedMajorRadioButton = (RadioButton) majorGroup.getSelectedToggle();
            if (selectedMajorRadioButton != null) {
                String selectedOption = selectedMajorRadioButton.getText();
                Major major = determineMajor(selectedOption);
                if (major != null) {
                    student.setMajor(major);
                    consoleTextArea.setText(searchProfile + " major changed to " + major.name());
                }
            } else {
                consoleTextArea.setText("Major Selection Invalid: no major is selected!");
            }
        } catch (Exception e) {
            consoleTextArea.setText("Fatal Error: Student major could not be changed, please try again.");
        }
    }

    /**
     * This is an event handler method used to handle a button click on the "Load from File" button
     * in the roster tab. When this button is clicked, we open a file explorer window prompting the
     * user to select a text file containing a roster of students that need to be imported. Once a
     * file is selected we import the students into the roster array with a helper method.
     * We display any error messages that are applicable.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleLoadFromFileButtonClick (ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fc = new FileChooser();

        fc.setTitle("Select Roster Text File");
        fc.setInitialDirectory(new File(System.getProperty("user.home")));

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Text files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(extFilter);

        File file = fc.showOpenDialog(stage);
        if (file != null) {
            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String studentInformation; Student student;
                    if ((studentInformation = loadFromFileHelper(sc.nextLine())) == null) return;
                    if ((student = createStudent(studentInformation)) == null) return;
                    try {
                        roster.add(student);
                    } catch (Exception e) {
                        consoleTextArea.setText("Fatal Error: Student could not be added, please try again.");
                    }
                }
                consoleTextArea.setText("Students loaded to the roster.");
                fileNameTextField.setText(file.getName());
                sc.close();
            } catch (FileNotFoundException e) {
                consoleTextArea.setText("File Not Found: " + file.getName());
            }
        }
    }

    /**
     * This is an event handler method used to handle a button click on the "Enroll" button
     * in the Enrollment tab. We first validate all the necessary fields needed to add the student
     * to the enrollment array, then we check to see if the student is registered into the roster.
     * If they are, then we validate their enrollment credits. We then try to add the student
     * to the enrollment array, displaying any error messages that are applicable. Note that, we
     * can update the enrolled credits by simply rentering the information and clicking "Enroll"
     * with the new value for enrolled credits.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleEnrollButtonClick (ActionEvent event) {
        if(validateFullName(ENROLLMENT_TAB) || validateDateOfBirth(ENROLLMENT_TAB)) {
            return;
        }
        if(creditsEnrolledTextField.getText().trim().isEmpty()) {
            consoleTextArea.setText("Enroll Error: credits enrolled field is empty!");
            return;
        }

        Profile searchProfile = new Profile(
                lastNameTextField_E.getText().trim(),
                firstNameTextField_E.getText().trim(),
                new Date(
                        dateOfBirthPicker_E.getValue().getMonth().getValue()
                                + "/" + dateOfBirthPicker_E.getValue().getDayOfMonth()
                                + "/" + dateOfBirthPicker_E.getValue().getYear()
                )
        );

        Student student;
        if((student = getStudent(roster, searchProfile)) == null) {
            consoleTextArea.setText(searchProfile + " is not in the roster.");
            return;
        }

        String creditsEnrolled = creditsEnrolledTextField.getText().trim();
        if (!(validateEnrollmentCredits(student, creditsEnrolled))) {
            return;
        }

        EnrollStudent enrollStudent = new EnrollStudent(
                student.getProfile(),
                Integer.parseInt(creditsEnrolled));

        enrollment.add(enrollStudent);

        consoleTextArea.setText(student.getProfile() + " enrolled "
                + creditsEnrolled + " credits");
    }

    /**
     * This is an event handler method used to handle a button click on the "Drop" button
     * in the Enrollment tab. We first validate all the necessary fields needed to drop the
     * student from the enrollment array, then we check to see if the student is registered
     * into the enrollment array. If they are, then we remove them from the enrollment array,
     * displaying any error messages that are applicable.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleDropButtonClick (ActionEvent event) {
        if(validateFullName(ENROLLMENT_TAB) || validateDateOfBirth(ENROLLMENT_TAB)) {
            return;
        }

        Profile searchProfile = new Profile(
                lastNameTextField_E.getText().trim(),
                firstNameTextField_E.getText().trim(),
                new Date(
                        dateOfBirthPicker_E.getValue().getMonth().getValue()
                                + "/" + dateOfBirthPicker_E.getValue().getDayOfMonth()
                                + "/" + dateOfBirthPicker_E.getValue().getYear()
                )
        );

        EnrollStudent enrollStudent = new EnrollStudent(searchProfile, DUMMY_VALUE_CREDITS);

        if (enrollment.getEnrollmentSize() == MIN_ENROLLMENT_SIZE) {
            consoleTextArea.setText("Enrollment is empty!");
            return;
        }
        if (!enrollment.contains(enrollStudent)) {
            consoleTextArea.setText(searchProfile + " is not enrolled.");
            return;
        }

        enrollment.remove(enrollStudent);
        consoleTextArea.setText(searchProfile + " dropped.");
    }

    /**
     * This is an event handler method used to handle a button click on the "Award" button
     * in the Scholarship tab. We first validate all the necessary fields needed to award the
     * student with a scholarship. Then we check to see if the student entered is eligibible for
     * a scholarship based on their resident status and enrollment status. If both are valid,
     * We then try to update the scholarship amount for the student, displaying any error messages
     * that are applicable. Note that, we can update the scholarship amount by simply re-entering
     * the information and clicking "Award" with the new value for scholarship amount.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleAwardButtonClick(ActionEvent event) {
        if (validateFullName(SCHOLARSHIP_TAB) || validateDateOfBirth(SCHOLARSHIP_TAB)) {
            return;
        }
        if (scholarshipAmountTextField.getText().trim().isEmpty()) {
            consoleTextArea.setText("Scholarship Award Error: scholarship amount field is empty!");
            return;
        }

        Profile searchProfile = new Profile(
                lastNameTextField_S.getText().trim(),
                firstNameTextField_S.getText().trim(),
                new Date(
                        dateOfBirthPicker_S.getValue().getMonth().getValue()
                                + "/" + dateOfBirthPicker_S.getValue().getDayOfMonth()
                                + "/" + dateOfBirthPicker_S.getValue().getYear()
                )
        );

        Resident student;
        if ((student = getResidentStudent(searchProfile, roster)) == null) {
            return;
        }
        if (!(isScholarshipEligible(student, enrollment))) {
            return;
        }
        try {
            int scholarshipAmount = Integer.parseInt(scholarshipAmountTextField.getText().trim());
            if (scholarshipAmount > MAX_SCHOLARSHIP_AMT || scholarshipAmount <= MIN_SCHOLARSHIP_AMT) {
                consoleTextArea.setText(scholarshipAmount + ": invalid amount.");
                return;
            }
            student.setScholarship(scholarshipAmount);
            consoleTextArea.setText(student.getProfile() + ": scholarship amount updated.");

        } catch (NumberFormatException e) {
            consoleTextArea.setText("Amount is not an integer.");
        }
    }

    /**
     * This is an event handler method used to handle a button click on the "Print" button
     * in the Print tab. We retrieve the selections for print type and school name (if applicable)
     * choice boxes and use them to determine which executePrint...() method to use in order to
     * output students.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handlePrintButtonClick(ActionEvent event) {
        if (sortTypeChoiceBox.getSelectionModel().isEmpty()) {
            consoleTextArea.setText("Print Error: please choose print type!");
            return;
        }
        if (schoolChoiceBox.isVisible() && schoolChoiceBox.getSelectionModel().isEmpty()) {
            consoleTextArea.setText("Print Error: please choose a school!");
            return;
        }

        printTextArea.setText(EMPTY_STRING);
        consoleTextArea.setText(EMPTY_STRING);
        String printType = sortTypeChoiceBox.getSelectionModel().getSelectedItem();

        switch (printType) {
            case "Roster: Profile - Ascending" -> executePrintRosterProfile(roster);
            case "Roster: Standing - Ascending" -> executePrintRosterStanding(roster);
            case "Roster: Major, School - Ascending" -> executePrintRosterMajorSchool(roster);
            case "Roster: Select School" -> {
                String school = schoolChoiceBox.getSelectionModel().getSelectedItem();
                executePrintRosterSelectSchool(roster, school);
            }
            case "Enrollment" -> executePrintEnrollment(enrollment);
            case "Tuition Due" -> executePrintTuitionDue(roster, enrollment);
        }
    }

    /**
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleEndCurrentSemesterButtonClick (ActionEvent event) {
        if (enrollment.getEnrollmentSize() == MIN_ENROLLMENT_SIZE) {
            consoleTextArea.setText("Semester Error: enrollment is empty!");
            return;
        }
        if (roster.getRosterSize() == MIN_ROSTER_SIZE) {
            consoleTextArea.setText("Semester Error: student roster is empty!");
            return;
        }

        for (int i = 0; i < enrollment.getEnrollmentSize(); i++) {
            EnrollStudent enrollStudent = enrollment.getEnrollStudents()[i];
            updateCompletedCredits(enrollStudent.getProfile(), roster,
                    enrollStudent.getCreditsEnrolled());
        }

        consoleTextArea.setText("Credit completed has been updated.");
        printTextArea_Sm.appendText("** list of students eligible for graduation **" + "\n");

        for(int i = 0; i < roster.getRosterSize(); i++) {
            Student student = roster.getRoster()[i];
            if(student.getCreditCompleted() >= GRAD_ELIGIBLE_CREDITS) {
                printTextArea_Sm.appendText(student + "\n");
            }
        }

        enrollment = new Enrollment();
    }

    /**
     * This is an event handler method used to handle a button click on the "Clear" button
     * in the roster tab. It clears out all text fields and resets all date/button selections
     * within the roster tab.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleClearRosterButtonClick(ActionEvent event) {
        majorGroup.selectToggle(baitRadioButton);
        statusGroup.selectToggle(residentRadioButton);
        stateGroup.selectToggle(null);
        nonResStatusGroup.selectToggle(null);
        studyAbroadCheckBox.setSelected(false);

        firstNameTextField.setText(EMPTY_STRING);
        lastNameTextField.setText(EMPTY_STRING);
        dateOfBirthPicker.setValue(null);
        creditsCompletedTextField.setText(EMPTY_STRING);

        fileNameTextField.setText(EMPTY_STRING);
    }

    /**
     * This is an event handler method used to handle a button click on the "Clear" button
     * in the enrollment tab. It clears out all text fields and resets all date/button selections
     * within the enrollment tab.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleClearEnrollmentButtonClick(ActionEvent event) {
        firstNameTextField_E.setText(EMPTY_STRING);
        lastNameTextField_E.setText(EMPTY_STRING);
        dateOfBirthPicker_E.setValue(null);
        creditsEnrolledTextField.setText(EMPTY_STRING);
    }

    /**
     * This is an event handler method used to handle a button click on the "Clear" button
     * in the scholarship tab. It clears out all text fields and resets all date/button selections
     * within the scholarship tab.
     *
     * @param event ActionEvent that represents a button click.
     */
    @FXML
    private void handleClearScholarshipButtonClick(ActionEvent event) {
        firstNameTextField_S.setText(EMPTY_STRING);
        lastNameTextField_S.setText(EMPTY_STRING);
        dateOfBirthPicker_S.setValue(null);
        scholarshipAmountTextField.setText(EMPTY_STRING);
    }

    /**
     * This method performs any initialization/setup methods upon loading the
     * fxml elements into the window scene.
     */
    @FXML
    private void initialize() {
        consoleTextArea.setText("Tuition Manager running...");

        roster = new Roster();
        enrollment = new Enrollment();

        initPrintChoiceBoxes();

        addListenerStatusSelectionGroup();
        addListenerNonResStatusSelectionGroup();
        addListenerStateSelectionGroup();

        addListenerPrintTypeChoiceBox();
    }
}