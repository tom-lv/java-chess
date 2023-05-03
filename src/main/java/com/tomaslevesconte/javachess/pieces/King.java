package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;
import java.util.Objects;

public class King extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 1;

    public King(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(PieceType.KING, pieceColour, square, SQUARES_IT_CAN_MOVE, chessboard);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();

        // Evaluate all the King's possible moves
        legalMoves.addAll(getVerticalAttackPattern());
        legalMoves.addAll(getHorizontalAttackPattern());
        legalMoves.addAll(getDiagonalAttackPattern());
        legalMoves.addAll(getCastlePattern()); // If castling is possible (need to fix)
        // Stop the King from putting itself in check by removing the opponent's moves from the possible pool
        legalMoves.removeAll(getOpponentsMoves());

        return legalMoves;
    }

    private ArrayList<Square> getOpponentsMoves() {
        ArrayList<Square> opponentsMoves = new ArrayList<>();

        // For each piece on the board
        getChessboard().getPieceList().forEach(piece -> {
            // If the piece's colour is different & =='King'
            if (piece.getPieceColour() != getPieceColour() && piece.getPieceType().equals(PieceType.KING)) {
                opponentsMoves.addAll(piece.getVerticalAttackPattern());
                opponentsMoves.addAll(piece.getHorizontalAttackPattern());
                opponentsMoves.addAll(piece.getDiagonalAttackPattern());
                // If the piece's colour is different & == 'Pawn'
            } else if (piece.getPieceColour() != getPieceColour() && piece.getPieceType().equals(PieceType.PAWN)) {
                opponentsMoves.addAll(getEnemyPawnAttackPattern(piece));
                // If the piece colour is different & != 'King' or 'Pawn'
            } else if (piece.getPieceColour() != getPieceColour()) {
                opponentsMoves.addAll(piece.getLegalMoves());
            }
        });

        return opponentsMoves;
    }

    private ArrayList<Square> getEnemyPawnAttackPattern(Piece piece) {
        ArrayList<Square> attackPattern = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = piece.getPieceColour().equals(PieceColour.WHITE) ? -squareSize : squareSize;

        // Every pawn attack pattern
        attackPattern.add(Square.find((piece.getCurrentX() - squareSize), (piece.getCurrentY() + multiplier), squareSize));
        attackPattern.add(Square.find((piece.getCurrentX() + squareSize), (piece.getCurrentY() + multiplier), squareSize));

        attackPattern.removeIf(Objects::isNull); // Remove square if null (out of bounds)

        return attackPattern;
    }

    private ArrayList<Square> getCastlePattern() {
        ArrayList<Square> castlePattern = new ArrayList<>();

        // Evaluate Queen's side
        Piece queenSideRook = getChessboard().getQueenSideRook(getPieceColour());
        Square[] queenSideSquares = getPieceColour().equals(PieceColour.WHITE)
                ? new Square[]{Square.B1, Square.C1, Square.D1}
                : new Square[]{Square.B8, Square.C8, Square.D8};
        if (hasMoved() || queenSideRook != null && queenSideRook.hasMoved()
                || getChessboard().isSquareOccupied(queenSideSquares[0])
                || getChessboard().isSquareOccupied(queenSideSquares[1])
                || getChessboard().isSquareOccupied(queenSideSquares[2])) {
            // Do nothing
        } else if (queenSideRook != null) {
            Square castleSquare = getPieceColour().equals(PieceColour.WHITE) ? Square.C1 : Square.C8;
            castlePattern.add(castleSquare);
        }

        // Evaluate King's side
        Piece kingSideRook = getChessboard().getKingSideRook(getPieceColour());
        Square[] kingSideSquares = getPieceColour().equals(PieceColour.WHITE)
                ? new Square[]{Square.F1, Square.G1}
                : new Square[]{Square.F8, Square.G8};
        if (hasMoved() || kingSideRook != null && kingSideRook.hasMoved()
                || getChessboard().isSquareOccupied(kingSideSquares[0])
                || getChessboard().isSquareOccupied(kingSideSquares[1])) {
            // Do nothing
        } else if (kingSideRook != null) {
            Square castleSquare = getPieceColour().equals(PieceColour.WHITE) ? Square.G1 : Square.G8;
            castlePattern.add(castleSquare);
        }

        return castlePattern;
    }
}
