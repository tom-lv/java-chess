package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Pawn extends Piece {

    private boolean isOnStartingSquare = true;

    public Pawn(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard, ArrayList<Piece> pieceList) {
        super(pieceColour, positionX, positionY, chessboard, pieceList);
        setPieceType(PieceType.PAWN);
        createPiece();
    }

    @Override
    public void move() {
        isOnStartingSquare = false;
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> squares = new ArrayList<>();
        double firstUpSquare = findUpSquare(getPositionY());

        if (isOnStartingSquare && !isSquareOccupied(getPositionX(), firstUpSquare) && !isSquareOccupied(getPositionX(), findUpSquare(firstUpSquare))) {
            squares.add(Square.findSquare(getPositionX(), firstUpSquare, 112.5));
            squares.add(Square.findSquare(getPositionX(), findUpSquare(firstUpSquare), 112.5));
        } else if (!isSquareOccupied(getPositionX(), firstUpSquare)) {
            squares.add(Square.findSquare(getPositionX(), firstUpSquare, 112.5));
        }

        return squares;
    }

    private boolean isMoveLegal() {
        return false;
    }
}
