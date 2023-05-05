package com.tomaslevesconte.javachess;

import com.tomaslevesconte.javachess.pieces.Piece;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Board {

    private static final byte TOTAL_NUM_OF_SQUARES = 64;
    private static final Color LIGHT_SQUARE_COLOUR = Color.web("#f0eef1"); // off-white #f0eef1, beach #F2D8B5
    private static final Color DARK_SQUARE_COLOUR = Color.web("#8877B3"); // purple #8877B3, orange #B78B64

    private boolean isWhitesTurn = true;

    private final double squareSize;
    private final AnchorPane anchorPane;
    private final ArrayList<Piece> pieceList = new ArrayList<>();
    private final GameState gameState;

    public Board(double boardSize, AnchorPane anchorPane) {
        this.squareSize = boardSize / Math.sqrt(TOTAL_NUM_OF_SQUARES);
        this.anchorPane = anchorPane;
        this.gameState = new GameState(this);
        createBoard();
        new PieceBuilder(this);
    }

    private void createBoard() {
        double x = 0;
        double y = 0;
        for (int i = 0; i < Math.sqrt(TOTAL_NUM_OF_SQUARES); i++) {
            for (int j = 0; j < Math.sqrt(TOTAL_NUM_OF_SQUARES); j++) {
                Rectangle rectangle = new Rectangle(x, y, squareSize, squareSize);
                rectangle.setSmooth(false); // Remove antialiasing
                if ((i + j) % 2 == 0) {
                    rectangle.setFill(Board.LIGHT_SQUARE_COLOUR);
                } else {
                    rectangle.setFill(Board.DARK_SQUARE_COLOUR);
                }
                getAnchorPane().getChildren().add(rectangle);
                x += rectangle.getWidth();
            }
            x = 0;
            y += squareSize;
        }
    }

    public void update() {
        if (isWhitesTurn) {
            System.out.println("White moved.");
        } else {
            System.out.println("Black moved.");
        }
        isWhitesTurn = !isWhitesTurn;
        gameState.checkGameState();
    }

    public double[] getPossibleXAndYCoordinates() {
        double[] possibleXAndYCoordinates = new double[(int) (Math.sqrt(Board.TOTAL_NUM_OF_SQUARES))];
        for (int i = 0; i < possibleXAndYCoordinates.length; i++) {
            possibleXAndYCoordinates[i] = squareSize * i;
        }
        return possibleXAndYCoordinates;
    }

    public double findClosestSquare(double input, double[] possibleCoordinates) {
        double result = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            if (input >= possibleCoordinates[i]
                    && input <= possibleCoordinates[i + 1]
                    || input < 0) {
                result = possibleCoordinates[i];
                break;
            } else if (input > possibleCoordinates[possibleCoordinates.length - 1]) {
                result = possibleCoordinates[possibleCoordinates.length - 1];
                break;
            }
        }
        return result;
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

    public boolean isWhitesTurn() {
        return isWhitesTurn;
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
}
