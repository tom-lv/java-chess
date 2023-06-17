package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.enums.Event;
import com.tomaslevesconte.javachess.enums.Colour;
import com.tomaslevesconte.javachess.enums.Type;
import com.tomaslevesconte.javachess.enums.Square;
import com.tomaslevesconte.javachess.pieces.Pawn;
import com.tomaslevesconte.javachess.pieces.Piece;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.Objects;

public class Game {

    private static final String CASTLE_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/castle.mp3";
    private static final String MOVE_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/move-self.mp3";
    private static final String CAPTURE_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/capture.mp3";
    private static final String CHECK_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/move-check.mp3";
    private static final String START_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/game-start.mp3";
    private static final String END_SOUND_PATH = "/com/tomaslevesconte/javachess/sounds/game-end.mp3";

    private final Board board;
    private final UIComponents uiComponents;

    private boolean isWhitesTurn; // Switch
    private Colour activeColour; // The colour which can move
    private Colour waitingColour; // The colour in waiting

    public Game(Board board, UIComponents uiComponents) {
        this.board = board;
        this.uiComponents = uiComponents;
    }

    // Called at the start of the game
    public void start() {

        startSound().play();

        isWhitesTurn = true; // White gets first move
        activeColour = Colour.WHITE;
        waitingColour = Colour.BLACK;

        board.getPieceHandler().enablePieceEventHandler(activeColour); // Allow the player to move the white pieces
    }

    // Called after each move
    public void update(Event event) {
        if (isWhitesTurn) {
            activeColour = Colour.BLACK;
            waitingColour = Colour.WHITE;
        } else {
            activeColour = Colour.WHITE;
            waitingColour = Colour.BLACK;
        }
        isWhitesTurn = !isWhitesTurn;
        playAudio(event);

        removeEnPassantStatus(activeColour);
        board.getPieceHandler().disablePieceEventHandler(waitingColour);
        board.getPieceHandler().enablePieceEventHandler(activeColour);

        System.out.println("Is King in checkmate: " + board.getGame().isKingInCheckmate());
        System.out.println("Is King in stalemate: " + board.getGame().isKingInStalemate());
    }

    public boolean isKingInCheck() {
        uiComponents.removeKingInCheck();

        Piece king = board.getKing(activeColour);

        ArrayList<Square> opponentsMoves = board.getMoves(waitingColour);
        for (Square move : opponentsMoves) {
            if (king != null && move.equals(king.getPosition())) {
                uiComponents.displayKingInCheck(king);
                System.out.println("King is in check!");
                return true;
            }
        }
        System.out.println("King is not in check!");
        return false;
    }
    
    public boolean isKingInCheckmate() {
        Piece king = board.getKing(activeColour);

        if (isKingInCheck() && !canEvade() && !canBlock() && !canCapture()) {
            endSound().play();
            board.getPieceHandler().disablePieceEventHandler(activeColour);
            uiComponents.removeKingInCheck();
            uiComponents.displayKingInCheckmate(king);
            return true;
        } else {
            return false;
        }
    }

