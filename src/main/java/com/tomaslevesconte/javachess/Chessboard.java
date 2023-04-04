package com.tomaslevesconte.javachess;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Chessboard {
    // Constant
    public final static byte TOTAL_NUM_OF_SQUARES = 64;
    public final static byte TOTAL_NUM_OF_RANKS = 8;
    public final static byte TOTAL_NUM_OF_FILES = 8;
    public static final Color LIGHT_SQUARE_COLOUR = Color.web("#F2D8B5");
    public static final Color DARK_SQUARE_COLOUR = Color.web("#B78B64");

    // Instance
    private final double squareWidth;
    private final double squareHeight;
    private final AnchorPane anchorPane;

    public Chessboard(double boardWidth, double boardHeight, AnchorPane anchorPane) {
        this.squareWidth = boardWidth / Chessboard.TOTAL_NUM_OF_RANKS;
        this.squareHeight = boardHeight / Chessboard.TOTAL_NUM_OF_FILES;
        this.anchorPane = anchorPane;
    }

    public void createBoard() {
        double x = 0;
        double y = 0;
        for (int i = 0; i < Chessboard.TOTAL_NUM_OF_RANKS; i++) {
            for (int j = 0; j < Chessboard.TOTAL_NUM_OF_FILES; j++) {
                Rectangle rectangle = new Rectangle(x, y, squareWidth, squareHeight);
                rectangle.setSmooth(false); // Remove antialiasing
                if ((i+j) % 2 == 0) {
                    rectangle.setFill(Chessboard.LIGHT_SQUARE_COLOUR);
                } else {
                    rectangle.setFill(Chessboard.DARK_SQUARE_COLOUR);
                }
                anchorPane.getChildren().add(rectangle);
                x += rectangle.getWidth();
            }
            x = 0;
            y += squareHeight;
        }
    }

    public double findClosestSquare(double input, double[] possibleCoordinates) {
        double result = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            if (input >= possibleCoordinates[i] & input <= possibleCoordinates[i+1] | input < 0) {
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
            if (squareWidth == squareHeight) {
                possibleXAndYCoordinates[i] = squareWidth * i;
            }
        }
        return possibleXAndYCoordinates;
    }

    public double getSquareWidth() {
        return squareWidth;
    }

    public double getSquareHeight() {
        return squareHeight;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }
}
