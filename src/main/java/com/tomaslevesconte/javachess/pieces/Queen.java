package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Queen extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 8;

    public Queen(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(pieceColour, square, chessboard);
        setPieceType(PieceType.QUEEN);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();

        // Evaluate up squares
        double nextY = getChessboard().findNextVerticalSquare(true, getCurrentY());
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentY() == 0 || nextY < 0) {
                break;
            } else if (!getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else {
                break;
            }
            nextY -= squareSize;
        }

        // Evaluate down squares
        nextY = getChessboard().findNextVerticalSquare(false, getCurrentY());
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentY() == (squareSize * 7) || nextY > (squareSize * 7)) {
                break;
            } else if (!getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else {
                break;
            }
            nextY += squareSize;
        }

        // Evaluate left squares
        double nextX = getChessboard().findNextVerticalSquare(true, getCurrentX());
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentX() == 0 || nextX < 0) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextX, getCurrentY())) {
                legalMoves.add(Square.findSquare(nextX, getCurrentY(), squareSize));
            } else {
                break;
            }
            nextX -= squareSize;
        }

        // Evaluate right squares
        nextX = getChessboard().findNextVerticalSquare(false, getCurrentX());
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentX() == (squareSize * 7) || nextX > (squareSize * 7)) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextX, getCurrentY())) {
                legalMoves.add(Square.findSquare(nextX, getCurrentY(), squareSize));
            } else {
                break;
            }
            nextX += squareSize;
        }

        // Evaluate up/left squares
        double[] nextDiagonal = getChessboard().findNextDiagonal(true, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentX() == 0 || getCurrentY() == 0 || nextDiagonal[0] < 0 || nextDiagonal[1] < 0) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else {
                break;
            }
            nextDiagonal[0] -= squareSize;
            nextDiagonal[1] -= squareSize;
        }

        // Evaluate up/right squares
        nextDiagonal = getChessboard().findNextDiagonal(true, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentX() == (squareSize * 7)
                    || getCurrentY() == 0
                    || nextDiagonal[0] > (squareSize * 7)
                    || nextDiagonal[1] < 0) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])) {
                legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
            } else {
                break;
            }
            nextDiagonal[0] += squareSize;
            nextDiagonal[1] -= squareSize;
        }

        // Evaluate down/left squares
        nextDiagonal = getChessboard().findNextDiagonal(false, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentY() == (squareSize * 7)
                    || getCurrentX() == 0
                    || nextDiagonal[0] < 0
                    || nextDiagonal[1] > (squareSize * 7)) {
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
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentY() == (squareSize * 7)
                    || getCurrentX() == (squareSize * 7)
                    || nextDiagonal[0] > (squareSize * 7)
                    || nextDiagonal[1] > (squareSize * 7)) {
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
