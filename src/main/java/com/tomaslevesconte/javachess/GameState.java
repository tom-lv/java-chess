package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;
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
        this.attacker = getAttacker(king);
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

        attacker = getAttacker(king);
        isWhitesTurn = !isWhitesTurn;
        System.out.println("Is King in checkmate: " + board.getGameState().isKingInCheckmate());
    }

    public boolean isKingInCheck() {
        ArrayList<Square> opponentsMoves = getMoves(nextColour);

        for (Square move : opponentsMoves) {
            if (king != null && move.equals(king.getSquare())) {
                System.out.println("King is in check!");
                // Remove moves that don't take king out of check
                return true;
            }
        }

        System.out.println("King is not in check!");
        return false;
    }

    // Gameover
    public boolean isKingInCheckmate() {
        return isKingInCheck() && !canEvade() && !canBlock() && !canCapture();
    }

    public boolean isKingInStalemate() {
        return false;
    }


    public boolean isPieceBlockingCheck(Piece piece) {
        ArrayList<Piece> attackers = getAttackers(piece);
        for (Piece attacker : attackers) {
            ArrayList<Square> attackPaths = getAttackPath(king, attacker);
            if (attacker.getPieceType().equals(PieceType.KNIGHT)) {
                // Do nothing
            } else if (!attackPaths.isEmpty()) {
                for (Square attackPath : attackPaths) {
                    if (board.isSquareOccupied(attackPath)
                            && attackPath != piece.getSquare()) {
                        return false;
                    }

                }
                // Remove moves that puts king in check
                return true;
            }
        }
        return false;
    }

    private boolean canEvade() {
        ArrayList<Square> kingsMoves = king.getLegalMoves();

        if (kingsMoves.isEmpty() || kingsMoves.get(0).equals(attacker.getSquare())) {
            System.out.println("King cannot evade.");
            return false;
        } else {
            System.out.println("King can evade.");
        }

        return true;
    }

    private boolean canBlock() {
        ArrayList<Square> moves = getMoves(PieceType.KING, currentColour);
        ArrayList<Square> attackPaths = Objects.requireNonNull(getAttackPath(king, attacker));

        for (Square move : moves) {
            for (Square attackPath : attackPaths) {
                if (move.equals(attackPath)) {
                    System.out.println("Can block attacker.");
                    return true;
                }
            }
        }

        System.out.println("Cannot block attacker.");
        return false;
    }

    private boolean canCapture() {
        ArrayList<Square> moves = getMoves(PieceType.KING, currentColour);
        ArrayList<Square> kingsMoves = getKingsMoves(currentColour);
        ArrayList<Square> unfilteredMoves = getMovesUnfiltered(nextColour);

        for (Square move : moves) {
            if (move.equals(attacker.getSquare())) {
                System.out.println("Can capture attacker.");
                return true;
            }
        }

        for (Square move : kingsMoves) {
            if (move.equals(attacker.getSquare())) {
                for (Square unfilteredMove : unfilteredMoves) {
                    if (unfilteredMove.equals(attacker.getSquare())) {
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

    private Piece getAttacker(Piece target) {
        for (Piece piece : board.getPieceList()) {
            for (Square move : piece.getLegalMoves()) {
                if (move.equals(target.getSquare())) {
                    return piece;
                }
            }
        }

        return null;
    }

    private ArrayList<Piece> getAttackers(Piece target) {
        ArrayList<Piece> attackers = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            for (Square move : piece.getLegalMoves()) {
                if (move.equals(target.getSquare())) {
                    attackers.add(piece);
                }
            }
        }

        return attackers;
    }

    private ArrayList<Square> getMoves(PieceColour pieceColour) {
        ArrayList<Square> moves = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)) {
                moves.addAll(piece.getLegalMoves());
            }
        }

        return moves;
    }

    private ArrayList<Square> getMoves(PieceType excludeType, PieceColour pieceColour) {
        ArrayList<Square> moves = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)
                    && piece.getPieceType() != excludeType) {
                moves.addAll(piece.getLegalMoves());
            }
        }

        return moves;
    }

    private ArrayList<Square> getMovesUnfiltered(PieceColour pieceColour) {
        ArrayList<Square> unfilteredMoves = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)) {
                unfilteredMoves.addAll(piece.getLegalMoves(false));
            }
        }

        return unfilteredMoves;
    }

    private ArrayList<Square> getKingsMoves(PieceColour pieceColour) {
        ArrayList<Square> kingsMoves = new ArrayList<>();

        for (Piece piece : board.getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)
                    && piece.getPieceType().equals(PieceType.KING)) {
                kingsMoves.addAll(piece.getLegalMoves());
            }
        }

        return kingsMoves;
    }

    private ArrayList<Square> getAttackPath(Piece target, Piece attacker) {
        // You cannot block a knight, only capture or move
        if (attacker.getPieceType() != PieceType.KNIGHT) {
            ArrayList<Square> aP = new ArrayList<>();

            double tX = target.getPosX();
            double tY = target.getPosY();
            double aX = attacker.getPosX();
            double aY = attacker.getPosY();

            double diffX = (aX - tX);
            double diffY = (aY - tY);

            if (diffX == 0) {
                aP.addAll(getVerticalAttackPath(tX, tY, diffY));
            } else if (diffY == 0) {
                aP.addAll(getHorizontalAttackPath(tX, tY, diffX));
            } else if (Math.round(Math.abs(diffX)) == (Math.round(Math.abs(diffY)))) {
                aP.addAll(getDiagonalAttackPath(tX, tY, diffX, diffY));
            }

            return aP;
        }

        return null;
    }

    private ArrayList<Square> getVerticalAttackPath(double tX, double tY, double diffY) {
        ArrayList<Square> aP = new ArrayList<>();
        double sqrSize = board.getSquareSize();

        while (diffY < 0) {
            tY -= sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffY != -sqrSize) {
                aP.add(pS);
            }
            diffY = (diffY + sqrSize);
        }

        while (diffY > 0) {
            tY += sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffY != sqrSize) {
                aP.add(pS);
            }
            diffY = (diffY - sqrSize);
        }

        return aP;
    }

    private ArrayList<Square> getHorizontalAttackPath(double tX, double tY, double diffX) {
        ArrayList<Square> aP = new ArrayList<>();
        double sqrSize = board.getSquareSize();

        while (diffX < 0) {
            tX -= sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != -sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX + sqrSize);
        }

        while (diffX > 0) {
            tX += sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX - sqrSize);
        }

        return aP;
    }

    private ArrayList<Square> getDiagonalAttackPath(double tX, double tY, double diffX, double diffY) {
        ArrayList<Square> aP = new ArrayList<>();
        double sqrSize = board.getSquareSize();

        while (diffX < 0 && diffY > 0) {
            tX -= sqrSize;
            tY += sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != -sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX + sqrSize);
            diffY = (diffY - sqrSize);
        }

        while (diffX > 0 && diffY > 0) {
            tX += sqrSize;
            tY += sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX - sqrSize);
            diffY = (diffY - sqrSize);
        }

        while (diffX > 0 && diffY < 0) {
            tX += sqrSize;
            tY -= sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != sqrSize) {
                aP.add(pS);
            }
            diffX = (diffX - sqrSize);
            diffY = (diffY + sqrSize);
        }

        while (diffX < 0 && diffY < 0) {
            tX -= sqrSize;
            tY -= sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
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
