package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.enums.Event;
import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;
import com.tomaslevesconte.javachess.pieces.Pawn;
import com.tomaslevesconte.javachess.pieces.Piece;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.Objects;

public class GameState {

    private static final String CASTLE_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/castle.mp3";
    private static final String MOVE_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/move-self.mp3";
    private static final String CAPTURE_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/capture.mp3";
    private static final String CHECK_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/move-check.mp3";
    private static final String START_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/game-start.mp3";
    private static final String END_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/game-end.mp3";

    private final Board board;
    private final UIComponents uiComponents;

    private Piece king;
    private boolean isWhitesTurn;
    private PieceColour currentColour;
    private PieceColour nextColour;

    public GameState(Board board) {
        this.board = board;
        this.uiComponents = new UIComponents(board);
        this.king = getKing(PieceColour.WHITE);
        this.isWhitesTurn = true;
        this.currentColour = PieceColour.WHITE;
        this.nextColour = PieceColour.BLACK;
    }

    public void start() {
        board.getPieceHandler().enablePieceEventHandler(currentColour);
        startSound().play();
    }

    public void update(Event event) {
        playAudio(event);

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

        toggleEnPassantStatus(currentColour);
        board.getPieceHandler().disablePieceEventHandler(nextColour);
        board.getPieceHandler().enablePieceEventHandler(currentColour);

        isWhitesTurn = !isWhitesTurn;
        System.out.println("Is King in checkmate: " + board.getGameState().isKingInCheckmate());
    }

    public boolean isKingInCheck() {
        ArrayList<Square> opponentsMoves = getMoves(nextColour);

        uiComponents.removeKingInCheck();

        for (Square move : opponentsMoves) {
            if (king != null && move.equals(king.getCurrentSquare())) {
                System.out.println("King is in check!");
                uiComponents.displayKingInCheck(king);
                return true;
            }
        }

        System.out.println("King is not in check!");
        return false;
    }
    
    public boolean isKingInCheckmate() {
        if (isKingInCheck() && !canEvade() && !canBlock() && !canCapture()) {
            endSound().play();
            board.getPieceHandler().disablePieceEventHandler(currentColour);
            uiComponents.removeKingInCheck();
            uiComponents.displayKingInCheckmate(king);

            return true;
        } else {
            return false;
        }
    }

    public boolean isKingInStalemate() {
        return false;
    }

    public boolean isPieceBlockingCheck(Piece piece) {
        ArrayList<Piece> attackers = getAttackers(piece);
        for (Piece attacker : attackers) {
            ArrayList<Square> aPath = Objects.requireNonNull(getAttackPath(king, attacker));
            if (!attacker.getPieceType().equals(PieceType.KNIGHT)) {
                int pieceCounter = 0;
                for (Square aSquare : aPath) {
                    if (board.isSquareOccupied(aSquare)) {
                        pieceCounter++;
                    }
                }
                return pieceCounter == 1;
            }
        }
        return false;
    }

    public ArrayList<Square> curateMoves(Piece piece) {
        ArrayList<Square> legalMoves = new ArrayList<>(piece.getLegalMoves());
        ArrayList<Square> curatedMoves = new ArrayList<>();

        if (piece.getPieceType().equals(PieceType.KING)) {
            return legalMoves;
        } else if (isPieceBlockingCheck(piece)) {
            ArrayList<Piece> attackers = getAttackers(piece);
            for (Piece attacker : attackers) {
                ArrayList<Square> aPath = Objects.requireNonNull(getAttackPath(king, attacker));
                for (Square lMove : legalMoves) {
                    for (Square aSquare : aPath) {
                        if (lMove.equals(aSquare)) {
                            curatedMoves.add(lMove);
                        }
                    }
                }
            }
            return curatedMoves;
        } else if (isKingInCheck()) {
            Piece attacker = getAttacker(king);
            if (canBlock()) {
                ArrayList<Square> aPath = Objects.requireNonNull(getAttackPath(king, attacker));
                for (Square legalMove : legalMoves) {
                    for (Square aSquare : aPath) {
                        if (legalMove.equals(aSquare)) {
                            curatedMoves.add(legalMove);
                        }
                    }
                }
            }
            if (canCapture()) {
                for (Square legalMove : legalMoves) {
                    if (legalMove.equals(Objects.requireNonNull(attacker).getCurrentSquare())) {
                        curatedMoves.add(legalMove);
                    }
                }
            }
            return curatedMoves;
        }

        return legalMoves;
    }

    private boolean canEvade() {
        Piece attacker = getAttacker(king);
        ArrayList<Square> kingsMoves = king.getLegalMoves();
        if (kingsMoves.isEmpty()
                || kingsMoves.get(0).equals(Objects.requireNonNull(attacker).getCurrentSquare())) {
            System.out.println("King cannot evade.");
            return false;
        } else {
            System.out.println("King can evade.");
        }

        return true;
    }

    private boolean canBlock() {
        Piece attacker = getAttacker(king);

        ArrayList<Square> moves = getMoves(PieceType.KING, currentColour);
        ArrayList<Square> attackPath = Objects.requireNonNull(getAttackPath(king, attacker));

        for (Square move : moves) {
            for (Square attackSquare : attackPath) {
                if (move.equals(attackSquare)) {
                    System.out.println("Can block attacker.");
                    return true;
                }
            }
        }

        System.out.println("Cannot block attacker.");
        return false;
    }

