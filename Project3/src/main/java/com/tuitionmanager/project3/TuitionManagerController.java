package com.tuitionmanager.project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

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

    /**
     * JavaFX Elements from Roster Tab
     */
    @FXML
    private TextField firstNameTextField, lastNameTextField, creditsCompletedTextField,
                      fileNameTextField;
    @FXML
    private Button addButton, removeButton, changeMajorButton, loadFromFileButton;

    @FXML
    private RadioButton baitRadioButton, csRadioButton, eceRadioButton, itiRadioButton,
                        mathRadioButton;
    @FXML
    private RadioButton residentRadioButton, nonResidentRadioButton;

    @FXML
    private RadioButton triStateRadioButton, internationalRadioButton;

    @FXML
    private RadioButton nyStateRadioButton, ctStateRadioButton;

    @FXML
    private ToggleGroup statusGroup, nonResStatusGroup, stateGroup, majorGroup;

    @FXML
    private HBox triStateSelectionView, internationalSelectionView;

    @FXML
    private CheckBox studyAbroadCheckBox;

    @FXML
    private DatePicker dateOfBirthPicker;

    /**
     * Persistent JavaFX Elements
     */
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
            consoleTextArea.setText("DOB invalid: no date selected!");
            return false;
        }

        String dateString = "" + dateOfBirthPicker.getValue().getDayOfMonth()
                            + "/" + dateOfBirthPicker.getValue().getDayOfMonth()
                            + "/" + dateOfBirthPicker.getValue().getYear();

        Date dob = new Date(dateString), current = new Date();

        if (dob.equals(current) || dob.compareTo(current) > 0) {
            consoleTextArea.setText("DOB invalid: cannot be current or future date!");
            return false;
        }

        int age = current.getYear() - dob.getYear();
        if (current.getMonth() < dob.getMonth()) {
            age--;
        } else if (current.getMonth() == dob.getMonth() && current.getDay() < dob.getDay()) {
            age--;
        }

        if (age < MIN_AGE) {
            consoleTextArea.setText("DOB invalid: " + dob + " younger than 16 years old.");
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
            if(major.name().equals(inputMajor.toUpperCase())) {
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

    /**
     * needs comments
     * @param e
     */
    @FXML
    private void handleAddButtonClick (ActionEvent e) {
        if(!validateFullName() || !validateDateOfBirth() || !validateCreditsCompleted()) {
            return;
        }

        int status;
        if ((status = validateStudentStatusSelection()) == -1) {
            return;
        }

        String studentInformation = firstNameTextField.getText()
                + " " + lastNameTextField.getText()
                + " " + dateOfBirthPicker.getValue().getMonth()
                + "/" + dateOfBirthPicker.getValue().getDayOfMonth()
                + "/" + dateOfBirthPicker.getValue().getYear()
                + " " + creditsCompletedTextField.getText()
                + " " + ((RadioButton) majorGroup.getSelectedToggle()).getText()
                + " " + status;

        consoleTextArea.setText(studentInformation);

    }

    /**
     * needs comments
     * @param e
     */
    private void handleRemoveButtonClick (ActionEvent e) {

    }

    /**
     * needs comments
     * @param e
     */
    private void handleChangeMajorButtonClick (ActionEvent e) {

    }

    /**
     * needs comments
     * @param e
     */
    private void handleLoadFromFileButtonClick (ActionEvent e) {

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

    }
}