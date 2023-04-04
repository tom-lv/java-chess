package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public abstract class Piece extends Rectangle {
    
    protected PieceType pieceType;
    protected PieceColour pieceColour;
    protected double positionX;
    protected double positionY;
    protected Chessboard chessboard;
    protected ArrayList<Piece> pieceList;
    protected boolean isSelected;

    public Piece(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard, ArrayList<Piece> pieceList) {
        this.pieceColour = pieceColour;
        this.positionX = positionX;
        this.positionY = positionY;
        this.chessboard = chessboard;
        this.pieceList = pieceList;
    }

    protected void createPiece() {
        this.setWidth(chessboard.getSquareSize());
        this.setHeight(chessboard.getSquareSize());
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
        this.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/" +
                pieceColour.toString().toLowerCase().charAt(0) + pieceType.toString().toLowerCase().charAt(0) + ".png")));
    }

    public abstract void move();

    public abstract void getLegalMoves();

    protected boolean isSquareOccupied(double x, double y) {
        boolean isOccupied = false;
        for (Piece piece : this.pieceList) {
            if (Math.round(x) == Math.round(piece.getPositionX()) && Math.round(y) == Math.round(piece.getPositionY())) {
                isOccupied = true;
                break;
            }
        }
        return isOccupied;
    }

    protected double findSquareInFront(int numOfSquares, double currentSquareY) {
        int index = pieceColour.equals(PieceColour.WHITE) ? -1 : 1;
        double[] possibleCoordinates = chessboard.getPossibleXAndYCoordinates();
        double squareInFront = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            if (Math.round(possibleCoordinates[i]) == Math.round(currentSquareY)) {
                squareInFront = possibleCoordinates[i + index];
                break;
            }
        }
        if (numOfSquares > 1) {
            return findSquareInFront(numOfSquares - 1, squareInFront);
        } else {
            return squareInFront;
        }
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public PieceType getPieceType() {
        return pieceType;
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
}
