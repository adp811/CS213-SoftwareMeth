module com.example.rucafe {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rucafe to javafx.fxml;
    exports com.example.rucafe;
}