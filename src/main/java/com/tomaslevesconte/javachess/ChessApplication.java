package com.tomaslevesconte.javachess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ChessApplication extends Application {

    public final static double WINDOW_SIZE = 800.0f;

    @Override
    public void start(Stage stage) {
        AnchorPane anchorPane = new AnchorPane();
        new Board(anchorPane, WINDOW_SIZE);
        Scene scene = new Scene(anchorPane, WINDOW_SIZE, WINDOW_SIZE);
        stage.setTitle("Chess");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}