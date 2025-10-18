module com.example.programapapa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    opens com.example.programapapa to javafx.fxml;
    exports com.example.programapapa;
}