package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Rook extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 8;

    public Rook(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.ROOK, pieceColour, square, SQUARES_IT_CAN_MOVE, board);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves(boolean filterCoveredSquares) {
        ArrayList<Square> legalMoves = new ArrayList<>();
        legalMoves.addAll(getVerticalAttackPattern(filterCoveredSquares));
        legalMoves.addAll(getHorizontalAttackPattern(filterCoveredSquares));
        return legalMoves;
    }
}
