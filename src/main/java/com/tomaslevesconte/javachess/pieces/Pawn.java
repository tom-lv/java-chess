package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;


public class Pawn extends Piece {

    public Pawn(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard) {
        super(pieceColour, positionX, positionY, chessboard);
        this.pieceType = PieceType.PAWN;
        createPiece();
    }

    @Override
    public void legalMove() {

    }
}
