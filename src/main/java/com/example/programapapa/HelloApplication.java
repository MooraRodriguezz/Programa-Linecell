package com.example.programapapa;

import BDD.OrdenDAO;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        OrdenDAO ordenDAO = new OrdenDAO();

        ordenDAO.inicializarBaseDeDatos();

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Gestión de Taller");
        stage.setScene(scene);
        stage.show();
    }
}