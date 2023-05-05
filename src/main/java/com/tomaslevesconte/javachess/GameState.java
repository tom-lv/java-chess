package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.pieces.Piece;

import java.util.ArrayList;
import java.util.Objects;

public class GameState {

    private final Board board;
    private boolean isKingInCheck;
    private boolean isKingInCheckMate;
    private boolean isKingInStaleMate;

    public GameState(Board board) {
        this.board = board;
        isKingInCheck = false;
        isKingInCheckMate = false;
        isKingInStaleMate = false;
    }

    public void checkGameState() {
        isKingInCheck();
    }

    private void isKingInCheck() {
        PieceColour pC = board.isWhitesTurn() ? PieceColour.WHITE : PieceColour.BLACK;
        PieceColour oPC = board.isWhitesTurn() ? PieceColour.BLACK : PieceColour.WHITE;

        ArrayList<Square> opponentsMoves = board.getMoves(oPC);
        Piece king = board.getKing(pC);

        for (Square move : opponentsMoves) {
            if (king != null && move.equals(king.getSquare())) {
                isKingInCheck = true;
                System.out.println("King is in check!");
                isKingInCheckMate(king);
            } else {
                isKingInCheck = false;
            }
        }
    }

    private void isKingInCheckMate(Piece king) {
        PieceColour pieceColour = board.isWhitesTurn() ? PieceColour.WHITE : PieceColour.BLACK;

        ArrayList<Square> ownMoves = board.getMoves(pieceColour);
        Piece attacker = board.getAttacker(king);

        // Can it move/capture
        // Maybe filter out the king's attack in certain scenarios
        if (king.getLegalMoves().isEmpty()) {
            System.out.println("King cannot move.");
        } else {
            System.out.println("King can move.");
        }


        // Can it block
        for (Square move : ownMoves) {
            for (Square aP : Objects.requireNonNull(getAttackPath(king, attacker))) {
                if (move.equals(aP)) {
                    System.out.println("Can block attacker.");
                }
            }
        }

        // Can it capture (Piece other than king)
        for (Square move : ownMoves) {
            if (move.equals(attacker.getSquare())) {
                System.out.println("Can capture attacker.");
            }
        }

    }

    private ArrayList<Square> getAttackPath(Piece king, Piece attacker) {
        // You cannot block a knight, only capture or move
        if (attacker.getPieceType() != PieceType.KNIGHT) {
            ArrayList<Square> aP = new ArrayList<>();

            double kX = king.getPosX();
            double kY = king.getPosY();
            double aX = attacker.getPosX();
            double aY = attacker.getPosY();

            double diffX = (aX - kX);
            double diffY = (aY - kY);

            if (diffX == 0) {
                aP.addAll(getVerticalAttackPath(kX, kY, diffY));
            } else if (diffY == 0) {
                aP.addAll(getHorizontalAttackPath(kX, kY, diffX));
            } else {
                aP.addAll(getDiagonalAttackPath(kX, kY, diffX, diffY));
            }

            return aP;
        }

        return null;
    }

    private ArrayList<Square> getVerticalAttackPath(double kX, double kY, double diffY) {
        ArrayList<Square> aP = new ArrayList<>();

        while (diffY < 0) {
            kY -= board.getSquareSize();
            Square pS = Square.find(kX, kY, board.getSquareSize());
            if (diffY != -board.getSquareSize()) {
                aP.add(pS);
            }
            diffY = (diffY + board.getSquareSize());
        }

        while (diffY > 0) {
            kY += board.getSquareSize();
            Square pS = Square.find(kX, kY, board.getSquareSize());
            if (diffY != board.getSquareSize()) {
                aP.add(pS);
            }
            diffY = (diffY - board.getSquareSize());
        }

        return aP;
    }

    private ArrayList<Square> getHorizontalAttackPath(double kX, double kY, double diffX) {
        ArrayList<Square> aP = new ArrayList<>();

        while (diffX < 0) {
            kX -= board.getSquareSize();
            Square pS = Square.find(kX, kY, board.getSquareSize());
            if (diffX != -board.getSquareSize()) {
                aP.add(pS);
            }
            diffX = (diffX + board.getSquareSize());
        }

        while (diffX > 0) {
            kX += board.getSquareSize();
            Square pS = Square.find(kX, kY, board.getSquareSize());
            if (diffX != board.getSquareSize()) {
                aP.add(pS);
            }
            diffX = (diffX - board.getSquareSize());
        }

        return aP;
    }

    private ArrayList<Square> getDiagonalAttackPath(double kX, double kY, double diffX, double diffY) {
        ArrayList<Square> aP = new ArrayList<>();

        while (diffX < 0 && diffY > 0) {
            kX -= board.getSquareSize();
            kY += board.getSquareSize();
            Square pS = Square.find(kX, kY, board.getSquareSize());
            if (diffX != -board.getSquareSize()) {
                aP.add(pS);
            }
            diffX = (diffX + board.getSquareSize());
            diffY = (diffY - board.getSquareSize());
        }

        while (diffX > 0 && diffY > 0) {
            kX += board.getSquareSize();
            kY += board.getSquareSize();
            Square pS = Square.find(kX, kY, board.getSquareSize());
            if (diffX != board.getSquareSize()) {
                aP.add(pS);
            }
            diffX = (diffX - board.getSquareSize());
            diffY = (diffY - board.getSquareSize());
        }

        while (diffX > 0 && diffY < 0) {
            kX += board.getSquareSize();
            kY -= board.getSquareSize();
            Square pS = Square.find(kX, kY, board.getSquareSize());
            if (diffX != board.getSquareSize()) {
                aP.add(pS);
            }
            diffX = (diffX - board.getSquareSize());
            diffY = (diffY + board.getSquareSize());
        }

        while (diffX < 0 && diffY < 0) {
            kX -= board.getSquareSize();
            kY -= board.getSquareSize();
            Square pS = Square.find(kX, kY, board.getSquareSize());
            if (diffX != board.getSquareSize()) {
                aP.add(pS);
            }
            diffX = (diffX + board.getSquareSize());
            diffY = (diffY + board.getSquareSize());
        }

        return aP;
    }
}
