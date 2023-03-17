package com.tuitionmanager.project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

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

    private Roster roster;
    private Enrollment enrollment;


    @FXML
    private TextField firstNameTextField, lastNameTextField, creditsCompletedTextField,
                      fileNameTextField;
    @FXML
    private RadioButton triStateRadioButton, nyStateRadioButton, ctStateRadioButton;

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
    private TextArea consoleTextArea;


    /**
     * needs comments, regex
     * @return
     */
    private boolean validateFullName(int tab) {
        TextField firstName;
        TextField lastName;

        if (tab == 1) {
            firstName = firstNameTextField_E;
            lastName = lastNameTextField_E;
        } else if (tab == 2) {
            firstName = firstNameTextField_S;
            lastName = lastNameTextField_S;
        } else {
            firstName = firstNameTextField;
            lastName = lastNameTextField;
        }

        if (firstName.getText().trim().isEmpty() && lastName.getText().trim().isEmpty()) {
            consoleTextArea.setText("Name Invalid: missing first and last name!");
            return false;
        } else {
            if (firstName.getText().trim().isEmpty()) {
                consoleTextArea.setText("Name Invalid: missing first name!");
                return false;
            }
            if (lastName.getText().trim().isEmpty()) {
                consoleTextArea.setText("Name Invalid: missing last name!");
                return false;
            }
        }
        return true;
    }

    /**
     * needs comments
     * @return
     */
    private boolean validateDateOfBirth(int tab) {
        DatePicker dateOfBirth;

        if (tab == 1) {
            dateOfBirth = dateOfBirthPicker_E;
        } else if (tab == 2) {
            dateOfBirth = dateOfBirthPicker_S;
        } else {
            dateOfBirth = dateOfBirthPicker;
        }

        if (dateOfBirth.getValue() == null) {
            consoleTextArea.setText("DOB Invalid: no date selected!");
            return false;
        }

        String dateString = "" + dateOfBirth.getValue().getDayOfMonth()
                + "/" + dateOfBirth.getValue().getDayOfMonth()
                + "/" + dateOfBirth.getValue().getYear();

        Date dob = new Date(dateString), current = new Date();

        if (dob.equals(current) || dob.compareTo(current) > 0) {
            consoleTextArea.setText("DOB Invalid: cannot be current or future date!");
            return false;
        }

        int age = current.getYear() - dob.getYear();
        if (current.getMonth() < dob.getMonth()) {
            age--;
        } else if (current.getMonth() == dob.getMonth() && current.getDay() < dob.getDay()) {
            age--;
        }
        if (age < MIN_AGE) {
            consoleTextArea.setText("DOB Invalid: " + dob + " younger than 16 years old.");
            return false;
        }

        return true;
    }

    /**
     * needs comments
     * @return
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
     * This method takes care of validating the number of credits enrolled by Student.
     * It checks for any incorrect input such as negative or non-integer values. If there
     * is an error with the value given, then a message is displayed.
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
     * needs comments
     * @return
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
     * needs comments
     * @param data
     * @return
     */
    private Student createStudent(String studentInformation) {
        String[] arrInfo = studentInformation.split("\\s+");

        Profile profile = new Profile(arrInfo[1], arrInfo[0], new Date(arrInfo[2]));
        Major major = determineMajor(arrInfo[3]);

        int creditCompleted = Integer.parseInt(arrInfo[4]);
        int status = Integer.parseInt(arrInfo[5]);

        return switch (status) {
            case 0 -> new Resident(profile, major, creditCompleted, 0);
            case 1 -> new NonResident(profile, major, creditCompleted);
            case 2 -> new International(profile, major, creditCompleted, false);
            case 3 -> new International(profile, major, creditCompleted, true);
            case 4 -> new TriState(profile, major, creditCompleted, "NY");
            case 5 -> new TriState(profile, major, creditCompleted, "CT");
            default -> null;
        };
    }

    /** rework
     *
     * @param roster Roster object containing roster array that we are searching through
     * @param profile Profile object containing a student's information that we are trying
     *                to look for
     * @return Student object associated with the given Profile or null if it doesnt exist
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

    /** rework
     * This method takes care of retrieving a Resident student object specifically for
     * awarding a scholarship. It searches through the given Roster object array and
     * finds the profile associated with the inputted information. If the Resident student is
     * not found or the input information is tied to a different type of student, an error
     * message is displayed.
     *
     * @param searchProfile String array representing an "S" command argument body
     *                    ("S Aryan Patel 1/22/2002 10000")
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

    /** rework
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
     *         a scholarship or not
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
            System.out.println(roster.getRoster()[i].toString());
        }
    }

    /**
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

    /** rework
     *
     * @param roster Roster object which contains the roster array that we are printing from
     */
    private void executePrintRoster(Roster roster) {
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
     * needs comments
     * @param line
     * @return
     */
    private String loadFromFileHelper(String line) {
        String[] lineInfo = line.split(",");

        String status = lineInfo[0];
        String studentInformation = lineInfo[1]
                + " " + lineInfo[2]
                + " " + lineInfo[3]
                + " " + lineInfo[4]
                + " " + lineInfo[5];

        switch (status) {
            case "R" -> studentInformation = studentInformation.concat(" 0");
            case "N" -> studentInformation = studentInformation.concat(" 1");
            case "T" -> {
                String state = lineInfo[6];
                if (state.equalsIgnoreCase("NY")) {
                    studentInformation = studentInformation.concat(" 4");
                } else if (state.equalsIgnoreCase("CT")) {
                    studentInformation = studentInformation.concat(" 5");
                } else return null;
            }
            case "I" -> {
                if (lineInfo.length == 6) {
                    studentInformation = studentInformation.concat(" 2");
                } else if (lineInfo.length == 7) {
                    String isStudyAbroad = lineInfo[6];
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
     * needs comments
     * @param event
     */
    @FXML
    private void handleAddButtonClick (ActionEvent event) {
        if(!validateFullName(0) || !validateDateOfBirth(0) || !validateCreditsCompleted()) {
            return;
        }

        int status;
        if ((status = validateStudentStatusSelection()) == -1) {
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
     * needs comments
     * @param event
     */
    @FXML
    private void handleRemoveButtonClick (ActionEvent event) {
        if(!validateFullName(0) || !validateDateOfBirth(0)) {
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
            if (roster.remove(student)) {
                consoleTextArea.setText(student.getProfile().toString() + " removed from the roster.");
            }
        } catch (Exception e) {
            consoleTextArea.setText("Fatal Error: Student could not be removed, please try again.");
        }
    }

    /**
     * needs comments
     * @param event
     */
    @FXML
    private void handleChangeMajorButtonClick (ActionEvent event) {
        if(!validateFullName(0) || !validateDateOfBirth(0)) {
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

        executePrintRoster(roster);
    }

    /**
     * needs comments
     * @param event
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
     * needs comments
     * @param event
     */
    @FXML
    private void handleEnrollButtonClick (ActionEvent event) {
        if(!validateFullName(1) || !validateDateOfBirth(1)) {
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
     * needs comments
     * @param event
     */
    @FXML
    private void handleDropButtonClick (ActionEvent event) {
        if(!validateFullName(1) || !validateDateOfBirth(1)) {
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
     *
     * @param event
     */
    @FXML
    private void handleAwardButtonClick(ActionEvent event) {
        if (!validateFullName(2) || !validateDateOfBirth(2)) {
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
     * needs comments
     */
    @FXML
    private void initialize() {
        consoleTextArea.setText("Tuition Manager running...");

        roster = new Roster();
        enrollment = new Enrollment();

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
}