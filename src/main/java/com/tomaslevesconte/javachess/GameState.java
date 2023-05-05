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
        PieceColour pieceColour = board.isWhitesTurn() ? PieceColour.WHITE : PieceColour.BLACK;
        ArrayList<Square> opponentsMoves = new ArrayList<>();
        Square kingsSquare = null;
        for (Piece piece : pieceList) {
            if (piece.getPieceColour() != pieceColour) {
                opponentsMoves.addAll(piece.getLegalMoves());
            } else if (piece.getPieceType().equals(PieceType.KING)
                        && piece.getPieceColour().equals(pieceColour)) {
                kingsSquare = Square.find(
                        piece.getPosX(),
                        piece.getPosY(),
                        board.getSquareSize()
                );
            }
        }
        Square finalKingsSquare = kingsSquare;
        opponentsMoves.forEach(move -> {
            if (move.equals(finalKingsSquare)) {
                System.out.println("King is in check!");
                isKingInCheckMate();
            }
        });
    }

    private void isKingInCheckMate() {

    }
}
