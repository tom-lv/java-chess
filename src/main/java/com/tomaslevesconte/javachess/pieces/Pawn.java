package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Pawn extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 2;
    private boolean onStartingSquare = true;

    public Pawn(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(pieceColour, square, chessboard);
        setPieceType(PieceType.PAWN);
        createPiece();
    }

    @Override
    public boolean move(double newX, double newY) {
        boolean isMoveLegal = false;
        for (Square legalMove : getLegalMoves()) {
            double legalX = Math.round(legalMove.getX(getChessboard().getSquareSize()));
            double legalY = Math.round(legalMove.getY(getChessboard().getSquareSize()));
            if (Math.round(newX) == legalX && Math.round(newY) == legalY) {
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
        double squareSize = getChessboard().getSquareSize();
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -squareSize : squareSize;

        // Evaluate up/down squares (depending on pieceColour)
        double nextY = getChessboard().findNextVerticalSquare(getPieceColour(), getCurrentY());
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (onStartingSquare && !getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else if (Math.round(getCurrentY()) == 0 ||  Math.round(getCurrentY()) == Math.round(squareSize * 7)) {
                break;
            } else if (!getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
                break;
            } else if (getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                break;
            }
            nextY += multiplier;
        }

        return legalMoves;
    }
}
