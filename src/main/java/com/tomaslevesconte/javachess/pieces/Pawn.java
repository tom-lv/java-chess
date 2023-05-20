package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.Event;
import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends Piece {

    private static final int MAX_SQUARE_ADVANCE = 2;

    private boolean inEnPassantState = false;

    public Pawn(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.PAWN, pieceColour, square, MAX_SQUARE_ADVANCE, board);
        createPiece();
    }

    @Override
    public boolean move(Square newSquare) {
        setLastSquare(Square.find(getPosX(), getPosY(), getBoard().getSquareSize()));

        for (Square legalSquare : getBoard().getGameState().curateMoves(this)) {
            if (newSquare.equals(legalSquare)) {
                Event event = Event.MOVE;
                if (getBoard().isSquareOccupied(newSquare)
                        && getBoard().getPiece(newSquare).getPieceColour() != getPieceColour()) {
                    Piece target = getBoard().getPiece(newSquare);
                    getBoard().getAnchorPane().getChildren().remove(target);
                    target.capture();
                    event = Event.CAPTURE;
                }

                setInEnPassantState(newSquare);
                updatePositionOnBoardAndList(newSquare);
                setHasMoved(true);
                event = attemptEnPassantCapture() ? Event.CAPTURE : event;
                getBoard().getGameState().update(event); // Update game state
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
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -sqrSize : sqrSize;

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
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -sqrSize : sqrSize;

        // Every pawn attack pattern
        attackPattern.add(Square.find(getPosX() - sqrSize, getPosY() + multiplier, sqrSize));
        attackPattern.add(Square.find(getPosX() + sqrSize, getPosY() + multiplier, sqrSize));

        if (!applyKingFilter) {
            // Remove if square !exist, or if square is !occupied, or if square is occupied by the same colour
            attackPattern.removeIf(attackSquare -> (attackSquare == null // If null (out of bounds)
                    || !getBoard().isSquareOccupied(attackSquare)
                    || getBoard().isSquareOccupied(attackSquare)
                    && getBoard().getPiece(attackSquare).getPieceColour().equals(getPieceColour())));
        } else {
            attackPattern.removeIf(Objects::isNull);
        }

        return attackPattern;
    }

    private ArrayList<Square> getEnPassantPattern() {
        ArrayList<Square> enPassantPattern = new ArrayList<>();

        double sqrSize = getBoard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -sqrSize : sqrSize;

        // In terms of x <-->
        double upX = getCurrentSquare().getX(sqrSize) + sqrSize;
        double downX = getCurrentSquare().getX(sqrSize) - sqrSize;

        // In terms of x <-->
        Square upSquare = Square.find(upX, getCurrentSquare().getY(sqrSize), sqrSize);
        Square downSquare = Square.find(downX, getCurrentSquare().getY(sqrSize), sqrSize);

        for (Pawn pawn : getBoard().getPawnList()) {
            if (pawn.isInEnPassantState()
                    && pawn.getCurrentSquare().equals(upSquare)) {
                Square moveSquare = Square.find(upX, (getPosY() + multiplier), sqrSize);
                enPassantPattern.add(moveSquare);
            } else if (pawn.isInEnPassantState()
                    && pawn.getCurrentSquare().equals(downSquare)) {
                Square moveSquare = Square.find(downX, (getPosY() + multiplier), sqrSize);
                enPassantPattern.add(moveSquare);
            }
        }

        enPassantPattern.removeIf(Objects::isNull);

        return enPassantPattern;
    }

    private boolean attemptEnPassantCapture() {
        double sqrSize = getBoard().getSquareSize();
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? sqrSize : -sqrSize;

        Square behindSquare = Square.find(getPosX(), (getPosY() + multiplier), sqrSize);

        Pawn behindPawn = (Pawn) getBoard().getPiece(behindSquare);
        if (getBoard().isSquareOccupied(behindSquare)
                && behindPawn.getPieceType().equals(PieceType.PAWN)
                && behindPawn.inEnPassantState) {
            getBoard().getAnchorPane().getChildren().remove(behindPawn);
            behindPawn.capture();
        }

        return false;
    }

    private void setInEnPassantState(Square newSquare) {
        double sqrSize = getBoard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = getPieceColour().equals(PieceColour.WHITE) ? -sqrSize : sqrSize;
        double secondSquareY = getCurrentSquare().getY(sqrSize) + (multiplier * 2);

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
