module org.example.homeassignment4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires mongo.java.driver;


    opens org.example.homeassignment4 to javafx.fxml;
    exports org.example.homeassignment4;
}