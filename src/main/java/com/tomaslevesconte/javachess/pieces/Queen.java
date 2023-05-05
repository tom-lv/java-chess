package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Queen extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 8;

    public Queen(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.QUEEN, pieceColour, square, SQUARES_IT_CAN_MOVE, board);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        legalMoves.addAll(getVerticalAttackPattern());
        legalMoves.addAll(getHorizontalAttackPattern());
        legalMoves.addAll(getDiagonalAttackPattern());
        return legalMoves;
    }
}
