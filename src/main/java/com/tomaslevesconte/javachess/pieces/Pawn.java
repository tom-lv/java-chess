package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Pawn extends Piece {

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
        double firstSquareUp = findYAxisSquares(getCurrentY());
        double secondSquareUp = findYAxisSquares(firstSquareUp);
        boolean firstSquareIsNotOccupied = isSquareOccupied(getCurrentX(), firstSquareUp);
        boolean secondSquareIsNotOccupied = isSquareOccupied(getCurrentX(), secondSquareUp);
        if (onStartingSquare && firstSquareIsNotOccupied && secondSquareIsNotOccupied) {
            legalMoves.add(Square.findSquare(getCurrentX(), firstSquareUp, getChessboard().getSquareSize()));
            legalMoves.add(Square.findSquare(getCurrentX(), findYAxisSquares(firstSquareUp), getChessboard().getSquareSize()));
        } else if (firstSquareIsNotOccupied) {
            legalMoves.add(Square.findSquare(getCurrentX(), firstSquareUp, getChessboard().getSquareSize()));
        }
        return legalMoves;
    }
}
