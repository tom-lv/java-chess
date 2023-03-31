module com.tomaslevesconte.javachess {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tomaslevesconte.javachess to javafx.fxml;
    exports com.tomaslevesconte.javachess;
}