package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.pieces.Piece;

import java.util.ArrayList;

public class GameState {

    private final Board board;
    private boolean isKingInCheck;
    private boolean isCheckMate;
    private boolean isStaleMate;

    public GameState(Board board) {
        this.board = board;
        isKingInCheck = false;
        isCheckMate = false;
        isStaleMate = false;
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

        // Get the piece which is putting the king in check and find the path it's using to do so
        // Can we capture that piece? can we block the attack? can we move out of the way?


        // Can it move/capture
        // Maybe filter out the king's attack in certain scenarios
        if (king.getLegalMoves().isEmpty()) {
            System.out.println("King cannot move.");
        } else {
            System.out.println("King can move.");
        }

        // If the king can capture, can it capture without putting itself in check?

        // Can it block

        // Can it capture
        for (Square move : ownMoves) {
            if (move.equals(attacker.getSquare())) {
                System.out.println("Can capture attacker.");
            }
        }

        getAttackPath(king, attacker);

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

            System.out.println("x: " + diffX + ", y: " + diffY);

            if (diffX == 0) {
                aP.addAll(getVerticalAttackPath(kX, kY, diffY));
            } else if (diffY == 0) {
                aP.addAll(getHorizontalAttackPath(kX, kY, diffX));
            } else {
                // Diagonal
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
                System.out.println(pS);
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
                System.out.println(pS);
                aP.add(pS);
            }
            diffX = (diffX + board.getSquareSize());
        }

        while (diffX > 0) {
            kX += board.getSquareSize();
            Square pS = Square.find(kX, kY, board.getSquareSize());
            if (diffX != board.getSquareSize()) {
                System.out.println(pS);
                aP.add(pS);
            }
            diffX = (diffX - board.getSquareSize());
        }

        return aP;
    }

    private ArrayList<Square> getDiagonalAttackPath() {
        ArrayList<Square> aP = new ArrayList<>();

        return aP;
    }
}
