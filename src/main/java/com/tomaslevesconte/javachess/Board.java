package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.enums.PieceColour;
import com.tomaslevesconte.javachess.enums.PieceType;
import com.tomaslevesconte.javachess.enums.Square;
import com.tomaslevesconte.javachess.pieces.Pawn;
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
    private final GameState gameState;
    private final PieceHandler pieceHandler;

    public Board(AnchorPane anchorPane, double boardSize) {
        this.anchorPane = anchorPane;
        this.squareSize = boardSize / 8;
        this.gameState = new GameState(this);
        createBoard(); // Create the board ui
        this.pieceHandler = new PieceHandler(this); // Initialise pieces and place them on the board
        gameState.start();
    }

    private void createBoard() {
        double x = 0.0f;
        double y = 0.0f;
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
            x = 0.0f;
            y += squareSize;
        }
    }

    public double findClosestSquare(double input) {
        double[] pC = getPossibleXAndYCoordinates();
        for (int i = 0; i < pC.length; i++) {
            if (input >= pC[i]
                    && input <= pC[i + 1]
                    || input < 0) {
                return pC[i];
            } else if (input > pC[pC.length - 1]) {
                return pC[pC.length - 1];
            }
        }
        return 0.0f;
    }

    public boolean isSquareOccupied(Square square) {
        for (Piece piece : getPieceList()) {
            Square pieceSquare = Square.find(
                    piece.getPosX(),
                    piece.getPosY(),
                    getSquareSize());
            if (square.equals(pieceSquare)) {
                return true;
            }
        }
        return false;
    }

    public Piece getPiece(Square square) {
        for (Piece piece : getPieceList()) {
            Square pieceSquare = Square.find(
                    piece.getPosX(),
                    piece.getPosY(),
                    getSquareSize());
            if (square.equals(pieceSquare)) {
                return piece;
            }
        }
        return null;
    }

    public int getPieceIndex(Square square) {
        int index = 0;
        for (Piece piece : getPieceList()) {
            Square pieceSquare = Square.find(
                    piece.getPosX(),
                    piece.getPosY(),
                    getSquareSize());
            if (square.equals(pieceSquare)) {
                return index;
            }
            index++;
        }
        return index;
    }

    public ArrayList<Pawn> getPawnList() {
        ArrayList<Pawn> pawnList = new ArrayList<>();
        for (Piece piece : getPieceList()) {
            if (piece.getPieceType().equals(PieceType.PAWN)) {
                Pawn currentPawn = (Pawn) piece;
                pawnList.add(currentPawn);
            }
        }
        return pawnList;
    }

    public Piece getQueenSideRook(PieceColour pieceColour) {
        if (pieceColour.equals(PieceColour.WHITE)
                && getPiece(Square.A1) != null
                && getPiece(Square.A1).getPieceType().equals(PieceType.ROOK)) {
            return getPiece(Square.A1);
        } else if (pieceColour.equals(PieceColour.BLACK)
                && getPiece(Square.A8) != null
                && getPiece(Square.A8).getPieceType().equals(PieceType.ROOK)) {
            return getPiece(Square.A8);
        } else {
            return null;
        }
    }

    public Piece getKingSideRook(PieceColour pieceColour) {
        if (pieceColour.equals(PieceColour.WHITE)
                && getPiece(Square.H1) != null
                && getPiece(Square.H1).getPieceType().equals(PieceType.ROOK)) {
            return getPiece(Square.H1);
        } else if (pieceColour.equals(PieceColour.BLACK)
                && getPiece(Square.H8) != null
                && getPiece(Square.H8).getPieceType().equals(PieceType.ROOK)) {
            return getPiece(Square.H8);
        } else {
            return null;
        }
    }

    private double[] getPossibleXAndYCoordinates() {
        double[] pXAYC = new double[8];
        for (int i = 0; i < pXAYC.length; i++) {
            pXAYC[i] = squareSize * i;
        }
        return pXAYC;
    }

    public GameState getGameState() {
        return gameState;
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
