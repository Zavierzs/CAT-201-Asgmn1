module com.example.librarysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.librarysystem to javafx.fxml;
    exports com.example.librarysystem;
}