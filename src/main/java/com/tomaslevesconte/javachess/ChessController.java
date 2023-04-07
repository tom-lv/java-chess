package com.tomaslevesconte.javachess;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ChessController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Chessboard(ChessApplication.BOARD_SIZE, anchorPane);
    }
}