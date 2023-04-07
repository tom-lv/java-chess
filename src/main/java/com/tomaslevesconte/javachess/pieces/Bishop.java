package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(pieceColour, square, chessboard);
        setPieceType(PieceType.BISHOP);
        createPiece();
    }

    @Override
    public boolean move(double newX, double newY) {
        return false;
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        return null;
    }
}
