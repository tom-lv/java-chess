package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.Colour;
import com.tomaslevesconte.javachess.enums.Type;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;

public class Rook extends Piece {

    private static final int MAX_SQUARE_ADVANCE = 8;

    public Rook(Colour colour, Square square, Board board) {
        super(Type.ROOK, colour, square, MAX_SQUARE_ADVANCE, board);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();

        legalMoves.addAll(getVerticalAttackPattern(false));
        legalMoves.addAll(getHorizontalAttackPattern(false));

        return legalMoves;
    }

    @Override
    public ArrayList<Square> getLegalMoves(boolean applyKingFilter) {
        ArrayList<Square> legalMoves = new ArrayList<>();

        legalMoves.addAll(getVerticalAttackPattern(applyKingFilter));
        legalMoves.addAll(getHorizontalAttackPattern(applyKingFilter));

        return legalMoves;
    }
}
