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
        double[] nextStepXY = findNextStepSquare(true, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == lowerBound || Math.round(getCurrentY()) == lowerBound) {
                break;
            } else if (Math.round(nextStepXY[0]) < lowerBound || Math.round(nextStepXY[1]) < lowerBound) {
                break;
            } else if (!isSquareOccupied(nextStepXY[0], nextStepXY[1])) {
                legalMoves.add(Square.findSquare(nextStepXY[0], nextStepXY[1], squareSize));
            } else if (isSquareOccupied(nextStepXY[0], nextStepXY[1])) {
                break;
            }
            nextStepXY[0] -= squareSize;
            nextStepXY[1] -= squareSize;
        }

        // Evaluate up/right squares
        nextStepXY = findNextStepSquare(true, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == upperBound || getCurrentY() == lowerBound) {
                break;
            } else if (Math.round(nextStepXY[0]) > upperBound || Math.round(nextStepXY[1]) < lowerBound) {
                break;
            } else if (!isSquareOccupied(nextStepXY[0], nextStepXY[1])) {
                legalMoves.add(Square.findSquare(nextStepXY[0], nextStepXY[1], squareSize));
            } else if (isSquareOccupied(nextStepXY[0], nextStepXY[1])) {
                break;
            }
            nextStepXY[0] += squareSize;
            nextStepXY[1] -= squareSize;
        }

        // Evaluate down/left squares
        nextStepXY = findNextStepSquare(false, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == upperBound || Math.round(getCurrentX()) == lowerBound) {
                break;
            } else if (Math.round(nextStepXY[0]) < lowerBound || Math.round(nextStepXY[1]) > upperBound ) {
                break;
            } else if (!isSquareOccupied(nextStepXY[0], nextStepXY[1])) {
                legalMoves.add(Square.findSquare(nextStepXY[0], nextStepXY[1], squareSize));
            } else if (isSquareOccupied(nextStepXY[0], nextStepXY[1])) {
                break;
            }
            nextStepXY[0] -= squareSize;
            nextStepXY[1] += squareSize;
        }

        // Evaluate down/right squares
        nextStepXY = findNextStepSquare(false, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == upperBound || Math.round(getCurrentX()) == upperBound) {
                break;
            } else if (Math.round(nextStepXY[0]) > upperBound || Math.round(nextStepXY[1]) > upperBound) {
                break;
            } else if (!isSquareOccupied(nextStepXY[0], nextStepXY[1])) {
                legalMoves.add(Square.findSquare(nextStepXY[0], nextStepXY[1], squareSize));
            } else if (isSquareOccupied(nextStepXY[0], nextStepXY[1])) {
                break;
            }
            nextStepXY[0] += squareSize;
            nextStepXY[1] += squareSize;
        }

        return legalMoves;
    }
}
