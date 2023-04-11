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
        double squareSize = getChessboard().getSquareSize();

        // Evaluate up/left squares
        double[] nextDiagonal = findNextDiagonal(true, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == 0 || Math.round(getCurrentY()) == 0) {
                break;
            } else if (Math.round(nextDiagonal[0]) < 0 || Math.round(nextDiagonal[1]) < 0) {
                break;
            } else if (!isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] -= squareSize;
            nextDiagonal[1] -= squareSize;
        }

        // Evaluate up/right squares
        nextDiagonal = findNextDiagonal(true, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == Math.round(squareSize * 7) || getCurrentY() == 0) {
                break;
            } else if (Math.round(nextDiagonal[0]) > Math.round(squareSize * 7) || Math.round(nextDiagonal[1]) < 0) {
                break;
            } else if (!isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] += squareSize;
            nextDiagonal[1] -= squareSize;
        }

        // Evaluate down/left squares
        nextDiagonal = findNextDiagonal(false, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == Math.round(squareSize * 7) || Math.round(getCurrentX()) == 0) {
                break;
            } else if (Math.round(nextDiagonal[0]) < 0 || Math.round(nextDiagonal[1]) > Math.round(squareSize * 7)) {
                break;
            } else if (!isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] -= squareSize;
            nextDiagonal[1] += squareSize;
        }

        // Evaluate down/right squares
        nextDiagonal = findNextDiagonal(false, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == Math.round(squareSize * 7) || Math.round(getCurrentX()) == Math.round(squareSize * 7)) {
                break;
            } else if (Math.round(nextDiagonal[0]) > Math.round(squareSize * 7) || Math.round(nextDiagonal[1]) > Math.round(squareSize * 7)) {
                break;
            } else if (!isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else if (isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                break;
            }
            nextDiagonal[0] += squareSize;
            nextDiagonal[1] += squareSize;
        }

        return legalMoves;
    }
}
