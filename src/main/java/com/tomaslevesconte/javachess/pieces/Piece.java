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

    public boolean move(double newX, double newY) {
        boolean isMoveLegal = false;
        for (Square legalMove : getLegalMoves()) {
            double legalX = Math.round(legalMove.getX(getChessboard().getSquareSize()));
            double legalY = Math.round(legalMove.getY(getChessboard().getSquareSize()));
            if (Math.round(newX) == legalX && Math.round(newY) == legalY) {
                setCurrentX(newX);
                setCurrentY(newY);
                isMoveLegal = true;
                break;
            }
        }
        return isMoveLegal;
    }

    public abstract ArrayList<Square> getLegalMoves();

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
