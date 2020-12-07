module com.marufeb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens com.marufeb to javafx.fxml, javafx.controls;
    opens com.marufeb.controllers to javafx.fxml, javafx.controls;

    exports com.marufeb;
    exports com.marufeb.models;
    exports com.marufeb.models.abstraction;
}