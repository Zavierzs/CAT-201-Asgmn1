module com.example.librarysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;


    opens com.example.librarysystem to javafx.fxml;
    exports com.example.librarysystem;
}