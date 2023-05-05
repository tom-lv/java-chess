package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.pieces.Piece;

import java.util.ArrayList;

public class GameState {

    private final Board board;
    private final ArrayList<Piece> pieceList;

    public GameState(Board board) {
        this.board = board;
        this.pieceList = board.getPieceList();
    }

    public void checkGameState() {
        isKingInCheck();
    }

    private void isKingInCheck() {
        if (board.isWhitesTurn()) {
            Square kingsSquare;
            for (Piece piece : pieceList) {
                if (piece.getPieceType().equals(PieceType.KING)) {
                    kingsSquare = Square.find(
                            piece.getPosX(),
                            piece.getPosY(),
                            board.getSquareSize()
                    );
                }
            }

        }
    }
}
