package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

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
        legalMoves.addAll(evaluateVerticalSquares());
        legalMoves.addAll(evaluateHorizontalSquares());
        legalMoves.addAll(evaluateDiagonalSquares());
        legalMoves.addAll(evaluateCastleSquares());
        // Stop the King from putting itself in check by removing the opponent's moves from the possible pool
        legalMoves.removeAll(getOpponentsMoves());

        return legalMoves;
    }

    private ArrayList<Square> getOpponentsMoves() {
        ArrayList<Square> opponentsMoves = new ArrayList<>();
        // For each piece on the board
        getChessboard().getPiecePositions().forEach(piece -> {
            // If the piece's colour is different & =='King'
            if (piece.getPieceColour() != getPieceColour() && piece.getPieceType().equals(PieceType.KING)) {
                opponentsMoves.addAll(piece.evaluateVerticalSquares());
                opponentsMoves.addAll(piece.evaluateHorizontalSquares());
                opponentsMoves.addAll(piece.evaluateDiagonalSquares());

                // If the piece's colour is different & == 'Pawn'
            } else if (piece.getPieceColour() != getPieceColour() && piece.getPieceType().equals(PieceType.PAWN)) {
                opponentsMoves.addAll(getEnemyPawnAttackPatterns(piece));

                // If the piece colour is different & != 'King' or 'Pawn'
            } else if (piece.getPieceColour() != getPieceColour()) {
                opponentsMoves.addAll(piece.getLegalMoves());
            }
        });

        return opponentsMoves;
    }

    private ArrayList<Square> getEnemyPawnAttackPatterns(Piece piece) {
        ArrayList<Square> pawnAttackPatterns = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();
        double multiplier = piece.getPieceColour().equals(PieceColour.WHITE) ? -squareSize : squareSize; // Pawns move in different directions depending on colour

        // Evaluate Pawn's x coordinate down (<--) for capturing
        double[] nextDiagonal = {(piece.getCurrentX() - squareSize), (piece.getCurrentY() + multiplier)};
        // Do not evaluate a Pawn's moves if they are on the edge of the board
        if (piece.getCurrentX() == 0
                || piece.getPieceColour().equals(PieceColour.WHITE) && piece.getCurrentY() == 0
                || piece.getPieceColour().equals(PieceColour.BLACK) && piece.getCurrentY() == (squareSize * 7)) {
            // Do nothing
        } else {
            pawnAttackPatterns.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
        }

        // Evaluate Pawn's x coordinate up (-->) for capturing
        nextDiagonal = new double[]{(piece.getCurrentX() + squareSize), (piece.getCurrentY() + multiplier)};
        // Do not evaluate a Pawn's moves if they are on the edge of the board
        if (piece.getCurrentX() == (squareSize * 7)
                || piece.getPieceColour().equals(PieceColour.WHITE) && piece.getCurrentY() == 0
                || piece.getPieceColour().equals(PieceColour.BLACK) && piece.getCurrentY() == (squareSize * 7)) {
            // Do nothing
        } else {
            pawnAttackPatterns.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
        }

        return pawnAttackPatterns;
    }

    private ArrayList<Square> evaluateCastleSquares() {
        ArrayList<Square> castleSquares = new ArrayList<>();
        ArrayList<Piece> rooks = getChessboard().getRooks(getPieceColour());
        double squareSize = getChessboard().getSquareSize();

        // Evaluate Queen's side
        double nextX = getChessboard().findNextHorizontalSquare(true, getCurrentX());
        int emptySquareCounter = 0;
        for (int i = 0; i < 4; i++) {
            if (getCurrentX() == 0 || nextX < 0) {
                break;
            } else if (!hasMoved()
                    && !rooks.get(0).hasMoved()
                    && emptySquareCounter == 3
                    && getChessboard().isSquareOccupied(nextX, getCurrentY())
                    && getChessboard().getPiece(nextX, getCurrentY()).getPieceType().equals(PieceType.ROOK)) {
                if (getPieceColour().equals(PieceColour.WHITE)) {
                    castleSquares.add(Square.C1);
                } else {
                    castleSquares.add(Square.C8);
                }
            } else if (!getChessboard().isSquareOccupied(nextX, getCurrentY())) {
                emptySquareCounter++;
            }
            nextX -= squareSize;
        }

        // Evaluate King's side
        nextX = getChessboard().findNextHorizontalSquare(false, getCurrentX());
        emptySquareCounter = 0;
        for (int i = 0; i < 3; i++) {
            if (getCurrentX() == 0 || nextX < 0) {
                break;
            } else if (!hasMoved()
                    && !rooks.get(1).hasMoved()
                    && emptySquareCounter == 2
                    && getChessboard().isSquareOccupied(nextX, getCurrentY())
                    && getChessboard().getPiece(nextX, getCurrentY()).getPieceType().equals(PieceType.ROOK)) {
                if (getPieceColour().equals(PieceColour.WHITE)) {
                    castleSquares.add(Square.G1);
                } else {
                    castleSquares.add(Square.G8);
                }
            } else if (!getChessboard().isSquareOccupied(nextX, getCurrentY())) {
                emptySquareCounter++;
            }
            nextX += squareSize;
        }

        return castleSquares;
    }
}
