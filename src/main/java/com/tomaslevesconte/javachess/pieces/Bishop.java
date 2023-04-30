package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Bishop extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 8;

    public Bishop(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(PieceType.BISHOP, pieceColour, square, SQUARES_IT_CAN_MOVE, chessboard);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        return new ArrayList<>(evaluateDiagonalSquares());
    }
}
