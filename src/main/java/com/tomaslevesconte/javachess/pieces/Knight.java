package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Knight extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 1;

    public Knight(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(PieceType.KNIGHT, pieceColour, square, SQUARES_IT_CAN_MOVE, chessboard);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();

        // Evaluate vertical up/left squares
        double[] lSquareXY = new double[]{(getCurrentX() - squareSize), (getCurrentY() - (squareSize * 2))};
        Square lSquare = Square.findSquare(lSquareXY[0], lSquareXY[1], squareSize);
        // If out of bounds
        if (getCurrentX() == 0
                || getCurrentY() == 0
                || lSquareXY[1] < 0) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(lSquare)
                && getPieceColour() != getChessboard().getPiece(lSquare).getPieceColour()
                || !getChessboard().isSquareOccupied(lSquare)) {
            legalMoves.add(lSquare);
        }

        // Evaluate vertical up/right squares
        lSquareXY = new double[]{(getCurrentX() + squareSize), (getCurrentY() - (squareSize * 2))};
        lSquare = Square.findSquare(lSquareXY[0], lSquareXY[1], squareSize);
        // If out of bounds
        if (getCurrentX() == (squareSize * 7)
                || getCurrentY() == 0
                || lSquareXY[1] < 0 ) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(lSquare)
                && getPieceColour() != getChessboard().getPiece(lSquare).getPieceColour()
                || !getChessboard().isSquareOccupied(lSquare)) {
            legalMoves.add(lSquare);
        }

        // Evaluate vertical down/left squares
        lSquareXY = new double[]{(getCurrentX() - squareSize), (getCurrentY() + (squareSize * 2))};
        lSquare = Square.findSquare(lSquareXY[0], lSquareXY[1], squareSize);
        // If out of bounds
        if (getCurrentX() == 0
                || getCurrentY() == (squareSize * 7)
                || lSquareXY[1] > (squareSize * 7)) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(lSquare)
                && getPieceColour() != getChessboard().getPiece(lSquare).getPieceColour()
                || !getChessboard().isSquareOccupied(lSquare)) {
            legalMoves.add(lSquare);
        }

        // Evaluate vertical down/right squares
        lSquareXY = new double[]{(getCurrentX() + squareSize), (getCurrentY() + (squareSize * 2))};
        lSquare = Square.findSquare(lSquareXY[0], lSquareXY[1], squareSize);
        // If out of bounds
        if (getCurrentX() == (squareSize * 7)
                || getCurrentY() == (squareSize * 7)
                || lSquareXY[1] > (squareSize * 7)) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(lSquare)
                && getPieceColour() != getChessboard().getPiece(lSquare).getPieceColour()
                || !getChessboard().isSquareOccupied(lSquare)) {
            legalMoves.add(lSquare);
        }

        // Evaluate horizontal left/up squares
        lSquareXY = new double[]{(getCurrentX() - (squareSize * 2)), (getCurrentY() - squareSize)};
        lSquare = Square.findSquare(lSquareXY[0], lSquareXY[1], squareSize);
        // If out of bounds
        if (getCurrentX() == 0
                || getCurrentY() == 0
                || lSquareXY[0] < 0) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(lSquare)
                && getPieceColour() != getChessboard().getPiece(lSquare).getPieceColour()
                || !getChessboard().isSquareOccupied(lSquare)) {
            legalMoves.add(lSquare);
        }

        // Evaluate horizontal right/up squares
        lSquareXY = new double[]{(getCurrentX() + (squareSize * 2)), (getCurrentY() - squareSize)};
        lSquare = Square.findSquare(lSquareXY[0], lSquareXY[1], squareSize);
        // If out of bounds
        if (getCurrentX() == (squareSize * 7)
                || getCurrentY() == 0
                || lSquareXY[0] > (squareSize * 7)) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(lSquare)
                && getPieceColour() != getChessboard().getPiece(lSquare).getPieceColour()
                || !getChessboard().isSquareOccupied(lSquare)) {
            legalMoves.add(lSquare);
        }

        // Evaluate horizontal left/down squares
        lSquareXY = new double[]{(getCurrentX() - (squareSize * 2)), (getCurrentY() + squareSize)};
        lSquare = Square.findSquare(lSquareXY[0], lSquareXY[1], squareSize);
        // If out of bounds
        if (getCurrentX() == 0
                || getCurrentY() == (squareSize * 7)
                || lSquareXY[0] < 0) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(lSquare)
                && getPieceColour() != getChessboard().getPiece(lSquare).getPieceColour()
                || !getChessboard().isSquareOccupied(lSquare)) {
            legalMoves.add(lSquare);
        }

        // Evaluate horizontal right/down squares
        lSquareXY = new double[]{(getCurrentX() + (squareSize * 2)), (getCurrentY() + squareSize)};
        lSquare = Square.findSquare(lSquareXY[0], lSquareXY[1], squareSize);
        // If out of bounds
        if (getCurrentX() == (squareSize * 7)
                || getCurrentY() == (squareSize * 7)
                || lSquareXY[0] > (squareSize * 7)) {
            // Do nothing
        } else if (getChessboard().isSquareOccupied(lSquare)
                && getPieceColour() != getChessboard().getPiece(lSquare).getPieceColour()
                || !getChessboard().isSquareOccupied(lSquare)) {
            legalMoves.add(lSquare);
        }

        return legalMoves;
    }
}
