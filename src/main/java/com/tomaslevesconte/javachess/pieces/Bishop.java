package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Bishop extends Piece {

    private static final int SQUARES_CAN_MOVE = 8;

    public Bishop(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(pieceColour, square, chessboard);
        setPieceType(PieceType.BISHOP);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        double[] possibleCoordinates = getChessboard().getPossibleXAndYCoordinates();
        double lowerBound = Math.round(possibleCoordinates[0]);
        double upperBound = Math.round(possibleCoordinates[possibleCoordinates.length - 1]);
        double squareSize = getChessboard().getSquareSize();

        // Evaluate up/left squares
        double[] nextDiagonal = getChessboard().findNextDiagonal(true, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == lowerBound || Math.round(getCurrentY()) == lowerBound) {
                break;
            } else if (Math.round(nextDiagonal[0]) < lowerBound || Math.round(nextDiagonal[1]) < lowerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] -= squareSize;
            nextDiagonal[1] -= squareSize;
        }

        // Evaluate up/right squares
        nextDiagonal = getChessboard().findNextDiagonal(true, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == upperBound || getCurrentY() == lowerBound) {
                break;
            } else if (Math.round(nextDiagonal[0]) > upperBound || Math.round(nextDiagonal[1]) < lowerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] += squareSize;
            nextDiagonal[1] -= squareSize;
        }

        // Evaluate down/left squares
        nextDiagonal = getChessboard().findNextDiagonal(false, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == upperBound || Math.round(getCurrentX()) == lowerBound) {
                break;
            } else if (Math.round(nextDiagonal[0]) < lowerBound || Math.round(nextDiagonal[1]) > upperBound ) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] -= squareSize;
            nextDiagonal[1] += squareSize;
        }

        // Evaluate down/right squares
        nextDiagonal = getChessboard().findNextDiagonal(false, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == upperBound || Math.round(getCurrentX()) == upperBound) {
                break;
            } else if (Math.round(nextDiagonal[0]) > upperBound || Math.round(nextDiagonal[1]) > upperBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] += squareSize;
            nextDiagonal[1] += squareSize;
        }

        return legalMoves;
    }
}
