package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;

public class Rook extends Piece {

    private static final int MAX_SQUARE_ADVANCE = 8;

    public Rook(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.ROOK, pieceColour, square, MAX_SQUARE_ADVANCE, board);
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
