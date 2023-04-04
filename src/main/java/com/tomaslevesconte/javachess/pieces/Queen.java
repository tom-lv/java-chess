package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

public class Queen extends Piece {

    public Queen(PieceColour pieceColour, double x, double y, Chessboard chessboard) {
        super(pieceColour, x, y, chessboard);
        this.pieceType = PieceType.QUEEN;
        createPiece();
    }

    @Override
    public void legalMove() {

    }
}
