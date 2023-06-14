package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;
import java.util.Objects;

public class Knight extends Piece {

    private static final int MAX_SQUARE_ADVANCE = 1;

    public Knight(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.KNIGHT, pieceColour, square, MAX_SQUARE_ADVANCE, board);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        return new ArrayList<>(getAttackPattern(false));
    }

    @Override
    public ArrayList<Square> getLegalMoves(boolean applyKingFilter) {
        return new ArrayList<>(getAttackPattern(applyKingFilter));
    }

    private ArrayList<Square> getAttackPattern(boolean applyKingFilter) {
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

        if (!applyKingFilter) {
            // Remove if square !exist, or if square is occupied by the same colour
            attackPattern.removeIf(attackSquare -> (attackSquare == null // If null (out of bounds)
                    || getBoard().isSquareOccupied(attackSquare)
                    && getBoard().getPieceOnSquare(attackSquare).getPieceColour().equals(getPieceColour())));
        } else {
            attackPattern.removeIf(Objects::isNull);
        }

        return attackPattern;
    }
}
