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
        char charIndex = getPieceType().equals(PieceType.KNIGHT)
                ? pieceType.toString().toLowerCase().charAt(1)
                : pieceType.toString().toLowerCase().charAt(0);
        setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/"
                + pieceColour.toString().toLowerCase().charAt(0)
                + charIndex
                + ".png")));
    }

    public abstract boolean move(double newX, double newY);

    public abstract ArrayList<Square> getLegalMoves();

    public boolean squareIsNotOccupied(double x, double y) {
        boolean isOccupied = true;
        for (Piece piece : chessboard.getPiecePos()) {
            if (Math.round(x) == Math.round(piece.getCurrentX()) && Math.round(y) == Math.round(piece.getCurrentY())) {
                isOccupied = false;
                break;
            }
        }
        return isOccupied;
    }

    public double findUpSquare(double initialSquareY) {
        int nextSquareIndex = pieceColour.equals(PieceColour.WHITE) ? -1 : 1;
        double[] possibleCoordinates = chessboard.getPossibleXAndYCoordinates();
        double upSquareY = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            if (Math.round(initialSquareY) == Math.round(possibleCoordinates[i]) && i <= 7 && i > 0) {
                upSquareY = i == possibleCoordinates.length - 1
                        ? possibleCoordinates[i]
                        : possibleCoordinates[i + nextSquareIndex];
                break;
            }
        }
        return upSquareY;
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
