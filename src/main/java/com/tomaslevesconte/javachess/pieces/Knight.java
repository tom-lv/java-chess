package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

public class Knight extends Piece {

    public Knight(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard) {
        super(pieceColour, positionX, positionY, chessboard);
        this.pieceType = PieceType.KNIGHT;
        createPiece();
    }

    @Override
    public void legalMove() {

    }
}
