package com.tomaslevesconte.javachess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChessApplication extends Application {

    public final static double BOARD_SIZE = 850.0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChessApplication.class.getResource("chess-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), BOARD_SIZE, BOARD_SIZE);
        stage.setTitle("Chess");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}