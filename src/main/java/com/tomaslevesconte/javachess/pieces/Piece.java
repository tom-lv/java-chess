package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

import com.tomaslevesconte.javachess.Square;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public abstract class Piece extends Rectangle {

    private static final String IMAGE_PATH = "com/tomaslevesconte/javachess/pieces/";
    private static final String IMAGE_TYPE = ".png";
    
    private PieceType pieceType;
    private final PieceColour pieceColour;
    private double currentX;
    private double currentY;
    private final Chessboard chessboard;

    public Piece(PieceColour pieceColour, Square square, Chessboard chessboard) {
        this.pieceColour = pieceColour;
        this.currentX = square.getX(chessboard.getSquareSize());
        this.currentY = square.getY(chessboard.getSquareSize());
        this.chessboard = chessboard;
    }

    protected void createPiece() {
        setCursor(Cursor.OPEN_HAND);
        setWidth(chessboard.getSquareSize());
        setHeight(chessboard.getSquareSize());
        setLayoutX(currentX);
        setLayoutY(currentY);
        char knightInitial = pieceType.toString().toLowerCase().charAt(1);
        char allInitial = pieceType.toString().toLowerCase().charAt(0);
        char colourInitial = pieceColour.toString().toLowerCase().charAt(0);
        char typeInitial = getPieceType().equals(PieceType.KNIGHT) ? knightInitial : allInitial;
        setFill(new ImagePattern(new Image(IMAGE_PATH + colourInitial + typeInitial + IMAGE_TYPE)));
    }

    public abstract boolean move(double newX, double newY);

    public abstract ArrayList<Square> getLegalMoves();

    public boolean isSquareOccupied(double squareX, double squareY) {
        boolean occupiedStatus = false;
        double argX = Math.round(squareX);
        double argY = Math.round(squareY);
        for (Piece piece : chessboard.getPiecePositions()) {
            double currentX = Math.round(piece.getCurrentX());
            double currentY = Math.round(piece.getCurrentY());
            if (argX == currentX && argY == currentY) {
                occupiedStatus = true;
                break;
            }
        }
        return occupiedStatus;
    }

    public double findYAxisSquares(double squareY) {
        int multiplier = pieceColour.equals(PieceColour.WHITE) ? -1 : 1;
        return findSquareForPawn(multiplier, squareY);
    }

    public double findSquareForPawn(int multiplier, double squareXY) {
        double[] possibleCoordinates = chessboard.getPossibleXAndYCoordinates();
        double targetSquareXY = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            int startSquareXY = (int) Math.round(squareXY);
            int arraySquareXY = (int) Math.round(possibleCoordinates[i]);
            if (startSquareXY == arraySquareXY && i > 0 && i < 7) {
                targetSquareXY = possibleCoordinates[i + multiplier];
                break;
            }
        }
        return targetSquareXY;
    }

    public double findYAxisSquares(boolean isUp, double squareY) {
        int multiplier = isUp ? -1 : 1;
        return findSquareForAll(multiplier, squareY);
    }

    public double findXAxisSquares(boolean isLeft, double squareX) {
        int multiplier = isLeft ? -1 : 1;
        return findSquareForAll(multiplier, squareX);
    }

    public double findSquareForAll(int multiplier, double squareXY) {
        double[] possibleCoordinates = chessboard.getPossibleXAndYCoordinates();
        double targetSquareXY = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            int startSquareXY = (int) Math.round(squareXY);
            int arraySquareXY = (int) Math.round(possibleCoordinates[i]);
            if (startSquareXY == arraySquareXY && i + multiplier > 0 && i + multiplier <= 7) {
                targetSquareXY = possibleCoordinates[i + multiplier];
                break;
            }
        }
        return targetSquareXY;
    }

    public double getCurrentX() {
        return currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public PieceColour getPieceColour() {
        return pieceColour;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }
}
