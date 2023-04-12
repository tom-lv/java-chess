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
        double[] possibleCoordinates = getChessboard().getPossibleXAndYCoordinates();
        double lowerBound = Math.round(possibleCoordinates[0]);
        double lowerInnerBound = Math.round(possibleCoordinates[1]);
        double upperBound = Math.round(possibleCoordinates[possibleCoordinates.length - 1]);
        double upperInnerBound = Math.round(possibleCoordinates[possibleCoordinates.length -2]);
        double squareSize = getChessboard().getSquareSize();

        // Evaluate vertical up/left squares
        double[] nextLSquare = getChessboard().findNextLSquare(true, true, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == lowerBound || Math.round(getCurrentY()) == lowerBound) {
                break;
            } else if (Math.round(nextLSquare[1]) <= lowerBound && Math.round(getCurrentY()) == lowerInnerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                break;
            }
        }

        // Evaluate vertical up/right squares
        nextLSquare = getChessboard().findNextLSquare(true, false, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == upperBound || getCurrentY() == lowerBound) {
                break;
            } else if (Math.round(nextLSquare[1]) <= lowerBound && Math.round(getCurrentY()) == lowerInnerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                break;
            }
        }

        // Evaluate vertical down/left squares
        nextLSquare = getChessboard().findNextLSquare(true, true, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == upperBound || Math.round(getCurrentX()) == lowerBound) {
                break;
            } else if (Math.round(nextLSquare[1]) <= lowerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                break;
            }
        }

        // Evaluate vertical down/right squares
        nextLSquare = getChessboard().findNextLSquare(true, false, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == upperBound || Math.round(getCurrentX()) == upperBound) {
                break;
            } else if (Math.round(nextLSquare[1]) <= lowerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                break;
            }
        }

        // Evaluate horizontal left/up squares
        nextLSquare = getChessboard().findNextLSquare(false, true, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == lowerBound || Math.round(getCurrentY()) == lowerBound) {
                break;
            } else if (Math.round(nextLSquare[0]) <= lowerBound && Math.round(getCurrentX()) == lowerInnerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                break;
            }
        }

        // Evaluate horizontal right/up squares
        nextLSquare = getChessboard().findNextLSquare(false, false, true, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == upperBound || getCurrentY() == lowerBound) {
                break;
            } else if (Math.round(nextLSquare[0]) <= upperBound && Math.round(getCurrentX()) == upperInnerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                break;
            }
        }

        // Evaluate horizontal left/down squares
        nextLSquare = getChessboard().findNextLSquare(false, true, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == upperBound || Math.round(getCurrentX()) == lowerBound) {
                break;
            } else if (Math.round(nextLSquare[0]) <= lowerBound && Math.round(getCurrentX()) == lowerInnerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                break;
            }
        }

        // Evaluate horizontal right/down squares
        nextLSquare = getChessboard().findNextLSquare(false, false, false, new double[]{getCurrentX(), getCurrentY()});
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == upperBound || Math.round(getCurrentX()) == upperBound) {
                break;
            } else if (Math.round(nextLSquare[0]) <= lowerBound && Math.round(getCurrentX()) == upperInnerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                legalMoves.add(Square.findSquare(nextLSquare[0], nextLSquare[1], squareSize));
            } else if (getChessboard().isSquareOccupied(nextLSquare[0], nextLSquare[1])) {
                break;
            }
        }

        return legalMoves;
    }
}
