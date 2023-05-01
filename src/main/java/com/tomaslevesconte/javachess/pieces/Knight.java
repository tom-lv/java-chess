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
        ArrayList<Square> legalMoves = new ArrayList<>();

        getKnightAttackPatterns().forEach(attackSquare -> {
            // If out of bounds
            if (attackSquare == null) {
                // Do nothing
            } else if (getChessboard().isSquareOccupied(attackSquare)
                    && getChessboard().getPiece(attackSquare).getPieceColour() != getPieceColour()
                    || !getChessboard().isSquareOccupied(attackSquare)) {
                legalMoves.add(attackSquare);
            }
        });

        return legalMoves;
    }

    private ArrayList<Square> getKnightAttackPatterns() {
        ArrayList<Square> attackPattern = new ArrayList<>(); // Attack patterns
        
        double squareSize = getChessboard().getSquareSize();

        attackPattern.add(Square.find((getCurrentX() - squareSize), (getCurrentY() - (squareSize * 2)), squareSize));
        attackPattern.add(Square.find((getCurrentX() + squareSize), (getCurrentY() - (squareSize * 2)), squareSize));
        attackPattern.add(Square.find((getCurrentX() - squareSize), (getCurrentY() + (squareSize * 2)), squareSize));
        attackPattern.add(Square.find((getCurrentX() + squareSize), (getCurrentY() + (squareSize * 2)), squareSize));
        attackPattern.add(Square.find((getCurrentX() - (squareSize * 2)), (getCurrentY() - squareSize), squareSize));
        attackPattern.add(Square.find((getCurrentX() + (squareSize * 2)), (getCurrentY() - squareSize), squareSize));
        attackPattern.add(Square.find((getCurrentX() - (squareSize * 2)), (getCurrentY() + squareSize), squareSize));
        attackPattern.add(Square.find((getCurrentX() + (squareSize * 2)), (getCurrentY() + squareSize), squareSize));

        return attackPattern;
    }
}
