package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Knight extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 1;

    public Knight(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(PieceType.KNIGHT, pieceColour, square, SQUARES_IT_CAN_MOVE, chessboard);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();

        // Evaluate vertical up/left squares
        double[] nextLSquare = new double[]{(getCurrentX() - squareSize), (getCurrentY() - (squareSize * 2))};
        // If out of bounds
        if (getCurrentX() == 0
                || getCurrentY() == 0
                || nextLSquare[1] < 0) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])
                && getPieceColour() != getChessboard().getPiece(nextLSquare[0], nextLSquare[1]).getPieceColour()
                || !getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
            legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
        }

        // Evaluate vertical up/right squares
        nextLSquare = new double[]{(getCurrentX() + squareSize), (getCurrentY() - (squareSize * 2))};
        // If out of bounds
        if (getCurrentX() == (squareSize * 7)
                || getCurrentY() == 0
                || nextLSquare[1] < 0 ) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])
                && getPieceColour() != getChessboard().getPiece(nextLSquare[0], nextLSquare[1]).getPieceColour()
                || !getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
            legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
        }

        // Evaluate vertical down/left squares
        nextLSquare = new double[]{(getCurrentX() - squareSize), (getCurrentY() + (squareSize * 2))};
        // If out of bounds
        if (getCurrentX() == 0
                || getCurrentY() == (squareSize * 7)
                || nextLSquare[1] > (squareSize * 7)) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])
                && getPieceColour() != getChessboard().getPiece(nextLSquare[0], nextLSquare[1]).getPieceColour()
                || !getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
            legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
        }

        // Evaluate vertical down/right squares
         nextLSquare = new double[]{(getCurrentX() + squareSize), (getCurrentY() + (squareSize * 2))};
        // If out of bounds
        if (getCurrentX() == (squareSize * 7)
                || getCurrentY() == (squareSize * 7)
                || nextLSquare[1] > (squareSize * 7)) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])
                && getPieceColour() != getChessboard().getPiece(nextLSquare[0], nextLSquare[1]).getPieceColour()
                || !getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
            legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
        }

        // Evaluate horizontal left/up squares
        nextLSquare = new double[]{(getCurrentX() - (squareSize * 2)), (getCurrentY() - squareSize)};
        // If out of bounds
        if (getCurrentX() == 0
                || getCurrentY() == 0
                || nextLSquare[0] < 0) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])
                && getPieceColour() != getChessboard().getPiece(nextLSquare[0], nextLSquare[1]).getPieceColour()
                || !getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
            legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
        }

        // Evaluate horizontal right/up squares
        nextLSquare = new double[]{(getCurrentX() + (squareSize * 2)), (getCurrentY() - squareSize)};
        // If out of bounds
        if (getCurrentX() == (squareSize * 7)
                || getCurrentY() == 0
                || nextLSquare[0] > (squareSize * 7)) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])
                && getPieceColour() != getChessboard().getPiece(nextLSquare[0], nextLSquare[1]).getPieceColour()
                || !getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
            legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
        }

        // Evaluate horizontal left/down squares
        nextLSquare = new double[]{(getCurrentX() - (squareSize * 2)), (getCurrentY() + squareSize)};
        // If out of bounds
        if (getCurrentX() == 0
                || getCurrentY() == (squareSize * 7)
                || nextLSquare[0] < 0) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])
                && getPieceColour() != getChessboard().getPiece(nextLSquare[0], nextLSquare[1]).getPieceColour()
                || !getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
            legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
        }

        // Evaluate horizontal right/down squares
        nextLSquare = new double[]{(getCurrentX() + (squareSize * 2)), (getCurrentY() + squareSize)};
        // If out of bounds
        if (getCurrentX() == (squareSize * 7)
                || getCurrentY() == (squareSize * 7)
                || nextLSquare[0] > (squareSize * 7)) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])
                && getPieceColour() != getChessboard().getPiece(nextLSquare[0], nextLSquare[1]).getPieceColour()
                || !getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
            legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
        }

        return legalMoves;
    }
}
