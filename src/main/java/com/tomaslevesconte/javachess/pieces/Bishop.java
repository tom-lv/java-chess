package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.Colour;
import com.tomaslevesconte.javachess.enums.Type;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;

public class Bishop extends Piece {

    private static final int MAX_SQUARE_ADVANCE = 8;

    public Bishop(Colour colour, Square square, Board board) {
        super(Type.BISHOP, colour, square, MAX_SQUARE_ADVANCE, board);
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
