module com.example.rucafe {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rucafe to javafx.fxml;
    exports com.example.rucafe;
    exports com.example.rucafe.utilities;
    opens com.example.rucafe.utilities to javafx.fxml;
    exports com.example.rucafe.model;
    opens com.example.rucafe.model to javafx.fxml;
    exports com.example.rucafe.controller;
    opens com.example.rucafe.controller to javafx.fxml;
}