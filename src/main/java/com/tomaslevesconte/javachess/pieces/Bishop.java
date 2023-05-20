package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;

public class Bishop extends Piece {

    private static final int MAX_SQUARE_ADVANCE = 8;

    public Bishop(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.BISHOP, pieceColour, square, MAX_SQUARE_ADVANCE, board);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        return new ArrayList<>(getDiagonalAttackPattern(false));
    }

    @Override
    public ArrayList<Square> getLegalMoves(boolean applyKingFilter) {
        return new ArrayList<>(getDiagonalAttackPattern(applyKingFilter));
    }
}
