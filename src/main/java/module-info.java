module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.Controllers;
    opens com.example.demo.Controllers to javafx.fxml;
    exports com.example.demo.Models;
    opens com.example.demo.Models to javafx.fxml;
    exports com.example.demo.Interfaces;
    opens com.example.demo.Interfaces to javafx.fxml;
}