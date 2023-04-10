package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Pawn extends Piece {

    private static final int SQUARES_CAN_MOVE = 2;
    private boolean onStartingSquare = true;

    public Pawn(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(pieceColour, square, chessboard);
        setPieceType(PieceType.PAWN);
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
                onStartingSquare = false;
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
        double firstValue = Math.round(possibleCoordinates[0]);
        double lastValue = Math.round(possibleCoordinates[possibleCoordinates.length - 1]);

        double squareSize = getChessboard().getSquareSize();
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -squareSize : squareSize;
        double nextY = findYAxisSquares(getCurrentY());

        for (int i = 0; i < SQUARES_CAN_MOVE; i++) {
            if (onStartingSquare && !isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else if (Math.round(getCurrentY()) == firstValue ||  Math.round(getCurrentY()) == lastValue) {
                break;
            } else if (!isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
                break;
            } else if (isSquareOccupied(getCurrentX(), nextY)) {
                break;
            }
            nextY += multiplier;
        }
        return legalMoves;
    }
}
