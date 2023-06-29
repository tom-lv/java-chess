package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.Colour;
import com.tomaslevesconte.javachess.enums.Type;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;

public class Queen extends Piece {

    private static final int squaresItCanMove = 8;

    public Queen(Colour colour, Square square, Board board) {
        super(Type.QUEEN, colour, board.getSquareSize(), square, squaresItCanMove, board);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();

        legalMoves.addAll(getVerticalAttackPattern(false));
        legalMoves.addAll(getHorizontalAttackPattern(false));
        legalMoves.addAll(getDiagonalAttackPattern(false));

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
