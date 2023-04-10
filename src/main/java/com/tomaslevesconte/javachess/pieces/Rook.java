package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Rook extends Piece {

    private static final int SQUARES_CAN_MOVE = 8;

    public Rook(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(pieceColour, square, chessboard);
        setPieceType(PieceType.ROOK);
        createPiece();
    }

    @Override
    public boolean move(double newX, double newY) {
        boolean isMoveLegal = false;
        double x = Math.round(newX);
        double y = Math.round(newY);
        for (Square legalMove : getLegalMoves()) {
            double legalX = Math.round(legalMove.getX(getChessboard().getSquareSize()));
            double legalY = Math.round(legalMove.getY(getChessboard().getSquareSize()));
            if (x == legalX && y == legalY) {
                setCurrentX(newX);
                setCurrentY(newY);
                isMoveLegal = true;
                break;
            }
        }
        return isMoveLegal;
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        double[] possibleCoordinates = getChessboard().getPossibleXAndYCoordinates();
        double squareSize = getChessboard().getSquareSize();

        // Evaluate up squares
        double lowerBound = Math.round(possibleCoordinates[0]);
        double nextY = findYAxisSquares(true, getCurrentY());
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(nextY) < lowerBound) {
                break;
            } else if (!isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else if (isSquareOccupied(getCurrentX(), nextY)) {
                break;
            }
            nextY -= squareSize;
        }

        //Evaluate down squares
        double upperBound = Math.round(possibleCoordinates[possibleCoordinates.length - 1]);
        nextY = findYAxisSquares(false, getCurrentY());
        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (Math.round(getCurrentY()) == upperBound) {
                break;
            } else if (Math.round(nextY) > upperBound) {
                break;
            } else if (!isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else if (isSquareOccupied(getCurrentX(), nextY)) {
                break;
            }
            nextY += squareSize;
        }
        return legalMoves;
    }
}
