package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.Event;
import com.tomaslevesconte.javachess.enums.Colour;
import com.tomaslevesconte.javachess.enums.Type;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends Piece {

    private static final int squaresItCanMove = 2;

    private boolean enPassant = false;

    public Pawn(Colour colour, Square square, Board board) {
        super(Type.PAWN, colour, board.getSquareSize(), square, squaresItCanMove, board);
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
                setEnPassant(newSquare);
                setPosition(newSquare);
                setHasMoved(true);
                event = attemptEnPassantCapture() ? Event.CAPTURE : event;
                getBoard().getGame().update(event);
                return true;
            }
        }

        return false;
    }

    @Override
    public ArrayList<Square> getLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();

        legalMoves.addAll(getMovePattern()); // Add move patterns
        legalMoves.addAll(getAttackPattern(false)); // Add attack patterns
        legalMoves.addAll(getEnPassantPattern());

        return legalMoves;
    }

    @Override
    public ArrayList<Square> getLegalMoves(boolean applyKingFilter) {
        ArrayList<Square> legalMoves = new ArrayList<>();

        legalMoves.addAll(getMovePattern()); // Add move patterns
        legalMoves.addAll(getAttackPattern(applyKingFilter)); // Add attack patterns
        legalMoves.addAll(getEnPassantPattern());

        return legalMoves;
    }

    private ArrayList<Square> getMovePattern() {
        ArrayList<Square> movePattern = new ArrayList<>();

        // Pawns move in different directions depending on colour
        double multiplier = getColour().equals(Colour.WHITE) ? -getSize() : getSize();
        // Every pawn move pattern
        movePattern.add(Square.find(getPosX(), getPosY() + multiplier, getSize()));
        movePattern.add(Square.find(getPosX(), getPosY() + (multiplier * 2), getSize()));

        // Remove if square !exist, or if square is occupied, or if square == 2nd square and pawn has moved
        movePattern.removeIf(moveSquare -> (moveSquare == null
                || getBoard().isSquareOccupied(moveSquare)
                || moveSquare.equals(movePattern.get(1))
                && getBoard().isSquareOccupied(movePattern.get(0))
                || moveSquare.equals(movePattern.get(1))
                && hasMoved()));

        return movePattern;
    }

    private ArrayList<Square> getAttackPattern(boolean applyKingFilter) {
        ArrayList<Square> attackPattern = new ArrayList<>();

        // Pawns move in different directions depending on colour
        double multiplier = getColour().equals(Colour.WHITE) ? -getSize() : getSize();
        // Every pawn attack pattern
        attackPattern.add(Square.find(getPosX() - getSize(), getPosY() + multiplier, getSize()));
        attackPattern.add(Square.find(getPosX() + getSize(), getPosY() + multiplier, getSize()));

        if (!applyKingFilter) {
            // Remove if square !exist, or if square is !occupied, or if square is occupied by the same colour
            attackPattern.removeIf(attackSquare -> (attackSquare == null // If null (out of bounds)
                    || !getBoard().isSquareOccupied(attackSquare)
                    || getBoard().isSquareOccupied(attackSquare)
                    && getBoard().getPiece(attackSquare).getColour().equals(getColour())));
        } else {
            attackPattern.removeIf(Objects::isNull);
        }

        return attackPattern;
    }

    private ArrayList<Square> getEnPassantPattern() {
        ArrayList<Square> enPassantPattern = new ArrayList<>();

        // Pawns move in different directions depending on colour
        double multiplier = getColour().equals(Colour.WHITE) ? -getSize() : getSize();
        // In terms of x <-->
        double upX = getPosition().getX(getSize()) + getSize();
        double downX = getPosition().getX(getSize()) - getSize();
        // In terms of x <-->
        Square upSquare = Square.find(upX, getPosition().getY(getSize()), getSize());
        Square downSquare = Square.find(downX, getPosition().getY(getSize()), getSize());

        for (Piece piece : getBoard().getSpecificPieces(Type.PAWN)) {
            Pawn pawn = (Pawn) piece;
            if (pawn.isEnPassant()
                    && pawn.getPosition().equals(upSquare)) {
                Square moveSquare = Square.find(upX, (getPosY() + multiplier), getSize());
                enPassantPattern.add(moveSquare);
            } else if (pawn.isEnPassant()
                    && pawn.getPosition().equals(downSquare)) {
                Square moveSquare = Square.find(downX, (getPosY() + multiplier), getSize());
                enPassantPattern.add(moveSquare);
            }
        }
        enPassantPattern.removeIf(Objects::isNull);

        return enPassantPattern;
    }

    private boolean attemptEnPassantCapture() {
        double multiplier = getColour().equals(Colour.WHITE) ? getSize() : -getSize();
        Square behindSquare = Square.find(getPosX(), (getPosY() + multiplier), getSize());

        Pawn behindPawn = (Pawn) getBoard().getPiece(behindSquare);
        if (getBoard().isSquareOccupied(behindSquare)
                && behindPawn.getType().equals(Type.PAWN)
                && behindPawn.enPassant) {
            behindPawn.capture(behindPawn);
            return true;
        }

        return false;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    private void setEnPassant(Square newSquare) {
        // Pawns move in different directions depending on colour
        double multiplier = getColour().equals(Colour.WHITE) ? -getSize() : getSize();

        double secondSquareY = getPosition().getY(getSize()) + (multiplier * 2);
        Square secondSquare = Square.find(getPosX(), secondSquareY, getSize());

        this.enPassant = !hasMoved() && newSquare.equals(secondSquare);
    }
}
