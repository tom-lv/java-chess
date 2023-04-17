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

        legalMoves.addAll(evaluateVerticalSquares(SQUARES_IT_CAN_MOVE));
        legalMoves.addAll(evaluateHorizontalSquares(SQUARES_IT_CAN_MOVE));
        legalMoves.addAll(evaluateDiagonalSquares(SQUARES_IT_CAN_MOVE));

        ArrayList<Square> opponentsMoves = new ArrayList<>();

        getChessboard().getPiecePositions().forEach(piece -> {

            if (piece.getPieceType() != PieceType.KING
                    && piece.getPieceType() != PieceType.PAWN
                    && piece.getPieceColour() != getPieceColour()) {

                opponentsMoves.addAll(piece.getLegalMoves());

            } else if (piece.getPieceType().equals(PieceType.PAWN)
                    && piece.getPieceColour() != getPieceColour()) {

                boolean direction = piece.getPieceColour().equals(PieceColour.WHITE);
                double squareSize = getChessboard().getSquareSize();

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

        ArrayList<Square> curatedMoves = new ArrayList<>(legalMoves);
        curatedMoves.removeAll(opponentsMoves);

        return curatedMoves;
    }
}
