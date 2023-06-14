package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;
import com.tomaslevesconte.javachess.pieces.Piece;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Board {

    private static final Color LIGHT_SQUARE_COLOUR = Color.web("#f0eef1"); // off-white #f0eef1, beach #F2D8B5
    private static final Color DARK_SQUARE_COLOUR = Color.web("#8877B3"); // purple #8877B3, orange #B78B64

    private final AnchorPane anchorPane;
    private final double squareSize;
    private final ArrayList<Piece> pieceList = new ArrayList<>();
    private final Game game;
    private final PieceHandler pieceHandler;

    public Board(AnchorPane anchorPane, double boardSize) {
        this.anchorPane = anchorPane;
        this.squareSize = boardSize / 8;
        this.game = new Game(this, new UIComponents(this));
        createBoard(); // Create the board ui
        this.pieceHandler = new PieceHandler(this); // Initialise pieces and place them on the board
        game.start();
    }

    private void createBoard() {
        double x = 0.0d;
        double y = 0.0d;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle rec = new Rectangle(x, y, squareSize, squareSize);
                rec.setSmooth(false); // Remove antialiasing
                if ((i + j) % 2 == 0) {
                    rec.setFill(Board.LIGHT_SQUARE_COLOUR);
                } else {
                    rec.setFill(Board.DARK_SQUARE_COLOUR);
                }
                getAnchorPane().getChildren().add(rec);
                x += rec.getWidth();
            }
            x = 0.0d;
            y += squareSize;
        }
    }

    public double findClosestSquare(double input) {
        double[] pC = getPossibleXAndYCoordinates();
        for (int i = 0; i < pC.length; i++) {
            if (input >= pC[i] && input <= pC[i + 1] || input < 0) {
                return pC[i];
            } else if (input > pC[pC.length - 1]) {
                return pC[pC.length - 1];
            }
        }
        return 0.0d;
    }

    public boolean isSquareOccupied(Square square) {
        for (Piece piece : getPieceList()) {
            Square pieceSquare = Square.find(piece.getPosX(), piece.getPosY(), getSquareSize());
            if (square.equals(pieceSquare)) {
                return true;
            }
        }
        return false;
    }

    public Piece getPieceOnSquare(Square square) {
        for (Piece piece : getPieceList()) {
            Square pieceSquare = Square.find(piece.getPosX(), piece.getPosY(), getSquareSize());
            if (square.equals(pieceSquare)) {
                return piece;
            }
        }
        return null;
    }

    public int getPieceIndex(Square square) {
        for (int i = 0; i < getPieceList().size(); i++) {
            Piece piece = getPieceList().get(i);
            Square pieceSquare = Square.find(piece.getPosX(), piece.getPosY(), getSquareSize());
            if (square.equals(pieceSquare)) {
                return i;
            }
        }
        return 0;
    }

    public Piece getSpecificPiece(PieceType pieceType) {
        for (Piece piece : getPieceList()) {
            if (piece.getPieceType().equals(pieceType)) {
                return piece;
            }
        }
        return null;
    }

    public ArrayList<Piece> getSpecificPieces(PieceType pieceType) {
        ArrayList<Piece> pieces = new ArrayList<>();
        for (Piece piece : getPieceList()) {
            if (piece.getPieceType().equals(pieceType)) {
                pieces.add(piece);
            }
        }
        return pieces;
    }

    public Piece getKing(PieceColour pieceColour) {
        for (Piece piece : getPieceList()) {
            if (piece.getPieceType().equals(PieceType.KING)
                    && piece.getPieceColour().equals(pieceColour)) {
                return piece;
            }
        }
        return null;
    }

    public Piece getAttacker(Piece target) {
        for (Piece piece : getPieceList()) {
            for (Square move : piece.getLegalMoves()) {
                if (move.equals(target.getCurrentSquare())) {
                    return piece;
                }
            }
        }
        return null;
    }

    public ArrayList<Piece> getAttackers(Piece target) {
        ArrayList<Piece> attackers = new ArrayList<>();
        for (Piece piece : getPieceList()) {
            for (Square move : piece.getLegalMoves()) {
                if (move.equals(target.getCurrentSquare())) {
                    attackers.add(piece);
                }
            }
        }
        return attackers;
    }

    public ArrayList<Square> getMoves(PieceColour pieceColour) {
        ArrayList<Square> moves = new ArrayList<>();
        for (Piece piece : getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)) {
                moves.addAll(piece.getLegalMoves());
            }
        }
        return moves;
    }

    public ArrayList<Square> getMovesExceptKing(PieceColour pieceColour) {
        ArrayList<Square> moves = new ArrayList<>();
        for (Piece piece : getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)
                    && piece.getPieceType() != PieceType.KING) {
                moves.addAll(piece.getLegalMoves());
            }
        }
        return moves;
    }

    public ArrayList<Square> getMovesUnfiltered(PieceColour pieceColour) {
        ArrayList<Square> unfilteredMoves = new ArrayList<>();
        for (Piece piece : getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)) {
                unfilteredMoves.addAll(piece.getLegalMoves(false));
            }
        }
        return unfilteredMoves;
    }

    public ArrayList<Square> getKingsMoves(PieceColour pieceColour) {
        ArrayList<Square> kingsMoves = new ArrayList<>();
        for (Piece piece : getPieceList()) {
            if (piece.getPieceColour().equals(pieceColour)
                    && piece.getPieceType().equals(PieceType.KING)) {
                kingsMoves.addAll(piece.getLegalMoves());
            }
        }
        return kingsMoves;
    }

    public Piece getQueenSideRook(PieceColour pieceColour) {
        if (pieceColour.equals(PieceColour.WHITE)
                && getPieceOnSquare(Square.A1) != null
                && getPieceOnSquare(Square.A1).getPieceType().equals(PieceType.ROOK)) {
            return getPieceOnSquare(Square.A1);
        } else if (pieceColour.equals(PieceColour.BLACK)
                && getPieceOnSquare(Square.A8) != null
                && getPieceOnSquare(Square.A8).getPieceType().equals(PieceType.ROOK)) {
            return getPieceOnSquare(Square.A8);
        } else {
            return null;
        }
    }

    public Piece getKingSideRook(PieceColour pieceColour) {
        if (pieceColour.equals(PieceColour.WHITE)
                && getPieceOnSquare(Square.H1) != null
                && getPieceOnSquare(Square.H1).getPieceType().equals(PieceType.ROOK)) {
            return getPieceOnSquare(Square.H1);
        } else if (pieceColour.equals(PieceColour.BLACK)
                && getPieceOnSquare(Square.H8) != null
                && getPieceOnSquare(Square.H8).getPieceType().equals(PieceType.ROOK)) {
            return getPieceOnSquare(Square.H8);
        } else {
            return null;
        }
    }

    private double[] getPossibleXAndYCoordinates() {
        double[] possibleXYCoordinates = new double[8];
        for (int i = 0; i < possibleXYCoordinates.length; i++) {
            possibleXYCoordinates[i] = squareSize * i;
        }
        return possibleXYCoordinates;
    }

    public Game getGameHandler() {
        return game;
    }

    public double getSquareSize() {
        return squareSize;
    }

    public ArrayList<Piece> getPieceList() {
        return pieceList;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public PieceHandler getPieceHandler() {
        return pieceHandler;
    }
}
