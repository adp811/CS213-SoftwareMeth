package com.tuitionmanager.project3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class TuitionManagerController {

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