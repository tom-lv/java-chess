package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Pawn extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 2;
    private boolean hasMoved = false;

    public Pawn(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(pieceColour, square, chessboard);
        setPieceType(PieceType.PAWN);
        createPiece();
    }

    @Override
    public boolean move(double newX, double newY) {
        double squareSize = getChessboard().getSquareSize();
        for (Square legalMove : getLegalMoves()) {
            double lmX = Math.round(legalMove.getX(squareSize));
            double lmY = Math.round(legalMove.getY(squareSize));
            if (Math.round(newX) == lmX && Math.round(newY) == lmY) {
                hasMoved = true;
                setCurrentX(newX);
                setCurrentY(newY);
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -squareSize : squareSize;
        boolean direction = getPieceColour().equals(PieceColour.WHITE);

        // Evaluate up/down squares (depending on pieceColour)
        double nextY = getChessboard().findNextVerticalSquare(getPieceColour(), getCurrentY());
        for (int i = 0; i < SQUARES_IT_CAN_MOVE; i++) {
            if (!hasMoved && !getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
            } else if (getCurrentY() == 0 ||  getCurrentY() == (squareSize * 7)) {
                break;
            } else if (!getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
                legalMoves.add(Square.findSquare(getCurrentX(), nextY, squareSize));
                break;
            } else {
                break;
            }
            nextY += multiplier;
        }

        // Evaluate diagonal left for capturing
        double[] nextDiagonal = getChessboard().findNextDiagonal(direction, true, new double[]{getCurrentX(), getCurrentY()});
        if (getPieceColour().equals(PieceColour.WHITE)
                && getCurrentY() == 0
                || getPieceColour().equals(PieceColour.BLACK)
                && getCurrentY() == (squareSize * 7)
                || getCurrentX() == 0
                && nextDiagonal[0] == 0) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                && getPieceColour() != getChessboard().findPiece(nextDiagonal[0], nextDiagonal[1]).getPieceColour()) {
            legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
        }

        // Evaluate diagonal right for capturing
        nextDiagonal = getChessboard().findNextDiagonal(direction, false, new double[]{getCurrentX(), getCurrentY()});
        if (getPieceColour().equals(PieceColour.WHITE)
                && getCurrentY() == 0
                || getPieceColour().equals(PieceColour.BLACK)
                && getCurrentY() == (squareSize * 7)
                || getCurrentX() == (squareSize * 7)
                && nextDiagonal[0] == 0) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                && getPieceColour() != getChessboard().findPiece(nextDiagonal[0], nextDiagonal[1]).getPieceColour()) {
            legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
        }

        return legalMoves;
    }
}
