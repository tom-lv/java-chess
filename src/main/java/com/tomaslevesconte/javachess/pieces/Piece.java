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
    protected Rectangle pieceRect;
    protected double positionX;
    protected double positionY;
    protected Chessboard chessboard;
    protected ArrayList<Piece> pieceList = new ArrayList<>();
    protected boolean isSelected;


    public Piece(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard) {
        this.pieceColour = pieceColour;
        this.positionX = positionX;
        this.positionY = positionY;
        this.chessboard = chessboard;
    }

    protected void createPiece() {
        this.setWidth(chessboard.getSquareSize());
        this.setHeight(chessboard.getSquareSize());
        this.setSmooth(false);
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
        this.setFill(new ImagePattern(new Image("com/tomaslevesconte/javachess/pieces/" +
                pieceColour.toString().toLowerCase().charAt(0) + pieceType.toString().toLowerCase().charAt(0) + ".png")));
    }

    public abstract void legalMove();

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
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
