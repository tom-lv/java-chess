package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Pawn extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 2;

    public Pawn(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(PieceType.PAWN, pieceColour, square, SQUARES_IT_CAN_MOVE, chessboard);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -squareSize : squareSize;

        // Evaluate y coordinate up/down squares (depending on pieceColour)
        double nextY = (getCurrentY() + multiplier);
        for (int i = 0; i < getSquaresItCanMove(); i++) {
            if (!hasMoved() && !getChessboard().isSquareOccupied(getCurrentX(), nextY)) {
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

        // Evaluate x coordinate down (<--) for capturing
        double[] nextDiagonal = {(getCurrentX() - squareSize), (getCurrentY() + multiplier)};
        if (getCurrentX() == 0
                || getPieceColour().equals(PieceColour.WHITE) && getCurrentY() == 0
                || getPieceColour().equals(PieceColour.BLACK) && getCurrentY() == (squareSize * 7)) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                && getPieceColour() != getChessboard().getPiece(nextDiagonal[0], nextDiagonal[1]).getPieceColour()) {
            legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
        }

        // Evaluate x coordinate up (-->) for capturing
        nextDiagonal = new double[]{(getCurrentX() + squareSize), (getCurrentY() + multiplier)};
        if (getCurrentX() == (squareSize * 7)
                || getPieceColour().equals(PieceColour.WHITE) && getCurrentY() == 0
                || getPieceColour().equals(PieceColour.BLACK) && getCurrentY() == (squareSize * 7)) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(nextDiagonal[0], nextDiagonal[1])
                && getPieceColour() != getChessboard().getPiece(nextDiagonal[0], nextDiagonal[1]).getPieceColour()) {
            legalMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
        }

        return legalMoves;
    }
}
