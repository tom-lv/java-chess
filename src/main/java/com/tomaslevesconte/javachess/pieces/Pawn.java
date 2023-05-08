package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 2;

    public Pawn(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.PAWN, pieceColour, square, SQUARES_IT_CAN_MOVE, board);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves(boolean filterCoveredSquares) {
        ArrayList<Square> legalMoves = new ArrayList<>();

        legalMoves.addAll(getMovePattern()); // Add move patterns
        legalMoves.addAll(getAttackPattern(filterCoveredSquares)); // Add attack patterns

        return legalMoves;
    }

    private ArrayList<Square> getMovePattern() {
        ArrayList<Square> movePattern = new ArrayList<>();
        double sqrSize = getBoard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -sqrSize : sqrSize;

        // Every pawn move pattern
        movePattern.add(Square.find(getPosX(), getPosY() + multiplier, sqrSize));
        movePattern.add(Square.find(getPosX(), getPosY() + (multiplier * 2), sqrSize));

        // Remove if square !exist, or if square is occupied, or if square == 2nd square and pawn has moved
        movePattern.removeIf(moveSquare -> (moveSquare == null
                || getBoard().isSquareOccupied(moveSquare))
                || moveSquare.equals(movePattern.get(1))
                && hasMoved());

        return movePattern;
    }

    private ArrayList<Square> getAttackPattern(boolean filterCoveredSquares) {
        ArrayList<Square> attackPattern = new ArrayList<>();
        double sqrSize = getBoard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -sqrSize : sqrSize;

        // Every pawn attack pattern
        attackPattern.add(Square.find(getPosX() - sqrSize, getPosY() + multiplier, sqrSize));
        attackPattern.add(Square.find(getPosX() + sqrSize, getPosY() + multiplier, sqrSize));

        if (filterCoveredSquares) {
            // Remove if square !exist, or if square is !occupied, or if square is occupied by the same colour
            attackPattern.removeIf(attackSquare -> (attackSquare == null // If null (out of bounds)
                    || !getBoard().isSquareOccupied(attackSquare)
                    || getBoard().isSquareOccupied(attackSquare)
                    && getBoard().getPiece(attackSquare).getPieceColour().equals(getPieceColour())));
        } else {
            attackPattern.removeIf(Objects::isNull);
        }

        return attackPattern;
    }
}