    private boolean canCapture() {
        Piece attacker = getAttacker(king);

        ArrayList<Square> moves = getMoves(PieceType.KING, currentColour);
        for (Square move : moves) {
            if (move.equals(Objects.requireNonNull(attacker).getCurrentSquare())) {
                System.out.println("Can capture attacker.");
                return true;
            }
        }

        ArrayList<Square> kingsMoves = getKingsMoves(currentColour);
        ArrayList<Square> unfilteredMoves = getMovesUnfiltered(nextColour);
        for (Square kingsMove : kingsMoves) {
            if (kingsMove.equals(Objects.requireNonNull(attacker).getCurrentSquare())) {
                for (Square unfilteredMove : unfilteredMoves) {
                    if (unfilteredMove.equals(attacker.getCurrentSquare())) {
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

    private void toggleEnPassantStatus(PieceColour pieceColour) {
        for (Pawn pawn : board.getPawnList()) {
            if (pawn.getPieceColour().equals(pieceColour)) {
                pawn.setInEnPassantState(false);
            }
        }
    }

    private void playAudio(Event event) {
        if (isKingInCheck()) {
            checkSound().play();
        } else if (event.equals(Event.CAPTURE)) {
            captureSound().play();
        } else if (event.equals(Event.MOVE)){
            moveSound().play();
        } else if (event.equals(Event.CASTLE)) {
            castleSound().play();
        }
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
                if (move.equals(target.getCurrentSquare())) {
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
                if (move.equals(target.getCurrentSquare())) {
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
        if (attacker != null && attacker.getPieceType() != PieceType.KNIGHT) {
            ArrayList<Square> aP = new ArrayList<>();

            double tX = target.getPosX();
            double tY = target.getPosY();
            double aX = attacker.getPosX();
            double aY = attacker.getPosY();

            double diffX = (aX - tX);
            double diffY = (aY - tY);

            if (diffX == 0 && attacker.getPieceType() != PieceType.BISHOP) {
                aP.addAll(getVerticalAttackPath(tX, tY, diffY));
            } else if (diffY == 0 && attacker.getPieceType() != PieceType.BISHOP) {
                aP.addAll(getHorizontalAttackPath(tX, tY, diffX));
            } else if (Math.round(Math.abs(diffX)) == (Math.round(Math.abs(diffY)))
                    && attacker.getPieceType() != PieceType.ROOK) {
                aP.addAll(getDiagonalAttackPath(tX, tY, diffX, diffY));
            }

            return aP;
        }

        return null;
    }

    private ArrayList<Square> getVerticalAttackPath(double tX, double tY, double diffY) {
        ArrayList<Square> aPath = new ArrayList<>();
        double sqrSize = board.getSquareSize();

        while (diffY < 0) {
            tY -= sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffY != -sqrSize) {
                aPath.add(pS);
            }
            diffY = (diffY + sqrSize);
        }

        while (diffY > 0) {
            tY += sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffY != sqrSize) {
                aPath.add(pS);
            }
            diffY = (diffY - sqrSize);
        }

        return aPath;
    }

    private ArrayList<Square> getHorizontalAttackPath(double tX, double tY, double diffX) {
        ArrayList<Square> aPath = new ArrayList<>();
        double sqrSize = board.getSquareSize();

        while (diffX < 0) {
            tX -= sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != -sqrSize) {
                aPath.add(pS);
            }
            diffX = (diffX + sqrSize);
        }

        while (diffX > 0) {
            tX += sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != sqrSize) {
                aPath.add(pS);
            }
            diffX = (diffX - sqrSize);
        }

        return aPath;
    }

    private ArrayList<Square> getDiagonalAttackPath(double tX, double tY, double diffX, double diffY) {
        ArrayList<Square> aPath = new ArrayList<>();
        double sqrSize = board.getSquareSize();

        while (diffX < 0 && diffY > 0) {
            tX -= sqrSize;
            tY += sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != -sqrSize) {
                aPath.add(pS);
            }
            diffX = (diffX + sqrSize);
            diffY = (diffY - sqrSize);
        }

        while (diffX > 0 && diffY > 0) {
            tX += sqrSize;
            tY += sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != sqrSize) {
                aPath.add(pS);
            }
            diffX = (diffX - sqrSize);
            diffY = (diffY - sqrSize);
        }

        while (diffX > 0 && diffY < 0) {
            tX += sqrSize;
            tY -= sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != sqrSize) {
                aPath.add(pS);
            }
            diffX = (diffX - sqrSize);
            diffY = (diffY + sqrSize);
        }

        while (diffX < 0 && diffY < 0) {
            tX -= sqrSize;
            tY -= sqrSize;
            Square pS = Square.find(tX, tY, sqrSize);
            if (diffX != sqrSize) {
                aPath.add(pS);
            }
            diffX = (diffX + sqrSize);
            diffY = (diffY + sqrSize);
        }

        return aPath;
    }

    private AudioClip startSound() {
        return new AudioClip(Objects.requireNonNull(getClass().getResource(START_SOUND_PATH)).toString());
    }

    private AudioClip endSound() {
        return new AudioClip(Objects.requireNonNull(getClass().getResource(END_SOUND_PATH)).toString());
    }

    private AudioClip moveSound() {
        return new AudioClip(Objects.requireNonNull(getClass().getResource(MOVE_SOUND_PATH)).toString());
    }

    private AudioClip captureSound() {
        return new AudioClip(Objects.requireNonNull(getClass().getResource(CAPTURE_SOUND_PATH)).toString());
    }

    private AudioClip checkSound() {
        return new AudioClip(Objects.requireNonNull(getClass().getResource(CHECK_SOUND_PATH)).toString());
    }

    private AudioClip castleSound() {
        return new AudioClip(Objects.requireNonNull(getClass().getResource(CASTLE_SOUND_PATH)).toString());
    }

    public boolean isWhitesTurn() {
        return isWhitesTurn;
    }
}
