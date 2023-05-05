package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Knight extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 1;

    public Knight(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.KNIGHT, pieceColour, square, SQUARES_IT_CAN_MOVE, board);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        return new ArrayList<>(getAttackPattern());
    }

    private ArrayList<Square> getAttackPattern() {
        ArrayList<Square> attackPattern = new ArrayList<>();
        double squareSize = getBoard().getSquareSize();

        // Every Knight attack pattern
        attackPattern.add(Square.find(getPosX() - squareSize, getPosY() - (squareSize * 2), squareSize));
        attackPattern.add(Square.find(getPosX() + squareSize, getPosY() - (squareSize * 2), squareSize));
        attackPattern.add(Square.find(getPosX() - squareSize, getPosY() + (squareSize * 2), squareSize));
        attackPattern.add(Square.find(getPosX() + squareSize, getPosY() + (squareSize * 2), squareSize));
        attackPattern.add(Square.find(getPosX() - (squareSize * 2), getPosY() - squareSize, squareSize));
        attackPattern.add(Square.find(getPosX() + (squareSize * 2), getPosY() - squareSize, squareSize));
        attackPattern.add(Square.find(getPosX() - (squareSize * 2), getPosY() + squareSize, squareSize));
        attackPattern.add(Square.find(getPosX() + (squareSize * 2), getPosY() + squareSize, squareSize));

        // Remove if square !exist, or if square is occupied by the same colour
        attackPattern.removeIf(attackSquare -> (attackSquare == null // If null (out of bounds)
                || getBoard().isSquareOccupied(attackSquare)
                && getBoard().getPiece(attackSquare).getPieceColour().equals(getPieceColour())));

        return attackPattern;
    }
}
