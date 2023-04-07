package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.pieces.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Chessboard {

    private static final byte TOTAL_NUM_OF_SQUARES = 64;
    private static final Color LIGHT_SQUARE_COLOUR = Color.web("#F2D8B5");
    private static final Color DARK_SQUARE_COLOUR = Color.web("#B78B64");

    private final double squareSize;
    private final AnchorPane anchorPane;
    private final ArrayList<Piece> piecePositions = new ArrayList<>();

    public Chessboard(double boardSize, AnchorPane anchorPane) {
        this.squareSize = boardSize / Math.sqrt(TOTAL_NUM_OF_SQUARES);
        this.anchorPane = anchorPane;
        createBoard();
    }

    public void createBoard() {
        double x = 0;
        double y = 0;
        for (int i = 0; i < Math.sqrt(TOTAL_NUM_OF_SQUARES); i++) {
            for (int j = 0; j < Math.sqrt(TOTAL_NUM_OF_SQUARES); j++) {
                Rectangle rectangle = new Rectangle(x, y, squareSize, squareSize);
                rectangle.setSmooth(false); // Remove antialiasing
                if ((i+j) % 2 == 0) {
                    rectangle.setFill(Chessboard.LIGHT_SQUARE_COLOUR);
                } else {
                    rectangle.setFill(Chessboard.DARK_SQUARE_COLOUR);
                }
                getAnchorPane().getChildren().add(rectangle);
                x += rectangle.getWidth();
            }
            x = 0;
            y += squareSize;
        }
    }

    public double findClosestSquare(double input, double[] possibleCoordinates) {
        double result = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            if (input >= possibleCoordinates[i] && input <= possibleCoordinates[i+1] || input < 0) {
                result = possibleCoordinates[i];
                break;
            } else if (input > possibleCoordinates[possibleCoordinates.length - 1]) {
                result = possibleCoordinates[possibleCoordinates.length - 1];
                break;
            }
        }
        return result;
    }
    
    public double[] getPossibleXAndYCoordinates() {
        double[] possibleXAndYCoordinates = new double[(int) (Math.sqrt(Chessboard.TOTAL_NUM_OF_SQUARES))];
        for (int i = 0; i < possibleXAndYCoordinates.length; i++) {
            possibleXAndYCoordinates[i] = squareSize * i;
        }
        return possibleXAndYCoordinates;
    }

    public double getSquareSize() {
        return squareSize;
    }

    public ArrayList<Piece> getPiecePositions() {
        return piecePositions;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }
}
