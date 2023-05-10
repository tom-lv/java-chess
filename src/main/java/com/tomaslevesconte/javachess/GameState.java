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

    public boolean isKingInStalemate() {
        return false;
    }

    private boolean canEvade() {
        ArrayList<Square> kMvs = king.getLegalMoves(true);

        if (kMvs.isEmpty() || kMvs.get(0).equals(attacker.getSquare())) {
            System.out.println("King cannot evade.");
            return false;
        } else {
            System.out.println("King can evade.");
        }

        return true;
    }

    private boolean canBlock() {
        ArrayList<Square> mvs = getMoves(PieceType.KING, currentColour);
        ArrayList<Square> aPs = Objects.requireNonNull(getAttackPath());

        for (Square mv : mvs) {
            for (Square aP : aPs) {
                if (mv.equals(aP)) {
                    System.out.println("Can block attacker.");
                    return true;
                }
            }
        }

        System.out.println("Cannot block attacker.");
        return false;
    }

    private boolean canCapture() {
        ArrayList<Square> mvs = getMoves(PieceType.KING, currentColour);
        ArrayList<Square> kMvs = getKingsMoves(currentColour);
        ArrayList<Square> uOMvs = getMovesUnfiltered(nextColour);

        for (Square mv : mvs) {
            if (mv.equals(attacker.getSquare())) {
                System.out.println("Can capture attacker.");
                return true;
            }
        }

        for (Square mv : kMvs) {
            if (mv.equals(attacker.getSquare())) {
                for (Square uMv : uOMvs) {
                    if (uMv.equals(attacker.getSquare())) {
                        System.out.println("Attacker is covered. King cannot capture.");
                        return false;
                    }
                }
                System.out.println("Attacker is not covered. King can capture.");
                return true;
            }
        }

        return false;
    }

    private Piece getKing(PieceColour pieceColour) {
        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceType().equals(PieceType.KING)
                    && piece.getPieceColour().equals(pieceColour)) {
                return piece;
            }
        }

        return null;
    }

    private Piece getAttacker() {
        for (Piece piece : board.getPieceList()) {
            for (Square move : piece.getLegalMoves(true)) {
                if (move.equals(king.getSquare())) {
                    return piece;
                }
            }
        }

        return null;
    }

    private ArrayList<Square> getMoves(PieceColour pieceColour) {
        ArrayList<Square> mvs = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)) {
                mvs.addAll(piece.getLegalMoves(true));
            }
        }

        return mvs;
    }

    private ArrayList<Square> getMoves(PieceType excludeType, PieceColour pieceColour) {
        ArrayList<Square> mvs = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)
                    && piece.getPieceType() != excludeType) {
                mvs.addAll(piece.getLegalMoves(true));
            }
        }

        return mvs;
    }

    private ArrayList<Square> getMovesUnfiltered(PieceColour pieceColour) {
        ArrayList<Square> uMvs = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)) {
                uMvs.addAll(piece.getLegalMoves(false));
            }
        }

        return uMvs;
    }

    private ArrayList<Square> getKingsMoves(PieceColour pieceColour) {
        ArrayList<Square> kMvs = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)
                    && piece.getPieceType().equals(PieceType.KING)) {
                kMvs.addAll(piece.getLegalMoves(true));
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
        double sqrSize = board.getSquareSize();

        while (diffY < 0) {
            kY -= sqrSize;
            Square pS = Square.find(kX, kY, sqrSize);
            if (diffY != -sqrSize) {
                aP.add(pS);
            }
            diffY = (diffY + sqrSize);
        }

        while (diffY > 0) {
            kY += sqrSize;
            Square pS = Square.find(kX, kY, sqrSize);
            if (diffY != sqrSize) {
                aP.add(pS);
            }
            diffY = (diffY - sqrSize);
        }

        return aP;
    }

    private ArrayList<Square> getHorizontalAttackPath(double kX, double kY, double diffX) {
        ArrayList<Square> aP = new ArrayList<>();
        double sqrSize = board.getSquareSize();

        while (diffX < 0) {
            kX -= sqrSize;
            Square pS = Square.find(kX, kY, sqrSize);
            if (diffX != -sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX + sqrSize);
        }

        while (diffX > 0) {
            kX += sqrSize;
            Square pS = Square.find(kX, kY, sqrSize);
            if (diffX != sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX - sqrSize);
        }

        return aP;
    }

    private ArrayList<Square> getDiagonalAttackPath(double kX, double kY, double diffX, double diffY) {
        ArrayList<Square> aP = new ArrayList<>();
        double sqrSize = board.getSquareSize();

        while (diffX < 0 && diffY > 0) {
            kX -= sqrSize;
            kY += sqrSize;
            Square pS = Square.find(kX, kY, sqrSize);
            if (diffX != -sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX + sqrSize);
            diffY = (diffY - sqrSize);
        }

        while (diffX > 0 && diffY > 0) {
            kX += sqrSize;
            kY += sqrSize;
            Square pS = Square.find(kX, kY, sqrSize);
            if (diffX != sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX - sqrSize);
            diffY = (diffY - sqrSize);
        }

        while (diffX > 0 && diffY < 0) {
            kX += sqrSize;
            kY -= sqrSize;
            Square pS = Square.find(kX, kY, sqrSize);
            if (diffX != sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX - sqrSize);
            diffY = (diffY + sqrSize);
        }

        while (diffX < 0 && diffY < 0) {
            kX -= sqrSize;
            kY -= sqrSize;
            Square pS = Square.find(kX, kY, sqrSize);
            if (diffX != sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX + sqrSize);
            diffY = (diffY + sqrSize);
        }

        return aP;
    }

    public boolean isWhitesTurn() {
        return isWhitesTurn;
    }
}
