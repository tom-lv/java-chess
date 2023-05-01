package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class King extends Piece {

    // Constant
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
        legalMoves.addAll(evaluateCastleSquares()); // If castling is possible
        // Stop the King from putting itself in check by removing the opponent's moves from the possible pool
        legalMoves.removeAll(getOpponentsMoves());

        return legalMoves;
    }

    private ArrayList<Square> getOpponentsMoves() {
        ArrayList<Square> opponentsMoves = new ArrayList<>();

        // For each piece on the board
        getChessboard().getPieceList().forEach(piece -> {
            // If the piece's colour is different & =='King'
            if (piece.getPieceColour() != getPieceColour()
                    && piece.getPieceType().equals(PieceType.KING)) {
                opponentsMoves.addAll(piece.evaluateVerticalSquares());
                opponentsMoves.addAll(piece.evaluateHorizontalSquares());
                opponentsMoves.addAll(piece.evaluateDiagonalSquares());
                // If the piece's colour is different & == 'Pawn'
            } else if (piece.getPieceColour() != getPieceColour()
                    && piece.getPieceType().equals(PieceType.PAWN)) {
                opponentsMoves.addAll(getEnemyPawnAttackPatterns(piece));
                // If the piece colour is different & != 'King' or 'Pawn'
            } else if (piece.getPieceColour() != getPieceColour()) {
                opponentsMoves.addAll(piece.getLegalMoves());
            }
        });

        return opponentsMoves;
    }

    private ArrayList<Square> getEnemyPawnAttackPatterns(Piece piece) {
        ArrayList<Square> pAP = new ArrayList<>();
        double squareSize = getChessboard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = piece.getPieceColour().equals(PieceColour.WHITE) ? -squareSize : squareSize;

        // Evaluate Pawn's x coordinates downwards (<--) for capturing
        double[] nextDiagonal = {(piece.getCurrentX() - squareSize), (piece.getCurrentY() + multiplier)};
        // If out of bounds
        if (piece.getCurrentX() == 0
                || piece.getPieceColour().equals(PieceColour.WHITE) && piece.getCurrentY() == 0
                || piece.getPieceColour().equals(PieceColour.BLACK) && piece.getCurrentY() == (squareSize * 7)) {
            // Do nothing
        } else {
            pAP.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
        }

        // Evaluate Pawn's x coordinates upwards (-->) for capturing
        nextDiagonal = new double[]{(piece.getCurrentX() + squareSize), (piece.getCurrentY() + multiplier)};
        // If out of bounds
        if (piece.getCurrentX() == (squareSize * 7)
                || piece.getPieceColour().equals(PieceColour.WHITE) && piece.getCurrentY() == 0
                || piece.getPieceColour().equals(PieceColour.BLACK) && piece.getCurrentY() == (squareSize * 7)) {
            // Do nothing
        } else {
            pAP.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
        }

        return pAP;
    }

    private ArrayList<Square> evaluateCastleSquares() {
        ArrayList<Square> castleSquares = new ArrayList<>();
        ArrayList<Piece> rooks = getChessboard().getRooks(getPieceColour());

        // Evaluate Queen's side
        Square[] queenSideSquares = getPieceColour().equals(PieceColour.WHITE)
                ? new Square[]{Square.B1, Square.C1, Square.D1}
                : new Square[]{Square.B8, Square.C8, Square.D8};
        if (hasMoved() || rooks.get(0).hasMoved()
                || getChessboard().isSquareOccupied(queenSideSquares[0])
                || getChessboard().isSquareOccupied(queenSideSquares[1])
                || getChessboard().isSquareOccupied(queenSideSquares[2])) {
            // Do nothing
        } else {
            Square castleSquare = getPieceColour().equals(PieceColour.WHITE) ? Square.C1 : Square.C8;
            castleSquares.add(castleSquare);
        }

        // Evaluate King's side
        Square[] kingSideSquares = getPieceColour().equals(PieceColour.WHITE)
                ? new Square[]{Square.F1, Square.G1}
                : new Square[]{Square.F8, Square.G8};
        if (hasMoved() || rooks.get(1).hasMoved()
                || getChessboard().isSquareOccupied(kingSideSquares[0])
                || getChessboard().isSquareOccupied(kingSideSquares[1])) {
            // Do nothing
        } else {
            Square castleSquare = getPieceColour().equals(PieceColour.WHITE) ? Square.G1 : Square.G8;
            castleSquares.add(castleSquare);
        }

        return castleSquares;
    }
}
