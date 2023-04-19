package com.tomaslevesconte.javachess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ChessApplication extends Application {

    public final static double BOARD_SIZE = 850;

    @Override
    public void start(Stage stage) {
        AnchorPane anchorPane = new AnchorPane();
        new PieceBuilder(new Chessboard(ChessApplication.BOARD_SIZE, anchorPane));
        Scene scene = new Scene(anchorPane, BOARD_SIZE, BOARD_SIZE);
        stage.setTitle("Chess");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}