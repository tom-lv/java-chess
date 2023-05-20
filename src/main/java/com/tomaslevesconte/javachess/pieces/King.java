package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Board;
import com.tomaslevesconte.javachess.enums.Event;
import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;

import java.util.ArrayList;
import java.util.Objects;

public class King extends Piece {

    private static final int MAX_SQUARE_ADVANCE = 1;

    public King(PieceColour pieceColour, Square square, Board board) {
        super(PieceType.KING, pieceColour, square, MAX_SQUARE_ADVANCE, board);
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

                updatePositionOnBoardAndList(newSquare);
                setHasMoved(true);
                event = attemptCastle() ? Event.CASTLE : event;
                getBoard().getGameState().update(event); // Update game state
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
            if (piece.getPieceColour() != getPieceColour()
                    && piece.getPieceType().equals(PieceType.KING)) {

                opponentsMoves.addAll(piece.getVerticalAttackPattern(false));
                opponentsMoves.addAll(piece.getHorizontalAttackPattern(false));
                opponentsMoves.addAll(piece.getDiagonalAttackPattern(false));

                // If the piece's colour is different & == 'Pawn'
            } else if (piece.getPieceColour() != getPieceColour()
                    && piece.getPieceType().equals(PieceType.PAWN)) {

                opponentsMoves.addAll(getEnemyPawnAttackPattern(piece));

                // If the piece colour is different & != 'King' or 'Pawn'
            } else if (piece.getPieceColour() != getPieceColour()) {

                opponentsMoves.addAll(piece.getLegalMoves(true));

            }
        });

        return opponentsMoves;
    }

    private ArrayList<Square> getEnemyPawnAttackPattern(Piece piece) {
        ArrayList<Square> attackPattern = new ArrayList<>();

        double sqrSize = getBoard().getSquareSize();
        // Pawns move in different directions depending on colour
        double multiplier = piece.getPieceColour().equals(PieceColour.WHITE) ? -sqrSize : sqrSize;

        // Every pawn attack pattern
        attackPattern.add(Square.find((piece.getPosX() - sqrSize), (piece.getPosY() + multiplier), sqrSize));
        attackPattern.add(Square.find((piece.getPosX() + sqrSize), (piece.getPosY() + multiplier), sqrSize));

        attackPattern.removeIf(Objects::isNull); // Remove square if null (out of bounds)

        return attackPattern;
    }

    private ArrayList<Square> getCastlePattern() {
        ArrayList<Square> castlePattern = new ArrayList<>();

        // Evaluate Queen's side
        Piece queenSideRook = getBoard().getQueenSideRook(getPieceColour());
        Square[] queenSideSquares = getPieceColour().equals(PieceColour.WHITE)
                ? new Square[]{Square.B1, Square.C1, Square.D1}
                : new Square[]{Square.B8, Square.C8, Square.D8};
        if (hasMoved() || queenSideRook != null && queenSideRook.hasMoved()
                || getBoard().isSquareOccupied(queenSideSquares[0])
                || getBoard().isSquareOccupied(queenSideSquares[1])
                || getBoard().isSquareOccupied(queenSideSquares[2])) {
            // Do nothing
        } else if (queenSideRook != null) {
            Square castleSquare = getPieceColour().equals(PieceColour.WHITE) ? Square.C1 : Square.C8;
            castlePattern.add(castleSquare);
        }

        // Evaluate King's side
        Piece kingSideRook = getBoard().getKingSideRook(getPieceColour());
        Square[] kingSideSquares = getPieceColour().equals(PieceColour.WHITE)
                ? new Square[]{Square.F1, Square.G1}
                : new Square[]{Square.F8, Square.G8};
        if (hasMoved() || kingSideRook != null && kingSideRook.hasMoved()
                || getBoard().isSquareOccupied(kingSideSquares[0])
                || getBoard().isSquareOccupied(kingSideSquares[1])) {
            // Do nothing
        } else if (kingSideRook != null) {
            Square castleSquare = getPieceColour().equals(PieceColour.WHITE) ? Square.G1 : Square.G8;
            castlePattern.add(castleSquare);
        }

        return castlePattern;
    }

    private boolean attemptCastle() {
        if (getPieceType().equals(PieceType.KING)) {
            Square kSquare = getPieceColour().equals(PieceColour.WHITE)
                    ? Square.E1
                    : Square.E8;
            Square[] kPos = getPieceColour().equals(PieceColour.WHITE)
                    ? new Square[]{Square.C1, Square.G1}
                    : new Square[]{Square.C8, Square.G8};
            Square[] rPos = getPieceColour().equals(PieceColour.WHITE)
                    ? new Square[]{Square.D1, Square.F1}
                    : new Square[]{Square.D8, Square.F8};

            if (Objects.equals(getLastSquare(), kSquare)
                    && getCurrentSquare().equals(kPos[0])) {
                // Queen Side Rook
                Piece qSR = getBoard().getQueenSideRook(getPieceColour());
                updateRookPositionOnBoardAndList(qSR, rPos[0]);
                return true;

            } else if (Objects.equals(getLastSquare(), kSquare)
                    && getCurrentSquare().equals(kPos[1])) {
                // King Side Rook
                Piece kSR = getBoard().getKingSideRook(getPieceColour());
                updateRookPositionOnBoardAndList(kSR, rPos[1]);
                return true;

            }
        }
        return false;
    }

    private void updateRookPositionOnBoardAndList(Piece rook, Square newSquare) {
        if (newSquare != null) {
            // Update visual pos on board
            rook.setLayoutX(newSquare.getX(getBoard().getSquareSize()));
            rook.setLayoutY(newSquare.getY(getBoard().getSquareSize()));
            // Update pos in list
            rook.setCurrentSquare(newSquare);
        }
    }
}
