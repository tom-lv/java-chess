package com.tomaslevesconte.javachess.pieces;

import com.tomaslevesconte.javachess.Chessboard;
import com.tomaslevesconte.javachess.PieceColour;
import com.tomaslevesconte.javachess.PieceType;

import com.tomaslevesconte.javachess.Square;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public abstract class Piece extends Rectangle {

    private static final String IMAGE_PATH = "com/tomaslevesconte/javachess/pieces/";
    private static final String IMAGE_TYPE = ".png";
    
    private PieceType pieceType;
    private final PieceColour pieceColour;
    private double currentX;
    private double currentY;
    private final Chessboard chessboard;

    public Piece(PieceColour pieceColour, Square square, Chessboard chessboard) {
        this.pieceColour = pieceColour;
        this.currentX = square.getX(chessboard.getSquareSize());
        this.currentY = square.getY(chessboard.getSquareSize());
        this.chessboard = chessboard;
    }

    protected void createPiece() {
        setCursor(Cursor.OPEN_HAND);
        setWidth(chessboard.getSquareSize());
        setHeight(chessboard.getSquareSize());
        setLayoutX(currentX);
        setLayoutY(currentY);
        char knightInitial = pieceType.toString().toLowerCase().charAt(1);
        char allInitial = pieceType.toString().toLowerCase().charAt(0);
        char colourInitial = pieceColour.toString().toLowerCase().charAt(0);
        char typeInitial = getPieceType().equals(PieceType.KNIGHT) ? knightInitial : allInitial;
        setFill(new ImagePattern(new Image(IMAGE_PATH + colourInitial + typeInitial + IMAGE_TYPE)));
    }

    public boolean move(double newX, double newY) {
        boolean isMoveLegal = false;
        double x = Math.round(newX);
        double y = Math.round(newY);
        for (Square legalMove : getLegalMoves()) {
            double legalX = Math.round(legalMove.getX(getChessboard().getSquareSize()));
            double legalY = Math.round(legalMove.getY(getChessboard().getSquareSize()));
            if (x == legalX && y == legalY) {
                setCurrentX(newX);
                setCurrentY(newY);
                isMoveLegal = true;
                break;
            }
        }
        return isMoveLegal;
    }

    public abstract ArrayList<Square> getLegalMoves();

    public boolean isSquareOccupied(double squareX, double squareY) {
        boolean occupiedStatus = false;
        double argX = Math.round(squareX);
        double argY = Math.round(squareY);
        for (Piece piece : chessboard.getPiecePositions()) {
            double currentX = Math.round(piece.getCurrentX());
            double currentY = Math.round(piece.getCurrentY());
            if (argX == currentX && argY == currentY) {
                occupiedStatus = true;
                break;
            }
        }
        return occupiedStatus;
    }

    public double findNextYAxisSquare(double startSquareY) {
        int multiplier = pieceColour.equals(PieceColour.WHITE) ? -1 : 1;
        return findSquareForPawn(multiplier, startSquareY);
    }

    private double findSquareForPawn(int multiplier, double startSquareXY) {
        double[] possibleCoordinates = chessboard.getPossibleXAndYCoordinates();
        double targetSquareXY = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            int squareXY = (int) Math.round(startSquareXY);
            int arraySquareXY = (int) Math.round(possibleCoordinates[i]);
            if (squareXY == arraySquareXY && i > 0 && i < 7) {
                targetSquareXY = possibleCoordinates[i + multiplier];
                break;
            }
        }
        return targetSquareXY;
    }

    public double findNextYAxisSquare(boolean isUp, double startSquareY) {
        int multiplier = isUp ? -1 : 1;
        return findSquareForAll(multiplier, startSquareY);
    }

    public double findNextXAxisSquare(boolean isLeft, double startSquareX) {
        int multiplier = isLeft ? -1 : 1;
        return findSquareForAll(multiplier, startSquareX);
    }

    private double findSquareForAll(int multiplier, double startSquareXY) {
        double[] possibleCoordinates = chessboard.getPossibleXAndYCoordinates();
        double targetSquareXY = 0.0;
        for (int i = 0; i < possibleCoordinates.length; i++) {
            int squareXY = (int) Math.round(startSquareXY);
            int arraySquareXY = (int) Math.round(possibleCoordinates[i]);
            if (squareXY == arraySquareXY && i + multiplier > 0 && i + multiplier <= 7) {
                targetSquareXY = possibleCoordinates[i + multiplier];
                break;
            }
        }
        return targetSquareXY;
    }

    public double[] findNextDiagonal(boolean isUp, boolean isLeft, double[] startSquareXY) {
        int multiplierX = isLeft ? -1 : 1;
        int multiplierY = isUp ? -1 : 1;
        return findDiagonal(multiplierX, multiplierY, startSquareXY);
    }

    private double[] findDiagonal(int multiplierX, int multiplierY, double[] startSquareXY) {
        double nextSquareX = findSquareForAll(multiplierX, startSquareXY[0]);
        double nextSquareY = findSquareForAll(multiplierY, startSquareXY[1]);
        return new double[]{nextSquareX, nextSquareY};
    }

    public double getCurrentX() {
        return currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public PieceColour getPieceColour() {
        return pieceColour;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }
}