    public boolean isKingInStalemate() {

        Piece king = board.getKing(activeColour);

        double sqrSize = board.getSquareSize();
        ArrayList<Square> sSK = new ArrayList<>(); // Squares surrounding King

        sSK.add(Square.find(king.getPosX(), king.getPosY() + sqrSize, sqrSize));
        sSK.add(Square.find(king.getPosX() - sqrSize, king.getPosY() + sqrSize, sqrSize));
        sSK.add(Square.find(king.getPosX() - sqrSize, king.getPosY(), sqrSize));
        sSK.add(Square.find(king.getPosX() - sqrSize, king.getPosY() - sqrSize, sqrSize));
        sSK.add(Square.find(king.getPosX(), king.getPosY() - sqrSize, sqrSize));
        sSK.add(Square.find(king.getPosX() + sqrSize, king.getPosY() - sqrSize, sqrSize));
        sSK.add(Square.find(king.getPosX() + sqrSize, king.getPosY(), sqrSize));
        sSK.add(Square.find(king.getPosX() + sqrSize, king.getPosY() + sqrSize, sqrSize));
        sSK.removeIf(Objects::isNull);

        ArrayList<Square> opponentsMoves = new ArrayList<>();

        // For each piece on the board
        board.getPieceList().forEach(piece -> {
            // If the piece's colour is different & =='King'
            if (piece.getColour() != king.getColour()
                    && piece.getType().equals(Type.KING)) {
                opponentsMoves.addAll(piece.getVerticalAttackPattern(false));
                opponentsMoves.addAll(piece.getHorizontalAttackPattern(false));
                opponentsMoves.addAll(piece.getDiagonalAttackPattern(false));
                // If the piece's colour is different & == 'Pawn'
            } else if (piece.getColour() != king.getColour()
                    && piece.getType().equals(Type.PAWN)) {
                opponentsMoves.addAll(getEnemyPawnAttackPattern(piece));
                // If the piece colour is different & != 'King' or 'Pawn'
            } else if (piece.getColour() != king.getColour()) {
                opponentsMoves.addAll(piece.getLegalMoves(true));

            }
        });

        if (opponentsMoves.containsAll(sSK) && !isKingInCheck()) {
            endSound().play();
            board.getPieceHandler().disablePieceEventHandler(activeColour);
            uiComponents.displayKingInStaleMate(king);
            return true;
        }

        return false;
    }

    private ArrayList<Square> getEnemyPawnAttackPattern(Piece piece) {
        ArrayList<Square> attackPattern = new ArrayList<>();

        double sqrSize = board.getSquareSize();
        double multiplier = piece.getColour().equals(Colour.WHITE) ? -sqrSize : sqrSize;

        // Every pawn attack pattern
        attackPattern.add(Square.find((piece.getPosX() - sqrSize), (piece.getPosY() + multiplier), sqrSize));
        attackPattern.add(Square.find((piece.getPosX() + sqrSize), (piece.getPosY() + multiplier), sqrSize));

        attackPattern.removeIf(Objects::isNull); // Remove square if null (out of bounds)

        return attackPattern;
    }

    public boolean isPieceBlockingCheck(Piece piece) {

        for (Piece attacker : board.getAttackers(piece)) {

            // If the attacker != Knight (Knights can jump squares, so they don't have a set attack path)
            if (!attacker.getType().equals(Type.KNIGHT)) {

                int pieceCounter = 0; // Number of pieces in between the attacker and the king

                Piece blockingPiece = null; // The blocking piece (only matters if there is 1 blocking piece)

                ArrayList<Square> attackersPath = Objects.requireNonNull(getAttackPath(board.getKing(activeColour), attacker));

                for (Square attackSquare : attackersPath) {
                    // If a piece is on the attack path (regardless of colour)
                    if (board.isSquareOccupied(attackSquare)) {
                        blockingPiece = board.getPiece(attackSquare);
                        pieceCounter++;
                    }
                }

                // If there is only 1 piece blocking the attacker's path & if that piece is the selected piece,
                // then return true,
                // else false (because if there are multiple pieces and 1 of them moves, then there is still a blocking piece)
                return pieceCounter == 1 && blockingPiece.getPosition().equals(piece.getPosition());
            }
        }

        return false;
    }

