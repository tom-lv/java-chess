package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(PieceColour pieceColour, double positionX, double positionY, Chessboard chessboard, ArrayList<Piece> pieceList) {
        super(pieceColour, positionX, positionY, chessboard, pieceList);
        this.pieceType = PieceType.ROOK;
        this.createPiece();
    }

    @Override
    public void move() {

    }

    @Override
    public void getLegalMoves() {

    }
}
