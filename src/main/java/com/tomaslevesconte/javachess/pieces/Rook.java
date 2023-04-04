package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

public class Rook extends Piece {

    public Rook(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard) {
        super(pieceColour, positionX, positionY, chessboard);
        this.pieceType = PieceType.ROOK;
        createPiece();
    }

    @Override
    public void legalMove() {

    }
}