    public ArrayList<Square> getPossibleMoves(Piece piece) {

        Piece king = board.getKing(activeColour);
        ArrayList<Square> legalMoves = new ArrayList<>(piece.getLegalMoves());
        ArrayList<Square> curatedMoves = new ArrayList<>();

        if (piece.getType().equals(Type.KING)) {
            return legalMoves;
        } else if (isPieceBlockingCheck(piece)) {
            ArrayList<Piece> attackers = board.getAttackers(piece);
            for (Piece attacker : attackers) {
                ArrayList<Square> aPath = Objects.requireNonNull(getAttackPath(king, attacker));
                for (Square lMove : legalMoves) {
                    for (Square aSquare : aPath) {
                        // If the move is on the attacker's path, or if it can capture the attacker
                        if (lMove.equals(aSquare) || lMove.equals(attacker.getPosition())) {
                            curatedMoves.add(lMove);
                        }
                    }
                }
            }
            return curatedMoves;
        } else if (isKingInCheck()) {
            Piece attacker = board.getAttacker(king);
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
                    if (legalMove.equals(Objects.requireNonNull(attacker).getPosition())) {
                        curatedMoves.add(legalMove);
                    }
                }
            }
            return curatedMoves;
        }

        return legalMoves;
    }

    private boolean canEvade() {
        Piece king = board.getKing(activeColour);
        Piece attacker = board.getAttacker(king);
        ArrayList<Square> kingsMoves = king.getLegalMoves();

        if (kingsMoves.isEmpty()
                || kingsMoves.get(0).equals(Objects.requireNonNull(attacker).getPosition())) {
            System.out.println("King cannot evade.");
            return false;
        } else {
            System.out.println("King can evade.");
        }

        return true;
    }

    private boolean canBlock() {
        Piece king = board.getKing(activeColour);
        Piece attacker = board.getAttacker(king);

        ArrayList<Square> moves = board.getMovesExceptKing(activeColour);
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
        Piece king = board.getKing(activeColour);
        Piece attacker = board.getAttacker(king);

        ArrayList<Square> moves = board.getMovesExceptKing(activeColour);
        for (Square move : moves) {
            if (move.equals(Objects.requireNonNull(attacker).getPosition())) {
                System.out.println("Can capture attacker.");
                return true;
            }
        }

        ArrayList<Square> kingsMoves = board.getKingsMoves(activeColour);
        ArrayList<Square> unfilteredMoves = board.getMovesUnfiltered(waitingColour);
        for (Square kingsMove : kingsMoves) {
            if (kingsMove.equals(Objects.requireNonNull(attacker).getPosition())) {
                for (Square unfilteredMove : unfilteredMoves) {
                    if (unfilteredMove.equals(attacker.getPosition())) {
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

    private void removeEnPassantStatus(Colour colour) {
        for (Piece piece : board.getSpecificPieces(Type.PAWN)) {
            Pawn pawn = (Pawn) piece;
            if (pawn.getColour().equals(colour)) {
                pawn.setInEnPassantState(false);
            }
        }
    }

    private void playAudio(Event event) {
        if (isKingInCheck()) {
            System.out.println("CHECK SOUND");
            checkSound().play();
        } else if (event.equals(Event.CAPTURE)) {
            captureSound().play();
        } else if (event.equals(Event.MOVE)){
            moveSound().play();
        } else if (event.equals(Event.CASTLE)) {
            castleSound().play();
        }
    }

    private ArrayList<Square> getAttackPath(Piece target, Piece attacker) {

        // You cannot block a knight, only capture or move
        if (attacker != null) {
            ArrayList<Square> aP = new ArrayList<>();

            double tX = target.getPosX();
            double tY = target.getPosY();

            double aX = attacker.getPosX();
            double aY = attacker.getPosY();

            double diffX = (aX - tX);
            double diffY = (aY - tY);

            if (diffX == 0 && attacker.getType() != Type.BISHOP) {
                aP.addAll(getVerticalAttackPath(tX, tY, diffY));
            } else if (diffY == 0 && attacker.getType() != Type.BISHOP) {
                aP.addAll(getHorizontalAttackPath(tX, tY, diffX));
            } else if (Math.round(Math.abs(diffX)) == (Math.round(Math.abs(diffY)))
                    && attacker.getType() != Type.ROOK) {
                aP.addAll(getDiagonalAttackPath(tX, tY, diffX, diffY));
            }

            return aP;
        }

        return null;
    }

    private ArrayList<Square> getVerticalAttackPath(double tX, double tY, double diffY) {

        double sqrSize = board.getSquareSize();

        ArrayList<Square> aPath = new ArrayList<>();

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

        double sqrSize = board.getSquareSize();

        ArrayList<Square> aPath = new ArrayList<>();

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

        double sqrSize = board.getSquareSize();

        ArrayList<Square> aPath = new ArrayList<>();

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
}
