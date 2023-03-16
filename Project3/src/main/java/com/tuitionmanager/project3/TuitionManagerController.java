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
    private TextArea consoleTextArea;


    /**
     * needs comments, regex
     * @return
     */
    private boolean validateFullName() {
        if (firstNameTextField.getText().isEmpty() && lastNameTextField.getText().isEmpty()) {
            consoleTextArea.setText("Name Invalid: missing first and last name!");
            return false;
        } else {
            if (firstNameTextField.getText().isEmpty()) {
                consoleTextArea.setText("Name Invalid: missing first name!");
                return false;
            }
            if (lastNameTextField.getText().isEmpty()) {
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
     * needs comments
     * @return
     */
    private boolean validateDateOfBirth() {
        if (dateOfBirthPicker.getValue() == null) {
            consoleTextArea.setText("DOB Invalid: no date selected!");
            return false;
        }

        String dateString = "" + dateOfBirthPicker.getValue().getDayOfMonth()
                            + "/" + dateOfBirthPicker.getValue().getDayOfMonth()
                            + "/" + dateOfBirthPicker.getValue().getYear();

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
        Major major = determineMajor(arrInfo[4]);

        int creditCompleted = Integer.parseInt(arrInfo[3]);
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
     * @param event
     */
    @FXML
    private void handleAddButtonClick (ActionEvent event) {
        if(!validateFullName() || !validateDateOfBirth() || !validateCreditsCompleted()) {
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
                + " " + creditsCompletedTextField.getText()
                + " " + ((RadioButton) majorGroup.getSelectedToggle()).getText()
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
        if(!validateFullName() || !validateDateOfBirth()) {
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
        if(!validateFullName() || !validateDateOfBirth()) {
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

        fc.setTitle("Select roster text file");
        fc.setInitialDirectory(new File(System.getProperty("user.home")));

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Text files (*.txt)", "*.txt");

        fc.getExtensionFilters().add(extFilter);

        File file = fc.showOpenDialog(stage);
        if (file != null) {
            try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
                // finish loading
            }
            System.out.println("Students loaded to the roster.");
            fileNameTextField.setText(file.getName());
            sc.close();

            } catch (FileNotFoundException e) {
                System.out.println("File Not Found: " + file.getName());
            }
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