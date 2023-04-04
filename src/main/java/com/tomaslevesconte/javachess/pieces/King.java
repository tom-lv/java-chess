package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

import java.util.ArrayList;

public class King extends Piece {

    public King(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard, ArrayList<Piece> pieceList) {
        super(pieceColour, positionX, positionY, chessboard, pieceList);
        setPieceType(PieceType.KING);
        createPiece();
    }

    @Override
    public void move() {

    }

    @Override
    public void getLegalMoves() {

    }
}
