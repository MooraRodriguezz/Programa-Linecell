module com.example.programapapa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.desktop;

    opens com.example.programapapa to javafx.fxml;
    exports com.example.programapapa;

    opens Controladores to javafx.fxml;
    exports Controladores;

    opens modelo to javafx.base; // <-- ESTA ES LA LÍNEA CORREGIDA
    exports BDD;
    exports utils;
}