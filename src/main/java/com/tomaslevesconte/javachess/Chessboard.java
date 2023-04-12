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

    private void createBoard() {
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

    public double[] getPossibleXAndYCoordinates() {
        double[] possibleXAndYCoordinates = new double[(int) (Math.sqrt(Chessboard.TOTAL_NUM_OF_SQUARES))];
        for (int i = 0; i < possibleXAndYCoordinates.length; i++) {
            possibleXAndYCoordinates[i] = squareSize * i;
        }
        return possibleXAndYCoordinates;
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

    public boolean isSquareOccupied(double squareX, double squareY) {
        boolean occupiedStatus = false;
        for (Piece piece : getPiecePositions()) {
            if (Math.round(squareX) == Math.round(piece.getCurrentX())
                    && Math.round(squareY) == Math.round(piece.getCurrentY())) {
                occupiedStatus = true;
                break;
            }
        }
        return occupiedStatus;
    }

    public double findNextVerticalSquare(PieceColour pieceColour, double startSquareY) {
        int multiplier = pieceColour.equals(PieceColour.WHITE) ? -1 : 1;
        return findSquare(multiplier, startSquareY);
    }

    public double findNextVerticalSquare(boolean isUp, double startSquareY) {
        int multiplier = isUp ? -1 : 1;
        return findSquare(multiplier, startSquareY);
    }

    public double findNextHorizontalSquare(boolean isLeft, double startSquareX) {
        int multiplier = isLeft ? -1 : 1;
        return findSquare(multiplier, startSquareX);
    }

    private double findSquare(int multiplier, double startSquareXY) {
        double[] possibleCoordinates = getPossibleXAndYCoordinates();
        double targetSquareXY = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            int squareXY = (int) Math.round(startSquareXY);
            int arraySquareXY = (int) Math.round(possibleCoordinates[i]);
            if (squareXY == arraySquareXY && i + multiplier > 0 && i + multiplier <= 7) {
                targetSquareXY = possibleCoordinates[i + multiplier];
                break;
            }
        }
        return targetSquareXY;
    }

    public double[] findNextDiagonal(boolean isUp, boolean isLeft, double[] startSquareXY) {
        int multiplierX = isLeft ? -1 : 1;
        int multiplierY = isUp ? -1 : 1;
        return findDiagonalSquare(multiplierX, multiplierY, startSquareXY);
    }

    private double[] findDiagonalSquare(int multiplierX, int multiplierY, double[] startSquareXY) {
        double nextSquareX = findSquare(multiplierX, startSquareXY[0]);
        double nextSquareY = findSquare(multiplierY, startSquareXY[1]);
        return new double[]{nextSquareX, nextSquareY};
    }

    //move isUp/isLeft
    public double[] findNextLSquare(boolean isVertical, boolean isLeft, boolean isUp, double[] startSquareXY) {
        int multiplierX = isLeft ? -1 : 1;
        int multiplierY = isUp ? -1 : 1;
        return isVertical
                ? findVerticalLSquare(multiplierX, multiplierY, startSquareXY)
                : findHorizontalLSquare(multiplierX, multiplierY, startSquareXY);
    }

    private double[] findVerticalLSquare(int multiplierX, int multiplierY, double[] startSquareXY) {
        double nextSquareX = findSquare(multiplierX, startSquareXY[0]);
        double firstSquareY = findSquare(multiplierY, startSquareXY[1]);
        double lastSquareY = findSquare(multiplierY, firstSquareY);
        return new double[]{nextSquareX, lastSquareY};
    }

    private double[] findHorizontalLSquare(int multiplierX, int multiplierY, double[] startSquareXY) {
        double firstSquareX = findSquare(multiplierX, startSquareXY[0]);
        double lastSquareX = findSquare(multiplierX, firstSquareX);
        double nextSquareY = findSquare(multiplierY, startSquareXY[1]);
        return new double[]{lastSquareX, nextSquareY};
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
