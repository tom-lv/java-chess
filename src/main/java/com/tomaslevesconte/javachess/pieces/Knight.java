package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;
import java.util.Objects;

public class Knight extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 1;

    public Knight(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.KNIGHT, pieceColour, square, SQUARES_IT_CAN_MOVE, board);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves(boolean filterCoveredSquares) {
        return new ArrayList<>(getAttackPattern(filterCoveredSquares));
    }

    private ArrayList<Square> getAttackPattern(boolean filterCoveredSquares) {
        ArrayList<Square> attackPattern = new ArrayList<>();
        double sqrSize = getBoard().getSquareSize();

        // Every Knight attack pattern
        attackPattern.add(Square.find(getPosX() - sqrSize, getPosY() - (sqrSize * 2), sqrSize));
        attackPattern.add(Square.find(getPosX() + sqrSize, getPosY() - (sqrSize * 2), sqrSize));
        attackPattern.add(Square.find(getPosX() - sqrSize, getPosY() + (sqrSize * 2), sqrSize));
        attackPattern.add(Square.find(getPosX() + sqrSize, getPosY() + (sqrSize * 2), sqrSize));
        attackPattern.add(Square.find(getPosX() - (sqrSize * 2), getPosY() - sqrSize, sqrSize));
        attackPattern.add(Square.find(getPosX() + (sqrSize * 2), getPosY() - sqrSize, sqrSize));
        attackPattern.add(Square.find(getPosX() - (sqrSize * 2), getPosY() + sqrSize, sqrSize));
        attackPattern.add(Square.find(getPosX() + (sqrSize * 2), getPosY() + sqrSize, sqrSize));

        if (filterCoveredSquares) {
            // Remove if square !exist, or if square is occupied by the same colour
            attackPattern.removeIf(attackSquare -> (attackSquare == null // If null (out of bounds)
                    || getBoard().isSquareOccupied(attackSquare)
                    && getBoard().getPiece(attackSquare).getPieceColour().equals(getPieceColour())));
        } else {
            attackPattern.removeIf(Objects::isNull);
        }

        return attackPattern;
    }
}
