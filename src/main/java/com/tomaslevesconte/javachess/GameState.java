package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.pieces.Piece;

import java.util.ArrayList;
import java.util.Objects;

public class GameState {

    private final Board board;

    private Piece king;
    private Piece attacker;
    private boolean isWhitesTurn;
    private PieceColour currentColour;
    private PieceColour nextColour;

    public GameState(Board board) {
        this.board = board;
        this.king = getKing(PieceColour.WHITE);
        this.attacker = getAttacker();
        this.isWhitesTurn = true;
        this.currentColour = PieceColour.WHITE;
        this.nextColour = PieceColour.BLACK;
    }

    public void update() {
        if (isWhitesTurn) {
            System.out.println("Whites moved.");
            king = getKing(PieceColour.BLACK);
            currentColour = PieceColour.BLACK;
            nextColour = PieceColour.WHITE;
        } else {
            System.out.println("Blacks moved.");
            king = getKing(PieceColour.WHITE);
            currentColour = PieceColour.WHITE;
            nextColour = PieceColour.BLACK;
        }
        attacker = getAttacker();
        isWhitesTurn = !isWhitesTurn;
        System.out.println("Is King in checkmate: " + board.getGameState().isKingInCheckmate());
    }

    public boolean isKingInCheck() {
        ArrayList<Square> oMvs = getMoves(nextColour);

        for (Square mv : oMvs) {
            if (king != null && mv.equals(king.getSquare())) {
                System.out.println("King is in check!");
                return true;
            }
        }
        return false;
    }

    public boolean isKingInCheckmate() {
        return isKingInCheck() && !canEvade() && !canBlock() && !canCapture();
    }

    private boolean canEvade() {
        ArrayList<Square> kMvs = king.getLegalMoves();

        if (kMvs.isEmpty() || kMvs.get(0).equals(attacker.getSquare())) {
            System.out.println("King cannot evade.");
            return false;
        }

        return true;
    }

    private boolean canBlock() {
        ArrayList<Square> mvs = getMoves(PieceType.KING, currentColour);

        for (Square mv : mvs) {
            ArrayList<Square> aPs = Objects.requireNonNull(getAttackPath());
            for (Square aP : aPs) {
                if (mv.equals(aP)) {
                    System.out.println("Can block attacker.");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canCapture() {
        ArrayList<Square> mvs = getMoves(PieceType.KING, currentColour);
        ArrayList<Square> kMvs = getKingsMoves(currentColour);

        for (Square mv : mvs) {
            if (mv.equals(attacker.getSquare())) {
                System.out.println("Can capture attacker.");
                return true;
            }
        }

        for (Square mv : kMvs) {
            if (mv.equals(attacker.getSquare())) {
                System.out.println("King can capture attacker.");
                return true;
            }
        }

        return false;
    }

    public Piece getKing(PieceColour pieceColour) {
        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceType().equals(PieceType.KING)
                    && piece.getPieceColour().equals(pieceColour)) {
                return piece;
            }
        }
        return null;
    }

    public Piece getAttacker() {
        for (Piece piece : board.getPieceList()) {
            for (Square move : piece.getLegalMoves()) {
                if (move.equals(king.getSquare())) {
                    return piece;
                }
            }
        }
        return null;
    }

    public ArrayList<Square> getMoves(PieceColour pieceColour) {
        ArrayList<Square> mvs = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)) {
                mvs.addAll(piece.getLegalMoves());
            }
        }
        return mvs;
    }

    public ArrayList<Square> getMoves(PieceType excludeType, PieceColour pieceColour) {
        ArrayList<Square> mvs = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)
                    && piece.getPieceType() != excludeType) {
                mvs.addAll(piece.getLegalMoves());
            }
        }
        return mvs;
    }

    public ArrayList<Square> getKingsMoves(PieceColour pieceColour) {
        ArrayList<Square> kMvs = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)
                    && piece.getPieceType().equals(PieceType.KING)) {
                kMvs.addAll(piece.getLegalMoves());
            }
        }
        return kMvs;
    }

    private ArrayList<Square> getAttackPath() {
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
        double sSize = board.getSquareSize();

        while (diffY < 0) {
            kY -= sSize;
            Square pS = Square.find(kX, kY, sSize);
            if (diffY != -sSize) {
                aP.add(pS);
            }
            diffY = (diffY + sSize);
        }

        while (diffY > 0) {
            kY += sSize;
            Square pS = Square.find(kX, kY, sSize);
            if (diffY != sSize) {
                aP.add(pS);
            }
            diffY = (diffY - sSize);
        }

        return aP;
    }

    private ArrayList<Square> getHorizontalAttackPath(double kX, double kY, double diffX) {
        ArrayList<Square> aP = new ArrayList<>();
        double sSize = board.getSquareSize();

        while (diffX < 0) {
            kX -= sSize;
            Square pS = Square.find(kX, kY, sSize);
            if (diffX != -sSize) {
                aP.add(pS);
            }
            diffX = (diffX + sSize);
        }

        while (diffX > 0) {
            kX += sSize;
            Square pS = Square.find(kX, kY, sSize);
            if (diffX != sSize) {
                aP.add(pS);
            }
            diffX = (diffX - sSize);
        }

        return aP;
    }

    private ArrayList<Square> getDiagonalAttackPath(double kX, double kY, double diffX, double diffY) {
        ArrayList<Square> aP = new ArrayList<>();
        double sSize = board.getSquareSize();

        while (diffX < 0 && diffY > 0) {
            kX -= sSize;
            kY += sSize;
            Square pS = Square.find(kX, kY, sSize);
            if (diffX != -sSize) {
                aP.add(pS);
            }
            diffX = (diffX + sSize);
            diffY = (diffY - sSize);
        }

        while (diffX > 0 && diffY > 0) {
            kX += sSize;
            kY += sSize;
            Square pS = Square.find(kX, kY, sSize);
            if (diffX != sSize) {
                aP.add(pS);
            }
            diffX = (diffX - sSize);
            diffY = (diffY - sSize);
        }

        while (diffX > 0 && diffY < 0) {
            kX += sSize;
            kY -= sSize;
            Square pS = Square.find(kX, kY, sSize);
            if (diffX != sSize) {
                aP.add(pS);
            }
            diffX = (diffX - sSize);
            diffY = (diffY + sSize);
        }

        while (diffX < 0 && diffY < 0) {
            kX -= sSize;
            kY -= sSize;
            Square pS = Square.find(kX, kY, sSize);
            if (diffX != sSize) {
                aP.add(pS);
            }
            diffX = (diffX + sSize);
            diffY = (diffY + sSize);
        }

        return aP;
    }

    public boolean isWhitesTurn() {
        return isWhitesTurn;
    }
}
