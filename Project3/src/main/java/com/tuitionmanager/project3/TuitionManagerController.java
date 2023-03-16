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
    private ToggleGroup statusGroup, nonResStatusGroup;

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
     * @param e
     */
    @FXML
    private void handleAddButtonClick (ActionEvent e) {
        if(!validateFullName() || !validateDateOfBirth() || !validateCreditsCompleted()) {
            return;
        }

        System.out.println("student can be added!");
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