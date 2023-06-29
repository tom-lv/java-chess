package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.Event;
import com.tomaslevesconte.javachess.enums.Colour;
import com.tomaslevesconte.javachess.enums.Type;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;
import java.util.Objects;

public class King extends Piece {

    private static final int squaresItCanMove = 1;

    public King(Colour colour, Square square, Board board) {
        super(Type.KING, colour, board.getSquareSize(), square, squaresItCanMove, board);
        createPiece();
    }

    @Override
    public boolean move(Square newSquare) {
        setPreviousPosition(Square.find(getPosX(), getPosY(), getBoard().getSquareSize()));

        for (Square legalMove : getBoard().getGame().getPossibleMoves(this)) {
            if (newSquare == legalMove) {
                Event event = Event.MOVE;
                if (getBoard().isSquareOccupied(newSquare)
                        && getBoard().getPiece(newSquare).getColour() != getColour()) {
                    event = Event.CAPTURE;
                    Piece target = getBoard().getPiece(newSquare);
                    target.capture(target);
                }
                setPosition(newSquare);
                setHasMoved(true);
                event = attemptCastle() ? Event.CASTLE : event;
                getBoard().getGame().update(event);
                return true;
            }
        }

        return false;
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();

        // Evaluate all the King's possible moves
        legalMoves.addAll(getVerticalAttackPattern(false));
        legalMoves.addAll(getHorizontalAttackPattern(false));
        legalMoves.addAll(getDiagonalAttackPattern(false));
        legalMoves.addAll(getCastlePattern()); // If castling is possible (need to fix)
        // Stop the King from putting itself in check by removing the opponent's moves from the possible pool
        legalMoves.removeAll(getOpponentsMoves());

        return legalMoves;
    }

    @Override
    public ArrayList<Square> getLegalMoves(boolean applyKingFilter) {
        ArrayList<Square> legalMoves = new ArrayList<>();

        // Evaluate all the King's possible moves
        legalMoves.addAll(getVerticalAttackPattern(applyKingFilter));
        legalMoves.addAll(getHorizontalAttackPattern(applyKingFilter));
        legalMoves.addAll(getDiagonalAttackPattern(applyKingFilter));
        legalMoves.addAll(getCastlePattern()); // If castling is possible (need to fix)
        // Stop the King from putting itself in check by removing the opponent's moves from the possible pool
        legalMoves.removeAll(getOpponentsMoves());

        return legalMoves;
    }

    private ArrayList<Square> getOpponentsMoves() {
        ArrayList<Square> opponentsMoves = new ArrayList<>();

        // For each piece on the board
        getBoard().getPieceList().forEach(piece -> {
            // If the piece's colour is different & =='King'
            if (piece.getColour() != getColour()
                    && piece.getType().equals(Type.KING)) {
                opponentsMoves.addAll(piece.getVerticalAttackPattern(false));
                opponentsMoves.addAll(piece.getHorizontalAttackPattern(false));
                opponentsMoves.addAll(piece.getDiagonalAttackPattern(false));
                // If the piece's colour is different & == 'Pawn'
            } else if (piece.getColour() != getColour()
                    && piece.getType().equals(Type.PAWN)) {
                opponentsMoves.addAll(getEnemyPawnAttackPattern(piece));
                // If the piece colour is different & != 'King' or 'Pawn'
            } else if (piece.getColour() != getColour()) {
                opponentsMoves.addAll(piece.getLegalMoves(true));

            }
        });

        return opponentsMoves;
    }

    private ArrayList<Square> getEnemyPawnAttackPattern(Piece piece) {
        ArrayList<Square> attackPattern = new ArrayList<>();

        // Pawns move in different directions depending on colour
        double multiplier = piece.getColour().equals(Colour.WHITE) ? -getSize() : getSize();
        // Every pawn attack pattern
        attackPattern.add(Square.find((piece.getPosX() - getSize()), (piece.getPosY() + multiplier), getSize()));
        attackPattern.add(Square.find((piece.getPosX() + getSize()), (piece.getPosY() + multiplier), getSize()));
        attackPattern.removeIf(Objects::isNull); // Remove square if null (out of bounds)

        return attackPattern;
    }

    private ArrayList<Square> getCastlePattern() {
        ArrayList<Square> castlePattern = new ArrayList<>();

        // Evaluate Queen's side
        Piece queenSideRook = getBoard().getQueenSideRook(getColour());
        Square[] queenSideSquares = getColour().equals(Colour.WHITE)
                ? new Square[]{Square.B1, Square.C1, Square.D1}
                : new Square[]{Square.B8, Square.C8, Square.D8};
        if (hasMoved() || queenSideRook != null && queenSideRook.hasMoved()
                || getBoard().isSquareOccupied(queenSideSquares[0])
                || getBoard().isSquareOccupied(queenSideSquares[1])
                || getBoard().isSquareOccupied(queenSideSquares[2])) {
            // Do nothing
        } else if (queenSideRook != null) {
            Square castleSquare = getColour().equals(Colour.WHITE) ? Square.C1 : Square.C8;
            castlePattern.add(castleSquare);
        }

        // Evaluate King's side
        Piece kingSideRook = getBoard().getKingSideRook(getColour());
        Square[] kingSideSquares = getColour().equals(Colour.WHITE)
                ? new Square[]{Square.F1, Square.G1}
                : new Square[]{Square.F8, Square.G8};
        if (hasMoved() || kingSideRook != null && kingSideRook.hasMoved()
                || getBoard().isSquareOccupied(kingSideSquares[0])
                || getBoard().isSquareOccupied(kingSideSquares[1])) {
            // Do nothing
        } else if (kingSideRook != null) {
            Square castleSquare = getColour().equals(Colour.WHITE) ? Square.G1 : Square.G8;
            castlePattern.add(castleSquare);
        }

        return castlePattern;
    }

    private boolean attemptCastle() {
        if (getType().equals(Type.KING)) {
            Square kSquare = getColour().equals(Colour.WHITE)
                    ? Square.E1
                    : Square.E8;
            Square[] kPos = getColour().equals(Colour.WHITE)
                    ? new Square[]{Square.C1, Square.G1}
                    : new Square[]{Square.C8, Square.G8};
            Square[] rPos = getColour().equals(Colour.WHITE)
                    ? new Square[]{Square.D1, Square.F1}
                    : new Square[]{Square.D8, Square.F8};

            if (Objects.equals(getPreviousPosition(), kSquare)
                    && getPosition().equals(kPos[0])) {
                // Queen Side Rook
                Piece qSR = getBoard().getQueenSideRook(getColour());
                qSR.setPosition(rPos[0]);
                return true;
            } else if (Objects.equals(getPreviousPosition(), kSquare)
                    && getPosition().equals(kPos[1])) {
                // King Side Rook
                Piece kSR = getBoard().getKingSideRook(getColour());
                kSR.setPosition(rPos[1]);
                return true;
            }
        }

        return false;
    }
}
