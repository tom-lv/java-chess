package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;

public class Bishop extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 8;

    public Bishop(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.BISHOP, pieceColour, square, SQUARES_IT_CAN_MOVE, board);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        return new ArrayList<>(getDiagonalAttackPattern(true));
    }

    @Override
    public ArrayList<Square> getLegalMoves(boolean ignoreCoveredSquares) {
        return new ArrayList<>(getDiagonalAttackPattern(ignoreCoveredSquares));
    }
}
