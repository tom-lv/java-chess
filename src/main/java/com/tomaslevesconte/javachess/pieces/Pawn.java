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
        for (Square legalMove : getLegalMoves()) {
            double legalX = legalMove.getX(getChessboard().getSquareSize());
            double legalY = legalMove.getY(getChessboard().getSquareSize());
            if (Math.round(newX) == Math.round(legalX) && Math.round(newY) == Math.round(legalY)) {
                onStartingSquare = false;
                isMoveLegal = true;
                setCurrentX(newX);
                setCurrentY(newY);
                break;
            }
        }
        return isMoveLegal;
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        double firstSquareUp = findUpSquare(getCurrentY());
        double secondSquareUp = findUpSquare(firstSquareUp);
        boolean firstSquareIsNotOccupied = isSquareOccupied(getCurrentX(), firstSquareUp);
        boolean secondSquareIsNotOccupied = isSquareOccupied(getCurrentX(), secondSquareUp);
        if (onStartingSquare && firstSquareIsNotOccupied && secondSquareIsNotOccupied) {
            legalMoves.add(Square.findSquare(getCurrentX(), firstSquareUp, getChessboard().getSquareSize()));
            legalMoves.add(Square.findSquare(getCurrentX(), findUpSquare(firstSquareUp), getChessboard().getSquareSize()));
        } else if (firstSquareIsNotOccupied) {
            legalMoves.add(Square.findSquare(getCurrentX(), firstSquareUp, getChessboard().getSquareSize()));
        }
        return legalMoves;
    }
}
