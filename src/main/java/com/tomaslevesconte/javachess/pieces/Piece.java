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
    private boolean isSelected;

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
        double x = Math.round(squareX);
        double y = Math.round(squareY);
        for (Piece piece : chessboard.getPiecePositions()) {
            double currentX = Math.round(piece.getCurrentX());
            double currentY = Math.round(piece.getCurrentY());
            if (x == currentX && y == currentY) {
                occupiedStatus = true;
                break;
            }
        }
        return occupiedStatus;
    }

    public double findYAxisSquares(double squareY) {
        int nextSquareIndex = pieceColour.equals(PieceColour.WHITE) ? -1 : 1;
        double[] possibleCoordinates = chessboard.getPossibleXAndYCoordinates();
        double targetSquareY = 0.0;
        int i = 0;
        for (double coordinate : possibleCoordinates) {
            int startSquareY = (int) Math.round(squareY);
            int arraySquareY = (int) Math.round(coordinate);
            if (startSquareY == arraySquareY && i <= 7 && i > 0) {
                targetSquareY = i == possibleCoordinates.length - 1
                        ? possibleCoordinates[i]
                        : possibleCoordinates[i + nextSquareIndex];
                break;
            }
            i++;
        }
        return targetSquareY;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }
}
