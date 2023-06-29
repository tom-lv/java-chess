package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.Colour;
import com.tomaslevesconte.javachess.enums.Type;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;
import java.util.Objects;

public class Knight extends Piece {

    private static final int squaresItCanMove = 1;

    public Knight(Colour colour, Square square, Board board) {
        super(Type.KNIGHT, colour, board.getSquareSize(), square, squaresItCanMove, board);
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

        double sqrSize = getBoard().getSquareSize();

        ArrayList<Square> attackPattern = new ArrayList<>();

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
                    && getBoard().getPiece(attackSquare).getColour().equals(getColour())));
        } else {
            attackPattern.removeIf(Objects::isNull);
        }

        return attackPattern;
    }
}
