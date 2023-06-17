package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.Event;
import com.tomaslevesconte.javachess.enums.Colour;
import com.tomaslevesconte.javachess.enums.Type;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends Piece {

    private static final int MAX_SQUARE_ADVANCE = 2;

    private boolean inEnPassantState = false;

    public Pawn(Colour colour, Square square, Board board) {
        super(Type.PAWN, colour, square, MAX_SQUARE_ADVANCE, board);
        createPiece();
    }

    @Override
    public boolean move(Square newSquare) {
        setPreviousPosition(Square.find(getPosX(), getPosY(), getBoard().getSquareSize()));

        for (Square legalSquare : getBoard().getGame().getPossibleMoves(this)) {
            if (newSquare.equals(legalSquare)) {
                Event event = Event.MOVE;
                if (getBoard().isSquareOccupied(newSquare)
                        && getBoard().getPiece(newSquare).getColour() != getColour()) {
                    Piece target = getBoard().getPiece(newSquare);
                    target.capture(target);
                    event = Event.CAPTURE;
                }

                setInEnPassantState(newSquare);
                setPosition(newSquare);
                setHasMoved(true);
                event = attemptEnPassantCapture() ? Event.CAPTURE : event;
                getBoard().getGame().update(event); // Update game state
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

        double sqrSize = getBoard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = getColour().equals(Colour.WHITE) ? -sqrSize : sqrSize;

        // Every pawn move pattern
        movePattern.add(Square.find(getPosX(), getPosY() + multiplier, sqrSize));
        movePattern.add(Square.find(getPosX(), getPosY() + (multiplier * 2), sqrSize));

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

        double sqrSize = getBoard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = getColour().equals(Colour.WHITE) ? -sqrSize : sqrSize;

        // Every pawn attack pattern
        attackPattern.add(Square.find(getPosX() - sqrSize, getPosY() + multiplier, sqrSize));
        attackPattern.add(Square.find(getPosX() + sqrSize, getPosY() + multiplier, sqrSize));

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

        double sqrSize = getBoard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = getColour().equals(Colour.WHITE) ? -sqrSize : sqrSize;

        // In terms of x <-->
        double upX = getPosition().getX(sqrSize) + sqrSize;
        double downX = getPosition().getX(sqrSize) - sqrSize;

        // In terms of x <-->
        Square upSquare = Square.find(upX, getPosition().getY(sqrSize), sqrSize);
        Square downSquare = Square.find(downX, getPosition().getY(sqrSize), sqrSize);

        for (Piece piece : getBoard().getSpecificPieces(Type.PAWN)) {
            Pawn pawn = (Pawn) piece;
            if (pawn.isInEnPassantState()
                    && pawn.getPosition().equals(upSquare)) {
                Square moveSquare = Square.find(upX, (getPosY() + multiplier), sqrSize);
                enPassantPattern.add(moveSquare);
            } else if (pawn.isInEnPassantState()
                    && pawn.getPosition().equals(downSquare)) {
                Square moveSquare = Square.find(downX, (getPosY() + multiplier), sqrSize);
                enPassantPattern.add(moveSquare);
            }
        }

        enPassantPattern.removeIf(Objects::isNull);

        return enPassantPattern;
    }

    private boolean attemptEnPassantCapture() {
        double sqrSize = getBoard().getSquareSize();
        double multiplier = getColour().equals(Colour.WHITE) ? sqrSize : -sqrSize;

        Square behindSquare = Square.find(getPosX(), (getPosY() + multiplier), sqrSize);

        Pawn behindPawn = (Pawn) getBoard().getPiece(behindSquare);
        if (getBoard().isSquareOccupied(behindSquare)
                && behindPawn.getType().equals(Type.PAWN)
                && behindPawn.inEnPassantState) {
            behindPawn.capture(behindPawn);
            return true;
        }

        return false;
    }

    private void setInEnPassantState(Square newSquare) {
        double sqrSize = getBoard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = getColour().equals(Colour.WHITE) ? -sqrSize : sqrSize;
        double secondSquareY = getPosition().getY(sqrSize) + (multiplier * 2);

        Square secondSquare = Square.find(getPosX(), secondSquareY, sqrSize);

        this.inEnPassantState = !hasMoved() && newSquare.equals(secondSquare);
    }

    public boolean isInEnPassantState() {
        return inEnPassantState;
    }

    public void setInEnPassantState(boolean inEnPassantState) {
        this.inEnPassantState = inEnPassantState;
    }
}
