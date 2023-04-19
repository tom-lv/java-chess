package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class King extends Piece {

    private static final int SQUARES_IT_CAN_MOVE = 1;

    public King(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(pieceColour, square, chessboard);
        setPieceType(PieceType.KING);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        // Add all legal moves for the king (can move one square in every direction)
        legalMoves.addAll(evaluateVerticalSquares(SQUARES_IT_CAN_MOVE));
        legalMoves.addAll(evaluateHorizontalSquares(SQUARES_IT_CAN_MOVE));
        legalMoves.addAll(evaluateDiagonalSquares(SQUARES_IT_CAN_MOVE));
        legalMoves.addAll(evaluateCastleSquares());


        ArrayList<Square> opponentsMoves = getOpponentsMoves(); // List of opponent's moves
        ArrayList<Square> curatedMoves = new ArrayList<>(legalMoves); // List of legal moves - opponent's moves
        curatedMoves.removeAll(opponentsMoves); // Stop the king from putting itself in check
        return curatedMoves;
    }

    private ArrayList<Square> getOpponentsMoves() {
        ArrayList<Square> opponentsMoves = new ArrayList<>(); // List of opponent's moves
        double squareSize = getChessboard().getSquareSize();
        getChessboard().getPiecePositions().forEach(piece -> {
            // If piece is of the opposite colour & is not a pawn or a king
            if (piece.getPieceColour() != getPieceColour()
                    && piece.getPieceType() != PieceType.KING
                    && piece.getPieceType() != PieceType.PAWN) {
                opponentsMoves.addAll(piece.getLegalMoves()); // Add all opposing pieces' moves (except for king & pawn)

                // If piece is of the opposite colour & piece is a king
            } else if (piece.getPieceColour() != getPieceColour()
                    && piece.getPieceType().equals(PieceType.KING)) {
                // Add opposing king's attack patterns
                opponentsMoves.addAll(piece.evaluateVerticalSquares(SQUARES_IT_CAN_MOVE));
                opponentsMoves.addAll(piece.evaluateHorizontalSquares(SQUARES_IT_CAN_MOVE));
                opponentsMoves.addAll(piece.evaluateDiagonalSquares(SQUARES_IT_CAN_MOVE));

                // If piece is of the opposite colour & piece is a pawn
            } else if (piece.getPieceColour() != getPieceColour()
                    && piece.getPieceType().equals(PieceType.PAWN)) {
                boolean direction = piece.getPieceColour().equals(PieceColour.WHITE);

                // Add opposing pawns attack patterns (left)
                double[] nextDiagonal = getChessboard().findNextDiagonal(direction, true, new double[]{piece.getCurrentX(), piece.getCurrentY()});
                if (piece.getPieceColour() != PieceColour.WHITE
                        && piece.getCurrentY() != 0
                        || piece.getPieceColour() != PieceColour.BLACK
                        && piece.getCurrentY() != (squareSize * 7)
                        || piece.getCurrentX() != 0
                        && nextDiagonal[0] != 0) {
                    opponentsMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
                }

                // Add opposing pawns attack patterns (right)
                nextDiagonal = getChessboard().findNextDiagonal(direction, false, new double[]{piece.getCurrentX(), piece.getCurrentY()});
                if (piece.getPieceColour() != PieceColour.WHITE
                        && piece.getCurrentY() != 0
                        || piece.getPieceColour() != PieceColour.BLACK
                        && piece.getCurrentY() != (squareSize * 7)
                        || piece.getCurrentX() != (squareSize * 7)
                        && nextDiagonal[0] != 0) {
                    opponentsMoves.add(Square.findSquare(nextDiagonal[0], nextDiagonal[1], squareSize));
                }
            }
        });
        return opponentsMoves;
    }

    private ArrayList<Square> evaluateCastleSquares() {
        ArrayList<Square> castleSquares = new ArrayList<>();
        ArrayList<Piece> rooks = getChessboard().getRooks(getPieceColour());
        double squareSize = getChessboard().getSquareSize();

        // Evaluate queen's side
        double nextX = getChessboard().findNextHorizontalSquare(true, getCurrentX());
        int emptySquareCounter = 0;
        for (int i = 0; i < 4; i++) {
            if (getCurrentX() == 0 || nextX < 0) {
                break;
            }  else if (!hasMoved()
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

        // Evaluate king's side
        nextX = getChessboard().findNextHorizontalSquare(false, getCurrentX());
        emptySquareCounter = 0;
        for (int i = 0; i < 3; i++) {
            if (getCurrentX() == 0 || nextX < 0) {
                break;
            }  else if (!hasMoved()
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
