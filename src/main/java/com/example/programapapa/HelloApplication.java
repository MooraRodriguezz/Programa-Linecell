package com.example.programapapa;

import BDD.OrdenDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        OrdenDAO ordenDAO = new OrdenDAO();
        ordenDAO.inicializarBaseDeDatos();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("TallerVista.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);

        String css = this.getClass().getResource("estilo.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Gestión de Taller");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.show();
    }
}