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
        double oneSqrY = findSquareInFront(1, this.getPositionY());
        double twoSqrY = findSquareInFront(2, this.getPositionY());
        if (!hasMoved && !isSquareOccupied(this.getPositionX(), twoSqrY)) {
            System.out.println("2");
        } else if (!isSquareOccupied(this.getPositionX(), oneSqrY)) {
            System.out.println("1");
        } else {
            System.out.println("0");
        }
    }

    private boolean isMoveLegal() {
        return false;
    }
}
