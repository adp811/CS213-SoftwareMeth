module com.tuitionmanager.project3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tuitionmanager.project3 to javafx.fxml;
    exports com.tuitionmanager.project3;
}