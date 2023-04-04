package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

public class King extends Piece {

    public King(PieceColour pieceColour, double x, double y, Chessboard chessboard) {
        super(pieceColour, x, y, chessboard);
        this.pieceType = PieceType.KING;
        createPiece();
    }

    @Override
    public void legalMove() {

    }
}
