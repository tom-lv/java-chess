package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Rook extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 8;

    public Rook(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(pieceColour, square, chessboard);
        setPieceType(PieceType.ROOK);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        double[] possibleCoordinates = getChessboard().getPossibleXAndYCoordinates();
        double lowerBound = Math.round(possibleCoordinates[0]);
        double upperBound = Math.round(possibleCoordinates[possibleCoordinates.length - 1]);
        double squareSize = getChessboard().getSquareSize();

        // Evaluate up squares
        double nextY = getChessboard().findNextVerticalSquare(true, getCurrentY());
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == lowerBound) {
                break;
            } else if (Math.round(nextY) < lowerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else if (getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                break;
            }
            nextY -= squareSize;
        }

        // Evaluate down squares
        nextY = getChessboard().findNextVerticalSquare(false, getCurrentY());
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == upperBound) {
                break;
            } else if (Math.round(nextY) > upperBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else if (getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                break;
            }
            nextY += squareSize;
        }

        // Evaluate left squares
        double nextX = getChessboard().findNextVerticalSquare(true, getCurrentX());
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == lowerBound) {
                break;
            } else if (Math.round(nextX) < lowerBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextX, getCurrentY())) {
                legalMoves.add(Square.findSquare(nextX, getCurrentY(), squareSize));
            } else if (getChessboard().isSquareOccupied(nextX, getCurrentY())) {
                break;
            }
            nextX -= squareSize;
        }

        // Evaluate right squares
        nextX = getChessboard().findNextVerticalSquare(false, getCurrentX());
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (Math.round(getCurrentX()) == upperBound) {
                break;
            } else if (Math.round(nextX) > upperBound) {
                break;
            } else if (!getChessboard().isSquareOccupied(nextX, getCurrentY())) {
                legalMoves.add(Square.findSquare(nextX, getCurrentY(), squareSize));
            } else if (getChessboard().isSquareOccupied(nextX, getCurrentY())) {
                break;
            }
            nextX += squareSize;
        }

        return legalMoves;
    }
}
