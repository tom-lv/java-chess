package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import javafx.scene.shape.Rectangle;

public abstract class Piece {
    protected PieceType pieceType;
    protected PieceColour pieceColour;
    protected double x;
    protected double y;
    protected Chessboard chessboard;

    public Piece(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard) {
        this.pieceColour = pieceColour;
        this.x = positionX;
        this.y = positionY;
        this.chessboard = chessboard;
    }

    public abstract Rectangle createPiece();

    public PieceType getPieceType() {
        return pieceType;
    }

    public PieceColour getPieceColour() {
        return pieceColour;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
