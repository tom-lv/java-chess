package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;
import com.tomaslevesconte.javachess.Square;

import java.util.ArrayList;

public class Pawn extends Piece {

    // Constant
    private static final int SQUARES_IT_CAN_MOVE = 2;

    public Pawn(PieceColour pieceColour, Square square, Chessboard chessboard) {
        super(PieceType.PAWN, pieceColour, square, SQUARES_IT_CAN_MOVE, chessboard);
        createPiece();
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();

        legalMoves.addAll(getPawnMovePatterns()); // Add move patterns
        legalMoves.addAll(getPawnAttackPatterns()); // Add attack patterns

        return legalMoves;
    }

    private ArrayList<Square> getPawnMovePatterns() {
        ArrayList<Square> movePatterns = new ArrayList<>();

        double squareSize = getChessboard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -squareSize : squareSize;

        // Every pawn move pattern
        movePatterns.add(Square.find(getCurrentX(), (getCurrentY() + multiplier), squareSize));
        movePatterns.add(Square.find(getCurrentX(), (getCurrentY() + (multiplier * 2)), squareSize));

        // Remove if square !exist, or if square is occupied, or if square == 2nd square and pawn has moved
        movePatterns.removeIf(moveSquare -> (moveSquare == null
                || getChessboard().isSquareOccupied(moveSquare))
                || moveSquare.equals(movePatterns.get(1))
                && hasMoved());

        return movePatterns;
    }

    private ArrayList<Square> getPawnAttackPatterns() {
        ArrayList<Square> attackPatterns = new ArrayList<>();

        double squareSize = getChessboard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -squareSize : squareSize;

        // Every pawn attack pattern
        attackPatterns.add(Square.find((getCurrentX() - squareSize), (getCurrentY() + multiplier), squareSize));
        attackPatterns.add(Square.find((getCurrentX() + squareSize), (getCurrentY() + multiplier), squareSize));

        // Remove if square !exist, or if square is !occupied, or if square is occupied by the same colour
        attackPatterns.removeIf(attackSquare -> (attackSquare == null // If null (out of bounds)
                || !getChessboard().isSquareOccupied(attackSquare)
                || getChessboard().isSquareOccupied(attackSquare)
                && getChessboard().getPiece(attackSquare).getPieceColour().equals(getPieceColour())));

        return attackPatterns;
    }
}
