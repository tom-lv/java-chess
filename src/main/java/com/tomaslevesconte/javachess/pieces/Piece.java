package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public abstract class Piece {
    protected PieceType pieceType;
    protected PieceColour pieceColour;
    protected Rectangle pieceRect;
    protected double x;
    protected double y;
    protected Chessboard chessboard;
    protected boolean isSelected;


    public Piece(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard) {
        this.pieceColour = pieceColour;
        this.x = positionX;
        this.y = positionY;
        this.chessboard = chessboard;
    }

    protected void createPiece() {
        Rectangle piece = new Rectangle(chessboard.getSquareWidth(), chessboard.getSquareHeight());
        piece.setSmooth(false);
        piece.setLayoutX(x);
        piece.setLayoutY(y);
        piece.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/" +
                pieceColour.toString().toLowerCase().charAt(0) + pieceType.toString().toLowerCase().charAt(0) + ".png")));
        this.pieceRect = piece;
    }

    public abstract void legalMove();

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
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

    public Rectangle getPieceRect() {
        return pieceRect;
    }
}
