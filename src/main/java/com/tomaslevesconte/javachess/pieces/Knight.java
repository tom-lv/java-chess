package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Knight extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 1;

    public Knight(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(pieceColour, square, chessboard);
        setPieceType(PieceType.KNIGHT);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();

        // Evaluate vertical up/left squares
        double[] nextLSquare = getChessboard().findNextLSquare(true, true, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentX() == 0
                    || getCurrentY() == 0
                    || nextLSquare[1] <= 0
                    && getCurrentY() == squareSize) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else {
                break;
            }
        }

        // Evaluate vertical up/right squares
        nextLSquare = getChessboard().findNextLSquare(true, false, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentX() == (squareSize * 7)
                    || getCurrentY() == 0
                    || nextLSquare[1] <= 0
                    && getCurrentY() == squareSize) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else {
                break;
            }
        }

        // Evaluate vertical down/left squares
        nextLSquare = getChessboard().findNextLSquare(true, true, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentY() == (squareSize * 7)
                    || getCurrentX() == 0
                    || nextLSquare[1] <= 0) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else {
                break;
            }
        }

        // Evaluate vertical down/right squares
        nextLSquare = getChessboard().findNextLSquare(true, false, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentY() == (squareSize * 7)
                    || getCurrentX() == (squareSize * 7)
                    || nextLSquare[1] <= 0) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else {
                break;
            }
        }

        // Evaluate horizontal left/up squares
        nextLSquare = getChessboard().findNextLSquare(false, true, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentX() == 0
                    || getCurrentY() == 0
                    || nextLSquare[0] <= 0
                    && getCurrentX() == squareSize) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else {
                break;
            }
        }

        // Evaluate horizontal right/up squares
        nextLSquare = getChessboard().findNextLSquare(false, false, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentX() == (squareSize * 7)
                    || getCurrentY() == 0
                    || nextLSquare[0] <= (squareSize * 7)
                    && getCurrentX() == (squareSize * 6)) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else {
                break;
            }
        }

        // Evaluate horizontal left/down squares
        nextLSquare = getChessboard().findNextLSquare(false, true, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentY() == (squareSize * 7)
                    || getCurrentX() == 0
                    || nextLSquare[0] <= 0
                    && getCurrentX() == squareSize) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else {
                break;
            }
        }

        // Evaluate horizontal right/down squares
        nextLSquare = getChessboard().findNextLSquare(false, false, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (getCurrentY() == (squareSize * 7)
                    || getCurrentX() == (squareSize * 7)
                    || nextLSquare[0] <= 0
                    && getCurrentX() == (squareSize * 6)) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else {
                break;
            }
        }

        return legalMoves;
    }
}
