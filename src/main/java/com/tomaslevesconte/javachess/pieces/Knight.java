package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Knight extends Piece {

    // Constant
    private static final int SQUARES_IT_CAN_MOVE = 1;

    public Knight(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(PieceType.KNIGHT, pieceColour, square, SQUARES_IT_CAN_MOVE, chessboard);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        return new ArrayList<>(getKnightAttackPatterns());
    }

    private ArrayList<Square> getKnightAttackPatterns() {
        ArrayList<Square> attackPatterns = new ArrayList<>();

        double squareSize = getChessboard().getSquareSize();

        // Every Knight attack pattern
        attackPatterns.add(Square.find((getCurrentX() - squareSize), (getCurrentY() - (squareSize * 2)), squareSize));
        attackPatterns.add(Square.find((getCurrentX() + squareSize), (getCurrentY() - (squareSize * 2)), squareSize));
        attackPatterns.add(Square.find((getCurrentX() - squareSize), (getCurrentY() + (squareSize * 2)), squareSize));
        attackPatterns.add(Square.find((getCurrentX() + squareSize), (getCurrentY() + (squareSize * 2)), squareSize));
        attackPatterns.add(Square.find((getCurrentX() - (squareSize * 2)), (getCurrentY() - squareSize), squareSize));
        attackPatterns.add(Square.find((getCurrentX() + (squareSize * 2)), (getCurrentY() - squareSize), squareSize));
        attackPatterns.add(Square.find((getCurrentX() - (squareSize * 2)), (getCurrentY() + squareSize), squareSize));
        attackPatterns.add(Square.find((getCurrentX() + (squareSize * 2)), (getCurrentY() + squareSize), squareSize));

        // Remove if square !exist, or if square is occupied by the same colour
        attackPatterns.removeIf(attackSquare -> (attackSquare == null // If null (out of bounds)
                || getChessboard().isSquareOccupied(attackSquare)
                && getChessboard().getPiece(attackSquare).getPieceColour().equals(getPieceColour())));

        return attackPatterns;
    }
}
