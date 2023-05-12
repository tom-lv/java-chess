package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;

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
        legalMoves.addAll(getVerticalAttackPattern(true));
        legalMoves.addAll(getHorizontalAttackPattern(true));
        legalMoves.addAll(getDiagonalAttackPattern(true));
        return legalMoves;
    }

    @Override
    public ArrayList<Square> getLegalMoves(boolean ignoreCoveredSquares) {
        ArrayList<Square> legalMoves = new ArrayList<>();
        legalMoves.addAll(getVerticalAttackPattern(ignoreCoveredSquares));
        legalMoves.addAll(getHorizontalAttackPattern(ignoreCoveredSquares));
        legalMoves.addAll(getDiagonalAttackPattern(ignoreCoveredSquares));
        return legalMoves;
    }
}
