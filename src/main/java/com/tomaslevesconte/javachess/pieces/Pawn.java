package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

import java.util.ArrayList;

public class Pawn extends Piece {

    private boolean hasMoved;

    public Pawn(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard, ArrayList<Piece> pieceList) {
        super(pieceColour, positionX, positionY, chessboard, pieceList);
        this.pieceType = PieceType.PAWN;
        this.createPiece();
    }

    @Override
    public void move() {
        hasMoved = true;
    }

    @Override
    public void getLegalMoves() {
        double x = this.getPositionX();
        double oneSqr = findSquareInFront(1, this.getPositionY());
        double twoSqr = findSquareInFront(2, this.getPositionY());
        if (!hasMoved && !isSquareOccupied(x, twoSqr)) {
            System.out.println("2");
        } else if (!isSquareOccupied(x, oneSqr)) {
            System.out.println("1");
        } else {
            System.out.println("0");
        }
    }

    private boolean isMoveLegal() {
        return false;
    }

    private boolean isSquareOccupied(double x, double y) {
        boolean isOccupied = false;
        for (Piece piece : this.pieceList) {
            if (Math.round(x) == Math.round(piece.getPositionX()) && Math.round(y) == Math.round(piece.getPositionY())) {
                isOccupied = true;
                break;
            }
        }
        return isOccupied;
    }

    private double findSquareInFront(int numOfSquares, double currentSquareY) {
        int index = pieceColour.equals(PieceColour.WHITE) ? -1 : 1;
        double[] possibleCoordinates = chessboard.getPossibleXAndYCoordinates();
        double squareInFront = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            if (Math.round(possibleCoordinates[i]) == Math.round(currentSquareY)) {
                squareInFront = possibleCoordinates[i + index];
                break;
            }
        }
        if (numOfSquares > 1) {
            return findSquareInFront(numOfSquares - 1, squareInFront);
        } else {
            return squareInFront;
        }
    }
}
